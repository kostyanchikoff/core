package com.kostynchikoff.core

import android.os.Bundle
import androidx.lifecycle.Observer
import com.kostynchikoff.core_application.data.network.ResultApi
import com.kostynchikoff.core_application.data.prefs.SecurityDataSource
import com.kostynchikoff.core_application.ui.activitys.CoreActivity
import com.kostynchikoff.core_application.ui.viewModel.CoreViewModel
import com.kostynchikoff.core_application.utils.extensions.safeClickListener
import com.kostynchikoff.core_application.utils.extensions.toast
import com.kostynchikoff.core_application.utils.safeApiCall
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.inject
import retrofit2.http.GET

class MainActivity : CoreActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


}

//class ExampleDataSource(private val apiService: ApiService) {
//
//    suspend fun getUser() = safeApiCall {
//        apiService.getUsers()
//    }
//}
//
//class ExampleRepository(private val dataSource: ExampleDataSource) {
//
//    suspend fun loadUser() = dataSource.getUser()
//}
//
//class ExampleUseCase(private val repos: ExampleRepository) : CoreUseCase<Nothing, ResultApi<Unit>> {
//    override suspend fun execute(data: Nothing?): ResultApi<Unit> = repos.loadUser()
//}
//
//class ExampleViewModel(private val useCase: ExampleUseCase) : CoreViewModel() {
//
//    private val pref by inject<SecurityDataSource>()
//
//    fun setToken(){
//        pref.addToken("Hello")
//    }
//
//    fun getToken() = pref.getToken()
//}
//
//interface CoreUseCase<in I, out V> {
//    suspend fun execute(data: I? = null): V
//}
//
//interface ApiService {
//
//    @GET("/api/v1/test")
//    suspend fun getUsers()
//}