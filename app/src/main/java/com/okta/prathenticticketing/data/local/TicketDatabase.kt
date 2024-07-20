package com.okta.prathenticticketing.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.okta.prathenticticketing.MainActivity

@Database(entities = [Ticket::class], version = 2)
abstract class TicketDatabase : RoomDatabase() {
    abstract fun ticketDao(): TicketDao

//    companion object {
//        @Volatile
//        private var INSTANCE: TicketDatabase? = null
//
//        @JvmStatic
//        fun getDatabase(context: Context): TicketDatabase {
//            return INSTANCE ?: synchronized(this) {
//                INSTANCE ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    TicketDatabase::class.java, "story_database"
//                )
//                    .fallbackToDestructiveMigration() // Add this line to allow destructive migrations
//                    .build()
//                    .also { INSTANCE = it }
//            }
//        }
//    }
}