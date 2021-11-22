package com.richpath.pathparser

import android.graphics.Path
import java.lang.reflect.InvocationTargetException
import androidx.core.graphics.PathParser

object PathParserCompatApi21 {

    /**
     * @param pathData The string representing a path, the same as "d" string in svg file.
     * @return the generated Path object.
     */
    fun createPathFromPathData(pathData: String?): Path? {
        try {
            return PathParser.createPathFromPathData(pathData)
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }
}