package game.server.event;

import com.gary.netty.codec.Worker;
import com.gary.netty.event.HandlerEvent;
import com.gary.netty.event.ReceivedEvent;
import com.gary.netty.handler.Handler;
import com.gary.netty.net.Packet;
import com.gary.netty.protobuf.ResultPro;
import com.sun.tools.javac.util.Assert;
import game.server.Cache;
import game.world.Server;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/18 13:36
 */
@Slf4j
public class CenterReceivedEvent extends ReceivedEvent<Server> {
    public CenterReceivedEvent(int length, short cmd, Server object, Channel channel, ResultPro.Result ret, Worker<Server, ? extends ReceivedEvent> worker, byte[] data) {
        super(length, cmd, object, channel, ret, worker, data);
    }

    @Override
    public void run() {
        Assert.checkNonNull(Cache.GAME_EVENT_CMD, "游戏事件处理Dispatcher未初始化!");
        HandlerEvent<Handler> handlerEvent = Cache.GAME_EVENT_CMD.get(this.getCmd());
        if(handlerEvent == null) {
            log.info("收到没有处理事件的消息, [中心服 = {},cmd = 0x{}]", this.getObject(), Integer.toHexString(this.getCmd()));
            this.write(Packet.createGlobalException());
            return;
        }
        handle(handlerEvent);
    }
}
