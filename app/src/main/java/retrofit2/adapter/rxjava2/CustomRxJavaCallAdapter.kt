/*
 * Copyright (C) 2016 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package retrofit2.adapter.rxjava2

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response

internal class CustomRxJava2CallAdapter<R>(private val responseType: Type, private val scheduler: Scheduler?, private val isAsync: Boolean,
                                     private val isResult: Boolean, private val isBody: Boolean, private val isFlowable: Boolean, private val isSingle: Boolean, private val isMaybe: Boolean,
                                     private val isCompletable: Boolean) : CallAdapter<R, Any> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): Any {
        val responseObservable = if (isAsync)
            CallEnqueueObservable(call)
        else
            CallExecuteObservable(call)

        var observable: Observable<*>
        if (isResult) {
            observable = ResultObservable(responseObservable)
        } else if (isBody) {
            observable = BodyObservable(responseObservable)
        } else {
            observable = responseObservable
        }

        if (scheduler != null) {
            observable = observable.subscribeOn(scheduler)
        }

        if (isFlowable) {
            return observable.toFlowable(BackpressureStrategy.LATEST)
        }
        if (isSingle) {
            return observable.singleOrError()
        }
        if (isMaybe) {
            return observable.singleElement()
        }
        if (isCompletable) {
            return observable.ignoreElements()
        }
        return observable
    }
}
