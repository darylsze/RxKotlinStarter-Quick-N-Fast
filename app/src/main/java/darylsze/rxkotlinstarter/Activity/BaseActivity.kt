package darylsze.rxkotlinstarter.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.salomonbrys.kodein.KodeinInjected
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import darylsze.rxkotlinstarter.Application.Component
import darylsze.rxkotlinstarter.Extension.Lifecycle
import darylsze.rxkotlinstarter.Extension.MyCompositeDisposable
import timber.log.Timber

open class BaseActivity : AppCompatActivity(), KodeinInjected {
    override val injector = KodeinInjector()
    val onResumeComponents by instance<List<Component>>("resume")
    val onStartComponents by instance<List<Component>>("start")
    val compositeSub by lazy { MyCompositeDisposable(this@BaseActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(appKodein())


        compositeSub.disposeAllWhen(Lifecycle.CREATE)
    }

    override fun onStart() {
        super.onStart()

        onStartComponents
                .forEach {
                    Timber.v("Register start component>>${it.javaClass.name}")
                    it.registerWithContext(this)
                }
        compositeSub.disposeAllWhen(Lifecycle.START)
    }

    override fun onResume() {
        super.onStart()

        onResumeComponents
                .forEach {
                    Timber.v("Register resume component>>${it.javaClass.name}")
                    it.registerWithContext(this)
                }

        compositeSub.disposeAllWhen(Lifecycle.RESUME)
    }

    override fun onPause() {
        super.onPause()

        onResumeComponents
                .forEach {
                    Timber.v("Unregister resume component>>${it.javaClass.name}")
                    it.unregister()
                }
        compositeSub.disposeAllWhen(Lifecycle.PAUSE)
    }


    override fun onStop() {
        super.onStop()
        onStartComponents
                .forEach {
                    Timber.v("Unregister start component>>${it.javaClass.name}")
                    it.unregister()
                }
        compositeSub.disposeAllWhen(Lifecycle.STOP)
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeSub.disposeAllWhen(Lifecycle.DESTROY)
    }

}