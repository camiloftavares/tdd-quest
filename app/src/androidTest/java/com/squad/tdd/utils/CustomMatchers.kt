package com.squad.tdd.utils

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

fun withDrawable(@DrawableRes id: Int) = object : TypeSafeMatcher<View>(){
    override fun describeTo(description: Description?) {

    }

    override fun matchesSafely(item: View): Boolean {
        val expected = item.context.getDrawable(id)?.toBitmap()

        return item is ImageView && item.drawable.toBitmap().sameAs(expected)
    }
}