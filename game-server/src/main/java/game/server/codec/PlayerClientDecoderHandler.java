package game.server.codec;

import com.gary.netty.codec.AbstractDecoderHandler;
import game.world.dto.PlayerUser;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description: 接收玩家客户端的消息解码
 * @date 2015/4/14 11:40
 */
@Slf4j
public class PlayerClientDecoderHandler extends AbstractDecoderHandler<PlayerWorker, PlayerUser> {
    public PlayerClientDecoderHandler(ChannelGroup channelGroup) {
        super(channelGroup);
    }
}
