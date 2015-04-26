package game.world.dto;

import com.gary.util.dto.DataMap;
import game.world.enums.ChannelEnum;
import game.world.enums.PlatEnum;
import game.world.enums.SexEnum;
import game.world.enums.ThirdTypeEnum;
import lombok.Data;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/23 10:19
 */
@Data
public class LoginInfo {
    /**平台*/
    private PlatEnum plat;

    /**渠道*/
    private ChannelEnum channel;

    /**
     * 第三方
     */
    private ThirdTypeEnum thirdType;

    private String id;

    private String password;

    /**版本*/
    private String version;

    /**名称*/
    private String name;

    /**昵称*/
    private String nickName;

    /**头像*/
    private String face;

    /**性别*/
    private SexEnum sex;

    /**额外扩展数据*/
    private DataMap<String, Object> data;
}
