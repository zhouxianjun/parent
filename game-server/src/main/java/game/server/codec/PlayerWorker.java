package game.server.codec;

import com.gary.netty.codec.Worker;
import com.gary.netty.net.Cmd;
import com.gary.netty.net.Packet;
import game.server.event.PlayerReceivedEvent;
import game.world.dto.PlayerUser;
import io.netty.channel.Channel;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description: 玩家客户端的消息woker
 * @date 2015/4/14 16:30
 */
public class PlayerWorker extends Worker<PlayerUser, PlayerReceivedEvent> {

    public PlayerWorker(Channel channel) {
        super(channel);
    }

    @Override
    protected boolean ping(Channel channel, PlayerUser object, short cmd) {
        if (cmd == Cmd.PING){
            object.heartTime = System.currentTimeMillis();
            channel.writeAndFlush(Packet.PING);
            return true;
        }
        return false;
    }
}
