 CREATE TABLE
    csp_life_pay_bill
    (
        ID BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
        PRODUCT_TYPE VARCHAR(20) NOT NULL  COMMENT '产品类别',
        PRODUCT_ID VARCHAR(50) NOT NULL  COMMENT '产品ID',
        BILL_ID VARCHAR(50) NOT NULL  COMMENT '账单ID',
        BILL_ORG_ID VARCHAR(20) NOT NULL COMMENT '出账机构代码',
        BILL_ORG_NAME VARCHAR(50) NOT NULL COMMENT '出账机构名称',
        BILL_NO VARCHAR(50)  COMMENT '账单号',
        BILL_MONTH VARCHAR(10) NOT NULL COMMENT '账期（YYYYMM）',
        BILL_AMT BIGINT DEFAULT 0 NOT NULL COMMENT '账单金额(分)',
        BILL_RECORD_TIMES VARCHAR(10) COMMENT '抄次',
        SEARCH_TYPE VARCHAR(10) COMMENT '查询方式',
        BARCODE VARCHAR(50) COMMENT '条码',
        DOOR_CODE VARCHAR(50) COMMENT '户号',
        BILL_ADDR VARCHAR(200)  COMMENT '账单地址',
        BILL_OWNER VARCHAR(15) COMMENT '户名',
        OVERDUE_FEE BIGINT DEFAULT 0 NOT NULL COMMENT '滞纳金（分）',
        IS_INSURANCE CHAR(1)  COMMENT '是否保险费（Y/N）',
        BILL_STATUS VARCHAR(10) COMMENT '账单状态',
        THIRD_ORDER_NO VARCHAR(50) COMMENT '第三方订单号',
        ORDER_NOTIFY_TIME DATETIME COMMENT '快币后台通知时间',
        ORDER_NOTIFY_STATUS VARCHAR(10) COMMENT '快币后台通知状态',
        OBJECT_VERSION_NUMBER bigint DEFAULT 1 COMMENT '记录版本号',
        REQUEST_ID bigint DEFAULT -1 COMMENT '请求ID',
        PROGRAM_ID bigint DEFAULT -1 COMMENT '线程ID',
        CREATED_BY bigint DEFAULT -1 COMMENT '创建人',
        CREATION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        LAST_UPDATED_BY bigint DEFAULT -1 COMMENT '最后更新人',
        LAST_UPDATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
        LAST_UPDATE_LOGIN bigint DEFAULT -1 COMMENT '最后登陆人',
        PRIMARY KEY (ID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='生活缴费账单表';