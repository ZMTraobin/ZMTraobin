package com.cmig.future.csportal.module.properties.mgtuser.mapper;

import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MgtUserMapper extends Mapper<MgtUser>{

    void updateUserLoginInfo(MgtUser mgtUser);
    /**
     * 根据手机号查询用户
     * @param dto
     * @return
     */
    public MgtUser getUserByMobile(MgtUser dto);

    /**
     * 根据email查询出用户
     * @param dto
     * @return
     */
    public MgtUser getUserByEmail(MgtUser dto);

    /**
     * 查询所有的物业员用户
     * @return
     */
    public List<MgtUser> selectMgtUser(MgtUser dto);

    /**
     * 根据id查询出详细的用户信息
     */
    public MgtUser getMgtUserById(MgtUser dto);

    /**
     * 批量删除物业用户，也就是软删除,批量更新
     *
     * @param map
     */
    public void updateBathMgtUser(Map<String, Object> map);

}