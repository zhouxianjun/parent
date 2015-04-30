package game.center.handler;

import com.alibaba.fastjson.JSONArray;
import com.gary.netty.event.Event;
import com.gary.netty.handler.Handler;
import com.gary.netty.net.Cmd;
import com.google.common.collect.Maps;
import game.center.WorldManager;
import game.world.AppCmd;
import game.world.Server;
import game.world.protobuf.ServerPro;
import game.world.utils.MemcachedCacheVar;
import game.world.utils.MemcachedUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/17 9:28
 */
@Cmd(AppCmd.CENTER_CONNECT)
@Component
@Slf4j
public class ConnectHandler implements Handler<Server> {
    @Override
    public void handle(Event<Server> event) throws Exception {
        ServerPro.Server server = ServerPro.Server.parseFrom(event.getData());
        Map<Integer, Map<String, Server>> servers = MemcachedUtil.get(MemcachedCacheVar.ALL_GAME_SERVER);
        if (servers == null){
            servers = Maps.newHashMap();
        }
        Map<String, Server> serverMap = servers.get(server.getArea());
        if (serverMap == null){
            serverMap = Maps.newHashMap();
        }
        Server s = new Server(server);
        s.setOnline(true);
        serverMap.put(s.getAddress(), s);
        servers.put(s.getArea(), serverMap);
        MemcachedUtil.set(MemcachedCacheVar.ALL_GAME_SERVER, 0, servers);
        event.getWorker().login(s, WorldManager.getInstance().getExecutorService(Integer.valueOf(s.getAddress().replace(":", "").replace(".", ""))));
        log.info("{} 连上中心服务器!", s);
        log.info("当前服务器:{}", JSONArray.toJSONString(MemcachedUtil.get(MemcachedCacheVar.ALL_GAME_SERVER)));
    }
}
