package com.example.lab_week_10.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface TotalDao {

    // Insert a new row or replace if ID already exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(total: Total)

    // Update an existing row
    @Update
    fun update(total: Total)

    // Delete an existing row
    @Delete
    fun delete(total: Total)

    // Custom query to fetch a row by ID
    @Query("SELECT * FROM total WHERE id = :id")
    fun getTotal(id: Long): Total?
}
