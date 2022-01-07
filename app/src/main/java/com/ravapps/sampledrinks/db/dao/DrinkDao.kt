package com.ravapps.sampledrinks.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ravapps.sampledrinks.model.Drink
import com.ravapps.sampledrinks.model.DrinkData

@Dao
interface DrinkDao {
    @Transaction
    @Query("SELECT * FROM Drinks")
    fun getAllDrinks(): LiveData<List<Drink>>

    @Transaction
    @Query("SELECT * FROM Drinks WHERE categoryId = :category")
    fun getAllDrinksFiltered(category: Int): LiveData<List<Drink>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDrink(drink: DrinkData)

    @Update
    suspend fun updateDrink(drink: DrinkData)

    @Delete
    suspend fun deleteDrink(drink: DrinkData)
}