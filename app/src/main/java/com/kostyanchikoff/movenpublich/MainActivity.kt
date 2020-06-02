package com.kostyanchikoff.movenpublich

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kostynchikoff.core_application.di.createRetrofitOkHttpClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        textView.setOnClickListener {
            createRetrofitOkHttpClient(this)
        }

    }
}
