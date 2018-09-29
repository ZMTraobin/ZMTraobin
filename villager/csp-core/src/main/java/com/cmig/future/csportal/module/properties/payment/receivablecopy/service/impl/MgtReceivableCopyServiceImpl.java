package com.cmig.future.csportal.module.properties.payment.receivablecopy.service.impl;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.properties.payment.receivablecopy.dto.MgtReceivableCopy;
import com.cmig.future.csportal.module.properties.payment.receivablecopy.mapper.MgtReceivableCopyMapper;
import com.cmig.future.csportal.module.properties.payment.receivablecopy.service.IMgtReceivableCopyService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MgtReceivableCopyServiceImpl extends BaseServiceImpl<MgtReceivableCopy> implements IMgtReceivableCopyService{

    @Autowired
    private MgtReceivableCopyMapper mgtReceivableCopyMapper;

    @Override
    public List<MgtReceivableCopy> queryAll() {

        return mgtReceivableCopyMapper.select(new MgtReceivableCopy());
    }

	@Override
	public void notifySuccess(Map map) {
		String sourceReceivableId=map.get("sourceReceivableId")==null?"":map.get("sourceReceivableId").toString();
		MgtReceivableCopy mgtReceivableCopy= mgtReceivableCopyMapper.selectByPrimaryKey(sourceReceivableId);
		if(null==mgtReceivableCopy){
			throw new DataWarnningException("未找到应收记录， 通知处理失败");
		}
		mgtReceivableCopy.setPaidTime(new Date());
		mgtReceivableCopy.setPayStatus(OrderFormHelper.OREDER_STATUS_Y);
		mgtReceivableCopy.setNotifyFlag("Y");
		mgtReceivableCopyMapper.updateByPrimaryKey(mgtReceivableCopy);
	}
}