package com.gary.netty.net;

import com.gary.error.ErrorCode;
import com.gary.netty.protobuf.ResultPro;
import com.gary.util.ErrorsUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/3/6 10:29
 */
@Slf4j
@AllArgsConstructor
@Getter
public class Packet {
    @Setter
    private Short cmd;

    private MessageLite ret;

    private MessageLite body;

    private long startTime;

    public MessageLite getRet(){
        if (ret == null)
            ret = ResultPro.Result.getDefaultInstance();
        try {
            ResultPro.Result.Builder builder = ResultPro.Result.parseFrom(ret.toByteArray()).toBuilder();
            if (!builder.hasMsg()){
                builder.setMsg(ErrorsUtil.getErrorDesc(builder.getCode()));
                return builder.build();
            }
        } catch (InvalidProtocolBufferException e) {
            log.error("获取ret异常!", e);
        }
        return ret;
    }

    public static Packet createException(Short cmd, int errorCode, MessageLite body){
        ResultPro.Result.Builder result = ResultPro.Result.newBuilder();
        result.setCode(errorCode);
        return new Packet(cmd, result.build(), body, 0);
    }

    public static Packet createGlobalException(){
        return createException(Cmd.GLOBAL_EXCEPTION, ErrorCode.UNKNOWN_ERROR, null);
    }

    public static Packet createGlobalException(int errorCode){
        return createException(Cmd.GLOBAL_EXCEPTION, errorCode, null);
    }

    public static Packet createSuccess(short cmd, MessageLite body){
        return new Packet(cmd, null, body, 0);
    }

    public static Packet createError(int errorCode, MessageLite body){
        return createException(null, errorCode, body);
    }

    public int calcSize(){
        int size = 3; //cmd
        size += getRet().toByteArray().length;
        if(getBody() != null) {
            size += getBody().toByteArray().length;
        }
        return size;
    }

    public void write(ByteBuf byteBuf){
        byteBuf.writeShort(calcSize()); //输出总长度
        byteBuf.writeShort(cmd); //命令
        byteBuf.writeShort(getRet().toByteArray().length); //命令
        byteBuf.writeBytes(getRet().toByteArray());

        if(getBody() != null) {
            byteBuf.writeBytes(getBody().toByteArray());
        }
    }

    public Packet setStartTime(long startTime){
        this.startTime = startTime;
        return this;
    }

    public static final Packet PING = createSuccess(Cmd.PING, null);
}
