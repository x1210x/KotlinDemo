package ssun.pe.kr.androiddemo.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ssun.pe.kr.androiddemo.domain.main.SearchImageUseCase
import ssun.pe.kr.androiddemo.presentation.BaseViewModel

class ImageViewModel : BaseViewModel() {

    private val searchImageUseCase = SearchImageUseCase(viewModelScope)

    private val query = MutableLiveData<String>()

    private val result = map(query) { query ->
        searchImageUseCase(query)
    }
    val items = switchMap(result) { it.pagedList }
    val networkState = switchMap(result) { it.networkState }
    val refrefhState = switchMap(result) { it.refreshState }

    fun refresh() = result.value?.refresh?.invoke()

    fun search(query: String) = viewModelScope.launch {
        if (query.isNotBlank()) {
            this@ImageViewModel.query.value = query
        }
    }
}