package com.azamovhudstc.tashkentmetro.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.azamovhudstc.tashkentmetro.app.App
import java.io.*
import kotlin.reflect.KFunction

fun initActivity(a: Activity) {
    val window = a.window
    AppCompatDelegate.setDefaultNightMode(
        AppCompatDelegate.MODE_NIGHT_NO
    )
//    WindowCompat.setDecorFitsSystemWindows(window, false)

}
//
//
//@Suppress("UNCHECKED_CAST")
//fun <T> loadData(fileName: String, context: Context? = null, toast: Boolean = true): T? {
//    val a = context ?: App.instance
//    try {
//        if (a?.fileList() != null)
//            if (fileName in a.fileList()) {
//                val fileIS: FileInputStream = a.openFileInput(fileName)
//                val objIS = ObjectInputStream(fileIS)
//                val data = objIS.readObject() as T
//                objIS.close()
//                fileIS.close()
//                return data
//            }
//    } catch (e: Exception) {
//        if (toast) snackString("Error loading data $fileName")
//        e.printStackTrace()
//    }
//    return null
//}

@Suppress("DEPRECATION")
fun Activity.hideSystemBars() {
    window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
}
@Suppress("DEPRECATION")
fun Fragment.hideSystemBars() {
    requireActivity().window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
}

@Suppress("DEPRECATION")
fun Activity.hideStatusBar() {
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}
//
//fun ImageView.loadImage(url: String?, size: Int = 0) {
//    if (!url.isNullOrEmpty()) {
//        loadImage(FileUrl(url), size)
//    }
//}

//fun ImageView.loadImage(file: FileUrl?, size: Int = 0) {
//    if (file?.url?.isNotEmpty() == true) {
//        tryWith {
//            val glideUrl = GlideUrl(file.url) { file.headers }
//            Glide.with(this.context).load(glideUrl)
//                .transition(DrawableTransitionOptions.withCrossFade()).override(size).into(this)
//        }
//    }
//}


data class FileUrl(
    val url: String,
    val headers: Map<String, String> = mapOf()
) : Serializable {
    companion object {
        operator fun get(url: String?, headers: Map<String, String> = mapOf()): FileUrl? {
            return FileUrl(url ?: return null, headers)
        }
    }
}

//Credits to leg
data class Lazier<T>(
    val lClass: KFunction<T>,
    val name: String
) {
    val get = lazy { lClass.call() }
}


fun <T> tryWith(post: Boolean = false, snackbar: Boolean = true, call: () -> T): T? {
    return try {
        call.invoke()
    } catch (e: Throwable) {
        null
    }
}

fun saveData(fileName: String, data: Any?, context: Context? = null) {
    tryWith {
        val a = context ?: App.currentContext()!!
        val fos: FileOutputStream = a.openFileOutput(fileName, Context.MODE_PRIVATE)
        val os = ObjectOutputStream(fos)
        os.writeObject(data)
        os.close()
        fos.close()
    }
}
