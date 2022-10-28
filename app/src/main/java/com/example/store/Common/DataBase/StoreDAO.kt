package com.example.store.Common.DataBase

import androidx.room.*
import com.example.store.Common.Entity.StoreEntity

@Dao
interface StoreDAO {
    @Query("Select * from StoreEntity")
    fun getAllStore(): MutableList<StoreEntity>

    @Insert
    fun addStore(storeEntity: StoreEntity) : Long

    @Update
    fun updateStore(storeEntity: StoreEntity)

    @Delete
    fun deleteStore(storeEntity: StoreEntity)

    @Query("Select * from StoreEntity where id = :id")
    fun getStoreById(id: Long): StoreEntity

}