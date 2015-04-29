package game.server.handler;

import com.gary.netty.event.Event;
import com.gary.netty.handler.Handler;
import com.gary.netty.net.Cmd;
import game.world.AppCmd;
import org.springframework.stereotype.Component;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/16 10:38
 */
@Component
@Cmd(AppCmd.LOGIN)
public class LoginHandler implements Handler {
    @Override
    public void handle(Event event) throws Exception {

    }
}
