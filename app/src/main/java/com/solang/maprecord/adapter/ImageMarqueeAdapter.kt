package com.solang.maprecord.adapter

import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.solang.maprecord.R
import com.solang.maprecord.beans.MapBean


/**
 * Created with Android Studio.
 * Description:
 * @author: Burning
 * @date: 2020/08/27
 * Time: 17:32
 */
class ImageMarqueeAdapter(layoutId: Int, listData: MutableList<MapBean>?) :
    BaseQuickAdapter<MapBean, BaseViewHolder>(layoutId, listData), LifecycleObserver {
    private var timer: CountDownTimer? = null
    var mWidth = 0
    var mParentWidth = 0
    var xScrolled = 0
    lateinit var ll: View
    var paused = false

    override fun convert(viewHolder: BaseViewHolder?, item: MapBean?) {
        viewHolder?.let { holder ->
            ll = holder.getView(R.id.item_ll)

            if (holder.adapterPosition == 0) {
                holder.setBackgroundRes(R.id.item_image, R.drawable.ic_baoxiu)
            } else if (holder.adapterPosition == mData.size - 1) {
                holder.setBackgroundRes(R.id.item_image, R.drawable.ic_renwu)
            } else {
                holder.setBackgroundRes(R.id.item_image, R.drawable.ic_tonggao)
            }
        }
    }


    fun realScroll() {
        Log.e("realScroll", "realScroll")
        Handler().postDelayed({
            scrollToEnd()
        }, 2000)
    }

    fun scrollToEnd() {
        timer?.start()
    }

    private fun initTimer() {
        Log.e("initTimer===", "initTimer")
        if (timer == null) {
            //单个item宽度
            mWidth = ll.width + ll.marginLeft + ll.marginRight//单个item宽度
            //recyclerView宽度
            mParentWidth = recyclerView.width - recyclerView.paddingLeft - recyclerView.paddingRight
            //如果可以显示完整，返回
            if (mData.size * mWidth < mParentWidth) {
                return
            }
            //需要滚动的总长度
            var xTotalScroll = mWidth * mData.size - mParentWidth + 50L
            //滚动间隔
            var interval = 50L
            //每次滚动距离
            var step = 20
            //总共滚动时间
            var time = xTotalScroll / step * interval
            timer = object : CountDownTimer(time, interval) {
                override fun onFinish() {
                    Handler().postDelayed({
                        //重置滚动状态
                        xScrolled = 0
                        recyclerView.scrollToPosition(0)
                    }, 1000)
                    realScroll()
                }

                override fun onTick(p0: Long) {
                    recyclerView.scrollBy(step, 0)
                    xScrolled += step
                }
            }
            realScroll()
        }
    }

    fun stopWork() {
        Log.e("onDetached===", recyclerView.toString())
        timer?.cancel()
        timer = null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        //初始化跑马灯
        recyclerView.post {
            initTimer()
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        //停止计时器
        stopWork()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.e("onResume===", "onResume")
        if (paused) {
            recyclerView.post {
                initTimer()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.e("onPause===", "onPause")
        paused = true
        stopWork()
    }
}