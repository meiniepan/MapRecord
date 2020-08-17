package com.solang.maprecord.ui

import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SimpleAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.solang.maprecord.R
import com.solang.maprecord.adapter.RoleAdapter
import com.solang.maprecord.base.BaseActivity
import com.solang.maprecord.beans.MapBean
import com.solang.maprecord.utils.*
import com.solang.maprecord.adapter.SystemAdapter
import com.solang.maprecord.beans.MapRefreshBean
import com.solang.maprecord.beans.RoleBean
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_role.*

class MainActivity : BaseActivity() {
    private lateinit var roleAdapter: RoleAdapter

    private lateinit var data: ArrayList<HashMap<String, Any>>
    private var mExitTime: Long = 0

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
    lateinit var mAdapter: SystemAdapter
    var mData: ArrayList<MapBean> = ArrayList()
    var mRoleData: ArrayList<RoleBean> = ArrayList()


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        currentPerson = mRoleData[0].name
        SPreference.setContext(applicationContext, currentPerson)
        initSpinner()
        initMapData()
//        mData.add("安其拉")

    }

    private fun initRoleData() {
        mRoleData.add(RoleBean("Triste", "fs"))
        mRoleData.add(RoleBean("Serafina", "fs"))
        mRoleData.add(RoleBean("婴寕", "fs"))
        mRoleData.add(RoleBean("婴宁小兔", "xd"))
        mRoleData.add(RoleBean("倾婴", "ms"))
        mRoleData.add(RoleBean("倾婴", "ss"))
        mRoleData.add(RoleBean("婴宁兔", "sq"))
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

    override fun initView() {
        initRoleData()
        tvRoleMain.text = mRoleData[0].name
        tvRoleMain.setTextColor(resources.getColor(R.color.colorFs))
        imgRoleMain.setImageResource(R.mipmap.ic_fs)
        llRole.setOnClickListener { showRoleDialog() }
        mSrlRefresh.setOnRefreshListener { mSrlRefresh.isRefreshing = false }
        mRvArticle?.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mAdapter = SystemAdapter(R.layout.system_item, mData)
        mRvArticle.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, _ ->
            mStartActivity<MapDetailActivity>(this)
        }

        mAdapter.setOnItemLongClickListener { adapter, view, position ->
            onDelete(mData[position])
            true
        }

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
        for (name in mRoleData) {
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
        // 底部弹出对话框
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
        roleAdapter = RoleAdapter(R.layout.spinner_item, mRoleData)
        rvRole?.adapter = roleAdapter
        roleAdapter.setOnItemClickListener { adapter, a, b ->
            tvRoleMain.text = mRoleData[b].name

            when (mRoleData[b].role) {
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

            currentPerson = mRoleData[b].name
            SPreference.setContext(applicationContext, currentPerson)

            initMapData()
            mAdapter.notifyDataSetChanged()
            bottomDialog.dismiss()
        }
        bottomDialog.show()
    }


    private fun markMap(name: String, isMark: String) {
//        for (bean in mData){
//            if (bean.name == name){
//                bean.isMark = isMark
//            }
//        }
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