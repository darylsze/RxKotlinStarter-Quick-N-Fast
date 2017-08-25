package darylsze.rxkotlinstarter.Utils

import android.content.Context
import android.content.DialogInterface
import darylsze.rxkotlinstarter.Application.Component
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.ReplaySubject
import org.jetbrains.anko.AlertBuilder
import timber.log.Timber

/**
 * Created by windsze on 25/8/2017.
 * HKMovie. GT.
 */

/**
 * Show dialog in cross context feature, same as [ProgressDialogManager]
 * This dialog manager has been registered in BaseActivity
 * in which pushes dialog to UI. Dialog will still be able to show if target activity is closed,
 * for example, user changed activity, the dialog will be pushed to next activity.
 */
class CrossContextDialogManager : Component {
    private var dialogSignal = ReplaySubject.create<(Context) -> AlertBuilder<DialogInterface>>()
    private var disposable: Disposable? = null

    override fun registerWithContext(context: Context) {
        disposable = dialogSignal
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    Timber.d("Creating alert...")
                    it.invoke(context).show()
                }
                .subscribe()
    }

    override fun unregister() {
        dialogSignal.onComplete()

        disposable?.dispose()

        //Create a new signal
        dialogSignal = ReplaySubject.create<(Context) -> AlertBuilder<DialogInterface>>()
    }

    fun pushDialog(f: Context.() -> AlertBuilder<DialogInterface>) {
        dialogSignal.onNext(f)
    }
}