package game.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gary.util.code.RSAUtil;
import com.gary.web.result.ExecuteResult;
import com.gary.web.result.Result;
import com.google.common.collect.Maps;
import game.mapper.player.PlayerMapper;
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
import java.util.Date;
import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/24 11:21
 */
@Slf4j
@Service(version = "1.0")
public class DefaultLoginServiceImpl extends BasicLoginServiceImpl<LoginInfo> {
    @Autowired
    private PlayerMapper userMapper;
    private Md5PasswordEncoder md5 = new Md5PasswordEncoder();
    @Override
    public Result login(LoginInfo loginInfo, String ip, HttpServletRequest request, HttpServletResponse response) {
        Result result = new Result();
        try {
            byte[] data = RSAUtil.decryptBASE64(loginInfo.getPassword());
            String pwd = new String(RSAUtil.decryptByPrivateKey(data));
            Map<String, Object> params = Maps.newHashMap();
            params.put("name", loginInfo.getName());
            Player player = new Player();
            player = userMapper.selectOne(player);
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
            log.warn("{} 登录,密码:{},登录失败!", loginInfo.getName(), loginInfo.getPassword());
            log.warn("登录失败", e);
        }
        return result;
    }

    @Override
    public Result reg(LoginInfo loginInfo, String ip, HttpServletRequest request, HttpServletResponse response) {
        Result result = new Result();
        Player player = new Player();
        player.setChannel(loginInfo.getChannel() == null ? ChannelEnum.DEFAULT : loginInfo.getChannel());
        player.setIp(ip);
        player.setFace(loginInfo.getFace());
        player.setName(loginInfo.getName());
        player.setNickName(loginInfo.getNickName() == null ? loginInfo.getName() : loginInfo.getNickName());
        player.setPassword(md5.encodePassword(loginInfo.getPassword(), loginInfo.getName()));
        player.setPlat(loginInfo.getPlat() == null ? PlatEnum.UNKNOWN : loginInfo.getPlat());
        player.setThirdType(loginInfo.getThirdType() == null ? ThirdTypeEnum.DEFAULT : loginInfo.getThirdType());
        player.setSex(loginInfo.getSex() == null ? SexEnum.UNKNOWN : loginInfo.getSex());
        player.setVersion(loginInfo.getVersion());
        player.setCreateTime(new Date());
        int ret = userMapper.insertSelective(player);
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
