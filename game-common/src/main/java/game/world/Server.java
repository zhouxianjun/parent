package game.world;

import com.google.protobuf.MessageLite;
import game.world.utils.BasicProto;
import game.world.utils.MemcachedCacheVar;
import game.world.utils.MemcachedUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/2 10:09
 */
@Component
@Data
@NoArgsConstructor
public class Server extends BasicProto implements Serializable {
    @Value("#{config_params['game_server_area']}")
    private int area;

    @Value("#{config_params['game_server_probability']}")
    private int probability;

    @Value("#{config_params['game_server_ip']}")
    private String ip;

    @Value("#{config_params['game_server_port']}")
    private int port;

    @Value("#{config_params['game_server_name']}")
    private String name;

    @Value("#{config_params['game_server_version']}")
    private String version;

    @Value("#{config_params['game_server_max']}")
    private int max;

    private int cur;

    private boolean online;

    public Server(MessageLite proto){
        parseProto(proto);
    }

    public String getAddress(){
        return getIp() + ":" + getPort();
    }

    public static Server getServer(String address){
        Map<Integer, Map<String, Server>> servers = MemcachedUtil.get(MemcachedCacheVar.ALL_GAME_SERVER);
        if (servers == null){
            return null;
        }
        Iterator<Map<String, Server>> it = servers.values().iterator();
        while (it.hasNext()){
            Server server = it.next().get(address);
            if (server != null){
                return server;
            }
        }
        return null;
    }

    public String toString(){
        return getAddress();
    }
}
