package com.vipulasri.streamer.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hudle.consumer.app.data.remote.DataWrapper
import com.vipulasri.streamer.data.PostsRepository
import com.vipulasri.streamer.model.Post
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

class PostsViewModel @Inject constructor(private var repository: PostsRepository): ViewModel() {

    private val mCompositeDisposable = CompositeDisposable()
    private val mPostsObserver = MutableLiveData<DataWrapper<List<Post>>>()

    fun loadPosts(page: Int = 1) {
        mPostsObserver.value = DataWrapper(isLoading = true)
        mCompositeDisposable.add(repository.loadPosts(page)
                .subscribe ({
                    mPostsObserver.value = DataWrapper(response = it)
                }, {
                    Log.e("error", "->${it.message}")
                    Log.e("error stacktrace", "->${Arrays.toString(it.stackTrace)}")
                    mPostsObserver.value = DataWrapper(error = it)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }

    fun getPostsObserver(): LiveData<DataWrapper<List<Post>>> = mPostsObserver
}