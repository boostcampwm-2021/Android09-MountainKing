package com.boostcamp.mountainking.ui.mountain

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.databinding.FragmentMountainBinding
import com.richpath.RichPath
import com.richpathanimator.AnimationListener
import com.richpathanimator.RichPathAnimator

class MountainFragment : Fragment() {

    private var _binding: FragmentMountainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMountainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        binding.rpMap.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    private fun initListener() {

        val richPathList = binding.rpMap.findAllRichPaths()
        val richPathMap = mutableMapOf<String?, RichPath>()

        richPathList.filter {
            it.name?.last() != '_'
        }.forEach {
            richPathMap[it.name] = it
        }

        richPathList.forEach { path ->
            path.onPathClickListener = object : RichPath.OnPathClickListener {
                override fun onClick(richPath: RichPath) {

                    val name = richPath.name
                    val realName = name?.substringBefore("_")
                    val realPath = richPathMap[realName]

                    if (realPath != null) {
                        RichPathAnimator.animate(realPath).fillColor(Color.GRAY)
                            .animationListener(object : AnimationListener {
                                override fun onStart() {

                                }

                                override fun onStop() {
                                    findNavController().navigate(
                                        R.id.action_navigation_mountain_to_mountainListFragment,
                                        bundleOf("state" to realPath.name)
                                    )
                                }
                            }).start()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}