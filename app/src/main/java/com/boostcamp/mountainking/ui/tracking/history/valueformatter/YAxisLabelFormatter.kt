package com.boostcamp.mountainking.ui.tracking.history.valueformatter

import com.github.mikephil.charting.formatter.ValueFormatter

class YAxisLabelFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value}m"
    }
}