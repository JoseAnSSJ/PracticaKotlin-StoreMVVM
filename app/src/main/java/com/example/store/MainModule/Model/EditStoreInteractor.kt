package com.example.store.MainModule.Model

import com.example.store.Common.Entity.StoreEntity
import com.example.store.StoreAplication
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditStoreInteractor {
    fun saveStore(storeEntity: StoreEntity, callback: (Long) -> Unit){
        doAsync {
            val newID = StoreAplication.database.storeDao().addStore(storeEntity)
            uiThread {
                callback(newID)
            }
        }
    }

    fun updateStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit){
        doAsync {
            StoreAplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }
}