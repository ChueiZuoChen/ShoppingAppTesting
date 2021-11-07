package com.example.shoppingapptesting.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShoppingItem::class],
    version = 1
)
abstract class ShoppingItemDatabase : RoomDatabase() {
    /**
     * For RoomDatabase, I am using dagger-hilt dependencies injection
     * to provide instance for other class they needs.
     *
     * On ShoppingItemDatabase, I only need to write a function for provide ShoppingDao
     *
     * ShoppingItemDatabase().shoppingDao -> for calling
     * */

    abstract fun shoppingDao(): ShoppingDao


}