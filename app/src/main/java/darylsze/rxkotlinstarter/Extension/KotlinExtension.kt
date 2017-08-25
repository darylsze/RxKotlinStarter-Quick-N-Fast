package darylsze.rxkotlinstarter.Extension

// return uuid, first ticketConcession sample and total size
fun <T, R> Iterable<T>.groupAndSumBy(keySelector: (T) -> R): Iterable<Triple<R, T, Int>> =
        groupBy(keySelector)
                .map { (key, list) ->
                    // key, object, size
                    key chain list.first() chain list.size
                }

inline fun <reified T> String.fromJsonToList(): List<T> {
    return com.google.gson.Gson().fromJson(this, object : com.google.gson.reflect.TypeToken<List<T>>() {}.type)
}

inline fun <reified T> List<T>.toJsonList(): String {
    return com.google.gson.Gson().toJson(this)
}

inline fun <reified T> android.os.Bundle.putGsonList(key: String, data: List<T>) {
    this.putString(key, data.toJsonList())
}

inline fun <reified T> android.os.Bundle.getGsonList(key: String): List<T> = this.getString(key).fromJsonToList()

inline fun <E> List<E>.sumByFloat(selector: (E) -> Float): Float {
    var sum: Float = 0f
    forEach {
        sum += selector(it)
    }
    return sum
}

fun Float.roundUp(): Int {
    return Math.round(this)
}

fun <E : Number> E.roundUp(): Int {
    return Math.round(this.toFloat())
}

