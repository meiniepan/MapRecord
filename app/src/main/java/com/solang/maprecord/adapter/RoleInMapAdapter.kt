package com.solang.maprecord.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.solang.maprecord.R
import com.solang.maprecord.beans.RoleBean
import com.solang.maprecord.utils.getRoleList


/**
 * Created with Android Studio.
 * Description:
 * @author: Burning
 * @date: 2020/02/27
 * Time: 17:32
 */
class RoleInMapAdapter(layoutId: Int, listData: List<RoleBean>?) :
    BaseQuickAdapter<RoleBean, BaseViewHolder>(layoutId, listData) {

    override fun convert(viewHolder: BaseViewHolder?, item: RoleBean?) {
        viewHolder?.let { holder ->
            holder.addOnClickListener(R.id.cbItem)
            holder.setText(R.id.tv1, item?.name)
                .setText(R.id.tvAccount,item?.account)
            when (item?.profession) {
                getRoleList()[0] -> {
                    holder.setImageResource(R.id.img, R.mipmap.ic_fs)
                        .setTextColor(R.id.tv1, mContext.resources.getColor(R.color.colorFs))
                }
                getRoleList()[1] -> {
                    holder.setImageResource(R.id.img, R.mipmap.ic_xd)
                        .setTextColor(R.id.tv1, mContext.resources.getColor(R.color.colorXd))
                }
                getRoleList()[2] -> {
                    holder.setImageResource(R.id.img, R.mipmap.ic_ss)
                        .setTextColor(R.id.tv1, mContext.resources.getColor(R.color.colorSs))
                }
                getRoleList()[3] -> {
                    holder.setImageResource(R.id.img, R.mipmap.ic_ms)
                        .setTextColor(R.id.tv1, mContext.resources.getColor(R.color.colorMs))
                }
                getRoleList()[4] -> {
                    holder.setImageResource(R.id.img, R.mipmap.ic_zs)
                        .setTextColor(R.id.tv1, mContext.resources.getColor(R.color.colorZs))
                }
                getRoleList()[5] -> {
                    holder.setImageResource(R.id.img, R.mipmap.ic_sq)
                        .setTextColor(R.id.tv1, mContext.resources.getColor(R.color.colorSq))
                }
                getRoleList()[6] -> {
                    holder.setImageResource(R.id.img, R.mipmap.ic_lr)
                        .setTextColor(R.id.tv1, mContext.resources.getColor(R.color.colorLr))
                }
                getRoleList()[7] -> {
                    holder.setImageResource(R.id.img, R.mipmap.ic_dz)
                        .setTextColor(R.id.tv1, mContext.resources.getColor(R.color.colorDz))
                }

                else -> {holder.setImageResource(R.id.img, R.mipmap.ic_fs)
                    .setTextColor(R.id.tv1, mContext.resources.getColor(R.color.colorFs))}}
            holder.setChecked(R.id.cbItem,item!!.canPlay)
            }

        }


    }