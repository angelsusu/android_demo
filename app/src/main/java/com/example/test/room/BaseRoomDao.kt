package com.shopee.foody.driver.db.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * @author zhuangchizhong
 * @date 2020/8/5
 *
 * Provide common function. Most of the time, business just needs to focus on the query.
 * If business require the computational results, change the method define to return [Int].
 */
interface BaseRoomDao<T> {

    /**
     * Insert an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     */
    @Insert
    suspend fun insert(vararg obj: T)

    /**
     * Insert or replace an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(vararg obj: T)

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    suspend fun update(vararg obj: T)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    suspend fun delete(vararg obj: T)
}