package com.example.projectfornapoleonit
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.CoroutineContext



class MainActivity : AppCompatActivity(), CoroutineScope {


    private val httpClient = OkHttpClient.Builder().build()
    var num = 1
    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

//    private fun isNetworkAvailable(): Boolean{
//        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
//        return if (connectivityManager is ConnectivityManager){
//            val networkInfo = connectivityManager.activeNetworkInfo
//            networkInfo.isConnected
//        }else false
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData(num)
    }

//    fun retryConnection(){
//        if (isNetworkAvailable()) {
//            loadData(num)
//        }else findViewById<TextView>(R.id.repoName).text = "No connection"
//    }
    fun selectComics(view: View){
        editText2.isCursorVisible = true
        val comicsNum = editText2.text.toString()
        if (comicsNum!=""){
            num = Integer.parseInt(comicsNum)
            if (num>2134) num = 2134
            numView.text = num.toString()
            loadData(num)
            editText2.text.clear()
            editText2.isCursorVisible = false
        }
    }

    fun firstComics(view: View) {
        num = 1
        loadData(num)
        numView.text = num.toString()
    }

    fun nextComics(view: View) {
        if (num<2134) {
            num++
            loadData(num)
            numView.text = num.toString()
        }
    }

    fun prevComics(view: View) {
        if (num>1) {
            num--
            loadData(num)
            numView.text = num.toString()
        }
    }

    fun randComics(view: View) {
        num = (1..2134).random()
        loadData(num)
        numView.text = num.toString()
    }

    fun lastComics(view: View) {
        num = 2134
        loadData(num)
        numView.text = num.toString()
    }

    private fun loadData(num: Int) = launch {
        val request = Request.Builder()
            .url("https://xkcd.com/$num/info.0.json")
            .build()
    val response: String = withContext(Dispatchers.IO) {
        httpClient.newCall(request).execute().body()!!.string()
    }
    val type = object : TypeToken<Repo>() {}
    val repos = Gson().fromJson<Repo>(response, type.type)
    findViewById<TextView>(R.id.repoName).text = repos.safe_title
    val imageWindow = findViewById<com.jsibbold.zoomage.ZoomageView>(R.id.comics)
    Picasso.get().load(repos.img).into(imageWindow);
}

    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
    }
}