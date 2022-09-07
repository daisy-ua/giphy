package com.daisy.data.repository.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.daisy.data.cache.dao.GIFDao
import com.daisy.data.cache.entities.GIFObjectEntity
import com.daisy.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.time.LocalDateTime
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val localSource: GIFDao,
) : ImageRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun cacheImage(
        context: Context,
        gifDrawable: GifDrawable,
        filename: String,
    ): String? {
        val path = cacheImageToInternalStorage(context, gifDrawable, filename)

        path?.let { url ->
            saveToLocalDatastore(filename, url)
        }

        return path
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun saveToLocalDatastore(filename: String, url: String) {
        withContext(Dispatchers.IO) {
            localSource.insertGIF(
                GIFObjectEntity(
                    uid = filename,
                    title = filename,
                    url = url,
                    dateAdded = LocalDateTime.now()
                )
            )
        }
    }

    private suspend fun cacheImageToInternalStorage(
        context: Context,
        gifDrawable: GifDrawable,
        filename: String,
    ): String? {
        return withContext(Dispatchers.IO) {
            val gifFile = createFile(context, filename)

            gifFile?.let { file ->
                val byteBuffer = gifDrawable.buffer

                val output = FileOutputStream(file)
                val bytes = ByteArray(byteBuffer.capacity())
                (byteBuffer.duplicate().clear() as ByteBuffer).get(bytes)
                output.write(bytes, 0, bytes.size)
                output.close()
            }

            gifFile?.path
        }
    }

    private fun createFile(context: Context, filename: String): File? {
        val dir = File(context.filesDir, MEDIA_DIR)

        if (!dir.exists()) {
            dir.mkdir();
        }

        val file = File(dir, filename)

        return if (file.exists()) null else file
    }
}

const val MEDIA_DIR = "giphy"