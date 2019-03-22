package com.chockwa.nettyclient.alarmInfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Rect implements Serializable {

    /**
     * left : 100
     * top : 350
     * right : 683
     * bottom : 427
     */

    /**
     * 左边界
     */
    private int left;
    /**
     * 上边界
     */
    private int top;
    /**
     * 右边界
     */
    private int right;
    /**
     * 下边界
     */
    private int bottom;

}
