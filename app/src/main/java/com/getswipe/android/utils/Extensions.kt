package com.getswipe.android.utils

import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import java.io.File

fun Fragment.getFile(imageUri: Uri): File? {
    var result: File? = null
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = requireContext().contentResolver.query(imageUri, filePathColumn, null, null, null)
    if (cursor != null) {
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            result = File(filePath)
        }
        cursor.close()
    }

    return result
}
