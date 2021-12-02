<template>
  <div style="margin: 0px 10px 15px 15px">
    <a-row style="margin-bottom: 15px">
      <a-col :span="2">
        序&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：
      </a-col>
      <a-col :span="4">
        {{ ybDrgData.orderNumber }}
      </a-col>
      <a-col :span="2">
        科&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;室：
      </a-col>
      <a-col :span="4">
        {{ ybDrgData.ks }}
      </a-col>
      <a-col :span="2"> 就诊记录号： </a-col>
      <a-col :span="3">
        {{ ybDrgData.jzjlh }}
      </a-col>
      <a-col :span="3"> 病&nbsp;&nbsp;案&nbsp;&nbsp;号： </a-col>
      <a-col :span="4">
        {{ ybDrgData.bah }}
      </a-col>
    </a-row>
    <a-row style="margin-bottom: 10px">
      <a-col :span="2"> 违规类型： </a-col>
      <a-col :span="4">
        {{ ybDrgData.wglx }}
      </a-col>
      <a-col :span="2"> 医疗总费用： </a-col>
      <a-col :span="4">
        {{ ybDrgData.ylzfy }}
      </a-col>
      <a-col :span="2"> 违规金额： </a-col>
      <a-col :span="3">
        {{ ybDrgData.wgje }}
      </a-col>
      <a-col :span="3"> 是否编码造成<br />直接错误： </a-col>
      <a-col :span="4">
        {{ ybDrgData.sfbmzczjcw }}
      </a-col>
    </a-row>
    <a-row style="margin-bottom: 15px">
      <a-col :span="2"> 问题描述： </a-col>
      <a-col :span="20">
        {{ ybDrgData.wtms }}
      </a-col>
    </a-row>
    <a-row style="margin-bottom: 15px">
      <a-col :span="2">
        理&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;由：
      </a-col>
      <a-col :span="20">
        {{ ybDrgData.ly }}
      </a-col>
    </a-row>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'YbDrgDataModule',
  props: {
    ybDrgData: {}
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
