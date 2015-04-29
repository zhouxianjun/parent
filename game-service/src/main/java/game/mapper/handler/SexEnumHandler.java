package game.mapper.handler;

import com.gary.dao.mybatis.AbstractEnumTypeHandler;
import game.world.enums.SexEnum;
import game.world.enums.ThirdTypeEnum;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/24 11:45
 */
@MappedTypes({SexEnum.class})
public class SexEnumHandler extends AbstractEnumTypeHandler {
}
