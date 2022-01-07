package com.ravapps.sampledrinks.model


import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categories")
data class Category (
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") override val imageName: String = "sprkl_water.webp",
): ItemModel {
    @PrimaryKey(autoGenerate = true) var categoryId: Int = 0

    override val id: Int
        get() = categoryId ?: 0
    override val title: String
        get() = name
    override val subTitle: String?
        get() = null

    fun toContentValues() = ContentValues(2).apply {
        put("categoryId", categoryId)
        put("name", name)
        put("image", imageName)
    }
}
