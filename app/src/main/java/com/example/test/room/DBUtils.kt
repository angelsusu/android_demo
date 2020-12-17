package com.example.test.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * author: beitingsu
 * created on: 2020/12/17 3:06 PM
 */
object DBUtils {

    const val USER_INFO_TABLE_NAME = "userInfo"


    /**
     * most easy way to generate a simple database
     *
     * Note: For applications using Room, full write-ahead logging mode (not Compatibility WAL) is
     * enabled by default. This applies to devices running API 16 and higher and are not categorized
     * as a low memory device. For more information, @see [androidx.room.RoomDatabase.JournalMode.AUTOMATIC].
     * @link [https://source.android.com/devices/tech/perf/compatibility-wal]
     * 所以默认不需要指定WAL模式，Room会自动判断机型来选择最优的读写模式，可以理解为绝大多数情况都是WAL事务模式。
     */
    fun <T : RoomDatabase> buildRoom(context: Context, cls: Class<T>, name: String): RoomDatabase.Builder<T> =
        Room.databaseBuilder(context, cls, name)

    /**
     * simple way to build migrate in kotlin
     */
    fun migrate(
        startVersion: Int,
        endVersion: Int,
        block: (database: SupportSQLiteDatabase) -> Unit
    ) = object : Migration(startVersion, endVersion) {
        override fun migrate(database: SupportSQLiteDatabase) {
            block.invoke(database)
        }
    }


}