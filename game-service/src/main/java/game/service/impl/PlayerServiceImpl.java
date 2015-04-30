package game.service.impl;

import com.gary.web.result.ExecuteResult;
import com.gary.web.result.Result;
import game.mapper.player.PlayerMapper;
import game.world.entity.user.Player;
import game.world.enums.ThirdTypeEnum;
import game.world.error.GameErrorCode;
import game.world.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:玩家服务
 * @date 2015/4/30 16:17
 */
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerMapper playerMapper;

    @Override
    public Player getPlayerByName(String name) {
        return playerMapper.selectByPrimaryKey(name);
    }

    @Override
    public Player getThirdPlayerByName(ThirdTypeEnum thirdType, String name) {
        Player player = new Player();
        player.setThirdType(thirdType);
        player.setName(name);
        return playerMapper.selectOne(player);
    }

    @Override
    public Result reg(Player player, Object... extProp) {
        int ret = playerMapper.insertSelective(player);
        return new Result(new ExecuteResult(ret > 0 ? GameErrorCode.SUCCESS : GameErrorCode.FAIL));
    }
}
