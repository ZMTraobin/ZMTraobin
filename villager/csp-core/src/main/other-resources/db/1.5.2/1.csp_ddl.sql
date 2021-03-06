 CREATE TABLE
    csp_ljh_base_company
    (
        COMPANY_ID CHAR(32) not null  COMMENT '公司ID',
        COMPANY_CODE VARCHAR(30) COMMENT '公司编码',
        COMPANY_NAME VARCHAR(100) not null  COMMENT '公司名称',
        CORP_ID  CHAR(32) COMMENT 'Corp_ID',
        ADDRESS_SECRET  CHAR(50) COMMENT '通讯录密钥',
        SOURCE_SYSTEM_ID VARCHAR(100) COMMENT '源系统ID',
        OPEN_APP_ID VARCHAR(50) COMMENT '第三方接入ID',
        SERVER_URL VARCHAR(5000) COMMENT '物管云服务地址',
        WORK_WX_FLAG VARCHAR(10) DEFAULT 'N' COMMENT '是否开启企业微信',
        OBJECT_VERSION_NUMBER bigint DEFAULT 1,
        REQUEST_ID bigint DEFAULT -1,
        PROGRAM_ID bigint DEFAULT -1,
        CREATED_BY bigint DEFAULT -1,
        CREATION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATED_BY bigint DEFAULT -1,
        LAST_UPDATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATE_LOGIN bigint DEFAULT -1,
        PRIMARY KEY (COMPANY_ID),
        INDEX BASE_COMPANY_N1 (CORP_ID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='物业公司配置信息表';

     CREATE TABLE
    csp_ljh_corp_agent
    (
        ID  bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
        CORP_ID  CHAR(32) COMMENT 'Corp_ID',
        AGENT_TYPE CHAR(32) not null DEFAULT 'build_self' COMMENT '应用类型（build_self,build_third）',
        AGENT_ID  VARCHAR(32) COMMENT 'agent_id',
        AGENT_SECRET  VARCHAR(50) COMMENT 'agent_secret',
        MESSAGE_TOKEN VARCHAR(50) COMMENT 'token',
        TNCODING_AES_KEY VARCHAR(50) COMMENT '消息密钥',
        OBJECT_VERSION_NUMBER bigint DEFAULT 1,
        REQUEST_ID bigint DEFAULT -1,
        PROGRAM_ID bigint DEFAULT -1,
        CREATED_BY bigint DEFAULT -1,
        CREATION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATED_BY bigint DEFAULT -1,
        LAST_UPDATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
        LAST_UPDATE_LOGIN bigint DEFAULT -1,
        PRIMARY KEY (ID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业号扩展应用信息';