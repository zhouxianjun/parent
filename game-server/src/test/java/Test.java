import game.server.codec.CenterEncoder;
import game.server.codec.CenterClientDecoderHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/15 11:19
 */
public class Test {
    private static ChannelGroup allChannels = new DefaultChannelGroup(new DefaultEventExecutorGroup(1).next());
    public static void main(String[] args) {

        TestClient client = new TestClient("127.0.0.1", 8586, null);
        client.connect();
    }
}
