package io.github.why168.httpcacheandroid.http.service

import io.github.why168.httpcacheandroid.http.call.MyCall
import io.github.why168.httpcacheandroid.model.bin.IpModel
import io.github.why168.httpcacheandroid.model.github.GithubModel
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.GET

/**
 *
 *
 * @author Edwin.Wu
 * @version 2018/8/30 下午7:15
 * @since JDK1.8
 */
internal interface HttpGithubService {

    @GET("/")
    fun getGitHub(): MyCall<GithubModel>

    @GET("/")
    fun getGitHub2(): Call<GithubModel>

    @GET("/")
    fun getGitHub3(): Flowable<GithubModel>

}