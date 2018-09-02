package io.github.why168.httpcacheandroid.http.service

import com.example.retrofit.model.DelayModel
import io.github.why168.httpcacheandroid.http.call.MyCall
import io.github.why168.httpcacheandroid.model.bin.IpModel
import retrofit2.http.GET

/**
 *
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/31 下午2:47
 * @since JDK1.8
 */
interface HttpBinService {

    @GET("/ip")
    fun geTip(): MyCall<IpModel>

    @GET("/delay/5")
    fun getDelay(): MyCall<DelayModel>
}