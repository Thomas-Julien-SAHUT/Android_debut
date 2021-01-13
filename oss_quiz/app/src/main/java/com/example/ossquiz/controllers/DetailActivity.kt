package com.example.ossquiz.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.ossquiz.R
import kotlinx.android.synthetic.main.toolbar.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        configureToolBar()
    }
    fun configureToolBar(){
        setSupportActionBar(toolbar)
        val ab : ActionBar? = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }
}