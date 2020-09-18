
<template>
    <div style="margin:5px 10px 5px 30px">
        <a-row
            justify="center"
            align="middle"
        >
            <a-col :span=6>
            <div style="height:40px;" v-if="ybResetResultModule.orderNumber!=''">
                序号：{{ybResetResultModule.orderNumber}}</div>
            <div style="height:40px;color:red" v-else>
                序号：{{ybResetResultModule.orderNumberNew}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                单据号：{{ybResetResultModule.billNo}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=8>
            <div style="height:40px">
                项目名称：{{ybResetResultModule.projectName}}</div>
            </a-col>
        </a-row>
        <a-row
            justify="center"
            align="middle"
        >
            <a-col :span=6>
            <div style="height:40px">
                扣款金额：{{ybResetResultModule.deductPrice}}</div>
            </a-col>
            <a-col :span=6>
            <div style="height:40px">
                还款金额：{{ybResetResultModule.repaymentPrice}}</div>
            </a-col>
            <a-col :span=1>
            &nbsp;
            </a-col>
            <a-col :span=8>
            <div style="height:40px">
                费用日期：{{ybResetResultModule.costDateStr}}</div>
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
