package com.hudle.consumer.app.data.remote

class DataWrapper<T>(var response: T? = null, var error: Throwable? = null, var isLoading: Boolean = false) {

}