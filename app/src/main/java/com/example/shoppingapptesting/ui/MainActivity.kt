package com.example.shoppingapptesting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoppingapptesting.R
import dagger.hilt.android.AndroidEntryPoint


/**
 * The hilt annotation @AndroidEntryPoint only annotate on Android component class,
 * other class like repository..tec doesn't need to annotate
 * */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}