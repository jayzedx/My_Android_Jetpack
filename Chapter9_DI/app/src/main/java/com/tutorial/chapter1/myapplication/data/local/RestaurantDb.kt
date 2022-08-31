package com.tutorial.chapter1.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [LocalRestaurant::class],
    version = 2,
    exportSchema = false
)
abstract class RestaurantDb : RoomDatabase() {
    abstract val dao: RestaurantDao


//    companion object {
//
//        @Volatile //This means that writes to this field are immediately made visible to other threads.
//        private var INSTANCE: RestaurantDao? = null
//
//        fun getDaoInstance(context: Context): RestaurantDao
//        {
//            synchronized(this) {
//                var instance = INSTANCE
//                if (instance == null) {
//                    instance = buildDatabase(context).dao
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
//
//        private fun buildDatabase(context: Context):
//                RestaurantDb = Room.databaseBuilder(
//            context.applicationContext,
//            RestaurantDb::class.java,
//            "restaurants_database"
//        )
//            .fallbackToDestructiveMigration()
//            .build()
//    }



}