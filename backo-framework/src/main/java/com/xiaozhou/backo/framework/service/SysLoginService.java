package com.xiaozhou.backo.framework.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.xiaozhou.backo.common.constants.Constants;
import com.xiaozhou.backo.common.exception.CustomException;
import com.xiaozhou.backo.common.exception.user.CaptchaException;
import com.xiaozhou.backo.common.exception.user.CaptchaExpireException;
import com.xiaozhou.backo.common.exception.user.UserPasswordNotMatchException;
import com.xiaozhou.backo.common.model.LoginUser;
import com.xiaozhou.backo.common.utils.redis.RedisCache;

import lombok.extern.slf4j.Slf4j;





/**   
 * @Description:   TODO(这里用一句话描述这个类的作用)   
 * @date:          2023年2月13日 下午10:15:56     
 */
@Slf4j
@Component
public class SysLoginService {
	
	   	@Autowired
	    private TokenService tokenService;

	    @Resource
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private RedisCache redisCache;
	    
	    /**
	     * 登录验证
	     * 
	     * @param username 用户名
	     * @param password 密码
	     * @param code 验证码
	     * @param uuid 唯一标识
	     * @return 结果
	     */
	    public String login(String username, String password, String code, String uuid)
	    {
	        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
	        String captcha = redisCache.getCacheObject(verifyKey);
	        redisCache.deleteObject(verifyKey);
	        if (captcha == null)
	        {
	            
	            throw new CaptchaExpireException();
	        }
	        if (!code.equalsIgnoreCase(captcha))
	        {
	            //AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
	            throw new CaptchaException();
	        }
	        // 用户验证
	        Authentication authentication = null;
	        try
	        {
	        	log.info("进入 SysLoginService 1");
	            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
	            authentication = authenticationManager
	                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
	        }
	        catch (Exception e)
	        {
	            if (e instanceof BadCredentialsException)
	            {
	                //AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
	                throw new UserPasswordNotMatchException();
	            }
	            else
	            {
	                //AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
	                throw new CustomException(e.getMessage());
	            }
	        }
	        //AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
	        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
	        // 生成token
	        return tokenService.createToken(loginUser);
	    }
	    
	 


}
