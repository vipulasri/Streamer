package com.vipulasri.streamer.data

import androidx.collection.arrayMapOf
import com.vipulasri.streamer.data.remote.ApiService
import com.vipulasri.streamer.model.Post
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepository @Inject
constructor(private val mApiService: ApiService) {

    fun loadPosts(page: Int): Observable<List<Post>> {
        val map = arrayMapOf<String, String>()
        map["loadPostOrder"] = "hot"
        map["loadPostMode"] = "standard"
        map["loadPostPage"] = page.toString()

        return mApiService.posts(map)
            .map { it.posts }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
