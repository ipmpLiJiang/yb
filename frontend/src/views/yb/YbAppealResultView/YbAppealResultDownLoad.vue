<template>
  <div>
  <div style="color:red">{{deptNotMsg}}</div>
  <a-popconfirm
      title="确定验证科室？"
      v-show="this.appealResultDownLoad.type === 1 ? true : false"
      @confirm="checkDept"
      okText="确定"
      cancelText="取消"
    >
      <a-button type="primary">验证科室</a-button>
    </a-popconfirm>

  <!-- 表格区域 -->
  <a-table :columns="columns" :data-source="dataSource" bordered :scroll="{ x: 600 }">
  <a slot="operation" slot-scope="text, record, index" @click="() => downloadFile(record)">下载</a>
  </a-table>
  </div>
</template>
<script>
export default {
  name: 'YbAppealResultDownLoad',
  props: {
    tableSelectKey: {
      default: '1'
    }
  },
  data () {
    return {
      dataSource: [],
      queryParams: {
      },
      appealResultDownLoad: {
      },
      deptNotMsg: '',
      user: this.$store.state.account.user,
      loading: false
    }
  },
  computed: {
    columns () {
      return [{
        title: '压缩包文件名称',
        dataIndex: 'fileName'
      },
      {
        title: this.appealResultDownLoad.type === 0 ? '科室' : '汇总科室',
        dataIndex: 'deptName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            if (row.deptId !== '' && row.deptId !== null) {
              return row.deptId + '-' + row.deptName
            } else {
              return row.deptName
            }
          }
        },
        width: 250
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 150
      }]
    }
  },
  mounted () {
  },
  methods: {
    checkDept () {
      this.queryParams = {}
      this.queryParams.applyDateStr = this.appealResultDownLoad.applyDateStr

      if (this.appealResultDownLoad.typeno !== undefined) {
        if (this.tableSelectKey === '1' || this.tableSelectKey === '2') {
          this.queryParams.typeno = this.appealResultDownLoad.typeno
        }
      }
      this.queryParams.sourceType = this.appealResultDownLoad.sourceType

      if (this.tableSelectKey === '1' || this.tableSelectKey === '2') {
        this.queryParams.sourceType = 0
      } else {
        this.queryParams.state = 1
        this.queryParams.sourceType = 1
      }
      this.deptNotMsg = ''
      this.queryParams.dataType = this.appealResultDownLoad.dataType
      this.queryParams.areaType = this.user.areaType.value
      this.$get('ybAppealResultView/fileNotDeptList', {
        ...this.queryParams
      }).then((r) => {
        let data = r.data.data.data
        let notMsg = ''
        if (data !== null) {
          if (data.length > 0) {
            data.forEach(item => {
              if (notMsg === '') {
                notMsg = item.deptId + '-' + item.deptName
              } else {
                notMsg += ' , ' + item.deptId + '-' + item.deptName
              }
            })
            this.deptNotMsg = '以下科室未在汇总科室中配置：' + notMsg + '.'
          } else {
            this.deptNotMsg = '汇总科室配置已完成.'
          }
        } else {
          this.deptNotMsg = '汇总科室配置已完成.'
        }
      }).catch(() => {
        this.$message.error('获取导出图片列表失败!')
      })
    },
    downloadFile (record) {
      let formData = {}
      formData.deptName = record.deptName
      formData.deptId = record.deptId
      formData.sumId = record.key
      formData.applyDateStr = this.appealResultDownLoad.applyDateStr
      if (this.appealResultDownLoad.typeno !== undefined) {
        if (this.tableSelectKey === '1' || this.tableSelectKey === '2') {
          formData.typeno = this.appealResultDownLoad.typeno
        }
      }
      let f = ''
      formData.sourceType = this.appealResultDownLoad.sourceType
      if (formData.typeno === 1 || formData.typeno === 2) {
        f = this.appealResultDownLoad.typeno === 1 ? '第一版' : '第二版'
        formData.state = 12
        formData.sourceType = 0
        if (this.appealResultDownLoad.dataType === 0) {
          f = f + '_明细扣款'
        } else {
          f = f + '_主单扣款'
        }
      } else {
        f = '人工复议'
        formData.state = 1
        formData.sourceType = 1
      }
      formData.areaType = this.user.areaType.value
      formData.dataType = this.appealResultDownLoad.dataType
      formData.fileName = formData.applyDateStr + '_' + f + '_' + formData.deptName
      let methods = 'fileSumImgZip'
      if (this.appealResultDownLoad.type === 0) {
        methods = 'fileImgZip'
      }
      this.$download('comFile/' + methods, {
        ...formData
      }, formData.fileName + '.zip')
    },
    setFormValues ({ ...appealResultDownLoad }) {
      this.deptNotMsg = ''
      this.appealResultDownLoad = appealResultDownLoad
      this.search()
    },
    search () {
      this.dataSource = []
      this.queryParams = {}
      this.queryParams.applyDateStr = this.appealResultDownLoad.applyDateStr

      if (this.appealResultDownLoad.typeno !== undefined) {
        if (this.tableSelectKey === '1' || this.tableSelectKey === '2') {
          this.queryParams.typeno = this.appealResultDownLoad.typeno
        }
      }
      this.queryParams.sourceType = this.appealResultDownLoad.sourceType

      if (this.tableSelectKey === '1' || this.tableSelectKey === '2') {
        this.queryParams.sourceType = 0
      } else {
        this.queryParams.state = 1
        this.queryParams.sourceType = 1
      }

      this.queryParams.dataType = this.appealResultDownLoad.dataType
      this.queryParams.areaType = this.user.areaType.value
      this.loading = true
      let methods = 'fileDownLoadSumList'
      if (this.appealResultDownLoad.type === 0) {
        methods = 'fileDownLoadList'
      }
      this.$get('ybAppealResultView/' + methods, {
        ...this.queryParams
      }).then((r) => {
        this.loading = false
        this.dataSource = r.data.data.data
      }).catch(() => {
        this.loading = false
        this.$message.error('获取导出图片列表失败!')
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
