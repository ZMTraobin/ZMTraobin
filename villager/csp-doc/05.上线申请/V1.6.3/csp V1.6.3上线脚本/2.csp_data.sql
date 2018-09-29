

-- 补齐新增字段历史数据
update csp_ljh_mgt_user_community set SOURCE_MGT_USER_ID=
(select  SOURCE_SYSTEM_ID from csp_ljh_mgt_user a where a.id=csp_ljh_mgt_user_community.MGT_USER_ID )
where SOURCE_MGT_USER_ID is null ;