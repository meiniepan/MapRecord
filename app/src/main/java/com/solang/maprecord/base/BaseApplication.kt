package com.solang.maprecord.base

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.solang.maprecord.utils.Constant
import com.solang.maprecord.utils.SPreference

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/22
 * Time: 14:27
 */
open class BaseApplication : Application() {
    companion object {
        lateinit var instance : BaseApplication
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        SPreference.setContext(applicationContext,"mm")
        initMode()

    }
    private fun initMode() {
    }
}