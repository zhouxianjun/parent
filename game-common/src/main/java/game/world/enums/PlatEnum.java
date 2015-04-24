package game.world.enums;

import com.gary.BasicEnum;
import lombok.Getter;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/23 10:22
 */
public enum PlatEnum implements BasicEnum {
    ANDROID(1, "安卓");

    @Getter
    private int val;
    @Getter
    private String name;

    private PlatEnum(int val, String name){
        this.val = val;
        this.name = name;
    }

    public PlatEnum getByVal(int val){
        for (PlatEnum platEnum : values()) {
            if (platEnum.val == val)
                return platEnum;
        }
        return null;
    }
}
