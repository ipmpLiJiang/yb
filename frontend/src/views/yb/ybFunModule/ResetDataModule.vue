
<template>
    <div style="margin:0px 10px 15px 30px">
        <table width="100%">
            <tr style="height:45px">
                <td width="7%">序&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
                <td width="18%">{{ybResetDataModule.orderNumber}}</td>
                <td width="9%">交易流水号：</td>
                <td width="18%">{{ybResetDataModule.serialNo}}</td>
                <td width="8%">单&nbsp;&nbsp;据&nbsp;&nbsp;号：</td>
                <td width="15%">{{ybResetDataModule.billNo}}</td>
                <td width="9%">费用日期：</td>
                <td width="16%">{{ybResetDataModule.costDateStr}}</td>
            </tr>
            <tr style="height:45px">
                <td width="7%">项目编码：</td>
                <td width="18%">{{ybResetDataModule.projectCode}}</td>
                <td width="9%">项目名称：</td>
                <td width="18%">{{ybResetDataModule.projectName}}</td>
                <td width="8%">住&nbsp;&nbsp;院&nbsp;&nbsp;号：</td>
                <td width="15%">{{ybResetDataModule.hospitalizedNo}}</td>
                <td width="9%">医保内金额：</td>
                <td width="16%">{{ybResetDataModule.medicalPrice}}</td>
            </tr>
            <tr style="height:45px">
                <td width="7%">规则名称：</td>
                <td width="18%">{{ybResetDataModule.ruleName}}</td>
                <td width="9%">扣除金额：</td>
                <td width="18%">{{ybResetDataModule.deductPrice}}</td>
                <td width="8%">扣除原因：</td>
                <td width="40%" colspan="3">{{ybResetDataModule.deductReason}}</td>
            </tr>
            <tr style="height:45px">
                <td width="7%">还款原因：</td>
                <td width="18%">{{ybResetDataModule.repaymentReason}}</td>
                <td width="9%">医生姓名：</td>
                <td width="18%">{{ybResetDataModule.doctorName}}</td>
                <td width="8%">科室编码：</td>
                <td width="15%">{{ybResetDataModule.deptCode}}</td>
                <td width="9%">科室名称：</td>
                <td width="16%">{{ybResetDataModule.deptName}}</td>
            </tr>
            <tr style="height:45px">
                <td width="7%">就医方式：</td>
                <td width="18%">{{ybResetDataModule.treatmentMode}}</td>
                <td width="9%">结算日期：</td>
                <td width="18%">{{ybResetDataModule.settlementDateStr}}</td>
                <td width="8%">个人编号：</td>
                <td width="15%">{{ybResetDataModule.personalNo}}</td>
                <td width="9%">参保人姓名：</td>
                <td width="16%">{{ybResetDataModule.insuredName}}</td>
            </tr>
            <tr style="height:45px">
                <td width="7%">参保类型：</td>
                <td width="18%">{{ybResetDataModule.insuredType}}</td>
                <td width="9%">统&nbsp;&nbsp;筹&nbsp;&nbsp;区：</td>
                <td width="66%" colspan="5">{{ybResetDataModule.areaName}}</td>
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
