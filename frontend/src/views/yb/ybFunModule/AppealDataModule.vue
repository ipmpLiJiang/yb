
<template>
    <div style="margin:0px 10px 15px 30px">
        <table width="100%">
            <tr style="height:45px">
                <td width="7%">序&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
                <td width="17%">{{ybAppealDataModule.orderNumber}}</td>
                <td width="8%">交易流水号：</td>
                <td width="19%">{{ybAppealDataModule.serialNo}}</td>
                <td width="7%">单&nbsp;&nbsp;据&nbsp;&nbsp;号：</td>
                <td width="16%">{{ybAppealDataModule.billNo}}</td>
                <td width="8%">意见书编码：</td>
                <td width="18%">{{ybAppealDataModule.proposalCode}}</td>
            </tr>
            <tr style="height:45px">
                <td width="7%">项目编码：</td>
                <td width="17%">{{ybAppealDataModule.projectCode}}</td>
                <td width="8%">项目名称：</td>
                <td width="19%">{{ybAppealDataModule.projectName}}</td>
                <td width="7%">数&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;量：</td>
                <td width="16%">{{ybAppealDataModule.num}}</td>
                <td width="8%">医保内金额：</td>
                <td width="18%">{{ybAppealDataModule.medicalPrice}}</td>
            </tr>
            <tr style="height:45px">
                <td width="7%">规则名称：</td>
                <td width="17%">{{ybAppealDataModule.ruleName}}</td>
                <td width="8%">扣除金额：</td>
                <td width="19%">{{ybAppealDataModule.deductPrice}}</td>
                <td width="7%">扣除原因：</td>
                <td width="42%" colspan="3">{{ybAppealDataModule.deductReason}}</td>
            </tr>
            <tr style="height:45px">
                <td width="7%">还款原因：</td>
                <td width="17%">{{ybAppealDataModule.repaymentReason}}</td>
                <td width="8%">医生姓名：</td>
                <td width="19%">{{ybAppealDataModule.doctorName}}</td>
                <td width="7%">科室编码：</td>
                <td width="16%">{{ybAppealDataModule.deptCode}}</td>
                <td width="8%">科室名称：</td>
                <td width="18%">{{ybAppealDataModule.deptName}}</td>
            </tr>
            <tr style="height:45px">
                <td width="7%">入院日期：</td>
                <td width="17%">{{ybAppealDataModule.enterHospitalDateStr}}</td>
                <td width="8%">出院日期：</td>
                <td width="19%">{{ybAppealDataModule.outHospitalDateStr}}</td>
                <td width="7%">费用日期：</td>
                <td width="16%">{{ybAppealDataModule.costDateStr}}</td>
                <td width="8%">住&nbsp;&nbsp;院&nbsp;&nbsp;号：</td>
                <td width="18%">{{ybAppealDataModule.hospitalizedNo}}</td>
            </tr>
            <tr style="height:45px">
                <td width="7%">就医方式：</td>
                <td width="17%">{{ybAppealDataModule.treatmentMode}}</td>
                <td width="8%">结算日期：</td>
                <td width="19%">{{ybAppealDataModule.settlementDateStr}}</td>
                <td width="7%">个人编号：</td>
                <td width="16%">{{ybAppealDataModule.personalNo}}</td>
                <td width="8%">参保人姓名：</td>
                <td width="18%">{{ybAppealDataModule.insuredName}}</td>
            </tr>
            <tr style="height:45px">
                <td width="7%">医保卡号：</td>
                <td width="17%">{{ybAppealDataModule.cardNumber}}</td>
                <td width="8%">统&nbsp;&nbsp;筹&nbsp;&nbsp;区：</td>
                <td width="19%">{{ybAppealDataModule.areaName}}</td>
                <td width="7%">版&nbsp;&nbsp;本&nbsp;&nbsp;号：</td>
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
