package com.kostyanchikoff.movenpublich

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kostynchikoff.core_application.utils.extensions.amountWithZeroDoOnTextChange
import com.kostynchikoff.core_application.utils.os.getOSVersion
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val androidOS = getOSVersion()
        textView.setOnClickListener {
            Log.e("name", "$androidOS")
        }

        editText.amountWithZeroDoOnTextChange {
            Log.e("CHECK", it)
        }

    }
}
