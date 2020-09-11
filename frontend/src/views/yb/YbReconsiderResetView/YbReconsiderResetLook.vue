<template>
  <a-drawer
    title="查看申诉材料"
    :maskClosable="false"
    width=50%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="lookVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
  查看申诉材料
  </a-drawer>
</template>

<script>
export default {
  name: 'YbReconsiderResetLook',
  props: {
    lookVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      ybAppealResult: {}
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    onClose () {
      this.loading = false
      this.ybAppealResult = {}
      this.$emit('close')
    },
    setFormValues ({ ...ybAppealResult }) {
      this.ybAppealResult = ybAppealResult
    }
  }
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
