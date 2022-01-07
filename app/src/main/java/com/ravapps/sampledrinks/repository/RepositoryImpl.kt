package com.ravapps.sampledrinks.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.ravapps.sampledrinks.db.dao.CategoryDao
import com.ravapps.sampledrinks.db.dao.DrinkDao
import com.ravapps.sampledrinks.model.Category
import com.ravapps.sampledrinks.model.Drink
import com.ravapps.sampledrinks.model.DrinkData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class RepositoryImpl(private val drinksDao: DrinkDao, private val categoryDao: CategoryDao): Repository, KoinComponent {

    override fun giveHelloAsLiveData() = liveData {
        emit("Loading started")
        kotlinx.coroutines.delay(3000)
        emit("Loading finished")
    }

    override suspend fun giveHello(): String = withContext(Dispatchers.IO) {
        kotlinx.coroutines.delay(3000)
        return@withContext "Loading finished"
    }

    override fun getCategories() = categoryDao.getCategories()

    override fun getCategoryNames() = categoryDao.getCategoryNames()

    override fun getDrinks() = drinksDao.getAllDrinks()

    override fun getDrinksFiltered(categoryId: Int?): LiveData<List<Drink>> {
        return  drinksDao.getAllDrinks().switchMap { list ->
            liveData { emit(list.filter {
                    drink -> categoryId?.let { drink.drinkData.categoryId == it } ?: true
            })}
        }

    }

    override suspend fun addDrink(drink: DrinkData) = withContext(Dispatchers.IO) {
        drinksDao.addDrink(drink)
    }
    override suspend fun editDrink(drink: DrinkData) = withContext(Dispatchers.IO) {
        drinksDao.updateDrink(drink)
    }

    override suspend fun editCategory(category: Category) = withContext(Dispatchers.IO) {
        categoryDao.updateCategory(category)
    }

    override suspend fun addCategory(category: Category) = withContext(Dispatchers.IO) {
        categoryDao.addCategory(category)
    }

    override suspend fun deleteDrink(id: Int) = withContext(Dispatchers.IO) {
        val drink = DrinkData("", "", 0).apply { drinkId = id }
        drinksDao.deleteDrink(drink)
    }

    override suspend fun deleteCategory(id: Int) = withContext(Dispatchers.IO) {
        val category = Category("", "").apply { categoryId = id }
        categoryDao.deleteCategory(category)
    }
}