package darylsze.rxkotlinstarter.Extension

/**
 * Usage:
 * 1 chain 2 chain 3 = Triple(1,2,3)
 * 1 chain 2 chain 3 chain 4 = Quad(1,2,3,4)
 */

data class Quad<out T, out S, out R, out F>(
        val first: T,
        val second: S,
        val third: R,
        val fourth: F
)

data class Penta<out T, out Q, out R, out S, out P>(
        val first: T,
        val second: Q,
        val third: R,
        val fourth: S,
        val fifth: P
)

data class Hexa<out T, out Q, out R, out S, out P, out V>(
        val first: T,
        val second: Q,
        val third: R,
        val fourth: S,
        val fifth: P,
        val sixth: V
)

infix fun <T, R> T.chain(r: R): Pair<T, R> = Pair(this, r)
infix fun <T, R, P> Pair<T, R>.chain(p: P): Triple<T, R, P> = Triple(first, second, p)
infix fun <T, R, P, Q> Triple<T, R, P>.chain(q: Q): Quad<T, R, P, Q> = Quad(first, second, third, q)
infix fun <T, R, P, Q, W> Quad<T, R, P, Q>.chain(w: W): Penta<T, R, P, Q, W> = Penta(first, second, third, fourth, w)
infix fun <T, R, P, Q, W, E> Penta<T, R, P, Q, W>.chain(e: E): Hexa<T, R, P, Q, W, E> = Hexa(first, second, third, fourth, fifth, e)
