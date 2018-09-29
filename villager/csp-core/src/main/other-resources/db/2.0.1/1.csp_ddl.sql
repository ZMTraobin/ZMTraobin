


 alter table csp_ljh_cms_article add cover VARCHAR(200) COMMENT '封面' after TITLE;
 alter table csp_ljh_cms_article add video VARCHAR(2000) COMMENT '视频' after cover;

alter table csp_ljh_app_user add SELF_INTRODUCTION varchar(1000) comment '自我介绍' after EMAIL;


alter table csp_ljh_neighbor_topic modify TYPE_ID char(32) comment 'tag类别';
alter table csp_ljh_neighbor_topic add topic_type char(32) comment '话题类型' after TYPE_ID;

 drop table csp_villager_order;
 CREATE TABLE
    csp_villager_order
    (
        ID bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
        APP_USER_ID VARCHAR(50) NOT NULL COMMENT '下单用户ID',
        ORDER_NUMBER VARCHAR(32) NOT NULL COMMENT '订单号',
        ORDER_AMOUNT bigint NOT NULL COMMENT '订单总额 (单位为分)',
        DISCOUNT_AMOUNT bigint DEFAULT 0 NOT NULL COMMENT '折扣金额 (单位为分)',
        INTEGRAL_AMOUNT bigint DEFAULT 0 NOT NULL COMMENT '积分抵扣 (单位为分)',
        PAYABLE_AMOUNT bigint DEFAULT 0 NOT NULL COMMENT '应付金额 (单位为分)',
        PAID_AMOUNT bigint COMMENT '实付金额(单位为分)',
        TIME_EXPIRE DATETIME COMMENT '订单失效时间',
        TIME_PAID DATETIME COMMENT '支付成功时间',
        TIME_SEND DATETIME COMMENT '送货完成时间',
        TIME_SETTLE DATETIME COMMENT '清算时间',
        GOOD_ID VARCHAR(50) NOT NULL COMMENT '商品ID',
        GOOD_NUM bigint DEFAULT 0 NOT NULL COMMENT '商品数量',
        CLIENT_IP VARCHAR(20) NOT NULL COMMENT '下单IPv4地址',
        PAY_STATUS VARCHAR(20) DEFAULT 'N' NOT NULL COMMENT '支付状态(YPN)',
        ORDER_STATUS VARCHAR(20) COMMENT '业务状态',
        ORDER_TYPE VARCHAR(50) COMMENT '订单类型',
        DESCRIPTION VARCHAR(255) COMMENT '订单附加说明',
        OBJECT_VERSION_NUMBER bigint DEFAULT -1,
        PROGRAM_ID bigint DEFAULT -1,
        REQUEST_ID bigint DEFAULT -1,
        CREATED_BY bigint DEFAULT -1,
        CREATION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP comment '下单时间',
        LAST_UPDATED_BY bigint DEFAULT -1,
        LAST_UPDATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATE_LOGIN bigint DEFAULT -1,
        PRIMARY KEY (ID),
        CONSTRAINT csp_villager_order_u1 UNIQUE (ORDER_NUMBER)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小商品订单表';

   drop table csp_villager_good;
 CREATE TABLE
    csp_villager_good
    (
        ID bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
        GOOD_CATEGORY VARCHAR(50) NOT NULL COMMENT '商品类别',
        GOOD_SKU VARCHAR(50) NOT NULL COMMENT '商品编码',
        GOOD_NAME VARCHAR(32) NOT NULL COMMENT '商品名称',
        GOOD_IMAGE VARCHAR(2000) COMMENT '商品配图',
        GOOD_SUBJECT VARCHAR(100) COMMENT '商品摘要',
        GOOD_BODY VARCHAR(100) COMMENT '商品描述',
        PRICE bigint NOT NULL COMMENT '单价 (单位为分)',
        UNIT VARCHAR(20) comment '单位',
        STATUS VARCHAR(100) COMMENT '状态',
        OBJECT_VERSION_NUMBER bigint DEFAULT -1,
        PROGRAM_ID bigint DEFAULT -1,
        REQUEST_ID bigint DEFAULT -1,
        CREATED_BY bigint DEFAULT -1,
        CREATION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATED_BY bigint DEFAULT -1,
        LAST_UPDATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATE_LOGIN bigint DEFAULT -1,
        PRIMARY KEY (ID),
        CONSTRAINT csp_villager_good_u1 UNIQUE (GOOD_SKU)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品配置表';

alter table csp_ljh_app_user modify RELATION_FLAG char(1) comment '是否实名认证';

 CREATE TABLE
    csp_villager_voice_order
    (
        ID bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
        APP_USER_ID VARCHAR(50) NOT NULL COMMENT '下单用户ID',
        ORDER_NUMBER VARCHAR(32) NOT NULL COMMENT '订单号',
        ORDER_AMOUNT bigint DEFAULT 0 NOT NULL COMMENT '订单总额 (单位为分)',
        DISCOUNT_AMOUNT bigint DEFAULT 0 NOT NULL COMMENT '折扣金额 (单位为分)',
        INTEGRAL_AMOUNT bigint DEFAULT 0 NOT NULL COMMENT '积分抵扣 (单位为分)',
        PAYABLE_AMOUNT bigint DEFAULT 0 NOT NULL COMMENT '应付金额 (单位为分)',
        PAID_AMOUNT bigint COMMENT '实付金额(单位为分)',
        TIME_EXPIRE DATETIME COMMENT '订单失效时间',
        TIME_PAID DATETIME COMMENT '支付成功时间',
        TIME_SEND DATETIME COMMENT '送货完成时间',
        TIME_SETTLE DATETIME COMMENT '清算时间',
        GOOD_ID VARCHAR(50) COMMENT '商品ID',
        GOOD_NAME VARCHAR(50) COMMENT '商品名称',
        GOOD_NUM bigint DEFAULT 0 NOT NULL COMMENT '商品数量',
        CLIENT_IP VARCHAR(20) NOT NULL COMMENT '下单IPv4地址',
        PAY_STATUS VARCHAR(20) DEFAULT 'N' NOT NULL COMMENT '支付状态(YPN)',
        ORDER_STATUS VARCHAR(20) COMMENT '业务状态',
        ORDER_TYPE VARCHAR(50) COMMENT '订单类型',
        DESCRIPTION VARCHAR(255) COMMENT '订单附加说明',
        VOICE_URL VARCHAR(2000) COMMENT '语音留言地址',
        VOICE_CONTENT VARCHAR(2000) COMMENT '语音留言内容',
        OBJECT_VERSION_NUMBER bigint DEFAULT -1,
        PROGRAM_ID bigint DEFAULT -1,
        REQUEST_ID bigint DEFAULT -1,
        CREATED_BY bigint DEFAULT -1,
        CREATION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP comment '下单时间',
        LAST_UPDATED_BY bigint DEFAULT -1,
        LAST_UPDATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATE_LOGIN bigint DEFAULT -1,
        PRIMARY KEY (ID),
        CONSTRAINT csp_villager_voice_order_u1 UNIQUE (ORDER_NUMBER)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='语音订单表';


CREATE TABLE
    csp_villager_keyword_function
    (
        ID bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
        keyword VARCHAR(1000) NOT NULL COMMENT '关键字',
        function_code VARCHAR(100) COMMENT '功能代码',
        function_name VARCHAR(100) COMMENT '功能描述',
        weight int default 0 not null COMMENT '权重',
        OBJECT_VERSION_NUMBER bigint DEFAULT -1,
        PROGRAM_ID bigint DEFAULT -1,
        REQUEST_ID bigint DEFAULT -1,
        CREATED_BY bigint DEFAULT -1,
        CREATION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP comment '创建时间',
        LAST_UPDATED_BY bigint DEFAULT -1,
        LAST_UPDATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATE_LOGIN bigint DEFAULT -1,
        PRIMARY KEY (ID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='关键词功能表';
