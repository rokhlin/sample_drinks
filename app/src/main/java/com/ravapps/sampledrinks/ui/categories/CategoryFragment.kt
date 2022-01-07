package com.ravapps.sampledrinks.ui.categories

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ravapps.sampledrinks.adapters.CategoryAdapter
import com.ravapps.sampledrinks.R
import org.koin.androidx.viewmodel.ext.android.viewModel


class CategoryFragment: Fragment() {
    private val categoryViewModel: CategoryViewModel by viewModel()
    private val categoryAdapter = CategoryAdapter(2)
    private lateinit var menu: Menu

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.category_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_category -> {
                findNavController().navigate(R.id.action_categoryFragment_to_addCategoryFragment, Bundle.EMPTY)
                menu.getItem(0).isVisible = false
            }
        }

        return true
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.categories_fragment, container, false)

        val rv: RecyclerView = view.findViewById(R.id.rv_categories)

        rv.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = categoryAdapter
        }

        categoryAdapter.setOnClickListener{ id, data, _ ->
            val bundle = Bundle().apply {
                putInt("categoryId", id)
            }
            findNavController().navigate(R.id.action_categoryFragment_to_drinksFragment, bundle)
        }

        categoryAdapter.setOnLongClickListener{ id, data, _->
            val bundle = Bundle().apply {
                putInt("categoryId",id)
                putString("category",data?.title)
                putString("imageName",data?.imageName)
            }

            findNavController().navigate(R.id.action_categoryFragment_to_addCategoryFragment, bundle)
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryViewModel.categories.observe(viewLifecycleOwner, {
            categoryAdapter.updateDataset(it)
        })

    }
}