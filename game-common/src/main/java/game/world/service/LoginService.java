package game.world.service;

import game.world.Server;
import game.world.dto.LoginInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    <T> T login(O o, String ip, HttpServletRequest request, HttpServletResponse response);

    /**
     * 注册
     * @param o
     * @param <T>
     * @return
     */
    <T> T reg(O o, String ip, HttpServletRequest request, HttpServletResponse response);

    /**
     * 使用key来验证是否已经登录
     * @param key
     * @param <T>
     * @return
     */
    <T> T validate(String key, HttpServletRequest request, HttpServletResponse response);

    /**
     * 退出
     * @param key
     */
    void logout(String key, HttpServletRequest request, HttpServletResponse response);

    Server getServer(String version, Integer area);

    /**
     * 先在缓存取,没有则在数据库取
     * @param key
     * @return
     */
    <T> T getUser(String key);
}
