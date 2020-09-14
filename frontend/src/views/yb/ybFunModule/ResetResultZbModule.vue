
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
                单据号：{{ybResetResultModule.billNo}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
                医院编号：{{ybResetResultModule.hospitalCode}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
            医院名称：{{ybResetResultModule.hospitalName}}
            </div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                个人编号：{{ybResetResultModule.personalNo}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                参保人姓名：{{ybResetResultModule.insuredName}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=5>
            <div style="height:40px">
            就医方式：{{ybResetResultModule.treatmentMode}}
            </div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                项目名称：{{ybResetResultModule.projectName}}</div>
            </a-col>
        </a-row>
        <a-row justify='center'>
            <a-col :span=6>
            <div style="height:40px">
                扣除金额：{{ybResetResultModule.deductPrice}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                还款金额：{{ybResetResultModule.repaymentPrice}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=11>
            <div style="height:40px">
                &nbsp;</div>
            </a-col>
        </a-row>
    </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'ResetDataZbModule',
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
