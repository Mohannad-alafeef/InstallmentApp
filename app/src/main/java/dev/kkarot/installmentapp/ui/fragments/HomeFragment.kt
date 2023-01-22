package dev.kkarot.installmentapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.kkarot.installmentapp.R
import dev.kkarot.installmentapp.adapters.HomeAdapter
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.databinding.FragmentHomeBinding
import dev.kkarot.installmentapp.viewmodels.SharedViewModel


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var customerList: List<CustomerInfo>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeToolbar.setupWithNavController(findNavController())

        sharedViewModel.infoLiveData.observe(viewLifecycleOwner) {
            customerList = it
            initRecView(customerList)
        }

        binding.addBtn.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddCustomerFragment()
            )
        }
        binding.homeToolbar.menu.apply {
            (findItem(R.id.search).actionView as SearchView).let { searchView ->
                searchView.queryHint = "Adam or 077xxxxxxx"

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {

                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let {
                            customerList.filter { info ->
                                info.customerName.contains(it, true)
                                        ||
                                        info.customerPhone.contains(it, true)
                            }.let { filteredList ->
                                homeAdapter.filter(filteredList)
                            }
                        }
                        return false
                    }
                })
            }
        }

        return binding.root
    }

    private fun initRecView(it: List<CustomerInfo>) {
        binding.mainRecView.apply {
            homeAdapter = HomeAdapter(it)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }
    }


}