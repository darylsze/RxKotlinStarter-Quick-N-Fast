package darylsze.rxkotlinstarter.Demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.jakewharton.rxbinding2.view.clicks
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import timber.log.Timber

class AnkoViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewComponent = MyActivityUI()
        viewComponent.setContentView(this)


        /**
         * for more detail, refer to
         * @link [https://github.com/Kotlin/anko/wiki/Anko-Layouts]
         */
        viewComponent.btnSayHello
                .clicks()
                .doOnNext { Timber.i("I am Button !") }
                .subscribe()
    }
}

class MyActivityUI : AnkoComponent<AnkoViewActivity> {
    lateinit var name: EditText
    lateinit var btnSayHello: Button

    override fun createView(ui: AnkoContext<AnkoViewActivity>) = with(ui) {
        verticalLayout {
            name = editText {
                hint = "Daryl SZE"
            }
            btnSayHello = button("Say Hello") {
                onClick { ctx.toast("Hello, ${name.text}!") }
            }
        }
    }
}
