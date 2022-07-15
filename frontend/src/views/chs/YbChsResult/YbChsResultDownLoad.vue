<template>
  <div>
    <a-row type="flex" justify="center">
      <a-col :span=2 style="margin-top:4px">
        序号：
      </a-col>
      <a-col :span=3>
        <a-input-number :step="10" :min="1" v-model="startOrderNum" />
      </a-col>
      <a-col :span=1 style="margin-top:4px">
        至
      </a-col>
      <a-col :span=3>
        <a-input-number :step="10" :min="10" v-model="endOrderNum" />
      </a-col>
      <a-col :span=2>
        <a-button type="primary" @click="search">查询</a-button>
      </a-col>
      <a-col :span=5>
      </a-col>
      <a-col :span=3 style="margin-top:4px">
        最大序号期间
      </a-col>
      <a-col :span=4>
        <a-input-number :step="10" :min="10" v-model="maxCount" />
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
      endOrderNum: 100,
      maxCount: 20,
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
        width: 120
      },
      {
        title: '文件个数',
        dataIndex: 'fileNumber',
        width: 120
      },
      {
        title: '文件大小',
        dataIndex: 'fileSizeWork'
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 120
      }]
    }
  },
  mounted () {
  },
  methods: {
    setFormValues ({ ...chsResultDownLoad }) {
      this.chsResultDownLoad = chsResultDownLoad
      this.startOrderNum = 1
      this.endOrderNum = 100
      this.search()
    },
    downloadFile (record) {
      let formData = {}
      formData.applyDateStr = record.applyDateStr
      formData.areaType = this.user.areaType.value
      formData.orderNumber = record.currencyField
      formData.refType = 'chs'
      formData.fileName = formData.applyDateStr + '-' + this.user.areaType.value + '-' + record.currencyField

      this.$download('comFile/fileChsZip', {
        ...formData
      }, formData.fileName + '.zip')
    },
    search () {
      this.dataSource = []
      this.queryParams = {}
      this.queryParams.applyDateStr = this.chsResultDownLoad.applyDateStr
      this.queryParams.areaType = this.user.areaType.value
      this.queryParams.startOrderNum = this.startOrderNum
      this.queryParams.endOrderNum = this.endOrderNum
      this.queryParams.maxCount = this.maxCount
      this.loading = true
      this.$get('ybChsResultView/fileDownLoadList', {
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
        this.$message.error('获取打包下载列表失败!')
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
