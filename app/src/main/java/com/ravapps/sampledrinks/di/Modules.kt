package com.ravapps.sampledrinks.di

import androidx.room.Room
import com.ravapps.sampledrinks.db.DatabaseCallback
import com.ravapps.sampledrinks.db.DrinksDatabase
import com.ravapps.sampledrinks.db.dao.CategoryDao
import com.ravapps.sampledrinks.db.dao.DrinkDao
import com.ravapps.sampledrinks.repository.Repository
import com.ravapps.sampledrinks.repository.RepositoryImpl
import com.ravapps.sampledrinks.ui.categories.CategoryViewModel
import com.ravapps.sampledrinks.ui.drinks.DrinksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // DB
    single { Room.databaseBuilder(get(), DrinksDatabase::class.java, "drinks_database")
        .addCallback(DatabaseCallback())
        .build()
    }
    single<CategoryDao> { get<DrinksDatabase>().categoryDao() }
    single<DrinkDao> { get<DrinksDatabase>().drinkDao() }

    // Repository
    single<Repository> { RepositoryImpl(get<DrinkDao>(), get<CategoryDao>()) }

    // ViewModel
    viewModel { CategoryViewModel(get<Repository>()) }
    viewModel { DrinksViewModel(get<Repository>()) }
}