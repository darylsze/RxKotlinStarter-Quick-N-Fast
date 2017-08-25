package darylsze.rxkotlinstarter.Utils

import android.app.ProgressDialog
import android.content.Context
import darylsze.rxkotlinstarter.Application.Component
import darylsze.rxkotlinstarter.Extension.catchAllExceptions
import darylsze.rxkotlinstarter.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.ReplaySubject
import org.jetbrains.anko.indeterminateProgressDialog

/**
 * Created by windsze on 25/8/2017.
 * HKMovie. GT.
 */

/**
 * Show progress dialog in cross context feature, same as [CrossContextDialogManager]
 * This dialog manager has been registered in BaseActivity
 * in which pushes dialog to UI. Dialog will still be able to show if target activity is closed,
 * for example, user changed activity, the dialog will be pushed to next activity.
 */
class ProgressDialogManager : Component {
    private var progressSignal = ReplaySubject.create<Boolean>()
    private var disposable: Disposable? = null

    private var progressDialog: ProgressDialog? = null

    override fun registerWithContext(context: Context) {
        disposable = progressSignal
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (it == null)
                        throw Exception("progressSignal is null")

                    if (it) {
                        progressDialog?.dismiss()
                        progressDialog = context
                                .indeterminateProgressDialog(message = context.getString(R.string.progress_dialog_loading_message)) { setCancelable(false) }
                                .apply { show() }
                    } else {
                        progressDialog?.dismiss()
                    }
                }
                .doOnComplete { progressDialog?.dismiss() }
                .catchAllExceptions(context)
                .subscribe()
    }

    override fun unregister() {
        //Dismiss any existing progress dialog
        progressSignal.onComplete()

        disposable?.dispose()
        progressSignal = ReplaySubject.create<Boolean>()
    }

    fun showProgressDialog() {
        progressSignal.onNext(true)
    }

    fun dismissProgressDialog() {
        progressSignal.onNext(false)
    }

}
