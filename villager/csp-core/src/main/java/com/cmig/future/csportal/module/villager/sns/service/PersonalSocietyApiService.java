package com.cmig.future.csportal.module.villager.sns.service;

import java.util.List;


import com.cmig.future.csportal.module.villager.sns.dto.PersonalSocietyUser;

public interface PersonalSocietyApiService {
	
	List<PersonalSocietyUser> PersonalSocietyUsers(PersonalSocietyUser personalSocietyUser,int page,int pageSize);

}
