package retrofit2.adapter.rxjava2

import io.reactivex.Observable
import retrofit2.Call

interface RxJavaCallHandler {
    fun <T> handleException(call: Call<T>, err: Throwable): Observable<out Nothing>
    fun <T> handlePostApiCall(call: Call<T>, obj: Any)
    fun <T> doOnRequest(call: Call<T>)
    fun <T> doOnTerminate(call: Call<T>)
}