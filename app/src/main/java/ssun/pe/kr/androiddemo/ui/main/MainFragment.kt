package ssun.pe.kr.androiddemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import ssun.pe.kr.androiddemo.R
import ssun.pe.kr.androiddemo.databinding.FragmentMainBinding
import ssun.pe.kr.androiddemo.ui.detail.DetailActivity

class MainFragment : Fragment() {

    companion object {
        const val TAG = "MainFragment"
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: MainAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)

        binding = FragmentMainBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MainFragment
            viewModel = this@MainFragment.mainViewModel
        }

        mainViewModel.navigateToDetail.observe(this, Observer { url ->
            startActivity(DetailActivity.starterIntent(requireContext(), url))
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        mainViewModel.items.observe(this, Observer { items ->
            adapter.submitList(items)
        })
    }

    private fun initViews() {
        adapter = MainAdapter(mainViewModel, this)

        binding.toolbar.inflateMenu(R.menu.menu_main)

        (binding.toolbar.menu.findItem(R.id.search).actionView as SearchView).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainViewModel.search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@MainFragment.adapter
            val deco = DividerItemDecoration(context, VERTICAL)
            deco.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.main_deco)!!)
            addItemDecoration(deco)
        }
    }

    fun onBackPressed(): Boolean {
        return if (!(binding.toolbar.menu.findItem(R.id.search).actionView as SearchView).isIconified) {
            binding.toolbar.menu.findItem(R.id.search).collapseActionView()
            true
        } else {
            false
        }
    }
}