package it.raffaa.test.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.mikepenz.crossfader.Crossfader
import com.mikepenz.crossfader.util.UIUtils
import com.mikepenz.crossfader.view.GmailStyleCrossFadeSlidingPaneLayout
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.model.NavigationDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.nameText
import com.mikepenz.materialdrawer.util.setupWithNavController
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView
import com.mikepenz.materialdrawer.widget.MiniDrawerSliderView
import it.htitalia.ht16000.views.sidebar.CrossfadeWrapper
import it.raffaa.test.R
import it.raffaa.test.viewcomponents.SidebarExpandedItem
import it.raffaa.test.viewcomponents.SidebarViewCollapsed
import kotlinx.android.synthetic.main.fragment_container.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Container.newInstance] factory method to
 * create an instance of this fragment.
 */
class Container : Fragment() {
    private lateinit var miniSliderView: MiniDrawerSliderView
    private lateinit var sliderView: MaterialDrawerSliderView
    private lateinit var crossFader: Crossfader<*>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sidebar1 = SidebarExpandedItem().apply {
            icon = ImageHolder(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_testicon))
            nameText = "SideBar1"
        }

        val sidebar2 = SidebarExpandedItem(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.ic_testicon
            )
        ).apply {
            nameText = "SideBar2"
        }

        //Create the drawer
        sliderView = MaterialDrawerSliderView(requireActivity()).apply {
            accountHeader = null
            customWidth = ViewGroup.LayoutParams.MATCH_PARENT
            closeOnClick = false
            onDrawerItemClickListener = { v, item, position ->
                true
            }
            itemAdapter.add(
                SidebarExpandedItem().apply {
                    icon = ImageHolder(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_testicon
                        )
                    )
                    isSelectable = false
                },
                //DividerDrawerItem(),
                NavigationDrawerItem(R.id.action_sidebar1, sidebar1, null, null),
                NavigationDrawerItem(R.id.action_sidebar2, sidebar2, null, null),
                SidebarExpandedItem().apply {
                    icon = ImageHolder(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_testicon
                        )
                    )
                    nameText = "prova"
                }
            )
        }

        sliderView.setupWithNavController(
            requireActivity().findNavController(R.id.sidebar_nav_graph_nav_host_fragment),
            fallBackListener = { v, item, position ->
                print("WEDO - click")
                if (!item.isSelectable) {
                    crossFader.crossFade()
                }
                false
            })

        // create the MiniDrawer and define the drawer and header to be used (it will automatically use the items from them)
        miniSliderView = SidebarViewCollapsed(requireActivity()).apply {
            drawer = sliderView
            enableSelectedMiniDrawerItemBackground = true
        }

        //get the widths in px for the first and second panel
        val firstWidth = UIUtils.convertDpToPixel(300f, requireContext()).toInt()
        val secondWidth = UIUtils.convertDpToPixel(72f, requireContext()).toInt()

        //create and build our crossfader (see the MiniDrawer is also builded in here, as the build method returns the view to be used in the crossfader)
        crossFader = Crossfader<GmailStyleCrossFadeSlidingPaneLayout>()
            .withContent(title)
            .withFirst(sliderView as View, firstWidth)
            .withSecond(miniSliderView, secondWidth)
            .withSavedInstance(savedInstanceState)
            .withCanSlide(true)
            .build()

        //Background color collpsed state
        crossFader.second.setBackgroundColor(
            resources.getColor(
                R.color.design_default_color_primary,
                requireActivity().theme
            )
        )
        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniSliderView.crossFader = CrossfadeWrapper(crossFader)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Container.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            Container()
    }
}