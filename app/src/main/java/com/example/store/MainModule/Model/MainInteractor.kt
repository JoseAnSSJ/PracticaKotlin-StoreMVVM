package com.example.store.MainModule.Model

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.store.Common.Entity.StoreEntity
import com.example.store.Common.Utili.Constants
import com.example.store.StoreAplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {



    fun getStores(callback: (MutableList<StoreEntity>) -> Unit){
        val url = Constants.STORES_URL + Constants.GET_ALL
        var storeList = mutableListOf<StoreEntity>()
        val jsonObject = JsonObjectRequest(Request.Method.GET, url, null, { response ->

            val status = response.optInt(Constants.STATUS_PROPETY, Constants.ERROR)
            if(status == Constants.SUCCESS){

                //val jsonList = response.getJSONArray(Constants.STORES_PROPETY).toString()

                val jsonList = response.optJSONArray(Constants.STORES_PROPETY)?.toString()
                if (jsonList != null){
                    val mutableListToken = object : TypeToken<MutableList<StoreEntity>>(){}.type
                    storeList = Gson().fromJson(jsonList,mutableListToken)
                    callback(storeList)

                    return@JsonObjectRequest
                }


            }
            callback(storeList)
        },{
            it.printStackTrace()
            callback(storeList)
        })

        StoreAplication.storeApi.addToRequestQueue(jsonObject)
    }

    fun getStoresRoom(callback: (MutableList<StoreEntity>) -> Unit){
        doAsync {
            val storesList = StoreAplication.database.storeDao().getAllStore()
            uiThread {
                callback(storesList)
            }
        }
    }

    fun delteStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit){
        doAsync {
            StoreAplication.database.storeDao().deleteStore(storeEntity)
            uiThread {
                callback(storeEntity)
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