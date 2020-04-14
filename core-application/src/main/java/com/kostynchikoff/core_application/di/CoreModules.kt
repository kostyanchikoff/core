package com.kostynchikoff.core_application.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kostynchikoff.core_application.BuildConfig
import com.kostynchikoff.core_application.data.constants.CoreConstant.SECRET_SHARED_PREF
import com.kostynchikoff.core_application.data.constants.CoreVariables
import com.kostynchikoff.core_application.data.network.AuthInterceptor
import com.kostynchikoff.core_application.data.prefs.SecurityDataSource
import com.kostynchikoff.core_application.ui.viewModel.ViewModelTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val coreModule = module {
    factory { createOkHttpClient() }
    single {
        val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            SECRET_SHARED_PREF,
            masterKeyAlias,
            androidContext(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        SecurityDataSource(sharedPreferences)
    }


}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
    val okHttpBuilder = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(AuthInterceptor())
    return okHttpBuilder.build()
}

inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient
): T = Retrofit.Builder()
    .baseUrl(CoreVariables.BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(createGson()))
    .build()
    .create(T::class.java)

fun createGson(): Gson = GsonBuilder().setLenient().create()

