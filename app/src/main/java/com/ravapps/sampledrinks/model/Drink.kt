package com.ravapps.sampledrinks.model

import androidx.room.Embedded
import androidx.room.Relation
import com.ravapps.sampledrinks.model.DrinkData
import com.ravapps.sampledrinks.model.ItemModel

data class Drink (
    @Embedded val drinkData: DrinkData,
    @Relation(
        parentColumn = "drinkId",
        entityColumn = "categoryId"
    )
    val category: Category
): ItemModel {
    override val id: Int
        get() = drinkData.drinkId
    override val title: String
        get() = drinkData.drink
    override val subTitle: String
        get() = category.name
    override val imageName: String
        get() = drinkData.imageName ?: "sprkl_water.webp"



}
