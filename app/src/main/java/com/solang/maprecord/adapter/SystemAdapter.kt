package com.solang.maprecord.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.solang.maprecord.R
import com.solang.maprecord.beans.MapBean
import com.solang.maprecord.utils.Constant
import com.solang.maprecord.utils.TimeUtils


/**
 * Created with Android Studio.
 * Description:
 * @author: Burning
 * @date: 2020/02/27
 * Time: 17:32
 */
class SystemAdapter(layoutId: Int, listData: List<MapBean>?) :
    BaseQuickAdapter<MapBean, BaseViewHolder>(layoutId, listData) {

    override fun convert(viewHolder: BaseViewHolder?, item: MapBean?) {
        viewHolder?.let { holder ->
            when {
                item?.isMark == "1" -> {
                    holder.setBackgroundColor(R.id.tvName,mContext.resources.getColor(R.color.white))
                }
                item?.isMark == "0" -> {
                    holder.setBackgroundColor(R.id.tvName,mContext.resources.getColor(R.color.common_gray))
                }}
            holder.setText(R.id.tvName, item?.name)
            var timeBegin: Long = 0
            var timeGap: Long = 0
            when {
                Constant.hlmm_name == item?.name -> {
                    timeBegin = Constant.hlmm_begin
                    timeGap = Constant.hlmm_gap
                }
                Constant.mc_name == item?.name -> {
                    timeBegin = Constant.mc_begin
                    timeGap = Constant.mc_gap
                }
                Constant.bwl_name == item?.name -> {
                    timeBegin = Constant.bwl_begin
                    timeGap = Constant.bwl_gap
                }
                Constant.zuge_name == item?.name -> {
                    timeBegin = Constant.zuge_begin
                    timeGap = Constant.zuge_gap
                }
                Constant.raq_name == item?.name -> {
                    timeBegin = Constant.raq_begin
                    timeGap = Constant.raq_gap
                }
                Constant.taq_name == item?.name -> {
                    timeBegin = Constant.taq_begin
                    timeGap = Constant.taq_gap
                }
            }

            var timeNum= timeGap -TimeUtils.getTimeSpanByNow(timeBegin, 1)%timeGap
            var timeDay = timeNum/(24*60*60*1000)
            var timeH = timeNum%(24*60*60*1000)/(60*60*1000)
            var timeM = timeNum%(60*60*1000)/(60*1000)
            var time = "剩余： "+timeDay+"天"+timeH+"小时"+timeM+"分"
            holder.setText(R.id.tvTime,time)
        }
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        val animatorX =
            ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0.0f, 1.0f)
        val animatorY =
            ObjectAnimator.ofFloat(holder.itemView, "scaleY", 0.0f, 1.0f)
        val set = AnimatorSet()
        set.duration = 1000
        set.interpolator = CustomScaleInterpolator(0.4f)
        set.playTogether(animatorX, animatorY)
        set.start()
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val animatorX =
            ObjectAnimator.ofFloat(holder.itemView, "scaleX", 1.0f, 0.0f)
        val animatorY =
            ObjectAnimator.ofFloat(holder.itemView, "scaleY", 1.0f, 0.0f)
        val set = AnimatorSet()
        set.duration = 1000
        set.interpolator = CustomScaleInterpolator(0.4f)
        set.playTogether(animatorX, animatorY)
        set.start()
    }
}