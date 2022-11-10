package com.solang.maprecord.ui

import android.os.CountDownTimer
import android.os.Handler
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.solang.maprecord.R
import com.solang.maprecord.adapter.ImageMarqueeAdapter
import com.solang.maprecord.base.BaseActivity
import com.solang.maprecord.beans.MapBean
import kotlinx.android.synthetic.main.activity_my_test.*

class MyTestActivity : BaseActivity() {
    lateinit var mAdapterFile: ImageMarqueeAdapter
    var mDataFile = ArrayList<MapBean>()
    private var timer: CountDownTimer? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_my_test
    }

    override fun initData() {

    }

    override fun initView() {
        mDataFile.add(MapBean(name = "1",isMark = ""))
        mDataFile.add(MapBean(name = "1",isMark = ""))
        mDataFile.add(MapBean(name = "1",isMark = ""))
        mDataFile.add(MapBean(name = "1",isMark = ""))
        mDataFile.add(MapBean(name = "1",isMark = ""))
        mDataFile.add(MapBean(name = "1",isMark = ""))
        mDataFile.add(MapBean(name = "1",isMark = ""))
        mDataFile.add(MapBean(name = "1",isMark = ""))
        mDataFile.add(MapBean(name = "1",isMark = ""))

        mAdapterFile = ImageMarqueeAdapter(R.layout.item_image_marquee, mDataFile)
        rvTest.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mAdapterFile.bindToRecyclerView(this)
        }
        //禁止手动滑动
        rvTest.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return true
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }
        })
        lifecycle.addObserver(mAdapterFile)
        tv1.setOnClickListener {
            rvTest.scrollToPosition(0)
        }
        tv2.setOnClickListener {
        }
    }

}