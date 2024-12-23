package com.example.mybottomnavigation.ui.favorite_event

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybottomnavigation.DetailActivity
import com.example.mybottomnavigation.data.local.entity.EventEntity
import com.example.mybottomnavigation.databinding.FragmentFavoriteEventBinding
import com.example.mybottomnavigation.helper.ViewModelFactory
import com.example.mybottomnavigation.ui.adapter.VerticalViewAdapter
import com.example.mybottomnavigation.ui.home.HomeViewModel

class FavoriteEventFragment : Fragment() {

    private var _binding: FragmentFavoriteEventBinding? = null
    private lateinit var rvVertical: RecyclerView
    private lateinit var verticalAdapter: VerticalViewAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val homeViewModel: HomeViewModel by viewModels {
            factory
        }

        rvVertical = binding.rvFavoriteEvent
        verticalAdapter = VerticalViewAdapter{ event ->
            if (event.isFavorite == true){
                homeViewModel.removeFavorite(event)
            }
        }
        rvVertical.adapter = verticalAdapter
        rvVertical.layoutManager = LinearLayoutManager(context)



        val searchView = _binding?.searchView

        homeViewModel.getFavoriteEvents().observe(viewLifecycleOwner) { result ->
            if (result != null){
               verticalAdapter.submitList(result)
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

        Log.d("Favorite data", "Intent Data $data")
        intent.putExtra("EVENT_DETAIL", data)

        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}