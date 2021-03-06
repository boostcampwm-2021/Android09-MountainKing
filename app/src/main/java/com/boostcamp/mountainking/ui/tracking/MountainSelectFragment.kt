package com.boostcamp.mountainking.ui.tracking

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.boostcamp.mountainking.databinding.FragmentMountainSelectBinding
import com.boostcamp.mountainking.entity.Mountain
import com.boostcamp.mountainking.util.EventObserver
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MountainSelectFragment : DialogFragment() {

    private var _binding: FragmentMountainSelectBinding? = null
    private val binding get() = _binding!!
    private val mountainSelectViewModel: MountainSelectViewModel by viewModels()
    private val mountainListAdapter =
        MountainListAdapter { mountain -> onMountainClicked(mountain) }
    private var location: LatLng? = null
    private val observableTextQuery = Observable
        .create { emitter: ObservableEmitter<String>? ->
            binding.etMountainName.addTextChangedListener { editable ->
                emitter?.onNext(editable.toString())
            }
        }
        .debounce(500, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.location = arguments?.getParcelable(LOCATION)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMountainSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = mountainSelectViewModel
        setRecyclerView()
        mountainSelectViewModel.mountainNameList.observe(viewLifecycleOwner) {
            mountainListAdapter.submitList(it) {
                binding.rvMountainList.scrollToPosition(0)
            }
        }
        mountainSelectViewModel.dismiss.observe(viewLifecycleOwner, EventObserver {
            dismiss()
        })
        mountainSelectViewModel.searchMountainName("", location)

        disposable = observableTextQuery.subscribe { name ->
            mountainSelectViewModel.searchMountainName(name, location)
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val parentFragment = parentFragment
        if (parentFragment is DialogInterface.OnDismissListener) {
            parentFragment.onDismiss(dialog)
        }
    }

    private fun setRecyclerView() {
        binding.rvMountainList.adapter = mountainListAdapter
    }

    private fun onMountainClicked(mountain: Mountain) {
        mountainSelectViewModel.setTrackingMountain(mountain.toString(), mountain.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable?.dispose()
    }

    companion object {
        fun newInstance(location: LatLng?): MountainSelectFragment {
            val fragment = MountainSelectFragment()
            val args = Bundle()
            args.putParcelable(LOCATION, location)
            fragment.arguments = args
            return fragment
        }

        private const val LOCATION = "location"
    }
}