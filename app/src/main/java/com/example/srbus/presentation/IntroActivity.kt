package com.example.srbus.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.srbus.R
import com.example.srbus.model.main.MainRepository

class IntroActivity : AppCompatActivity() {

    private val repository = MainRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        repository.getAllFavoriteStations()

    }
}