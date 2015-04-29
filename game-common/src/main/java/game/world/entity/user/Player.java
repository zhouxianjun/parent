package game.world.entity.user;

import com.gary.dao.dto.AbstractTable;
import game.world.enums.ChannelEnum;
import game.world.enums.PlatEnum;
import game.world.enums.SexEnum;
import game.world.enums.ThirdTypeEnum;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description: 玩家
 * @date 2015/4/24 11:47
 */
@Data
@Table(name = "t_player")
public class Player extends AbstractTable implements Serializable {
    @GeneratedValue(generator = "UUID")
    private String id;

    /**玩家名称*/
    @Id
    private String name;

    /**玩家密码*/
    private String password;

    /**昵称*/
    @Column(name = "nickName")
    private String nickName;

    /**注册平台*/
    private PlatEnum plat;

    /**注册渠道*/
    private ChannelEnum channel;

    /**注册第三方平台类型*/
    @Id
    @Column(name = "thirdType")
    private ThirdTypeEnum thirdType;

    /**注册版本*/
    private String version;

    /**注册IP*/
    private String ip;

    /**注册时间*/
    @Column(name = "createTime")
    private Date createTime;

    /**性别*/
    private SexEnum sex;

    /**头像*/
    private String face;

    /**最后登录时间*/
    @Column(name = "lastLoginTime")
    private Date lastLoginTime;
}
