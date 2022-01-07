package com.ravapps.sampledrinks

import android.content.Context
import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import java.io.File
import java.io.IOException
import java.io.InputStream


fun ImageView.loadAssetImage(fileName: String) {
    val storedImage = getStoredImageData(fileName)
    storedImage?.let { setImageDrawable(Drawable.createFromStream(it, null)) }
            ?: setImageResource(R.drawable.ic_launcher_foreground)
}

private fun ImageView.getStoredImageData(
    fileName: String
): InputStream? {
    try {
        val assetMgr: AssetManager = context.assets
        val assets = assetMgr.list("")?.filter { name -> name.equals(fileName) }
        val pictures = getAppSpecificAlbumStorageDir(context, DEFAULT_PICTURE_FOLDER).list()
            ?.filter { name -> name.equals(fileName) }

        return if (!pictures.isNullOrEmpty()) {
            getAppSpecificAlbumStorageDir(
                context,
                "$DEFAULT_PICTURE_FOLDER/${fileName}"
            ).inputStream()

        } else if (!assets.isNullOrEmpty()) {
            assetMgr.open(fileName)

        } else {
            assetMgr.open(DEFAULT_PICTURE_NAME)
        }
    } catch (e: IOException) {
        Log.e("loadAssetImage", e.localizedMessage)

        return null
    }
}

fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File {
    val file = File(
        context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        ), albumName
    )

    if (!file.mkdirs()) Log.i("getAppSpecificAlbumStorageDir", "Directory already exist")
    return file
}
