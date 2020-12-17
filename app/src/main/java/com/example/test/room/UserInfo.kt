package com.example.test.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shopee.foody.driver.db.base.ImportVersion

/**
 * author: beitingsu
 * created on: 2020/12/17 2:32 PM
 * 数据实体
 */
@Entity(tableName = DBUtils.USER_INFO_TABLE_NAME)
data class UserInfo(

    @PrimaryKey
    @ColumnInfo(name = "uid")
    var uid: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "age")
    var age: Int = 18,

    @ImportVersion(2)
    @ColumnInfo(name = "isMale")
    var isMale: Int = 0
) {
    override fun toString(): String {
        return "uid:$uid:name:$name:age$age:isMale:$isMale"
    }
}