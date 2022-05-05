package com.dreavee.test1.room

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dreavee.test1.room.dao.ProductDao
import com.dreavee.test1.room.entity.Product


@Database(
    entities = [Product::class],
//    autoMigrations = [
//        AutoMigration (from = 1, to = 2)
//    ],
    version = 1,
    exportSchema = true
)
//@TypeConverters(NoteConverters::class)
abstract class CartDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: CartDatabase? = null

        fun getDatabase(context: Context): CartDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

//        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // The following query will add a new column called lastUpdate to the notes database
//                database.execSQL("ALTER TABLE products ADD COLUMN lastUpdate INTEGER NOT NULL DEFAULT 0")
//            }
//        }

        private fun buildDatabase(context: Context): CartDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                CartDatabase::class.java,
                "cart_database"
            )
                //.addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}