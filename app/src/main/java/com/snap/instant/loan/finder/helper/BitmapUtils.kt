package com.snap.instant.loan.finder.helper

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object BitmapUtils {

    suspend fun saveBitmapToCache(context: Context, bitmap: Bitmap, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            val cacheDir = getCacheDir(context)
            val imageFile = File(cacheDir, fileName)

            try {
                val outputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                imageFile.absolutePath
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun getCacheDir(context: Context): File {
        val cacheDir: File? = when {
            Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() -> context.externalCacheDir
            else -> context.cacheDir
        }

        return cacheDir ?: context.filesDir
    }
}