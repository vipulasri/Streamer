package com.vipulasri.streamer.data.remote;

import androidx.collection.ArrayMap;
import com.vipulasri.streamer.model.PostCard;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET("load/loadPosts.php")
    Observable<PostCard> posts(@QueryMap ArrayMap<String, String> queryMap);

}
