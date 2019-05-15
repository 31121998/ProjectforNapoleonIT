package com.example.projectfornapoleonit

import android.arch.persistence.room.*


@Entity
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "nameComics") val nameComics: String?
    //@ColumnInfo(title = "imageUrl") val title: String?
)
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getReadoutById(id: Long): User

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)
}
@Database(
    entities = [User::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

