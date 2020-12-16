#�������
DROP TABLE IF EXISTS yb_reconsider_rule;
CREATE TABLE yb_reconsider_rule (
  id char(36) NOT NULL COMMENT '�������id',
	rNo int(11) NOT NULL COMMENT '���',
  rDescribe varchar(255) NOT NULL COMMENT '��������',
  rXplain varchar(255) NOT NULL COMMENT '�������',
  rkeypoints varchar(255) NOT NULL COMMENT '�����ص�',
	rmaterials varchar(2000) NOT NULL COMMENT '��������',
	operatorId bigint(11) NOT NULL COMMENT '����Ա����',
	operatorName varchar(50) NOT NULL COMMENT '����Ա����',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#������Ϣ
DROP TABLE IF EXISTS com_sms;
CREATE TABLE com_sms (
  id char(36) NOT NULL COMMENT '��ϢID',
	sendcode varchar(50) NOT NULL COMMENT '�����˻�', 
	sendname varchar(100) NOT NULL COMMENT '������', 
	mobile varchar(11) NOT NULL COMMENT '�ֻ�����', 
	sendType int(4) NOT NULL COMMENT '��������', #1 �˶Է��� 2�˹����鷢�� 3������ 4����Ա���
  operatorId bigint(11) NOT NULL COMMENT '����Ա����',
  operatorName varchar(50) NOT NULL COMMENT '����Ա����',
	sendcontent varchar(100) NOT NULL COMMENT '��������', 
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT 0 COMMENT '״̬',#0 δ���� 1�ѷ���
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


#�������ȼ�
DROP TABLE IF EXISTS yb_reconsider_priority_level;
CREATE TABLE yb_reconsider_priority_level (
  id char(36) NOT NULL COMMENT '�������id',
  rplName varchar(100) DEFAULT NULL COMMENT '��������',
  doctorCode varchar(50) DEFAULT NULL COMMENT 'ҽ������',  
	doctorName varchar(50) DEFAULT NULL COMMENT 'ҽ������',
	deptCode varchar(100) NOT NULL COMMENT '���ұ���',
	deptName varchar(100) NOT NULL COMMENT '��������',
	operatorId bigint(11) NOT NULL COMMENT '����Ա����',
	operatorName varchar(50) NOT NULL COMMENT '����Ա����',
currencyField varchar(50) DEFAULT NULL COMMENT 'ͨ���ֶ�',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬', #1���� 2��Ŀ 3����
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#�������������¼
DROP TABLE IF EXISTS yb_reconsider_apply_task;
CREATE TABLE yb_reconsider_apply_task (
  id char(36) NOT NULL COMMENT '�������������¼',
  applyDateStr varchar(10) NOT NULL COMMENT '��������Str',  
  dataType int(4) DEFAULT 0 COMMENT '��������',	
  typeno int(4) NOT NULL COMMENT '�汾����',
  startNum int(5) DEFAULT 0 COMMENT '��ʼ��', 
  endNum int(5) DEFAULT 0 COMMENT '������', 	
	totalRow int(5) DEFAULT 0 COMMENT '����', 
	currentPage int(5) DEFAULT 0 COMMENT '��ǰҳ', 	
	pageSize int(5) DEFAULT 0 COMMENT 'ҳ��', 	
	totalPage int(5) DEFAULT 0 COMMENT '��ҳ��', 	
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT 0 COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#��������
DROP TABLE IF EXISTS yb_reconsider_apply;
CREATE TABLE yb_reconsider_apply (
  id char(36) NOT NULL COMMENT '������',
  applyDate datetime NOT NULL COMMENT '��������',
  applyDateStr varchar(10) NOT NULL COMMENT '��������Str',
  operatorId bigint(11) NOT NULL COMMENT '����Ա����',
  operatorName varchar(50) NOT NULL COMMENT '����Ա����',
  uploadFileNameOne varchar(100) DEFAULT NULL COMMENT '���һ�ϴ�����',
  uploadFileNameTwo varchar(100) DEFAULT NULL COMMENT '��˶��ϴ�����',
  endDateOne datetime DEFAULT NULL COMMENT '���һ����ʱ��',
  endDateTwo datetime DEFAULT NULL COMMENT '��˶�����ʱ��',
  resultState int(4) DEFAULT 0 COMMENT '����״̬',#0δ���� 1������
  repayState int(4) DEFAULT 0 COMMENT '����״̬', #0δ���� 1�ѻ���
  resetState int(4) DEFAULT 0 COMMENT '�޳�״̬', #0δ�޳� 1���޳�
  taskState int(4) DEFAULT 0 COMMENT '����״̬', #0û������ 1���һ���� 2��˶�����	
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT 1 COMMENT '״̬',#1������ 2�ϴ�һ 3����һ 4�ϴ��� 5������ 6���޳� 7������
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


#����������ϸ
DROP TABLE IF EXISTS yb_reconsider_apply_data;
CREATE TABLE yb_reconsider_apply_data (
  id char(36) NOT NULL COMMENT '����������ϸ',
	orderNumber varchar(50) NOT NULLL COMMENT '���',
	pid char(36) NOT NULL COMMENT '������',
  serialNo varchar(100) NOT NULL COMMENT '������ˮ��',
	billNo varchar(50) NOT NULL COMMENT '���ݺ�',
	proposalCode varchar(50) NOT NULL COMMENT '��������',
	projectCode varchar(100) DEFAULT NULL COMMENT '��Ŀ����',
	projectName varchar(200) DEFAULT NULL COMMENT '��Ŀ����',
	num decimal(12,4) DEFAULT NULL COMMENT '����',
	medicalPrice decimal(17,4) DEFAULT NULL COMMENT 'ҽ���ڽ��',
	ruleName varchar(50) DEFAULT NULL COMMENT '��������',
	deductPrice decimal(17,4) DEFAULT NULL COMMENT '�۳����',
	deductReason varchar(1000) DEFAULT NULL COMMENT '�۳�ԭ��',
	repaymentReason varchar(1000) DEFAULT NULL COMMENT '����ԭ��',
	doctorName varchar(50) DEFAULT NULL COMMENT 'ҽ������',
	deptCode varchar(100) DEFAULT NULL COMMENT '���ұ���',
	deptName varchar(100) DEFAULT NULL COMMENT '��������',
	enterHospitalDate datetime DEFAULT NULL COMMENT '��Ժ����',
  enterHospitalDateStr varchar(50) DEFAULT NULL COMMENT '��Ժ����str',
	outHospitalDate datetime DEFAULT NULL COMMENT '��Ժ����',
	outHospitalDateStr varchar(50) DEFAULT NULL COMMENT '��Ժ����str',
	costDate datetime DEFAULT NULL COMMENT '��������',
  costDateStr varchar(50) DEFAULT NULL COMMENT '��������str',
	hospitalizedNo varchar(50) DEFAULT NULL COMMENT 'סԺ��',
	treatmentMode varchar(50) DEFAULT NULL COMMENT '��ҽ��ʽ',
	settlementDate datetime DEFAULT NULL COMMENT '��������',
  settlementDateStr varchar(50) DEFAULT NULL COMMENT '��������Str',
	personalNo varchar(50) DEFAULT NULL COMMENT '���˱��',
	insuredName varchar(50) DEFAULT NULL COMMENT '�α�������',
	cardNumber varchar(50) DEFAULT NULL COMMENT 'ҽ������',
	areaName varchar(50) DEFAULT NULL COMMENT 'ͳ��������',
	versionNumber varchar(50) DEFAULT NULL COMMENT '�汾��',
	backAppeal varchar(1000) DEFAULT NULL COMMENT '��������',
	insuredType varchar(50) DEFAULT NULL COMMENT '�α�����',
	orderNum int(4) NOT NULL COMMENT '����',
	orderSettlementNum int(4) NOT NULL COMMENT '��������',
	typeno int(4) NOT NULL COMMENT '�汾����',
	dataType int(4) NOT NULL COMMENT '�ۿ�����', -- 0 ��ϸ�ۿ� 1 �����ۿ�
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


#�˶�����
#DROP TABLE IF EXISTS yb_reconsider_verify;
CREATE TABLE yb_reconsider_verify (
  id char(36) NOT NULL COMMENT '�˶�����',
	applyDataId char(36) NOT NULL COMMENT '����������ϸ',  
	verifyDoctorCode varchar(50) NOT NULL COMMENT '�ο�����ҽ������',
	verifyDoctorName varchar(50) NOT NULL COMMENT '�ο�����ҽ��',
  verifyDeptCode varchar(100) NOT NULL COMMENT '�ο�������ұ���',
	verifyDeptName varchar(100) NOT NULL COMMENT '�ο��������',
	operateReason varchar(1000) DEFAULT NULL COMMENT '��������',
	operateDate datetime DEFAULT NULL COMMENT '��������',
	matchPersonId bigint(11) DEFAULT NULL COMMENT 'ƥ���˴���',
	matchPersonName varchar(50) DEFAULT NULL COMMENT 'ƥ����',
	matchDate datetime DEFAULT NULL COMMENT 'ƥ������', 
	reviewerId bigint(11) DEFAULT NULL COMMENT '����˴���',
	reviewerName varchar(50) DEFAULT NULL COMMENT '�����',
	reviewerDate datetime DEFAULT NULL COMMENT '�������', 
	sendPersonId bigint(11) DEFAULT NULL COMMENT '�����˴���',
	sendPersonName varchar(50) DEFAULT NULL COMMENT '������',	
	sendDate datetime DEFAULT NULL COMMENT '��������', 
	currencyField varchar(50) DEFAULT NULL COMMENT 'ͨ��',
dataType int(4) NOT NULL COMMENT '�ۿ�����', -- 0 ��ϸ�ۿ� 1 �����ۿ�
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT 1 COMMENT '״̬',#1����ˡ�2����ˡ�3�ѷ���
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;



#���߹���
#DROP TABLE IF EXISTS yb_appeal_manage;
CREATE TABLE yb_appeal_manage (
  id char(36) NOT NULL COMMENT '���߹���',
	applyDataId char(36) NOT NULL COMMENT '����������ϸ',
	verifyId  char(36) NOT NULL COMMENT '�˶�Id',
	verifySendId  char(36) NOT NULL COMMENT '���͹���Id',
	readyDoctorCode varchar(50) NOT NULL COMMENT '����ҽ������',
	readyDoctorName varchar(50) NOT NULL COMMENT '����ҽ��',
  	readyDeptCode varchar(100) NOT NULL COMMENT '������ұ���',
	readyDeptName varchar(100) NOT NULL COMMENT '�������',
	changeDoctorCode varchar(50) DEFAULT NULL COMMENT '���ҽ������',
	changeDoctorName varchar(50) DEFAULT NULL COMMENT '���ҽ��',
  changeDeptCode varchar(100) DEFAULT NULL COMMENT '���������ұ���',
	changeDeptName varchar(100) DEFAULT NULL COMMENT '����������',	
	acceptState int(4) DEFAULT 0 COMMENT '����״̬',#0�����գ�1���ܣ�2�����ܣ�3����Ա���ģ�4ҽ���ܾ���6���ϴ���7δ����
	operateReason varchar(1000) DEFAULT NULL COMMENT '��������',
	operateProcess varchar(50) DEFAULT NULL COMMENT '��������',
	operateDate datetime DEFAULT NULL COMMENT '��������',	
	refuseId bigint(11) DEFAULT NULL COMMENT 'ҽ���˴���',
	refuseName varchar(50) DEFAULT NULL COMMENT 'ҽ����',
	refuseReason varchar(1000) DEFAULT NULL COMMENT '�ܾ�����',
	refuseDate datetime DEFAULT NULL COMMENT '�ܾ�����',
	adminPersonId bigint(11) DEFAULT NULL COMMENT '����Ա����',
	adminPersonName varchar(50) DEFAULT NULL COMMENT '����Ա',
	adminChangeDate datetime DEFAULT NULL COMMENT '��������', 
	adminReason varchar(1000) DEFAULT NULL COMMENT '����Ա����',
	enableDate datetime DEFAULT NULL COMMENT '�ɲ�������',
	sourceType int(4) DEFAULT 0 COMMENT '��Դ����',//0�������̡�1�޳�ҵ��
	currencyField varchar(50) DEFAULT NULL COMMENT 'ͨ��',
	approvalState DEFAULT 0 COMMENT '����״̬',//0���� 1ͬ�� 2�ܾ�
dataType int(4) NOT NULL COMMENT '�ۿ�����', -- 0 ��ϸ�ۿ� 1 �����ۿ�
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT 1 COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


#DROP TABLE IF EXISTS yb_appeal_result;
CREATE TABLE yb_appeal_result (
  id char(36) NOT NULL COMMENT '������',
	applyDataId char(36) NOT NULL COMMENT '����������ϸ',
	verifyId  char(36) NOT NULL COMMENT '�˶�Id',
	manageId  char(36) NOT NULL COMMENT '����Id',
	doctorCode varchar(50) NOT NULL COMMENT 'ҽ������',
	doctorName varchar(50) NOT NULL COMMENT 'ҽ��',
  	deptCode varchar(100) NOT NULL COMMENT '���ұ���',
	deptName varchar(100) NOT NULL COMMENT '����',
	operateReason varchar(1000) DEFAULT NULL COMMENT '����',
	operateDate datetime DEFAULT NULL COMMENT '��������',
	resetDataId  char(36) DEFAULT NULL COMMENT '�޳���ϸ�ۿ�Id',
	resetDate datetime DEFAULT NULL COMMENT '�޳�����',
        resetPersonId bigint(11) DEFAULT NULL COMMENT '�޳��˴���',
	resetPersonName varchar(50) DEFAULT NULL COMMENT '�޳���',
	sourceType int(4) DEFAULT 0 COMMENT '��Դ����',//0�������̡�1�޳�ҵ��
	#isDeductImplement int(4) DEFAULT 0 COMMENT '�Ƿ���ۿ���ʵ',// 0δ���1���
	#isRepayImplement int(4) DEFAULT 0 COMMENT '�Ƿ��������ʵ',// 0δ���1���
	currencyField varchar(50) DEFAULT NULL COMMENT 'ͨ��',
dataType int(4) NOT NULL COMMENT '�ۿ�����', -- 0 ��ϸ�ۿ� 1 �����ۿ�
repayState int(4) DEFAULT 1 COMMENT '����״̬', -- 1 �ɹ� 2 ʧ�� 3 ���ֵ��� 
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT 1 COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#�ۿ���ʵ
#DROP TABLE IF EXISTS yb_appeal_result_deductImplement;
CREATE TABLE yb_appeal_result_deductImplement (
  id char(36) NOT NULL COMMENT '�ۿ���ʵId',
	resultId char(36) NOT NULL COMMENT '�����ϴ�Id',
	resetDataId  char(36) NOT NULL COMMENT '�޳���ϸId',
	applyDate datetime NOT NULL COMMENT '��������',
	applyDateStr varchar(10) NOT NULL COMMENT '��������Str',
	implementDate datetime NOT NULL COMMENT '��Ч����',
	implementDateStr varchar(10) NOT NULL COMMENT '��Ч����Str',
	shareState int(4) DEFAULT 0 COMMENT '��̯��ʽ', # 0 ���� 1����
	shareProgramme varchar(1000) DEFAULT NULL COMMENT '��̯����',
	dataType int(4) NOT NULL COMMENT '�ۿ�����', -- 0 ��ϸ�ۿ� 1 �����ۿ�
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT 1 COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#������ʵ
#DROP TABLE IF EXISTS yb_appeal_result_repayment;
CREATE TABLE yb_appeal_result_repayment (
  id char(36) NOT NULL COMMENT '������ʵId',
  applyDate datetime NOT NULL COMMENT '��������',
  applyDateStr varchar(10) NOT NULL COMMENT '��������Str',
  resultId char(36) NOT NULL COMMENT '�����ϴ�Id',
  resetDataId  char(36) NOT NULL COMMENT '�޳���ϸId',
  deductImplementId char(36) NOT NULL COMMENT '�۳���ʵId',
  repaymentProgramme varchar(1000) DEFAULT NULL COMMENT '�����',
  dataType int(4) NOT NULL COMMENT '�ۿ�����', -- 0 ��ϸ�ۿ� 1 �����ۿ�
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT 1 COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


#DROP TABLE IF EXISTS yb_reconsider_InpatientFees;
CREATE TABLE yb_reconsider_InpatientFees (
id char(36) NOT NULL COMMENT 'ҽ��סԺHis',
inpatientId varchar(100) NOT NULL COMMENT 'סԺ��',
patientName varchar(100) NOT NULL COMMENT '��������',
settlementId varchar(100) NOT NULL COMMENT 'HIS�������',
billNo varchar(100) NOT NULL COMMENT '���ݺ�',
transNo varchar(100) NOT NULL COMMENT '������ˮ��',
itemId varchar(100) DEFAULT NULL COMMENT '��Ŀ����',
itemCode varchar(100) DEFAULT NULL COMMENT '��Ŀҽ������',
itemName varchar(100) NOT NULL COMMENT '��Ŀ����',
itemCount decimal(17,4) DEFAULT NULL COMMENT '��Ŀ����',
itemPrice decimal(17,4) DEFAULT NULL COMMENT '��Ŀ����',
itemAmount decimal(17,4) DEFAULT NULL COMMENT '��Ŀ���',
feeDate datetime DEFAULT NULL COMMENT '��������',
deptId varchar(100) DEFAULT NULL COMMENT 'סԺ���Ҵ���',
deptName varchar(100) DEFAULT NULL COMMENT 'סԺ��������',
orderDocId varchar(100) DEFAULT NULL COMMENT '����ҽ������',
orderDocName varchar(100) DEFAULT NULL COMMENT '����ҽ������',
excuteDeptId varchar(100) DEFAULT NULL COMMENT 'ִ�п��Ҵ���',
excuteDeptName varchar(100) DEFAULT NULL COMMENT 'ִ�п�������',
excuteDocId varchar(100) DEFAULT NULL COMMENT 'ִ��ҽ������',
excuteDocName varchar(100) DEFAULT NULL COMMENT 'ִ��ҽ������',
settlementDate datetime DEFAULT NULL COMMENT '����ʱ��',
applyDateStr varchar(10) NOT NULL COMMENT '��������Str',
dataType int(4) NOT NULL COMMENT '�ۿ�����', -- 0 ��ϸ�ۿ� 1 �����ۿ�
typeno int(4) NOT NULL COMMENT '�汾����',
orderNumber varchar(50) NOT NULLL COMMENT '���',
applyDataId char(36) NOT NULL COMMENT '����������ϸ',  
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS com_configureManage;
CREATE TABLE com_configureManage (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '���ù���',
  intField tinyint(4) DEFAULT 0 COMMENT '����',
	numericField numeric(8,2) DEFAULT 0 COMMENT 'С��',
	stringField varchar(50) DEFAULT NULL COMMENT '�ַ�',
	currencyField varchar(50) DEFAULT NULL COMMENT 'ͨ���ֶ�',
	configureType tinyint(4) DEFAULT NULL COMMENT '����',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#�޳�
DROP TABLE IF EXISTS yb_reconsider_reset;
CREATE TABLE yb_reconsider_reset (
  id char(36) NOT NULL COMMENT '�޳���',
  applyDate datetime DEFAULT NULL COMMENT '��������',
  applyDateStr varchar(10) NOT NULL COMMENT '��������Str',
  currencyField varchar(100) DEFAULT NULL COMMENT 'ͨ��',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#�޳���ϸ
DROP TABLE IF EXISTS yb_reconsider_reset_data;
CREATE TABLE yb_reconsider_reset_data (
  id char(36) NOT NULL COMMENT '�޳�����ϸ�ۿ�',
orderNumber varchar(50) DEFAULT NULL COMMENT '���',
	pid char(36) NOT NULL COMMENT '����',
  serialNo varchar(100) NOT NULL COMMENT '������ˮ��',
	billNo varchar(50) NOT NULL COMMENT '���ݺ�',
	projectCode varchar(100) DEFAULT NULL COMMENT '��Ŀ����',
	projectName varchar(200) DEFAULT NULL COMMENT '��Ŀ����',
	medicalPrice decimal(17,4) DEFAULT NULL COMMENT 'ҽ���ڽ��',
	ruleName varchar(50) DEFAULT NULL COMMENT '��������',
	deductPrice decimal(17,4) DEFAULT NULL COMMENT '�۳����',
	deductReason varchar(1000) DEFAULT NULL COMMENT '�۳�ԭ��',
	repaymentReason varchar(1000) DEFAULT NULL COMMENT '����ԭ��',
	doctorName varchar(50) DEFAULT NULL COMMENT 'ҽ������',
	deptCode varchar(100) DEFAULT NULL COMMENT '���ұ���',
	deptName varchar(100) DEFAULT NULL COMMENT '��������',
	costDate datetime DEFAULT NULL COMMENT '��������',
  costDateStr varchar(50) DEFAULT NULL COMMENT '��������str',
	hospitalizedNo varchar(50) DEFAULT NULL COMMENT 'סԺ��',
	treatmentMode varchar(50) DEFAULT NULL COMMENT '��ҽ��ʽ',
	settlementDate datetime DEFAULT NULL COMMENT '��������',
  settlementDateStr varchar(50) DEFAULT NULL COMMENT '��������Str',
	personalNo varchar(50) DEFAULT NULL COMMENT '���˱��',
	insuredName varchar(50) DEFAULT NULL COMMENT '�α�������',
	cardNumber varchar(50) DEFAULT NULL COMMENT 'ҽ������',
	areaName varchar(50) DEFAULT NULL COMMENT 'ͳ��������',
	orderNum int(4) NOT NULL COMMENT '����',
	insuredType varchar(50) DEFAULT NULL COMMENT '�α�����',
	dataType int(4) NOT NULL COMMENT '�ۿ�����', -- 0 ��ϸ�ۿ� 1 �����ۿ�
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  seekState int(4) DEFAULT 0 COMMENT 'ƥ��״̬',-- 0δƥ�� 1��ƥ��
repaymentPrice decimal(17,4)  DEFAULT NULL COMMENT '������',
  STATE int(4) DEFAULT 0 COMMENT '״̬',-- 0�ϴ� 1һ�Զ� 2δ֪
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#�˹�����
DROP TABLE IF EXISTS yb_handle_verify;
CREATE TABLE yb_handle_verify (
  id char(36) NOT NULL COMMENT '�˹�����',
  applyDate datetime DEFAULT NULL COMMENT '��������',
  applyDateStr varchar(10) NOT NULL COMMENT '��������Str',
  currencyField varchar(100) DEFAULT NULL COMMENT 'ͨ��',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS yb_handle_verify_data;
CREATE TABLE yb_handle_verify_data (
  id char(36) NOT NULL COMMENT '�˹�������ϸ',
	pid char(36) NOT NULL COMMENT '�˹�����Id',
	resetId char(36) NOT NULL COMMENT '�޳���ϸId',  
	resultId char(36) NOT NULL COMMENT '�����ϴ�Id',  
	applyDataId char(36) NOT NULL COMMENT '����������ϸ',
	verifyId  char(36) NOT NULL COMMENT '�˶�Id',
	manageId  char(36) NOT NULL COMMENT '����Id',
	doctorCode varchar(50) NOT NULL COMMENT '����ҽ������',
	doctorName varchar(50) NOT NULL COMMENT '����ҽ��',
  	deptCode varchar(100) NOT NULL COMMENT '������ұ���',
	deptName varchar(100) NOT NULL COMMENT '�������',
	operateReason varchar(1000) DEFAULT NULL COMMENT '��������',
	operateDate datetime DEFAULT NULL COMMENT '��������',
	matchPersonId bigint(11) DEFAULT NULL COMMENT 'ƥ���˴���',
	matchPersonName varchar(50) DEFAULT NULL COMMENT 'ƥ����',
	matchDate datetime DEFAULT NULL COMMENT 'ƥ������', 
	reviewerId bigint(11) DEFAULT NULL COMMENT '����˴���',
	reviewerName varchar(50) DEFAULT NULL COMMENT '�����',
	reviewerDate datetime DEFAULT NULL COMMENT '�������', 
	sendPersonId bigint(11) DEFAULT NULL COMMENT '�����˴���',
	sendPersonName varchar(50) DEFAULT NULL COMMENT '������',	
	sendDate datetime DEFAULT NULL COMMENT '��������', 
	currencyField varchar(100) DEFAULT NULL COMMENT 'ͨ��',
  dataType int(4) NOT NULL COMMENT '�ۿ�����', -- 0 ��ϸ�ۿ� 1 �����ۿ�
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT 1 COMMENT '״̬',#1����ˡ�2����ˡ�3�ѷ���
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#����˶�
DROP TABLE IF EXISTS yb_reconsider_repay;
CREATE TABLE yb_reconsider_repay (
  id char(36) NOT NULL COMMENT '����˶�',
  applyDate datetime DEFAULT NULL COMMENT '��������',
  applyDateStr varchar(10) NOT NULL COMMENT '��������Str',
  operatorId bigint(11) NOT NULL COMMENT '����Ա����',
  operatorName varchar(50) NOT NULL COMMENT '����Ա����',
  uploadFileName varchar(100) DEFAULT NULL COMMENT '�ӱ��ϴ�����',
  dataType int(4) NOT NULL COMMENT '�ۿ�����', -- 0 ��ϸ�ۿ� 1 �����ۿ�
  repayType int(4)  DEFAULT 0 COMMENT '��������',-- 0 �ӱ� 1ְ��
  currencyField varchar(100) DEFAULT NULL COMMENT 'ͨ��',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS yb_reconsider_repay_data;
CREATE TABLE yb_reconsider_repay_data (
  id char(36) NOT NULL COMMENT '����˶���ϸ',
	pid char(36) NOT NULL COMMENT '����˶�Id',
	belongDate datetime NOT NULL COMMENT  '������',
	belongDateUpload datetime NOT NULL COMMENT  '������Upload',
	belongDateStr varchar(10) NOT NULL COMMENT '������Str',
	hospitalCode varchar(50) DEFAULT NULL COMMENT 'ҽԺ���',
	hospitalName varchar(100) DEFAULT NULL COMMENT 'ҽԺ����',
	orderNumber varchar(50) DEFAULT NULL COMMENT '���',
	orderNumberNew varchar(50) DEFAULT NULL COMMENT '���new',
  	serialNo varchar(100) DEFAULT NULL COMMENT '������ˮ��',
	billNo varchar(50) DEFAULT NULL COMMENT '���ݺ�',
	projectCode varchar(100) DEFAULT NULL COMMENT '��Ŀ����',
	projectName varchar(200) DEFAULT NULL COMMENT '��Ŀ����',
	medicalPrice decimal(17,4) DEFAULT NULL COMMENT 'ҽ���ڽ��',
	ruleName varchar(50) DEFAULT NULL COMMENT '��������',
	deductPrice decimal(17,4) DEFAULT NULL COMMENT '�۳����',
	deductReason varchar(1000) DEFAULT NULL COMMENT '�۳�ԭ��',
	repaymentPrice decimal(17,4) DEFAULT NULL COMMENT '������',
	repaymentReason varchar(1000) DEFAULT NULL COMMENT '����ԭ��',
	doctorName varchar(50) DEFAULT NULL COMMENT 'ҽ������',
	deptCode varchar(100) DEFAULT NULL COMMENT '���ұ���',
	deptName varchar(100) DEFAULT NULL COMMENT '��������',
	costDate datetime DEFAULT NULL COMMENT '��������',
  costDateStr varchar(50) DEFAULT NULL COMMENT '��������str',
	hospitalizedNo varchar(50) DEFAULT NULL COMMENT 'סԺ��',
	treatmentMode varchar(50) DEFAULT NULL COMMENT '��ҽ��ʽ',
	settlementDate datetime DEFAULT NULL COMMENT '��������',
  settlementDateStr varchar(50) DEFAULT NULL COMMENT '��������Str',
	personalNo varchar(50) DEFAULT NULL COMMENT '���˱��',
	insuredName varchar(50) DEFAULT NULL COMMENT '�α�������',
	applyDataId char(36) DEFAULT NULL COMMENT '����������ϸ',
	resetDataId  char(36) DEFAULT NULL COMMENT '�޳���ϸ�ۿ�Id',
	resultId char(36) DEFAULT NULL COMMENT '�����ϴ�Id',  
	orderNum int(4) NOT NULL COMMENT '����',
	dataType int(4) NOT NULL COMMENT '�ۿ�����', -- 0 ��ϸ�ۿ� 1 �����ۿ�
	seekState int(4) DEFAULT 0 COMMENT 'ƥ��״̬',-- 0δƥ�� 1��ƥ��
	updateType int(4) DEFAULT 0 COMMENT '��������',-- 0��Ÿ��� 1�������
	repayType int(4)  DEFAULT NULL COMMENT '��������',-- 0 �ӱ� 1ְ��  �����ۿ����ְ�� �ӱ� Ĭ��ֵΪNULL
	warnType int(4) DEFAULT 0 COMMENT '����״̬',-- 1�������(һ�� �����ƥ��) 2�����(һ�� �����ƥ��) 3�����(��� ��Ŵ��������ƥ��) 4ȫ��ƥ�� 5�쳣ƥ��(�ֶ�ƥ������ѡ��һ��ƥ�䣬���쳣������ƥ��)
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',  
  STATE int(4) DEFAULT 0 COMMENT '״̬',-- 0�ϴ� 1һ�Զ� 2δ֪
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


ALTER TABLE yb_reconsider_verify ADD INDEX applyDataId ( applyDataId ) 

ALTER TABLE yb_appeal_manage ADD INDEX applyDataId ( applyDataId ) 

ALTER TABLE yb_appeal_result ADD INDEX applyDataId ( applyDataId ) 

ALTER TABLE yb_reconsider_inpatientfees ADD INDEX applyDateStr ( applyDateStr ) ;
		
ALTER TABLE yb_reconsider_inpatientfees ADD INDEX orderNumber ( orderNumber ) ;

ALTER TABLE yb_reconsider_inpatientfees ADD INDEX applyDataId ( applyDataId ) ;

ALTER TABLE t_user_role ADD INDEX user_id ( user_id ) ;

ALTER TABLE t_user ADD INDEX USERNAME ( USERNAME ) ;

ALTER TABLE yb_person ADD INDEX personCode ( personCode ) 

DROP VIEW IF EXISTS yb_reconsider_verify_view;
create view yb_reconsider_verify_view
as
select	
	ad.serialNo,#������ˮ��
	ad.billNo,#���ݺ�
	ad.proposalCode,#��������
	ad.projectCode,#��Ŀ����
	ad.projectName,#��Ŀ����
	ad.num,#����
	ad.medicalPrice,#ҽ���ڽ��
	ad.ruleName,#��������
	ad.deductPrice,#�۳����
	ad.deductReason,#�۳�ԭ��
	ad.repaymentReason,#����ԭ��
	ad.doctorName,#ҽ������
	ad.deptCode,#���ұ���
	ad.deptName,#��������
	ad.enterHospitalDate,#��Ժ����
	ad.outHospitalDate,#��Ժ����
	ad.costDate,#��������
	ad.enterHospitalDateStr,#��Ժ����Str
	ad.outHospitalDateStr,#��Ժ����Str
	ad.costDateStr,#��������Str
	ad.hospitalizedNo,#סԺ��
	ad.treatmentMode,#��ҽ��ʽ
	ad.settlementDate,#��������
	ad.settlementDateStr,#��������Str
	ad.personalNo,#���˱��
	ad.insuredName,#�α�������
	ad.cardNumber,#ҽ������
	ad.areaName,#ͳ��������
	ad.versionNumber,#�汾��
	ad.backAppeal,#��������
	ad.typeno,	#�汾����
	ad.insuredType,
	ad.dataType,
	ad.orderNumber,
	ad.orderNum,
	ra.applyDate, #��������
	ra.applyDateStr, #��������Str
	ra.operatorId, #����Ա����'
  	ra.operatorName, #����Ա����,
  	case when rv.id is null then UUID() else rv.id end id,
	ad.id applyDataId,
	rv.verifyDoctorCode,
	rv.verifyDoctorName,
	rv.verifyDeptCode,
	rv.verifyDeptName,
	rv.operateReason,
	rv.operateDate,
	rv.matchPersonId,#ƥ���˴���',
	rv.matchPersonName,#ƥ����',
	rv.matchDate,#ƥ������', 
	rv.reviewerId,#����˴���',
	rv.reviewerName,#�����',
	rv.reviewerDate,#�������', 
	rv.sendPersonId,#�����˴���',
	rv.sendPersonName,#������',	
	rv.sendDate,#��������', 
	rv.COMMENTS,
	rv.STATE,
	rv.IS_DELETEMARK,
	rv.MODIFY_TIME,
	rv.CREATE_TIME,
	rv.CREATE_USER_ID,
	rv.MODIFY_USER_ID,
	rv.currencyField,
	case when rv.id is null then 0 else 1 end isVerify,
	case when yp.id is null then 0 else 1 end isPerson
from 
	yb_reconsider_apply_data ad 		
	INNER JOIN yb_reconsider_apply ra ON
		ra.id = ad.pid AND
		ra.IS_DELETEMARK = 1
  LEFT JOIN yb_reconsider_verify rv ON
    ad.id = rv.applyDataId AND
	rv.IS_DELETEMARK = 1
  LEFT join yb_person yp on
	rv.verifyDoctorCode = yp.personCode and
	yp.IS_DELETEMARK = 1
where
	ad.IS_DELETEMARK = 1;

DROP VIEW IF EXISTS yb_appeal_manage_view;
create view yb_appeal_manage_view
as
select	
	ad.serialNo,#������ˮ��
	ad.billNo,#���ݺ�
	ad.proposalCode,#��������
	ad.projectCode,#��Ŀ����
	ad.projectName,#��Ŀ����
	ad.num,#����
	ad.medicalPrice,#ҽ���ڽ��
	ad.ruleName,#��������
	ad.deductPrice,#�۳����
	ad.deductReason,#�۳�ԭ��
	ad.repaymentReason,#����ԭ��
	ad.doctorName,#ҽ������
	ad.deptCode,#���ұ���
	ad.deptName,#��������
	ad.enterHospitalDate,#��Ժ����
	ad.outHospitalDate,#��Ժ����
	ad.costDate,#��������
	ad.enterHospitalDateStr,#��Ժ����Str
	ad.outHospitalDateStr,#��Ժ����Str
	ad.costDateStr,#��������Str
	ad.hospitalizedNo,#סԺ��
	ad.treatmentMode,#��ҽ��ʽ
	ad.settlementDate,#��������
	ad.settlementDateStr,#��������Str
	ad.personalNo,#���˱��
	ad.insuredName,#�α�������
	ad.cardNumber,#ҽ������
	ad.areaName,#ͳ��������
	ad.versionNumber,#�汾��
	ad.backAppeal,#��������
	ad.typeno,	#�汾����
	ad.insuredType,
	ad.dataType,
	ad.orderNumber,
	ad.orderNum,
	ra.applyDate, #��������
	ra.applyDateStr, #��������Str
	ra.operatorId, #����Ա����'
  ra.operatorName, #����Ա����,
  am.id,
	ad.id applyDataId,
  am.verifyId,
	am.readyDoctorCode,
	am.readyDoctorName,
	am.readyDeptCode,
	am.readyDeptName,
	am.changeDoctorCode,#���ҽ������',
	am.changeDoctorName,#���ҽ��',
  am.changeDeptCode,#���������ұ���',
	am.changeDeptName,#����������',	
	am.operateReason,
	am.operateDate,
	am.acceptState,
	am.refuseId,#ҽ���˴���',
	am.refuseName,#ҽ����',
	am.refuseReason,#�ܾ�����',
	am.refuseDate,#�ܾ�����',
	am.adminPersonId,#����Ա����',
	am.adminPersonName,#����Ա',
	am.adminChangeDate,#��������', 
	am.adminReason,#����Ա����',
	am.enableDate,#�ɲ�������
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
	ad.serialNo,#������ˮ��
	ad.billNo,#���ݺ�
	ad.proposalCode,#��������
	ad.projectCode,#��Ŀ����
	ad.projectName,#��Ŀ����
	ad.num,#����
	ad.medicalPrice,#ҽ���ڽ��
	ad.ruleName,#��������
	ad.deductPrice,#�۳����
	ad.deductReason,#�۳�ԭ��
	ad.repaymentReason,#����ԭ��
	ad.doctorName,#ҽ������
	ad.deptCode,#���ұ���
	ad.deptName,#��������
	ad.enterHospitalDate,#��Ժ����
	ad.outHospitalDate,#��Ժ����
	ad.costDate,#��������
	ad.enterHospitalDateStr,#��Ժ����Str
	ad.outHospitalDateStr,#��Ժ����Str
	ad.costDateStr,#��������Str
	ad.hospitalizedNo,#סԺ��
	ad.treatmentMode,#��ҽ��ʽ
	ad.settlementDate,#��������
	ad.settlementDateStr,#��������Str
	ad.personalNo,#���˱��
	ad.insuredName,#�α�������
	ad.cardNumber,#ҽ������
	ad.areaName,#ͳ��������
	ad.versionNumber,#�汾��
	ad.backAppeal,#��������
	ad.typeno,	#�汾����
	ad.insuredType,
	ad.dataType,
	ad.orderNumber,
	ad.orderNum,
	ra.applyDate, #��������
	ra.applyDateStr, #��������Str
	ra.operatorId, #����Ա����'
  ra.operatorName, #����Ա����,
	ra.resetState raResetState,
	ra.resultState raResultState,
	ra.repayState raRepayState,
  art.id,
	art.applyDataId,
  art.verifyId,
	art.doctorCode arDoctorCode,
	art.doctorName arDoctorName,
	art.deptCode arDeptCode,
	art.deptName arDeptName,
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
	yb_reconsider_reset_data.serialNo ,-- ������ˮ��
	yb_reconsider_reset_data.billNo ,-- ���ݺ�
	yb_reconsider_reset_data.projectCode ,-- ��Ŀ����
	yb_reconsider_reset_data.projectName ,-- ��Ŀ����
	yb_reconsider_reset_data.medicalPrice ,-- ҽ���ڽ��
	yb_reconsider_reset_data.ruleName ,-- ��������
	yb_reconsider_reset_data.deductPrice ,-- �۳����
	yb_reconsider_reset_data.deductReason ,-- �۳�ԭ��
	yb_reconsider_reset_data.repaymentReason ,-- ����ԭ��
	yb_reconsider_reset_data.doctorName ,-- ҽ������
	yb_reconsider_reset_data.deptCode ,-- ���ұ���
	yb_reconsider_reset_data.deptName ,-- ��������
	yb_reconsider_reset_data.costDate ,-- ��������
  yb_reconsider_reset_data.costDateStr ,-- ��������str
	yb_reconsider_reset_data.hospitalizedNo ,-- סԺ��
	yb_reconsider_reset_data.treatmentMode ,-- ��ҽ��ʽ
	yb_reconsider_reset_data.settlementDate ,-- ��������
  yb_reconsider_reset_data.settlementDateStr ,-- ��������Str
	yb_reconsider_reset_data.personalNo ,-- ���˱��
	yb_reconsider_reset_data.insuredName ,-- �α�������
	yb_reconsider_reset_data.cardNumber ,-- ҽ������
	yb_reconsider_reset_data.areaName ,-- ͳ��������
	yb_reconsider_reset_data.insuredType,-- �α�����
	yb_reconsider_reset_data.dataType,-- ��������
	yb_reconsider_reset_data.orderNumber,-- ���
	yb_reconsider_reset_data.orderNum,-- ����
	yb_reconsider_reset_data.repaymentPrice,-- ������
	yb_reconsider_reset_data.STATE, -- ״̬
	yb_reconsider_reset_data.seekState,-- ����״̬
	yb_reconsider_reset.id resetId, -- ����Id
	yb_reconsider_reset.applyDate,  -- ����
	yb_reconsider_reset.applyDateStr,-- ���� 
	yb_reconsider_reset.currencyField -- ͨ��
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
	rrd.serialNo,#������ˮ��
	rrd.billNo,#���ݺ�
	ad.proposalCode,#��������
	rrd.projectCode,#��Ŀ����
	rrd.projectName,#��Ŀ����
	ad.num,#����
	rrd.medicalPrice,#ҽ���ڽ��
	rrd.ruleName,#��������
	rrd.deductPrice,#�۳����
	rrd.deductReason,#�۳�ԭ��
	rrd.repaymentReason,#����ԭ��
	rrd.doctorName,#ҽ������
	rrd.deptCode,#���ұ���
	rrd.deptName,#��������
	ad.enterHospitalDate,#��Ժ����
	ad.outHospitalDate,#��Ժ����
	rrd.costDate,#��������
	ad.enterHospitalDateStr,#��Ժ����Str
	ad.outHospitalDateStr,#��Ժ����Str
	rrd.costDateStr,#��������Str
	rrd.hospitalizedNo,#סԺ��
	rrd.treatmentMode,#��ҽ��ʽ
	rrd.settlementDate,#��������
	rrd.settlementDateStr,#��������Str
	rrd.personalNo,#���˱��
	rrd.insuredName,#�α�������
	rrd.cardNumber,#ҽ������
	rrd.areaName,#ͳ��������
	ad.versionNumber,#�汾��
	ad.backAppeal,#��������
	ad.typeno,	#�汾����
	rrd.insuredType,
	rrd.dataType,
	rrd.orderNumber,
	rrd.orderNum,
	hv.applyDate, #��������
	hv.applyDateStr, #��������Str
  hvd.id,
	hvd.pid,
	hvd.applyDataId,
  hvd.verifyId,
	hvd.manageId,
	hvd.resetId,
	hvd.resultId,
	hvd.doctorCode hvDoctorCode,
	hvd.doctorName hvDoctorName,
	hvd.deptCode hvDeptCode,
	hvd.deptName hvDeptName,
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
	INNER JOIN yb_reconsider_reset_data rrd ON
		rrd.id = hvd.resetId AND
		rrd.IS_DELETEMARK = 1
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
	yb_reconsider_reset_data.serialNo ,-- ������ˮ��
	yb_reconsider_reset_data.billNo ,-- ���ݺ�
	yb_reconsider_reset_data.projectCode ,-- ��Ŀ����
	yb_reconsider_reset_data.projectName ,-- ��Ŀ����
	yb_reconsider_reset_data.medicalPrice ,-- ҽ���ڽ��
	yb_reconsider_reset_data.ruleName ,-- ��������
	yb_reconsider_reset_data.deductPrice ,-- �۳����
	yb_reconsider_reset_data.deductReason ,-- �۳�ԭ��
	yb_reconsider_reset_data.repaymentReason ,-- ����ԭ��
	yb_reconsider_reset_data.doctorName ,-- ҽ������
	yb_reconsider_reset_data.deptCode ,-- ���ұ���
	yb_reconsider_reset_data.deptName ,-- ��������
	yb_reconsider_reset_data.costDate ,-- ��������
  yb_reconsider_reset_data.costDateStr ,-- ��������str
	yb_reconsider_reset_data.hospitalizedNo ,-- סԺ��
	yb_reconsider_reset_data.treatmentMode ,-- ��ҽ��ʽ
	yb_reconsider_reset_data.settlementDate ,-- ��������
  yb_reconsider_reset_data.settlementDateStr ,-- ��������Str
	yb_reconsider_reset_data.personalNo ,-- ���˱��
	yb_reconsider_reset_data.insuredName ,-- �α�������
	yb_reconsider_reset_data.cardNumber ,-- ҽ������
	yb_reconsider_reset_data.areaName ,-- ͳ��������
	yb_reconsider_reset_data.insuredType,-- �α�����
	yb_reconsider_reset_data.dataType,-- ��������
	yb_reconsider_reset_data.orderNumber,-- ���
	yb_reconsider_reset_data.orderNum,-- ����
	yb_reconsider_reset_data.repaymentPrice ,-- ������
	yb_reconsider_reset_data.STATE, -- ״̬
	yb_reconsider_reset_data.seekState,-- ����״̬
	yb_reconsider_reset.id resetId, -- ����Id
	yb_reconsider_reset.applyDate,  -- ����
	yb_reconsider_reset.applyDateStr,-- ���� 
	yb_reconsider_reset.currencyField, -- ͨ��
	yb_appeal_result.doctorCode arDoctorCode,
	yb_appeal_result.doctorName arDoctorName,
	yb_appeal_result.deptCode arDeptCode,
	yb_appeal_result.deptName arDeptName,
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
	ad.serialNo,#������ˮ��
	ad.billNo,#���ݺ�
	ad.proposalCode,#��������
	ad.projectCode,#��Ŀ����
	ad.projectName,#��Ŀ����
	ad.num,#����
	ad.medicalPrice,#ҽ���ڽ��
	ad.ruleName,#��������
	ad.deductPrice,#�۳����
	ad.deductReason,#�۳�ԭ��
	ad.repaymentReason,#����ԭ��
	ad.doctorName,#ҽ������
	ad.deptCode,#���ұ���
	ad.deptName,#��������
	ad.enterHospitalDate,#��Ժ����
	ad.outHospitalDate,#��Ժ����
	ad.costDate,#��������
	ad.enterHospitalDateStr,#��Ժ����Str
	ad.outHospitalDateStr,#��Ժ����Str
	ad.costDateStr,#��������Str
	ad.hospitalizedNo,#סԺ��
	ad.treatmentMode,#��ҽ��ʽ
	ad.settlementDate,#��������
	ad.settlementDateStr,#��������Str
	ad.personalNo,#���˱��
	ad.insuredName,#�α�������
	ad.cardNumber,#ҽ������
	ad.areaName,#ͳ��������
	ad.versionNumber,#�汾��
	ad.backAppeal,#��������
	ad.typeno,	#�汾����
	ad.insuredType,
	ad.dataType,
	ad.orderNumber,
	ad.orderNum,
	ra.applyDate, #��������
	ra.applyDateStr, #��������Str
	ra.operatorId, #����Ա����'
  ra.operatorName, #����Ա����,
	ra.resetState raResetState,
	ra.resultState raResultState,
	ra.repayState raRepayState,
  art.id,
	art.manageId,
	art.applyDataId,
  art.verifyId,
	art.doctorCode arDoctorCode,
	art.doctorName arDoctorName,
	art.deptCode arDeptCode,
	art.deptName arDeptName,
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
	CASE WHEN art.state = 1 THEN -ad.deductPrice WHEN ad.deductPrice = resetDate.deductPrice 
	THEN 0 ELSE resetDate.deductPrice - ad.deductPrice  END adjustPrice
from 
	yb_reconsider_apply_data ad 		
	INNER JOIN yb_reconsider_apply ra ON
		ra.id = ad.pid AND
		ra.resetState = 1 AND
		ra.IS_DELETEMARK = 1
  INNER JOIN yb_appeal_result art ON
		art.applyDataId = ad.id AND
		art.sourceType = 0 AND
		art.IS_DELETEMARK = 1
	LEFT JOIN yb_reconsider_reset_data resetDate on
		resetDate.id = art.resetDataId AND
		resetDate.seekState = 1 AND 
		resetDate.IS_DELETEMARK = 1
where
    ad.IS_DELETEMARK = 1;

DROP VIEW IF EXISTS yb_appeal_result_deductImplement_view;
create view yb_appeal_result_deductImplement_view
as
select	
	rrd.serialNo,#������ˮ��
	rrd.billNo,#���ݺ�
	ad.proposalCode,#��������
	rrd.projectCode,#��Ŀ����
	rrd.projectName,#��Ŀ����
	ad.num,#����
	rrd.medicalPrice,#ҽ���ڽ��
	rrd.ruleName,#��������
	rrd.deductPrice,#�۳����
	rrd.deductReason,#�۳�ԭ��
	rrd.repaymentReason,#����ԭ��
	rrd.doctorName,#ҽ������
	rrd.deptCode,#���ұ���
	rrd.deptName,#��������
	ad.enterHospitalDate,#��Ժ����
	ad.outHospitalDate,#��Ժ����
	rrd.costDate,#��������
	ad.enterHospitalDateStr,#��Ժ����Str
	ad.outHospitalDateStr,#��Ժ����Str
	rrd.costDateStr,#��������Str
	rrd.hospitalizedNo,#סԺ��
	rrd.treatmentMode,#��ҽ��ʽ
	rrd.settlementDate,#��������
	rrd.settlementDateStr,#��������Str
	rrd.personalNo,#���˱��
	rrd.insuredName,#�α�������
	rrd.cardNumber,#ҽ������
	rrd.areaName,#ͳ��������
	ad.versionNumber,#�汾��
	ad.backAppeal,#��������
	ad.typeno,	#�汾����
	rrd.insuredType,
	rrd.dataType,
	rrd.orderNumber,
	rrd.orderNum,
	ra.applyDate, #��������
	ra.applyDateStr, #��������Str
	ra.operatorId, #����Ա����'
  ra.operatorName, #����Ա����,
	ra.resetState raResetState,
	ra.resultState raResultState,
	ra.repayState raRepayState,
  art.id,
	art.manageId,
	art.applyDataId,
  art.verifyId,
	art.doctorCode arDoctorCode,
	art.doctorName arDoctorName,
	art.deptCode arDeptCode,
	art.deptName arDeptName,
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
	ardi.implementDate, #'��Ч����'
	ardi.implementDateStr, #'��Ч����Str'
	ardi.shareState, #'��̯��ʽ'  # 0 ���� 1����
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
	INNER JOIN yb_reconsider_reset_data rrd ON
		rrd.id = art.resetDataId AND
		rrd.IS_DELETEMARK = 1
	LEFT JOIN yb_appeal_result_deductImplement ardi ON
		ardi.resultId = art.id
where
    ad.IS_DELETEMARK = 1;

DROP VIEW IF EXISTS yb_appeal_result_repayment_view;
create view yb_appeal_result_repayment_view
as
select
	yb_reconsider_repay_data.id,
	yb_reconsider_repay.applyDate,
	yb_reconsider_repay.applyDateStr,
	yb_reconsider_repay.dataType,#�ۿ�����', -- 0 ��ϸ�ۿ� 1 �����ۿ�
  yb_reconsider_repay.repayType,#�������� 0 �ӱ� 1ְ��  �����ۿ����ְ�� �ӱ� Ĭ��ֵΪNULL
	yb_reconsider_repay_data.belongDate ,#������
	yb_reconsider_repay_data.belongDateStr,#������Str
	yb_reconsider_repay_data.hospitalCode,#ҽԺ���
	yb_reconsider_repay_data.hospitalName,#ҽԺ����
	CASE WHEN yb_reconsider_repay_data.updateType = 0 THEN yb_reconsider_repay_data.orderNumber
	ELSE yb_reconsider_repay_data.orderNumberNew END orderNumber,#���
	yb_reconsider_repay_data.orderNumberNew,#���new
	yb_reconsider_repay_data.orderNum,#����
  yb_reconsider_repay_data.serialNo,#������ˮ��
	yb_reconsider_repay_data.billNo,#���ݺ�
	yb_reconsider_repay_data.projectCode,#��Ŀ����
	yb_reconsider_repay_data.projectName,#��Ŀ����
	yb_reconsider_repay_data.medicalPrice,#ҽ���ڽ��
	yb_reconsider_repay_data.ruleName,#��������
	yb_reconsider_repay_data.deductPrice,#�۳����
	yb_reconsider_repay_data.deductReason,#�۳�ԭ��
	yb_reconsider_repay_data.repaymentPrice,#������
	yb_reconsider_repay_data.repaymentReason,#����ԭ��
	yb_reconsider_repay_data.doctorName,#ҽ������
	yb_reconsider_repay_data.deptCode,#���ұ���
	yb_reconsider_repay_data.deptName,#��������
	yb_reconsider_repay_data.costDate,#��������
  yb_reconsider_repay_data.costDateStr,#��������str
	yb_reconsider_repay_data.hospitalizedNo,#סԺ��
	yb_reconsider_repay_data.treatmentMode,#��ҽ��ʽ
	yb_reconsider_repay_data.settlementDate,#��������
  yb_reconsider_repay_data.settlementDateStr,#��������Str
	yb_reconsider_repay_data.personalNo,#���˱��
	yb_reconsider_repay_data.insuredName,#�α�������
	yb_reconsider_repay_data.updateType,#�������� 0��Ÿ��� 1�������
	yb_reconsider_repay_data.warnType,#����״̬ 1������� 2����� 3�����(��� ��Ŵ��������ƥ��) 4ȫ��ƥ�� 5�쳣ƥ��
	yb_appeal_result.doctorCode arDoctorCode,
	yb_appeal_result.doctorName arDoctorName,
	yb_appeal_result.deptCode arDeptCode,
	yb_appeal_result.deptName arDeptName,
	yb_appeal_result_repayment.id repaymentId,
	yb_reconsider_repay_data.resultId,
	yb_reconsider_repay_data.resetDataId,
	yb_appeal_result_deductimplement.id deductImplementId,
	yb_appeal_result_deductimplement.shareProgramme,
	yb_appeal_result_deductimplement.shareState, # 0 ���� 1����
	yb_appeal_result_repayment.repaymentProgramme,
	yb_reconsider_repay.currencyField
from 
	yb_reconsider_repay_data
	INNER JOIN yb_reconsider_repay ON
		yb_reconsider_repay_data.pid = yb_reconsider_repay.id AND
		yb_reconsider_repay.IS_DELETEMARK = 1
	INNER JOIN yb_appeal_result ON
		yb_appeal_result.id = yb_reconsider_repay_data.resultId AND
		yb_appeal_result.sourceType = 0 AND
		yb_appeal_result.IS_DELETEMARK = 1
	INNER JOIN yb_appeal_result_deductimplement ON
		yb_appeal_result_deductimplement.resultId = yb_appeal_result.id AND
		yb_appeal_result_deductimplement.IS_DELETEMARK = 1
	INNER JOIN yb_reconsider_apply_data ON
		yb_reconsider_repay_data.applyDataId = yb_reconsider_apply_data.id AND
		yb_reconsider_apply_data.IS_DELETEMARK = 1
	INNER JOIN yb_reconsider_apply ON
		yb_reconsider_apply.id = yb_reconsider_apply_data.pid AND
		yb_reconsider_apply.repayState = 1 AND
		yb_reconsider_apply.IS_DELETEMARK = 1
	LEFT JOIN yb_appeal_result_repayment ON
		yb_appeal_result_repayment.resultId = yb_appeal_result.id AND 
		yb_appeal_result_repayment.IS_DELETEMARK = 1
where
		yb_reconsider_repay_data.seekState = 1 AND
    yb_reconsider_repay_data.IS_DELETEMARK = 1;


DROP PROCEDURE IF EXISTS p_appeal_manage_enableOverdue;
CREATE PROCEDURE p_appeal_manage_enableOverdue(in applyDateStr VARCHAR(50),out message VARCHAR(50))
begin
    declare m_id char(36);
		declare w_applyDateStr VARCHAR(50) DEFAULT CASE WHEN applyDateStr = '' THEN DATE_FORMAT(now(), '%Y-%m') ELSE applyDateStr end;
		declare t_update INTEGER DEFAULT 0; 
		declare t_error INTEGER DEFAULT 0;
		declare done int default 0;

    -- �����α�
    declare mc cursor for 
		
		SELECT
			yb_appeal_manage.id
		FROM
			yb_appeal_manage
			INNER JOIN yb_reconsider_apply_data ON 
				yb_appeal_manage.applyDataId = yb_reconsider_apply_data.id AND
				yb_reconsider_apply_data.IS_DELETEMARK = 1
			INNER JOIN yb_reconsider_apply ON
				yb_reconsider_apply.id = yb_reconsider_apply_data.pid AND
				yb_reconsider_apply.applyDateStr =  w_applyDateStr AND
				yb_reconsider_apply.IS_DELETEMARK = 1
		WHERE
			yb_appeal_manage.IS_DELETEMARK = 1 AND 
			yb_appeal_manage.enableDate <= now() AND 
			yb_appeal_manage.acceptState = 0;

    declare continue handler for not found set done = 1;
		declare CONTINUE HANDLER FOR SQLEXCEPTION SET t_error=1; 
				
		START TRANSACTION;
    -- ���α�
    open mc;
		
		-- ѭ��
		itemloop: loop
    -- ��ȡ���
    fetch mc into m_id;
		
			if done = 1 then
					leave itemloop;
			end if;
		
			update yb_appeal_manage 
			set 
				acceptState= 1,
				operateReason='�Զ�����',
				operateDate=now(),
				operateProcess= '������-�Զ�����'
			where 
				id = m_id;			
			
			set t_update = 1;

		end loop;
    -- �ر��α�
    close mc;
		
		IF t_error = 1 THEN    
				set message = '�쳣';
				ROLLBACK;    
		ELSE    
				if t_update = 1 then
					set message = 'ok';
					COMMIT;
				else
					set message = '�޸�������';
					-- select '�޸�������'; 
				end if;
		END IF;

   -- select t_error; 
end;

DROP PROCEDURE IF EXISTS p_appeal_manage_applyEndDateOne;

CREATE PROCEDURE p_appeal_manage_applyEndDateOne(in applyDateStr VARCHAR(50),out message VARCHAR(50))
begin
    declare m_id char(36);
		declare m_acceptState int;
		declare w_applyDateStr VARCHAR(50) DEFAULT CASE WHEN applyDateStr = '' THEN DATE_FORMAT(now(), '%Y-%m') ELSE applyDateStr end;
		declare c_count int;
		declare t_update INTEGER DEFAULT 0; 
		declare t_error INTEGER DEFAULT 0;    
		declare done int default 0;
    -- �����α�
    declare mc cursor for 
		SELECT
			yb_appeal_manage.id,
			yb_appeal_manage.acceptState
		FROM
			yb_appeal_manage
			INNER JOIN yb_reconsider_apply_data ON 
				yb_appeal_manage.applyDataId = yb_reconsider_apply_data.id and
				yb_reconsider_apply_data.typeno = 1 and
				yb_reconsider_apply_data.IS_DELETEMARK = 1
			INNER JOIN yb_reconsider_apply ON
				yb_reconsider_apply.id = yb_reconsider_apply_data.pid and
				yb_reconsider_apply.applyDateStr = w_applyDateStr  AND
				yb_reconsider_apply.endDateOne <= now() AND 
				yb_reconsider_apply.IS_DELETEMARK = 1
		WHERE
			yb_appeal_manage.IS_DELETEMARK = 1 AND
			yb_appeal_manage.acceptState in(0,1);
			
    declare continue handler for not found set done = 1;
		declare CONTINUE HANDLER FOR SQLEXCEPTION SET t_error=1; 
				
		START TRANSACTION;
    -- ���α�
    open mc;
		
		-- ѭ��
		itemloop: loop
    -- ��ȡ���
    fetch mc into m_id,m_acceptState;
		
			if done = 1 then
					leave itemloop;
			end if;
		
			update yb_appeal_manage 
			set 
				acceptState= 7,
				operateReason='',
				operateDate=now(),
				operateProcess= case when m_acceptState=0 then '��������-δ����' else '������-δ����' end		
			where 
				id = m_id;
			
			select count(1) into c_count from yb_appeal_result where id = m_id;
			if c_count = 0 then
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
				'δ����',
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
			end if;
			
			set t_update = 1;

		end loop;
    -- �ر��α�
    close mc;
		
		IF t_error = 1 THEN    
			set message = 'ִ���쳣';
			ROLLBACK;    
		ELSE    
				if t_update = 1 then
					set message = 'ok';
					COMMIT;
				else
					set message = '�޸�������';
					-- select '�޸�������'; 
				end if;
		END IF;
   -- select t_error; 
end;

DROP PROCEDURE IF EXISTS p_appeal_manage_applyEndDateTwo;

CREATE PROCEDURE p_appeal_manage_applyEndDateTwo(in applyDateStr VARCHAR(50),out message VARCHAR(50))
begin
    declare m_id char(36);
		declare m_acceptState int;
		declare w_applyDateStr VARCHAR(50) DEFAULT CASE WHEN applyDateStr = '' THEN DATE_FORMAT(now(), '%Y-%m') ELSE applyDateStr end;
		declare c_count int;	
		declare t_update INTEGER DEFAULT 0; 
		declare t_error INTEGER DEFAULT 0;    
		declare done int default 0;
    -- �����α�
    declare mc cursor for 
		SELECT
			yb_appeal_manage.id,
			yb_appeal_manage.acceptState
		FROM
			yb_appeal_manage
			INNER JOIN yb_reconsider_apply_data ON 
				yb_appeal_manage.applyDataId = yb_reconsider_apply_data.id and
				yb_reconsider_apply_data.typeno = 2 and
				yb_reconsider_apply_data.IS_DELETEMARK = 1
			INNER JOIN yb_reconsider_apply ON
				yb_reconsider_apply.id = yb_reconsider_apply_data.pid and
				yb_reconsider_apply.applyDateStr = w_applyDateStr  AND
				yb_reconsider_apply.endDateTwo <= now() AND 
				yb_reconsider_apply.IS_DELETEMARK = 1
		WHERE
			yb_appeal_manage.IS_DELETEMARK = 1 AND
			yb_appeal_manage.acceptState in(0,1);
			
    declare continue handler for not found set done = 1;
		declare CONTINUE HANDLER FOR SQLEXCEPTION SET t_error=1; 
				
		START TRANSACTION;
    -- ���α�
    open mc;
		
		-- ѭ��
		itemloop: loop
    -- ��ȡ���
    fetch mc into m_id,m_acceptState;
		
			if done = 1 then
					leave itemloop;
			end if;
		
			update yb_appeal_manage 
			set 
				acceptState= 7,
				operateReason='',
				operateDate=now(),
				operateProcess= case when m_acceptState=0 then '��������-δ����' else '������-δ����' end		
			where 
				id = m_id;
			
			select count(1) into c_count from yb_appeal_result where id = m_id;
			if c_count = 0 then	
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
				'δ����',
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
			end if;
			
			set t_update = 1;

		end loop;
    -- �ر��α�
    close mc;
		
		IF t_error = 1 THEN    
				set message = 'ִ���쳣';
				ROLLBACK;    
		ELSE    
				if t_update = 1 then
					set message = 'ok';
					COMMIT;
				else
					set message = '�޸�������';
					-- select '�޸�������'; 
				end if;
		END IF;
   -- select t_error; 
end;


DROP EVENT IF EXISTS e_appeal_manage_enableOverdue;
CREATE EVENT e_appeal_manage_enableOverdue
-- ��ǰʱ�� 1�����һ���¼�
-- ON SCHEDULE EVERY 1 DAY 
-- ���� �ڶ��� �賿 1 �㿪ʼִ���¼�
ON SCHEDULE EVERY 1 DAY STARTS DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 1 DAY), INTERVAL 1 HOUR)
DO call p_appeal_manage_enableOverdue();

-- SELECT @@event_scheduler;-- �鿴 �¼��Ƿ�����

-- �����¼�
-- SET GLOBAL event_scheduler = ON;
-- SET GLOBAL event_scheduler = 1;

-- �ر��¼�
-- SET GLOBAL event_scheduler = OFF;
-- SET GLOBAL event_scheduler = 0;

-- �رն�ʱ����
-- ALTER EVENT �¼��� DISABLE;

-- ������ʱ����
-- ALTER EVENT �¼��� ENABLE;

-- ɾ���ƻ�����
-- DROP event �¼���;

#DROP TABLE IF EXISTS yb_dept;
CREATE TABLE yb_dept (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '����Id',
  deptId varchar(50) NOT NULL COMMENT '���ұ���',
  deptName varchar(200) NOT NULL COMMENT '��������',  
  spellCode varchar(50) DEFAULT NULL COMMENT 'ƴ����',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#DROP TABLE IF EXISTS yb_person;
CREATE TABLE yb_person (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'ҽ��Id',
  personCode varchar(50) NOT NULL COMMENT 'ҽ������',
  personName varchar(200) NOT NULL COMMENT 'ҽ������',  
  deptCode varchar(50) DEFAULT NULL COMMENT '���ұ���',
  deptName varchar(200) DEFAULT NULL COMMENT '��������', 
  sex varchar(50) NOT NULL COMMENT '�Ա�', 
  email varchar(50) DEFAULT NULL COMMENT '����', 
  tel varchar(50) NOT NULL COMMENT '��ϵ�绰', 
  spellCode varchar(50) DEFAULT NULL COMMENT 'ƴ����', 
  strokeCode varchar(50) DEFAULT NULL COMMENT '�����', 
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL  COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL  COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS yb_reconsider_verify_import;
CREATE TABLE yb_reconsider_verify_import (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	orderNumber varchar(50) NOT NULLL COMMENT '���',
  serialNo varchar(100) NOT NULL COMMENT '������ˮ��',
	proposalCode varchar(50) NOT NULL COMMENT '��������',
	projectName varchar(200) DEFAULT NULL COMMENT '��Ŀ����',
	verifyDoctorCode varchar(50) NOT NULL COMMENT '�ο�����ҽ������',
	verifyDoctorName varchar(50) NOT NULL COMMENT '�ο�����ҽ��',
  verifyDeptCode varchar(100) NOT NULL COMMENT '�ο�������ұ���',
	verifyDeptName varchar(100) NOT NULL COMMENT '�ο��������',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1839 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


������ˮ��	
���ݺ�	
��Ŀ����	
��Ŀ����	
ҽ���ڽ��	
��������	
�۳����	
�۳�ԭ��	
����ԭ��	
ҽ������	
���ұ���	
��������	
��������	
סԺ��	
��ҽ��ʽ	
��������	
���˱��	
�α�������	
ͳ��������

ҽԺ���	
ҽԺ����	
������	
���	
���ݺ�	
��������	
��ҽ��ʽ	
���˱��	
�α�������	
��Ŀ����	
�۳����	
������
