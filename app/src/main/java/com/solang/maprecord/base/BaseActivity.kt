package com.solang.maprecord.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import com.solang.maprecord.R
import com.solang.maprecord.utils.RevealUtil.circularFinishReveal
import com.solang.maprecord.utils.RevealUtil.setReveal
import com.solang.maprecord.utils.toast
import com.zackratos.ultimatebarx.library.UltimateBarX
import java.util.*


/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/22
 * Time: 19:56
 */
abstract class BaseActivity : AppCompatActivity() {

    lateinit var mRootView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        initStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        mRootView = (findViewById(android.R.id.content) as ViewGroup).getChildAt(0)
        initView()
        initData()
        if (showCreateReveal()) {
//            setUpReveal(savedInstanceState)
        }
    }


    open fun showCreateReveal(): Boolean = true

    open fun showDestroyReveal(): Boolean = false

    open fun initView() {}
    open fun initData() {}

    abstract fun getLayoutId(): Int

    open fun reLoad() {}



    fun setUpReveal(savedInstanceState: Bundle?) {
        setReveal(savedInstanceState)
    }

    /**
     *  设置返回
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onPause() {
        super.onPause()
        if (showDestroyReveal()) {
            circularFinishReveal(mRootView)
        }
    }

    override fun finish() {
        super.finish()
        if (showDestroyReveal()) {
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
        } else {
            overridePendingTransition(
                R.anim.animo_alph_open,
                R.anim.animo_alph_close
            )
        }
    }
    open fun initStatusBar() {
        UltimateBarX.with(this)
            .fitWindow(false)
            .color(Color.TRANSPARENT)
//            .drawableRes(R.drawable.bac_blue_bac_19)
            .light(false)
            .applyStatusBar()
    }

}