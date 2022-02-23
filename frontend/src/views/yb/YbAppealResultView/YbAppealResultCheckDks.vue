<template>
<div>
  <a-popconfirm
    title="确定更新汇总科室？"
    @confirm="updateDks"
    okText="确定"
    cancelText="取消"
  >
    <a-button type="primary">更新汇总科室</a-button>
  </a-popconfirm>
  <!-- 表格区域 -->
  <a-table :columns="columns" :data-source="dataSource" bordered :scroll="{ x: 450 }">
  </a-table>
</div>
</template>
<script>
export default {
  name: 'YbAppealResultCheckDks',
  data () {
    return {
      dataSource: [],
      queryParams: {
      },
      appealResultCheckDks: {
      },
      user: this.$store.state.account.user,
      loading: false
    }
  },
  computed: {
    columns () {
      return [{
        title: '复议科室编码',
        dataIndex: 'deptId',
        width: 150
      },
      {
        title: '复议科室名称',
        dataIndex: 'deptName'
      }]
    }
  },
  mounted () {
  },
  methods: {
    setFormValues ({ ...appealResultCheckDks }) {
      this.appealResultCheckDks = appealResultCheckDks
      this.queryParams = {}
      this.queryParams.applyDateStr = this.appealResultCheckDks.applyDateStr
      this.queryParams.areaType = this.user.areaType.value
      this.search()
    },
    updateDks () {
      this.$post('ybAppealResult/updateAppealResultCheckDks', {
        ...this.queryParams
      }).then((r) => {
        if (r.data.data.success === 0) {
          this.$message.error(r.data.data.message)
        } else {
          if (r.data.data.message === 'ok') {
            this.$message.success('更新成功.')
            this.search()
          } else {
            this.$message.warning(r.data.data.message)
          }
        }
      }).catch(() => {
        this.loading = false
      })
    },
    search () {
      this.dataSource = []
      this.loading = true
      this.$get('ybAppealResult/findAppealResultCheckDksList', {
        ...this.queryParams
      }).then((r) => {
        this.loading = false
        if (r.data.data.data !== null) {
          this.dataSource = r.data.data.data
        } else {
          this.$message.warning('无数据.')
        }
      }).catch(() => {
        this.loading = false
        this.$message.error('获取复议科室列表失败!')
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
