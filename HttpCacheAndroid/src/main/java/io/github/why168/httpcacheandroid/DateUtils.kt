package io.github.why168.httpcacheandroid

import java.text.SimpleDateFormat
import java.util.*

/**
 *
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/31 下午4:08
 * @since JDK1.8
 */
class DateUtils {
    companion object {

        fun getLoactTime(): String {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")//设置日期格式
            return df.format(Date())
        }

    }
}