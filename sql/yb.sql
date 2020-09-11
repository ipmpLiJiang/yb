DROP TABLE IF EXISTS yb_b_reconsider;
CREATE TABLE yb_b_reconsider (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '规则编号',
	rno int(11) NOT NULL COMMENT '序号',
  rdescribe varchar(255) NOT NULL COMMENT '规则描述',
  rxplain varchar(255) NOT NULL COMMENT '规则解释',
  rkeypoints varchar(255) NOT NULL COMMENT '复议重点',
	rmaterials varchar(2000) NOT NULL COMMENT '复议资料',
	operatorid bigint(11) NOT NULL COMMENT '操作员代码',
	operatorname varchar(50) NOT NULL COMMENT '操作员名称',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS yb_reconsider_apply;
CREATE TABLE yb_reconsider_apply (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '规则编号',
  applydate datetime NOT NULL COMMENT '复议年月',
  operatorid bigint(11) NOT NULL COMMENT '操作员代码',
  operatorname varchar(50) NOT NULL COMMENT '操作员名称',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS yb_reconsider_apply_data
CREATE TABLE yb_reconsider_apply_data (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '申请数据明细',
	pid int(11) NOT NULL COMMENT '申请编号',
  serialNo varchar(100) NOT NULL COMMENT '交易流水号',
	billNo varchar(50) NOT NULL COMMENT '单据号',
	proposalCode varchar(50) NOT NULL COMMENT '意见书编码',
	projectCode varchar(100) NOT NULL COMMENT '项目编码',
	projectName varchar(200) NOT NULL COMMENT '项目名称',
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
	typeno int(4) NOT NULL COMMENT '版本类型',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT NULL COMMENT '状态',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


#DROP TABLE IF EXISTS yb_reconsider_verify
CREATE TABLE yb_reconsider_verify (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '核对申请',
	applyDataId int(11) NOT NULL COMMENT '复议申请明细',  
	verifyDoctorCode varchar(50) NOT NULL COMMENT '参考复议医生编码',
	verifyDoctorName varchar(50) NOT NULL COMMENT '参考复议医生',
  verifyDeptCode varchar(100) NOT NULL COMMENT '参考复议科室编码',
	verifyDeptName varchar(100) NOT NULL COMMENT '参考复议科室',	
	changVerifyDoctorCode varchar(50) DEFAULT NULL COMMENT '变更复议医生编码',
	changVerifyDoctorName varchar(50) DEFAULT NULL COMMENT '变更复议医生',
  changVerifyDeptCode varchar(100) DEFAULT NULL COMMENT '变更复议科室编码',
	changVerifyDeptName varchar(100) DEFAULT NULL COMMENT '变更复议科室',	
	acceptState int(4) DEFAULT 0 COMMENT '接受状态',#0待接收1接受，不接受
	refuseReason varchar(1000) DEFAULT NULL COMMENT '拒绝理由',
	refuseDate datetime DEFAULT NULL COMMENT '拒绝日期', 
	matchPersonId bigint(11) NOT NULL COMMENT '匹配人代码',
	matchPersonName varchar(50) NOT NULL COMMENT '匹配人',
	matchDate datetime DEFAULT NULL COMMENT '匹配日期', 
	reviewerId bigint(11) NOT NULL COMMENT '审核人代码',
	reviewerName varchar(50) NOT NULL COMMENT '审核人',
	reviewerDate datetime DEFAULT NULL COMMENT '审核日期', 
	sendPersonId bigint(11) NOT NULL COMMENT '发送人代码',
	sendPersonName varchar(50) NOT NULL COMMENT '发送人',	
	sendDate datetime DEFAULT NULL COMMENT '发送日期', 
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '备注',
  STATE int(4) DEFAULT 1 COMMENT '状态',#1待审核、2已审核、3已发送
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '是否删除',
  MODIFY_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  CREATE_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '创建人',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

create view v_reconsider_verify_data
as
select
	rv.id,
	rv.applyDataId,
	rv.verifyDoctorCode,
	rv.verifyDoctorName,
	rv.verifyDeptCode,
	rv.verifyDeptName,
	rv.changVerifyDoctorCode,
	rv.changVerifyDoctorName,
	rv.changVerifyDeptCode,
	rv.changVerifyDeptName,
	rv.acceptState,
	rv.refuseReason,
	rv.refuseDate,
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
	ad.hospitalizedNo,#住院号
	ad.treatmentMode,#就医方式
	ad.settlementDate,#结算日期
	ad.personalNo,#个人编号
	ad.insuredName,#参保人姓名
	ad.cardNumber,#医保卡号
	ad.areaName,#统筹区名称
	ad.versionNumber,#版本号
	ad.backAppeal,#反馈申诉
	ad.typeno,	#版本类型
	ra.applydate, #复议年月
	ra.operatorid, #操作员代码'
  ra.operatorname #操作员名称,
from 
	yb_reconsider_verify rv 
	INNER JOIN yb_reconsider_apply_data ad ON
		ad.id = rv.applyDataId
	INNER JOIN yb_reconsider_apply ra ON
		ra.id = ad.pid


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

