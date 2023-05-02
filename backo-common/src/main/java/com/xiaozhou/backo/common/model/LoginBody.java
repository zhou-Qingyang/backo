package com.xiaozhou.backo.common.model;

import lombok.Data;

/**   
 * @Description:   TODO(这里用一句话描述这个类的作用)   
 * @date:          2023年1月22日 下午9:31:46     
 */

@Data
public class LoginBody {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid = "";


}
