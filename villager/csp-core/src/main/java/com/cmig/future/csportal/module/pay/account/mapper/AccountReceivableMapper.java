package com.cmig.future.csportal.module.pay.account.mapper;

import com.cmig.future.csportal.module.pay.account.dto.AccountReceivable;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface AccountReceivableMapper extends Mapper<AccountReceivable>{

    List<AccountReceivable> findList(AccountReceivable dto);

    AccountReceivable get(AccountReceivable dto);

    int deleteAccountReceivable(AccountReceivable dto);
}