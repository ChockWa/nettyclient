package com.chockwa.nettyclient.alarmInfo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/19 18:07
 * @description:
 */
@Data
public class AlarmInfoPlateResult implements Serializable {

    /**
     * AlarmInfoPlateResult : {"channel":1,"car":{"color":"white","direction":0,"type":1},"plate":{"license":"粤B12345","color":1,"type":1,"confidence":85,"rect":{"left":100,"top":350,"right":683,"bottom":427}},"drive":{"speed":54,"type":0,"recognize":0},"image":{"filelen":540783,"file":"","thumblen":37652,"thumb":""},"timestamp":1552817488,"now_time":1552817492}
     */
    @JSONField(name = "AlarmInfoPlate")
    private AlarmInfoPlate alarmInfoPlate;

    @JsonIgnore
    private String deviceId;
}
