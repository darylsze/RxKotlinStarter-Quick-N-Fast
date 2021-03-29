> Lastest update: 29 March. 2021 <br />
> This all-in-one framework is no longer maintained 
> Since I have switched to flutter community.



> Lastest update: 27 Aug. 2017. <br />
> Not yet test for Android Studio 3.0 with build gradle 3.0 <br />
> At least one issue found from Anko in Android Studio 3.0. Go to [Wiki](https://github.com/darylsze/RxKotlinStarter-Quick-N-Fast/wiki) for details.


# RxKotlinStarter
New kotlin architecture for Android applications developing based on ReactiveX, integrates many open source projects( like Kodein,Rxjava2, RxKotlin, Retrofit... ),to make your developing quicker and easier. 

# Functionality & Libraries
* [Kodein](https://github.com/SalomonBrys/Kodein)
* [RxJava2](https://github.com/ReactiveX/RxJava)
* [RxAndroid - based on RxJava2](https://github.com/ReactiveX/RxAndroid)
* [RxPermissions - based on RxJava2](https://github.com/tbruyelle/RxPermissions)
* [Retrofit - based on RxJava2](http://square.github.io/retrofit/)
* [Okhttp](https://github.com/square/okhttp/)
* [Gson](https://github.com/google/gson)
* [Kodein](https://salomonbrys.github.io/Kodein/)
* [Timber](https://github.com/JakeWharton/timber)
* [Glide (version 4)](https://github.com/bumptech/glide)
* [Anko](https://github.com/Kotlin/anko)

# View binding
Anko from Kotlin provides a large set of extension for kotlin user to build any views, create intent in fastest way. Also kotlin android binding plugins allows user to use any view without writing 'findViewById' and 'bindView' code. No butterknife or kotterknife library required from now on.

# Runtime permission
RxPermissions provides chaining method for ReactiveX user to grant permission and result response in a single block of code. Annotation-based permission libraries are given up cause they makes huge trouble when gradle build fail, especially when projects go large....

# Dependency injection
Instead of annotation-based dagger, I use Kodein for faster development. It requires implementing kodein injector on the base activity and application, and afterwards inject any modules or components inside target. It supports both Kotlin and Java with DSL coding style.

# Networking
By using annotation-based Retrofit, you can define huge set of api call service class in a fingertip. Also, gson is used for serializer and deserializer for api response. Both retrofit and gson provides flexible and huge support of adapters and converters for different use cases. All the token handling can be done via Retrofit's RxJavaCallAdapter class.

# Logging
Timber is used for logging. It provides flexible and intutive logging by providing different leaves according to each scenarios, for example, debug environment and release environment can be seperated into two leaves, for debug, only print it out, while log to both Firebase and Fabric for release.

# Imaging
Glide is from Google, it supports animated image and has fast configuration that any other libraries.

# Layout programmatically
As producing view ui code in code are convenience for some small use case, it's important to have a library for programming layout easily intead of maniplating complex xml code. Anko library provides DSL coding style
for making views, and the view result can also be extended somehow, providing flexible and readable view code. [But anko's realtime preview plugin is not unavailable]


# Folder structure
    app
        |- [Activity]   
                |- BaseActivity     // parent of all activity
        |- [Application]
                |- MainApplication  // application for all dependencies
    
        |- [Demo]                   // all demo usage activities.
                |- AnkoViewActivity 
                |- BaseApiCallingActivity
                |- RxViewBindingActivity
        |- [Extension]              // all usage extension methods
                |- AndroidExtension
                |- KodeinExtension
                |- KotlinExtension
                |- MyCompositeDisposable
                |- ObservableExtension
                |- RetrofitExtension
                |- Truples
        |- [Kodein]                 // all dependency injection related stuff
                |- [Modules]        // modules used in kodein
                    |- ApiModule
                    |- NetworkModule
                    |- UtilModule
        |- [Retrofit]
                |- [Plugins]
                    |- InstantTypeConverter
                |- [Response]
                    |- Response
                |- [Service]
                    |- ExampleApiService
        |- [Utils]
                |- CrossContextDialogManager        // dialog manager push dialog to current activity
                                                    // wont be affected by context
                |- ProgressDialogManager            // works same as above but is progress dialog
                
# Usage
All usage, known issue(s), reminder(s) will be added into [Wiki](https://github.com/darylsze/RxKotlinStarter-Quick-N-Fast/wiki) page, please refer to it. 

# License

Copyright 2016, darylsze       

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at 

   http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

