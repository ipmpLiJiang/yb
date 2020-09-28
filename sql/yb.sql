#复议规则
DROP TABLE IF EXISTS yb_reconsider_rule;
CREATE TABLE yb_reconsider_rule (
  id char(36) NOT NULL COMMENT '复议规则id',
	gzNo int(11) NOT NULL COMMENT '序号',
  gzDescribe varchar(255) NOT NULL COMMENT '规则描述',
  gzXplain varchar(255) NOT NULL COMMENT '规则解释',
  keypoints varchar(255) NOT NULL COMMENT '复议重点',
	materials varchar(2000) NOT NULL COMMENT '复议资料',
	operatorId bigint(11) NOT NULL COMMENT '操作员代码',
	operatorName varchar(50) NOT NULL COMMENT '操作员名称',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#复议申请
DROP TABLE IF EXISTS yb_reconsider_apply;
CREATE TABLE yb_reconsider_apply (
  id char(36) NOT NULL COMMENT '规则编号',
  applyDate datetime NOT NULL COMMENT '复议年月',
  applyDateStr varchar(10) NOT NULL COMMENT '复议年月Str',
  operatorId bigint(11) NOT NULL COMMENT '操作员代码',
  operatorName varchar(50) NOT NULL COMMENT '操作员名称',
  uploadFileNameOne varchar(100) DEFAULT NULL COMMENT '审核一上传名称',
  uploadFileNameTwo varchar(100) DEFAULT NULL COMMENT '审核二上传名称',
  endDateOne datetime DEFAULT NULL COMMENT '审核一结束时间',
  endDateTwo datetime DEFAULT NULL COMMENT '审核二结束时间',
  resultState int(4) DEFAULT 0 COMMENT '申诉状态',#0未申诉 1已申诉
  repayState int(4) DEFAULT 0 COMMENT '还款状态', #0未还款 1已还款
  resetState int(4) DEFAULT 0 COMMENT '剔除状态', #0未剔除 1已剔除
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT 1 COMMENT '状态',#1待复议 2上传一 3申述一 4上传二 5申述二 6已剔除 7已还款
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


#复议申请明细
DROP TABLE IF EXISTS yb_reconsider_apply_data;
CREATE TABLE yb_reconsider_apply_data (
  id char(36) NOT NULL COMMENT '申请数据明细',
	orderNumber varchar(50) NOT NULLL COMMENT '序号',
	pid char(36) NOT NULL COMMENT '申请编号',
  serialNo varchar(100) NOT NULL COMMENT '交易流水号',
	billNo varchar(50) NOT NULL COMMENT '单据号',
	proposalCode varchar(50) NOT NULL COMMENT '意见书编码',
	projectCode varchar(100) DEFAULT NULL COMMENT '项目编码',
	projectName varchar(200) DEFAULT NULL COMMENT '项目名称',
	num decimal(12,4) DEFAULT NULL COMMENT '数量',
	medicalPrice decimal(17,4) DEFAULT NULL COMMENT '医保内金额',
	ruleName varchar(50) DEFAULT NULL COMMENT '规则名称',
	deductPrice decimal(17,4) DEFAULT NULL COMMENT '扣除金额',
	deductReason varchar(1000) DEFAULT NULL COMMENT '扣除原因',
	repaymentReason varchar(1000) DEFAULT NULL COMMENT '还款原因',
	doctorName varchar(50) DEFAULT NULL COMMENT '医生姓名',
	deptCode varchar(100) DEFAULT NULL COMMENT '科室编码',
	deptName varchar(100) DEFAULT NULL COMMENT '科室名称',
	enterHospitalDate datetime DEFAULT NULL COMMENT '入院日期',
  enterHospitalDateStr varchar(50) DEFAULT NULL COMMENT '入院日期str',
	outHospitalDate datetime DEFAULT NULL COMMENT '出院日期',
	outHospitalDateStr varchar(50) DEFAULT NULL COMMENT '出院日期str',
	costDate datetime DEFAULT NULL COMMENT '费用日期',
  costDateStr varchar(50) DEFAULT NULL COMMENT '费用日期str',
	hospitalizedNo varchar(50) DEFAULT NULL COMMENT '住院号',
	treatmentMode varchar(50) DEFAULT NULL COMMENT '就医方式',
	settlementDate datetime DEFAULT NULL COMMENT '结算日期',
  settlementDateStr varchar(50) DEFAULT NULL COMMENT '结算日期Str',
	personalNo varchar(50) DEFAULT NULL COMMENT '个人编号',
	insuredName varchar(50) DEFAULT NULL COMMENT '参保人姓名',
	cardNumber varchar(50) DEFAULT NULL COMMENT '医保卡号',
	areaName varchar(50) DEFAULT NULL COMMENT '统筹区名称',
	versionNumber varchar(50) DEFAULT NULL COMMENT '版本号',
	backAppeal varchar(1000) DEFAULT NULL COMMENT '反馈申诉',
	insuredType varchar(50) DEFAULT NULL COMMENT '参保类型',
	typeno int(4) NOT NULL COMMENT '版本类型',
	dataType int(4) NOT NULL COMMENT '扣款类型', -- 0 明细扣款 1 主单扣款
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#核对申请
#DROP TABLE IF EXISTS yb_reconsider_verify;
CREATE TABLE yb_reconsider_verify (
  id char(36) NOT NULL COMMENT '核对申请',
	applyDataId char(36) NOT NULL COMMENT '复议申请明细',  
	verifyDoctorCode varchar(50) NOT NULL COMMENT '参考复议医生编码',
	verifyDoctorName varchar(50) NOT NULL COMMENT '参考复议医生',
  verifyDeptCode varchar(100) NOT NULL COMMENT '参考复议科室编码',
	verifyDeptName varchar(100) NOT NULL COMMENT '参考复议科室',
	operateReason varchar(1000) DEFAULT NULL COMMENT '操作理由',
	operateDate datetime DEFAULT NULL COMMENT '操作日期',
	matchPersonId bigint(11) DEFAULT NULL COMMENT '匹配人代码',
	matchPersonName varchar(50) DEFAULT NULL COMMENT '匹配人',
	matchDate datetime DEFAULT NULL COMMENT '匹配日期', 
	reviewerId bigint(11) DEFAULT NULL COMMENT '审核人代码',
	reviewerName varchar(50) DEFAULT NULL COMMENT '审核人',
	reviewerDate datetime DEFAULT NULL COMMENT '审核日期', 
	sendPersonId bigint(11) DEFAULT NULL COMMENT '发送人代码',
	sendPersonName varchar(50) DEFAULT NULL COMMENT '发送人',	
	sendDate datetime DEFAULT NULL COMMENT '发送日期', 
	currencyField varchar(50) DEFAULT NULL COMMENT '通用',
dataType int(4) NOT NULL COMMENT '扣款类型', -- 0 明细扣款 1 主单扣款
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT 1 COMMENT '状态',#1待审核、2已审核、3已发送
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#申诉管理
#DROP TABLE IF EXISTS yb_appeal_manage;
CREATE TABLE yb_appeal_manage (
  id char(36) NOT NULL COMMENT '申诉管理',
	applyDataId char(36) NOT NULL COMMENT '复议申请明细',
	verifyId  char(36) NOT NULL COMMENT '核对Id',
	verifySendId  char(36) NOT NULL COMMENT '发送关联Id',
	readyDoctorCode varchar(50) NOT NULL COMMENT '复议医生编码',
	readyDoctorName varchar(50) NOT NULL COMMENT '复议医生',
  	readyDeptCode varchar(100) NOT NULL COMMENT '复议科室编码',
	readyDeptName varchar(100) NOT NULL COMMENT '复议科室',
	changeDoctorCode varchar(50) DEFAULT NULL COMMENT '变更医生编码',
	changeDoctorName varchar(50) DEFAULT NULL COMMENT '变更医生',
  changeDeptCode varchar(100) DEFAULT NULL COMMENT '变更复议科室编码',
	changeDeptName varchar(100) DEFAULT NULL COMMENT '变更复议科室',	
	acceptState int(4) DEFAULT 0 COMMENT '接受状态',#0待接收，1接受，2不接受，3管理员更改，4医保拒绝，6已上传，7已过期(未申诉)
	operateReason varchar(1000) DEFAULT NULL COMMENT '操作理由',
	operateProcess varchar(50) DEFAULT NULL COMMENT '操作过程',
	operateDate datetime DEFAULT NULL COMMENT '操作日期',	
	refuseId bigint(11) DEFAULT NULL COMMENT '医保人代码',
	refuseName varchar(50) DEFAULT NULL COMMENT '医保人',
	refuseReason varchar(1000) DEFAULT NULL COMMENT '拒绝理由',
	refuseDate datetime DEFAULT NULL COMMENT '拒绝日期',
	adminPersonId bigint(11) DEFAULT NULL COMMENT '管理员代码',
	adminPersonName varchar(50) DEFAULT NULL COMMENT '管理员',
	adminChangeDate datetime DEFAULT NULL COMMENT '更改日期', 
	adminReason varchar(1000) DEFAULT NULL COMMENT '管理员理由',
	enableDate datetime DEFAULT NULL COMMENT '可操作日期',
	sourceType int(4) DEFAULT 0 COMMENT '来源类型',//0正常流程、1剔除业务
	currencyField varchar(50) DEFAULT NULL COMMENT '通用',
	approvalState DEFAULT 0 COMMENT '审批状态',//0正常 1同意 2拒绝
dataType int(4) NOT NULL COMMENT '扣款类型', -- 0 明细扣款 1 主单扣款
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT 1 COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


#DROP TABLE IF EXISTS yb_appeal_result;
CREATE TABLE yb_appeal_result (
  id char(36) NOT NULL COMMENT '复议结果',
	applyDataId char(36) NOT NULL COMMENT '复议申请明细',
	verifyId  char(36) NOT NULL COMMENT '核对Id',
	manageId  char(36) NOT NULL COMMENT '管理Id',
	doctorCode varchar(50) NOT NULL COMMENT '医生编码',
	doctorName varchar(50) NOT NULL COMMENT '医生',
  	deptCode varchar(100) NOT NULL COMMENT '科室编码',
	deptName varchar(100) NOT NULL COMMENT '科室',
	operateReason varchar(1000) DEFAULT NULL COMMENT '理由',
	operateDate datetime DEFAULT NULL COMMENT '操作日期',
	resetDataId  char(36) DEFAULT NULL COMMENT '剔除明细扣款Id',
	resetDate datetime DEFAULT NULL COMMENT '剔除日期',
        resetPersonId bigint(11) DEFAULT NULL COMMENT '剔除人代码',
	resetPersonName varchar(50) DEFAULT NULL COMMENT '剔除人',
	sourceType int(4) DEFAULT 0 COMMENT '来源类型',//0正常流程、1剔除业务
	#isDeductImplement int(4) DEFAULT 0 COMMENT '是否填报扣款落实',// 0未填报、1已填报
	#isRepayImplement int(4) DEFAULT 0 COMMENT '是否填报还款落实',// 0未填报、1已填报
	currencyField varchar(50) DEFAULT NULL COMMENT '通用',
dataType int(4) NOT NULL COMMENT '扣款类型', -- 0 明细扣款 1 主单扣款
repayState int(4) DEFAULT 1 COMMENT '还款状态', -- 1 成功 2 失败 3 部分调整 
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT 1 COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#DROP TABLE IF EXISTS yb_appeal_result_deductImplement;
CREATE TABLE yb_appeal_result_deductImplement (
  id char(36) NOT NULL COMMENT '扣款落实Id',
	resultId char(36) NOT NULL COMMENT '复议上传Id',
	resetDataId  char(36) NOT NULL COMMENT '剔除明细Id',
	implementDate datetime NOT NULL COMMENT '绩效年月',
	implementDateStr varchar(10) NOT NULL COMMENT '绩效年月Str',
	shareState int(4) DEFAULT 0 COMMENT '分摊方式', # 0 个人 1科室
	shareProgramme varchar(200) DEFAULT NULL COMMENT '分摊方案',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT 1 COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


#DROP TABLE IF EXISTS yb_reconsider_InpatientFees;
CREATE TABLE yb_reconsider_InpatientFees (
id char(36) NOT NULL COMMENT '医保住院His',
inpatientId varchar(100) NOT NULL COMMENT '住院号',
patientName varchar(100) NOT NULL COMMENT '患者姓名',
settlementId varchar(100) NOT NULL COMMENT 'HIS结算序号',
billNo varchar(100) NOT NULL COMMENT '单据号',
transNo varchar(100) NOT NULL COMMENT '交易流水号',
itemId varchar(100) NOT NULL COMMENT '项目代码',
itemCode varchar(100) NOT NULL COMMENT '项目医保编码',
itemName varchar(100) NOT NULL COMMENT '项目名称',
itemCount decimal(17,4) DEFAULT NULL COMMENT '项目数量',
itemPrice decimal(17,4) DEFAULT NULL COMMENT '项目单价',
itemAmount decimal(17,4) DEFAULT NULL COMMENT '项目金额',
feeDate datetime DEFAULT NULL COMMENT '费用日期',
deptId varchar(100) DEFAULT NULL COMMENT '住院科室代码',
deptName varchar(100) DEFAULT NULL COMMENT '住院科室名称',
orderDocId varchar(100) DEFAULT NULL COMMENT '开方医生代码',
orderDocName varchar(100) DEFAULT NULL COMMENT '开方医生名称',
excuteDeptId varchar(100) DEFAULT NULL COMMENT '执行科室代码',
excuteDeptName varchar(100) DEFAULT NULL COMMENT '执行科室名称',
excuteDocId varchar(100) DEFAULT NULL COMMENT '执行医生代码',
excuteDocName varchar(100) DEFAULT NULL COMMENT '执行医生名称',
settlementDate datetime DEFAULT NULL COMMENT '结算时间',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS com_configureManage;
CREATE TABLE com_configureManage (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '配置管理',
  intField tinyint(4) DEFAULT 0 COMMENT '数字',
	numericField numeric(8,2) DEFAULT 0 COMMENT '小数',
	stringField varchar(50) DEFAULT NULL COMMENT '字符',
	currencyField varchar(50) DEFAULT NULL COMMENT '通用字段',
	configureType tinyint(4) DEFAULT NULL COMMENT '类型',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#剔除
DROP TABLE IF EXISTS yb_reconsider_reset;
CREATE TABLE yb_reconsider_reset (
  id char(36) NOT NULL COMMENT '剔除表',
  applyDate datetime DEFAULT NULL COMMENT '复议年月',
  applyDateStr varchar(10) NOT NULL COMMENT '复议年月Str',
  currencyField varchar(100) DEFAULT NULL COMMENT '通用',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#剔除明细
DROP TABLE IF EXISTS yb_reconsider_reset_data;
CREATE TABLE yb_reconsider_reset_data (
  id char(36) NOT NULL COMMENT '剔除表明细扣款',
orderNumber varchar(50) DEFAULT NULL COMMENT '序号',
	pid char(36) NOT NULL COMMENT '关联',
  serialNo varchar(100) NOT NULL COMMENT '交易流水号',
	billNo varchar(50) NOT NULL COMMENT '单据号',
	projectCode varchar(100) DEFAULT NULL COMMENT '项目编码',
	projectName varchar(200) DEFAULT NULL COMMENT '项目名称',
	medicalPrice decimal(17,4) DEFAULT NULL COMMENT '医保内金额',
	ruleName varchar(50) DEFAULT NULL COMMENT '规则名称',
	deductPrice decimal(17,4) DEFAULT NULL COMMENT '扣除金额',
	deductReason varchar(1000) DEFAULT NULL COMMENT '扣除原因',
	repaymentReason varchar(1000) DEFAULT NULL COMMENT '还款原因',
	doctorName varchar(50) DEFAULT NULL COMMENT '医生姓名',
	deptCode varchar(100) DEFAULT NULL COMMENT '科室编码',
	deptName varchar(100) DEFAULT NULL COMMENT '科室名称',
	costDate datetime DEFAULT NULL COMMENT '费用日期',
  costDateStr varchar(50) DEFAULT NULL COMMENT '费用日期str',
	hospitalizedNo varchar(50) DEFAULT NULL COMMENT '住院号',
	treatmentMode varchar(50) DEFAULT NULL COMMENT '就医方式',
	settlementDate datetime DEFAULT NULL COMMENT '结算日期',
  settlementDateStr varchar(50) DEFAULT NULL COMMENT '结算日期Str',
	personalNo varchar(50) DEFAULT NULL COMMENT '个人编号',
	insuredName varchar(50) DEFAULT NULL COMMENT '参保人姓名',
	cardNumber varchar(50) DEFAULT NULL COMMENT '医保卡号',
	areaName varchar(50) DEFAULT NULL COMMENT '统筹区名称',
	insuredType varchar(50) DEFAULT NULL COMMENT '参保类型',
	dataType int(4) NOT NULL COMMENT '扣款类型', -- 0 明细扣款 1 主单扣款
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  seekState int(4) DEFAULT 0 COMMENT '匹配状态',-- 0未匹配 1已匹配
repaymentPrice decimal(17,4)  DEFAULT NULL COMMENT '还款金额',
  STATE int(4) DEFAULT 0 COMMENT '状态',-- 0上传 1一对多 2未知
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#人工复议
DROP TABLE IF EXISTS yb_handle_verify;
CREATE TABLE yb_handle_verify (
  id char(36) NOT NULL COMMENT '手动核对',
  applyDate datetime DEFAULT NULL COMMENT '复议年月',
  applyDateStr varchar(10) NOT NULL COMMENT '复议年月Str',
  currencyField varchar(100) DEFAULT NULL COMMENT '通用',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS yb_handle_verify_data;
CREATE TABLE yb_handle_verify_data (
  id char(36) NOT NULL COMMENT '手动核对明细',
	pid char(36) NOT NULL COMMENT '手动核对Id',
	resetId char(36) NOT NULL COMMENT '剔除明细Id',  
	resultId char(36) NOT NULL COMMENT '复议上传Id',  
	applyDataId char(36) NOT NULL COMMENT '复议申请明细',
	verifyId  char(36) NOT NULL COMMENT '核对Id',
	manageId  char(36) NOT NULL COMMENT '管理Id',
	doctorCode varchar(50) NOT NULL COMMENT '复议医生编码',
	doctorName varchar(50) NOT NULL COMMENT '复议医生',
  	deptCode varchar(100) NOT NULL COMMENT '复议科室编码',
	deptName varchar(100) NOT NULL COMMENT '复议科室',
	operateReason varchar(1000) DEFAULT NULL COMMENT '操作理由',
	operateDate datetime DEFAULT NULL COMMENT '操作日期',
	matchPersonId bigint(11) DEFAULT NULL COMMENT '匹配人代码',
	matchPersonName varchar(50) DEFAULT NULL COMMENT '匹配人',
	matchDate datetime DEFAULT NULL COMMENT '匹配日期', 
	reviewerId bigint(11) DEFAULT NULL COMMENT '审核人代码',
	reviewerName varchar(50) DEFAULT NULL COMMENT '审核人',
	reviewerDate datetime DEFAULT NULL COMMENT '审核日期', 
	sendPersonId bigint(11) DEFAULT NULL COMMENT '发送人代码',
	sendPersonName varchar(50) DEFAULT NULL COMMENT '发送人',	
	sendDate datetime DEFAULT NULL COMMENT '发送日期', 
	currencyField varchar(100) DEFAULT NULL COMMENT '通用',
  dataType int(4) NOT NULL COMMENT '扣款类型', -- 0 明细扣款 1 主单扣款
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT 0 COMMENT '状态',#0待发送、1已发送
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS yb_reconsider_repay;
CREATE TABLE yb_reconsider_repay (
  id char(36) NOT NULL COMMENT '还款核对',
  applyDate datetime DEFAULT NULL COMMENT '复议年月',
  applyDateStr varchar(10) NOT NULL COMMENT '复议年月Str',
  operatorId bigint(11) NOT NULL COMMENT '操作员代码',
  operatorName varchar(50) NOT NULL COMMENT '操作员名称',
  uploadFileName varchar(100) DEFAULT NULL COMMENT '居保上传名称',
  dataType int(4) NOT NULL COMMENT '扣款类型', -- 0 明细扣款 1 主单扣款
  repayType int(4)  DEFAULT 0 COMMENT '保险类型',-- 0 居保 1职保
  currencyField varchar(100) DEFAULT NULL COMMENT '通用',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS yb_reconsider_repay_data;
CREATE TABLE yb_reconsider_repay_data (
  id char(36) NOT NULL COMMENT '还款核对明细',
	pid char(36) NOT NULL COMMENT '还款核对Id',
	belongDate datetime NOT NULL COMMENT  '所属期',
	belongDateUpload datetime NOT NULL COMMENT  '所属期Upload',
	belongDateStr varchar(10) NOT NULL COMMENT '所属期Str',
	hospitalCode varchar(50) DEFAULT NULL COMMENT '医院编号',
	hospitalName varchar(100) DEFAULT NULL COMMENT '医院名称',
	orderNumber varchar(50) DEFAULT NULL COMMENT '序号',
	orderNumberNew varchar(50) DEFAULT NULL COMMENT '序号new',
  	serialNo varchar(100) DEFAULT NULL COMMENT '交易流水号',
	billNo varchar(50) DEFAULT NULL COMMENT '单据号',
	projectCode varchar(100) DEFAULT NULL COMMENT '项目编码',
	projectName varchar(200) DEFAULT NULL COMMENT '项目名称',
	medicalPrice decimal(17,4) DEFAULT NULL COMMENT '医保内金额',
	ruleName varchar(50) DEFAULT NULL COMMENT '规则名称',
	deductPrice decimal(17,4) DEFAULT NULL COMMENT '扣除金额',
	deductReason varchar(1000) DEFAULT NULL COMMENT '扣除原因',
	repaymentPrice decimal(17,4) DEFAULT NULL COMMENT '还款金额',
	repaymentReason varchar(1000) DEFAULT NULL COMMENT '还款原因',
	doctorName varchar(50) DEFAULT NULL COMMENT '医生姓名',
	deptCode varchar(100) DEFAULT NULL COMMENT '科室编码',
	deptName varchar(100) DEFAULT NULL COMMENT '科室名称',
	costDate datetime DEFAULT NULL COMMENT '费用日期',
  costDateStr varchar(50) DEFAULT NULL COMMENT '费用日期str',
	hospitalizedNo varchar(50) DEFAULT NULL COMMENT '住院号',
	treatmentMode varchar(50) DEFAULT NULL COMMENT '就医方式',
	settlementDate datetime DEFAULT NULL COMMENT '结算日期',
  settlementDateStr varchar(50) DEFAULT NULL COMMENT '结算日期Str',
	personalNo varchar(50) DEFAULT NULL COMMENT '个人编号',
	insuredName varchar(50) DEFAULT NULL COMMENT '参保人姓名',
	resetDataId  char(36) DEFAULT NULL COMMENT '剔除明细扣款Id',
	dataType int(4) NOT NULL COMMENT '扣款类型', -- 0 明细扣款 1 主单扣款
	seekState int(4) DEFAULT 0 COMMENT '查找状态',-- 0未匹配 1已匹配
	updateType int(4) DEFAULT 0 COMMENT '更新类型',-- 0序号更新 1规则更新
	repayType int(4)  DEFAULT NULL COMMENT '保险类型',-- 0 居保 1职保  主单扣款不区分职保 居保 默认值为NULL
	warnType int(4) DEFAULT 0 COMMENT '提醒状态',-- 1序号正常(一个 用序号匹配) 2新序号(一个 无序号匹配) 3新序号(多个 序号错误，无序号匹配) 4全无匹配 5异常匹配
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',  
  STATE int(4) DEFAULT 0 COMMENT '状态',-- 0上传 1一对多 2未知
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


DROP VIEW IF EXISTS yb_reconsider_verify_view;
create view yb_reconsider_verify_view
as
select	
	ad.serialNo,#交易流水号
	ad.billNo,#单据号
	ad.proposalCode,#意见书编码
	ad.projectCode,#项目编码
	ad.projectName,#项目名称
	ad.num,#数量
	ad.medicalPrice,#医保内金额
	ad.ruleName,#规则名称
	ad.deductPrice,#扣除金额
	ad.deductReason,#扣除原因
	ad.repaymentReason,#还款原因
	ad.doctorName,#医生姓名
	ad.deptCode,#科室编码
	ad.deptName,#科室名称
	ad.enterHospitalDate,#入院日期
	ad.outHospitalDate,#出院日期
	ad.costDate,#费用日期
	ad.enterHospitalDateStr,#入院日期Str
	ad.outHospitalDateStr,#出院日期Str
	ad.costDateStr,#费用日期Str
	ad.hospitalizedNo,#住院号
	ad.treatmentMode,#就医方式
	ad.settlementDate,#结算日期
	ad.settlementDateStr,#结算日期Str
	ad.personalNo,#个人编号
	ad.insuredName,#参保人姓名
	ad.cardNumber,#医保卡号
	ad.areaName,#统筹区名称
	ad.versionNumber,#版本号
	ad.backAppeal,#反馈申诉
	ad.typeno,	#版本类型
	ad.insuredType,
	ad.dataType,
	ad.orderNumber,
	ra.applyDate, #复议年月
	ra.applyDateStr, #复议年月Str
	ra.operatorId, #操作员代码'
  	ra.operatorName, #操作员名称,
  	case when rv.id is null then '00000000-0000-0000-0000-000000000000' else rv.id end id,
	ad.id applyDataId,
	rv.verifyDoctorCode,
	rv.verifyDoctorName,
	rv.verifyDeptCode,
	rv.verifyDeptName,
	rv.operateReason,
	rv.operateDate,
	rv.matchPersonId,#匹配人代码',
	rv.matchPersonName,#匹配人',
	rv.matchDate,#匹配日期', 
	rv.reviewerId,#审核人代码',
	rv.reviewerName,#审核人',
	rv.reviewerDate,#审核日期', 
	rv.sendPersonId,#发送人代码',
	rv.sendPersonName,#发送人',	
	rv.sendDate,#发送日期', 
	rv.COMMENTS,
	rv.STATE,
	rv.IS_DELETEMARK,
	rv.MODIFY_TIME,
	rv.CREATE_TIME,
	rv.CREATE_USER_ID,
	rv.MODIFY_USER_ID,
	rv.currencyField
from 
	yb_reconsider_apply_data ad 		
	INNER JOIN yb_reconsider_apply ra ON
		ra.id = ad.pid AND
		ra.IS_DELETEMARK = 1
  LEFT JOIN yb_reconsider_verify rv ON
    ad.id = rv.applyDataId AND
	rv.IS_DELETEMARK = 1
where
	ad.IS_DELETEMARK = 1;

DROP VIEW IF EXISTS yb_appeal_manage_view;
create view yb_appeal_manage_view
as
select	
	ad.serialNo,#交易流水号
	ad.billNo,#单据号
	ad.proposalCode,#意见书编码
	ad.projectCode,#项目编码
	ad.projectName,#项目名称
	ad.num,#数量
	ad.medicalPrice,#医保内金额
	ad.ruleName,#规则名称
	ad.deductPrice,#扣除金额
	ad.deductReason,#扣除原因
	ad.repaymentReason,#还款原因
	ad.doctorName,#医生姓名
	ad.deptCode,#科室编码
	ad.deptName,#科室名称
	ad.enterHospitalDate,#入院日期
	ad.outHospitalDate,#出院日期
	ad.costDate,#费用日期
	ad.enterHospitalDateStr,#入院日期Str
	ad.outHospitalDateStr,#出院日期Str
	ad.costDateStr,#费用日期Str
	ad.hospitalizedNo,#住院号
	ad.treatmentMode,#就医方式
	ad.settlementDate,#结算日期
	ad.settlementDateStr,#结算日期Str
	ad.personalNo,#个人编号
	ad.insuredName,#参保人姓名
	ad.cardNumber,#医保卡号
	ad.areaName,#统筹区名称
	ad.versionNumber,#版本号
	ad.backAppeal,#反馈申诉
	ad.typeno,	#版本类型
	ad.insuredType,
	ad.dataType,
	ad.orderNumber,
	ra.applyDate, #复议年月
	ra.applyDateStr, #复议年月Str
	ra.operatorId, #操作员代码'
  ra.operatorName, #操作员名称,
  am.id,
	ad.id applyDataId,
  am.verifyId,
	am.readyDoctorCode,
	am.readyDoctorName,
	am.readyDeptCode,
	am.readyDeptName,
	am.changeDoctorCode,#变更医生编码',
	am.changeDoctorName,#变更医生',
  am.changeDeptCode,#变更复议科室编码',
	am.changeDeptName,#变更复议科室',	
	am.operateReason,
	am.operateDate,
	am.acceptState,
	am.refuseId,#医保人代码',
	am.refuseName,#医保人',
	am.refuseReason,#拒绝理由',
	am.refuseDate,#拒绝日期',
	am.adminPersonId,#管理员代码',
	am.adminPersonName,#管理员',
	am.adminChangeDate,#更改日期', 
	am.adminReason,#管理员理由',
	am.enableDate,#可操作日期
	am.COMMENTS,
	am.STATE,
	am.IS_DELETEMARK,
	am.MODIFY_TIME,
	am.CREATE_TIME,
	am.CREATE_USER_ID,
	am.MODIFY_USER_ID,
	am.currencyField,
	am.sourceType,
	am.operateProcess,
	am.verifySendId,
	am.approvalState,
	case when (UNIX_TIMESTAMP(am.enableDate)-UNIX_TIMESTAMP(sysdate()))/(60*60*24) > 0 then 1 else 0 end enableType 
from 
	yb_reconsider_apply_data ad 		
	INNER JOIN yb_reconsider_apply ra ON
		ra.id = ad.pid AND
		ra.IS_DELETEMARK = 1
  INNER JOIN yb_appeal_manage am ON
am.applyDataId = ad.id And
		am.IS_DELETEMARK = 1
where
	ad.IS_DELETEMARK  = 1;

DROP VIEW IF EXISTS yb_appeal_result_view;
create view yb_appeal_result_view
as
select	
	ad.serialNo,#交易流水号
	ad.billNo,#单据号
	ad.proposalCode,#意见书编码
	ad.projectCode,#项目编码
	ad.projectName,#项目名称
	ad.num,#数量
	ad.medicalPrice,#医保内金额
	ad.ruleName,#规则名称
	ad.deductPrice,#扣除金额
	ad.deductReason,#扣除原因
	ad.repaymentReason,#还款原因
	ad.doctorName,#医生姓名
	ad.deptCode,#科室编码
	ad.deptName,#科室名称
	ad.enterHospitalDate,#入院日期
	ad.outHospitalDate,#出院日期
	ad.costDate,#费用日期
	ad.enterHospitalDateStr,#入院日期Str
	ad.outHospitalDateStr,#出院日期Str
	ad.costDateStr,#费用日期Str
	ad.hospitalizedNo,#住院号
	ad.treatmentMode,#就医方式
	ad.settlementDate,#结算日期
	ad.settlementDateStr,#结算日期Str
	ad.personalNo,#个人编号
	ad.insuredName,#参保人姓名
	ad.cardNumber,#医保卡号
	ad.areaName,#统筹区名称
	ad.versionNumber,#版本号
	ad.backAppeal,#反馈申诉
	ad.typeno,	#版本类型
	ad.insuredType,
	ad.dataType,
	ad.orderNumber,
	ra.applyDate, #复议年月
	ra.applyDateStr, #复议年月Str
	ra.operatorId, #操作员代码'
  ra.operatorName, #操作员名称,
	ra.resetState raResetState,
	ra.resultState raResultState,
	ra.repayState raRepayState,
  art.id,
	art.applyDataId,
  art.verifyId,
	art.doctorCode ar_doctorCode,
	art.doctorName ar_doctorName,
	art.deptCode ar_deptCode,
	art.deptName ar_deptName,
	art.operateReason,
	art.operateDate,
	art.COMMENTS,
	art.STATE,
	art.IS_DELETEMARK,
	art.MODIFY_TIME,
	art.CREATE_TIME,
	art.CREATE_USER_ID,
	art.MODIFY_USER_ID,
	art.sourceType,
	art.resetDataId,
	art.repayState,
	art.currencyField 
from 
	yb_reconsider_apply_data ad 		
	INNER JOIN yb_reconsider_apply ra ON
		ra.id = ad.pid AND
		ra.IS_DELETEMARK = 1
  INNER JOIN yb_appeal_result art ON
art.applyDataId = ad.id AND
		art.IS_DELETEMARK = 1
where
    ad.IS_DELETEMARK = 1;



DROP VIEW IF EXISTS yb_reconsider_reset_data_view;
create view yb_reconsider_reset_data_view
as
SELECT
	yb_reconsider_reset_data.id,-- ID
	yb_reconsider_reset_data.serialNo ,-- 交易流水号
	yb_reconsider_reset_data.billNo ,-- 单据号
	yb_reconsider_reset_data.projectCode ,-- 项目编码
	yb_reconsider_reset_data.projectName ,-- 项目名称
	yb_reconsider_reset_data.medicalPrice ,-- 医保内金额
	yb_reconsider_reset_data.ruleName ,-- 规则名称
	yb_reconsider_reset_data.deductPrice ,-- 扣除金额
	yb_reconsider_reset_data.deductReason ,-- 扣除原因
	yb_reconsider_reset_data.repaymentReason ,-- 还款原因
	yb_reconsider_reset_data.doctorName ,-- 医生姓名
	yb_reconsider_reset_data.deptCode ,-- 科室编码
	yb_reconsider_reset_data.deptName ,-- 科室名称
	yb_reconsider_reset_data.costDate ,-- 费用日期
  yb_reconsider_reset_data.costDateStr ,-- 费用日期str
	yb_reconsider_reset_data.hospitalizedNo ,-- 住院号
	yb_reconsider_reset_data.treatmentMode ,-- 就医方式
	yb_reconsider_reset_data.settlementDate ,-- 结算日期
  yb_reconsider_reset_data.settlementDateStr ,-- 结算日期Str
	yb_reconsider_reset_data.personalNo ,-- 个人编号
	yb_reconsider_reset_data.insuredName ,-- 参保人姓名
	yb_reconsider_reset_data.cardNumber ,-- 医保卡号
	yb_reconsider_reset_data.areaName ,-- 统筹区名称
	yb_reconsider_reset_data.insuredType,-- 参保类型
	yb_reconsider_reset_data.dataType,-- 数据类型
	yb_reconsider_reset_data.orderNumber,-- 序号
	yb_reconsider_reset_data.repaymentPrice,-- 还款金额
	yb_reconsider_reset_data.STATE, -- 状态
	yb_reconsider_reset_data.seekState,-- 查找状态
	yb_reconsider_reset.id resetId, -- 主表Id
	yb_reconsider_reset.applyDate,  -- 日期
	yb_reconsider_reset.applyDateStr,-- 年月 
	yb_reconsider_reset.currencyField -- 通用
FROM
	yb_reconsider_reset_data
	INNER JOIN yb_reconsider_reset ON
		yb_reconsider_reset_data.pid = yb_reconsider_reset.id AND
		yb_reconsider_reset.IS_DELETEMARK = 1
WHERE
		yb_reconsider_reset_data.IS_DELETEMARK = 1;

DROP VIEW IF EXISTS yb_handle_verify_data_view;
create view yb_handle_verify_data_view
as
select	
	ad.serialNo,#交易流水号
	ad.billNo,#单据号
	ad.proposalCode,#意见书编码
	ad.projectCode,#项目编码
	ad.projectName,#项目名称
	ad.num,#数量
	ad.medicalPrice,#医保内金额
	ad.ruleName,#规则名称
	ad.deductPrice,#扣除金额
	ad.deductReason,#扣除原因
	ad.repaymentReason,#还款原因
	ad.doctorName,#医生姓名
	ad.deptCode,#科室编码
	ad.deptName,#科室名称
	ad.enterHospitalDate,#入院日期
	ad.outHospitalDate,#出院日期
	ad.costDate,#费用日期
	ad.enterHospitalDateStr,#入院日期Str
	ad.outHospitalDateStr,#出院日期Str
	ad.costDateStr,#费用日期Str
	ad.hospitalizedNo,#住院号
	ad.treatmentMode,#就医方式
	ad.settlementDate,#结算日期
	ad.settlementDateStr,#结算日期Str
	ad.personalNo,#个人编号
	ad.insuredName,#参保人姓名
	ad.cardNumber,#医保卡号
	ad.areaName,#统筹区名称
	ad.versionNumber,#版本号
	ad.backAppeal,#反馈申诉
	ad.typeno,	#版本类型
	ad.insuredType,
	ad.dataType,
	ad.orderNumber,
	hv.applyDate, #复议年月
	hv.applyDateStr, #复议年月Str
  hvd.id,
	hvd.pid,
	hvd.applyDataId,
  hvd.verifyId,
	hvd.manageId,
	hvd.resetId,
	hvd.resultId,
	hvd.doctorCode hv_doctorCode,
	hvd.doctorName hv_doctorName,
	hvd.deptCode hv_deptCode,
	hvd.deptName hv_deptName,
	hvd.operateReason,
	hvd.operateDate,
	hvd.STATE,
	hvd.IS_DELETEMARK,
	hvd.currencyField
from 
	yb_handle_verify_data hvd
	INNER JOIN yb_handle_verify hv ON
		hv.id = hvd.pid AND
		hv.IS_DELETEMARK = 1
	INNER JOIN yb_reconsider_apply_data ad ON
		ad.id = hvd.applyDataId AND
		ad.IS_DELETEMARK = 1
WHERE
	hvd.IS_DELETEMARK = 1;

DROP VIEW IF EXISTS yb_reconsider_reset_result_view;
create view yb_reconsider_reset_result_view
as
SELECT
	yb_reconsider_reset_data.id,-- ID
	yb_reconsider_reset_data.serialNo ,-- 交易流水号
	yb_reconsider_reset_data.billNo ,-- 单据号
	yb_reconsider_reset_data.projectCode ,-- 项目编码
	yb_reconsider_reset_data.projectName ,-- 项目名称
	yb_reconsider_reset_data.medicalPrice ,-- 医保内金额
	yb_reconsider_reset_data.ruleName ,-- 规则名称
	yb_reconsider_reset_data.deductPrice ,-- 扣除金额
	yb_reconsider_reset_data.deductReason ,-- 扣除原因
	yb_reconsider_reset_data.repaymentReason ,-- 还款原因
	yb_reconsider_reset_data.doctorName ,-- 医生姓名
	yb_reconsider_reset_data.deptCode ,-- 科室编码
	yb_reconsider_reset_data.deptName ,-- 科室名称
	yb_reconsider_reset_data.costDate ,-- 费用日期
  yb_reconsider_reset_data.costDateStr ,-- 费用日期str
	yb_reconsider_reset_data.hospitalizedNo ,-- 住院号
	yb_reconsider_reset_data.treatmentMode ,-- 就医方式
	yb_reconsider_reset_data.settlementDate ,-- 结算日期
  yb_reconsider_reset_data.settlementDateStr ,-- 结算日期Str
	yb_reconsider_reset_data.personalNo ,-- 个人编号
	yb_reconsider_reset_data.insuredName ,-- 参保人姓名
	yb_reconsider_reset_data.cardNumber ,-- 医保卡号
	yb_reconsider_reset_data.areaName ,-- 统筹区名称
	yb_reconsider_reset_data.insuredType,-- 参保类型
	yb_reconsider_reset_data.dataType,-- 数据类型
	yb_reconsider_reset_data.orderNumber,-- 序号
	yb_reconsider_reset_data.repaymentPrice ,-- 还款金额
	yb_reconsider_reset_data.STATE, -- 状态
	yb_reconsider_reset_data.seekState,-- 查找状态
	yb_reconsider_reset.id resetId, -- 主表Id
	yb_reconsider_reset.applyDate,  -- 日期
	yb_reconsider_reset.applyDateStr,-- 年月 
	yb_reconsider_reset.currencyField, -- 通用
	yb_appeal_result.doctorCode ar_doctorCode,
	yb_appeal_result.doctorName ar_doctorName,
	yb_appeal_result.deptCode ar_deptCode,
	yb_appeal_result.deptName ar_deptName,
	yb_appeal_result.applyDataId,
	yb_appeal_result.id resultId,
	yb_appeal_result.resetDate,
	yb_appeal_result.resetPersonId,
	yb_appeal_result.resetPersonName,
	yb_appeal_result.repayState
FROM
	yb_reconsider_reset_data
	INNER JOIN yb_reconsider_reset ON
		yb_reconsider_reset_data.pid = yb_reconsider_reset.id AND
		yb_reconsider_reset.IS_DELETEMARK = 1
	INNER JOIN yb_appeal_result ON
		yb_appeal_result.IS_DELETEMARK = 1 AND
		yb_appeal_result.state = 2 AND
		yb_appeal_result.resetDataId = yb_reconsider_reset_data.id AND
		yb_appeal_result.sourceType = 0 AND
		yb_appeal_result.resetDataId IS NOT NULL
WHERE
		yb_reconsider_reset_data.IS_DELETEMARK = 1;

DROP VIEW IF EXISTS yb_appeal_result_report_view;
create view yb_appeal_result_report_view
as
select	
	ad.serialNo,#交易流水号
	ad.billNo,#单据号
	ad.proposalCode,#意见书编码
	ad.projectCode,#项目编码
	ad.projectName,#项目名称
	ad.num,#数量
	ad.medicalPrice,#医保内金额
	ad.ruleName,#规则名称
	ad.deductPrice,#扣除金额
	ad.deductReason,#扣除原因
	ad.repaymentReason,#还款原因
	ad.doctorName,#医生姓名
	ad.deptCode,#科室编码
	ad.deptName,#科室名称
	ad.enterHospitalDate,#入院日期
	ad.outHospitalDate,#出院日期
	ad.costDate,#费用日期
	ad.enterHospitalDateStr,#入院日期Str
	ad.outHospitalDateStr,#出院日期Str
	ad.costDateStr,#费用日期Str
	ad.hospitalizedNo,#住院号
	ad.treatmentMode,#就医方式
	ad.settlementDate,#结算日期
	ad.settlementDateStr,#结算日期Str
	ad.personalNo,#个人编号
	ad.insuredName,#参保人姓名
	ad.cardNumber,#医保卡号
	ad.areaName,#统筹区名称
	ad.versionNumber,#版本号
	ad.backAppeal,#反馈申诉
	ad.typeno,	#版本类型
	ad.insuredType,
	ad.dataType,
	ad.orderNumber,
	ra.applyDate, #复议年月
	ra.applyDateStr, #复议年月Str
	ra.operatorId, #操作员代码'
  ra.operatorName, #操作员名称,
	ra.resetState raResetState,
	ra.resultState raResultState,
	ra.repayState raRepayState,
  art.id,
	art.manageId,
	art.applyDataId,
  art.verifyId,
	art.doctorCode ar_doctorCode,
	art.doctorName ar_doctorName,
	art.deptCode ar_deptCode,
	art.deptName ar_deptName,
	art.operateReason,
	art.operateDate,
	art.COMMENTS,
	art.STATE,
	art.IS_DELETEMARK,
	art.MODIFY_TIME,
	art.CREATE_TIME,
	art.CREATE_USER_ID,
	art.MODIFY_USER_ID,
	art.sourceType,
	art.resetDataId,
	art.repayState,
	art.currencyField,
	resetDate.repaymentPrice,
	CASE WHEN art.state = 1 or (art.state = 2 and (art.repayState = 1 or art.repayState = 3)) THEN 1 
	WHEN art.state = 2 and art.repayState = 2 THEN 0 ELSE 2 END isSuccess,
	CASE WHEN art.state = 1 THEN ad.deductPrice WHEN ad.deductPrice = resetDate.deductPrice 
	THEN 0 ELSE ad.deductPrice - resetDate.deductPrice  END adjustPrice
from 
	yb_reconsider_apply_data ad 		
	INNER JOIN yb_reconsider_apply ra ON
		ra.id = ad.pid AND
		ra.resetState = 1 AND
		ra.IS_DELETEMARK = 1
  INNER JOIN yb_appeal_result art ON
		art.applyDataId = ad.id AND
		art.IS_DELETEMARK = 1
	LEFT JOIN (
		SELECT 
			yb_reconsider_reset.applyDateStr, 
			yb_reconsider_reset_data.id,
			yb_reconsider_reset_data.repaymentPrice,
			yb_reconsider_reset_data.deductPrice
		FROM 
			yb_reconsider_reset_data
		INNER JOIN yb_reconsider_reset ON
			yb_reconsider_reset_data.pid = yb_reconsider_reset.id	AND 
			yb_reconsider_reset.IS_DELETEMARK = 1
		WHERE
			yb_reconsider_reset_data.seekState = 1 AND 
			yb_reconsider_reset_data.IS_DELETEMARK = 1
	) resetDate ON resetDate.id = art.resetDataId AND resetDate.applyDateStr = ra.applyDateStr
where
    ad.IS_DELETEMARK = 1;

DROP VIEW IF EXISTS yb_appeal_result_deductImplement_view;
create view yb_appeal_result_deductImplement_view
as
select	
	ad.serialNo,#交易流水号
	ad.billNo,#单据号
	ad.proposalCode,#意见书编码
	ad.projectCode,#项目编码
	ad.projectName,#项目名称
	ad.num,#数量
	ad.medicalPrice,#医保内金额
	ad.ruleName,#规则名称
	ad.deductPrice,#扣除金额
	ad.deductReason,#扣除原因
	ad.repaymentReason,#还款原因
	ad.doctorName,#医生姓名
	ad.deptCode,#科室编码
	ad.deptName,#科室名称
	ad.enterHospitalDate,#入院日期
	ad.outHospitalDate,#出院日期
	ad.costDate,#费用日期
	ad.enterHospitalDateStr,#入院日期Str
	ad.outHospitalDateStr,#出院日期Str
	ad.costDateStr,#费用日期Str
	ad.hospitalizedNo,#住院号
	ad.treatmentMode,#就医方式
	ad.settlementDate,#结算日期
	ad.settlementDateStr,#结算日期Str
	ad.personalNo,#个人编号
	ad.insuredName,#参保人姓名
	ad.cardNumber,#医保卡号
	ad.areaName,#统筹区名称
	ad.versionNumber,#版本号
	ad.backAppeal,#反馈申诉
	ad.typeno,	#版本类型
	ad.insuredType,
	ad.dataType,
	ad.orderNumber,
	ra.applyDate, #复议年月
	ra.applyDateStr, #复议年月Str
	ra.operatorId, #操作员代码'
  ra.operatorName, #操作员名称,
	ra.resetState raResetState,
	ra.resultState raResultState,
	ra.repayState raRepayState,
  art.id,
	art.manageId,
	art.applyDataId,
  art.verifyId,
	art.doctorCode ar_doctorCode,
	art.doctorName ar_doctorName,
	art.deptCode ar_deptCode,
	art.deptName ar_deptName,
	art.operateReason,
	art.operateDate,
	art.COMMENTS,
	art.STATE,
	art.IS_DELETEMARK,
	art.MODIFY_TIME,
	art.CREATE_TIME,
	art.CREATE_USER_ID,
	art.MODIFY_USER_ID,
	art.sourceType,
	art.resetDataId,
	art.repayState,
	art.currencyField,
	ardi.implementDate, #'绩效年月'
	ardi.implementDateStr, #'绩效年月Str'
	ardi.shareState, #'分摊方式'  # 0 个人 1科室
	ardi.shareProgramme,
	ardi.id deductImplementId
from 
	yb_reconsider_apply_data ad 		
	INNER JOIN yb_reconsider_apply ra ON
		ra.id = ad.pid AND
		ra.resetState = 1 AND
		ra.IS_DELETEMARK = 1
  INNER JOIN yb_appeal_result art ON
		art.applyDataId = ad.id AND
		art.state = 2 AND
		art.sourceType = 0 AND
		art.IS_DELETEMARK = 1
	LEFT JOIN yb_appeal_result_deductImplement ardi ON
		ardi.resultId = art.id
where
    ad.IS_DELETEMARK = 1;


DROP PROCEDURE IF EXISTS p_appeal_manage_enableOverdue;

CREATE PROCEDURE p_appeal_manage_enableOverdue()
begin
    declare m_id char(36);
		declare m_acceptState int;
		declare t_update INTEGER DEFAULT 0; 
		declare t_error INTEGER DEFAULT 0;    
		declare done int default 0;
    -- 声明游标
    declare mc cursor for select id,acceptState from yb_appeal_manage where IS_DELETEMARK = 1 AND enableDate <= now() and acceptState in(0,1);
    declare continue handler for not found set done = 1;
		declare CONTINUE HANDLER FOR SQLEXCEPTION SET t_error=1; 
				
		START TRANSACTION;
    -- 打开游标
    open mc;
		
		-- 循环
		itemloop: loop
    -- 获取结果
    fetch mc into m_id,m_acceptState;
		
			if done = 1 then
					leave itemloop;
			end if;
		
			update yb_appeal_manage 
			set 
				acceptState= 7,
				operateReason='过期',
				operateDate=now(),
				operateProcess= case when m_acceptState=0 then '接受申请-过期' else '待申诉-过期' end		
			where 
				id = m_id;
				
			insert into yb_appeal_result
			(
				id,applyDataId,verifyId,manageId,doctorCode,doctorName,deptCode,deptName,
				operateReason,operateDate,state,sourceType,dataType,repayState,
				IS_DELETEMARK,CREATE_TIME
			)
			SELECT
				id,
				applyDataId,
				verifyId,
				id,
				readyDoctorCode,
				readyDoctorName,
				readyDeptCode,
				readyDeptName,
				'过期',
				now(),
				1,
				sourceType,
				dataType,
				1,
				1,
				now()
			FROM
				yb_appeal_manage 
			WHERE
				id = m_id;
			
			
			set t_update = 1;

		end loop;
    -- 关闭游标
    close mc;
		
		IF t_error = 1 THEN    
				ROLLBACK;    
		ELSE    
				if t_update = 1 then
					COMMIT;
				else
					select '无更新条数'; 
				end if;
		END IF;
   select t_error; 
end


DROP EVENT IF EXISTS e_appeal_manage_enableOverdue;
CREATE EVENT e_appeal_manage_enableOverdue
-- 当前时间 1天更新一次事件
-- ON SCHEDULE EVERY 1 DAY 
-- 以下 第二天 凌晨 1 点开始执行事件
ON SCHEDULE EVERY 1 DAY STARTS DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 1 DAY), INTERVAL 1 HOUR)
DO call p_appeal_manage_enableOverdue();

-- SELECT @@event_scheduler;-- 查看 事件是否启用

-- 开启事件
-- SET GLOBAL event_scheduler = ON;
-- SET GLOBAL event_scheduler = 1;

-- 关闭事件
-- SET GLOBAL event_scheduler = OFF;
-- SET GLOBAL event_scheduler = 0;

-- 关闭定时任务
-- ALTER EVENT 事件名 DISABLE;

-- 开启定时任务
-- ALTER EVENT 事件名 ENABLE;

-- 删除计划任务
-- DROP event 事件名;

#DROP TABLE IF EXISTS yb_dept;
CREATE TABLE yb_dept (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '部门Id',
  deptCode varchar(255) NOT NULL COMMENT '部门编码',
  deptName varchar(255) NOT NULL COMMENT '部门名称',  
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#DROP TABLE IF EXISTS yb_person;
CREATE TABLE yb_person (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '人员Id',
  personCode varchar(255) NOT NULL COMMENT '人员编码',
  personName varchar(255) NOT NULL COMMENT '人员名称',  
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS yb_reconsider_reset_main;
CREATE TABLE yb_reconsider_reset_main (
  id char(36) NOT NULL COMMENT '剔除表主单扣款',
	pid char(36) NOT NULL COMMENT '关联',
  serialNo varchar(100) NOT NULL COMMENT '交易流水号',
	billNo varchar(50) NOT NULL COMMENT '单据号',
	medicalPrice decimal(17,4) DEFAULT NULL COMMENT '医保内金额',
	ruleName varchar(50) DEFAULT NULL COMMENT '规则名称',
	deductPrice decimal(17,4) DEFAULT NULL COMMENT '扣除金额',
	hospitalizedNo varchar(50) DEFAULT NULL COMMENT '住院号',
	treatmentMode varchar(50) DEFAULT NULL COMMENT '就医方式',
	settlementDate datetime DEFAULT NULL COMMENT '结算日期',
  settlementDateStr varchar(50) DEFAULT NULL COMMENT '结算日期Str',
	personalNo varchar(50) DEFAULT NULL COMMENT '个人编号',
	insuredName varchar(50) DEFAULT NULL COMMENT '参保人姓名',
	insuredType varchar(50) DEFAULT NULL COMMENT '参保类型',
	areaName varchar(50) DEFAULT NULL COMMENT '统筹区名称',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


交易流水号	
单据号	
项目编码	
项目名称	
医保内金额	
规则名称	
扣除金额	
扣除原因	
还款原因	
医生姓名	
科室编码	
科室名称	
费用日期	
住院号	
就医方式	
结算日期	
个人编号	
参保人姓名	
统筹区名称

医院编号	
医院名称	
所属期	
序号	
单据号	
费用日期	
就医方式	
个人编号	
参保人姓名	
项目名称	
扣除金额	
还款金额
