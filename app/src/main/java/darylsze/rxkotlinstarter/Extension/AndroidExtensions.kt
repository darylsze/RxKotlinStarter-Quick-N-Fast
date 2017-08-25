package darylsze.rxkotlinstarter.Extension

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jakewharton.rxbinding2.view.clicks
import darylsze.rxkotlinstarter.Activity.BaseActivity
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import org.jetbrains.anko.AnkoContext
import org.joda.time.Instant
import timber.log.Timber
import java.util.concurrent.TimeUnit


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun ViewGroup.addAllViews(views: List<View>) =
        views.forEach {
            addView(it.removeParentView())
        }

fun <T : View> T.clicksRtn(): Observable<T> =
        clicks().map { this }

fun <T : View> T.clicksRtnThrottle(millisecond: Long): Observable<T> =
        this.
                clicks()
                .throttleFirst(millisecond, TimeUnit.MILLISECONDS)
                .map { this }


fun View.removeParentView(): View {
    (parent as? ViewGroup)?.removeView(this)
    return this
}

fun ViewGroup.refreshViews(): Consumer<List<View>> =
        Consumer { rows ->
            this.removeAllViews()
            this.addAllViews(rows)
        }


fun SharedPreferences.Editor.putStringAndSave(key: String, value: String) {
    putString(key, value).commit()
    Timber.d("preference saved key $key with value $value")
}

fun SharedPreferences.clear() {
    edit().clear().apply()
}

fun View.setBackgroundColorWithDefault(color: String, default: Int) {
    try {
        setBackgroundColor(Color.parseColor(color))
    } catch (e: Exception) {
        Timber.e("Unknown color:  in setBackgroundColor $color")
        setBackgroundColor(default)
    }
}

fun View.toBitmap(): Bitmap {
    return convertBitmapToMemory(this)
}

/**
 * Generate bitmap via given calView which is not inflated. So each calView should be measured before use.

 * @param v
 * *
 * @return
 */
fun convertBitmapToMemory(v: View): Bitmap {
    val spec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.UNSPECIFIED)
    v.measure(spec, spec)

    Timber.d("measured width: ${v.measuredWidth}, height: ${v.measuredHeight}")

    val b = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_4444)
    val canvas = Canvas(b)
    v.isDrawingCacheEnabled = true
    v.layout(0, 0, v.measuredWidth, v.measuredHeight)
    v.draw(canvas)

    return b
}

fun ImageView.setImageDrawable(@DrawableRes drawableInt: Int) {
    val drawable = this.context.resources.getDrawable(drawableInt)
    setImageDrawable(drawable)
}

fun <T> T.chain(function: (T) -> Unit): T {
    function(this)
    return this
}

fun ViewGroup.getAnkoContext(): AnkoContext<Context> {
    val act = context as BaseActivity
    return AnkoContext.create(act as Context, act as Context)
}