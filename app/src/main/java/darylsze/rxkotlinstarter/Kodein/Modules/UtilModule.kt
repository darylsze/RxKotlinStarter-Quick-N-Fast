package darylsze.rxkotlinstarter.Kodein.Modules

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import darylsze.rxkotlinstarter.Utils.CrossContextDialogManager
import darylsze.rxkotlinstarter.Utils.ProgressDialogManager

val utilModule = Kodein.Module {
    bind<CrossContextDialogManager>() with singleton { CrossContextDialogManager() }
    bind<ProgressDialogManager>() with singleton { ProgressDialogManager() }
}