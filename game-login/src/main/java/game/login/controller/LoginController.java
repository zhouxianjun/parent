package game.login.controller;

import com.gary.web.controller.BaseController;
import com.gary.web.result.Result;
import com.gary.web.spring.bind.annotation.FormModel;
import game.world.dto.LoginInfo;
import game.world.service.LoginService;
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
    private LoginService<LoginInfo> loginService;
    @RequestMapping("login")
    private void login(@FormModel("info") LoginInfo loginInfo, HttpServletRequest request, HttpServletResponse response){

        Result result = loginService.login(loginInfo, getIpAddr(request), request, response);
        result.put("server", loginService.getServer(loginInfo.getVersion(), null));
    }
}
