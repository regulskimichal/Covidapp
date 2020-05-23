package pl.gurbakregulski.covidapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.gurbakregulski.covidapp.R
import pl.gurbakregulski.covidapp.databinding.StatsFragmentBinding
import pl.gurbakregulski.covidapp.debounce
import pl.gurbakregulski.covidapp.setOnQueryTextListener
import pl.gurbakregulski.covidapp.viewmodel.MainActivityViewModel

class StatsFragment : Fragment() {

    private lateinit var binding: StatsFragmentBinding
    private val viewModel: MainActivityViewModel by sharedViewModel()
    private val statsAdapter = StatsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StatsFragmentBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = activity
            viewModel = this@StatsFragment.viewModel
        }
        binding.recyclerView.apply {
            viewModel.filteredStats.observe(
                requireActivity(),
                Observer { statsAdapter.submitList(it) })
            adapter = statsAdapter
            adapter?.stateRestorationPolicy = PREVENT_WHEN_EMPTY
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setOnQueryTextListener(
                debounce(viewModel.viewModelScope, 1000L, viewModel::filterStats)
            )
        }
    }

}
