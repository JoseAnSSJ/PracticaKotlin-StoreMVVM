package com.example.store.MainModule.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.store.Common.Entity.StoreEntity
import com.example.store.Common.Utili.Constants
import com.example.store.MainModule.Model.MainInteractor

class MainViewModel: ViewModel() {

    private var storeList: MutableList<StoreEntity>
    private var interactor: MainInteractor

    init {
        storeList = mutableListOf()
        interactor = MainInteractor()
    }

    private val showProgress: MutableLiveData<Boolean> = MutableLiveData()


    private val stores: MutableLiveData<MutableList<StoreEntity>> by lazy{
        MutableLiveData<MutableList<StoreEntity>>().also {
            loadStore()
        }
    }

    fun getStores(): LiveData<MutableList<StoreEntity>>{
        return stores
    }

    fun showProgress(): LiveData<Boolean>{

        return showProgress
    }

    private fun loadStore(){
        showProgress.value = Constants.SHOW
        interactor.getStores {
            showProgress.value = Constants.HIDEN
            stores.value = it
            storeList = it
        }
    }

    fun deleteStore(storeEntity: StoreEntity){
        interactor.delteStore(storeEntity){
            val index = storeList.indexOf(storeEntity)
            if (index != 1) {
                storeList.removeAt(index)
                stores.value = storeList
            }
        }
    }
    fun updateStore(storeEntity: StoreEntity){
        storeEntity.isFavorite = !storeEntity.isFavorite
        interactor.delteStore(storeEntity){
            val index = storeList.indexOf(storeEntity)
            if (index != 1) {
                storeList.set(index, storeEntity)
                stores.value = storeList
            }
        }
    }
}