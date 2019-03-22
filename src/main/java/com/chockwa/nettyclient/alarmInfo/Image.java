package com.chockwa.nettyclient.alarmInfo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Image implements Serializable {
    /**
     * filelen : 540783
     * file :
     * thumblen : 37652
     * thumb :
     */

    /**
     * 大图片数据实际长度
     */
    @JSONField(name = "filelen")
    private int fileLength;
    /**
     * 大图片数据的base64编码结果
     */
    private String file;
    /**
     * 小图片数据实际长度
     */
    @JSONField(name = "thumblen")
    private int thumbLength;
    /**
     * 小图片数据的base64编码结果
     */
    private String thumb;

}
