package retrofit2.adapter.rxjava2

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import darylsze.rxkotlinstarter.Utils.CrossContextDialogManager
import darylsze.rxkotlinstarter.Utils.ProgressDialogManager
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by windsze on 21/8/2017.
 * HKMovie. GT.
 */

val REFRESH_TOKEN_EXPIRED = 410
val TOKEN_EXPIRED = 401
val INTERNAL_SERVER_EXCEPTION = 500

class RxJavaCallHandlerService(
        private val sp: SharedPreferences,
        private val gson: Gson,
        private val client: OkHttpClient,
        private val context: Context,
        private val dialogManager: CrossContextDialogManager,
        private val progressDialogManager: ProgressDialogManager,
        private val doShowLoadingDialog: Boolean = true
) : RxJavaCallHandler {
    override fun <T> handleException(call: Call<T>, err: Throwable): Observable<out Nothing> {

        // skip unnecessary exception handling for protopuf
        if (call.request().url().url().toString().contains("movie6-web-app-dev.appspot.com")){
            Timber.d("found error in RxJavaCallHandlerService, but is protopuf error, so skip it")
            return Observable.error(err)
        }

        // print error out.
        err.printStackTrace()

        Timber.e("error found.")

        return when (err) {
            is HttpException                             -> handleHttpException(call, err)
            is ConnectException, is UnknownHostException -> handleNoNetworkException(call, err)
            is SocketTimeoutException                    -> handleTimeoutException(call, err)
            else                                         -> {
                Timber.d("in handleException of RxJavaCallHandlerService, throw err out.")
                throw err
            }
        }
    }

    private fun <T> handleTimeoutException(call: Call<T>, err: SocketTimeoutException): Observable<out Nothing> {
        Timber.d("socket timeout")
        throw SocketTimeoutException()
    }

    private fun <T> handleNoNetworkException(call: Call<T>, err: Throwable): Observable<out Nothing> {
        Timber.d("no network available")
        throw NO_NETWORK_ERROR()
    }

    private fun <T> handleHttpException(call: Call<T>, err: HttpException): Observable<out Nothing> {
        Timber.e("In RxJavaCallHandlerService. Received http exception for url ${call.request().url()} with body ${err.message()}", err)

        throw err
//        // only handled 401, and 410 exception
//        when (err.code()) {
//            TOKEN_EXPIRED, REFRESH_TOKEN_EXPIRED -> {
//                // skip checking oauth token if request from login page.
//                if (isLoginRequest(call))
//                    throw err
//                val oauthToken = sp.getAccountInfo()?.oAuthToken
//                if (oauthToken == null) {
//                    Timber.d("no oauth token found. Go to login page")
//                    gotoLoginPage()
//                    // not pass through
//                    return Observable.empty()
//                }
//                return handleOAuthTokenExpiration(oauthToken, call, err)
//            }
//            INTERNAL_SERVER_EXCEPTION            -> {
//                val errRes = err.response().errorBody().string()
//                try {
//                    val obj = errRes.toJsonObj<ApiError>()
//                    Timber.d("parsed into ApiError")
//                    throw API_ERROR(obj)
//                } catch (e: Exception) {
//                    throw err
//                }
//            }
//            else                                 -> {
//                context.alert {
//                    title = err.code().toString()
//                    message = err.response().errorBody().string()
//                    yesButton { message = "OK" }
//                }.show()
//                return Observable.empty()
//            }
//        }
//
    }

    //    private fun <T> handleOAuthTokenExpiration(oauthToken: OAuthToken, call: Call<T>, err: HttpException): Observable<out Nothing> {
//        Timber.d("in handleOAuthTokenExpiration ")
//        when (err.code()) {
//            TOKEN_EXPIRED         -> {
//                // refresh token
//                val refreshRequest = makeRefreshRequest(
//                        domain = getApiDomain(),
//                        gson = gson,
//                        refreshToken = oauthToken.refreshToken
//                )
//                val response = client.newCall(refreshRequest).execute()
//                Timber.d("call done: ${response.body()}}")
//                if (response.isSuccessful) {
//                    val newOAuthToken = response.body().string().toJsonObj<OAuthToken>()
//                    sp.saveOAuthToken(newOAuthToken)
//                    Timber.d("token reloaded and saved. Done nothing.")
//                    context.runOnUiThread {
//                        toast("Please reload this page")
//                    }
//                } else {
//                    Timber.e("Refresh token unsuccessful")
//                    gotoLoginPage()
//                }
//                // call request again in retry(1)
//                return Observable.empty()
//
//            }
//
//            REFRESH_TOKEN_EXPIRED -> {
//                Timber.d("refresh token expired, go to login page")
//                gotoLoginPage()
//                return Observable.empty()
//
//            }
//
//            else                  -> {
//                Timber.d("Unknown error code in handleOAuthTokenExpiration. Error code is not 401 or 410, but ${err.code()}")
//                context.runOnUiThread {
//                    toast("Unknown error on RxJavaCallHandlerService.")
//                }
//                return Observable.empty()
//            }
//        }
//    }
//
//    private fun <T> isLoginRequest(call: Call<T>): Boolean {
//        return call.request().url().toString().contains("login")
//    }
//
//    private fun makeRefreshRequest(domain: String, gson: Gson, refreshToken: String): Request {
//        val body = gson.toJson(LoginServiceV2.RefreshTokenRequest(refreshToken))
//        val JSON = MediaType.parse("application/json; charset=utf-8")
//
//        val request = Request.Builder()
//                .url("$domain/api/v1/auth/pos/refresh")
//                .post(RequestBody.create(JSON, body))
//                .build()
//        return request
//    }
//
//    private fun gotoLoginPage() {
//        // unauthorized
//        // refresh token error. redirect to login page
//        context.startActivity(SelectUserActivity.createIntent(context).clearTask().newTask())
//        context.runOnUiThread {
//            toast("Token expired. Please login again")
//        }
//    }
//
    override fun <T> handlePostApiCall(call: Call<T>, obj: Any) {
        Timber.d("after http call: $obj")

//        if (context.defaultSharedPreferences.getBoolean("VERBOSE_API_RESPONSE", false)) {
//            dialogManager.pushDialog {
//                alert {
//                    title = call.request().url().toString()
//                    negativeButton("Body") { alert { title = "Body"; message = obj.toString() }.show() }
//                    positiveButton("Request") { alert { title = "Request"; message = call.request().toString() }.show() }
//                }
//            }
//        }
    }

    override fun <T> doOnRequest(call: Call<T>) {
        if (doShowLoadingDialog)
            progressDialogManager.showProgressDialog()
    }

    override fun <T> doOnTerminate(call: Call<T>) {
        if (doShowLoadingDialog)
            progressDialogManager.dismissProgressDialog()
    }

//    fun toggleVerboseApiResponse() = context.defaultSharedPreferences
//            .toggle("VERBOSE_API_RESPONSE", false)
//
//    fun toggleVerboseApiError() = context.defaultSharedPreferences
//            .toggle("VERBOSE_API_ERROR", false)
}


class NO_NETWORK_ERROR : Throwable()

//class API_ERROR(val errObj: ApiError) : Throwable()




