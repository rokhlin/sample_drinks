package com.ravapps.sampledrinks.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Drinks")
data class DrinkData(
    @ColumnInfo(name = "drink_name") val drink: String,
    @ColumnInfo(name = "image") val imageName: String?,
    @ColumnInfo(name = "categoryId") val categoryId: Int
): Parcelable {
    @PrimaryKey(autoGenerate = true) var drinkId: Int = 0
}
