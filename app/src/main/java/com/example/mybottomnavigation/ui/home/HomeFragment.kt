package com.example.mybottomnavigation.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybottomnavigation.DetailActivity
import com.example.mybottomnavigation.data.Result
import com.example.mybottomnavigation.data.local.entity.EventEntity
import com.example.mybottomnavigation.databinding.FragmentHomeBinding
import com.example.mybottomnavigation.helper.ViewModelFactory
import com.example.mybottomnavigation.ui.adapter.HorizontalViewAdapter
import com.example.mybottomnavigation.ui.adapter.VerticalViewAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var rv_horizontal: RecyclerView
    private lateinit var rv_vertical: RecyclerView
    private lateinit var horizontalAdapter: HorizontalViewAdapter
    private lateinit var verticalAdapter: VerticalViewAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val homeViewModel: HomeViewModel by viewModels {
            factory
        }

        rv_vertical = binding.rvFinishedEvent
        rv_horizontal = binding.rvUpcomingEvent

        horizontalAdapter = HorizontalViewAdapter{ event ->
            if (event.isFavorite == true){
                homeViewModel.removeFavorite(event)
            }else{
                homeViewModel.setFavorite(event)
            }
        }

        verticalAdapter = VerticalViewAdapter { event ->
            if (event.isFavorite == true){
                homeViewModel.removeFavorite(event)
            }else{
                homeViewModel.setFavorite(event)
            }
        }

        rv_horizontal.adapter = horizontalAdapter
        rv_vertical.adapter = verticalAdapter

        rv_horizontal.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_vertical.layoutManager = LinearLayoutManager(context)


        homeViewModel.getUpcomingEvents().observe(viewLifecycleOwner) { result ->
            if (result != null){
                when(result){
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val filteredData = result.data.filter { event -> event.isActive == true }
                        horizontalAdapter.submitList(filteredData)
                    }
                    is Result.Error -> {

                    }
                }
            }
        }

        homeViewModel.getFinishedEvents().observe(viewLifecycleOwner) { result ->
            if (result != null){
                when(result){
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val filteredData = result.data.filter { event -> event.isActive == false }
                        verticalAdapter.submitList(filteredData)
                    }
                    is Result.Error -> {

                    }
                }
            }
        }
        adapterOnClickCallback()
    }

    private fun adapterOnClickCallback(){
        verticalAdapter.setOnItemClickCallback(object: VerticalViewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: EventEntity) {
                intentToDetail(data)
            }
        })

        horizontalAdapter.setOnItemClickCallback(object: HorizontalViewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: EventEntity) {
                intentToDetail(data)
            }
        })
    }

    private fun intentToDetail(data: EventEntity
    ){
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("EVENT_DETAIL", data)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

