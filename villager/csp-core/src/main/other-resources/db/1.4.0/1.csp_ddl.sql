
-- 业主与app用户关系视图
create or replace view csp_v_app_user_bp  as
select  g.BP_ID,a.ID as APP_USER_ID
from csp_bp_general g , csp_ljh_app_user a where a.MOBILE=g.MOBILE;

-- 业主联系人信息视图
create or replace view csp_v_app_user_contacter  as
select  vau.BP_ID,vau.APP_USER_ID,
c.OWNER_CONTACTER_ID,
c.BP_OWNER_ID,
c.TYPE,
c.CONTACTER_NAME,
c.CONTACTER_NICK_NAME,
c.ID_TYPE,
c.ID_NO,
c.MOBILE,
c.PHOTO
from csp_bp_owner_contacter c , csp_bp_owner o,  csp_v_app_user_bp vau
where c.BP_OWNER_ID=o.BP_OWNER_ID and o.BP_ID =vau.BP_ID;