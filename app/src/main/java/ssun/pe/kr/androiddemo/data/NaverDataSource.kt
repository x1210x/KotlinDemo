package ssun.pe.kr.androiddemo.data

import kotlinx.coroutines.experimental.Deferred
import ssun.pe.kr.androiddemo.data.model.Result

interface NaverDataSource {
    fun searchShop(
            query: String,
            display: Int?,
            start: Int?,
            sort: String?): Deferred<Result>
}