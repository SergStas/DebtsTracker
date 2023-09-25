package com.sergstas.debtstracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DebtsTracker)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}