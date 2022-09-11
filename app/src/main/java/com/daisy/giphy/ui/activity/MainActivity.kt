package com.daisy.giphy.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.daisy.giphy.databinding.ActivityMainBinding
import com.daisy.giphy.ui.utils.ImageManager
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ImageManager(context = this)
    }
}