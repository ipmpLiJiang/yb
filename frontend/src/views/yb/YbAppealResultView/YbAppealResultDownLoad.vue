<template>
    <!-- 表格区域 -->
    <a-table :columns="columns" :data-source="dataSource" bordered :scroll="{ x: 600 }">
    <a slot="operation" slot-scope="text, record, index" @click="() => downloadFile(record)">下载</a>
    </a-table>
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
        title: '科室',
        dataIndex: 'deptName',
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
    downloadFile (record) {
      let formData = {}
      formData.deptName = record.deptName
      formData.applyDateStr = this.appealResultDownLoad.applyDateStr
      if (this.appealResultDownLoad.typeno !== undefined) {
        if (this.tableSelectKey === '1' || this.tableSelectKey === '2') {
          formData.typeno = this.appealResultDownLoad.typeno
        }
      }
      let f = ''
      formData.sourceType = this.appealResultDownLoad.sourceType
      if (formData.typeno === 1 || formData.typeno === 2) {
        f = this.appealResultDownLoad.typeno === 1 ? '审核一' : '审核二'
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

      formData.dataType = this.appealResultDownLoad.dataType
      formData.fileName = formData.applyDateStr + '_' + f + '_' + formData.deptName
      this.$download('comFile/fileImgZip', {
        ...formData
      }, formData.fileName + '.zip')
    },
    setFormValues ({ ...appealResultDownLoad }) {
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
        this.queryParams.state = 12
        this.queryParams.sourceType = 0
      } else {
        this.queryParams.state = 1
        this.queryParams.sourceType = 1
      }

      this.queryParams.dataType = this.appealResultDownLoad.dataType

      this.loading = true
      this.$get('ybAppealResultView/fileDownLoadList', {
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
