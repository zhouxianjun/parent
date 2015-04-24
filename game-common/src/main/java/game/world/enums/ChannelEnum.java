package game.world.enums;

import com.gary.BasicEnum;
import lombok.Getter;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/23 10:54
 */
public enum ChannelEnum implements BasicEnum {
    DEFAULT(1, "默认");

    @Getter
    private int val;
    @Getter
    private String name;

    private ChannelEnum(int val, String name){
        this.val = val;
        this.name = name;
    }

    public ChannelEnum getByVal(int val){
        for (ChannelEnum eu: values()) {
            if (eu.val == val)
                return eu;
        }
        return null;
    }
}
