package it.raffaa.test.viewcomponents

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.*
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import it.raffaa.test.R

internal fun View.setDrawerVerticalPadding(level: Int) {
    val verticalPadding = context.resources.getDimensionPixelSize(R.dimen.sideBarItemPadding)
    setPaddingRelative(verticalPadding * level, 0, verticalPadding, 0)
}

/**
 * Util method to theme the drawer item view's background (and foreground if possible)
 *
 * @param ctx            the context to use
 * @param view           the view to theme
 * @param selectedColor the selected color to use
 * @param animate        true if we want to animate the StateListDrawable
 * @param shapeAppearanceModel defines the shape appearance to use for items starting API 21
 * @param paddingTopBottomRes padding on top and bottom of the drawable for selection drawable
 * @param paddingStartRes padding to the beginning of the selection drawable
 * @param paddingEndRes padding to the end of the selection drawable
 * @param highlightColorRes the color for the highlight to use (e.g. touch the item, when it get's filled)
 */
fun themeDrawerItem(
    ctx: Context,
    view: View,
    selectedColor: Int,
    animate: Boolean,
    shapeAppearanceModel: ShapeAppearanceModel,
    @DimenRes paddingTopBottomRes: Int = R.dimen.material_drawer_item_background_padding_top_bottom,
    @DimenRes paddingStartRes: Int = R.dimen.material_drawer_item_background_padding_start,
    @DimenRes paddingEndRes: Int = R.dimen.material_drawer_item_background_padding_end,
    @AttrRes highlightColorRes: Int = R.attr.colorControlHighlight,
    /* a hint for the drawable if it should already be selected at the very moment */
    isSelected: Boolean = false
) {
    val selected: Drawable
    val unselected: Drawable

    // Material 2.0 styling
    val paddingTopBottom = /*ctx.resources.getDimensionPixelSize(paddingTopBottomRes)*/0
    val paddingStart = /*ctx.resources.getDimensionPixelSize(paddingStartRes)*/0
    val paddingEnd = /*ctx.resources.getDimensionPixelSize(paddingEndRes)*/0

    // define normal selected background
    val gradientDrawable = MaterialShapeDrawable(shapeAppearanceModel)
    gradientDrawable.fillColor = ColorStateList.valueOf(ResourcesCompat.getColor(ctx.resources, R.color.design_default_color_primary, ctx.theme))
    gradientDrawable.setCornerSize(0f)
    selected = InsetDrawable(
        gradientDrawable,
        paddingStart,
        paddingTopBottom,
        paddingEnd,
        paddingTopBottom
    )

    // define mask for ripple
    val gradientMask = MaterialShapeDrawable(shapeAppearanceModel)
    gradientMask.fillColor = ColorStateList.valueOf(Color.BLACK)
    val mask = InsetDrawable(
        gradientMask,
        paddingStart,
        paddingTopBottom,
        paddingEnd,
        paddingTopBottom
    )

    unselected = RippleDrawable(
        ColorStateList(
            arrayOf(intArrayOf()),
            intArrayOf(ctx.getThemeColor(highlightColorRes))
        ), null, mask
    )

    val states = StateListDrawable()

    //if possible and wanted we enable animating across states
    if (animate) {
        val duration = ctx.resources.getInteger(android.R.integer.config_shortAnimTime)
        states.setEnterFadeDuration(duration)
        states.setExitFadeDuration(duration)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        states.addState(intArrayOf(android.R.attr.state_selected), selected)
        states.addState(intArrayOf(), ColorDrawable(Color.TRANSPARENT))

        ViewCompat.setBackground(view, states)
        view.foreground = unselected
    } else {
        states.addState(intArrayOf(android.R.attr.state_selected), selected)
        states.addState(intArrayOf(), unselected)

        ViewCompat.setBackground(view, states)
    }

    if (isSelected && animate) {
        states.state = intArrayOf(android.R.attr.state_selected)
        states.jumpToCurrentState()
    }
}


internal fun Context.getThemeColor(@AttrRes attr: Int, @ColorInt def: Int = 0): Int {
    val tv = TypedValue()
    return if (theme.resolveAttribute(attr, tv, true)) {
        if (tv.resourceId != 0) ResourcesCompat.getColor(
            resources,
            tv.resourceId,
            theme
        ) else tv.data
    } else def
}

fun Drawable.setColorFilter(color: Int, mode: Mode = Mode.SRC_ATOP) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = BlendModeColorFilter(color, mode.getBlendMode())
    } else {
        @Suppress("DEPRECATION")
        setColorFilter(color, mode.getPorterDuffMode())
    }
}

// This class is needed to call the setColorFilter
// with different BlendMode on older API (before 29).
enum class Mode {
    CLEAR,
    SRC,
    DST,
    SRC_OVER,
    DST_OVER,
    SRC_IN,
    DST_IN,
    SRC_OUT,
    DST_OUT,
    SRC_ATOP,
    DST_ATOP,
    XOR,
    DARKEN,
    LIGHTEN,
    MULTIPLY,
    SCREEN,
    ADD,
    OVERLAY;

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getBlendMode(): BlendMode =
        when (this) {
            CLEAR -> BlendMode.CLEAR
            SRC -> BlendMode.SRC
            DST -> BlendMode.DST
            SRC_OVER -> BlendMode.SRC_OVER
            DST_OVER -> BlendMode.DST_OVER
            SRC_IN -> BlendMode.SRC_IN
            DST_IN -> BlendMode.DST_IN
            SRC_OUT -> BlendMode.SRC_OUT
            DST_OUT -> BlendMode.DST_OUT
            SRC_ATOP -> BlendMode.SRC_ATOP
            DST_ATOP -> BlendMode.DST_ATOP
            XOR -> BlendMode.XOR
            DARKEN -> BlendMode.DARKEN
            LIGHTEN -> BlendMode.LIGHTEN
            MULTIPLY -> BlendMode.MULTIPLY
            SCREEN -> BlendMode.SCREEN
            ADD -> BlendMode.PLUS
            OVERLAY -> BlendMode.OVERLAY
        }

    fun getPorterDuffMode(): PorterDuff.Mode =
        when (this) {
            CLEAR -> PorterDuff.Mode.CLEAR
            SRC -> PorterDuff.Mode.SRC
            DST -> PorterDuff.Mode.DST
            SRC_OVER -> PorterDuff.Mode.SRC_OVER
            DST_OVER -> PorterDuff.Mode.DST_OVER
            SRC_IN -> PorterDuff.Mode.SRC_IN
            DST_IN -> PorterDuff.Mode.DST_IN
            SRC_OUT -> PorterDuff.Mode.SRC_OUT
            DST_OUT -> PorterDuff.Mode.DST_OUT
            SRC_ATOP -> PorterDuff.Mode.SRC_ATOP
            DST_ATOP -> PorterDuff.Mode.DST_ATOP
            XOR -> PorterDuff.Mode.XOR
            DARKEN -> PorterDuff.Mode.DARKEN
            LIGHTEN -> PorterDuff.Mode.LIGHTEN
            MULTIPLY -> PorterDuff.Mode.MULTIPLY
            SCREEN -> PorterDuff.Mode.SCREEN
            ADD -> PorterDuff.Mode.ADD
            OVERLAY -> PorterDuff.Mode.OVERLAY
        }
}

