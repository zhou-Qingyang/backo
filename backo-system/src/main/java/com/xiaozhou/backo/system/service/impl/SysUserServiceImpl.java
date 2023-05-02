package com.xiaozhou.backo.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaozhou.backo.common.constants.UserConstants;
import com.xiaozhou.backo.common.exception.CustomException;
import com.xiaozhou.backo.common.utils.StringUtils;
import com.xiaozhou.backo.system.domain.SysUser;
import com.xiaozhou.backo.system.domain.SysUserPost;
import com.xiaozhou.backo.system.domain.SysUserRole;
import com.xiaozhou.backo.system.mapper.SysPostMapper;
import com.xiaozhou.backo.system.mapper.SysRoleMapper;
import com.xiaozhou.backo.system.mapper.SysUserMapper;
import com.xiaozhou.backo.system.mapper.SysUserPostMapper;
import com.xiaozhou.backo.system.mapper.SysUserRoleMapper;
import com.xiaozhou.backo.system.service.ISysUserService;


/**
 * 用户 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Resource
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public List<SysUser> selectUserList(SysUser user) {
		 return userMapper.selectUserList(user);
	}

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName)
    {
        return userMapper.selectUserByUserName(userName);
    }
	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public SysUser selectUserById(Long userId) {
		
		return userMapper.selectUserById(userId);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public String selectUserRoleGroup(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public String selectUserPostGroup(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public String checkUserNameUnique(String userName) {
	    int count = userMapper.checkUserNameUnique(userName);
        if (count > 0)
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public String checkPhoneUnique(SysUser user) {
		Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
	
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public String checkEmailUnique(SysUser user) {
		
		 Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
	        SysUser info = userMapper.checkEmailUnique(user.getEmail());
	        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
	        {
	            return UserConstants.NOT_UNIQUE;
	        }
	        return UserConstants.UNIQUE;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public void checkUserAllowed(SysUser user) {
		
		 if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
	        {
	            throw new CustomException("不允许操作超级管理员用户");
	        }
		
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public int insertUser(SysUser user) {
		 // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public int updateUser(SysUser user) {
		  Long userId = user.getUserId();
	        // 删除用户与角色关联
	        userRoleMapper.deleteUserRoleByUserId(userId);
	        // 新增用户与角色管理
	        insertUserRole(user);
	        // 删除用户与岗位关联
	        userPostMapper.deleteUserPostByUserId(userId);
	        // 新增用户与岗位管理
	        insertUserPost(user);
	        return userMapper.updateUser(user);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public int updateUserStatus(SysUser user) {
		 return userMapper.updateUser(user);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public int updateUserProfile(SysUser user) {
		  return userMapper.updateUser(user);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public boolean updateUserAvatar(String userName, String avatar) {
		  return userMapper.updateUserAvatar(userName, avatar) > 0;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public int resetPwd(SysUser user) {
		return userMapper.updateUser(user);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public int resetUserPwd(String userName, String password) {
	       return userMapper.resetUserPwd(userName, password);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
    @Transactional
	public int deleteUserById(Long userId) {
		  // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	@Transactional
	public int deleteUserByIds(Long[] userIds) {
		 for (Long userId : userIds)
	        {
	            checkUserAllowed(new SysUser(userId));
	        }
	        // 删除用户与角色关联
	        userRoleMapper.deleteUserRole(userIds);
	        // 删除用户与岗位关联
	        userPostMapper.deleteUserPost(userIds);
	        return userMapper.deleteUserByIds(userIds);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
		// TODO Auto-generated method stub
		return null;
	}
	

    /**
     * 新增用户角色信息
     * 
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user)
    {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     * 
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts))
        {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts)
            {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0)
            {
                userPostMapper.batchUserPost(list);
            }
        }
    }


}
