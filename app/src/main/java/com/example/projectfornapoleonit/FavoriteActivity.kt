package com.example.projectfornapoleonit

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import org.jetbrains.anko.doAsync
import kotlin.coroutines.CoroutineContext

class FavoriteActivity : AppCompatActivity() {

    private val adapter = FavoriteAdapter()

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_layout)
        val repoList = findViewById<RecyclerView>(R.id.favoriteList)
        repoList.layoutManager = LinearLayoutManager(this)
        repoList.adapter = adapter
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Database"
        ).build()
        getFavorite()
    }

    private fun getFavorite() {
        adapter.data.clear()
        doAsync {
            adapter.data.addAll(db.userDao().getAll())
        }
        adapter.notifyDataSetChanged()
    }


}