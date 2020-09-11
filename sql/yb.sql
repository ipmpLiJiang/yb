DROP TABLE IF EXISTS yb_b_reconsider;
CREATE TABLE yb_b_reconsider (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '������',
	rno int(11) NOT NULL COMMENT '���',
  rdescribe varchar(255) NOT NULL COMMENT '��������',
  rxplain varchar(255) NOT NULL COMMENT '�������',
  rkeypoints varchar(255) NOT NULL COMMENT '�����ص�',
	rmaterials varchar(2000) NOT NULL COMMENT '��������',
	operatorid bigint(11) NOT NULL COMMENT '����Ա����',
	operatorname varchar(50) NOT NULL COMMENT '����Ա����',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS yb_reconsider_apply;
CREATE TABLE yb_reconsider_apply (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '������',
  applydate datetime NOT NULL COMMENT '��������',
  operatorid bigint(11) NOT NULL COMMENT '����Ա����',
  operatorname varchar(50) NOT NULL COMMENT '����Ա����',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS yb_reconsider_apply_data
CREATE TABLE yb_reconsider_apply_data (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '����������ϸ',
	pid int(11) NOT NULL COMMENT '������',
  serialNo varchar(100) NOT NULL COMMENT '������ˮ��',
	billNo varchar(50) NOT NULL COMMENT '���ݺ�',
	proposalCode varchar(50) NOT NULL COMMENT '��������',
	projectCode varchar(100) NOT NULL COMMENT '��Ŀ����',
	projectName varchar(200) NOT NULL COMMENT '��Ŀ����',
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
	typeno int(4) NOT NULL COMMENT '�汾����',
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT NULL COMMENT '״̬',
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


#DROP TABLE IF EXISTS yb_reconsider_verify
CREATE TABLE yb_reconsider_verify (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '�˶�����',
	applyDataId int(11) NOT NULL COMMENT '����������ϸ',  
	verifyDoctorCode varchar(50) NOT NULL COMMENT '�ο�����ҽ������',
	verifyDoctorName varchar(50) NOT NULL COMMENT '�ο�����ҽ��',
  verifyDeptCode varchar(100) NOT NULL COMMENT '�ο�������ұ���',
	verifyDeptName varchar(100) NOT NULL COMMENT '�ο��������',	
	changVerifyDoctorCode varchar(50) DEFAULT NULL COMMENT '�������ҽ������',
	changVerifyDoctorName varchar(50) DEFAULT NULL COMMENT '�������ҽ��',
  changVerifyDeptCode varchar(100) DEFAULT NULL COMMENT '���������ұ���',
	changVerifyDeptName varchar(100) DEFAULT NULL COMMENT '����������',	
	acceptState int(4) DEFAULT 0 COMMENT '����״̬',#0������1���ܣ�������
	refuseReason varchar(1000) DEFAULT NULL COMMENT '�ܾ�����',
	refuseDate datetime DEFAULT NULL COMMENT '�ܾ�����', 
	matchPersonId bigint(11) NOT NULL COMMENT 'ƥ���˴���',
	matchPersonName varchar(50) NOT NULL COMMENT 'ƥ����',
	matchDate datetime DEFAULT NULL COMMENT 'ƥ������', 
	reviewerId bigint(11) NOT NULL COMMENT '����˴���',
	reviewerName varchar(50) NOT NULL COMMENT '�����',
	reviewerDate datetime DEFAULT NULL COMMENT '�������', 
	sendPersonId bigint(11) NOT NULL COMMENT '�����˴���',
	sendPersonName varchar(50) NOT NULL COMMENT '������',	
	sendDate datetime DEFAULT NULL COMMENT '��������', 
  COMMENTS varchar(1000) DEFAULT NULL COMMENT '��ע',
  STATE int(4) DEFAULT 1 COMMENT '״̬',#1����ˡ�2����ˡ�3�ѷ���
  IS_DELETEMARK tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ɾ��',
  MODIFY_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '�޸�ʱ��',
  CREATE_TIME datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��',
  CREATE_USER_ID bigint(11) DEFAULT NULL COMMENT '������',
  MODIFY_USER_ID bigint(11) DEFAULT NULL COMMENT '�޸���',
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
	ad.hospitalizedNo,#סԺ��
	ad.treatmentMode,#��ҽ��ʽ
	ad.settlementDate,#��������
	ad.personalNo,#���˱��
	ad.insuredName,#�α�������
	ad.cardNumber,#ҽ������
	ad.areaName,#ͳ��������
	ad.versionNumber,#�汾��
	ad.backAppeal,#��������
	ad.typeno,	#�汾����
	ra.applydate, #��������
	ra.operatorid, #����Ա����'
  ra.operatorname #����Ա����,
from 
	yb_reconsider_verify rv 
	INNER JOIN yb_reconsider_apply_data ad ON
		ad.id = rv.applyDataId
	INNER JOIN yb_reconsider_apply ra ON
		ra.id = ad.pid


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

