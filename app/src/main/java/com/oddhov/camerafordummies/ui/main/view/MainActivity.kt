package com.oddhov.camerafordummies.ui.main.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.oddhov.camerafordummies.MainContract
import com.oddhov.camerafordummies.R

/**
 * Created by sammy on 08/11/2017.
 */

class MainActivity : AppCompatActivity(), MainContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }
}