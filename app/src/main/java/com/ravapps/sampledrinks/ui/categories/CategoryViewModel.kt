package com.ravapps.sampledrinks.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravapps.sampledrinks.DEFAULT_PICTURE_NAME
import com.ravapps.sampledrinks.model.Category
import com.ravapps.sampledrinks.repository.Repository
import kotlinx.coroutines.launch

class CategoryViewModel(private val repo : Repository): ViewModel() {
    var editCategoryId: Int = -1

    val categories: LiveData<List<Category>>  = repo.getCategories()

    private val _imageName = MutableLiveData<String>().apply { value = DEFAULT_PICTURE_NAME }
    val imageName: LiveData<String>
        get() = _imageName

    private val _newCategoryName = MutableLiveData<String?>().apply { value = null }
    private val newCategoryName: LiveData<String?>
        get() = _newCategoryName

    fun getCategoryIdByName(categoryName: String?): Int {
        return categories.value?.first { category -> category.name == categoryName }?.categoryId ?: 0
    }

    fun addCategory() {
        viewModelScope.launch {
            val newCategory = Category(newCategoryName.value!!, imageName.value!!)

            if (editCategoryId >= 0) {
                newCategory.categoryId = editCategoryId
                repo.editCategory(newCategory)
            } else {
                repo.addCategory(newCategory)
            }
        }
    }

    fun setCategoryName(text: CharSequence?) = _newCategoryName.postValue(text?.toString())
    fun setImageName(imageName: String) = _imageName.postValue(imageName)
    fun deleteDrink() {
        viewModelScope.launch {
            repo.deleteCategory(editCategoryId)
        }
    }
}