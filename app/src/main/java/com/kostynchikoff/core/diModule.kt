package com.kostynchikoff.core

import com.kostynchikoff.core_application.di.createWebService
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

//val httpModule = module {
//    single { createWebService<ApiService>(get()) }
//}
//
//val dataSourceModule = module {
//    factory {
//        ExampleDataSource(get())
//    }
//}
//
//val useCaseModule = module{
//
//    factory {
//        ExampleUseCase(get())
//    }
//}
//
//
//val repositoryModule = module {
//
//    factory {
//        ExampleRepository(get())
//    }
//
//}
//
//
//val viewModelModule = module {
//
//    viewModel {
//        ExampleViewModel(get())
//    }
//}