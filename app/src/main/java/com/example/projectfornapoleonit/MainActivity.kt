package com.example.projectfornapoleonit
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.repo_layout.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import kotlin.coroutines.CoroutineContext



class MainActivity : AppCompatActivity(), CoroutineScope {

    val adapter = RepoAdapter()

    private val httpClient = OkHttpClient.Builder().build()
    var num = 1
    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    private fun isNetworkAvailable(): Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager){
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo.isConnected
        }else false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val repoList = findViewById<RecyclerView>(R.id.repoList)
        repoList.layoutManager = LinearLayoutManager(this)
        repoList.adapter = adapter
        loadData(num)

    }

    fun retryConnection(){
        if (isNetworkAvailable()) {
            loadData(num)
        }else findViewById<TextView>(R.id.repoName).text = "No connection"
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
        adapter.data.clear()
        adapter.data.add(repos)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
    }
}