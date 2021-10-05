package com.example.spacexships.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacexships.data.ShipsData
import com.example.spacexships.retriever.ShipsRetriever
import kotlinx.coroutines.*

class ShipsViewModel: ViewModel() {
    private lateinit var resultList: ArrayList<ShipsData>

    private val shipsData: MutableLiveData<ArrayList<ShipsData>> by lazy {
        MutableLiveData<ArrayList<ShipsData>>().also {
            retrieveShips()
        }
    }

    fun getShips(): LiveData<ArrayList<ShipsData>> {
        return shipsData
    }

    private fun retrieveShips(){
        viewModelScope.launch {
            resultList = ShipsRetriever().getShips()
            shipsData.value = resultList
        }
    }
}