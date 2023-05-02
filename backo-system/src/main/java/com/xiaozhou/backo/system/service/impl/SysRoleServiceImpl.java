package com.xiaozhou.backo.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaozhou.backo.common.constants.UserConstants;
import com.xiaozhou.backo.common.exception.CustomException;
import com.xiaozhou.backo.common.utils.StringUtils;
import com.xiaozhou.backo.system.domain.SysRole;
import com.xiaozhou.backo.system.domain.SysRoleMenu;
import com.xiaozhou.backo.system.mapper.SysRoleDeptMapper;
import com.xiaozhou.backo.system.mapper.SysRoleMapper;
import com.xiaozhou.backo.system.mapper.SysRoleMenuMapper;
import com.xiaozhou.backo.system.mapper.SysUserRoleMapper;
import com.xiaozhou.backo.system.service.ISysRoleService;

/**
 * 角色 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {
	@Autowired
	private SysRoleMapper roleMapper;

	@Autowired
	private SysRoleMenuMapper roleMenuMapper;

	@Autowired
	private SysUserRoleMapper userRoleMapper;

	@Autowired
	private SysRoleDeptMapper roleDeptMapper;

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public List<SysRole> selectRoleList(SysRole role) {
		return roleMapper.selectRoleList(role);
	}

	/**
	 * 根据用户ID查询权限
	 * 
	 * @param userId
	 *            用户ID
	 * @return 权限列表
	 */
	@Override
	public Set<String> selectRolePermissionByUserId(Long userId) {
		List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
		Set<String> permsSet = new HashSet<>();
		for (SysRole perm : perms) {
			if (StringUtils.isNotNull(perm)) {
				permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
			}
		}
		return permsSet;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public List<SysRole> selectRoleAll() {
		// return SpringUtils.getAopProxy(this).selectRoleList(new SysRole());
		return selectRoleList(new SysRole());
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public List<Integer> selectRoleListByUserId(Long userId) {
		return roleMapper.selectRoleListByUserId(userId);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public SysRole selectRoleById(Long roleId) {
		return roleMapper.selectRoleById(roleId);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public String checkRoleNameUnique(SysRole role) {
		Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
		SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
		if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
			return UserConstants.NOT_UNIQUE;
		}
		return UserConstants.UNIQUE;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public String checkRoleKeyUnique(SysRole role) {
		Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
		SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
		if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
			return UserConstants.NOT_UNIQUE;
		}
		return UserConstants.UNIQUE;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public void checkRoleAllowed(SysRole role) {
		  if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin())
	        {
	            throw new CustomException("不允许操作超级管理员角色");
	        }

	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int countUserRoleByRoleId(Long roleId) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int insertRole(SysRole role) {
		// 新增角色信息
		roleMapper.insertRole(role);
		return insertRoleMenu(role);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int updateRole(SysRole role) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int updateRoleStatus(SysRole role) {
		return roleMapper.updateRole(role);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int authDataScope(SysRole role) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int deleteRoleById(Long roleId) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	@Transactional
	public int deleteRoleByIds(Long[] roleIds) {
		for (Long roleId : roleIds) {
			checkRoleAllowed(new SysRole(roleId));
			SysRole role = selectRoleById(roleId);
			if (countUserRoleByRoleId(roleId) > 0) {
				throw new CustomException(String.format("%1$s已分配,不能删除", role.getRoleName()));
			}
		}
		// 删除角色与菜单关联
		roleMenuMapper.deleteRoleMenu(roleIds);
		// 删除角色与部门关联
		roleDeptMapper.deleteRoleDept(roleIds);
		return roleMapper.deleteRoleByIds(roleIds);
	}

	/**
	 * 新增角色菜单信息
	 * 
	 * @param role
	 *            角色对象
	 */
	public int insertRoleMenu(SysRole role) {
		int rows = 1;
		// 新增用户与角色管理
		List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
		for (Long menuId : role.getMenuIds()) {
			SysRoleMenu rm = new SysRoleMenu();
			rm.setRoleId(role.getRoleId());
			rm.setMenuId(menuId);
			list.add(rm);
		}
		if (list.size() > 0) {
			rows = roleMenuMapper.batchRoleMenu(list);
		}
		return rows;
	}

}
