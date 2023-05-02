package com.xiaozhou.backo.common.exception.user;

import com.xiaozhou.backo.common.exception.BaseException;

/**   
 * @Description:   TODO(这里用一句话描述这个类的作用)   
 * @date:          2023年2月13日 下午10:48:17     
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
