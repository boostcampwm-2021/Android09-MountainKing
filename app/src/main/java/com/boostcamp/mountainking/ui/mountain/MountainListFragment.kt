package com.boostcamp.mountainking.ui.mountain

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.boostcamp.mountainking.data.city
import com.boostcamp.mountainking.databinding.FragmentMountainListBinding
import com.boostcamp.mountainking.entity.Mountain
import com.boostcamp.mountainking.ui.tracking.MountainListAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MountainListFragment : Fragment() {

    private val mountainListViewModel: MountainListViewModel by viewModels()
    private var _binding: FragmentMountainListBinding? = null
    private val mountainListAdapter =
        MountainListAdapter { mountain -> onMountainClicked(mountain) }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMountainListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMountainList.adapter = mountainListAdapter
        initObserve()

        val state = arguments?.getString("state") ?: "경기도"
        Log.d("state", state)
        Log.d("city", city[state].toString())
        binding.spMountainCityList.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            city[state] ?: emptyList()
        )

        val observableTextQuery = Observable
            .create { emitter: ObservableEmitter<String>? ->
                binding.etMountainName.addTextChangedListener { editable ->
                    emitter?.onNext(editable.toString())
                }
            }
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
        observableTextQuery.subscribe { name ->
            Log.d("cityName", binding.spMountainCityList.selectedItem.toString())
            mountainListViewModel.searchMountainNameInCity(
                state,
                binding.spMountainCityList.selectedItem.toString(),
                name
            )
        }
    }

    private fun initObserve() = with(mountainListViewModel) {
        mountainNameList.observe(viewLifecycleOwner) {
            mountainListAdapter.submitList(it) {
                binding.rvMountainList.scrollToPosition(0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onMountainClicked(mountain: Mountain) {
        //TODO "화면 전환"
    }

}
