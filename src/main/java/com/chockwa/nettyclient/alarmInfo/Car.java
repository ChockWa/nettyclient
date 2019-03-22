package com.chockwa.nettyclient.alarmInfo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Car implements Serializable {

    /**
     * color : white
     * direction : 0
     * type : 1
     */

    /**
     * 车辆颜色
     */
    @JSONField(name = "carcolor")
    private String carColor;
    /**
     * 行车方向：0来向，车头冲相机，1去向，车尾冲相机
     */
    private int direction;
    /**
     * 车辆类型：0未知，1小型乘用车（7座以下），2货车，3大中型客车（7座以上
     */
    @JSONField(name = "cartype")
    private int carType;

}
