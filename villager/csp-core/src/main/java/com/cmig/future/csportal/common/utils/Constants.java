package com.cmig.future.csportal.common.utils;


public class Constants {
	public static final String YES = "Y";

	public static final String NO = "N";

	public static final String TRUE = "true";

	public static final String FALSE = "false";

    public static final String SERVER_TEL="4008115251";

    //public static final String IMPORT_USER_FLAG="95279";
	
	/**
	 * 默认币种
	 * 
	 */
	public static final String DEFAULT_CURRENY_CODE = "CNY";
	public static final String DEFAULT_CURRENY_POINT = "POINT";
	/**
	 * 逗号
	 */
	public static final String COMMA = ",";
	/**
	 * 百分号
	 */
	public static final String PERCENT = "%";
	
	/**
	* 系统管理员角色ID
	*/
	public static final String ADMIN_ROLE_ID="1";

	/**
	 * 消息发送状态
	 */
	public static final String MSG_FLAG_FINISHED="1";//发送完成
	public static final String MSG_FLAG_DRAFT="2";//草稿
	public static final String MSG_FLAG_JOB="3";//定时发送
	
	/**
	 * 打印模板配置类型
	 */
	public static final String PRINTTEMPL_TYPE_FAMILY="TYPE_FAMILY";//报修单打印模板（家庭区域）
	public static final String PRINTTEMPL_TYPE_PUBILE="TYPE_PUBLIC";//报修单打印模板（公共区域）
	
	/**
	 * 消息发送类型
	 */
	public static final String MSG_TYPE_MESSAGE="2";//短信
	public static final String MSG_TYPE_EMAIL="1";//邮件
	public static final String MSG_TYPE_INNER="3";//站内信
	public static final String MSG_TYPE_OUT="4";//站外信	
	
	/**
	 * 消息类型
	 */
	public static final String MSG_TYPE_ID_SYSTEM="1";//系统消息
	public static final String MSG_TYPE_ID_WUYE="2";//物业小修
	public static final String MSG_TYPE_ID_EXPRESS="3";//快递消息
	public static final String MSG_TYPE_ID_EXPRESS_SEND="4";//代取消息
	
	/**
	 * 消息发送方式 
	 */
	public static final String AUTOMATICID_MANUAL="1";//手动
	public static final String AUTOMATICID_AUTO="0";//自动
	
	/**
	 * 消息发送人员类型
	 */
	public static final String PERSONNEL_TYPE_APP_USER="0";//业主
	public static final String PERSONNEL_TYPE_SYS_USER="1";//用户
	
	/**
	 * 消息模版名称
	 */
	public static final String MSG_TEMPLATE_NAME_COST_BILL_NEW="0";//缴费单据生成 
	public static final String MSG_TEMPLATE_NAME_REPAIR_ING="1";//报修单维修中 
	public static final String MSG_TEMPLATE_NAME_REPAIR_FINISHED="2";//报修单已完成 
	public static final String MSG_TEMPLATE_NAME_COMPLAINT_ING="3";//投诉单处理中
	
	/**
	 * 邻里活动、话题发布人员类型
	 */
	public static final String PUBLISH_TYPE_APP_USER="1";//业主
	public static final String PUBLISH_TYPE_SYS_USER="0";//用户
	/**
	 * 菜单类型
	 */
	public static final String MENU_TYPE_MENU="0";//菜单
	public static final String MENU_TYPE_ROLE="1";//权限
	
	/**
	 * 活动编辑状态
	 */
	public static final String BASE_ACTIVITY_SAVE="0";//保存
	public static final String BASE_ACTIVITY_SUBMIT="1";//提交
	
	/**
	 * 活动类型
	 */
	public static final String BASE_ACTIVITY_TYPE="0";//后台配置选项
	
	/**
	 * 展示方式
	 */
	public static final String BASE_DISPLAY_MODE="0";//小图平铺
	
	/**
	 * 参与者级可见性
	 */
	public static final String BASE_ACTIVITY_USER_RANGE="0";//全部用户
	
