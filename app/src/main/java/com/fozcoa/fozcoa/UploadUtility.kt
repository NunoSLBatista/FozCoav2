package com.fozcoa.fozcoa

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import okhttp3.RequestBody
import okhttp3.MultipartBody


class UploadUtility() {

    var dialog: ProgressDialog? = null
    var serverURL: String = "https://app.ecoa.pt/api/upload/upload_video.php"
    val client = OkHttpClient()

    fun uploadFile(sourceFile: File, uploadedFileName: String? = null) : String {

            var mimeType = getMimeType(sourceFile);
            mimeType = "video/mp4";
            if (mimeType == null) {
                Log.e("file error", "Not able to get mime type")
                return ""
            }
            val fileName: String = if (uploadedFileName == null)  sourceFile.name else uploadedFileName
            try {
                val req = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "video",
                        fileName,
                        RequestBody.create(mimeType.toMediaTypeOrNull(), sourceFile)
                    ).build()

                val request: Request = Request.Builder().url(serverURL).post(req).build()

                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val response = response.body!!.string();
                    return response;
                } else {
                    Log.e("File upload", "failed")
                    return "";
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("File upload", "failed")
                return "";
            }

    }

    // url = file path or whatever suitable URL you want.
    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }


}