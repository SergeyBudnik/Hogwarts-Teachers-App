package com.bdev.hengschoolteacher.utils

import android.content.Context
import java.io.*

object StorageUtils {
    @Throws(IOException::class)
    fun <T> readData(context: Context, fileName: String): T? {
        createFileIfNotExists(context, fileName)

        try {
            context.openFileInput(fileName).use {
                val ois = ObjectInputStream(it)

                    @Suppress("UNCHECKED_CAST")
                    return ois.readObject() as T
            }
        } catch (e: Exception) {
            clearData(context, fileName)

            return null
        }
    }

    @Throws(IOException::class)
    fun <T> writeData(context: Context, fileName: String, o: T) {
        var os: OutputStream? = null

        try {
            createFileIfNotExists(context, fileName)

            os = context.openFileOutput(fileName, Context.MODE_PRIVATE)

            val oos = ObjectOutputStream(os)

            oos.writeObject(o)
            oos.flush()
        } finally {
            if (os != null) {
                os.close()
            }
        }
    }

    fun clearData(context: Context, fileName: String) {
        val file = File(context.filesDir.toString() + "/" + fileName)

        if (file.exists()) {
            file.delete()
        }
    }

    @Throws(IOException::class)
    private fun createFileIfNotExists(context: Context, fileName: String) {
        val file = File(context.filesDir.toString() + "/" + fileName)

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw IOException()
            }
        }
    }
}