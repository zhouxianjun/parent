package com.gary.netty.codec;

import com.gary.netty.event.ReceivedEvent;
import io.netty.channel.group.ChannelGroup;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description: Basic解码处理
 * @date 2015/6/4 14:55
 */
public class BasicDecoderHandler<W extends Worker<T, ? extends ReceivedEvent<T>>, T> extends AbstractDecoderHandler<W, T> {
    public BasicDecoderHandler(ChannelGroup channelGroup, Class<W> worker) {
        super(channelGroup);
        super.setWorkerClass(worker);
    }
    public BasicDecoderHandler(ChannelGroup channelGroup, int maxFrameLength,
                               int lengthFieldOffset, int lengthFieldLength,
                               int lengthAdjustment, int initialBytesToStrip, Class<W> worker) {
        super(channelGroup, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        super.setWorkerClass(worker);
    }
}
