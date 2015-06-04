package game.server.handler;

import com.gary.netty.event.Event;
import com.gary.netty.handler.Handler;
import com.gary.netty.net.Cmd;
import com.gary.netty.net.Packet;
import game.world.AppCmd;
import game.world.dto.PlayerUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/16 10:38
 */
@Component
@Cmd(AppCmd.LOGIN)
@Slf4j
public class LoginHandler implements Handler<PlayerUser> {
    @Override
    public void handle(Event<PlayerUser> event) throws Exception {
        log.debug("收到登录请求...");
        event.write(Packet.createGlobalException());
    }
}
