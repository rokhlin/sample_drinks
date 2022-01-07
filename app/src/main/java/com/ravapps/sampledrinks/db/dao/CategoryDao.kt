package com.ravapps.sampledrinks.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ravapps.sampledrinks.model.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Categories")
    fun getCategories(): LiveData<List<Category>>

    @Query("SELECT name FROM Categories")
    fun getCategoryNames(): LiveData<List<String>>

    @Query("SELECT * FROM Categories WHERE name = :categoryName")
    fun getCategoryByName(categoryName: String): Category

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)

}