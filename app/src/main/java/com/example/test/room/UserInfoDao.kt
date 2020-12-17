package com.example.test.room

import androidx.room.Dao
import androidx.room.Query
import com.shopee.foody.driver.db.base.BaseRoomDao

/**
 * author: beitingsu
 * created on: 2020/12/17 2:46 PM
 * 操作数据库的接口
 */
@Dao
interface UserInfoDao : BaseRoomDao<UserInfo> {

    //sql语句
    @Query("SELECT * FROM ${DBUtils.USER_INFO_TABLE_NAME}")
    suspend fun getAll(): List<UserInfo>?


    //sql语句
    @Query("SELECT * FROM ${DBUtils.USER_INFO_TABLE_NAME} WHERE uid IN (:ids)")
    suspend fun getByIds(vararg ids: String): List<UserInfo>?
}