package com.lelestacia.dicodingstoryapp.utility

import com.lelestacia.dicodingstoryapp.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object Requirement {

    fun getPhoto() : File {
        val newFile = File("file-testing-lele")
        val inputStream: InputStream =
            Module.getContext().resources.openRawResource(R.drawable.foto_twrp)
        val out: OutputStream = FileOutputStream(newFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) out.write(buf, 0, len)
        out.close()
        inputStream.close()
        return newFile
    }

    suspend fun getToken(): String {
        val loginInfo = Module.getRepository().signInWithEmailAndPassword(
            email = "km8003296@gmail.com", password = "kamilmalik"
        )
        return "Bearer ${(loginInfo as NetworkResponse.Success).data.loginResult.token}"
    }
}