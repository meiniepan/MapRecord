package com.solang.maprecord.ui

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.solang.maprecord.R
import com.solang.maprecord.base.BaseActivity
import com.solang.maprecord.utils.mStartActivity
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * @author Burning
 * @description:
 * @date :2020/8/10 3:02 PM
 */
class SplashActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
    }
    /**
     * 初始化进场动画
     */
    override fun initView() {
        var handler = Handler()
        var runnable = Runnable { startIntent() }
        handler.postDelayed(runnable, stt_splash.getDurationTime())
        stt_splash.setOnClickListener {
            handler.removeCallbacks(runnable)
            startIntent()
        }
        startAnimatabe()
    }

    private fun startAnimatabe() {
        val animatedVectorDrawable =
            AnimatedVectorDrawableCompat.create(this, R.drawable.ic_ice_anim)
        iv.setImageDrawable(animatedVectorDrawable)
        val animatable = iv.drawable as Animatable
        animatable.start()
    }


    private fun startIntent() {
        mStartActivity<MainActivity>(this)
        finish()
    }


}