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

    public static ChannelEnum getByVal(int val, ChannelEnum def){
        for (ChannelEnum eu: values()) {
            if (eu.val == val)
                return eu;
        }
        return def;
    }

    public static ChannelEnum getByVal(int val){
        return getByVal(val, null);
    }
}
