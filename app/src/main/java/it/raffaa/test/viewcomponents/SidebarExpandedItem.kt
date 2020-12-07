package it.raffaa.test.viewcomponents

import android.content.Context
import android.graphics.drawable.*
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.shape.ShapeAppearanceModel
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.model.AbstractBadgeableDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.utils.hiddenInMiniDrawer
import it.raffaa.test.R

class SidebarExpandedItem() : PrimaryDrawerItem() {

    constructor(icon: Drawable?) : this() {
        this.icon = ImageHolder(icon)
    }

    init {
        isSelectable = true
        hiddenInMiniDrawer = false
        isIconTinted = true
        level = 0 //Serve per il padding
    }

    override val layoutRes: Int
        get() = R.layout.sidebar_expanded_item

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        //setCorrectPadding
        holder.itemView.setDrawerVerticalPadding(level)

        //setIndicator
        holder as MyViewHolder
        if (isSelected) {
            holder.selectionIndicator.visibility = View.VISIBLE
        } else {
            holder.selectionIndicator.visibility = View.INVISIBLE
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)

        //resetIndicator
        holder as MyViewHolder
        holder.selectionIndicator.visibility = View.INVISIBLE
    }

    override fun getViewHolder(v: View): ViewHolder {
        return MyViewHolder(v)
    }

    class MyViewHolder(v : View) : AbstractBadgeableDrawerItem.ViewHolder(v) {
        var selectionIndicator : FrameLayout = v.findViewById(R.id.selectionIndicator)
    }

    override fun applyDrawerItemTheme(
        ctx: Context,
        view: View,
        selected_color: Int,
        animate: Boolean,
        shapeAppearanceModel: ShapeAppearanceModel
    ) {
        themeDrawerItem(
            ctx,
            view,
            selected_color,
            animate,
            shapeAppearanceModel,
            isSelected = isSelected
        )
    }
}