package com.solang.maprecord.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.solang.maprecord.R
import com.solang.maprecord.adapter.RoleAdapter
import com.solang.maprecord.base.BaseActivity
import com.solang.maprecord.beans.MapBean
import com.solang.maprecord.utils.*
import com.solang.maprecord.adapter.MapAdapter
import com.solang.maprecord.beans.MapRefreshBean
import com.solang.maprecord.beans.RoleBean
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : BaseActivity() {
    private var haveRole: Boolean = false
    private var changeList: ArrayList<Int> = ArrayList()
    private lateinit var roleAdapter: RoleAdapter

    private lateinit var data: ArrayList<HashMap<String, Any>>
    private lateinit var roleList: ArrayList<RoleBean>
    private var mExitTime: Long = 0
    var firstColor: Int = R.color.colorFs
    var firstImg: Int = R.mipmap.ic_fs

    var currentPerson = ""

    private var isHlmm: String by SPreference(Constant.hlmm, "1")
    private var isMc: String by SPreference(Constant.mc, "1")
    private var isBwl: String by SPreference(Constant.bwl, "1")
    private var isNaxx: String by SPreference(Constant.naxx, "1")
    private var isZuge: String by SPreference(Constant.zuge, "1")
    private var isTaq: String by SPreference(Constant.taq, "1")
    private var isRaq: String by SPreference(Constant.raq, "1")


    var emptyJson = Gson().toJson(ArrayList<RoleBean>())
    private var roleListJson: String by SPreference(Constant.ROLE_LIST, emptyJson)

    private var timeRefreshZuge: Long by SPreference(
        Constant.TIME_REFRESH_ZUGE,
        Constant.zuge_begin
    )
    private var timeRefreshMc: Long by SPreference(Constant.TIME_REFRESH_MC, Constant.mc_begin)
    private var timeRefreshHlmm: Long by SPreference(
        Constant.TIME_REFRESH_HLMM,
        Constant.hlmm_begin
    )
    private var timeRefreshBwl: Long by SPreference(Constant.TIME_REFRESH_BWL, Constant.bwl_begin)
    private var timeRefreshNaxx: Long by SPreference(Constant.TIME_REFRESH_NAXX, Constant.naxx_begin)
    private var timeRefreshTaq: Long by SPreference(Constant.TIME_REFRESH_TAQ, Constant.taq_begin)
    private var timeRefreshRaq: Long by SPreference(Constant.TIME_REFRESH_RAQ, Constant.raq_begin)
    lateinit var mAdapter: MapAdapter
    var mData: ArrayList<MapBean> = ArrayList()


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {

        initRoleData()
        initRoleTitle()
        ivAdd.setOnClickListener {
            showAddDialog()
        }

        mRvMaps?.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mAdapter = MapAdapter(R.layout.item_map, mData)
        mRvMaps.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, p ->

            if (haveRole) {
                enterMap(p)
            } else {
                toast("有角色后才可以进入哦")
            }

        }

        mAdapter.setOnItemLongClickListener { adapter, view, position ->
            if (haveRole) {
                onDelete(mData[position])
            } else {
                toast("有角色后才可以标记哦")
            }
            true
        }

    }

    private fun enterMap(p: Int) {
        mStartActivity<MapDetailActivity>(this) {
            putExtra("flag", mData[p].name)
            putExtra(Constant.CURRENT, currentPerson)
            var roList = ArrayList<RoleBean>()
            for (i in roleList) {
                SPreference.setContext(applicationContext, i.id!!)
                var hasPlayThis = "1"
                when (mData[p].name) {
                    Constant.taq_name -> {
                        hasPlayThis = isTaq
                    }
                    Constant.raq_name -> {
                        hasPlayThis = isRaq
                    }
                    Constant.mc_name -> {
                        hasPlayThis = isMc
                    }
                    Constant.bwl_name -> {
                        hasPlayThis = isBwl
                    }
                    Constant.naxx_name -> {
                        hasPlayThis = isNaxx
                    }
                    Constant.hlmm_name -> {
                        hasPlayThis = isHlmm
                    }
                    Constant.zuge_name -> {
                        hasPlayThis = isZuge
                    }
                }
                i.canPlay = hasPlayThis == "1"
                roList.add(i)
            }
            roList.sortBy { it.canPlay }
            roList.reverse()
            putExtra(Constant.ROLE_LIST, roList)
        }
        SPreference.setContext(applicationContext, currentPerson)
    }

    private fun initRoleTitle() {
        if (roleList.size > 0) {
            haveRole = true
            tvRoleMain.text = roleList[0].name
            tvAccountMain.text = roleList[0].account
            when (roleList[0].profession) {
                getRoleList()[0] -> {
                    firstColor = R.color.colorFs
                    firstImg = R.mipmap.ic_fs
                }
                getRoleList()[1] -> {
                    firstColor = R.color.colorXd
                    firstImg = R.mipmap.ic_xd
                }
                getRoleList()[2] -> {
                    firstColor = R.color.colorSs
                    firstImg = R.mipmap.ic_ss
                }
                getRoleList()[3] -> {
                    firstColor = R.color.colorMs
                    firstImg = R.mipmap.ic_ms
                }
                getRoleList()[4] -> {
                    firstColor = R.color.colorZs
                    firstImg = R.mipmap.ic_zs
                }
                getRoleList()[5] -> {
                    firstColor = R.color.colorSq
                    firstImg = R.mipmap.ic_sq
                }
                getRoleList()[6] -> {
                    firstColor = R.color.colorLr
                    firstImg = R.mipmap.ic_lr
                }
                getRoleList()[7] -> {
                    firstColor = R.color.colorDz
                    firstImg = R.mipmap.ic_dz
                }

                else -> {
                }
            }
            tvRoleMain.setTextColor(resources.getColor(firstColor))
            imgRoleMain.setImageResource(firstImg)
            llRole.setOnClickListener { showRoleDialog() }
        } else {
            imgRoleMain.setImageResource(R.mipmap.ic_emptyy)
            tvRoleMain.setTextColor(resources.getColor(R.color.white))
            haveRole = false
            tvRoleMain.text = "按右边的+添加角色"
            llRole.isClickable = false
        }
    }

    override fun initData() {
        if (haveRole) {
            setCurrentRole()
            SPreference.setContext(applicationContext, currentPerson)
        }
        initMapData()
    }

    private fun setCurrentRole(id: String = roleList[0].id!!) {
        currentPerson = id
    }

    private fun initRoleData() {

        getRoleInfoList()
    }

    private fun initMapData() {
        mData.clear()
        changeList.clear()
        mData.add(MapBean(Constant.taq_name, isTaq))
        changeList.add(0)
        mData.add(MapBean(Constant.raq_name, isRaq))
        changeList.add(1)
        mData.add(MapBean(Constant.mc_name, isMc))
        changeList.add(2)
        mData.add(MapBean(Constant.bwl_name, isBwl))
        changeList.add(3)
        mData.add(MapBean(Constant.hlmm_name, isHlmm))
        changeList.add(4)
        mData.add(MapBean(Constant.zuge_name, isZuge))
        changeList.add(5)
        mData.add(MapBean(Constant.naxx_name, isNaxx))
        changeList.add(6)
    }

    private fun refreshMapData() {
        changeList.clear()

        if (isTaq != mData[0].isMark) {
            changeList.add(0)
            mData.removeAt(0)
            mData.add(0, MapBean(Constant.taq_name, isTaq))
        }
        if (isRaq != mData[1].isMark) {
            changeList.add(1)
            mData.removeAt(1)
            mData.add(1, MapBean(Constant.raq_name, isRaq))
        }
        if (isMc != mData[2].isMark) {
            changeList.add(2)
            mData.removeAt(2)
            mData.add(2, MapBean(Constant.mc_name, isMc))
        }
        if (isBwl != mData[3].isMark) {
            changeList.add(3)
            mData.removeAt(3)
            mData.add(3, MapBean(Constant.bwl_name, isBwl))
        }
        if (isHlmm != mData[4].isMark) {
            changeList.add(4)
            mData.removeAt(4)
            mData.add(4, MapBean(Constant.hlmm_name, isHlmm))
        }
        if (isZuge != mData[5].isMark) {
            changeList.add(5)
            mData.removeAt(5)
            mData.add(5, MapBean(Constant.zuge_name, isZuge))
        }
        if (isNaxx != mData[6].isMark) {
            changeList.add(6)
            mData.removeAt(6)
            mData.add(6, MapBean(Constant.naxx_name, isNaxx))
        }
    }

    override fun onResume() {

        super.onResume()
        initRefreshCD()
        SPreference.setContext(applicationContext, currentPerson)
        refreshMapData()
        myNotify()
    }

    private fun myNotify() {
        for (i in changeList) {
            mAdapter.notifyItemChanged(i)
        }
        changeList.clear()
    }

    private fun initRefreshCD() {
        var map: ArrayList<MapRefreshBean> = ArrayList()
        map.add(MapRefreshBean(Constant.bwl_name, timeRefreshBwl, Constant.bwl_gap))
        map.add(MapRefreshBean(Constant.naxx_name, timeRefreshNaxx, Constant.naxx_gap))
        map.add(MapRefreshBean(Constant.mc_name, timeRefreshMc, Constant.mc_gap))
        map.add(
            MapRefreshBean(
                Constant.zuge_name,
                timeRefreshZuge,
                Constant.zuge_gap
            )
        )
        map.add(
            MapRefreshBean(
                Constant.hlmm_name,
                timeRefreshHlmm,
                Constant.hlmm_gap
            )
        )
        map.add(MapRefreshBean(Constant.raq_name, timeRefreshRaq, Constant.raq_gap))
        map.add(MapRefreshBean(Constant.taq_name, timeRefreshTaq, Constant.taq_gap))
        for (name in roleList) {
            doRefresh(name.id!!, map)
        }
    }

    private fun doRefresh(
        name: String,
        map: ArrayList<MapRefreshBean>
    ) {
        SPreference.setContext(applicationContext, name)
        for (bean in map) {
            if (System.currentTimeMillis() > bean.time) {
                markRefresh(bean)
                markMap(bean.name, "1")
            }
        }
    }

    private fun onDelete(get: MapBean) {
        // 底部弹出对话框
        val bottomDialog =
            Dialog(this, R.style.BottomDialog)
        val contentView: View =
            LayoutInflater.from(this).inflate(R.layout.dialog_edit_todo, null)
        bottomDialog.setContentView(contentView)
        val params = contentView.layoutParams as ViewGroup.MarginLayoutParams
        params.width =
            resources.displayMetrics.widthPixels - dp2Px(this, 200)
        params.bottomMargin = dp2Px(this, 8)
        contentView.layoutParams = params
        bottomDialog.window!!.setGravity(Gravity.CENTER)
        bottomDialog.show()
        contentView.findViewById<View>(R.id.tvMarkNo)
            .setOnClickListener { v: View? ->
                get.isMark = "1"
                markMap(get.name, "1")
                myNotify()
                bottomDialog.dismiss()
            }
        contentView.findViewById<View>(R.id.tvMark)
            .setOnClickListener { v: View? ->
                get.isMark = "0"
                markMap(get.name, "0")
                myNotify()
                bottomDialog.dismiss()
            }
    }

    private fun showRoleDialog() {
        // 顶部弹出对话框
        val bottomDialog =
            Dialog(this, R.style.BottomDialog)
        val contentView: View =
            LayoutInflater.from(this).inflate(R.layout.layout_role, null)
        bottomDialog.setContentView(contentView)
        val params = contentView.layoutParams as ViewGroup.MarginLayoutParams
        params.width =
            resources.displayMetrics.widthPixels - dp2Px(this, 16)
        params.bottomMargin = dp2Px(this, 8)
        contentView.layoutParams = params
        bottomDialog.window!!.setGravity(Gravity.TOP)
        bottomDialog.window!!.setWindowAnimations(R.style.TopDialog_Animation)
        var rvRole: RecyclerView = bottomDialog.findViewById(R.id.rvRole)
        rvRole?.layoutManager =
            LinearLayoutManager(this)
        roleAdapter = RoleAdapter(R.layout.spinner_item, roleList)
        rvRole.addItemDecoration(
            RecycleViewDivider(
                dp2Px(this, 1),
                resources.getColor(R.color.colorPrimary)
            )
        )
        rvRole?.adapter = roleAdapter
        roleAdapter.setOnItemClickListener { adapter, a, b ->
            tvRoleMain.text = roleList[b].name
            tvAccountMain.text = roleList[b].account

            when (roleList[b].profession) {
                getRoleList()[0] -> {
                    imgRoleMain.setImageResource(R.mipmap.ic_fs)
                    tvRoleMain.setTextColor(resources.getColor(R.color.colorFs))
                }
                getRoleList()[1] -> {
                    imgRoleMain.setImageResource(R.mipmap.ic_xd)
                    tvRoleMain.setTextColor(resources.getColor(R.color.colorXd))
                }
                getRoleList()[2] -> {
                    imgRoleMain.setImageResource(R.mipmap.ic_ss)
                    tvRoleMain.setTextColor(resources.getColor(R.color.colorSs))
                }
                getRoleList()[3] -> {
                    imgRoleMain.setImageResource(R.mipmap.ic_ms)
                    tvRoleMain.setTextColor(resources.getColor(R.color.colorMs))
                }
                getRoleList()[4] -> {
                    imgRoleMain.setImageResource(R.mipmap.ic_zs)
                    tvRoleMain.setTextColor(resources.getColor(R.color.colorZs))
                }
                getRoleList()[5] -> {
                    imgRoleMain.setImageResource(R.mipmap.ic_sq)
                    tvRoleMain.setTextColor(resources.getColor(R.color.colorSq))
                }
                getRoleList()[6] -> {
                    imgRoleMain.setImageResource(R.mipmap.ic_lr)
                    tvRoleMain.setTextColor(resources.getColor(R.color.colorLr))
                }
                getRoleList()[7] -> {
                    imgRoleMain.setImageResource(R.mipmap.ic_dz)
                    tvRoleMain.setTextColor(resources.getColor(R.color.colorDz))
                }

                else -> {
                }
            }

            setCurrentRole(roleList[b].id!!)
            SPreference.setContext(applicationContext, currentPerson)
            refreshMapData()
            myNotify()
            bottomDialog.dismiss()
        }

        roleAdapter.setOnItemLongClickListener { adapter, view, position ->
            AlertDialog.Builder(this)
                .setMessage("确定删除该角色？")
                .setTitle("提示")
                .setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i ->
                    roleList.removeAt(position)
                    saveRoleInfoList()
                    roleAdapter.notifyDataSetChanged()
                    initRoleTitle()
                })
                .setNeutralButton("取消", null)
                .create()
                .show()

            true
        }
        bottomDialog.show()
    }

    /*
    添加角色的对话框
     */
    private fun showAddDialog() {
        // 底部弹出对话框
        val bottomDialog =
            AlertDialog.Builder(this, R.style.BottomDialog)
        val contentView: View =
            LayoutInflater.from(this).inflate(R.layout.layout_add_role, null)
        bottomDialog.setView(contentView)

        val dialog = bottomDialog.show()
        var cc = getRoleList()
        var roleProfession: String = cc[0]
        var roleName: EditText = contentView.findViewById(R.id.et_name_role)
        var spinnerRole: Spinner = contentView.findViewById(R.id.spn_profession_role)
        var roleAccount: EditText = contentView.findViewById(R.id.et_account_role)
        var btnAdd: View = contentView.findViewById(R.id.tvAdd)
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, cc)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Set Adapter to Spinner
        spinnerRole!!.setAdapter(aa)
        spinnerRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                roleProfession = cc[p2]
            }
        }
        btnAdd.setOnClickListener {
            if (TextUtils.isEmpty(roleName.text) || TextUtils.isEmpty(
                    roleProfession
                )
            ) {
                toast("请填写角色名")
                return@setOnClickListener
            }
            var uuid = UUID.randomUUID().toString().replace("-", "")
            roleList.add(
                RoleBean(
                    uuid,
                    roleName.text.toString(),
                    roleProfession,
                    roleAccount.text.toString()
                )
            )
            initNewRoleRefreshTime(uuid)
            saveRoleInfoList()
            if (!haveRole) {
                setCurrentRole()
                SPreference.setContext(applicationContext, currentPerson)
                refreshMapData()
                myNotify()
                initRoleTitle()
            }
            toast("添加成功")
            dialog.dismiss()
        }
    }

    private fun initNewRoleRefreshTime(uuid: String) {
        var time1 = Constant.zuge_begin
        while (time1 < System.currentTimeMillis()) {
            time1 += Constant.zuge_gap
        }

        var time2 = Constant.mc_begin
        while (time2 < System.currentTimeMillis()) {
            time2 += Constant.mc_gap
        }

        var time3 = Constant.hlmm_begin
        while (time3 < System.currentTimeMillis()) {
            time3 += Constant.hlmm_gap
        }

        var time4 = Constant.bwl_begin
        while (time4 < System.currentTimeMillis()) {
            time4 += Constant.bwl_gap
        }

        var time5 = Constant.taq_begin
        while (time5 < System.currentTimeMillis()) {
            time5 += Constant.taq_gap
        }

        var time6 = Constant.raq_begin
        while (time6 < System.currentTimeMillis()) {
            time6 += Constant.raq_gap
        }

        var time7 = Constant.naxx_begin
        while (time7 < System.currentTimeMillis()) {
            time7 += Constant.naxx_gap
        }
        SPreference.setContext(this, uuid)
        timeRefreshZuge = time1
        timeRefreshMc = time2
        timeRefreshHlmm = time3
        timeRefreshBwl = time4
        timeRefreshTaq = time5
        timeRefreshRaq = time6
        timeRefreshNaxx = time7
        SPreference.setContext(this, currentPerson)
    }


    private fun markMap(name: String, isMark: String) {
        changeList.clear()
        currentPerson
        when (name) {
            Constant.taq_name -> {
                if (isTaq != isMark) {
                    isTaq = isMark
                    changeList.add(0)
                }
            }
            Constant.raq_name -> {
                if (isRaq != isMark) {
                    isRaq = isMark
                    changeList.add(1)
                }
            }
            Constant.mc_name -> {
                if (isMc != isMark) {
                    isMc = isMark
                    changeList.add(2)
                }
            }
            Constant.bwl_name -> {
                if (isBwl != isMark) {
                    isBwl = isMark
                    changeList.add(3)
                }
            }
            Constant.hlmm_name -> {
                if (isHlmm != isMark) {
                    isHlmm = isMark
                    changeList.add(4)
                }
            }
            Constant.zuge_name -> {
                if (isZuge != isMark) {
                    isZuge = isMark
                    changeList.add(5)
                }
            }
            Constant.naxx_name -> {
                if (isNaxx != isMark) {
                    isNaxx = isMark
                    changeList.add(6)
                }
            }
        }
    }

    private fun markRefresh(bean: MapRefreshBean) {
        //计算下一次的刷新时间
        var time2 = bean.time
        while (time2 < System.currentTimeMillis()) {
            time2 += bean.gap
        }
        when (bean.name) {
            Constant.hlmm_name -> {
                timeRefreshHlmm = time2
            }
            Constant.mc_name -> {
                timeRefreshMc = time2
            }
            Constant.bwl_name -> {
                timeRefreshBwl = time2
            }
            Constant.naxx_name -> {
                timeRefreshNaxx = time2
            }
            Constant.zuge_name -> {
                timeRefreshZuge = time2
            }
            Constant.raq_name -> {
                timeRefreshRaq = time2
            }
            Constant.taq_name -> {
                timeRefreshTaq = time2
            }
        }
    }

    fun getRoleInfoList() {
        SPreference.setContext(this, Constant.ROLE_TABLE)

        val resultType = object : TypeToken<ArrayList<RoleBean>>() {}.type
        val gson = Gson()
        roleList = gson.fromJson<ArrayList<RoleBean>>(roleListJson, resultType)
    }

    fun saveRoleInfoList() {
        SPreference.setContext(this, Constant.ROLE_TABLE)
        roleList.sortBy {
            it.account
        }
        roleListJson = Gson().toJson(roleList)
        SPreference.setContext(this, currentPerson)
    }

    override fun onBackPressed() {
        val time = System.currentTimeMillis()

        if (time - mExitTime > 2000) {
            toast(getString(R.string.exit_app))
            mExitTime = time
        } else {
            finish()
        }
    }
}