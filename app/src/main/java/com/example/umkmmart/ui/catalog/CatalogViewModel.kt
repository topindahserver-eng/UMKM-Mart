package com.example.umkmmart.ui.catalog

import android.app.Application
import androidx.lifecycle.*
import com.example.umkmmart.data.local.AppDatabase
import com.example.umkmmart.data.local.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class CatalogViewModel(application: Application) : AndroidViewModel(application) {

    private val productDao = AppDatabase.getDatabase(application).productDao()
    
    private val _searchQuery = MutableStateFlow("")
    private val _categoryFilter = MutableStateFlow("Semua")

    @OptIn(ExperimentalCoroutinesApi::class)
    val products: LiveData<List<Product>> = combine(_searchQuery, _categoryFilter) { query, category ->
        Pair(query, category)
    }.flatMapLatest { (query, category) ->
        productDao.searchAndFilterProducts(query, category)
    }.asLiveData()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: String) {
        _categoryFilter.value = category
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productDao.deleteProduct(product)
        }
    }
}