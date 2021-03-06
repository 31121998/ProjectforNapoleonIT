package com.example.projectfornapoleonit

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.fav_layout.*

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


class FavoriteActivity : AppCompatActivity() {

    private val adapter = FavoriteAdapter()

    lateinit var db: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_layout)

        val actionBar = supportActionBar
        actionBar!!.isHideOnContentScrollEnabled
        actionBar.title = "FavoriteList"
        actionBar.setDisplayHomeAsUpEnabled(true)




        val repoList = findViewById<RecyclerView>(R.id.favoriteList)
        repoList.layoutManager = LinearLayoutManager(this)
        repoList.adapter = adapter
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Database"
        ).build()
        getFavorite()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun deleteFromFavorite(view: View){
        doAsync {
            db.userDao().delete(db.userDao().loadById(Integer.parseInt(button3.text.toString())))
            uiThread {
                getFavorite()
            }
        }
    }

    private fun getFavorite() {
        adapter.data.clear()
        doAsync {
            adapter.data.addAll(db.userDao().getAll())
        }
        adapter.notifyDataSetChanged()
    }


}