package com.example.swipapplication.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipapplication.R
import com.example.swipapplication.databinding.FragmentProductsBinding
import com.example.swipapplication.viewmodel.ProductsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val viewModel by viewModel<ProductsFragmentViewModel>()
    private val navController by lazy { findNavController() }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter = ProductListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        viewModel.productList.observe(viewLifecycleOwner) {
            if (it == null) {
                if (viewModel.isLoading.value == true) {
                    binding.errorView.visibility = View.GONE
                } else {
                    binding.errorView.visibility = View.VISIBLE
                }
            } else {
                if (it.isEmpty()) {
                    binding.recycleView.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE
                } else {
                    binding.recycleView.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.GONE
                    adapter.setProductList(it)
                }
            }
        }
        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleView.adapter = adapter

        binding.buttonTryAgain.setOnClickListener {
            viewModel.getProducts()
        }
        binding.buttonAddNewProduct.setOnClickListener {
            navController.navigate(R.id.SecondFragment)
        }
        binding.fabAddProduct.setOnClickListener {
            navController.navigate(R.id.SecondFragment)
        }

        viewModel.refreshList()

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_products, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    _binding?.let {
                        binding.recycleView.scrollToPosition(0)
                        viewModel.searchProducts(query.lowercase())
                    }
                }
                return true
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}