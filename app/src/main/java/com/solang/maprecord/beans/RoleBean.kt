package com.solang.maprecord.beans

import android.os.Parcel
import android.os.Parcelable

/**
 * @author Burning
 * @description:
 * @date :2020/8/17 11:50 AM
 */
data class RoleBean(
    var id:String?,
    var name:String?,
    var profession:String?,
    var account:String?,
    var canPlay:Boolean = false
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(profession)
        parcel.writeString(account)
        parcel.writeByte(if (canPlay) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoleBean> {
        override fun createFromParcel(parcel: Parcel): RoleBean {
            return RoleBean(parcel)
        }

        override fun newArray(size: Int): Array<RoleBean?> {
            return arrayOfNulls(size)
        }
    }
}