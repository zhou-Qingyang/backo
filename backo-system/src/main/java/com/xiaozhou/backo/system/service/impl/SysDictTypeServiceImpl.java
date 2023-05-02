package com.xiaozhou.backo.system.service.impl;

import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.xiaozhou.backo.common.constants.UserConstants;
import com.xiaozhou.backo.common.core.domain.SysDictData;
import com.xiaozhou.backo.common.core.domain.SysDictType;
import com.xiaozhou.backo.common.exception.CustomException;
import com.xiaozhou.backo.common.utils.DictUtils;
import com.xiaozhou.backo.common.utils.StringUtils;
import com.xiaozhou.backo.system.mapper.SysDictDataMapper;
import com.xiaozhou.backo.system.mapper.SysDictTypeMapper;
import com.xiaozhou.backo.system.service.ISysDictTypeService;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService {
	@Autowired
	private SysDictTypeMapper dictTypeMapper;

	@Autowired
	private SysDictDataMapper dictDataMapper;

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public List<SysDictType> selectDictTypeList(SysDictType dictType) {
		return dictTypeMapper.selectDictTypeList(dictType);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public List<SysDictType> selectDictTypeAll() {
		  return dictTypeMapper.selectDictTypeAll();
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public List<SysDictData> selectDictDataByType(String dictType) {
		  List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
	        if (StringUtils.isNotEmpty(dictDatas))
	        {
	            return dictDatas;
	        }
	        dictDatas = dictDataMapper.selectDictDataByType(dictType);
	        if (StringUtils.isNotEmpty(dictDatas))
	        {
	            DictUtils.setDictCache(dictType, dictDatas);
	            return dictDatas;
	        }
	        return null;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public SysDictType selectDictTypeById(Long dictId) {
		return dictTypeMapper.selectDictTypeById(dictId);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public SysDictType selectDictTypeByType(String dictType) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int deleteDictTypeByIds(Long[] dictIds) {
		  for (Long dictId : dictIds)
	        {
	            SysDictType dictType = selectDictTypeById(dictId);
	            if (dictDataMapper.countDictDataByType(dictType.getDictType()) > 0)
	            {
	                throw new CustomException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
	            }
	        }
	        int count = dictTypeMapper.deleteDictTypeByIds(dictIds);
	        if (count > 0)
	        {
	            DictUtils.clearDictCache();
	        }
	        return count;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public void clearCache() {  
		DictUtils.clearDictCache();
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public int insertDictType(SysDictType dictType) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	@Transactional
	public int updateDictType(SysDictType dictType) {
		   SysDictType oldDict = dictTypeMapper.selectDictTypeById(dictType.getDictId());
	        dictDataMapper.updateDictDataType(oldDict.getDictType(), dictType.getDictType());
	        int row = dictTypeMapper.updateDictType(dictType);
	        if (row > 0)
	        {
	            DictUtils.clearDictCache();
	        }
	        return row;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param:
	 */
	@Override
	public String checkDictTypeUnique(SysDictType dict) {
		Long dictId = StringUtils.isNull(dict.getDictId()) ? -1L : dict.getDictId();
		SysDictType dictType = dictTypeMapper.checkDictTypeUnique(dict.getDictType());
		if (StringUtils.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue()) {
			return UserConstants.NOT_UNIQUE;
		}
		return UserConstants.UNIQUE;
	}

}
