package game.world.enums;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/23 10:58
 */
public enum SexEnum {
    UNKNOWN(0, "未知"),
    PRIVATE(1, "保密"),
    MAN(2, "男"),
    WOMEN(3, "女");

    private int val;
    private String name;

    private SexEnum(int val, String name){
        this.val = val;
        this.name = name;
    }

    public SexEnum getByVal(int val){
        for (SexEnum eu: values()) {
            if (eu.val == val)
                return eu;
        }
        return null;
    }
}
