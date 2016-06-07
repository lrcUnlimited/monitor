package com.monitor.service.commandrecord;

import java.util.List;

import com.monitor.exception.CodeException;
import com.monitor.model.CommandRecord;
import com.monitor.model.Pager;

public interface CommandService {
	/**
	 * 保存命令
	 * 
	 * @param accountId
	 *            ：操作人id
	 * @param commandRecord
	 * @return
	 * @throws CodeException
	 */
	public boolean saveCommandRecord(int accountId, CommandRecord commandRecord)
			throws CodeException;
	
	/**
	 * 获取命令列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param accountId
	 * @param type 预留，为后面提供类型搜索
	 * @return
	 * @throws CodeException
	 */
	public Pager queryCommandRecord(Integer pageNo, Integer pageSize, Integer accountId,
			Integer type) throws CodeException;
	

}
