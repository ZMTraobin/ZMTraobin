package com.cmig.future.csportal.module.properties.base.contacter.service;

import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.properties.base.contacter.dto.BpOwnerContacter;

import java.util.List;

public interface IBpOwnerContacterService extends IBaseService<BpOwnerContacter>, ProxySelf<IBpOwnerContacterService>{

    List<BpOwnerContacter> getContacterList(String userId, String communityId, Integer page, Integer pageSize);

    BpOwnerContacter getContacterDetail(String userId,Long ownerContacterId,String communityId);

    void saveContacter(BpOwnerContacter contacter) throws ServiceException;

    void deleteContacter(BpOwnerContacter contacter);
}