	/**
	 * 活动审核状态
	 */
	public static final String BASE_ACTIVITY_CHECK_STATUS_0="0";//未提交
	public static final String BASE_ACTIVITY_CHECK_STATUS_1="1";//待审核
	public static final String BASE_ACTIVITY_CHECK_STATUS_2="2";//审核通过
	public static final String BASE_ACTIVITY_CHECK_STATUS_3="3";//审核未通过
	/**
	 * 活动状态
	 */
	public static final String BASE_ACTIVITY_STATUS_0="0";//已结束
	public static final String BASE_ACTIVITY_STATUS_1="1";//未开始
	public static final String BASE_ACTIVITY_STATUS_2="2";//进行中
	/**
	 * 适用平台
	 */
	public static final String APP_TYPE_IOS="IOS";//IOS
	public static final String APP_TYPE_ANDROID ="Android";//ANDROID

	/**
	 * 活动上线标识
	 */
	public static final String BASE_ACTIVITY_DOWN_NO="0";//下线
	public static final String BASE_ACTIVITY_DOWN_YES="1";//上线
	/**
	 * 前段用户类型
	 */
	public static final String APP_USER_TYPE_ONE="1";//普通用户
	public static final String APP_USER_TYPE_TWO="2";//业主
	
	/**
	 * 快递单状态、等待自取、楼长待取、待楼长派送、已收
	 */
	public static final String EXPRESS_BILL_STATUS_TO_BE_TAKEN="TO_BE_TAKEN";//待取
	public static final String EXPRESS_BILL_STATUS_PICK_UP_SELF ="PICK_UP_SELF";//等待自取
	public static final String EXPRESS_BILL_STATUS_BUILDING_MANAGER_TO_BE_TAKEN ="BUILDING_MANAGER_TO_BE_TAKEN";//楼长待取
	public static final String EXPRESS_BILL_STATUS_BUILDING_MANAGER_TO_SEND ="BUILDING_MANAGER_TO_SEND";//待楼长派送
	public static final String EXPRESS_BILL_STATUS_RECEIVED ="RECEIVED";//已收
	
	
	/**
	 * 快递模块触发机制
	 */
	public static final String EXPRESS_EILL_TO_BE_TAKEN_SEND="EXPRESS_EILL_TO_BE_TAKEN_SEND";//快递单状态：待取（支持自取和上门的小区）
	public static final String EXPRESS_EILL_TO_BE_TAKEN ="EXPRESS_EILL_TO_BE_TAKEN";//快递单状态：待取  （仅支持自取的小区）
	public static final String EXPRESS_EILL_RECEIVED ="EXPRESS_EILL_RECEIVED";//快递单状态：已收
	public static final String EXPRESS_MANAGER_TO_SEND ="EXPRESS_MANAGER_TO_SEND";//快递单状态：派送上门
	public static final String EXPRESS_TO_BE_TAKENRECEIVED ="EXPRESS_TO_BE_TAKENRECEIVED";//待送单生成（状态为：已收）
	public static final String EXPRESS_TO_BE_TAKEN ="EXPRESS_TO_BE_TAKEN";//待送单生成（状态为：上门待取）
	/**
	 * 快递自取流程固话文案
	 */
	public static final String ADD_EXPRESS_BILL_SELF_STEP1="【%s】录入快递信息";
	public static final String ADD_EXPRESS_BILL_SELF_STEP2="用户选择自取";
	public static final String ADD_EXPRESS_BILL_SELF_STEP3="【%s】领取快件";
	/**
	 * 快递代送流程固话文案
	 */
	public static final String ADD_EXPRESS_BILL_DS_STEP2="用户选择代送";
	public static final String ADD_EXPRESS_BILL_DS_STEP3="【%s】领取快件";
	public static final String ADD_EXPRESS_BILL_DS_STEP4="【%s】确认快递已送达";
	public static final String ADD_EXPRESS_BILL_DS_STEP5="系统默认已签收";
	/**
	 * 取件方式
	 */
	public static final String PICKUPTYPE_SELF="SELF";//自取
	public static final String PICKUPTYPE_SEND="SEND";//代送
	
