package com.example.test.room

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * author: beitingsu
 * created on: 2020/12/17 2:43 PM
 * 数据库
 */
@Database(entities = [UserInfo::class], version = 2)
abstract class UserInfoDB : RoomDatabase() {

   protected abstract fun getUserInfoDao(): UserInfoDao

    //必须在这里定义？
    fun userInfoDao(): UserInfoDao? {
        return getUserInfoDao()
    }


    companion object {

        //构建DataBase
        fun build(context: Context, id: String) = DBUtils
            .buildRoom(context, UserInfoDB::class.java, "user_$id")
            .addMigrations(
                MIGRATIONS.M_1_2
            )   //数据库升级使用
            .build()
    }
}

object MIGRATIONS {
    val M_1_2 = DBUtils.migrate(1, 2) { database ->
        database.execSQL(
            "ALTER TABLE ${DBUtils.USER_INFO_TABLE_NAME} ADD COLUMN ${UserInfo::isMale.name} INTEGER NOT NULL DEFAULT 0 "
        )
    }
}