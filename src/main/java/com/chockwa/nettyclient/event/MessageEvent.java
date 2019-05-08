package com.chockwa.nettyclient.event;

import lombok.Builder;
import lombok.Data;

/**
 * sse推送事件
 * @param <T>
 */
@Builder
@Data
public class MessageEvent<T> {

    private T data;
    private int eventCode;

}
