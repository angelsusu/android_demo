package com.example.test.data

import android.os.Parcel
import android.os.Parcelable

/**
 * author: beitingsu
 * created on: 2021/4/23 3:26 PM
 */
data class BookInfo(val name: String? = "", val price: Double = 0.0) : Parcelable {

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeDouble(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField var CREATOR = object : Parcelable.Creator<BookInfo?> {

            override fun newArray(size: Int): Array<BookInfo?> {
                return arrayOfNulls(size)
            }

            override fun createFromParcel(parcel: Parcel): BookInfo {
                return BookInfo(parcel.readString(), parcel.readDouble())
            }
        }
    }
}