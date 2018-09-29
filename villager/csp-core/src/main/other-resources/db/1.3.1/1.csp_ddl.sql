CREATE TABLE
    csp_ljh_app_user_auth
    (
        AUTH_ID CHAR(32) NOT NULL COMMENT '主键',
        APP_USER_ID CHAR(32) NOT NULL COMMENT '主键',
        IDENTITY_TYPE VARCHAR(50) NOT NULL COMMENT '身份类型',
        UUID VARCHAR(100) NOT NULL COMMENT 'UUID',
        NICK_NAME VARCHAR(100) COLLATE utf8mb4_general_ci COMMENT '昵称',
        AVATAR VARCHAR(500) COMMENT '头像',
        OBJECT_VERSION_NUMBER bigint DEFAULT 1,
        REQUEST_ID bigint DEFAULT -1,
        PROGRAM_ID bigint DEFAULT -1,6
        CREATED_BY bigint DEFAULT -1,
        CREATION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATED_BY bigint DEFAULT -1,
        LAST_UPDATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATE_LOGIN bigint DEFAULT -1,
        PRIMARY KEY (AUTH_ID),
        CONSTRAINT MOBILE UNIQUE (IDENTITY_TYPE,UUID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方登录信息';

DROP TABLE IF EXISTS `csp_app_adv`;
CREATE TABLE `csp_app_adv` (
  `ADV_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `TITLE` varchar(200) DEFAULT NULL COMMENT '标题',
  `DESCRIPTION` varchar(1000) DEFAULT NULL COMMENT '审核意见(描述)',
  `URL_TYPE` varchar(200) DEFAULT NULL COMMENT '链接地址类型 1URL',
  `URL` varchar(200) DEFAULT NULL COMMENT '链接地址点击后去的地方',
  `PIC` varchar(200) DEFAULT NULL COMMENT '图片地址',
  `IS_CAS` bigint(2) NOT NULL DEFAULT '0' COMMENT '是否支持单点登录 1不传TGT 2传TGT不验证  3传TGT并验证',
  `ADV_TYPE` bigint(2) NOT NULL DEFAULT '1' COMMENT '广告类型  1单图 2轮播 3左一右二 4单行三图',
  `STATUS` bigint(2) NOT NULL DEFAULT '1' COMMENT '广告状态,1待审核 2审核失败 3待上线 4已上线 5已下线',
  `ADV_RANK` bigint(2) NOT NULL DEFAULT '0' COMMENT '排序大的在前',
  `APPROVAL_BY` bigint(20) DEFAULT '0' COMMENT '批准用户',
  `APPROVAL_COMMENT` varchar(200) DEFAULT NULL COMMENT '批准注释',
  `ADV_BEGIN` int(11) DEFAULT '0' COMMENT '开始时间',
  `ADV_END` int(11) DEFAULT '0' COMMENT '结束时间',
  `OBJECT_VERSION_NUMBER` bigint(20) DEFAULT '1',
  `REQUEST_ID` bigint(20) DEFAULT '-1',
  `PROGRAM_ID` bigint(20) DEFAULT '-1',
  `CREATED_BY` bigint(20) DEFAULT '-1',
  `CREATION_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` bigint(20) DEFAULT '-1',
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATE_LOGIN` bigint(20) DEFAULT '-1',
  `GROUP_IDENTIFYING` varchar(32) DEFAULT NULL COMMENT '分组辨识',
  `description_one` varchar(50) DEFAULT NULL COMMENT '描述 第一行文字',
  `description_two` varchar(100) DEFAULT NULL COMMENT '描述 第二行文字',
  PRIMARY KEY (`ADV_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='广告主表';

DROP TABLE IF EXISTS `csp_app_adv_sub`;
CREATE TABLE `csp_app_adv_sub` (
  `GROUP_IDENTIFYING_ID` varchar(32) NOT NULL COMMENT '分组辨识ID',
  `GROUP_SORT` bigint(2) NOT NULL DEFAULT '0' COMMENT '分组排序 数字越大越靠前',
  PRIMARY KEY (`GROUP_IDENTIFYING_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广告组排序子表';

-- 是否支持业主联系人管理
alter table csp_ljh_base_community add RESIDENT_MANAGER varchar(1) default 'N';

drop table if exists csp_bp_owner_contacter;

/*==============================================================*/
/* Table: csp_bp_owner_contacter                                */
/*==============================================================*/
create table csp_bp_owner_contacter
(
   OWNER_CONTACTER_ID   bigint not null auto_increment comment '业主主键',
   TYPE                 varchar(50) not null comment '关系分类',
   BP_OWNER_ID          bigint not null comment '业主ID',
   CONTACTER_NAME       varchar(100) not null comment '姓名',
   CONTACTER_NICK_NAME  varchar(100) comment '别名',
   ID_TYPE              varchar(50) comment '证件类型（身份证/护照/营业执照）',
   ID_NO                varchar(100) comment '证件号',
   MOBILE               varchar(20) not null comment '手机号',
   PHOTO                varchar(100) comment '头像/LOGO',
   OBJECT_VERSION_NUMBER bigint(20) default 1 comment '记录版本号',
   REQUEST_ID           bigint(20) default -1 comment '请求ID',
   PROGRAM_ID           bigint(20) default -1 comment '线程ID',
   CREATED_BY           bigint(20) default -1 comment '创建人',
   CREATION_DATE        datetime default CURRENT_TIMESTAMP comment '创建时间',
   LAST_UPDATED_BY      bigint(20) default -1 comment '最后更新人',
   LAST_UPDATE_DATE     datetime default CURRENT_TIMESTAMP comment '最后更新时间',
   LAST_UPDATE_LOGIN    bigint(20) default -1 comment '最后登陆人',
   primary key (OWNER_CONTACTER_ID)
)ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COMMENT='业主联系人信息';

-- 房屋映射表视图
create or replace view csp_v_bp_house_map as
select  g.MOBILE,g.BP_NAME as 'BP_NAME',a.MAP_ID, a.BP_TYPE, a.BP_EXT_ID,a.BUILDING_TYPE, a.BUILDING_ID, a.AUTHENTICATE_STATUS,a.APP_USER_ID,a.EFFECTIVE_START_DATE,a.EFFECTIVE_END_DATE ,a.STATUS from  csp_bp_house_map  a , csp_bp_general g ,csp_bp_owner o
where a.BP_EXT_ID=o.BP_OWNER_ID and o.BP_ID=g.BP_ID
union
select  c.MOBILE,c.CONTACTER_NAME as 'BP_NAME',a.MAP_ID, a.BP_TYPE, a.BP_EXT_ID,a.BUILDING_TYPE, a.BUILDING_ID, a.AUTHENTICATE_STATUS,a.APP_USER_ID,a.EFFECTIVE_START_DATE,a.EFFECTIVE_END_DATE ,a.STATUS from  csp_bp_house_map  a ,  csp_bp_owner_contacter c
where a.BP_EXT_ID=c.OWNER_CONTACTER_ID;

-- 物管云用户同步，增加证件类型，增加证件号码长度
 alter table csp_ljh_mgt_user  add IDENTITY_TYPE VARCHAR(50)  COMMENT '证件类型' after POST ;
 alter table csp_ljh_mgt_user modify IDCARD VARCHAR(100)  COMMENT '证件号码' after  IDENTITY_TYPE;
