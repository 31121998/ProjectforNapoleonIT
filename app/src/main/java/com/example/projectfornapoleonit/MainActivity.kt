package com.example.projectfornapoleonit
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
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
import org.jetbrains.anko.doAsync
import kotlin.coroutines.CoroutineContext
import android.content.Intent
import android.support.v7.app.ActionBar
import android.widget.Button
import android.widget.Toast
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.jetbrains.anko.wrapContent
import android.view.ViewGroup




class MainActivity : AppCompatActivity(), CoroutineScope {


    private val httpClient = OkHttpClient.Builder().build()
    var num = (1..2134).random()
    var urlOfImg = ""
    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    lateinit var db: AppDatabase
    lateinit var mShare: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Database"
        ).build()
        val goFavBtn = findViewById<Button>(R.id.button2)
        goFavBtn.setOnClickListener {
            goToFavorite()
        }
        mShare = findViewById(R.id.button4)
        mShare.setOnClickListener {
            val myIntent = Intent(Intent.ACTION_SEND)
            myIntent.type = "type/plain"
            myIntent.putExtra(Intent.EXTRA_TEXT , urlOfImg)
            startActivity(Intent.createChooser(myIntent, "Share comics"))
        }
        loadData(num)
    }


    fun selectComics(view: View){
        editText2.isCursorVisible = true
        val comicsNum = editText2.text.toString()
        if (comicsNum!=""){
            num = Integer.parseInt(comicsNum)
            if (num>2134) num = 2134
            loadData(num)
            editText2.text.clear()
            editText2.isCursorVisible = false
        }
    }

    fun firstComics(view: View) {
        num = 1
        loadData(num)
    }

    fun nextComics(view: View) {
        if (num<2134) {
            num++
            loadData(num)
        }
    }

    fun prevComics(view: View) {
        if (num>1) {
            num--
            loadData(num)
        }
    }

    fun randComics(view: View) {
        num = (1..2134).random()
        loadData(num)
    }

    fun lastComics(view: View) {
        num = 2134
        loadData(num)
    }

    fun addToFavorite(view: View){
        doAsync {
            db.userDao().insert(User(num, repoName.text.toString(), urlOfImg))
            uiThread{
                toast("Comics added to favorite")
            }
        }
    }

    fun goToFavorite(){
        val goToFavorite = Intent(this, FavoriteActivity::class.java)
        startActivity(goToFavorite)
    }

    private fun loadData(num: Int) = launch {
        numView.text = num.toString()
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
        urlOfImg = repos.img
        Picasso.get().load(urlOfImg).into(imageWindow)
        }

        override fun onDestroy() {
            rootJob.cancel()
            super.onDestroy()
    }
}