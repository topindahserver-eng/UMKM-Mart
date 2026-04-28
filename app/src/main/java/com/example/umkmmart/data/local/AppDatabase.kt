package com.example.umkmmart.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CartItem::class, Product::class, OrderItem::class, User::class], version = 9, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "umkm_mart_db"
                )
                .addCallback(DatabaseCallback())
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.productDao())
                    }
                }
            }

            suspend fun populateDatabase(productDao: ProductDao) {
                productDao.insertProduct(Product(0, "Kripik Singkong Pedas", "Kripik singkong renyah buatan UMKM lokal.", 15000.0, "https://images.unsplash.com/photo-1599490659213-e2b9527bb087?w=500", "Makanan"))
                productDao.insertProduct(Product(0, "Tas Anyaman Bambu", "Tas tangan estetik dari bambu pilihan.", 75000.0, "https://images.unsplash.com/photo-1598532163257-ae3c6b2524b6?w=500", "Kerajinan"))
                productDao.insertProduct(Product(0, "Madu Hutan Murni", "Madu asli tanpa bahan pengawet.", 50000.0, "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=500", "Makanan"))
            }
        }
    }
}