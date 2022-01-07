# sample_drinks
Important components and patterns:
DI KOIN, Room, Mvvm, Coroutines, LiveData, ImagePicker, Navigation. LifeCycle components


Baseflow 
Application Starts on Categories view.
All Items are stored in db. 
Images assets in APP PICTURES folder
The First start prepopulates the default category set.

Categories view.
The View is based on Grid RecyclerView with 2 columns. Exist an ability to change it on the fly:(UI toggle not implemented)
1 column - List RecyclerView
2 and more - Grid RecyclerView. Item size calculated dynamically but not less than 100dp

Menu contains
1- Option to add new Category (opening Category creator screen)

Click on CategoryItem opens Existing Items in the category.
Long Click on CategoryItem opens Category view edit screen(opening Category creator screen in view mode)


Items View
1 column - List RecyclerView. Exist an ability to change it on the fly:(UI toggle not implemented)
2 and more - Grid RecyclerView. Item size calculated dynamically but not less than 100dp

Menu contains
1- Clear Filters item (reset category filter and show all items)

Click on Item shows Toast message to use for view and Edin Long Click action.
Long Click on Item opens view edit screen(opening Item creator screen in view mode)


Add Category creator view
Has Image view and selector to change it using Image Picker(External Library)
Category Name field 

and Submit button (will be enabled if the name exists) updates item in Edit mode and add the new item in Create mode

Open in View mode hide all button and text fields and fill data from selected category
Activation of the Edit mode(activation in the menu) returns all possibilities to create a category with previous values. 


Menu contains
In Create mode and Edit mode:
1- Cancel button (return to the previous screen without changes)

In View mode:
1- Edit mode (Turn on Edit mode)
2- Delete item (Delete item and return to the previous screen)

Save Action start saving transaction in the background and update the dataset in the previous screen.

All Db operations wrapped with coroutine or LiveData

Add Item creator view - has the same functionality
But In Addition category selector.

Possibility to improve(small steps that wasn't implemented yet):
1. Add persistency by saving test field data in the saved state 
2. Add Button to show items without filter by Category
3. Change column count in Grid in landscape orientation
4. Add themes support
5. In Item view add on click action - View Mode on Long click Edit mode
6. Add possibility sort Items by Name and Id in menu
7. Custom layout for Category selector(Spinner)





