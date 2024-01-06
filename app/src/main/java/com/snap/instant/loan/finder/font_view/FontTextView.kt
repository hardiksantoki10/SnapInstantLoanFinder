package com.snap.instant.loan.finder.font_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.snap.instant.loan.finder.R

class FontTextView : AppCompatTextView {
    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context!!, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            @SuppressLint("CustomViewStyleable") val a =
                context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)
            val fontName = a.getString(R.styleable.CustomTextView_app_font)
            try {
                if (fontName != null) {
                    val myTypeface =
                        Typeface.createFromAsset(context.assets, fontName)
                    typeface = myTypeface
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            a.recycle()
        }
    }
}