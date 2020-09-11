<template>
  <a-drawer
    title="变更详情"
    :maskClosable="false"
    width=70%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="detailVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <appealData-module
    ref="appealDataModule"
    :ybAppealDataModule="ybAppealManageChangeDetail"
    >
    </appealData-module>
    <inpatientfees-module
    ref="inpatientfeesModule"
    :inpatientfeesModule="ybAppealManageChangeDetail"
    >
    </inpatientfees-module>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import AppealDataModule from '../ybFunModule/AppealDataModule'
import InpatientfeesModule from '../ybFunModule/InpatientfeesModule'
export default {
  name: 'YbAppealManageChangeDetail',
  components: {
    AppealDataModule, InpatientfeesModule},
  props: {
    detailVisiable: {
      default: false
    }
  },
  data () {
    return {
      ybAppealManageChangeDetail: {}
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    moment,
    onClose () {
      this.ybAppealManageChangeDetail = {}
      this.$emit('close')
    },
    setFormValues ({ ...ybAppealManageChangeDetail }) {
      this.ybAppealManageChangeDetail = ybAppealManageChangeDetail
      setTimeout(() => {
        this.$refs.inpatientfeesModule.search()
      }, 200)
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
