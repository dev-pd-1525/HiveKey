package com.example.hivekey

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CloudinaryHelper {

    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return
        try {
            val config = HashMap<String, String>()
            config["cloud_name"] = "dtmx6ftgr"
            MediaManager.init(context, config)
            isInitialized = true
            Log.d("CloudinaryHelper", "Cloudinary initialized successfully")
        } catch (e: Exception) {
            Log.e("CloudinaryHelper", "Failed to init Cloudinary: ${e.message}")
        }
    }

    /**
     * Upload a profile photo to Cloudinary with user info as metadata.
     */
    fun uploadImage(
        context: Context,
        bitmap: Bitmap,
        username: String = "",
        email: String = "",
        role: String = "",
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
            fos.flush()
            fos.close()

            // Build context metadata so user info appears in Cloudinary dashboard
            val contextMeta = "username=$username|email=$email|role=$role|joined=${getCurrentDate()}"

            MediaManager.get().upload(file.absolutePath)
                .unsigned("hivekey_unsigned")
                .option("folder", "hivekey_users")
                .option("context", contextMeta)
                .option("tags", "$role,$email")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        Log.d("CloudinaryHelper", "Photo upload started")
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        val progress = (bytes.toDouble() / totalBytes * 100).toInt()
                        Log.d("CloudinaryHelper", "Photo upload: $progress%")
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val url = resultData["secure_url"] as? String ?: ""
                        Log.d("CloudinaryHelper", "Photo upload success: $url")
                        file.delete()
                        onSuccess(url)
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        Log.e("CloudinaryHelper", "Photo upload error: ${error.description}")
                        file.delete()
                        onError(error.description)
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        Log.w("CloudinaryHelper", "Photo upload rescheduled")
                    }
                })
                .dispatch()
        } catch (e: Exception) {
            onError(e.localizedMessage ?: "Upload failed")
        }
    }

    /**
     * Upload user signup data as a JSON log file to Cloudinary.
     * These appear in Cloudinary Media Library under hivekey_logs/ folder.
     */
    fun uploadUserLog(
        context: Context,
        username: String,
        email: String,
        role: String,
        hasPhoto: Boolean,
        onComplete: () -> Unit
    ) {
        try {
            val timestamp = getCurrentDate()
            val safeEmail = email.replace("@", "_at_").replace(".", "_")

            // Create JSON log
            val json = """
            {
                "app": "HiveKey",
                "type": "user_signup",
                "username": "$username",
                "email": "$email",
                "role": "$role",
                "has_photo": $hasPhoto,
                "signup_date": "$timestamp",
                "device": "${android.os.Build.MODEL}",
                "android_version": "${android.os.Build.VERSION.RELEASE}"
            }
            """.trimIndent()

            // Save to temp file
            val file = File(context.cacheDir, "log_${safeEmail}.json")
            val writer = FileWriter(file)
            writer.write(json)
            writer.flush()
            writer.close()

            // Upload as raw file to Cloudinary
            MediaManager.get().upload(file.absolutePath)
                .unsigned("hivekey_unsigned")
                .option("resource_type", "raw")
                .option("folder", "hivekey_logs")
                .option("public_id", "user_${safeEmail}_${System.currentTimeMillis()}")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        Log.d("CloudinaryHelper", "Log upload started for: $email")
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        Log.d("CloudinaryHelper", "User log uploaded: ${resultData["secure_url"]}")
                        file.delete()
                        onComplete()
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        Log.e("CloudinaryHelper", "Log upload error: ${error.description}")
                        file.delete()
                        onComplete() // Continue even if log fails
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {}
                })
                .dispatch()
        } catch (e: Exception) {
            Log.e("CloudinaryHelper", "Log upload exception: ${e.message}")
            onComplete()
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
