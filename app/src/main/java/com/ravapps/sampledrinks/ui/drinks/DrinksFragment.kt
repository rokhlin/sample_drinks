package com.ravapps.sampledrinks.ui.drinks

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ravapps.sampledrinks.adapters.CategoryAdapter
import com.ravapps.sampledrinks.R
import org.koin.androidx.viewmodel.ext.android.viewModel





class DrinksFragment: Fragment() {
    companion object {
        const val actionToAdd = R.id.action_drinksFragment_to_addDrinkFragment
        const val actionIn = R.id.action_global_drinksFragment
    }

    private val drinksViewModel: DrinksViewModel by viewModel()
    private val categoryAdapter = CategoryAdapter()
    private lateinit var menu: Menu

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.drinks_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear_filters -> {
                drinksViewModel.setFilterByCategory(drinksViewModel.categoryId).removeObservers(viewLifecycleOwner)
                drinksViewModel.setFilterByCategory(null).observe(viewLifecycleOwner, { categoryAdapter.updateDataset(it ?: emptyList()) })
                menu.getItem(0).isVisible = false
            }
        }

        return true
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.drinks_fragment, container, false)
        arguments?.getInt("categoryId")?.let {
            Log.d("=========", "category received: ${it}")
           // drinksViewModel.drinksFiltered.removeObserver(observer)
            drinksViewModel.setFilterByCategory(it).observe(viewLifecycleOwner, { categoryAdapter.updateDataset(it ?: emptyList()) })
        }

        val rv: RecyclerView = view.findViewById(R.id.rv_drinks)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add_new_drink)
        fab.setOnClickListener {
            findNavController().navigate(actionToAdd)
        }

        rv.apply {
            layoutManager = LinearLayoutManager(context)
                      adapter = categoryAdapter
        }



        categoryAdapter.setOnLongClickListener{ id, data, _->
            val bundle = Bundle().apply {
                putInt("drinkId",id)
                putString("drink",data?.title)
                putString("category",data?.subTitle)
                putString("imageName",data?.imageName)
            }

            findNavController().navigate(R.id.action_drinksFragment_to_addDrinkFragment, bundle)
        }

        categoryAdapter.setOnClickListener{ _, _, _->
            Toast.makeText(requireContext(), "Long press to edit", Toast.LENGTH_SHORT).show()
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  drinksViewModel.drinksFiltered.observe(viewLifecycleOwner, observer)
//        drinksViewModel.filterByCategory.observe(viewLifecycleOwner, { category ->
//            if (category !== null) {
//                Log.d("=========", "category received: ${category}")
////                drinksViewModel.drinks.removeObservers(viewLifecycleOwner)
////                drinksViewModel.drinksFiltered.observe(viewLifecycleOwner, observer)
//            } else {
//                Log.d("=========", "category received 2: ${category}")
////                drinksViewModel.drinksFiltered.removeObservers(viewLifecycleOwner)
////                drinksViewModel.drinks.observe(viewLifecycleOwner, observer)
//
//            }
//        })

//        drinksViewModel.drinks2.observe(viewLifecycleOwner, {
//            Log.d("========= drinks 2", it.toString())
//        })
    }

}