package com.junior.forjunto.mvp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ProductHuntTopicsApiResponse(
        @SerializedName("topics") var topics: List<Topic>? = null
)

data class ProductHuntProductsApiResponse(
        @SerializedName("posts") @Expose var posts: List<Post>? = null
)

data class Post(
        @SerializedName("name") @Expose var name: String? = null,

        @SerializedName("tagline") @Expose var tagline: String? = null,

        @SerializedName("redirect_url") @Expose var redirectUrl: String? = null,

        @SerializedName("screenshot_url") @Expose var screenshotUrl: ScreenshotUrl? = null,

        @SerializedName("thumbnail") @Expose var thumbnail: Thumbnail? = null,

        @SerializedName("votes_count") @Expose var votesCount: Int? = null
)

data class Thumbnail(
        @SerializedName("image_url") @Expose var imageUrl: String? = null
)

data class ScreenshotUrl(
        @SerializedName("300px") @Expose var _300px: String? = null,

        @SerializedName("850px") @Expose var _850px: String? = null
)

data class Topic(
        @SerializedName("id") @Expose var id: Int? = null, // 356

        @SerializedName("name") @Expose var name: String? = null, // Touch Bar Apps

        @SerializedName("slug") @Expose var slug: String? = null, // touch-bar-apps

        @SerializedName("created_at") @Expose var createdAt: String? = null, // 2016-11-07T06:33:29.567-08:00

        @SerializedName("description") @Expose var description: String? = null, // Apps built for Apple's new Touch Bar �

        @SerializedName("followers_count") @Expose var followersCount: Int? = null, // 25156

        @SerializedName("posts_count") @Expose var postsCount: Int? = null, // 152

        @SerializedName("trending") @Expose var trending: Boolean? = null, // true

        @SerializedName("updated_at") @Expose var updatedAt: String? = null, // 2017-12-24T22:43:00.630-08:00

        @SerializedName("image") @Expose var image: Any? = null // https://ph-files.imgix.net/c74c96b5-94a5-4fce-8b39-9ec0e094d7ac?auto=format
)