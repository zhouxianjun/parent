package game.world.service;

import game.world.BasicUser;
import game.world.Server;
import game.world.dto.LoginInfo;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:登录接口
 * @date 2015/4/23 9:45
 */
public interface LoginService<O extends LoginInfo> {
    /**
     * 登录
     * @param o
     * @param <T>
     * @return
     */
    <T> T login(O o);

    /**
     * 注册
     * @param o
     * @param <T>
     * @return
     */
    <T> T reg(O o);

    /**
     * 使用key来验证是否已经登录
     * @param key
     * @param <T>
     * @return
     */
    <T> T validate(String key);

    /**
     * 退出
     * @param key
     */
    void logout(String key);

    Server getServer();

    /**
     * 先在缓存取,没有则在数据库取
     * @param key
     * @param <E>
     * @return
     */
    <E extends BasicUser> E getUser(String key);
}
