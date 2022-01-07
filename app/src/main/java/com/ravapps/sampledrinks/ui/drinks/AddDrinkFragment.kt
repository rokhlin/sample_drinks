package com.ravapps.sampledrinks.ui.drinks

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ravapps.sampledrinks.DEFAULT_PICTURE_FOLDER
import com.ravapps.sampledrinks.R
import com.ravapps.sampledrinks.getAppSpecificAlbumStorageDir
import com.ravapps.sampledrinks.loadAssetImage
import com.ravapps.sampledrinks.ui.categories.CategoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddDrinkFragment: Fragment() {
    private val drinksViewModel: DrinksViewModel by viewModel()
    private val categoryViewModel: CategoryViewModel by viewModel()

    private lateinit var sp: Spinner
    private lateinit var image: ImageView
    private lateinit var et: EditText
    private lateinit var button: Button
    private lateinit var fab: FloatingActionButton
    private lateinit var menu: Menu

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.add_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)

        initMenuState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit -> {
                et.isEnabled = true
                sp.isEnabled = true
                fab.visibility = View.VISIBLE
                button.visibility = View.VISIBLE

                menu.getItem(0).isVisible = false
                menu.getItem(1).isVisible = true
                menu.getItem(2).isVisible = false
            }
            R.id.menu_cancel -> {
                findNavController().popBackStack()
            }
            R.id.menu_delete -> {
                drinksViewModel.deleteDrink()
            }
        }

        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.add_drink_fragment, container, false)
        sp = view.findViewById(R.id.sp_category)
        et = view.findViewById(R.id.et_name)
        image = view.findViewById(R.id.img_drink)
        fab = view.findViewById(R.id.fab_add_image)
        button = view.findViewById(R.id.btn_save)


        initListeners()

        observeCategories()

        observeImageChanges()

        fillDataForEditAction()
        return view
    }

    private fun initListeners() {
        et.addTextChangedListener(onTextChanged = { text, _, _, _ ->
            drinksViewModel.setDrinkName(
                text
            )
        })

        fab.setOnClickListener {
            // Add logic to save image and get name
            ImagePicker.with(this)
                .crop(
                    1f,
                    1f
                )                // Crop image(Optional), Check Customization for more option
                .maxResultSize(
                    600,
                    600
                ) // Final image resolution will be less than 1080 x 1080(Optional)
                .saveDir(getAppSpecificAlbumStorageDir(requireContext(), DEFAULT_PICTURE_FOLDER))
                .galleryMimeTypes(  // Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .createIntent { intent -> startForProfileImageResult.launch(intent) }
        }


        button.setOnClickListener {
            val categoryName = sp.selectedItem?.toString()
            Log.d("=======", "spinner.categoryName: ${categoryName}")
            val categoryId = categoryViewModel.getCategoryIdByName(categoryName)

            drinksViewModel.addDrink(categoryId)
            findNavController().popBackStack()
        }
    }

    private fun fillDataForEditAction() {
        arguments?.run {
            val id = getInt("drinkId")
            val name = getString("drink")
            val category = getString("category")
            val imageName = getString("imageName")

            name?.let { et.setText(name) }
            imageName?.let { drinksViewModel.setImageName(it) }
            category?.let { drinksViewModel.setCategoryName(it) }
            drinksViewModel.editDrinkId = id
        }
    }

    private fun observeImageChanges() {
        drinksViewModel.imageName.observe(viewLifecycleOwner, {
            image.loadAssetImage(it)
        })
    }

    private fun observeCategories() {
        categoryViewModel.categories.observe(viewLifecycleOwner, {
            val categoryNames = it.map { category -> category.name }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, categoryNames
            )
            sp.adapter = adapter
            sp.setSelection(0)

            observeCategoryChanges()
        })
    }

    private fun observeCategoryChanges() {
        drinksViewModel.categoryName.observe(viewLifecycleOwner, { categoryName ->
            val spinnerPosition: Int =
                (sp.adapter as ArrayAdapter<String>).getPosition(categoryName)
            sp.setSelection(spinnerPosition)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drinksViewModel
            .newDrinkName
            .observe(viewLifecycleOwner, { button.isEnabled = !it.isNullOrEmpty()})
    }

    private fun initMenuState() {
        if (drinksViewModel.editDrinkId >= 0) {
            menu.getItem(0).isVisible = true // Show Edit button
            menu.getItem(1).isVisible = false // Hide Cancel Button
            menu.getItem(2).isVisible = true // Show Delete button

            // disable Items for edit state
            et.isEnabled = false
            sp.isEnabled = false
            fab.visibility = View.GONE
            button.visibility = View.GONE
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val uri = result.data

            if (resultCode == Activity.RESULT_OK) {
                uri?.data?.lastPathSegment?.let { drinksViewModel.setImageName(it) }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(uri), Toast.LENGTH_SHORT).show()
            }
        }
}