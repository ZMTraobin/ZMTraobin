

-- 增加注释
alter table csp_ljh_app_user MODIFY RET_TIME DATETIME COMMENT '注册时间'  ;
alter table csp_ljh_app_user MODIFY NICK_NAME VARCHAR(1000) COLLATE utf8mb4_general_ci COMMENT '昵称'  ;

-- 订单表增加字段
  alter table csp_order_form add DISCOUNT_AMOUNT bigint DEFAULT 0  NOT NULL COMMENT '折扣金额 (单位为分)';
  alter table csp_order_form add INTEGRAL_AMOUNT bigint DEFAULT 0 NOT NULL COMMENT '积分抵扣 (单位为分)';
  alter table csp_order_form add PAYABLE_AMOUNT bigint DEFAULT 0 NOT NULL COMMENT '应付金额 (单位为分)';

-- 日志记录增加字段大小
alter table csp_ljh_sys_log  modify   METHOD VARCHAR(15) COMMENT '操作方式' ;

drop table if exists csp_mgt_structure_version;

create table csp_mgt_structure_version
(
   VERSION_ID           bigint not null auto_increment comment '主键',
   COMMUNITY_ID         varchar(50) not null comment '项目ID',
   VERSION_NO           varchar(50) not null comment '版本编号',
   VERSION_NAME         varchar(100) not null comment '版本名称',
   STATUS               varchar(20) not null default 'N' comment '是否启用（Y/N)',
   IS_DEFAULT           varchar(20) default 'Y' comment '是否默认版本（Y/N)',
   OBJECT_VERSION_NUMBER bigint(20) default 1 comment '记录版本号',
   REQUEST_ID           bigint(20) default -1 comment '请求ID',
   PROGRAM_ID           bigint(20) default -1 comment '线程ID',
   CREATED_BY           bigint(20) default -1 comment '创建人',
   CREATION_DATE        datetime default CURRENT_TIMESTAMP comment '创建时间',
   LAST_UPDATED_BY      bigint(20) default -1 comment '最后更新人',
   LAST_UPDATE_DATE     datetime default CURRENT_TIMESTAMP comment '最后更新时间',
   LAST_UPDATE_LOGIN    bigint(20) default -1 comment '最后登陆人',
   primary key (VERSION_ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='建筑结构版本';

drop table if exists csp_mgt_structure;

create table csp_mgt_structure
(
   STRUCTURE_ID         bigint not null auto_increment comment '主键',
   VERSION_ID           bigint not null comment '建筑结构版本ID',
   PARENT_STRUCTURE_ID  bigint not null comment '父结构ID',
   COMMUNITY_ID         varchar(50) not null comment '项目ID',
   STRUCTURE_CODE       varchar(50) not null comment '建筑编号',
   STRUCTURE_NAME       varchar(100) not null comment '建筑名称',
   STRUCTURE_NICK_NAME  varchar(50) comment '建筑别名',
   STRUCTURE_FULL_NAME  varchar(100) comment '建筑全称',
   STRUCTURE_TYPE       varchar(50) comment '建筑结构分类',
   BUSINESS_TYPE        varchar(50) comment '业态',
   STATUS               varchar(20) default 'Y' comment '是否启用（Y/N)',
   SOURCE_STRUCTURE_CODE varchar(50) comment '源系统编码',
   OBJECT_VERSION_NUMBER bigint(20) default 1 comment '记录版本号',
   REQUEST_ID           bigint(20) default -1 comment '请求ID',
   PROGRAM_ID           bigint(20) default -1 comment '线程ID',
   CREATED_BY           bigint(20) default -1 comment '创建人',
   CREATION_DATE        datetime default CURRENT_TIMESTAMP comment '创建时间',
   LAST_UPDATED_BY      bigint(20) default -1 comment '最后更新人',
   LAST_UPDATE_DATE     datetime default CURRENT_TIMESTAMP comment '最后更新时间',
   LAST_UPDATE_LOGIN    bigint(20) default -1 comment '最后登陆人',
   primary key (STRUCTURE_ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='建筑结构表';

alter table csp_mgt_structure add unique key `unq_STRUCTURE_CODE` (STRUCTURE_CODE);

drop table if exists csp_mgt_house;

create table csp_mgt_house
(
   HOUSE_ID             bigint not null auto_increment comment '主键',
   HOUSE_CODE           varchar(50) not null comment '房屋编号',
   COMMUNITY_ID         varchar(50) not null comment '项目ID',
   HOUSE_NAME           varchar(100) not null comment '房屋名称',
   HOUSE_NICK_NAME      varchar(50) comment '房屋别名',
   HOUSE_FULL_NAME      varchar(100) comment '房屋全称',
   USE_TYPE             varchar(50) comment '用途分类（住宅、商业、厂房、仓库、办公）',
   BUILDING_AREA        decimal(18,2) default 0 comment '建筑面积',
   PAYMENT_AREA         decimal(18,2) default 0 comment '收费面积',
   DECORATION_STATUS    varchar(50) comment '装修情况',
   STATUS               varchar(20) default 'Y' comment '是否启用（Y/N)',
   SOURCE_HOUSE_CODE    varchar(50) comment '源系统编码',
   OBJECT_VERSION_NUMBER bigint(20) default 1 comment '记录版本号',
   REQUEST_ID           bigint(20) default -1 comment '请求ID',
   PROGRAM_ID           bigint(20) default -1 comment '线程ID',
   CREATED_BY           bigint(20) default -1 comment '创建人',
   CREATION_DATE        datetime default CURRENT_TIMESTAMP comment '创建时间',
   LAST_UPDATED_BY      bigint(20) default -1 comment '最后更新人',
   LAST_UPDATE_DATE     datetime default CURRENT_TIMESTAMP comment '最后更新时间',
   LAST_UPDATE_LOGIN    bigint(20) default -1 comment '最后登陆人',
   primary key (HOUSE_ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房屋信息';

alter table csp_mgt_house add unique key `unq_HOUSE_CODE` (HOUSE_CODE);

drop table if exists csp_structure_building_map;

create table csp_structure_building_map
(
   MAP_ID               bigint not null auto_increment comment '主键',
   BUILDING_TYPE        varchar(20) not null comment '建筑类别（房屋、车位、商铺)',
   STRUCTURE_ID         bigint not null comment '建筑结构ID',
   BUILDING_ID          bigint not null comment '建筑实体ID（房屋、车位、商铺)',
   STATUS               varchar(20) default 'Y' comment '是否有效（Y/N）',
   OBJECT_VERSION_NUMBER bigint(20) default 1 comment '记录版本号',
   REQUEST_ID           bigint(20) default -1 comment '请求ID',
   PROGRAM_ID           bigint(20) default -1 comment '线程ID',
   CREATED_BY           bigint(20) default -1 comment '创建人',
   CREATION_DATE        datetime default CURRENT_TIMESTAMP comment '创建时间',
   LAST_UPDATED_BY      bigint(20) default -1 comment '最后更新人',
   LAST_UPDATE_DATE     datetime default CURRENT_TIMESTAMP comment '最后更新时间',
   LAST_UPDATE_LOGIN    bigint(20) default -1 comment '最后登陆人',
	 primary key (MAP_ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='建筑实体与结构关系表';




DROP VIEW IF EXISTS csp_mgt_v_structure_house_view;

CREATE or replace VIEW `csp_mgt_v_structure_house_view`
AS
(SELECT DISTINCT v.VERSION_ID AS `VERSION_ID`,v.COMMUNITY_ID AS COMMUNITY_ID,
CONCAT('a',s.STRUCTURE_ID) AS VIEW_ID,s.PARENT_STRUCTURE_ID AS PARENT_STRUCTURE_ID,
s.STRUCTURE_CODE AS VIEW_CODE,s.STRUCTURE_NAME AS NAME,s.STRUCTURE_NICK_NAME AS NICK_NAME,
s.STRUCTURE_FULL_NAME AS FULL_NAME,
(SELECT s1.STRUCTURE_NAME FROM `csp_mgt_structure` `s1` WHERE s1.STRUCTURE_ID = s.PARENT_STRUCTURE_ID) AS P_NAME,
s.BUSINESS_TYPE AS TYPE,s.STATUS AS STATUS,s.SOURCE_STRUCTURE_CODE AS SOURCE_CODE,"N" AS IS_BUILDING
FROM `csp_mgt_structure_version` `v` LEFT JOIN `csp_mgt_structure` `s` ON v.VERSION_ID = s.VERSION_ID
LEFT JOIN `csp_structure_building_map` `m` ON m.STRUCTURE_ID = s.STRUCTURE_ID
LEFT JOIN `csp_mgt_house` `h` ON h.HOUSE_ID = m.BUILDING_ID
ORDER BY v.VERSION_ID,s.STRUCTURE_ID ASC)
UNION
(SELECT DISTINCT v.VERSION_ID AS `VERSION_ID`,v.COMMUNITY_ID AS COMMUNITY_ID,
CONCAT('b',h.HOUSE_ID) AS VIEW_ID,m.STRUCTURE_ID AS PARENT_STRUCTURE_ID,
h.HOUSE_CODE AS VIEW_CODE,h.HOUSE_NAME AS NAME,h.HOUSE_NICK_NAME AS NICK_NAME,
h.HOUSE_FULL_NAME AS FULL_NAME,
(SELECT s1.STRUCTURE_NAME FROM `csp_mgt_structure` `s1` WHERE s1.STRUCTURE_ID = m.STRUCTURE_ID) AS P_NAME,
h.USE_TYPE AS TYPE,h.STATUS AS STATUS,h.SOURCE_HOUSE_CODE AS SOURCE_CODE,"Y" AS IS_BUILDING
FROM `csp_mgt_structure_version` `v` LEFT JOIN `csp_mgt_structure` `s` ON v.VERSION_ID = s.VERSION_ID
LEFT JOIN `csp_structure_building_map` `m` ON m.STRUCTURE_ID = s.STRUCTURE_ID
LEFT JOIN `csp_mgt_house` `h` ON h.HOUSE_ID = m.BUILDING_ID
WHERE h.HOUSE_ID IS NOT NULL AND h.`STATUS` = "Y"
ORDER BY v.VERSION_ID,h.HOUSE_ID ASC);

/*项目编码增加唯一约束*/
alter table csp_ljh_base_community add unique key `unq_COMMUNITY_CODE` (COMMUNITY_CODE);


drop table if exists csp_mgt_receivable_detail;

create table csp_mgt_receivable_detail
(
   RECEIVABLE_ID        bigint not null auto_increment comment '应收主键',
   BUILDING_TYPE        varchar(20) not null comment '建筑实体类别（房屋、车位、商铺)',
   BUILDING_ID          bigint not null comment '建筑实体ID（房屋、车位、商铺)',
   EXPENDITURE          varchar(50) not null comment '费项（租金、物业费、卫生费）',
   START_DATE           datetime comment '开始日期',
   END_DATE             datetime comment '结束日期',
   PERIOD_NAME          varchar(50) not null comment '期间（YYYY-MM）',
   PRICE_AMOUT          bigint default 0 comment '单价',
   AREA                 decimal(18,2) default 0 comment '收费面积',
   TOTAL_AMOUNT         bigint not null default 0 comment '总金额',
   DISCOUNT_AMOUNT      bigint not null default 0 comment '折扣金额',
   BREAK_CONTRACT_AMOUNT bigint not null default 0 comment '违约金',
   RECEIVABLE_AMOUNT    bigint not null default 0 comment '应收总额',
   BUILD_TIME           datetime not null default CURRENT_TIMESTAMP comment '应收生成时间',
   PAID_TIME            datetime comment '付款完成时间',
   BACK_URL             varchar(255) comment '通知商户的服务器端地址',
   DESCRIPTION          varchar(255) comment '附加说明',
   NOTIFY_FLAG          varchar(20) not null default 'N' comment '是否通知商户（N未通知；Y已通知）',
   CHECK_FLAG           varchar(20) not null default 'N' comment '是否已清算（N未清算；Y已清算）',
   PAY_STATUS           varchar(20) not null default 'N' comment '支付状态（YPN）',
   ENABLE_FLAG          varchar(20) not null default 'Y' comment '是否有效（Y有效；N无效）',
   APP_ID               varchar(50) comment 'csp APP_ID',
   SOURCE_SYSTEM        varchar(50) comment '源系统代码',
   SOURCE_RECEIVABLE_ID varchar(50) comment '源系统应收ID',
   OBJECT_VERSION_NUMBER bigint(20) default 1 comment '记录版本号',
   REQUEST_ID           bigint(20) default -1 comment '请求ID',
   PROGRAM_ID           bigint(20) default -1 comment '线程ID',
   CREATED_BY           bigint(20) default -1 comment '创建人',
   CREATION_DATE        datetime default CURRENT_TIMESTAMP comment '创建时间',
   LAST_UPDATED_BY      bigint(20) default -1 comment '最后更新人',
   LAST_UPDATE_DATE     datetime default CURRENT_TIMESTAMP comment '最后更新时间',
   LAST_UPDATE_LOGIN    bigint(20) default -1 comment '最后登陆人',
   primary key (RECEIVABLE_ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应收明细';

alter table csp_mgt_receivable_detail add unique key `csp_mgt_receivable_detail_u1` (SOURCE_SYSTEM,SOURCE_RECEIVABLE_ID);


drop table if exists csp_bp_general;

CREATE TABLE `csp_bp_general` (
  `BP_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'BP主键',
  `BP_CODE` varchar(50) DEFAULT NULL COMMENT 'BP编号',
  `BP_NAME` varchar(100) NOT NULL COMMENT 'BP名称',
  `BP_NICK_NAME` varchar(100) DEFAULT NULL COMMENT 'BP别名',
  `ID_TYPE` varchar(50) NOT NULL COMMENT '证件类型（身份证/护照）',
  `ID_NO` varchar(100) NOT NULL COMMENT '证件号',
  `PHOTO` varchar(100) DEFAULT NULL COMMENT '头像/LOGO',
  `MOBILE` varchar(20) DEFAULT NULL COMMENT '手机号',
  `PHONE` char(20) DEFAULT NULL COMMENT '联系电话',
  `EMAIL` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `GENDER` char(2) DEFAULT NULL COMMENT '性别（F/M)',
  `BIRTHDAY` datetime DEFAULT NULL COMMENT '生日',
  `AGE` bigint(10) DEFAULT NULL COMMENT '年龄',
  `OBJECT_VERSION_NUMBER` bigint(20) DEFAULT '1' COMMENT '记录版本号',
  `REQUEST_ID` bigint(20) DEFAULT '-1' COMMENT '请求ID',
  `PROGRAM_ID` bigint(20) DEFAULT '-1' COMMENT '线程ID',
  `CREATED_BY` bigint(20) DEFAULT '-1' COMMENT '创建人',
  `CREATION_DATE` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LAST_UPDATED_BY` bigint(20) DEFAULT '-1' COMMENT '最后更新人',
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `LAST_UPDATE_LOGIN` bigint(20) DEFAULT '-1' COMMENT '最后登陆人',
  PRIMARY KEY (`BP_ID`),
  UNIQUE KEY `id_card` (`ID_NO`) USING BTREE,
  UNIQUE KEY `code` (`BP_CODE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10100 DEFAULT CHARSET=utf8 COMMENT='业务伙伴基本表';

drop table if exists csp_bp_owner;

CREATE TABLE `csp_bp_owner` (
  `BP_OWNER_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '业主主键',
  `BP_ID` bigint(20) NOT NULL COMMENT 'BP_ID',
  `OBJECT_VERSION_NUMBER` bigint(20) DEFAULT '1' COMMENT '记录版本号',
  `REQUEST_ID` bigint(20) DEFAULT '-1' COMMENT '请求ID',
  `PROGRAM_ID` bigint(20) DEFAULT '-1' COMMENT '线程ID',
  `CREATED_BY` bigint(20) DEFAULT '-1' COMMENT '创建人',
  `CREATION_DATE` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LAST_UPDATED_BY` bigint(20) DEFAULT '-1' COMMENT '最后更新人',
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `LAST_UPDATE_LOGIN` bigint(20) DEFAULT '-1' COMMENT '最后登陆人',
  PRIMARY KEY (`BP_OWNER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4338 DEFAULT CHARSET=utf8 COMMENT='业主信息';

drop table if exists csp_bp_house_map;

CREATE TABLE `csp_bp_house_map` (
  `MAP_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `APP_USER_ID` varchar(50) DEFAULT NULL,
  `BP_TYPE` varchar(20) NOT NULL COMMENT 'BP类别（业主、租户、商户)',
  `BP_EXT_ID` bigint(20) NOT NULL COMMENT 'BP扩展表主键（业主、租户、商户ID)',
  `BUILDING_TYPE` varchar(20) NOT NULL COMMENT '建筑实体类别（房屋、车位、商铺)',
  `BUILDING_ID` bigint(20) NOT NULL COMMENT '建筑实体ID（房屋、车位、商铺)',
  `EFFECTIVE_START_DATE` datetime NOT NULL COMMENT '生效开始日期',
  `EFFECTIVE_END_DATE` datetime NOT NULL COMMENT '生效结束日期',
  `STATUS` varchar(20) DEFAULT 'Y' COMMENT '是否有效（Y/N）',
  `AUTHENTICATE_STATUS` varchar(20) DEFAULT 'N' COMMENT '认证状态（Y/N）',
  `OBJECT_VERSION_NUMBER` bigint(20) DEFAULT '1' COMMENT '记录版本号',
  `REQUEST_ID` bigint(20) DEFAULT '-1' COMMENT '请求ID',
  `PROGRAM_ID` bigint(20) DEFAULT '-1' COMMENT '线程ID',
  `CREATED_BY` bigint(20) DEFAULT '-1' COMMENT '创建人',
  `CREATION_DATE` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LAST_UPDATED_BY` bigint(20) DEFAULT '-1' COMMENT '最后更新人',
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `LAST_UPDATE_LOGIN` bigint(20) DEFAULT '-1' COMMENT '最后登陆人',
  PRIMARY KEY (`MAP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=503 DEFAULT CHARSET=utf8 COMMENT='房屋客户关系表';


 drop table if exists csp_order_form_line;
create table csp_order_form_line
(
   LINE_ID              bigint not null auto_increment comment '主键',
   ORDER_ID             bigint not null comment '头表ID',
   RECEIVABLE_ID        bigint not null comment '应收ID',
   OBJECT_VERSION_NUMBER bigint(20) default 1 comment '记录版本号',
   REQUEST_ID           bigint(20) default -1 comment '请求ID',
   PROGRAM_ID           bigint(20) default -1 comment '线程ID',
   CREATED_BY           bigint(20) default -1 comment '创建人',
   CREATION_DATE        datetime default CURRENT_TIMESTAMP comment '创建时间',
   LAST_UPDATED_BY      bigint(20) default -1 comment '最后更新人',
   LAST_UPDATE_DATE     datetime default CURRENT_TIMESTAMP comment '最后更新时间',
   LAST_UPDATE_LOGIN    bigint(20) default -1 comment '最后登陆人',
   primary key (LINE_ID)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='订单行表';

/* 新建房屋节点选择视图 */
CREATE OR REPLACE VIEW csp_v_structure as
SELECT
s.STRUCTURE_ID as VIEW_ID,
s.PARENT_STRUCTURE_ID as PARENT_VIEW_ID,
v.VERSION_ID,
v.COMMUNITY_ID,
v.VERSION_NO,
v.VERSION_NAME,
v.IS_DEFAULT,
c.COMMUNITY_CODE,
c.COMMUNITY_NAME,
c.COMPANY_ID,
c.COMPANY_NAME,
s.STRUCTURE_ID,
s.PARENT_STRUCTURE_ID,
s.STRUCTURE_CODE,
s.STRUCTURE_NAME,
s.STRUCTURE_NICK_NAME,
s.STRUCTURE_FULL_NAME,
s.STRUCTURE_TYPE,
null as HOUSE_ID,
null as HOUSE_CODE,
null as HOUSE_NAME,
null as HOUSE_NICK_NAME,
null as HOUSE_FULL_NAME,
null as USE_TYPE,
null as BUILDING_AREA,
null as PAYMENT_AREA,
null as DECORATION_STATUS,
null as SOURCE_HOUSE_CODE
from csp_mgt_structure_version v
left join csp_ljh_base_community c on v.COMMUNITY_ID=c.ID
left join csp_mgt_structure s on v.VERSION_ID=s.VERSION_ID
where  v.STATUS='Y' and s.STATUS='Y'
 union all
  select
h.HOUSE_ID as VIEW_ID,
s.STRUCTURE_ID as PARENT_VIEW_ID,
v.VERSION_ID,
v.COMMUNITY_ID,
v.VERSION_NO,
v.VERSION_NAME,
v.IS_DEFAULT,
c.COMMUNITY_CODE,
c.COMMUNITY_NAME,
c.COMPANY_ID,
c.COMPANY_NAME,
s.STRUCTURE_ID,
s.PARENT_STRUCTURE_ID,
s.STRUCTURE_CODE,
s.STRUCTURE_NAME,
s.STRUCTURE_NICK_NAME,
s.STRUCTURE_FULL_NAME,
s.STRUCTURE_TYPE,
h.HOUSE_ID,
h.HOUSE_CODE,
h.HOUSE_NAME,
h.HOUSE_NICK_NAME,
h.HOUSE_FULL_NAME,
h.USE_TYPE,
h.BUILDING_AREA,
h.PAYMENT_AREA,
h.DECORATION_STATUS,
h.SOURCE_HOUSE_CODE
 from csp_mgt_structure_version v
 left join csp_ljh_base_community c on v.COMMUNITY_ID=c.ID
 left join csp_mgt_structure s on v.VERSION_ID=s.VERSION_ID
 left join csp_structure_building_map m on s.STRUCTURE_ID=m.STRUCTURE_ID
 left join csp_mgt_house h on m.BUILDING_ID=h.HOUSE_ID
 where  v.STATUS='Y' and s.STATUS='Y' and m.STATUS='Y' and h.STATUS='Y'
 ;
--增加索引
CREATE UNIQUE INDEX unq_ext_building_id ON csp_bp_house_map( BP_EXT_ID,BUILDING_ID);
CREATE UNIQUE INDEX unq_mobile ON csp_bp_general(mobile);
CREATE UNIQUE INDEX unq_id_type_no ON csp_bp_general(id_type,id_no);


