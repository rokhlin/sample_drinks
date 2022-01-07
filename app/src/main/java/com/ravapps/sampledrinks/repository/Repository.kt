package com.ravapps.sampledrinks.repository

import androidx.lifecycle.LiveData
import com.ravapps.sampledrinks.model.Category
import com.ravapps.sampledrinks.model.Drink
import com.ravapps.sampledrinks.model.DrinkData

interface Repository {
    fun getCategories(): LiveData<List<Category>>
    fun getCategoryNames(): LiveData<List<String>>

    fun getDrinks(): LiveData<List<Drink>>
    fun getDrinksFiltered(categoryId: Int?): LiveData<List<Drink>>

    suspend fun addDrink(drink: DrinkData)
    suspend fun editDrink(drink: DrinkData)

    suspend fun editCategory(category: Category)
    suspend fun addCategory(category: Category)
    suspend fun getCategoryIdByName(categoryName: String): Int
    suspend fun deleteDrink(id: Int)
    suspend fun deleteCategory(id: Int)


}