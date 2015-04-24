package game.service.impl;

import game.world.BasicUser;
import game.world.dto.LoginInfo;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/24 11:21
 */
public class DefaultLoginServiceImpl extends BasicLoginServiceImpl<LoginInfo> {
    @Override
    public <T> T login(LoginInfo loginInfo) {
        return null;
    }

    @Override
    public <T> T reg(LoginInfo loginInfo) {
        return null;
    }

    @Override
    public <T> T validate(String key) {
        return null;
    }

    @Override
    public void logout(String key) {

    }

    @Override
    public BasicUser getUser(String key) {
        return null;
    }
}
