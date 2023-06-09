package com.example.swipapplication.utils

import android.content.Context
import java.io.File

fun createFile(context: Context, fileName: String): File {
    return context.filesDir.listFiles()?.find { it.name == fileName } ?: File(
        context.filesDir,
        fileName
    )
}