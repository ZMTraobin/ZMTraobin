package com.cmig.future.csportal.module.properties.mgtuser.service;

import java.util.List;

import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserSyn;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

public interface IMgtUserSynService extends IBaseService<MgtUserSyn>, ProxySelf<IMgtUserSynService>{
    /**
     * 根据sourceId和sourceSystem查询
     * @param mgtUserSyn
     * @return
     */
	public List<MgtUserSyn> findList(MgtUserSyn mgtUserSyn);
    /**
     * 保存同步的物业用户
     * @param mgtUserSyn
     */
	public void save(MgtUserSyn mgtUserSyn);

    /**
     * 根据sourceSystem和sourceId 查询集合
     * @param mgtUserSyn
     * @return
     */
    public List<MgtUserSyn> checkSourceAndSystemId(MgtUserSyn mgtUserSyn);
	
}