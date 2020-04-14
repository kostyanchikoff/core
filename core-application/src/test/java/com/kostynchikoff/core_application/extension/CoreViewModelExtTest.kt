package com.kostynchikoff.core_application.extension

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.ui.viewModel.ViewModelTest
import com.kostynchikoff.core_application.utils.extensions.launch
import com.kostynchikoff.core_application.utils.extensions.observeOnce
import com.kostynchikoff.core_application.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.koin.core.KoinComponent

class CoreViewModelExtTest : KoinComponent {


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testingDispatcher = Dispatchers.Unconfined

    val viewModel = ViewModelTest()




    @Before
    fun doBeforeEach() {
        Dispatchers.setMain(testingDispatcher)
    }

    @Test
    fun `error launch viewModel`(){
        viewModel.launch({
            safeApiCall {
               throw Exception()
            }
        },{
           // do nothing
        })

        viewModel.statusLiveData.observeOnce {
            Assert.assertEquals(Status.ERROR, it)
        }
        viewModel.errorLiveData.observeOnce {
            Assert.assertEquals("Ошибка : Exception null", it)
        }

    }

    @Test
    fun `success launch viewModel`(){
        viewModel.launch({
            safeApiCall {
                "Hello world"
            }
        },{
            Assert.assertEquals("Hello world", it)
        })

        viewModel.statusLiveData.observeOnce {
            Assert.assertEquals(Status.HIDE_LOADING, it)
        }
    }

    @Test
    fun `incorrect data launch viewModel`(){
        val msg = "Ошибка : ClassCastException java.lang.String cannot be cast to java.lang.Integer"
        viewModel.launch({
            safeApiCall {
                "Hello world" as Int
            }
        },{
            Assert.assertEquals("Hello world", it)
        },{
            Assert.assertEquals(msg, it)
        })

        viewModel.statusLiveData.observeOnce {
            Assert.assertEquals(Status.ERROR, it)
        }

        viewModel.errorLiveData.observeOnce {
            Assert.assertEquals(msg, it)
        }
    }

    @After
    fun doAfterEach() {
        Dispatchers.resetMain()
    }


}