import com.gary.netty.AbstractClient;
import com.gary.netty.net.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/15 19:01
 */
public class TestClient extends AbstractClient {
    public TestClient(String ip, int port, String name) {
        super(ip, port, name);
    }

    public TestClient(String ip, int port, ChannelHandler handler, String name) {
        super(ip, port, name);
    }

    @Override
    protected void connected(final Channel channel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    channel.writeAndFlush(Packet.createGlobalException());
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        System.out.println("11111");
    }

    @Override
    protected ChannelHandler getDecoderHandler() {
        return null;
    }
}
