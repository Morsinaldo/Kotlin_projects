package com.example.sendfile

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import kotlin.coroutines.*

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import okhttp3.*
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class MainActivity : AppCompatActivity() {
    // declare attribute for textview
    private var editText: EditText? = null
    private var button: Button? = null
    private var okHttpClient: OkHttpClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = this.findViewById(R.id.dummy_text)
        button = this.findViewById(R.id.dummy_send)
        okHttpClient = OkHttpClient()

        button!!.setOnClickListener {
            sendData()
        }

    }

    private fun sendData(){
        runOnUiThread {
            val dummyText = editText?.text.toString()

            // we add the information we want to send in
            // a form. each string we want to send should
            // have a name. in our case we sent the
            // dummyText with a name 'sample'
            val formbody: FormBody = FormBody.Builder()
                .add("sample", dummyText)
                .build()

            // while building request
            // we give our form
            // as a parameter to post()
            /*
            val request: Request = Request.Builder().url("http://192.168.0.31:5000/json")
                .post(formbody)
                .build()
             */

            // - - - - - - - - -  PASSING A FILE - - - - - - - - - - -
            // - - - - - - - - - - - - CSV - - - - - - - - - - - - - -
            val fileCsv = File.createTempFile("filename",".txt")
            fileCsv.appendText("This an upload csv file test")
            val requestBodyCsv = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file_csv", "filename.txt", fileCsv.asRequestBody()).build()
            // - - - - - - - - - - - - JSON - - - - - - - - - - - - - -
            val fileJson = File.createTempFile("filename",".txt")
            fileJson.appendText("This an upload json file test")
            val requestBodyJson = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file_json", "filename.txt", fileJson.asRequestBody()).build()


            // - - - - - - REQUEST CSV - - - - - - - - - - - - - - - - -
            val requestCSV: Request = Request.Builder()
                .url("https://connected-cars-api-v01.herokuapp.com/uploader/csv")
                .post(requestBodyCsv)
                .build()

            // - - - - - - REQUEST JSON - - - - - - - - - - - - - - - - -
            val requestJson: Request = Request.Builder()
                .url("https://connected-cars-api-v01.herokuapp.com/uploader/json")
                .post(requestBodyJson)
                .build()


            // - - - - - - ERROR HANDLING CSV- - - - - - - - - - - - - -
            okHttpClient!!.newCall(requestCSV).enqueue(object : Callback {
                override fun onFailure(
                    call: Call,
                    e: IOException
                ) {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "Server down (CSV)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (response.body!!.string() == "received") {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Data received (CSV)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })

            // - - - - - - ERROR HANDLING JSON- - - - - - - - - - - - - -
            okHttpClient!!.newCall(requestJson).enqueue(object : Callback {
                override fun onFailure(
                    call: Call,
                    e: IOException
                ) {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "Server down (JSON)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (response.body!!.string() == "received") {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Data received (Json)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
        }
    }
}