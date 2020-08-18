package com.solang.maprecord.ui

import android.app.AlertDialog
import android.app.Dialog
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
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
    private var isZuge: String by SPreference(Constant.zuge, "1")
    private var isTaq: String by SPreference(Constant.taq, "1")
    private var isRaq: String by SPreference(Constant.raq, "1")

    private var isRefreshZuge: Boolean by SPreference(Constant.IS_REFRESH_ZUGE, false)
    private var isRefreshMc: Boolean by SPreference(Constant.IS_REFRESH_MC, false)
    private var isRefreshHlmm: Boolean by SPreference(Constant.IS_REFRESH_HLMM, false)
    private var isRefreshBwl: Boolean by SPreference(Constant.IS_REFRESH_BWL, false)
    private var isRefreshTaq: Boolean by SPreference(Constant.IS_REFRESH_TAQ, false)
    private var isRefreshRaq: Boolean by SPreference(Constant.IS_REFRESH_RAQ, false)
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

        mSrlRefresh.setOnRefreshListener { mSrlRefresh.isRefreshing = false }
        mRvArticle?.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mAdapter = MapAdapter(R.layout.system_item, mData)
        mRvArticle.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, p ->
            mStartActivity<MapDetailActivity>(this){
                putExtra("flag", p)
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
            tvRoleMain.text = "按下方的+添加角色"
            llRole.isClickable = false
        }
    }

    override fun initData() {
        if (haveRole) {
            setCurrentRole()
            SPreference.setContext(applicationContext, currentPerson)
        }
        initSpinner()
        initMapData()
    }

    private fun setCurrentRole(id: String = roleList[0].id) {
        currentPerson = id
    }

    private fun initRoleData() {
//        roleList.add(RoleBean("Triste", "fs"))
//        roleList.add(RoleBean("Serafina", "fs"))
//        roleList.add(RoleBean("婴寕", "fs"))
//        roleList.add(RoleBean("婴宁小兔", "xd"))
//        roleList.add(RoleBean("倾婴", "ms"))
//        roleList.add(RoleBean("倾婴", "ss"))
//        roleList.add(RoleBean("婴宁兔", "sq"))
        getRoleInfoList()
    }

    private fun initSpinner() {
//        val intArr: IntArray = intArrayOf(R.id.img, R.id.tv1)
//        var simp_adapter = SimpleAdapter(
//            this, getData(), R.layout.spinner_item, arrayOf("img", "tv1"),
//            intArr
//        );
//        //Adapter设置一个下拉列表样式(上一步只是一个下拉列表框（不包括下拉菜单），这里要设置下拉菜单的样式)
//        simp_adapter.setDropDownViewResource(R.layout.spinner_item);
//
//        //Set Adapter to Spinner
//        spinner.setAdapter(simp_adapter)
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                currentPerson = data[p2].get("tv1").toString()
//                SPreference.setContext(applicationContext,currentPerson )
////                Glide.with(this@MainActivity)
////                    .load(data[p2].get("img"))
////                    .transform(GlideCircleBorderTransform(1f,R.color.white))
////                    .into(img)
//                initMapData()
//                mAdapter.notifyDataSetChanged()
//            }
//        }
    }

    private fun initMapData() {
        mData.clear()
        mData.add(MapBean(Constant.taq_name, isTaq))

        mData.add(MapBean(Constant.raq_name, isRaq))

        mData.add(MapBean(Constant.mc_name, isMc))

        mData.add(MapBean(Constant.bwl_name, isBwl))

        mData.add(MapBean(Constant.hlmm_name, isHlmm))

        mData.add(MapBean(Constant.zuge_name, isZuge))

    }

    override fun onResume() {
        super.onResume()
        initRefreshCD()
        SPreference.setContext(applicationContext, currentPerson)
        initMapData()
        mAdapter.notifyDataSetChanged()
    }


    private fun initRefreshCD() {
        var map: ArrayList<MapRefreshBean> = ArrayList()
        map.add(MapRefreshBean(Constant.bwl_name, timeRefreshBwl, isRefreshBwl, Constant.bwl_gap))
        map.add(MapRefreshBean(Constant.mc_name, timeRefreshMc, isRefreshMc, Constant.mc_gap))
        map.add(
            MapRefreshBean(
                Constant.zuge_name,
                timeRefreshZuge,
                isRefreshZuge,
                Constant.zuge_gap
            )
        )
        map.add(
            MapRefreshBean(
                Constant.hlmm_name,
                timeRefreshHlmm,
                isRefreshHlmm,
                Constant.hlmm_gap
            )
        )
        map.add(MapRefreshBean(Constant.raq_name, timeRefreshRaq, isRefreshRaq, Constant.raq_gap))
        map.add(MapRefreshBean(Constant.taq_name, timeRefreshTaq, isRefreshTaq, Constant.taq_gap))
        for (name in roleList) {
            doRefresh(name.name, map)
        }
    }

    private fun doRefresh(
        name: String,
        map: ArrayList<MapRefreshBean>
    ) {
        SPreference.setContext(applicationContext, name)
        for (bean in map) {
            if (!bean.isRefresh) {
                if (System.currentTimeMillis() > bean.time) {
                    markRefresh(bean)

                    markMap(bean.name, "1")
                }
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
            resources.displayMetrics.widthPixels - dp2Px(this, 16)
        params.bottomMargin = dp2Px(this, 8)
        contentView.layoutParams = params
        bottomDialog.window!!.setGravity(Gravity.CENTER)
        bottomDialog.window!!.setWindowAnimations(R.style.BottomDialog_Animation)
        bottomDialog.show()
        contentView.findViewById<View>(R.id.tvMarkNo)
            .setOnClickListener { v: View? ->
                get.isMark = "1"
                markMap(get.name, "1")
                mAdapter.notifyDataSetChanged()
                bottomDialog.dismiss()
            }
        contentView.findViewById<View>(R.id.tvMark)
            .setOnClickListener { v: View? ->
                get.isMark = "0"
                markMap(get.name, "0")
                mAdapter.notifyDataSetChanged()
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

            setCurrentRole(roleList[b].id)
            SPreference.setContext(applicationContext, currentPerson)

            initMapData()
            mAdapter.notifyDataSetChanged()
            bottomDialog.dismiss()
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
            if (TextUtils.isEmpty(roleName.text) || TextUtils.isEmpty(roleAccount.text) || TextUtils.isEmpty(
                    roleProfession
                )
            ) {
                toast("请填写完整")
                return@setOnClickListener
            }
            roleList.add(
                RoleBean(
                    UUID.randomUUID().toString().replace("-", ""),
                    roleName.text.toString(),
                    roleProfession,
                    roleAccount.text.toString()
                )
            )
            saveRoleInfoList()
            if (!haveRole) {
                setCurrentRole()
                SPreference.setContext(applicationContext, currentPerson)
                initMapData()
                mAdapter.notifyDataSetChanged()
                initRoleTitle()
            }
            toast("添加成功")
            dialog.dismiss()
        }
    }


    private fun markMap(name: String, isMark: String) {
        currentPerson
        when (name) {
            Constant.hlmm_name -> {

                isHlmm = isMark

            }
            Constant.mc_name -> {
                isMc = isMark

            }
            Constant.bwl_name -> {
                isBwl = isMark

            }
            Constant.zuge_name -> {
                isZuge = isMark

            }
            Constant.raq_name -> {
                isRaq = isMark

            }
            Constant.taq_name -> {
                isTaq = isMark

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
                isRefreshHlmm = false
            }
            Constant.mc_name -> {
                timeRefreshMc = time2
                isRefreshMc = false
            }
            Constant.bwl_name -> {
                timeRefreshBwl = time2
                isRefreshBwl = false
            }
            Constant.zuge_name -> {
                timeRefreshZuge = time2
                isRefreshZuge = false
            }
            Constant.raq_name -> {
                timeRefreshRaq = time2
                isRefreshRaq = false
            }
            Constant.taq_name -> {
                timeRefreshTaq = time2
                isRefreshTaq = false
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
        roleListJson = Gson().toJson(roleList)
        roleList.sortBy {
            it.account
        }
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