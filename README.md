# RxKotlinArms
New kotlin architecture for Android applications developing based on ReactiveX, integrates many open source projects( like Kodein,Rxjava2, RxKotlin, Retrofit... ),to make your developing quicker and easier. 

# Functionality & Libraries
* [Kodein](https://github.com/SalomonBrys/Kodein)
* RxJava2
* RxAndroid - based on RxJava2
* Rxlifecycle - based on RxJava2
* RxPermissions - based on RxJava2
* Retrofit - based on RxJava2
* Okhttp
* Gson
* Timber
* Glide (version 4)


# View binding
Anko from Kotlin provides a large set of extension for kotlin user to build any views, create intent in fastest way. Also kotlin android binding plugins allows user to use any view without writing 'findViewById' and 'bindView' code. No butterknife or kotterknife library required from now on.

# Runtime permission
RxPermissions provides chaining method for ReactiveX user to grant permission and result response in a single block of code. Annotation-based permission libraries are given up cause they makes huge trouble when gradle build fail, especially when projects go large....

# Kodein (KOtlin DEpendency INjection)
Instead of annotation-based dagger, I use Kodein for faster development. It requires implementing kodein injector on the base activity and application, and afterwards inject any modules or components inside target. It supports both Kotlin and Java with DSL coding style.



# Project still under development... Do NOT use currently.
