package com.ravapps.sampledrinks.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ravapps.sampledrinks.db.dao.CategoryDao
import com.ravapps.sampledrinks.db.dao.DrinkDao
import com.ravapps.sampledrinks.model.Category
import com.ravapps.sampledrinks.model.DrinkData

@Database(entities = [DrinkData::class, Category::class], version = 1, exportSchema = false)
abstract class DrinksDatabase: RoomDatabase() {
    abstract fun drinkDao(): DrinkDao
    abstract fun categoryDao(): CategoryDao
}