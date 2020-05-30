package com.android.jetpack.room

import androidx.room.*
import com.android.jetpack.basiclib.bean.User

/**
 * 使用 @Database 注释的类应满足以下条件：
 * 1.是扩展 RoomDatabase 的抽象类。
 * 2.在注释中添加与数据库关联的实体列表。
 * 3.包含具有 0 个参数且返回使用 @Dao 注释的类的抽象方法。
 */
@Database(entities = arrayOf(UserEntity::class), version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

/**
 * 表示数据库中的表
 */
@Entity
data class UserEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "age") val age: Int
)

/**
 * 包含用于访问数据库的方法
 */
@Dao
interface UserDao {
    @Query("SELECT * from userentity")
    fun getAllUsers(): List<UserEntity>

    @Query("SELECT * From userentity where username like:username limit 1")
    fun loadUserInfo(username: String): UserEntity

    @Insert
    fun insertUsers(vararg users: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)
}