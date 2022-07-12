<template>
  <div>
    <a-row type="flex" justify="center">
      <a-col :span=2>
        序号：
      </a-col>
      <a-col :span=3>
        <a-input-number :step="1" :min="1" v-model="startOrderNum" />
      </a-col>
      <a-col :span=1>
        至
      </a-col>
      <a-col :span=3>
        <a-input-number :step="1" :min="2" v-model="endOrderNum" />
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
  name: 'YbChsResultDownLoad',
  props: {
  },
  data () {
    return {
      dataSource: [],
      queryParams: {
      },
      chsResultDownLoad: {
      },
      startOrderNum: 1,
      endOrderNum: 10,
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
        dataIndex: 'currencyField',
        fixed: 'left',
        width: 100
      },
      {
        title: '医疗类别',
        dataIndex: 'medicalType',
        fixed: 'left',
        width: 100
      },
      {
        title: '住院门诊号',
        dataIndex: 'zymzNumber',
        fixed: 'left',
        width: 100
      },
      {
        title: '参保人',
        dataIndex: 'insuredName',
        fixed: 'left',
        width: 90
      },
      {
        title: '复议医生',
        dataIndex: 'resultDoctorName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.orderNum === 0 ? '-' : row.resultDoctorCode + '-' + row.resultDoctorName
          }
        },
        width: 120
      },
      {
        title: '复议科室',
        dataIndex: 'resultDksName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.resultDksId + '-' + row.resultDksName
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
    setFormValues ({ ...chsResultDownLoad }) {
      this.chsResultDownLoad = chsResultDownLoad
      this.startOrderNum = 1
      this.endOrderNum = 10
      this.search()
    },
    downloadFile (record) {
      let formData = {}
      formData.applyDateStr = record.applyDateStr
      formData.areaType = this.user.areaType.value
      if (record.orderNum !== 0) {
        formData.id = record.id
        formData.orderNum = record.orderNum
        formData.fileName = formData.applyDateStr + '-' + this.user.areaType.value + '-' +
        record.orderNum + '-' + record.ks + '-' + record.jzjlh + '-' + record.bah + '-' + record.fileNum

        this.$download('comFile/fileChsImgZip', {
          ...formData
        }, formData.fileName + '.zip')
      } else {
        if (this.dataSource.length < 51) {
          let arr = record.orderNum.split('-')
          if (arr.length === 1) {
            formData.startOrderNum = arr[0]
            formData.endOrderNum = arr[0]
          } else {
            formData.startOrderNum = arr[0]
            formData.endOrderNum = arr[1]
          }
          this.$download('comFile/fileChsSumImgZip', {
            ...formData
          }, record.applyDateStr + '-' + this.user.areaType.value + '-' + record.orderNum + '-' + record.fileNum + '.zip')
        } else {
          this.$message.error('打包下载，筛选结果不得大于50条')
        }
      }
    },
    search () {
      this.dataSource = []
      this.queryParams = {}
      this.queryParams.applyDateStr = this.chsResultDownLoad.applyDateStr
      this.queryParams.areaType = this.user.areaType.value
      this.queryParams.startOrderNum = this.startOrderNum
      this.queryParams.endOrderNum = this.endOrderNum
      this.loading = true
      this.$get('ybChsResultView/fileDownLoadList', {
        ...this.queryParams
      }).then((r) => {
        debugger
        this.loading = false
        if (r.data.data.success === 1) {
          this.dataSource = r.data.data.data
        } else {
          this.$message.error(r.data.data.error)
        }
      }).catch(() => {
        this.loading = false
        this.$message.error('获取打包下载列表失败!')
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
