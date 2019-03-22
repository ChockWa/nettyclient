package com.chockwa.nettyclient.alarmInfo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class AlarmInfoPlate implements Serializable {



    /**
     * channel : 1
     * car : {"color":"white","direction":0,"type":1}
     * plate : {"license":"粤B12345","color":1,"type":1,"confidence":85,"rect":{"left":100,"top":350,"right":683,"bottom":427}}
     * drive : {"speed":54,"type":0,"recognize":0}
     * image : {"filelen":540783,"file":"","thumblen":37652,"thumb":""}
     * timestamp : 1552817488
     * now_time : 1552817492
     */

    /**
     * 数字型，通道号
     */
    private int channel;
    /**
     * 车辆特征
     */
    private Car car;
    /**
     * 车牌特征
     */
    private Plate plate;
    /**
     * 行车信息
     */
    private Drive drive;
    /**
     * 图片信息
     */
    private Image image;
    /**
     * 识别图像帧在视频中的时间戳，采用Unix时间戳格式
     */
    private long timestamp;
    /**
     * 相机与服务器的同步时间，采用Unix时间戳格式，即1970-1-1起到现在的秒数
     */
    @JSONField(name = "now_time")
    private long nowTime;

}
