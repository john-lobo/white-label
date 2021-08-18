package br.com.douglasmotta.whitelabeltutorial.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import br.com.douglasmotta.whitelabeltutorial.R
import br.com.douglasmotta.whitelabeltutorial.databinding.FragmentProductsBinding
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import br.com.douglasmotta.whitelabeltutorial.util.PRODUCT_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val productsAdapter = ProductsAdapter()
    private val viewModel: ProdutcsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        setRecycleView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycleView()
        setListeners()
        observeNavBackStack()
        observeVMEvents()

        viewModel.getProducts()

    }

    private fun setRecycleView() {
        binding.recyclerProducts.run {
            setHasFixedSize(true)
            adapter=productsAdapter
        }
    }

    private fun setListeners(){
        binding.floatingActionButtonAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_addProductFragment)
        }

    }

    private fun observeNavBackStack(){
        findNavController().run {
            val navBackStackEntry = getBackStackEntry(R.id.productsFragment)
            val savedStateHandle = navBackStackEntry.savedStateHandle
            val observer = LifecycleEventObserver{ _, event ->
                if(event == Lifecycle.Event.ON_RESUME && savedStateHandle.contains(PRODUCT_KEY)){
                    val product = savedStateHandle.get<Product>(PRODUCT_KEY)
                    val oldList = productsAdapter.currentList
                    val newList = oldList.toMutableList().apply {
                        add(product)
                    }
                    productsAdapter.submitList(newList)
                    binding.recyclerProducts.smoothScrollToPosition(newList.size - 1)
                    savedStateHandle.remove<Product>(PRODUCT_KEY)
                }
            }
            navBackStackEntry.lifecycle.addObserver(observer)

            viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver{ _,event ->
                if(event == Lifecycle.Event.ON_DESTROY){
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            })
        }
    }

    private fun observeVMEvents(){
        viewModel.productData.observe(viewLifecycleOwner){products ->
            productsAdapter.submitList(products)
        }

        viewModel.addButtonVisibilityData.observe(viewLifecycleOwner){ visibility ->
            binding.floatingActionButtonAddProduct.visibility = visibility
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}