package game.world.enums;

import com.gary.BasicEnum;
import lombok.Getter;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/23 10:58
 */
public enum SexEnum implements BasicEnum {
    UNKNOWN(0, "未知"),
    PRIVATE(1, "保密"),
    MAN(2, "男"),
    WOMEN(3, "女");

    @Getter
    private int val;
    @Getter
    private String name;

    private SexEnum(int val, String name){
        this.val = val;
        this.name = name;
    }

    public static SexEnum getByVal(int val, SexEnum def){
        for (SexEnum eu: values()) {
            if (eu.val == val)
                return eu;
        }
        return def;
    }

    public static SexEnum getByVal(int val){
        return getByVal(val, null);
    }
}
