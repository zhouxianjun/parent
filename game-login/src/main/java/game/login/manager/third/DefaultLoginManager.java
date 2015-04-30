package game.login.manager.third;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gary.util.code.RSAUtil;
import com.gary.web.result.ExecuteResult;
import com.gary.web.result.Result;
import com.google.common.collect.Maps;
import game.login.manager.AbstractLoginManager;
import game.login.manager.LoginManager;
import game.world.dto.LoginInfo;
import game.world.entity.user.Player;
import game.world.enums.ChannelEnum;
import game.world.enums.PlatEnum;
import game.world.enums.SexEnum;
import game.world.enums.ThirdTypeEnum;
import game.world.error.GameErrorCode;
import game.world.service.PlayerService;
import game.world.utils.MemcachedUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:默认的登录处理
 * @date 2015/4/30 15:54
 */
@Slf4j
@Component("DEFAULT")
public class DefaultLoginManager extends AbstractLoginManager<LoginInfo> implements LoginManager<LoginInfo> {

    @Reference(version = "1.0")
    private PlayerService playerService;

    @Override
    public Result login(LoginInfo info, String ip, HttpServletRequest request, HttpServletResponse response) {
        Result result = new Result();
        try {
            byte[] data = RSAUtil.decryptBASE64(info.getPassword());
            String pwd = new String(RSAUtil.decryptByPrivateKey(data));
            Player player = playerService.getThirdPlayerByName(ThirdTypeEnum.DEFAULT, info.getName());
            if (player != null){
                if (!md5.isPasswordValid(player.getPassword(), pwd, info.getName())){
                    return result.setExecuteResult(new ExecuteResult(GameErrorCode.PASSWORD_VALID_ERROR));
                }
            }else {
                info.setPassword(pwd);
                player = this.reg(info, ip, request, response);
                result = playerService.reg(player);
                if (!result.isSuccess()){
                    return result;
                }
            }
            player = playerService.getThirdPlayerByName(ThirdTypeEnum.DEFAULT, info.getName());
            result.put("player", player);
            MemcachedUtil.set(player.getId(), 0, player);
        } catch (Exception e) {
            log.warn("{} 登录,密码:{},登录失败!", info.getName(), info.getPassword());
            log.warn("登录失败", e);
            result.setExecuteResult(new ExecuteResult(GameErrorCode.FAIL));
        }
        return result;
    }

    @Override
    public Player reg(LoginInfo info, String ip, HttpServletRequest request, HttpServletResponse response) {
        Player player = new Player();
        player.setChannel(info.getChannel() == null ? ChannelEnum.DEFAULT : info.getChannel());
        player.setIp(ip);
        player.setFace(info.getFace());
        player.setName(info.getName());
        player.setNickName(info.getNickName() == null ? info.getName() : info.getNickName());
        if (StringUtils.isNotBlank(info.getPassword())) {
            player.setPassword(md5.encodePassword(info.getPassword(), info.getName()));
        }
        player.setPlat(info.getPlat() == null ? PlatEnum.UNKNOWN : info.getPlat());
        player.setThirdType(info.getThirdType() == null ? ThirdTypeEnum.DEFAULT : info.getThirdType());
        player.setSex(info.getSex() == null ? SexEnum.UNKNOWN : info.getSex());
        player.setVersion(info.getVersion());
        player.setCreateTime(new Date());
        return player;
    }

    @Override
    public <T> T validate(String key, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public void logout(String key, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public <T> T getUser(String key) {
        return null;
    }

    public static void main(String[] args) {
        try {
            String pwd = RSAUtil.encryptBASE64(RSAUtil.encryptByPublicKey("123456".getBytes()));
            System.out.printf(pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
