package com.cmig.future.csportal.module.pay.account.service;

import com.cmig.future.csportal.module.pay.account.dto.AccountReceivable;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IAccountReceivableService extends IBaseService<AccountReceivable>, ProxySelf<IAccountReceivableService>{

    AccountReceivable get(AccountReceivable dto);
    List<AccountReceivable> findList(AccountReceivable dto);

    List<AccountReceivable> findList(IRequest requestContext, AccountReceivable dto, int page, int pageSize);

    void batchDeleteByUpdate(List<AccountReceivable> dto);
}