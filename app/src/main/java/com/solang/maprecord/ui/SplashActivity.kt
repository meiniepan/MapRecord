package com.solang.maprecord.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import com.solang.maprecord.R
import com.solang.maprecord.utils.mStartActivity

import kotlinx.android.synthetic.main.activity_splash.*
import java.security.Permissions

/**
 * @author Burning
 * @description:
 * @date :2020/8/10 3:02 PM
 */
class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var handler = Handler()
        var runnable = Runnable { initView() }
        handler.postDelayed(runnable,stt_splash.getDurationTime())
        stt_splash.setOnClickListener {
            handler.removeCallbacks(runnable)
            initView()
        }
    }

    /**
     * 初始化进场动画
     */
    private fun initView() {
        startIntent()
    }


    private fun startIntent() {
        mStartActivity<MainActivity>(this)
        finish()
    }



}