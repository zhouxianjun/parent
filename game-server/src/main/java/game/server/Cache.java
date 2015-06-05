package game.server;

import com.gary.netty.event.HandlerEvent;
import com.gary.netty.handler.Handler;

import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/14 18:29
 */
public class Cache {
    public static Map<Short, HandlerEvent<Handler>> GAME_EVENT_CMD = null;

    class Keys{
        public static final String THREAD_DB = "game:thread:db";
        public static final String THREAD_GAME = "game:thread:game";
        public static final String THREAD_LOG = "game:thread:log";
    }
}
