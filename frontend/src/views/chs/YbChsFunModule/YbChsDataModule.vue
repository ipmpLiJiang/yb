<template>
  <div style="margin: 0px 10px 15px 15px">
    <a-row style="margin-bottom: 15px">
      <a-col :span="3">
        序&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：
      </a-col>
      <a-col :span="4">
        {{ ybChsData.orderNum }}
      </a-col>
      <a-col :span="2">
        医疗类别：
      </a-col>
      <a-col :span="4">
        {{ ybChsData.medicalType }}
      </a-col>
      <a-col :span="2"> 住院门诊号： </a-col>
      <a-col :span="3">
        {{ ybChsData.zymzNumber }}
      </a-col>
      <a-col :span="3"> 参保人： </a-col>
      <a-col :span="3">
        {{ ybChsData.insuredName }}
      </a-col>
    </a-row>
    <a-row style="margin-bottom: 10px">
      <a-col :span="3"> 身份证号： </a-col>
      <a-col :span="4">
        {{ ybChsData.cardNumber }}
      </a-col>
      <a-col :span="2"> 入院日期： </a-col>
      <a-col :span="4">
        {{ ybChsData.enterHospitalDate }}
      </a-col>
      <a-col :span="2"> 出院日期： </a-col>
      <a-col :span="3">
        {{ ybChsData.outHospitalDate }}
      </a-col>
      <a-col :span="3"> 结算日期： </a-col>
      <a-col :span="3">
        {{ ybChsData.settlementDate }}
      </a-col>
    </a-row>
    <a-row style="margin-bottom: 10px">
      <a-col :span="3"> 医保项目编码： </a-col>
      <a-col :span="4">
        {{ ybChsData.projectCode }}
      </a-col>
      <a-col :span="2"> 医保项目名称： </a-col>
      <a-col :span="4">
        {{ ybChsData.projectName }}
      </a-col>
      <a-col :span="2"> 规则名称： </a-col>
      <a-col :span="3">
        {{ ybChsData.ruleName }}
      </a-col>
      <a-col :span="3"> 初审违规金额（元）： </a-col>
      <a-col :span="3">
        {{ ybChsData.violateCsPrice }}
      </a-col>
    </a-row>
    <a-row style="margin-bottom: 10px">
      <a-col :span="3"> 违规金额（元）： </a-col>
      <a-col :span="4">
        {{ ybChsData.violatePrice }}
      </a-col>
      <a-col :span="2"> 违规内容： </a-col>
      <a-col :span="4">
        {{ ybChsData.violateReason }}
      </a-col>
      <a-col :span="2"> 费用日期： </a-col>
      <a-col :span="3">
        {{ ybChsData.costDate }}
      </a-col>
      <a-col :span="3"> 险种类型： </a-col>
      <a-col :span="3">
        {{ ybChsData.insuredType }}
      </a-col>
    </a-row>
    <a-row style="margin-bottom: 10px">
      <a-col :span="3"> 单价（元）： </a-col>
      <a-col :span="4">
        {{ ybChsData.price }}
      </a-col>
      <a-col :span="2"> 数量： </a-col>
      <a-col :span="4">
        {{ ybChsData.num }}
      </a-col>
      <a-col :span="2"> 金额（元）： </a-col>
      <a-col :span="3">
        {{ ybChsData.medicalPrice }}
      </a-col>
      <a-col :span="3"> 统筹支付（元）： </a-col>
      <a-col :span="3">
        {{ ybChsData.tcPayPrice }}
      </a-col>
    </a-row>
    <a-row style="margin-bottom: 10px">
      <a-col :span="3"> 规格： </a-col>
      <a-col :span="4">
        {{ ybChsData.specs }}
      </a-col>
      <a-col :span="2"> 剂型： </a-col>
      <a-col :span="4">
        {{ ybChsData.jx }}
      </a-col>
      <a-col :span="2"> 机构等级： </a-col>
      <a-col :span="3">
        {{ ybChsData.jgLevel }}
      </a-col>
      <!-- <a-col :span="3"> 统筹支付（元）： </a-col>
      <a-col :span="4">
        {{ ybChsData. }}
      </a-col> -->
    </a-row>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'YbChsDataModule',
  props: {
    ybChsData: {}
  },
  data () {
    return {}
  },
  computed: {},
  mounted () {},
  methods: {
    moment
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
