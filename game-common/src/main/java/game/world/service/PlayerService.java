package game.world.service;

import game.world.entity.user.Player;
import game.world.enums.ThirdTypeEnum;

import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:登录接口
 * @date 2015/4/23 9:45
 */
public interface PlayerService {

    /**
     * 根据用户名获取
     * @param name
     * @return
     */
    Player getPlayerByName(String name);

    /**
     * 根据第三方获取用户
     * @param thirdType
     * @param name
     * @return
     */
    Player getThirdPlayerByName(ThirdTypeEnum thirdType, String name);


    /**
     * 注册
     * @param player
     * @param <T>
     * @return
     */
    <T> T reg(Player player, Object... extProp);

}
