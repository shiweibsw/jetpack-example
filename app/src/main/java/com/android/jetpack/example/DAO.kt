package com.android.jetpack.example

import androidx.room.*

@Entity
data class People(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "age") val age: Int
)

@Dao
interface PeopleDao {

    @Query("SELECT * FROM people")
    fun getAll(): List<People>

    @Insert
    fun insertAll(vararg people: People)
}

@Database(entities = arrayOf(People::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun peopleDao(): PeopleDao
}