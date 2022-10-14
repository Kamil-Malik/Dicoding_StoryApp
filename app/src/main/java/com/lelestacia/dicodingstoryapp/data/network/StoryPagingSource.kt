package com.lelestacia.dicodingstoryapp.data.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.dicodingstoryapp.data.api.DicodingAPI
import com.lelestacia.dicodingstoryapp.data.model.network.NetworkStory

class StoryPagingSource(private val dicodingAPI: DicodingAPI, private val token: String) :
    PagingSource<Int, NetworkStory>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkStory> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData =
                dicodingAPI.getStoriesWithPage(this.token, position, params.loadSize)
            LoadResult.Page(
                data = responseData.listNetworkStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listNetworkStory.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            Log.e(javaClass.name, "load: ${exception.message}")
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkStory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}