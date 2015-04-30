package game.login.manager;

import com.google.common.collect.Maps;
import game.world.Server;
import game.world.dto.LoginInfo;
import game.world.utils.MemcachedCacheVar;
import game.world.utils.MemcachedUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/30 15:55
 */
public abstract class AbstractLoginManager<I extends LoginInfo> implements LoginManager<I> {
    protected Md5PasswordEncoder md5 = new Md5PasswordEncoder();
    @Override
    public Server getServer(String version, Integer area) {
        Map<Integer, Map<String, Server>> servers = MemcachedUtil.get(MemcachedCacheVar.ALL_GAME_SERVER);
        if (servers == null)
            return null;

        Map<String, Server> serverMap = null;
        //获取该大区的所有服务器
        if (area != null){
            serverMap = servers.get(area);
        }
        if (serverMap == null)
            serverMap = Maps.newHashMap();

        //获取制定版本的服务器
        if (StringUtils.isNotBlank(version)) {
            serverMap.putAll(getForVersion(version, servers));
        }

        //如果服务器为空则取所有服务器
        if (serverMap == null || serverMap.isEmpty() || serverMap.size() <= 0){
            serverMap = getAllServer(servers);
        }

        //把服务器已满的剔除
        Server min = delMaxHoldMin(serverMap);

        //如果服务器为空则返回一个 相对最少人数的服务器
        if (serverMap.isEmpty()){
            return min;
        }

        return selectProbability(serverMap.values());
    }

    /**
     * 获取知道版本的服务器
     * @param version
     * @param servers
     * @return
     */
    protected Map<String, Server> getForVersion(String version, Map<Integer, Map<String, Server>> servers) {
        Map<String, Server> serverMap = Maps.newHashMap();
        Iterator<Map.Entry<Integer, Map<String, Server>>> it = servers.entrySet().iterator();
        while (it.hasNext()) {
            Map<String, Server> value = it.next().getValue();
            Iterator<Map.Entry<String, Server>> valueIt = value.entrySet().iterator();
            while (valueIt.hasNext()) {
                Server server = valueIt.next().getValue();
                if (StringUtils.isNotBlank(server.getVersion()) && server.getVersion().equals(version) && !serverMap.containsKey(server.getAddress())) {
                    serverMap.put(server.getAddress(), server);
                }
            }
        }
        return serverMap;
    }

    /**
     * 获取所有服务器
     * @param servers
     * @return
     */
    protected Map<String, Server> getAllServer(Map<Integer, Map<String, Server>> servers){
        Map<String, Server> serverMap = Maps.newHashMap();
        Iterator<Map.Entry<Integer, Map<String, Server>>> it = servers.entrySet().iterator();
        while (it.hasNext()) {
            serverMap.putAll(it.next().getValue());
        }
        return serverMap;
    }

    /**
     * 删除已满服务器,并且保留已满的最小的服务器
     * @param serverMap
     * @return
     */
    protected Server delMaxHoldMin(Map<String, Server> serverMap){
        Iterator<Map.Entry<String, Server>> serverIt = serverMap.entrySet().iterator();
        Server min = null;
        while (serverIt.hasNext()){
            Server server = serverIt.next().getValue();
            if (server.getCur() >= server.getMax() || !server.isOnline()){
                if ((min == null || server.getCur() < min.getCur()) && server.isOnline()) {
                    min = server;
                }
                serverIt.remove();
            }
        }
        return min;
    }

    /**
     * 根据概率赛选服务器
     * @param serverList
     * @return
     */
    protected Server selectProbability(Collection<Server> serverList){
        int randomNumber = (int) (Math.random() * total(serverList));
        int priority = 0;
        for (Server server : serverList) {
            priority += server.getProbability();
            if (priority > randomNumber) {
                return server;
            }
        }
        return selectProbability(serverList);
    }

    /**
     * 计算总优先级
     * @param serverList
     * @return
     */
    private int total(Collection<Server> serverList) {
        int result = 0;
        for (Server server : serverList) {
            result += server.getProbability();
        }
        return result;
    }
}
