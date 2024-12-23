package com.example.mybottomnavigation.ui.finished_event

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybottomnavigation.DetailActivity
import com.example.mybottomnavigation.data.Result
import com.example.mybottomnavigation.data.local.entity.EventEntity
import com.example.mybottomnavigation.databinding.FragmentFinishedEventBinding
import com.example.mybottomnavigation.helper.ViewModelFactory
import com.example.mybottomnavigation.ui.adapter.VerticalViewAdapter
import com.example.mybottomnavigation.ui.home.HomeViewModel

class FinishedEventFragment : Fragment() {

    private var _binding: FragmentFinishedEventBinding? = null
    private lateinit var rvVertical: RecyclerView
    private lateinit var verticalAdapter: VerticalViewAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val homeViewModel: HomeViewModel by viewModels {
            factory
        }

        rvVertical = binding.rvFinishedEvent
        verticalAdapter = VerticalViewAdapter{ event ->
            if (event.isFavorite == true){
                homeViewModel.removeFavorite(event)
            }else{
                homeViewModel.setFavorite(event)
            }
        }

        rvVertical.adapter = verticalAdapter
        rvVertical.layoutManager = LinearLayoutManager(context)

        val searchView = _binding?.searchView

        homeViewModel.getFinishedEvents().observe(viewLifecycleOwner) { result ->
            if (result != null){
                when(result){
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val filteredData = result.data.filter { event -> event.isActive == false }
                        verticalAdapter.setFullList(filteredData)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                verticalAdapter.filter(newText ?: "")
                return true
            }
        })
        adapterOnClickCallback()
    }
    private fun adapterOnClickCallback(){
        verticalAdapter.setOnItemClickCallback(object: VerticalViewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: EventEntity) {
                intentToDetail(data)
            }
        })
    }

    private fun intentToDetail(data: EventEntity){
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("EVENT_DETAIL", data)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}