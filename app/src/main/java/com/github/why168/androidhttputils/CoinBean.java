package com.github.why168.androidhttputils;

/**
 * @author Edwin.Wu
 * @version 2017/6/20 00:24
 * @since JDK1.8
 */
public class CoinBean {
    public String high;
    public String low;
    public String buy;
    public String sell;
    public String last;
    public double vol;
    public double volume;

    public CoinBean(String high, String low, String buy, String sell, String last, double vol, double volume) {
        this.high = high;
        this.low = low;
        this.buy = buy;
        this.sell = sell;
        this.last = last;
        this.vol = vol;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "CoinBean{" +
                "\nhigh='" + high + '\'' +
                "\nlow='" + low + '\'' +
                "\nbuy='" + buy + '\'' +
                "\nsell='" + sell + '\'' +
                "\nlast='" + last + '\'' +
                "\nvol=" + vol +
                "\nvolume=" + volume +
                '}';
    }
}
