package game.world.dto;

import com.gary.netty.BasicUser;
import com.gary.netty.listeners.UserStateListener;
import game.world.Server;

import java.util.List;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/29 17:56
 */
public class PlayerUser extends BasicUser<Server> {
    @Override
    protected List<? extends UserStateListener> getUserStateListeners() {
        return null;
    }

    public String toString(){
        return this.id;
    }
}
