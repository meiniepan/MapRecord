package com.solang.maprecord.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.solang.maprecord.R
import com.solang.maprecord.adapter.RoleInMapAdapter
import com.solang.maprecord.base.BaseActivity
import com.solang.maprecord.beans.RoleBean
import com.solang.maprecord.utils.Constant
import com.solang.maprecord.utils.SPreference
import kotlinx.android.synthetic.main.activity_map_detail.*

class MapDetailActivity : BaseActivity() {
    private lateinit var roleList: ArrayList<RoleBean>
    private lateinit var roleAdapter: RoleInMapAdapter
    private var isHlmm: String by SPreference(Constant.hlmm, "1")
    private var isMc: String by SPreference(Constant.mc, "1")
    private var isBwl: String by SPreference(Constant.bwl, "1")
    private var isZuge: String by SPreference(Constant.zuge, "1")
    private var isTaq: String by SPreference(Constant.taq, "1")
    private var isRaq: String by SPreference(Constant.raq, "1")
    private var isNaxx: String by SPreference(Constant.naxx, "1")
    var mapName: String = ""
    var currentPerson: String = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_map_detail
    }


    override fun initView() {
        super.initView()
        mapName = intent.getStringExtra("flag")
        currentPerson = intent.getStringExtra(Constant.CURRENT)
        roleList = intent.getParcelableArrayListExtra(Constant.ROLE_LIST)
        ctbTitle.setTitle(mapName)
        roleAdapter = RoleInMapAdapter(R.layout.item_role_in_map, roleList)
        rvRoles.apply {
            layoutManager = LinearLayoutManager(this@MapDetailActivity)

            adapter = roleAdapter
        }
        roleAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.cbItem) {
                SPreference.setContext(applicationContext, roleList[position].id!!)
                markMap(mapName, if (roleList[position].canPlay) "0" else "1")
                SPreference.setContext(applicationContext, currentPerson)
            }
        }
    }


    private fun markMap(name: String, isMark: String) {
        currentPerson
        when (name) {
            Constant.taq_name -> {
                if (isTaq != isMark) {
                    isTaq = isMark
                }
            }
            Constant.raq_name -> {
                if (isRaq != isMark) {
                    isRaq = isMark
                }
            }
            Constant.mc_name -> {
                if (isMc != isMark) {
                    isMc = isMark
                }
            }
            Constant.bwl_name -> {
                if (isBwl != isMark) {
                    isBwl = isMark
                }
            }
            Constant.naxx_name -> {
                if (isNaxx != isMark) {
                    isNaxx = isMark
                }
            }
            Constant.hlmm_name -> {
                if (isHlmm != isMark) {
                    isHlmm = isMark
                }
            }
            Constant.zuge_name -> {
                if (isZuge != isMark) {
                    isZuge = isMark
                }
            }
        }
    }

}