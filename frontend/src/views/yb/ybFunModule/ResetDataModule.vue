
<template>
    <div style="margin:5px 10px 5px 30px">
        <a-row
            justify="center"
            align="middle"
        >
            <a-col :span=6>
            <div style="height:40px">
                序号：{{ybResetDataModule.orderNumber}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                交易流水号：{{ybResetDataModule.serialNo}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                单据号：{{ybResetDataModule.billNo}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
            费用日期：{{ybResetDataModule.costDateStr}}
            </div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                项目编码：{{ybResetDataModule.projectCode}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                项目名称：{{ybResetDataModule.projectName}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
            住院号：{{ybResetDataModule.hospitalizedNo}}
            </div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                医保内金额：{{ybResetDataModule.medicalPrice}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                规则名称：{{ybResetDataModule.ruleName}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                扣除金额：{{ybResetDataModule.deductPrice}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=11>
            <div style="height:40px">
                扣除原因：{{ybResetDataModule.deductReason}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                还款原因：{{ybResetDataModule.repaymentReason}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                医生姓名：{{ybResetDataModule.doctorName}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                科室编码：{{ybResetDataModule.deptCode}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                科室名称：{{ybResetDataModule.deptName}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                就医方式：{{ybResetDataModule.treatmentMode}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                结算日期：{{ybResetDataModule.settlementDateStr}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                个人编号：{{ybResetDataModule.personalNo}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                参保人姓名：{{ybResetDataModule.insuredName}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                参保类型：{{ybResetDataModule.insuredType}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                统筹区：{{ybResetDataModule.areaName}}</div>
            </a-col>
        </a-row>
    </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'ResetDataModule',
  props: {
    ybResetDataModule: {}
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
