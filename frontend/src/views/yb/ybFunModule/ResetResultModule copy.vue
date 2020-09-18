
<template>
    <div style="margin:5px 10px 5px 30px">
        <a-row
            justify="center"
            align="middle"
        >
            <a-col :span=6>
            <div style="height:40px">
                序号：{{ybResetResultModule.orderNumber}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                交易流水号：{{ybResetResultModule.serialNo}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                单据号：{{ybResetResultModule.billNo}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
            费用日期：{{ybResetResultModule.costDateStr}}
            </div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                项目编码：{{ybResetResultModule.projectCode}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                项目名称：{{ybResetResultModule.projectName}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
            住院号：{{ybResetResultModule.hospitalizedNo}}
            </div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                医保内金额：{{ybResetResultModule.medicalPrice}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                规则名称：{{ybResetResultModule.ruleName}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                扣除金额：{{ybResetResultModule.deductPrice}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=11>
            <div style="height:40px">
                扣除原因：{{ybResetResultModule.deductReason}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                还款原因：{{ybResetResultModule.repaymentReason}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                医生姓名：{{ybResetResultModule.doctorName}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                科室编码：{{ybResetResultModule.deptCode}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                科室名称：{{ybResetResultModule.deptName}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                就医方式：{{ybResetResultModule.treatmentMode}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                结算日期：{{ybResetResultModule.settlementDateStr}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                个人编号：{{ybResetResultModule.personalNo}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                参保人姓名：{{ybResetResultModule.insuredName}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                参保类型：{{ybResetResultModule.insuredType}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                &nbsp;
            </div>
            </a-col>
        </a-row>
    </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'ResetDataModule',
  props: {
    ybResetResultModule: {}
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
