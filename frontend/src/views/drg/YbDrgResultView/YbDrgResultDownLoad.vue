<template>
  <div>
    <a-row type="flex" justify="center">
      <a-col :span=2>
        序号：
      </a-col>
      <a-col :span=3>
        <a-input-number :step="1" :min="1" v-model="startOrderNumber" />
      </a-col>
      <a-col :span=1>
        至
      </a-col>
      <a-col :span=3>
        <a-input-number :step="1" :min="2" v-model="endOrderNumber" />
      </a-col>
      <a-col :span=2>
        <a-button type="primary" @click="search">查询</a-button>
      </a-col>
      <a-col :span=13>
      </a-col>
    </a-row>
  <br>
  <!-- 表格区域 -->
  <a-table
    :columns="columns"
    :data-source="dataSource"
    :pagination="pagination"
    size="small"
    bordered :scroll="{ x: 700 }">
  <a slot="operation" slot-scope="text, record, index" @click="() => downloadFile(record)">
    {{record.orderNum === 0 ? '全部下载' : '下载'}}
  </a>
  </a-table>
  </div>
</template>
<script>
export default {
  name: 'YbDrgResultDownLoad',
  props: {
  },
  data () {
    return {
      dataSource: [],
      queryParams: {
      },
      drgResultDownLoad: {
      },
      startOrderNumber: 1,
      endOrderNumber: 10,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      user: this.$store.state.account.user,
      loading: false
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'orderNumber',
        fixed: 'left',
        width: 70
      },
      {
        title: '科室',
        dataIndex: 'ks',
        fixed: 'left',
        width: 120
      },
      {
        title: '就诊记录号',
        dataIndex: 'jzjlh',
        fixed: 'left',
        width: 90
      },
      {
        title: '病案号',
        dataIndex: 'bah',
        fixed: 'left',
        width: 80
      },
      {
        title: '复议医生',
        dataIndex: 'doctorName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.orderNum === 0 ? '-' : row.doctorCode + '-' + row.doctorName
          }
        },
        width: 120
      },
      {
        title: '复议科室',
        dataIndex: 'dksName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.dksId + '-' + row.dksName
          }
        },
        width: 200
      },
      {
        title: '文件个数',
        dataIndex: 'fileNumber',
        fixed: 'right',
        width: 80
      },
      {
        title: '文件大小',
        dataIndex: 'fileSizeWork',
        fixed: 'right',
        width: 90
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 85
      }]
    }
  },
  mounted () {
  },
  methods: {
    setFormValues ({ ...drgResultDownLoad }) {
      this.drgResultDownLoad = drgResultDownLoad
      this.startOrderNumber = 1
      this.endOrderNumber = 10
      this.search()
    },
    downloadFile (record) {
      let formData = {}
      formData.applyDateStr = record.applyDateStr
      formData.areaType = this.user.areaType.value
      if (record.orderNum !== 0) {
        formData.id = record.id
        formData.orderNumber = record.orderNumber
        formData.fileName = formData.applyDateStr + '-' + this.user.areaType.value + '-' +
        record.orderNumber + '-' + record.ks + '-' + record.jzjlh + '-' + record.bah + '-' + record.fileNumber

        this.$download('comFile/fileDrgImgZip', {
          ...formData
        }, formData.fileName + '.zip')
      } else {
        if (this.dataSource.length < 51) {
          let arr = record.orderNumber.split('-')
          if (arr.length === 1) {
            formData.startOrderNumber = arr[0]
            formData.endOrderNumber = arr[0]
          } else {
            formData.startOrderNumber = arr[0]
            formData.endOrderNumber = arr[1]
          }
          this.$download('comFile/fileDrgSumImgZip', {
            ...formData
          }, record.applyDateStr + '-' + this.user.areaType.value + '-' + record.orderNumber + '-' + record.fileNumber + '.zip')
        } else {
          this.$message.error('打包下载，筛选结果不得大于50条')
        }
      }
    },
    search () {
      this.dataSource = []
      this.queryParams = {}
      this.queryParams.applyDateStr = this.drgResultDownLoad.applyDateStr
      this.queryParams.areaType = this.user.areaType.value
      this.queryParams.startOrderNumber = this.startOrderNumber
      this.queryParams.endOrderNumber = this.endOrderNumber
      this.loading = true
      this.$get('ybDrgResultView/fileDownLoadList', {
        ...this.queryParams
      }).then((r) => {
        this.loading = false
        if (r.data.data.success === 1) {
          this.dataSource = r.data.data.data
        } else {
          this.$message.error(r.data.data.error)
        }
      }).catch(() => {
        this.loading = false
        this.$message.error('获取DRG打包下载列表失败!')
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
