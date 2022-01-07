package com.ravapps.sampledrinks.ui.drinks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravapps.sampledrinks.DEFAULT_PICTURE_NAME
import com.ravapps.sampledrinks.model.Drink
import com.ravapps.sampledrinks.model.DrinkData
import com.ravapps.sampledrinks.repository.Repository
import kotlinx.coroutines.launch

class DrinksViewModel(private val repo : Repository): ViewModel() {
    var editDrinkId: Int = -1
    var categoryId: Int? = null

    private val _imageName = MutableLiveData<String>().apply { value = DEFAULT_PICTURE_NAME }
    val imageName: LiveData<String>
        get() = _imageName

    private val _newDrinkName = MutableLiveData<String?>().apply { value = null }
    val newDrinkName: LiveData<String?>
        get() = _newDrinkName

    private val _categoryName = MutableLiveData<String>().apply { value = "General" }
    val categoryName: LiveData<String>
        get() = _categoryName

    fun addDrink(categoryId: Int) {
        viewModelScope.launch {
            val newDrink = DrinkData(newDrinkName.value!!, imageName.value, categoryId)
            if (editDrinkId >= 0) {
                newDrink.drinkId = editDrinkId
                repo.editDrink(newDrink)
            } else {
                repo.addDrink(newDrink)
            }
        }
    }

    fun setDrinkName(text: CharSequence?) = _newDrinkName.postValue(text?.toString())
    fun setImageName(imageName: String) = _imageName.postValue(imageName)
    fun setCategoryName(categoryName: String) = _categoryName.postValue(categoryName)
    fun setFilterByCategory(categoryId: Int?): LiveData<List<Drink>> {
        this.categoryId = categoryId
        return repo.getDrinksFiltered(categoryId)
    }

    fun deleteDrink() {
        viewModelScope.launch {
            repo.deleteDrink(editDrinkId)
        }
    }
}

