package darylsze.rxkotlinstarter.Extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import darylsze.rxkotlinstarter.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import org.joda.time.Instant
import timber.log.Timber


fun <T, R : T> Consumer<T>.bindTo(observable: Observable<R>, onError: Consumer<Throwable> = EMPTY_ON_ERROR): Disposable = observable.subscribe(this, onError)

infix fun <T, R : T> Consumer<T>.bindTo(observable: Observable<R>): Disposable = bindTo(observable, onError = EMPTY_ON_ERROR)
infix fun <T, R : T> Consumer<T>.bindTo(observable: Lazy<Observable<R>>): Disposable = bindTo(observable.value, onError = EMPTY_ON_ERROR)


internal val EMPTY_ON_ERROR = Consumer<Throwable> {}

inline fun <T> Observable<T>.withErrorToast(context: Context, msg: String = context.getString(R.string.error_msg_network_failure)): Observable<T> =
        doOnError { err ->
            when (err) {
            /**
             * For any case [NO_NETWORK_ERROR] has been throw, show "No Network connection"
             */
                is NO_NETWORK_ERROR -> {
                    Timber.d("no network connection")
                    context.runOnUiThread {
                        if (!(context as Activity).isFinishing)
                            toast(context.getString(R.string.error_msg_network_failure))
                    }
                }
            /**
             * For all self-created error, show error message out.
             */
                is ICustomError     -> {
                    context.runOnUiThread {
                        if (!(context as Activity).isFinishing)
                            toast(err.localizedMessage)
                    }
                    err.printStackTrace()
                }
            /**
             * For unknown error, show "Unknown error."
             */
                else                -> {
                    context.runOnUiThread {
                        if (!(context as Activity).isFinishing)
                            toast(context.getString(R.string.error_unknown))
                    }
                    err.printStackTrace()
                }
            }

        }


fun main(args: Array<String>) {
    val error = Error("HIHI")

    // only 1, 4 can print message successfully
    print("1" + error.localizedMessage)
    print("2" + error.cause?.message)
    print("3" + error.cause?.localizedMessage)
    print("4" + error.message)

}

inline fun <T> Observable<T>.debug(tag: String = ""): Observable<T> =
        this
                .doOnDispose { Timber.d("onDispose") }
                .doOnNext { Timber.d("$tag onNext $it") }
                .doOnError { Timber.d("$tag onError $it") }
                .doOnSubscribe { Timber.d("$tag onSubscribe") }
                .doOnTerminate { Timber.d("$tag onTerminate") }
                .doOnComplete { Timber.d("$tag onCompleted") }

fun getCallerClassName(clazz: Class<*>): String? {
    val stackTrace = Thread.currentThread().stackTrace
    val className = clazz.name
    var classFound = false
    for (i in 1..stackTrace.size - 1) {
        val element = stackTrace[i]
        val callerClassName = element.className
        // check if class name is the requested class
        if (callerClassName == className)
            classFound = true
        else if (classFound) return callerClassName
    }
    return null
}


fun Disposable.addTo(allDisposable: MyCompositeDisposable) {
    allDisposable.add(this)
}

class Empty : Any()

fun <T> Observable<T>.onErrorStopEvent(): Observable<T> = this.onErrorResumeNext { err: Throwable -> Observable.empty<T>() }

inline fun <reified T> Observable<T>.catchAllExceptions(context: Context, skipErrorToast: Boolean = false): Observable<T> {
    if (skipErrorToast) {
        return onErrorStopEvent()
    } else {
        return withErrorToast(context)
                .onErrorStopEvent()
    }
}

fun <T> Observable<T?>.filterNotNull(): Observable<T> {
    return this.filter { it != null } as Observable<T>
}

inline fun <T> Observable<List<T>>.isEmptyList(): Observable<Boolean> {
    return map { it.isEmpty() }
}

inline fun <T> Observable<List<T>>.isNotEmptyList(): Observable<Boolean> {
    return map { it.isNotEmpty() }
}


inline fun Observable<Boolean>.not(): Observable<Boolean> = map { !it }

fun <T> Observable<T>.autoNetworkThread(): Observable<T> {
    return this.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
}


inline fun <E> Collection<E>.isNotEmptyObs(): Observable<Boolean> = Observable.create { isNotEmpty() }

inline fun ViewGroup.childViews(): Consumer<List<View>> =
        Consumer {
            removeAllViews()
            it.forEach {
                addView(it.removeParentView())
            }
        }

inline fun Observable<out Number>.toInt(): Observable<Int> = this.map { it.toInt() }
inline fun Observable<out Number>.toFloat(): Observable<Float> = this.map { it.toFloat() }


infix fun Observable<Boolean>.or(o: Observable<Boolean>) = Observables.combineLatest(this, o) { left, right -> left || right }
infix fun Observable<Boolean>.and(o: Observable<Boolean>) = Observables.combineLatest(this, o) { left, right -> left && right }

fun Observable<out Number>.toStringObs() = this.map { it.toString() }

class NO_NETWORK_ERROR : Throwable()

interface ICustomError
class CustomError(msg: String) : Error(msg), ICustomError


