package game.service;

import com.gary.util.code.RSAUtil;
import game.world.dto.LoginInfo;
import game.world.entity.user.Player;
import game.world.service.LoginService;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/29 13:28
 */
@ContextConfiguration(locations = "classpath*:spring*.xml")
public class ServiceTest extends AbstractJUnit4SpringContextTests {

    @Test
    public void login(){
        LoginService loginService = applicationContext.getBean(LoginService.class);
        System.out.println(loginService);
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setName("test");
        loginInfo.setPassword("123456");
        try {
            loginInfo.setPassword(RSAUtil.encryptBASE64(RSAUtil.encryptByPublicKey(loginInfo.getPassword().getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginService.login(loginInfo, "", null, null);
    }
}
