package com.getswipe.android.utils

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
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

fun TextInputLayout.getText(): String {
    return editText?.text.toString()
}

fun TextInputLayout.isEmptyText(): Boolean {
    return editText?.text.toString().isEmpty()
}
fun TextInputLayout.removeText() {
    editText?.text?.clear()
}

fun Context.messageDialog(mesage: String, dialog: (DialogInterface) -> Unit) {
    MaterialAlertDialogBuilder(this)
        .setTitle(mesage)
        .setPositiveButton("Ok") { d, _ ->
            dialog.invoke(d)
        }
        .create()
        .show()
}