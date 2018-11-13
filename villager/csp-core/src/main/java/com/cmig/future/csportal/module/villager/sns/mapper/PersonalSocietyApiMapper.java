package com.cmig.future.csportal.module.villager.sns.mapper;

import java.util.List;

import com.cmig.future.csportal.module.villager.sns.dto.PersonalSocietyUser;

public interface PersonalSocietyApiMapper {
	
	List<PersonalSocietyUser> PersonalSocietyUsers(PersonalSocietyUser personalSocietyUser);

}
