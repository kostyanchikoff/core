package com.kostynchikoff.core_application.di

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kostynchikoff.core_application.data.constants.CoreConstant
import com.kostynchikoff.core_application.data.constants.CoreConstant.PREF_SOURCES_LOCAL
import com.kostynchikoff.core_application.data.constants.CoreVariables
import com.kostynchikoff.core_application.data.network.interceptors.DefaultHeadersInterceptor
import com.kostynchikoff.core_application.data.network.interceptors.OAuthInterceptor
import com.kostynchikoff.core_application.data.prefs.SecurityDataSource
import com.kostynchikoff.core_application.data.prefs.SourcesLocalDataSource
import com.kostynchikoff.core_application.utils.delegates.LocaleDelegate
import com.kostynchikoff.core_application.utils.network.getSsl
import com.securepreferences.SecurePreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

val coreModule = module {
    factory { createRetrofitOkHttpClient(androidContext()) }
    single { SecurityDataSource(SecurePreferences(androidContext())) }
    single {
        SourcesLocalDataSource(
            androidContext().getSharedPreferences(
                PREF_SOURCES_LOCAL,
                Context.MODE_PRIVATE
            )
        )
    }

    single { createApollo(createApolloOkHttpClient()) }
}

/**
 * OkHttpClient использовать только для retrofit
 */
fun createRetrofitOkHttpClient(context: Context): OkHttpClient {
    val (manager, factory) = getSsl()
    val httpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpBuilder = OkHttpClient.Builder()
        .addNetworkInterceptor(DefaultHeadersInterceptor(LocaleDelegate(context).getLocale()))
        .addInterceptor(OAuthInterceptor())
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(CoreConstant.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(CoreConstant.READ_TIMEOUT, TimeUnit.MILLISECONDS)
        okHttpBuilder.sslSocketFactory(factory, manager)
        okHttpBuilder.hostnameVerifier(HostnameVerifier { _, _ -> true })
    return okHttpBuilder.build()
}

/**
 * OkHttpClient использовать только для Apollo
 */
fun createApolloOkHttpClient(): OkHttpClient {
    val (manager, factory) = getSsl()
    val httpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpBuilder = OkHttpClient.Builder()
        .addNetworkInterceptor(OAuthInterceptor())
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(CoreConstant.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(CoreConstant.READ_TIMEOUT, TimeUnit.MILLISECONDS)
        okHttpBuilder.sslSocketFactory(factory, manager)
        okHttpBuilder.hostnameVerifier(HostnameVerifier { _, _ -> true })
    return okHttpBuilder.build()
}

/**
 * Создание api сервисов для Retrofit
 */
inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient
): T = Retrofit.Builder()
    .baseUrl(CoreVariables.BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(createGson()))
    .build()
    .create(T::class.java)

fun createGson(): Gson = GsonBuilder().setLenient().create()

/**
 * Создание сервиса для Apollo
 */
fun createApollo(okHttp: OkHttpClient): ApolloClient =
    ApolloClient.builder().serverUrl(CoreVariables.BASE_APOLLO_URL).okHttpClient(okHttp).build()






