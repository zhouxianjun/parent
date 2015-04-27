package game.service.impl;

import com.gary.dao.hibernate.DaoUUID;
import com.gary.util.code.RSAUtil;
import com.gary.web.result.ExecuteResult;
import com.gary.web.result.Result;
import com.google.common.collect.Maps;
import game.mapper.player.UserMapper;
import game.world.dto.LoginInfo;
import game.world.entity.user.Player;
import game.world.enums.ChannelEnum;
import game.world.enums.PlatEnum;
import game.world.enums.SexEnum;
import game.world.enums.ThirdTypeEnum;
import game.world.error.GameErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/24 11:21
 */
@Slf4j
public class DefaultLoginServiceImpl extends BasicLoginServiceImpl<LoginInfo> {
    @Autowired
    private UserMapper userMapper;
    private Md5PasswordEncoder md5 = new Md5PasswordEncoder();
    @Override
    public Result login(LoginInfo loginInfo, String ip, HttpServletRequest request, HttpServletResponse response) {
        Result result = new Result();
        try {
            byte[] data = RSAUtil.decryptBASE64(loginInfo.getPassword());
            String pwd = new String(RSAUtil.decryptByPrivateKey(data));
            Map<String, Object> params = Maps.newHashMap();
            params.put("name", loginInfo.getName());
            Player player = userMapper.get(params);
            if (player != null){
                if (!md5.isPasswordValid(player.getPassword(), pwd, loginInfo.getName())){
                    return result.setExecuteResult(new ExecuteResult(GameErrorCode.PASSWORD_VALID_ERROR));
                }
            }else {
                Result regResult = this.reg(loginInfo, ip, request, response);
                if (!regResult.isSuccess()){
                    return regResult;
                }
                player = regResult.get("player");
            }

        } catch (Exception e) {
            log.warn("{} 登录,密码:{},登录失败!", loginInfo.getId(), loginInfo.getPassword());
            log.warn("登录失败", e);
        }
        return result;
    }

    @Override
    public Result reg(LoginInfo loginInfo, String ip, HttpServletRequest request, HttpServletResponse response) {
        Result result = new Result();
        Player player = new Player();
        player.setId(DaoUUID.generate());
        player.setChannel(loginInfo.getChannel() == null ? ChannelEnum.DEFAULT : loginInfo.getChannel());
        player.setIp(ip);
        player.setFace(loginInfo.getFace());
        player.setName(loginInfo.getName());
        player.setNickName(loginInfo.getNickName());
        player.setPassword(loginInfo.getPassword());
        player.setPlat(loginInfo.getPlat() == null ? PlatEnum.UNKNOWN : loginInfo.getPlat());
        player.setThirdType(loginInfo.getThirdType() == null ? ThirdTypeEnum.DEFAULT : loginInfo.getThirdType());
        player.setSex(loginInfo.getSex() == null ? SexEnum.UNKNOWN : loginInfo.getSex());
        player.setVersion(loginInfo.getVersion());
        int ret = userMapper.save(player);
        if (ret <= 0)
            result.setExecuteResult(new ExecuteResult(GameErrorCode.REG_FAIL));
        else
            result.put("player", player);
        return result;
    }

    @Override
    public <T> T validate(String key, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public void logout(String key, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public Player getUser(String key) {
        return null;
    }
}
