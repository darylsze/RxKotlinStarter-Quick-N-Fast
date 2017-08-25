package darylsze.rxkotlinstarter.Kodein.Modules

import android.content.Context
import com.github.salomonbrys.kodein.*
import com.google.gson.Gson
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

/**
 * Created by windsze on 25/8/2017.
 * HKMovie. GT.
 */

fun networkModule(context: Context) = Kodein.Module {
    bind<Retrofit>() with multiton { domain: String ->
        // by using multiton (for more detail, please refer to kodein),
        // retrofit instance would be created everytime when given domain is not the same as previous
        // it provides flexibility for multiple domain usage through whole application
        Retrofit.Builder()
                .baseUrl(domain)
                .client(instance<OkHttpClient>())
                .addCallAdapterFactory(CustomRxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create(instance<Gson>()))
                .build()
    }


    bind<OkHttpClient>() with singleton {
        OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor { chain ->
                    // simply for logging request and response.
                    val request = chain.request()
                    val response = chain.proceed(request)

                    val copy = request.newBuilder().build()
                    val b = Buffer()
                    copy.body()?.writeTo(b)

//                    Timber.i("Requesting: ${request.method()}  ${request.url()} \n Body: ${b.readUtf8()} \n Header: ${request.headers()}")
//                    Timber.i("Responding: ${response.code()} with headers ${response.headers()}")
//
                    response
                }
                .build()
    }

    bind<RxJavaCallHandlerService>() with singleton {
        RxJavaCallHandlerService(
                context.defaultSharedPreferences,
                instance<Gson>(),
                instance<OkHttpClient>(),
                context,
                instance<CrossContextDialogManager>(),
                instance<ProgressDialogManager>())
    }
}

