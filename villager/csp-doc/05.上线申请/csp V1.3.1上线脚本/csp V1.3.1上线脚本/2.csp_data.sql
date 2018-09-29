
-- 历史身份证号，补充证件类型字段值
update csp_ljh_mgt_user set IDENTITY_TYPE='IDENTIFICATION_CARDS' where IDENTITY_TYPE is null ;

