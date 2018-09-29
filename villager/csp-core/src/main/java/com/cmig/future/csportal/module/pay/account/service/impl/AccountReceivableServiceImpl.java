package com.cmig.future.csportal.module.pay.account.service.impl;

import com.cmig.future.csportal.module.pay.account.dto.AccountReceivable;
import com.cmig.future.csportal.module.pay.account.mapper.AccountReceivableMapper;
import com.cmig.future.csportal.module.pay.account.service.IAccountReceivableService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountReceivableServiceImpl extends BaseServiceImpl<AccountReceivable> implements IAccountReceivableService {

    @Autowired
    private AccountReceivableMapper accountReceivableMapper;

    @Override
    public AccountReceivable get(AccountReceivable dto) {
        return accountReceivableMapper.get(dto);
    }

    @Override
    public List<AccountReceivable> findList(AccountReceivable dto) {
        return accountReceivableMapper.findList(dto);
    }

    @Override
    public List<AccountReceivable> findList(IRequest requestContext, AccountReceivable dto, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return accountReceivableMapper.findList(dto);
    }

    @Override
    public void batchDeleteByUpdate(List<AccountReceivable> dtoList) {
        for(AccountReceivable dto:dtoList){
            accountReceivableMapper.deleteAccountReceivable(dto);
        }
    }
}