package com.chockwa.nettyclient.alarmInfo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Plate implements Serializable {


    /**
     * license : 粤B12345
     * color : 1
     * type : 1
     * confidence : 85
     * rect : {"left":100,"top":350,"right":683,"bottom":427}
     */

    /**
     * 车牌号
     */
    private String license;
    /**
     * 车牌颜色：0：未知、1：蓝色、2：黄色、3：白色、4：黑色、5：绿色
     */
    @JSONField(name = "platecolor")
    private int plateColor;
    /**
     * 车牌类型 0：未知车牌:、1：蓝牌小汽车、2：:黑牌小汽车、3：单排黄牌、
     * 4：双排黄牌、 5： 警车车牌、6：武警车牌、7：个性化车牌、8：单排军车牌、
     * 9：双排军车牌、10：使馆车牌、11： 香港进出中国大陆车牌、12：农用车牌、
     * 13：教练车牌、14：澳门进出中国大陆车牌、15：双层武警车牌、16：武警总队车牌、
     * 17：双层武警总队车牌
     */
    @JSONField(name = "platetype")
    private int plateType;
    /**
     * 识别结果可信度1-100，1最低
     */
    private int confidence;
    /**
     * 车牌在大图片中的位置，单位为像素，大图的左上角为(0，0)
     */
    private Rect rect;

}
