package game.center.codec;

import com.alibaba.fastjson.JSONArray;
import com.gary.netty.codec.Worker;
import game.center.event.ServerReceivedEvent;
import game.world.Server;
import game.world.utils.MemcachedCacheVar;
import game.world.utils.MemcachedUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:处理服务器的Worker
 * @date 2015/4/16 15:36
 */
@Slf4j
public class ServerWorker extends Worker<Server, ServerReceivedEvent> {
    public ServerWorker(Channel channel) {
        super(channel);
    }

    @Override
    protected boolean ping(Channel channel, Server object, short cmd) {
        return false;
    }

    @Override
    protected void disconnection(Server object, Channel channel, String ip) {
        Map<Integer, Map<String, Server>> servers = MemcachedUtil.get(MemcachedCacheVar.ALL_GAME_SERVER);
        if (servers == null || object == null){
            return;
        }
        log.info("服务器【{}】掉线!", object);
        Map<String, Server> areaServers = servers.get(object.getArea());
        Server server = areaServers.get(object.getAddress());
        if (server != null){
            server.setOnline(false);
            MemcachedUtil.set(MemcachedCacheVar.ALL_GAME_SERVER, 0, servers);
        }
        log.info("当前服务器:{}", JSONArray.toJSONString(MemcachedUtil.get(MemcachedCacheVar.ALL_GAME_SERVER)));
    }
}
