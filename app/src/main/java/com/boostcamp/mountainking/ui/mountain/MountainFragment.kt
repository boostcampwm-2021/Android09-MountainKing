package com.boostcamp.mountainking.ui.mountain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.boostcamp.mountainking.databinding.FragmentMountainBinding

class MountainFragment : Fragment() {

    private lateinit var mountainViewModel: MountainViewModel
    private var _binding: FragmentMountainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mountainViewModel =
            ViewModelProvider(this).get(MountainViewModel::class.java)

        _binding = FragmentMountainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rpMap.findAllRichPaths().forEach { path ->
            path.setOnPathClickListener {
                Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}