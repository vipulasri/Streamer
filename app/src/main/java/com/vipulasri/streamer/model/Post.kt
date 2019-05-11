package com.vipulasri.streamer.model

import pl.droidsonroids.jspoon.annotation.Selector

class PostCard {
    @Selector("div.post-card") lateinit var posts: List<Post>

    override fun toString(): String {
        return "PostCard(posts=$posts)"
    }
}

class Post {
    @Selector("p.card-text.title") lateinit var name: String
    @Selector("small.text-muted") lateinit var info: String
    @Selector(value = "img.card-img-top", attr = "src") lateinit var imageUrl: String

    override fun toString(): String {
        return "Post(name='$name', info='$info', imageUrl='$imageUrl')"
    }

}