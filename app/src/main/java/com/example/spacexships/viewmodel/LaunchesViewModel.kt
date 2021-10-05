package com.example.spacexships.viewmodel

import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacexships.data.LaunchData
import com.example.spacexships.retriever.ShipsRetriever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LaunchesViewModel : ViewModel() {
    val launchesData: MutableLiveData<MutableList<LaunchData>> by lazy {
        MutableLiveData<MutableList<LaunchData>>()
    }
    lateinit var progressBar: ProgressBar

    fun getLaunchesData(ids: Array<String>) {

        viewModelScope.launch(Dispatchers.IO) {
            val launches = mutableListOf<LaunchData>()
            for (i in ids) {
                launches.add(ShipsRetriever().getLaunchData("launches/$i"))
                progressBar.progress += 100/ids.count()
            }
            withContext(Dispatchers.Main) {

                launchesData.value = launches as ArrayList<LaunchData>
            }

        }
    }

}