package com.boostcamp.mountainking.util

import android.content.Context

class StringGetter(private val context: Context) {
    fun getString(id: Int): String {
        return context.getString(id)
    }
}