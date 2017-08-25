package darylsze.rxkotlinstarter.Demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.enabled
import com.jakewharton.rxbinding2.view.visibility
import com.jakewharton.rxbinding2.widget.textChanges
import darylsze.rxkotlinstarter.Extension.bindTo
import org.jetbrains.anko.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class RxViewBindingActivity : AppCompatActivity() {

    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ui = MainViewUI()
        ui.setContentView(this)

        // use input changes to determine btnSubmit is enable or not
        // bindTo is a infix function helps subscribe the observable and assign output value
        // to a consumer function.
        ui.btnSubmit.enabled() bindTo ui.txtInput
                .textChanges()
                .map { input ->
                    val inputNotEmpty = input.toString().trim().isNotEmpty()
                    inputNotEmpty
                }

        // handle btnSubmit click event.
        ui.btnSubmit.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .doOnNext {
                    Timber.d("Wow, you clicked submit!")
                    counter++
                }
                .subscribe()
    }

}

class MainViewUI : AnkoComponent<RxViewBindingActivity> {
    lateinit var lblLabel: TextView
    lateinit var txtInput: EditText
    lateinit var btnSubmit: Button

    override fun createView(ui: AnkoContext<RxViewBindingActivity>) = with(ui) {
        verticalLayout {
            // label
            lblLabel = textView {
                text = "Input anything and click submit"
            }.lparams(wrapContent, wrapContent) {
                gravity = Gravity.CENTER_HORIZONTAL
            }

            // input
            txtInput = editText {
                hint = "Input anything."
            }.lparams(wrapContent, wrapContent) {
                gravity = Gravity.CENTER_HORIZONTAL
            }

            // submit
            btnSubmit = button("Submit").lparams(wrapContent, wrapContent) {
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }
    }
}