package it.raffaa.test.viewcomponents

import android.content.Context
import android.util.AttributeSet
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.model.MiniDrawerItem
import com.mikepenz.materialdrawer.model.MiniProfileDrawerItem
import com.mikepenz.materialdrawer.model.NavigationDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.utils.hiddenInMiniDrawer
import com.mikepenz.materialdrawer.widget.MiniDrawerSliderView
import it.raffaa.test.R

class SidebarViewCollapsed @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.materialDrawerStyle
) : MiniDrawerSliderView(context, attrs, defStyleAttr) {

    override fun generateMiniDrawerItem(drawerItem: IDrawerItem<*>): IDrawerItem<*>? {
        return when (drawerItem) {

            is SidebarExpandedItem -> if (!drawerItem.hiddenInMiniDrawer) SidebarCollapsedItem(
                drawerItem
            ).apply {
                enableSelectedBackground = enableSelectedMiniDrawerItemBackground
                isSelectedBackgroundAnimated = false
                customHeight = DimenHolder.fromResource(R.dimen.sidebarItemHeight)
            } else null
            is NavigationDrawerItem -> if (!drawerItem.hiddenInMiniDrawer) {
                SidebarCollapsedItem(drawerItem.item as SidebarExpandedItem).apply {
                    enableSelectedBackground = enableSelectedMiniDrawerItemBackground
                    isSelectedBackgroundAnimated = false
                    customHeight = DimenHolder.fromResource(R.dimen.sidebarItemHeight)
                }
            } else null
            else -> null
        }
    }

    /**
     * gets the type of a IDrawerItem
     *
     * @param drawerItem
     * @return
     */
    override fun getMiniDrawerType(drawerItem: IDrawerItem<*>): Int {
        if (drawerItem is MiniProfileDrawerItem) {
            return PROFILE
        } else if (drawerItem is MiniDrawerItem || drawerItem is SidebarCollapsedItem) {
            return ITEM
        }
        return -1
    }

}