	/**
	 * 字典类型
	 */
	public static final String DIC_TYPE_DELIVERY_TIME_TYPE="DELIVERY_TIME_TYPE";//代送时间段
	public static final String DIC_TYPE_EXPRESS_COMPANY_TYPE="EXPRESS_COMPANY_TYPE";//快递公司
	public static final String DIC_TYPE_EXPRESS_BILL_STATUS="EXPRESS_BILL_STATUS";//快递单状态
	public static final String DIC_TYPE_PARK_TYPE="parkindType";//车位类型
	public static final String DIC_TYPE_PARK_STATUS="parkindStatus";//车位类型
	
	/**
	* 角色代码
	*/
	public static final String ROLE_CODE_LZ="LZ";//楼长
	/**
	* 与业主关系
	*/
	public static final String HOUSE_RELATION_MASTER="2";//房东
	public static final String HOUSE_RELATION_VISITOR="3";//租客
	/**
	 * 营销活动积分基数
	 */
	public static final String MARKETING_ACTIVITY_INTEGRAL_BASE="1";
	/**
	 * 营销活动金额基数
	 */
	public static final String MARKETING_ACTIVITY_MONEY_BASE="0.01";
	/**
	 * 营销活动活动状态
	 */
	public static final String MARKETING_ACTIVITY_STATUS_STOP="0";//已停止
	public static final String MARKETING_ACTIVITY_STATUS_NSTART="1";//未开始
	public static final String MARKETING_ACTIVITY_STATUS_START="2";//进行中
	public static final String MARKETING_ACTIVITY_STATUS_END="3";//已结束
	/**
	 * 营销活动活动类型
	 */
	public static final String MARKETING_ACTIVITY_TYPE_SYN="0";//综合活动
	public static final String MARKETING_ACTIVITY_TYPE_INTEGRAL="1";//积分活动
	public static final String MARKETING_ACTIVITY_TYPE_TICKET="2";//券活动
	
	/**
	 * 交易类型
	 */
	public static final String TRANSACTIONTYPE_INTEGRAL_CONSUMPTION="integral_consumption";//积分支付
	public static final String TRANSACTIONTYPE_INTEGRAL_REFUND="integral_refund";//积分返还
	
	
	/**
	* 短信验证码session中存储属性名
	*/
	public static final String LJLIFE_SMS_CODE="ljlife.sms.code";//短信验证码属性名
	/**
	 * 员工打卡迟到早退次数控制
	 */
	public static final Integer ATTENDANCE_EMPLOYEE_YES=0;//正常
	public static final Integer ATTENDANCE_EMPLOYEE_NO=1;//异常
	
	/**
	 * APP端功能终端分类
	 */
	public static final String APP_CONFIG_FLAG_OWNER="owner";//业主端
	public static final String APP_CONFIG_FLAG="propertyMC";//物业端
	/**
	 * APP端功能添加或移除
	 */
	public static final String APP_CONFIG_ADD="add";//添加
	public static final String APP_CONFIG_DEL="del";//移除
	public static final String APP_CONFIG_UP="up";//上移
	public static final String APP_CONFIG_DOWN="down";//下移
	
	/**
	 * 业主认证状态
	 */
	public static final String RELATIONS_ID_N="0";//未认证
	public static final String RELATIONS_ID_Y="1";//已认证
	/**
	 * 业主认证等级
	 */
	public static final String AUTHENTICATION_LEVEL_USER_NAME="USER_NAME";//业主姓名认证
	public static final String AUTHENTICATION_LEVEL_MANUAL="MANUAL";//人工审核认证
	
	public static final String MESSAGE_TYPE="messageType"; //message消息分类中的大类的字典type
	
	/**
	 * APP端功能分类
	 */
	public static final String APP_CONFIG_TYPE_INTERIOR="interior";//内部功能
	public static final String APP_CONFIG_TYPE_EXTERIOR="exterior";//外部功能
	public static final String APP_CONFIG_TYPE_PUBLICH="publich";//公共功能
	/**
	 * APP端功能状态
	 */
	public static final String APP_CONFIG_STATUS_ON="on";//启用
	public static final String APP_CONFIG_STATUS_OFF="off";//停用
	
	
	public static final String DEFAULT_SHOP_NAME="乐家自营";//店铺名默认
	
	
	//费用类型
	public static final String WYF_OREDER="wyf_";
	//费用类型
	public static final String WYF_NAME="物业费";
	//交易状态
	public static final String WYF_STATUS_NO_PAY = "noPay"; //待付款
	public static final String WYF_STATUS_YES_PAY = "yesPay"; //已付款
	//是否写入99生活
	public static final String WYF_WRITE_NO = "N"; //未写入
	public static final String WYF_WRITE_YES = "Y"; //已写入
	//缴费记录进账状态
	public static final String WYF_HOUSTON_NO = "0"; //0未进账
	public static final String WYF_HOUSTON_YES = "1"; //1已进账
	//支付方式
	//微信支付
	public static final String PAY_WEIXIN="微信支付";
	//支付宝
	public static final String PAY_ALIPAY="支付宝支付";

