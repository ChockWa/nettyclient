package com.chockwa.nettyclient.alarmInfo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Drive implements Serializable {

    /**
     * speed : 54
     * type : 0
     * recognize : 0
     */

    /**
     * 行驶速度，单位km/h
     */
    private int speed;
    /**
     * 行驶类型：0正常行驶、1超速行驶、2压线行驶、3逆向行驶、4不按导向车道行驶、5闯红灯行驶
     */
    @JSONField(name = "drivetype")
    private int driveType;
    /**
     * 测速类型：0雷达测速，1视频测速
     */
    private int recognize;

}
