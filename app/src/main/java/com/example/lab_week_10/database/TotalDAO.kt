package com.example.lab_week_10.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface TotalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(total: Total)

    @Update
    fun update(total: Total)

    @Query("SELECT * FROM total LIMIT 1")
    fun getTotal(): Total?
}

