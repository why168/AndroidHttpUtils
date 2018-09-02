package io.github.why168.httpcacheandroid.extensions

import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.channels.FileChannel
import java.security.MessageDigest
import java.util.regex.Pattern

/**
 *
 * 字符串转换工具类
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/4/10 下午4:24
 * @since JDK1.8
 * @version v0.9
 * @see
 */

// 判断字符串是否为NUll
fun String?.isEmptyString(): Boolean {
    if (this == null)
        return false
    return this == "null" || this == "NULL" || this.trim().isEmpty() || this.isEmpty()
}


// 检测密码
fun String.checkPassword(): Boolean {
    val pattern1 = Pattern.compile("^(?![0-9]+$)[a-zA-Z0-9~'!@#￥$%^&*()-+_=:]{8,}")
    val matcher = pattern1.matcher(this)
    return matcher.matches()
}

// 计算大小
fun Any.getFormatDiskSize(size: Double): String {
    val kiloByte = size / 1024
    if (kiloByte < 1) {
        return size.toString() + "Byte"
    }

    val megaByte = kiloByte / 1024
    if (megaByte < 1) {
        val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
        return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
    }

    val gigaByte = megaByte / 1024
    if (gigaByte < 1) {
        val result2 = BigDecimal(java.lang.Double.toString(megaByte))
        return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
    }

    val teraBytes = gigaByte / 1024
    if (teraBytes < 1) {
        val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
        return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
    }
    val result4 = BigDecimal(teraBytes)

    return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
}

// 创建错误消息JS
fun Any.createErrorMessage(callbackId: String, errorMessage: String): String = String.format("executeCallback(%s,\"%s\",null)", callbackId, errorMessage)

// 创建成功消息JS
fun Any.createSuccessMessage(callbackId: String, encodedCallback: String): String = String.format("executeCallback(%s,null,\"%s\")", callbackId, encodedCallback)


fun String.MD5(): String = SHA(this, "MD5")
fun String.SHA1(): String = SHA(this, "SHA-1")
fun String.SHA224(): String = SHA(this, "SHA-224")
fun String.SHA256(): String = SHA(this, "SHA-256")
fun String.SHA384(): String = SHA(this, "SHA-384")
fun String.SHA512(): String = SHA(this, "SHA-512")

/**
 * 二进制转十六进制
 *
 * @param bytes
 * @return
 */
private fun bytesToHex(bytes: ByteArray): String {
    val hexStr = StringBuilder()
    var num: Int
    for (aByte in bytes) {
        num = aByte.toInt()
        if (num < 0) {
            num += 256
        }
        if (num < 16) {
            hexStr.append("0")
        }
        hexStr.append(Integer.toHexString(num))
    }
    return hexStr.toString().toLowerCase()
}

/**
 * SHA
 *
 * @param strText 加密字符串
 * @param strType 将此换成MD5、SHA-1、SHA-224、SHA-256、SHA-384、SHA-512等参数
 * @return 结果统一以小写返回
 */
private fun SHA(strText: String, strType: String): String {
    return try {
        // SHA 加密开始
        // 创建加密对象 并傳入加密类型
        val md = MessageDigest.getInstance(strType)
        // 传入要加密的字符串
        md.update(strText.toByteArray(charset("UTF-8")))
        // 得到 byte 类型结果
        val byteBuffer = md.digest()
        bytesToHex(byteBuffer)
    } catch (e: Exception) {
        e.printStackTrace()
        strText
    }
}

/**
 * 获取文件的md5值
 */
fun getFileMD5(file: File): String {
    var value = ""
    FileInputStream(file).use {
        val byteBuffer = it.channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
        val md5 = MessageDigest.getInstance("MD5")
        md5.update(byteBuffer)
        val bi = BigInteger(1, md5.digest())
        value = bi.toString(16)
    }
    return value.toUpperCase()
}