    /**
     *  票选活动状态
     */
    public static final String VOTING_ACTIVITY_STATUS_ON_LINE="online";//已上线
    public static final String VOTING_ACTIVITY_STATUS_ON_GOING="ongoing";//进行中
    public static final String VOTING_ACTIVITY_STATUS_FINISHED="finished";//已结束
    public static final String VOTING_ACTIVITY_STATUS_CLOSED="closed";//已关闭

    public static final String VOTING_ACTIVITY_SN="vote";//票选单号前缀

    public static final String RULE_VOTE_TYPE_CLOSED_SECOND="second";//按次数
    public static final String RULE_VOTE_TYPE_CLOSED_DAY="day";//按天

    /**
     * 第三方授权信息缓存key名
     */
    public static final String CACHE_OPENAPP_MAP = "app.open.info.map";

    /**
     * 组织机构类型
     */
    public static final String SYS_OFFICE_TYPE_1="1";//公司
    public static final String SYS_OFFICE_TYPE_2="2";//部门
    public static final String SYS_OFFICE_TYPE_3="3";//小组
    public static final String SYS_OFFICE_TYPE_4="4";//其它
    public static final String SYS_OFFICE_TYPE_5="5";//小区


    /**
     * 建筑状态
     */
    public static final String STRUCTURE_STATE_ENABLED="ENABLED";
    public static final String STRUCTURE_STATE_DISABLED="DISABLED";

	/**
	 * 积分规则状态
	 */
	public static final String INTEGRAL_RULE_ENABLED="enable";
	public static final String INTEGRAL_RULE_DISABLED="disabled";

    /**
     * 栏目名
     */
    public static final String CATEGORY_NAME_NOTIFY="通知公告";
    public static final String CATEGORY_NAME_COMMUNITY_NOTICE="小区公告";
    public static final String CATEGORY_NAME_HEALTH="乐家健康";
    public static final String CATEGORY_NAME_ABOUTUS="关于我们";
    public static final String CATEGORY_NAME_USER_AGREEMENT="用户协议";
    public static final String CATEGORY_NAME_PRODUCT_ISSUING_RULES="商品发布规则";

    /**
     * 通知公告状态
     */
    public static final String CMS_NOTIFY_DRAFT ="DRAFT";//草稿
    public static final String CMS_NOTIFY_PUBLISHED="PUBLISHED";//已发布

    /**
     * 消息推送状态
     */
    public static final String NOTIFY_STATUS_ING ="PUSHING";//发送中
    public static final String NOTIFY_STATUS_SUCCESS ="SUCCESS";//成功
    public static final String NOTIFY_STATUS_FAIL ="FAIL";//失败


	/**
	 * 字典类型常量
	 */
	public static final String CODE_MGT_EXPENDITURE_TYPE="MGT.EXPENDITURE_TYPE";
	public static final String CODE_MGT_BUILDING_TYPE="MGT.BUILDING_TYPE";


	/**
	 * 功能类目
	 */
	public static final String FUNCTION_SORT_PAYFEES="payfees";//物管云物业缴费
	public static final String FUNCTION_SORT_PAYFEES_STANDER="payfees_stander";//标准物业缴费
	public static final String FUNCTION_SORT_RENOVATION="renovation";//装修申请
	public static final String FUNCTION_SORT_REPAIRS="repairs";//报修
	public static final String FUNCTION_SORT_PASSCARD="passcard";//门禁
	public static final String FUNCTION_SORT_MYHOUSEKEEPER="myHousekeeper";//管家


}
