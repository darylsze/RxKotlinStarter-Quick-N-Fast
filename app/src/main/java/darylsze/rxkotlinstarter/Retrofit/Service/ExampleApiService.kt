package darylsze.rxkotlinstarter.Retrofit.Service

import darylsze.rxkotlinstarter.Extension.autoNetworkThread
import darylsze.rxkotlinstarter.Retrofit.Response.User
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.*

interface ExampleApiService {
    @GET("/users")
    fun userList(): Observable<List<User>>
}

class ExampleApiServiceImpl(retrofit: Retrofit) : ExampleApiService {
    private val apiService = retrofit.create(ExampleApiService::class.java)

    override fun userList() =
            apiService
                    .userList()
                    .autoNetworkThread()
}