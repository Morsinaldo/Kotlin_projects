package com.example.savefile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.documentfile.provider.DocumentFile
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import retrofit2.Call
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.sql.Timestamp
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val PERMISSION_REQUEST = 10

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    lateinit var locationManager: LocationManager
    private var hasGPS = false
    private var hasNetwork = false
    private var locationGPS: Location? = null
    private var locationNetwork: Location? = null
    private var finalLocation: Location? = null
    var spinner: Spinner? = null
    val mapper = jacksonObjectMapper()


    /*
    val jsonString = """{
    "id":101,
    "username":"admin",
    "password":"Admin123",
    "fullName":"Best Admin"
    }"""
     */


    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Dropdown
        spinner = this.findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(this, R.array.Times_requests, android.R.layout.simple_spinner_item, ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner?.adapter = adapter
        }

        val btnSpinner: Button = findViewById(R.id.button_spinner)
        btnSpinner.setOnClickListener{
            val item = spinner?.selectedItem.toString()
            Toast.makeText(this,"The selected item is " + item, Toast.LENGTH_LONG).show()
        }
        */

        // Save file
        var btnSave: Button = findViewById(R.id.button_save)
        btnSave.setOnClickListener {
            //toGetLocation()
            //createJsonData()
            //saveJsonJackson()
            //getData()
            sendData()
        }



    }

    //Testando com o OkHHTP
    private fun sendData(){

    }


    /*
    private fun sendData(){
        runBlocking {
            val client = HttpClient(CIO)
            val path = this@MainActivity.externalCacheDir
            val response: HttpResponse = client.submitFormWithBinaryData(
                url = "http://localhost:8080/upload",
                formData = formData {
                    append("description", "Ktor logo")
                    append("image", File("$path/JSON","myJson.json").readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "document/json")
                        append(HttpHeaders.ContentDisposition, "filename=ktor_logo.png")
                    })
                }
            )
        }
    }
     */

    private fun getData() {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance("https://jsonplaceholder.typicode.com")

        val endpoint = retrofitClient.create(Endpoint::class.java)
        val callback = endpoint.getPosts()


        callback.enqueue(object : Callback<List<Posts>> {
            override fun onFailure(call: Call<List<Posts>>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Posts>>, response: Response<List<Posts>>) {
                response.body()?.forEach {
                    var textView = findViewById<TextView>(R.id.textView)
                    textView.append(it.body)
                }
            }
        })
    }

    private fun saveJsonJackson() {
        val user = User(102, "test", "pass12", "Test User")
        val userJson = mapper.writeValueAsString(user)

        saveJson(userJson)
    }

    private fun createJsonData() {
        val json = JSONObject()

        val owner = Owner("Morsinaldo", "Medeiros", "Jardim do Seridó", 23)
        json.put("owner", addOwner(owner))

        json.put("numberOfDogs", 3)
        json.put("dogBreeds",
            JSONArray().put("beagle")
                        .put("bullgod")
                        .put("rottweiler"))

        val dogs = arrayListOf<Dog>(
            Dog("Buck", "beagle", 4, 7.3F),
            Dog("Gizmo", "bulldog", 1, 5.2F),
            Dog("Tank", "rottweiler", 2, 6.4F)
        )

        json.put("dogs", addDogs(dogs))

        saveJson(json.toString())
    }

    private fun saveJson(jsonString: String) {
        val output : Writer
        val path = this.externalCacheDir
        val letDirectory = File(path, "JSON")
        letDirectory.mkdirs()

        val file = File(letDirectory, "myJson.json")
        if (!file.exists()) {
            file.createNewFile()
            output=BufferedWriter(FileWriter(file))
            output.write("[\n$jsonString\n]")
            output.close()
            Toast.makeText(this, "File saved first time", Toast.LENGTH_SHORT).show()
        } else {
            var textFile = readFile(path.toString())
            Log.i("Text file read", textFile)
            textFile = textFile.subSequence(0,textFile.length-2).toString()

            Log.i("Text file modified", textFile)
            file.writeText("$textFile,\n$jsonString\n]")
            Log.i("Text file modified", "$textFile,$jsonString\n]")
            Toast.makeText(this, "File saved!", Toast.LENGTH_SHORT).show()
        }

        ///Toast.makeText(this,"JSON file saved successfully", Toast.LENGTH_SHORT).show()
    }

    private fun readFile(path: String?): String {
        val bufferedReader: BufferedReader = File("$path/JSON/myJson.json").bufferedReader()
        val inputStream = bufferedReader.use { it.readText() }
        return inputStream
    }


    private fun addDogs(dogs: ArrayList<Dog>): JSONArray {

        var dogsJson = JSONArray()

        dogs.forEach{
            dogsJson.put(
                JSONArray()
                    .put(it.name)
                    .put(it.breed)
                    .put(it.age)
                    .put(it.weight)
            )
        }
        return dogsJson
    }

    private fun addOwner(owner: Owner): JSONObject {
        return JSONObject()
            .put("firstName", owner.firstName)
            .put("lastName", owner.lastName)
            .put("city", owner.city)
            .put("age", owner.age)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Toast.makeText(this,"The selected item is " + parent.getItemAtPosition(pos), Toast.LENGTH_LONG).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    fun toGetLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissionLocation(permissions)) {
                getLocation()
                Toast.makeText(this, "Location saved with success", Toast.LENGTH_SHORT).show()
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {
            Toast.makeText(this, "Location not saved with success", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if(hasGPS || hasNetwork){

            if(hasGPS){
                Log.d("CondeAndroidLocation", "hasGPS")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0F,object: LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(location != null){
                            locationGPS = location
                        }
                    }

                })

                val localGPSLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(localGPSLocation != null){
                    locationGPS = localGPSLocation
                }
            }

            if(hasNetwork){
                Log.d("CondeAndroidLocation", "hasGPS")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0F,object: LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(location != null){
                            locationNetwork = location
                        }
                    }

                })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if(localNetworkLocation != null){
                    locationNetwork = localNetworkLocation
                }
            }

            if(locationGPS != null && locationNetwork != null){
                if(locationGPS!!.accuracy > locationNetwork!!.accuracy){
                    finalLocation = locationNetwork
                } else {
                    finalLocation = locationGPS
                }
            }
            chooseDirectory()
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun checkPermissionLocation(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for(i in permissionArray.indices){
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess)
                getLocation()
        }
    }


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val treeUri: Uri? = data?.data
            val pickedDir = DocumentFile.fromTreeUri(this!!, treeUri!!) //

            // Create a new file and write into it
            saveFile(pickedDir)
        }
    }

    fun chooseDirectory(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        //startActivityForResult(intent, 42) // Deprecated
        resultLauncher.launch(intent)
    }

    fun isStoragePermissionGranted(): Boolean {
        val tag = "Storage Permission"
        return if (Build.VERSION.SDK_INT >= 23) {
            if (this?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(tag, "Permission is granted")
                true
            } else {
                Log.v(tag, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this!!,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else {
            Log.v(tag, "Permission is granted")
            true
        }
    }

    fun saveFile(pickedDir: DocumentFile?) {
        if (isStoragePermissionGranted()) { // check or ask permission
            val timestamp = System.currentTimeMillis()
            val fname = "Location-${Timestamp(timestamp)}.csv"
            val file = pickedDir?.createFile("text/csv", fname)
            try {
                //var text = "Hello World!"
                //val collums = arrayOf("Devide name", "start time", "end time", "initial measure", "final measure", "tank capacit")
                //val data1 = arrayOf("Note9 Morsinaldo", "${Timestamp(timestamp)}", "${Timestamp(timestamp)}", "75.0", "79.0", "60")

                val out: OutputStream = this!!.contentResolver.openOutputStream(file?.uri!!)!!
                val csvwriter = CSVWriter(out)
                var dataLocation = arrayOf("${finalLocation!!.latitude}","${finalLocation!!.longitude}")

                csvwriter.writeNext(dataLocation)
                out.close()
                Toast.makeText(this, "Report saved successfully", Toast.LENGTH_LONG).show()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun save(){
        // -------- MÉTODO 1 ----------
        /*
        val fos: FileOutputStream
        var text = "Hello World!"

        try {
            fos = openFileOutput("teste.txt", MODE_PRIVATE)
            fos.write(text.toByteArray())
            fos.close()
        } catch (e: FileNotFoundException){
            e.printStackTrace()
        } catch (e: Exception){
            e.printStackTrace()
        }

        Toast.makeText(this,"Exported SucessFully!",Toast.LENGTH_LONG).show()
         */
        // -------- MÉTODO 2 ----------
        /*
        val filename = "myfile.txt"
        val fileContents = "Hello world!"
        //val file = File(context.filesDir, filename)
        this.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }

         */
        // -------- MÉTODO 3 ----------
        val path = this.filesDir
        val letDirectory = File(path, "LOG")
        letDirectory.mkdirs()

        val file = File(letDirectory, "com_arvore.txt")

        file.appendText("Hello World!!!")

        Toast.makeText(this,"Exported Sucessfully!",Toast.LENGTH_LONG).show()

    }

    override fun onClick(p0: View?) {

    }

}

