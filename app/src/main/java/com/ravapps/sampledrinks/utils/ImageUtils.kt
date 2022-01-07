package com.ravapps.sampledrinks

import android.content.Context
import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File
import java.io.IOException
import java.io.InputStream

const val DEFAULT_PICTURE_NAME = "tea.webp"
const val DEFAULT_PICTURE_FOLDER = "DataImages"

/**
 *  Load  locally stored image
 */
fun ImageView.useLocalImage(fileName: String) {
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
            // Case if file found in App Pictures folder
            getAppSpecificAlbumStorageDir(
                context,
                "$DEFAULT_PICTURE_FOLDER/${fileName}"
            ).inputStream()

        } else if (!assets.isNullOrEmpty()) {
            // Case if file found in App assets folder
            assetMgr.open(fileName)

        } else {
            // Used Default from App assets folder
            assetMgr.open(DEFAULT_PICTURE_NAME)
        }
    } catch (e: IOException) {
        Log.e("loadAssetImage", e.localizedMessage)

        return null
    }
}

/**
 *  Open Image picker Factory
 *
 *  Result file has:
 *  - ratio 1x1
 *  - width: 600 px
 *  - height: 600 px
 *  - saved to DEFAULT_PICTURE_FOLDER/DataImages
 *  - supported formats: .png, .jpg, .jpeg
 */
fun prepareImagePicker(context: Fragment) = ImagePicker.with(context)
    .crop(
        1f,
        1f
    )
    .maxResultSize(
        600,
        600
    )
    .saveDir(getAppSpecificAlbumStorageDir(context.requireContext(), DEFAULT_PICTURE_FOLDER))
    .galleryMimeTypes(
        mimeTypes = arrayOf(
            "image/png",
            "image/jpg",
            "image/jpeg"
        )
    )

/**
 *  Provides Application Pictures Directory
 */
fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File {
    val file = File(
        context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        ), albumName
    )

    if (!file.mkdirs()) Log.i("getAppSpecificAlbumStorageDir", "Directory already exist")
    return file
}
