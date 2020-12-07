package it.raffaa.test.viewcomponents

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.model.BaseDrawerItem
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import it.raffaa.test.R

class SidebarCollapsedItem(primaryDrawerItem: SidebarExpandedItem) :
    BaseDrawerItem<SidebarCollapsedItem, SidebarCollapsedItem.ViewHolder>() {
    var enableSelectedBackground = true
    var customHeight: DimenHolder? = null

    override val type: Int
        get() = R.id.material_drawer_item_mini

    init {
        this.identifier = primaryDrawerItem.identifier
        this.tag = primaryDrawerItem.tag
        this.isEnabled = primaryDrawerItem.isEnabled
        this.isSelectable = primaryDrawerItem.isSelectable
        this.isSelected = primaryDrawerItem.isSelected
        this.icon = primaryDrawerItem.icon
        this.selectedIcon = primaryDrawerItem.selectedIcon
        this.isIconTinted = primaryDrawerItem.isIconTinted
        this.selectedColor = primaryDrawerItem.selectedColor
        this.iconColor = primaryDrawerItem.iconColor
    }

    override val layoutRes: Int
        get() = R.layout.sidebar_collpsed_item

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //set a different height for this item
        customHeight?.let {
            val lp = holder.itemView.layoutParams as RecyclerView.LayoutParams
            lp.height = it.asPixel(ctx)
            holder.itemView.layoutParams = lp
        }

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //set the item enabled if it is
        holder.itemView.isEnabled = isEnabled
        holder.icon.isEnabled = isEnabled

        //set the item selected if it is
        holder.itemView.isSelected = isSelected
        holder.icon.isSelected = isSelected

        //
        holder.itemView.tag = this

        //get the correct color for the icon
        val iconColor = getIconColor(ctx)
        val shapeAppearanceModel = getShapeAppearanceModel(ctx)

        if (enableSelectedBackground) {
            //get the correct color for the background
            val selectedColor = getSelectedColor(ctx)
            //set the background for the item
            themeDrawerItem(
                ctx,
                holder.view,
                selectedColor,
                isSelectedBackgroundAnimated,
                shapeAppearanceModel,
                isSelected = isSelected
            )
        }

        // check if we should load from a url, false if normal icon
        val loaded = icon?.uri?.let {
            DrawerImageLoader.instance.setImage(
                holder.icon,
                it,
                DrawerImageLoader.Tags.MINI_ITEM.name
            )
        } ?: false

        if (!loaded) {
            // get the drawables for our icon and set it
            val icon = ImageHolder.decideIcon(icon, ctx, iconColor, isIconTinted, 1)
            val selectedIcon = ImageHolder.decideIcon(selectedIcon, ctx, iconColor, isIconTinted, 1)
            ImageHolder.applyMultiIconTo(icon, selectedIcon, iconColor, isIconTinted, holder.icon)
        }

        if (isSelected) {
            holder.selectionIndicator.visibility = View.VISIBLE
        } else {
            holder.selectionIndicator.visibility = View.INVISIBLE
        }

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)

        // reset image loading for the item
        DrawerImageLoader.instance.cancelImage(holder.icon)
        holder.icon.setImageBitmap(null)
        holder.selectionIndicator.visibility = View.INVISIBLE
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(internal val view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.material_drawer_icon)
        val selectionIndicator: FrameLayout = view.findViewById(R.id.selectionIndicator)
    }

}