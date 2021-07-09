package com.example.pictureoftheday.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictureoftheday.ui.main.model.PODServerResponseData
import com.example.pictureoftheday.ui.main.model.PODRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {

    fun getData(minusDays: Int): LiveData<AppState> {
        sendServerRequest(minusDays)
        return liveDataForViewToObserve
    }

    private fun sendServerRequest(minusDays: Int) {
        liveDataForViewToObserve.value = AppState.Loading(null)
        val apiKey: String = com.example.pictureoftheday.BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            AppState.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitPODQuery().getPictureOfTheDay(apiKey, getPreviousDateForRequest(minusDays))
                .enqueue(object : Callback<PODServerResponseData> {
                    override fun onResponse(
                        call: Call<PODServerResponseData>,
                        response: Response<PODServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value = AppState.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                AppState.Error(Throwable("Undefined Error"))
                            } else {
                                AppState.Error(Throwable(message))
                            }
                        }
                    }
                    override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                        liveDataForViewToObserve.value = AppState.Error(t)
                    }
                })
        }
    }



    fun getPreviousDateForRequest(minusDays: Int): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -minusDays)
        dateFormat.format(calendar.time)
        return dateFormat.format(calendar.time).toString()
    }


}