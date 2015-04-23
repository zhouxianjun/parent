package game.world.enums;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/23 10:54
 */
public enum ChannelEnum {
    DEFAULT(1, "默认");

    private int val;
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
