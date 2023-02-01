package dev.kkarot.installmentapp.ui.fragments.info

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.kkarot.installmentapp.R
import dev.kkarot.installmentapp.databinding.FragmentHomeBinding
import dev.kkarot.installmentapp.adapters.HomeAdapter
import dev.kkarot.installmentapp.database.models.CustomerInfo
import dev.kkarot.installmentapp.viewmodels.SharedData
import dev.kkarot.installmentapp.viewmodels.SharedDataBase
import dev.kkarot.installmentapp.views.bottomsheets.CustomerBottomSheet


@AndroidEntryPoint
class HomeFragment : Fragment() {
    companion object{
        private const val TAG = "---HomeFrag"
    }
    private lateinit var binding: FragmentHomeBinding
    private val sharedDataBase: SharedDataBase by activityViewModels()
    private val sharedData:SharedData by activityViewModels()
    private lateinit var homeAdapter: HomeAdapter
    private var customerList: ArrayList<CustomerInfo> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d("---TAG", "onCreateView: ")

        binding.homeToolbar.setupWithNavController(findNavController())
        sharedDataBase.onHome()
        sharedDataBase.infoLiveData.observe(viewLifecycleOwner) {
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
                searchView.queryHint = "name or phone"
                searchView.setOnCloseListener {
                    searchView.query?.let {
                        customerList.filter { info ->
                            info.customerName.contains(it, true)
                                    ||
                                    info.customerPhone.contains(it, true)
                        }.let { filteredList ->
                            homeAdapter.filter(filteredList)
                        }
                    }
                    return@setOnCloseListener true

                }
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
            homeAdapter = HomeAdapter(it,onItemClick,onItemLongClick)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }

    }
    private val onItemClick:(CustomerInfo) ->Unit = { info ->
        sharedData.setCustomer(info)
        HomeFragmentDirections.actionHomeFragmentToCustomerInfoFragment().let { navDir ->
            findNavController().navigate(navDir)
        }
    }
    private val onItemLongClick:(CustomerInfo,Int)->Unit = { info,pos ->
        val sheet = CustomerBottomSheet(info,pos,onDelete)
        sheet.show(childFragmentManager,CustomerBottomSheet.TAG)
    }
    private val onDelete : (Int) ->Unit = { pos ->
        customerList.removeAt(pos)
        homeAdapter.remove(customerList,pos)

    }





}