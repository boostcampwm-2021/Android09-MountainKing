package com.boostcamp.mountainking.ui.tracking.history.valueformatter

import com.github.mikephil.charting.formatter.ValueFormatter

class XAxisLabelFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value}s"
    }
}