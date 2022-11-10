package com.solang.maprecord.utils

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/24
 * Time: 17:36
 */
object Constant {

    const val ROLE_TABLE = "role_table"
    const val ROLE_LIST = "role_list"
    const val CURRENT = "current_person"

    const val IS_REFRESH_ZUGE = "is_refresh_zuge"
    const val IS_REFRESH_MC = "is_refresh_mc"
    const val IS_REFRESH_BWL = "is_refresh"
    const val IS_REFRESH_HLMM = "is_refresh"
    const val IS_REFRESH_RAQ = "is_refresh"
    const val IS_REFRESH_TAQ = "is_refresh"

    const val TIME_REFRESH_ZUGE = "time_refresh_zuge"
    const val TIME_REFRESH_MC = "time_refresh_mc"
    const val TIME_REFRESH_BWL = "time_refresh_bwl"
    const val TIME_REFRESH_HLMM = "time_refresh_hlmm"
    const val TIME_REFRESH_RAQ = "time_refresh_raq"
    const val TIME_REFRESH_TAQ = "time_refresh_taq"
    const val TIME_REFRESH_NAXX = "time_refresh_naxx"

    const val NIGHT_MODE = "night_mode"
    const val zuge = "zuge"
    const val mc = "mc"
    const val bwl = "bwl"
    const val IS_INIT_ROLE = "is_init_role"
    const val hlmm = "hlmm"
    const val taq = "taq"
    const val naxx = "naxx"
    const val raq = "raq"

    const val zuge_name = "祖尔格拉布"
    const val mc_name = "熔火之心"
    const val bwl_name = "黑翼之巢"
    const val naxx_name = "纳克萨玛斯"
    const val hlmm_name = "奥妮克希亚的巢穴"
    const val taq_name = "安其拉神殿"
    const val raq_name = "安其拉废墟"

    const val zuge_begin :Long = 1597532400L *1000 //8月19 07
    const val mc_begin:Long = 1597100400L *1000//8月18 07
    const val bwl_begin:Long = 1597100400L *1000
    const val naxx_begin:Long = 1597100400L *1000
    const val hlmm_begin:Long = 1597446000L *1000
    const val taq_begin:Long = 1597100400L *1000
    const val raq_begin:Long = 1597532400L *1000

    const val zuge_gap:Long = 3*24*60*60 *1000
    const val mc_gap:Long = 7*24*60*60 *1000
    const val bwl_gap:Long= 7*24*60*60 *1000
    const val naxx_gap:Long= 7*24*60*60 *1000
    const val hlmm_gap:Long = 5*24*60*60 *1000
    const val taq_gap:Long = 7*24*60*60 *1000
    const val raq_gap:Long = 3*24*60*60 *1000

}