package darylsze.rxkotlinstarter.Extension

import android.content.Context
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.doAsync
import timber.log.Timber
import java.util.*

/**
 * Disposable that represents a group of Subscriptions that are unsubscribed together.
 *
 *
 * All methods of this class are thread-safe.
 */
enum class Lifecycle {
    CREATE, RESUME, START, STOP, PAUSE, DESTROY
}

/**
 * A custom implementation with regards to rx's[CompositeSubscription] class.
 * This customization adds [Lifecycle] consideration to subscribe
 * different subscription(s) from inventory when activity goes into certain stage.
 */
class MyCompositeDisposable(val context: Context, vararg disposable: Disposable) : MyDisposable {

    /**
     * contain information of desired lifecycle, in order to disposeAllWhen
     * different disposable according to lifecycle declared.
     */
    private var disposables: MutableList<Pair<Disposable, Lifecycle>> = ArrayList()

    /**
     * Adds a new [Disposable] to this `MyCompositeDisposable` if the
     * `MyCompositeDisposable` is not yet unsubscribed. If the `MyCompositeDisposable` *is*
     * unsubscribed, `add` will indicate this by explicitly unsubscribing the new `Disposable` as
     * well.

     * @param s the [Disposable] to add
     */
    fun add(s: Disposable, l: Lifecycle = Lifecycle.STOP) {
        if (s.isDisposed) {
            return
        }

        // skip existing subscription
        doAsync {
            disposables
                    .filterNot { it.first == s }
                    .apply { disposables.add(s to l) }
        }
    }

    /**
     * Adds collection of [Disposable] to this `MyCompositeDisposable` if the
     * `MyCompositeDisposable` is not yet unsubscribed. If the `MyCompositeDisposable` *is*
     * unsubscribed, `addAll` will indicate this by explicitly unsubscribing all `Disposable` in collection as
     * well.

     * @param disposables the collection of [Disposable] to add
     */
    fun addAll(vararg subNLifes: Pair<Disposable, Lifecycle>) {
        doAsync {
            disposables
                    .filterNot { it.first.isDisposed }
                    .forEach { disposables.add(it) }
        }
    }

    /**
     * Removes a [Disposable] from this `MyCompositeDisposable`, and unsubscribes the
     * [Disposable].
     * @param s the [Disposable] to remove
     */
    fun remove(s: Disposable) {
        disposables
                .filter { it.first == s }
                .forEach {
                    disposables.remove(it)
                }
    }

    override fun disposeAllWhen(lifecycle: Lifecycle) {
        disposables
                .filter { it.second == lifecycle }
                .forEach { it.first.dispose() }
                .apply { Timber.d("Unsubscribed all subscription in lifecycle : ${lifecycle.name}") }
    }

    private fun disposeWhen(disposables: Collection<Pair<Disposable, Lifecycle>>) {
//        var es: MutableList<Throwable>? = null
//        disposable.forEach {
//            try {
//                it.first.unsubscribe()
//            } catch (e: Throwable) {
//                if (es == null) {
//                    es = ArrayList<Throwable>()
//                }
//                es!!.add(e)
//            }
//        }
//
//        Exceptions.throwIfAny(es)
    }
}

/**
 * Disposable returns from [Observable.subscribe] to allow unsubscribing.
 *
 *
 * See the utilities in [Subscriptions] and the implementations in the `rx.disposables` package.
 *
 *
 * This interface is the RxJava equivalent of `IDisposable` in Microsoft's Rx implementation.
 */
interface MyDisposable {

    /**
     * Stops the receipt of notifications on the [Subscriber] that was registered when this Disposable
     * was received.
     *
     *
     * This allows deregistering an [Subscriber] before it has finished receiving all events (i.e. before
     * onCompleted is called).
     */
    fun disposeAllWhen(lifecycle: Lifecycle )

}

