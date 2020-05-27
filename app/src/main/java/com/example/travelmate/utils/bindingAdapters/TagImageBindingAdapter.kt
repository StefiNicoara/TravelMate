package com.example.travelmate.utils.bindingAdapters

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.travelmate.R
import com.example.travelmate.model.AttractionTag
import android.view.Gravity
import android.util.TypedValue
import kotlin.math.roundToInt


val tagsIcon: Map<AttractionTag, Int> = mapOf(
    AttractionTag.ACCOMODATION to R.drawable.ic_accomodation,
    AttractionTag.BAR to R.drawable.ic_bar,
    AttractionTag.CAFE to R.drawable.ic_cafe,
    AttractionTag.FOOD to R.drawable.ic_food,
    AttractionTag.RECREATIONAL to R.drawable.ic_recreational,
    AttractionTag.CULTURAL to R.drawable.ic_cultural,
    AttractionTag.SOCIAL to R.drawable.ic_social,
    AttractionTag.FUN to R.drawable.ic_fun

)

@BindingAdapter("addTags")
fun addTags(layout: LinearLayout, tagsList: List<AttractionTag>?) {
    layout.removeAllViews()

    // Converts dp into its equivalent px
    val r = layout.context.resources
    val sizeImg = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 20f, r.displayMetrics
    ).roundToInt()

    val sizeMargin = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 5f, r.displayMetrics
    ).roundToInt()

    val layoutParams =
        LinearLayout.LayoutParams(sizeImg, sizeImg)
    layoutParams.setMargins(sizeMargin, 0, 0, 0)
    layoutParams.gravity = Gravity.BOTTOM


    tagsList?.forEach { tag ->
        val img = ImageView(layout.context)
        val drawableId = tagsIcon[tag]
        img.layoutParams = layoutParams
        img.background = ContextCompat.getDrawable(layout.context, drawableId!!)
        layout.addView(img)
    }

}