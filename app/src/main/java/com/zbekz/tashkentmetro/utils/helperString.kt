package com.zbekz.tashkentmetro.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.data.local.shp.Language
import com.zbekz.tashkentmetro.data.model.station.StationLang
import com.zbekz.tashkentmetro.utils.custom.StationFilter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.firstLetterUpper(): String {
    var firstLatter = substring(0, 1)
    var lastLetters = substring(1, length).lowercase()


    return "${firstLatter}$lastLetters"
}
//fun String.screenEnum(): LanguageType {
//    return when (this) {
//        "uz" -> LanguageType.uz
//        else -> LanguageType.ru
//    }
//}


fun localizeDone(context: Context):String{
    val appReference = AppReference(context)
    return when (appReference.language) {
        Language.ENGLISH -> "Done"
        Language.RUSSIAN -> "Готово"
        Language.UZBEK -> "Tugatish"
    }
}
fun localizeEnterCode(context: Context,input: String):String{
    val appReference = AppReference(context)
    return when (appReference.language) {

        Language.ENGLISH -> "Enter received code from ${input}"
        Language.RUSSIAN -> "Введи полученный код от ${input}"
        Language.UZBEK -> "Shu ${input} bilan yuborilgan kodni kiriting"
    }
}
fun localizeLastStation():String{
    val appReference   = AppReference(currContext()!!)
    return when (appReference.language) {
        Language.ENGLISH -> "Last station"
        Language.RUSSIAN -> "Последняя станция"
        Language.UZBEK -> "So`ngi stansiya"
    }
}
fun formatString(input: String, context: Context): String {
    return checkLanAndReturn(input, context).split("_")  // Pastki chiziqlar bo'yicha bo'lib olish
        .joinToString(" ") { it.capitalize() }  // So'zlarni bosh harfini katta qilish va probel bilan birlashtirish
}

fun checkLanAndReturn(name: String, context: Context): String {
    val appReference = AppReference(context)
    val lang = appReference.language
    val langResult =StationFilter.getStationByQuery(name)?: StationLang("none", mapOf())
    return when (lang) {
        Language.ENGLISH -> {
            langResult.translations["en"] ?: name
        }

        Language.RUSSIAN -> {
            langResult.translations["ru"] ?: name

        }

        Language.UZBEK -> {
            langResult.translations["uz"] ?: name
        }
    }
}

fun Bitmap.toStringWithBitmap(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}


//fun String.screenCurrentEnum(): CurrentScreenEnum {
//    return when (this) {
//        "HOME" -> CurrentScreenEnum.HOME
//        else -> CurrentScreenEnum.LANGUAGE
//    }
//}

fun String.parseCode(): String {
    val p: Pattern = Pattern.compile("\\b\\d{6}\\b")
    val m: Matcher = p.matcher(this)
    var code = ""
    while (m.find()) {
        code = m.group(0)
    }
    return code
}

fun String.checkPhone(): Boolean {
    val REG = "^(\\+998|998)(90|91|93|94|95|97|98|99|88)\\d{7}$"
    val pattern = Pattern.compile(REG)
    return pattern.matcher(this).find()
}

fun String.extractString(): String {
    val sanitizedString = this.replace("(", "").replace(")", "").replace("-", "")
    println(sanitizedString)
    return sanitizedString


}


//fun String.getYearCurrentLanguage():String{
//    return if (AppReference(App.currentContext()!!).currentLanguage==LanguageType.ru){
//        "$this-год"
//    }
//    else{
//        "$this-yil"
//
//    }
//}

fun String.stringToBitmap(): Bitmap {
    val encodeByte: ByteArray = Base64.decode(this, Base64.DEFAULT)

    val inputStream: InputStream = ByteArrayInputStream(encodeByte)
    return BitmapFactory.decodeStream(inputStream)
}
