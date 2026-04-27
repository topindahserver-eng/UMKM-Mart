package com.example.umkmmart.ui.catalog

import android.app.Application
import androidx.lifecycle.*
import com.example.umkmmart.data.local.AppDatabase
import com.example.umkmmart.data.local.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val products: List<Product>) : UiState()
    data class Error(val message: String) : UiState()
}

class CatalogViewModel(application: Application) : AndroidViewModel(application) {

    private val productDao = AppDatabase.getDatabase(application).productDao()
    
    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val products: LiveData<List<Product>> = _searchQuery.flatMapLatest { query ->
        if (query.isEmpty()) {
            productDao.getAllProducts()
        } else {
            productDao.searchProducts(query)
        }
    }.asLiveData()

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    init {
        _uiState.value = UiState.Loading
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            productDao.insertProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productDao.deleteProduct(product)
        }
    }
}