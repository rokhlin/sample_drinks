package com.ravapps.sampledrinks.ui.categories

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ravapps.sampledrinks.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCategoryFragment: Fragment() {
    private val categoryViewModel: CategoryViewModel by viewModel()

    private lateinit var image: ImageView
    private lateinit var et: EditText
    private lateinit var button: Button
    private lateinit var fab: FloatingActionButton
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.add_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        initMenuState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit -> {
                onEditItemSelected()
            }
            R.id.menu_cancel -> {
                findNavController().popBackStack()
            }
            R.id.menu_delete -> {
                categoryViewModel.deleteDrink()
                findNavController().popBackStack()
            }
        }
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.add_category_fragment, container, false)
        et = view.findViewById(R.id.et_name)
        image = view.findViewById(R.id.img_category)
        fab = view.findViewById(R.id.fab_add_image)
        button = view.findViewById(R.id.btn_save)

        initListeners()

        observeImageChanges()

        fillDataForEditAction()
        return view
    }

    private fun initListeners() {
        et.addTextChangedListener(onTextChanged = { text, _, _, _ ->
            categoryViewModel.setCategoryName(
                text
            )
        })

        fab.setOnClickListener {
            prepareImagePicker(this).createIntent { intent -> startForProfileImageResult.launch(intent) }
        }


        button.setOnClickListener {
            categoryViewModel.addCategory()
            findNavController().popBackStack()
        }
    }

    private fun initMenuState() {
        if (categoryViewModel.editCategoryId >= 0) {
            menu.getItem(0).isVisible = true // Show Edit button
            menu.getItem(1).isVisible = false // Hide Cancel Button
            menu.getItem(2).isVisible = true // Show Delete button

            // disable Items for edit state
            et.isEnabled = false
            fab.visibility = View.GONE
            button.visibility = View.GONE
        }
    }

    private fun fillDataForEditAction() {
        arguments?.run {
            val id = getInt("categoryId")
            val category = getString("category")
            val imageName = getString("imageName")

            category?.let { et.setText(category) }
            imageName?.let { categoryViewModel.setImageName(it) }
            categoryViewModel.editCategoryId = id
        }
    }

    private fun observeImageChanges() {
        categoryViewModel.imageName.observe(viewLifecycleOwner, {
            image.useLocalImage(it)
        })
    }

    private fun onEditItemSelected() {
        et.isEnabled = true
        fab.visibility = View.VISIBLE
        button.visibility = View.VISIBLE

        menu.getItem(0).isVisible = false
        menu.getItem(1).isVisible = true
        menu.getItem(2).isVisible = false
    }

    /**
     *  registerForActivityResult replaced StartActivity for Result
     */
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val uri = result.data

            if (resultCode == Activity.RESULT_OK) {
                uri?.data?.lastPathSegment?.let { categoryViewModel.setImageName(it) }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(uri), Toast.LENGTH_SHORT).show()
            }
        }
}