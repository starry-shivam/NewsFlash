package com.starry.newsflash

import android.content.Context
import android.content.res.Configuration

fun isDarkModeOn(ctx: Context): Boolean {
    return when (ctx.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> true
        Configuration.UI_MODE_NIGHT_NO -> false
        Configuration.UI_MODE_NIGHT_UNDEFINED -> false
        else -> false
    }
}