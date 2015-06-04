package game.server;

import com.gary.netty.AbstractClient;
import com.gary.netty.codec.BasicDecoderHandler;
import com.gary.netty.codec.Worker;
import com.gary.netty.net.Packet;
import game.server.codec.CenterWorker;
import game.world.AppCmd;
import game.world.AppContext;
import game.world.Server;
import game.world.protobuf.ServerPro;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/15 17:39
 */
public class CenterClient extends AbstractClient {

    public CenterClient(String ip, int port, String name) {
        super(ip, port, name);
    }

    @Override
    protected void connected(Channel channel) {
        ServerPro.Server.Builder server = ServerPro.Server.newBuilder();
        AppContext.getBean(Server.class).parseObject(server);
        Server s = new Server();
        s.setIp(ip);
        s.setPort(port);
        s.setName(name);
        channel.attr(Worker.PLAYER_KEY).set(s);
        channel.writeAndFlush(Packet.createSuccess(AppCmd.CENTER_CONNECT, server.build()));
    }

    @Override
    protected ChannelHandler getDecoderHandler() {
        return new BasicDecoderHandler<CenterWorker, Server>(getAllChannels(), CenterWorker.class);
    }
}
