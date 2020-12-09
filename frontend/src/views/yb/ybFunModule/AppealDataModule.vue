
<template>
    <div style="margin:0px 10px 15px 30px">
        <table width="100%">
            <tr style="height:45px">
                <td width="9%">序&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
                <td width="16%">{{ybAppealDataModule.orderNumber}}</td>
                <td width="10%">交易流水号：</td>
                <td width="15%">{{ybAppealDataModule.serialNo}}</td>
                <td width="8%">单&nbsp;&nbsp;据&nbsp;&nbsp;号：</td>
                <td width="17%">{{ybAppealDataModule.billNo}}</td>
                <td width="10%">意见书编码：</td>
                <td width="15%">{{ybAppealDataModule.proposalCode}}</td>
            </tr>
            <tr style="height:45px">
                <td width="9%">项目编码：</td>
                <td width="16%">{{ybAppealDataModule.projectCode}}</td>
                <td width="10%">项目名称：</td>
                <td width="15%">{{ybAppealDataModule.projectName}}</td>
                <td width="8%">数&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;量：</td>
                <td width="17%">{{ybAppealDataModule.num}}</td>
                <td width="10%">医保内金额：</td>
                <td width="15%">{{ybAppealDataModule.medicalPrice}}</td>
            </tr>
            <tr style="height:45px">
                <td width="9%">规则名称：</td>
                <td width="16%">{{ybAppealDataModule.ruleName}}</td>
                <td width="10%">扣除金额：</td>
                <td width="15%">{{ybAppealDataModule.deductPrice}}</td>
                <td width="8%">扣除原因：</td>
                <td width="42%" colspan="3">{{ybAppealDataModule.deductReason}}</td>
            </tr>
            <tr style="height:45px">
                <td width="9%">还款原因：</td>
                <td width="16%">{{ybAppealDataModule.repaymentReason}}</td>
                <td width="10%">医生姓名：</td>
                <td width="15%">{{ybAppealDataModule.doctorName}}</td>
                <td width="8%">科室编码：</td>
                <td width="17%">{{ybAppealDataModule.deptCode}}</td>
                <td width="10%">科室名称：</td>
                <td width="15%">{{ybAppealDataModule.deptName}}</td>
            </tr>
            <tr style="height:45px">
                <td width="9%">入院日期：</td>
                <td width="16%">{{ybAppealDataModule.enterHospitalDateStr}}</td>
                <td width="10%">出院日期：</td>
                <td width="15%">{{ybAppealDataModule.outHospitalDateStr}}</td>
                <td width="8%">费用日期：</td>
                <td width="17%">{{ybAppealDataModule.costDateStr}}</td>
                <td width="10%">住&nbsp;&nbsp;院&nbsp;&nbsp;号：</td>
                <td width="15%">{{ybAppealDataModule.hospitalizedNo}}</td>
            </tr>
            <tr style="height:45px">
                <td width="9%">就医方式：</td>
                <td width="16%">{{ybAppealDataModule.treatmentMode}}</td>
                <td width="10%">结算日期：</td>
                <td width="15%">{{ybAppealDataModule.settlementDateStr}}</td>
                <td width="8%">个人编号：</td>
                <td width="17%">{{ybAppealDataModule.personalNo}}</td>
                <td width="10%">参保人姓名：</td>
                <td width="15%">{{ybAppealDataModule.insuredName}}</td>
            </tr>
            <tr style="height:45px">
                <td width="9%">医保卡号：</td>
                <td width="16%">{{ybAppealDataModule.cardNumber}}</td>
                <td width="10%">统&nbsp;&nbsp;筹&nbsp;&nbsp;区：</td>
                <td width="15%">{{ybAppealDataModule.areaName}}</td>
                <td width="8%">版&nbsp;&nbsp;本&nbsp;&nbsp;号：</td>
                <td width="42%" colspan="3">{{ybAppealDataModule.versionNumber}}</td>
            </tr>
        </table>
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
