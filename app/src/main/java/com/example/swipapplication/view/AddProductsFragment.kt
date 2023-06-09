package com.example.swipapplication.view

import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.swipapplication.R
import com.example.swipapplication.databinding.FragmentAddProductsBinding
import com.example.swipapplication.utils.createFile
import com.example.swipapplication.viewmodel.AddProductsFragmentViewModel
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.UCrop
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddProductsFragment : Fragment() {

    private var _binding: FragmentAddProductsBinding? = null
    private val viewModel by viewModel<AddProductsFragmentViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            uri?.let {
                startImageCrop(uri)
            }
        }

    private val uCropLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { uCropImage ->
            uCropImage.data?.let {
                val outputImageUri = UCrop.getOutput(it)
                outputImageUri?.let {
                    val file = File(outputImageUri.path!!)
                    viewModel.isProductDetailsValid(
                        binding.productName.text.toString(),
                        binding.productType.text.toString(),
                        binding.productPrice.text.toString(),
                        binding.productTax.text.toString(),
                        file
                    )
                    Picasso.get().load(outputImageUri).into(binding.productImage)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddProductsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productName.addTextChangedListener {
            viewModel.isProductDetailsValid(
                binding.productName.text.toString(),
                binding.productType.text.toString(),
                binding.productPrice.text.toString(),
                binding.productTax.text.toString(),
                viewModel.product.value?.file
            )
        }

        binding.productType.apply {
            threshold = 0
            setAdapter(
                ArrayAdapter(
                    context,
                    android.R.layout.select_dialog_singlechoice,
                    viewModel.productTypeList
                )
            )
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    showDropDown()
                }
            }
            setOnClickListener { showDropDown() }
            addTextChangedListener {
                viewModel.isProductDetailsValid(
                    binding.productName.text.toString(),
                    binding.productType.text.toString(),
                    binding.productPrice.text.toString(),
                    binding.productTax.text.toString(),
                    viewModel.product.value?.file
                )
            }
        }

        binding.productPrice.addTextChangedListener {
            viewModel.isProductDetailsValid(
                binding.productName.text.toString(),
                binding.productType.text.toString(),
                binding.productPrice.text.toString(),
                binding.productTax.text.toString(),
                viewModel.product.value?.file
            )
        }

        binding.productTax.addTextChangedListener {
            viewModel.isProductDetailsValid(
                binding.productName.text.toString(),
                binding.productType.text.toString(),
                binding.productPrice.text.toString(),
                binding.productTax.text.toString(),
                viewModel.product.value?.file
            )
        }

        binding.productImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        viewModel.isProductDetailsValid.observe(viewLifecycleOwner) {
            binding.buttonAddProduct.isEnabled = it ?: false
        }
        binding.buttonAddProduct.setOnClickListener {
            viewModel.addProduct()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        viewModel.isAdded.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    context,
                    "Product added  ${viewModel.product.value?.product_name}",
                    Toast.LENGTH_SHORT
                ).show()
                binding.productName.setText("")
                binding.productType.setText("")
                binding.productPrice.setText("")
                binding.productTax.setText("")
                Picasso.get().load(R.drawable.product_iomage_placeholder).into(binding.productImage)
                viewModel.productAdded()
            }
        }

    }

    private fun startImageCrop(inputImageUri: Uri) {
        if (inputImageUri.path.isNullOrBlank()) {
            return
        }

        val outputImageFile =
            createFile(requireContext(), "${Calendar.getInstance().timeInMillis}.jpg")
        val outputImageUri = Uri.fromFile(outputImageFile)

        val cropOptions = UCrop.Options().apply {
            setCompressionQuality(100)
            setCompressionFormat(Bitmap.CompressFormat.PNG)
            setToolbarColor(ResourcesCompat.getColor(resources, R.color.purple_200, null))
            setToolbarWidgetColor(ResourcesCompat.getColor(resources, R.color.white, null))
            setCropFrameColor(ResourcesCompat.getColor(resources, R.color.purple_200, null))
            setStatusBarColor(ResourcesCompat.getColor(resources, R.color.black, null))
            setToolbarTitle("Crop Photo")
            setFreeStyleCropEnabled(false)
        }
        val uCropIntent = UCrop.of(inputImageUri, outputImageUri)
            .withAspectRatio(1f, 1f)
            .withOptions(cropOptions)
            .getIntent(requireContext())
        uCropLauncher.launch(uCropIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}