package com.vipulasri.streamer.model

import pl.droidsonroids.jspoon.annotation.Selector

class PostCard {
    @Selector(".post-card") lateinit var posts: List<Post>

    override fun toString(): String {
        return "PostCard(posts=$posts)"
    }
}

class Post {
    @Selector("div.card-body > a[href]", attr = "href") lateinit var url: String
    @Selector(".card-text.title") lateinit var name: String
    @Selector("small.text-muted") lateinit var info: String
    @Selector(value = "img.card-img-top", attr = "src") lateinit var imageUrl: String

    fun getId() = url.split('/', ignoreCase = true).last()

    override fun toString(): String {
        return "Post(url='$url', name='$name', info='$info', imageUrl='$imageUrl')"
    }

}

class PostDetails {
    @Selector(value = "video > source", attr = "src") lateinit var videoUrl: String
    @Selector(value = "img.card-img-top", attr = "src") lateinit var thumbUrl: String

    override fun toString(): String {
        return "PostDetails(videoUrl='$videoUrl', thumbUrl='$thumbUrl')"
    }

}