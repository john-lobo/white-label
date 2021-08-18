package br.com.douglasmotta.whitelabeltutorial.ui.addproduct

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.douglasmotta.whitelabeltutorial.databinding.AddProductFragmentBinding
import br.com.douglasmotta.whitelabeltutorial.util.CurrencyTextWatcher
import br.com.douglasmotta.whitelabeltutorial.util.PRODUCT_KEY
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductFragment : BottomSheetDialogFragment() {

    private var _binding: AddProductFragmentBinding? = null
    private val binding get()= _binding!!
    private val viewModel: AddProductViewModel by viewModels()

    private var imagemUri : Uri? = null
    private val getContent  =
        registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
            imagemUri = uri
            binding.imageProduct.setImageURI(uri)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddProductFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeVMevents()
        setListeners()
    }

    private fun observeVMevents(){
        viewModel.imageUriErrorResId.observe(viewLifecycleOwner){drawabreResId ->
            binding.imageProduct.setBackgroundResource(drawabreResId)
        }

        viewModel.descriptionFieldErrorResId.observe(viewLifecycleOwner){stringErrorResID ->
            binding.inputLayoutDescription.setError(stringErrorResID)
        }
        viewModel.priceFieldErrorResId.observe(viewLifecycleOwner){stringErrorResID ->
            binding.inputLayoutPrice.setError(stringErrorResID)
        }
        viewModel.productCreated.observe(viewLifecycleOwner){ product ->
            findNavController().run {
                previousBackStackEntry?.savedStateHandle?.set(PRODUCT_KEY, product)
                popBackStack()
            }
        }
    }

    private fun TextInputLayout.setError(stringResId:Int?){
        error = if(stringResId != null){
            getString(stringResId)
        }else null
    }

    private fun setListeners(){
        binding.imageProduct.setOnClickListener {
            chooseImage()
        }

        binding.inputPrice.run {
            addTextChangedListener(CurrencyTextWatcher(this))
        }

        binding.buttonAddProduct.setOnClickListener {
            val description = binding.inputDescription.text.toString()
            val price = binding.inputPrice.text.toString()
            viewModel.createProduct(description,price,imagemUri)

        }
    }

    private fun chooseImage(){
        getContent.launch("image/*")
    }


}