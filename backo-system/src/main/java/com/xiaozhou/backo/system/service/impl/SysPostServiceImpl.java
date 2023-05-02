package com.xiaozhou.backo.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaozhou.backo.common.constants.UserConstants;
import com.xiaozhou.backo.common.exception.CustomException;
import com.xiaozhou.backo.common.utils.StringUtils;
import com.xiaozhou.backo.system.domain.SysPost;
import com.xiaozhou.backo.system.mapper.SysPostMapper;
import com.xiaozhou.backo.system.mapper.SysUserPostMapper;
import com.xiaozhou.backo.system.service.ISysPostService;

/**
 * 岗位信息 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysPostServiceImpl implements ISysPostService {
	@Autowired
	private SysPostMapper postMapper;

	@Autowired
	private SysUserPostMapper userPostMapper;

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public List<SysPost> selectPostList(SysPost post) {
		return postMapper.selectPostList(post);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public List<SysPost> selectPostAll() {
		return postMapper.selectPostAll();
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public SysPost selectPostById(Long postId) {
		return postMapper.selectPostById(postId);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public List<Integer> selectPostListByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public String checkPostNameUnique(SysPost post) {
		Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
		SysPost info = postMapper.checkPostNameUnique(post.getPostName());
		if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
			return UserConstants.NOT_UNIQUE;
		}
		return UserConstants.UNIQUE;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public String checkPostCodeUnique(SysPost post) {
		Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
		SysPost info = postMapper.checkPostCodeUnique(post.getPostCode());
		if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
			return UserConstants.NOT_UNIQUE;
		}
		return UserConstants.UNIQUE;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int countUserPostById(Long postId) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int deletePostById(Long postId) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int deletePostByIds(Long[] postIds) {
		for (Long postId : postIds) {
			SysPost post = selectPostById(postId);
			if (countUserPostById(postId) > 0) {
				throw new CustomException(String.format("%1$s已分配,不能删除", post.getPostName()));
			}
		}
		return postMapper.deletePostByIds(postIds);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int insertPost(SysPost post) {
		return postMapper.insertPost(post);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int updatePost(SysPost post) {
		return postMapper.updatePost(post);
	}

}
