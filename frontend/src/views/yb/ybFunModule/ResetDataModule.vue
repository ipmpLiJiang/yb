
<template>
    <div style="margin:0px 10px 15px 30px">
        <table width="100%">
            <tr style="height:45px">
                <td width="8%">序&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
                <td width="17%">{{ybResetDataModule.orderNumber}}</td>
                <td width="10%">交易流水号：</td>
                <td width="15%">{{ybResetDataModule.serialNo}}</td>
                <td width="8%">单&nbsp;&nbsp;据&nbsp;&nbsp;号：</td>
                <td width="17%">{{ybResetDataModule.billNo}}</td>
                <td width="10%">费用日期：</td>
                <td width="15%">{{ybResetDataModule.costDateStr}}</td>
            </tr>
            <tr style="height:45px">
                <td width="8%">项目编码：</td>
                <td width="17%">{{ybResetDataModule.projectCode}}</td>
                <td width="10%">项目名称：</td>
                <td width="15%">{{ybResetDataModule.projectName}}</td>
                <td width="8%">住&nbsp;&nbsp;院&nbsp;&nbsp;号：</td>
                <td width="17%">{{ybResetDataModule.hospitalizedNo}}</td>
                <td width="10%">医保内金额：</td>
                <td width="15%">{{ybResetDataModule.medicalPrice}}</td>
            </tr>
            <tr style="height:45px">
                <td width="8%">规则名称：</td>
                <td width="17%">{{ybResetDataModule.ruleName}}</td>
                <td width="10%">扣除金额：</td>
                <td width="15%">{{ybResetDataModule.deductPrice}}</td>
                <td width="8%">扣除原因：</td>
                <td width="42%" colspan="3">{{ybResetDataModule.deductReason}}</td>
            </tr>
            <tr style="height:45px">
                <td width="8%">还款原因：</td>
                <td width="17%">{{ybResetDataModule.repaymentReason}}</td>
                <td width="10%">医生姓名：</td>
                <td width="15%">{{ybResetDataModule.doctorName}}</td>
                <td width="8%">科室编码：</td>
                <td width="17%">{{ybResetDataModule.deptCode}}</td>
                <td width="10%">科室名称：</td>
                <td width="15%">{{ybResetDataModule.deptName}}</td>
            </tr>
            <tr style="height:45px">
                <td width="8%">就医方式：</td>
                <td width="17%">{{ybResetDataModule.treatmentMode}}</td>
                <td width="10%">结算日期：</td>
                <td width="15%">{{ybResetDataModule.settlementDateStr}}</td>
                <td width="8%">个人编号：</td>
                <td width="17%">{{ybResetDataModule.personalNo}}</td>
                <td width="10%">参保人姓名：</td>
                <td width="15%">{{ybResetDataModule.insuredName}}</td>
            </tr>
            <tr style="height:45px">
                <td width="8%">参保类型：</td>
                <td width="17%">{{ybResetDataModule.insuredType}}</td>
                <td width="10%">统&nbsp;&nbsp;筹&nbsp;&nbsp;区：</td>
                <td width="65%">{{ybResetDataModule.areaName}}</td>
            </tr>
        </table>
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
