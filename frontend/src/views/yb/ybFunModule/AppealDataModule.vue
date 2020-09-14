
<template>
    <div style="margin:5px 10px 5px 30px">
        <a-row
            justify="center"
            align="middle"
        >
            <a-col :span=6>
            <div style="height:40px">
                序号：{{ybAppealDataModule.orderNumber}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                交易流水号：{{ybAppealDataModule.serialNo}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                单据号：{{ybAppealDataModule.billNo}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                意见书编码：{{ybAppealDataModule.proposalCode}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                项目编码：{{ybAppealDataModule.projectCode}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                项目名称：{{ybAppealDataModule.projectName}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                数量：{{ybAppealDataModule.num}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                医保内金额：{{ybAppealDataModule.medicalPrice}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                规则名称：{{ybAppealDataModule.ruleName}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                扣除金额：{{ybAppealDataModule.deductPrice}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=11>
            <div style="height:40px">
                扣除原因：{{ybAppealDataModule.deductReason}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                还款原因：{{ybAppealDataModule.repaymentReason}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                医生姓名：{{ybAppealDataModule.doctorName}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                科室编码：{{ybAppealDataModule.deptCode}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                科室名称：{{ybAppealDataModule.deptName}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                入院日期：{{ybAppealDataModule.enterHospitalDateStr}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                出院日期：{{ybAppealDataModule.outHospitalDateStr}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                费用日期：{{ybAppealDataModule.costDateStr}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                住院号：{{ybAppealDataModule.hospitalizedNo}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                就医方式：{{ybAppealDataModule.treatmentMode}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                结算日期：{{ybAppealDataModule.settlementDateStr}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                个人编号：{{ybAppealDataModule.personalNo}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                参保人姓名：{{ybAppealDataModule.insuredName}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                医保卡号：{{ybAppealDataModule.cardNumber}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                统筹区：{{ybAppealDataModule.areaName}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                版本号：{{ybAppealDataModule.versionNumber}}</div>
            </a-col>
            <a-col :span=6>
            &nbsp;
            </a-col>
        </a-row>
    </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'YbAppealDataModule',
  props: {
    ybAppealDataModule: {}
  },
  data () {
    return {
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    moment
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
