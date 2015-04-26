package game.world.error;

import com.gary.error.ErrorCode;
import com.gary.error.ErrorMsg;

/**
 * Created by Gary on 2015/4/26.
 */
public class GameErrorCode implements ErrorCode {
    @ErrorMsg("密码验证错误")
    public static final int PASSWORD_VALID_ERROR = 1001;

    @ErrorMsg("注册失败")
    public static final int REG_FAIL = 1002;
}
