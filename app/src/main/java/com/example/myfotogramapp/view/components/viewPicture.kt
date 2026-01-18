package com.example.myfotogramapp.view.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun viewPicture(image: String): Bitmap {
    val decodedBytes = Base64.decode(image, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

    return bitmap
}