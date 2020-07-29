@file:Suppress("DEPRECATION")

package com.github.kr328.clash.common.utils

import android.content.res.Configuration
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.os.LocaleListCompat

fun fromHtmlCompat(source: String, flag: Int): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(source, flag)
            else
                Html.fromHtml(source)
}

val Configuration.localesCompat: LocaleListCompat
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                LocaleListCompat.wrap(this.locales)
            else
                LocaleListCompat.create(this.locale)
