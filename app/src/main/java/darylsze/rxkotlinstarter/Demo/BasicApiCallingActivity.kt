package darylsze.rxkotlinstarter.Demo

import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import com.google.gson.Gson
import darylsze.rxkotlinstarter.Activity.BaseActivity
import darylsze.rxkotlinstarter.Extension.catchAllExceptions
import darylsze.rxkotlinstarter.R
import darylsze.rxkotlinstarter.Retrofit.Service.ExampleApiServiceImpl
import timber.log.Timber

class BasicApiCallingActivity : BaseActivity() {
    val exampleApiService by instance<ExampleApiServiceImpl>()
    val gson by instance<Gson>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exampleApiService
                .userList()
                .doOnNext { response ->
                    Timber.d(gson.toJson(response))
                }
                .doOnError { err ->
                    err.printStackTrace()
                }
                .catchAllExceptions(this)
                .subscribe()
    }
}
