package com.ravapps.sampledrinks.db

import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ravapps.sampledrinks.model.Category

class DatabaseCallback : RoomDatabase.Callback() {
    private val categoryNames = listOf("General", "Sparkling", "Coffee", "Juice", "Sports", "Vitamin Water", "Energy", "Tea", "Water", "Flavored Water", "Sparkling Water")
    private val categoryImages = listOf("water.webp", "sprkl.webp", "coffee.webp", "flavored.webp", "sport.webp", "flavored2.webp", "energy.webp", "tea.webp", "water.webp", "flavored3.webp", "sprkl_water.webp")

    override fun onCreate(db: SupportSQLiteDatabase) = db.run {
        beginTransaction()
        try {
            categoryNames.forEachIndexed { index, beverage ->
                val category = Category(name = beverage, imageName = categoryImages[index]).apply { categoryId = index }
                insert("Categories", OnConflictStrategy.REPLACE, category.toContentValues())
            }

            setTransactionSuccessful()
        } finally {
            endTransaction()
        }
    }
}