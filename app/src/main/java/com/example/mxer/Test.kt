//package com.codepath.example
//
//import okio.source
//import okio.buffer
//import okio.BufferedSource.readByteString
//import okio.ByteString.toByteArray
//import okhttp3.MultipartBody.Builder.setType
//import okhttp3.MultipartBody.Builder.addFormDataPart
//import okhttp3.MultipartBody.Builder.build
//import okhttp3.Response.body
//import okhttp3.ResponseBody.byteStream
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.os.Environment
//import android.util.Log
//import android.view.View
//import com.codepath.asynchttpclient.AsyncHttpClient
//import com.codepath.asynchttpclient.callback.TextHttpResponseHandler
//import com.codepath.asynchttpclient.RequestParams
//import com.codepath.asynchttpclient.RequestHeaders
//import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
//import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler.JSON
//import okio.BufferedSource
//import okio.ByteString
//import okhttp3.RequestBody
//import okhttp3.MultipartBody
//import com.codepath.asynchttpclient.callback.BinaryHttpResponseHandler
//import com.example.mxer.R
//import okhttp3.Headers
//import okhttp3.Response
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//
//class Test : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_test)
//    }
//
//    fun onTest(view: View?) {
//        val client = AsyncHttpClient()
//        // Basic GET calls
//
//        // Sending no headers with API key
//        client["https://api.thecatapi.com/v1/images/search", object : TextHttpResponseHandler() {
//            override fun onSuccess(statusCode: Int, headers: Headers, response: String) {
//                Log.d("DEBUG", response)
//            }
//
//            override fun onFailure(
//                statusCode: Int,
//                headers: Headers?,
//                errorResponse: String,
//                throwable: Throwable?
//            ) {
//                Log.d("DEBUG", errorResponse)
//            }
//        }]
//        client.setReadTimeout(10)
//        client.setConnectTimeout(10)
//        val params = RequestParams()
//        params["limit"] = "5"
//        params["page"] = 0
//        val requestHeaders = RequestHeaders()
//        requestHeaders["x-api-key"] = BuildConfig.api_key
//        client["https://api.thecatapi.com/v1/images/search", requestHeaders, params, object :
//            JsonHttpResponseHandler() {
//            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
//                Log.d("DEBUG", json.jsonArray.toString())
//            }
//
//            override fun onFailure(
//                statusCode: Int,
//                headers: Headers,
//                response: String,
//                throwable: Throwable
//            ) {
//                Log.d("DEBUG", response)
//            }
//        }]
//        val bufferedSource = resources.openRawResource(R.raw.cat).source().buffer()
//        try {
//            val source = bufferedSource.readByteString()
//            val body: RequestBody = RequestBody.create(source.toByteArray(), get.get("image/jpg"))
//            val requestBody: RequestBody = Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("file", "cat.jpg", body)
//                .build()
//            client.post(
//                "https://api.thecatapi.com/v1/images/upload",
//                requestHeaders,
//                params,
//                requestBody,
//                object : JsonHttpResponseHandler() {
//                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
//                        Log.d("DEBUG", json.toString())
//                    }
//
//                    override fun onFailure(
//                        statusCode: Int,
//                        headers: Headers,
//                        response: String,
//                        throwable: Throwable
//                    ) {
//                        Log.d("DEBUG", response)
//                    }
//                })
//        } catch (e: IOException) {
//        }
//        client["https://cdn2.thecatapi.com/images/6eg.jpg", object : BinaryHttpResponseHandler() {
//            override fun onSuccess(statusCode: Int, headers: Headers, response: Response) {
//                try {
//                    // ~/Library/Android/sdk/platform-tools/adb pull /sdcard/Android/data/com.codepath.cpasynchttpclient/files/Pictures/TEST/test.jpg
//                    val mediaStorageDir =
//                        File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "TEST")
//
//                    // Create the storage directory if it does not exist
//                    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
//                        Log.d("DEBUG", "failed to create directory")
//                    }
//
//                    // Return the file target for the photo based on filename
//                    val data = response.body!!.byteStream()
//                    val file = File(mediaStorageDir.path + File.separator + "test.jpg")
//                    val outputStream = FileOutputStream(file)
//                    val buffer = ByteArray(2048)
//                    var len: Int
//                    while (data.read(buffer).also { len = it } != -1) {
//                        outputStream.write(buffer, 0, len)
//                    }
//                    Log.e("DEBUG", "done!")
//                } catch (e: IOException) {
//                    Log.e("DEBUG", e.toString())
//                }
//            }
//
//            override fun onFailure(
//                statusCode: Int,
//                headers: Headers?,
//                response: String,
//                throwable: Throwable?
//            ) {
//                Log.d("DEBUG", response)
//            }
//        }]
//    }
//}