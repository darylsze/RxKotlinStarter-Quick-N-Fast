package darylsze.rxkotlinstarter.Extension

import darylsze.rxkotlinstarter.Application.getApiDomain
import okhttp3.Request
import okio.Buffer
import retrofit2.Retrofit


fun Request.toReadableString(): String {
    val request = this.newBuilder().build()
    val buffer = Buffer()
    request.body()!!.writeTo(buffer)
    return buffer.readUtf8()
}