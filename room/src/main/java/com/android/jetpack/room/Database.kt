package com.android.jetpack.room

import android.drm.DrmStore.DrmObjectType.UNKNOWN
import androidx.room.*
import androidx.lifecycle.LiveData

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
 * 1.每个实体必须将至少 1 个字段定义为主键。即使只有 1 个字段，您仍然需要为该字段添加 @PrimaryKey 注释。
 * 2.默认情况下，Room 将类名称用作数据库表名称。如果您希望表具有不同的名称，请设置 @Entity 注释的 tableName 属性
 * @Entity(tableName = "users")
 * 3.与 tableName 属性类似，Room 将字段名称用作数据库中的列名称。如果您希望列具有不同的名称，请将 @ColumnInfo 注释添加到字段，如以下代码段所示
 * 4.默认情况下，Room 会为在实体中定义的每个字段创建一个列。如果某个实体中有您不想保留的字段，则可以使用 @Ignore 为这些字段注释，如以下代码段所示
 * 5. 如果您的应用需要通过全文搜索 (FTS) 快速访问数据库信息，请使用虚拟表（使用 FTS3 或 FTS4 SQLite 扩展模块）为您的实体提供支持。(
 * 启用 FTS 的表始终使用 INTEGER 类型的主键且列名称为“rowid”。如果是由 FTS 表支持的实体定义主键，则必须使用相应的类型和列名称。)
 */
@Entity
data class UserEntity(
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "age") val age: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

/**
 * 包含用于访问数据库的方法
 */
@Dao
interface UserDao {

    @Query("SELECT * from userentity")
    fun getAllUsers(): LiveData<MutableList<UserEntity>>

    @Query("SELECT * From Userentity where username like:username limit 1")
    fun loadUserInfo(username: String): UserEntity

    @Insert
    fun insertUsers(vararg users: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)
}