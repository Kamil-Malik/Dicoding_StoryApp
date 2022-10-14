package com.lelestacia.dicodingstoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.dicodingstoryapp.data.model.local.LocalStory

class StoryPagingSourceTest : PagingSource<Int, LiveData<List<LocalStory>>>() {
    companion object {
        fun snapshot(items: List<LocalStory>): PagingData<LocalStory> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<LocalStory>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<LocalStory>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}