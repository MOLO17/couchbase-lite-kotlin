package com.molo17.couchbase.lite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.molo17.couchbase.lite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var views: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityMainBinding.inflate(layoutInflater)
        setContentView(views.root)
    }
}
