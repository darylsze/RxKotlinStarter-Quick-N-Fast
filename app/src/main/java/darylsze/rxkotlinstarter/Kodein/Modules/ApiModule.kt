package darylsze.rxkotlinstarter.Kodein.Modules

import com.github.salomonbrys.kodein.*
import darylsze.rxkotlinstarter.Application.getApiDomain
import darylsze.rxkotlinstarter.Retrofit.Service.ExampleApiServiceImpl
import retrofit2.Retrofit

val apiModule = Kodein.Module {
    bind<ExampleApiServiceImpl>() with singleton {
        val retrofit: Retrofit = with(getApiDomain()).instance()
        ExampleApiServiceImpl(retrofit = retrofit)
    }
}