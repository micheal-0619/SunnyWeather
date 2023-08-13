package com.axb.sunnyweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.axb.sunnyweather.databinding.FragmentPlaceBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: FragmentPlaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
