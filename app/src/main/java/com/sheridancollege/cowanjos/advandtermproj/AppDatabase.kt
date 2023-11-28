package com.sheridancollege.cowanjos.advandtermproj

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Annotate with @Database, list all the entities and set version
@Database(entities = [Diet::class, Meal::class, Cycling::class, FreeWeights::class /*, any other entities */], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // Provide abstract methods for each DAO
    abstract fun dietDao(): DietDao
    abstract fun mealDao(): MealDao
    abstract fun cyclingDao(): CyclingDao
    abstract fun freeWeightsDao(): FreeWeightsDao

    // Companion object to provide a way to get an instance of the database
    companion object {
        // Singleton prevents multiple instances of the database opening at the same time
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_tracker_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

