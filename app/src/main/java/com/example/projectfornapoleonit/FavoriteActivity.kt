package com.example.projectfornapoleonit

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fav_layout.*

import org.jetbrains.anko.doAsync
import kotlin.coroutines.CoroutineContext
import android.support.v7.widget.helper.ItemTouchHelper.Callback.makeMovementFlags
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import android.app.ListActivity
import kotlinx.android.synthetic.main.favorite_layout.*


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

    fun deleteFromFavorite(view: View){
        doAsync {
            db.userDao().delete(db.userDao().loadById(Integer.parseInt(numComicsView.text.toString())))
        }
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