package com.example.store.MainModule.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.store.Common.Entity.StoreEntity
import com.example.store.MainModule.Model.EditStoreInteractor

class EditSotreViewModel: ViewModel() {

    private val storeSelect = MutableLiveData<StoreEntity>()
    private val showFab = MutableLiveData<Boolean>()
    private val result = MutableLiveData<Any>()


    private val interactor: EditStoreInteractor

    init {
        interactor = EditStoreInteractor()
    }

    fun setSotoreSelect(storeEntity: StoreEntity){
        storeSelect.value = storeEntity
    }

    fun getStoreSelect(): LiveData<StoreEntity>{
        return storeSelect
    }

    fun setShowFab(isVisible: Boolean){
        showFab.value = isVisible
    }

    fun getShowFab(): LiveData<Boolean>{
        return showFab
    }

    fun setResult(value: Any){
        result.value = value
    }

    fun getResult(): LiveData<Any>{
        return result
    }

    fun saveStore(storeEntity: StoreEntity){
        interactor.saveStore(storeEntity){ newID ->
            result.value = newID
        }
    }

    fun updateStore(storeEntity: StoreEntity){
        interactor.updateStore(storeEntity){ storeUpdate ->
            result.value = storeUpdate
        }
    }

}