package darylsze.rxkotlinstarter.Extension

import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.bindings.SingletonBinding
import com.google.gson.Gson
import darylsze.rxkotlinstarter.Application.getApiDomain
import darylsze.rxkotlinstarter.Retrofit.Service.ExampleApiService
import darylsze.rxkotlinstarter.Retrofit.Service.ExampleApiServiceImpl
import darylsze.rxkotlinstarter.Utils.CrossContextDialogManager
import darylsze.rxkotlinstarter.Utils.ProgressDialogManager
import okhttp3.OkHttpClient
import okio.Buffer
import org.jetbrains.anko.defaultSharedPreferences
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.CustomRxJava2CallAdapterFactory
import retrofit2.adapter.rxjava2.RxJavaCallHandlerService
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

inline fun <reified T : kotlin.Any> Kodein.Builder.retrofitService(service: Class<T>): SingletonBinding<T> =
        singleton { instance<Retrofit>().create(service) }