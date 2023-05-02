package com.xiaozhou.backo.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.xiaozhou.backo.common.core.domain.SysDictData;
import com.xiaozhou.backo.common.utils.DictUtils;
import com.xiaozhou.backo.system.mapper.SysDictDataMapper;
import com.xiaozhou.backo.system.service.ISysDictDataService;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService
{

	
	 @Autowired
	 private SysDictDataMapper dictDataMapper;
	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public List<SysDictData> selectDictDataList(SysDictData dictData) {
	    return dictDataMapper.selectDictDataList(dictData);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public String selectDictLabel(String dictType, String dictValue) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public SysDictData selectDictDataById(Long dictCode) {
		  return dictDataMapper.selectDictDataById(dictCode);
	}

	/**
	 * @Description: 重写方法的描述
	 * @param: 
	 */
	@Override
	public int deleteDictDataByIds(Long[] dictCodes) {
		  int row = dictDataMapper.deleteDictDataByIds(dictCodes);
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
	public int insertDictData(SysDictData dictData) {
		int row = dictDataMapper.insertDictData(dictData);
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
	public int updateDictData(SysDictData dictData) {
		  int row = dictDataMapper.updateDictData(dictData);
	        if (row > 0)
	        {
	            DictUtils.clearDictCache();
	        }
	        return row;
	}
}
