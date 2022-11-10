package com.solang.maprecord.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.solang.maprecord.beans.MapBean

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @CreateDate: 2020/5/1 16:34
 */

inline fun <reified T> mStartActivity(context: Context) {
    val intent = Intent(context, T::class.java)
    context.startActivity(intent)
}

inline fun <reified T> mStartActivity(context: Context, block: Intent.() -> Unit) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}

inline fun Context.toast(message: CharSequence): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }

fun dp2Px(context: Context, dp: Int): Int {
    val density: Float
    density = context.resources.displayMetrics.density
    return Math.round(dp.toFloat() * density)
}

fun dp2px(context: Context, dp: Float): Float {
    val density = context.resources.displayMetrics.density
    return (dp * density + 0.5).toFloat()
}

fun sp2px(context: Context, sp: Float): Float {
    val scaleDensity = context.resources.displayMetrics.scaledDensity
    return (sp * scaleDensity + 0.5).toFloat()
}

fun getRoleList(): ArrayList<String> {
    val wowData: ArrayList<String> = ArrayList()
    wowData.add("法师")
    wowData.add("德鲁伊")
    wowData.add("术士")
    wowData.add("牧师")
    wowData.add("战士")
    wowData.add("圣骑士")
    wowData.add("猎人")
    wowData.add("潜行者")
    return wowData
}


