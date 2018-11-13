package com.cmig.future.csportal.module.villager.sns.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmig.future.csportal.module.villager.sns.dto.PersonalSocietyUser;
import com.cmig.future.csportal.module.villager.sns.mapper.PersonalSocietyApiMapper;
import com.cmig.future.csportal.module.villager.sns.service.PersonalSocietyApiService;
import com.github.pagehelper.PageHelper;

@Service
//@Transactional
public class PersonalSocietyApiServiceImpl  implements PersonalSocietyApiService{

	@Autowired
	private PersonalSocietyApiMapper personalSocietyApiMapper;
	@Override
	public List<PersonalSocietyUser> PersonalSocietyUsers(PersonalSocietyUser personalSocietyUser,int page,int pageSize) {
		// TODO Auto-generated method stub
        PageHelper.startPage(page, pageSize);
		return personalSocietyApiMapper.PersonalSocietyUsers(personalSocietyUser);
	}

}
