package game.mapper.handler;

import com.gary.dao.mybatis.AbstractEnumTypeHandler;
import game.world.enums.ChannelEnum;
import game.world.enums.PlatEnum;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/24 11:45
 */
@MappedTypes({PlatEnum.class})
public class PlatEnumHandler extends AbstractEnumTypeHandler {
}
