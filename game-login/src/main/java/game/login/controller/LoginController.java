package game.login.controller;

import com.gary.web.config.ApplicationContextHolder;
import com.gary.web.controller.BaseController;
import com.gary.web.result.Result;
import com.gary.web.spring.bind.annotation.FormModel;
import game.login.manager.LoginManager;
import game.world.dto.LoginInfo;
import game.world.entity.user.Player;
import game.world.enums.ThirdTypeEnum;
import game.world.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/22 17:02
 */
@Controller
@RequestMapping
public class LoginController extends BaseController {
    @RequestMapping("login")
    private void login(@FormModel("info") LoginInfo loginInfo, HttpServletRequest request, HttpServletResponse response, String jsoncallback) throws Exception {

        LoginManager loginManager = getLoginManager(loginInfo.getThirdType());
        Result result = (Result) loginManager.login(loginInfo, getIpAddr(request), request, response);
        if (!result.isSuccess()){
            writer(result, jsoncallback, response);
            return;
        }
        result.put("server", loginManager.getServer(loginInfo.getVersion(), null));
        Player player = result.get("player");
        result.put("session", player.getId());
        writer(result, jsoncallback, response);
    }

    private LoginManager getLoginManager(ThirdTypeEnum thirdType){
        if (thirdType == null)
            thirdType = ThirdTypeEnum.DEFAULT;
        return ApplicationContextHolder.getBean(thirdType.name(), LoginManager.class);
    }
}
