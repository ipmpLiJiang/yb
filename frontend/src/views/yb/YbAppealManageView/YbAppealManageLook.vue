<template>
  <a-drawer
    title="查看"
    :maskClosable="false"
    width=70%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="lookVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <appealData-module
    ref="appealDataModule"
    :ybAppealDataModule="ybAppealManageLook"
    >
    </appealData-module>
    <inpatientfees-module
    ref="inpatientfeesModule"
    :inpatientfeesModule="ybAppealManageLook"
    >
    </inpatientfees-module>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import AppealDataModule from '../ybFunModule/AppealDataModule'
import InpatientfeesModule from '../ybFunModule/InpatientfeesModule'
export default {
  name: 'YbAppealManageLook',
  components: {
    AppealDataModule, InpatientfeesModule},
  props: {
    lookVisiable: {
      default: false
    }
  },
  data () {
    return {
      ybAppealManageLook: {},
      ybAppealManage: {}
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    moment,
    onClose () {
      this.ybAppealManage = {}
      this.ybAppealManageLook = {}
      this.$emit('close')
    },
    setFormValues ({ ...ybAppealManageLook }) {
      this.ybAppealManageLook = ybAppealManageLook
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
