package game.service.impl;

import game.world.Server;
import game.world.dto.LoginInfo;
import game.world.service.LoginService;
import game.world.utils.MemcachedCacheVar;
import game.world.utils.MemcachedUtil;

import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/23 16:02
 */
public abstract class BasicLoginServiceImpl<O extends LoginInfo> implements LoginService<O> {
    @Override
    public Server getServer() {
        Map<Integer, Map<String, Server>> servers = MemcachedUtil.get(MemcachedCacheVar.ALL_GAME_SERVER);
        if (servers == null)
            return null;

        return null;
    }
}
