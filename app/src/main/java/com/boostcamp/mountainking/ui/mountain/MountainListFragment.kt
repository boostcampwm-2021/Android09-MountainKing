package com.boostcamp.mountainking.ui.mountain

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.data.city
import com.boostcamp.mountainking.databinding.FragmentMountainListBinding
import com.boostcamp.mountainking.entity.Mountain
import com.boostcamp.mountainking.ui.tracking.MountainListAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.disposables.Disposable
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
    private lateinit var state: String
    private val observableTextQuery = Observable
        .create { emitter: ObservableEmitter<String>? ->
            binding.etMountainName.addTextChangedListener { editable ->
                emitter?.onNext(editable.toString())
            }
        }
        .debounce(500, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
    private var disposable: Disposable? = null

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

        state = arguments?.getString("state") ?: "경기도"
        initToolbar()

        initObserve()

        val cityList = mutableListOf("전체")
        city[state]?.let {
            cityList.addAll(it)
        }

        binding.spMountainCityList.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            cityList
        )

        binding.spMountainCityList.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d("spinner_listener", binding.spMountainCityList.selectedItem.toString())
                    mountainListViewModel.searchMountainNameInCity(
                        state,
                        binding.spMountainCityList.selectedItem.toString().let {
                            if (it == "전체") {
                                ""
                            } else {
                                it
                            }
                        },
                        binding.etMountainName.text.toString()
                    )
                }
            }
        disposable = observableTextQuery.subscribe { name ->
            Log.d("cityName", binding.spMountainCityList.selectedItem.toString())
            if (binding.spMountainCityList.selectedItem.toString() == "전체") {
                mountainListViewModel.searchMountainNameInCity(
                    state,
                    "",
                    name
                )
            } else {
                mountainListViewModel.searchMountainNameInCity(
                    state,
                    binding.spMountainCityList.selectedItem.toString(),
                    name
                )
            }
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
        disposable?.dispose()
    }

    private fun onMountainClicked(mountain: Mountain) {
        findNavController().navigate(
            R.id.action_mountainListFragment_to_mountainDetailFragment,
            bundleOf("mountainId" to mountain.id)
        )
    }

    private fun initToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.tbState)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        binding.tbState.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_mountainListFragment_to_navigation_mountain)
        }

        binding.tbState.title = state
    }

}
