<template>
  <div>
    <a-table :columns="columns" :data-source="dataSource" :rowKey="record => record.id" :pagination="pagination" bordered :scroll="{ x: 500 }">
      <template
        slot="operation"
        slot-scope="text, record"
      >
        <a-popconfirm
        title="确定下载？"
        @confirm="() => download(record)"
        >
        <a>下载</a>
        </a-popconfirm>
        <a-divider type="vertical" />
        <a-popconfirm
        title="确定删除？"
        @confirm="() => del(record)"
        >
        <a>删除</a>
        </a-popconfirm>
      </template>
    </a-table>
  </div>
</template>

<script>
export default {
  name: 'YbNoticeFile',
  props: {
  },
  data () {
    return {
      dataSource: [],
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        onChange: (current, size) => {
          this.pagination.defaultCurrent = current
          this.pagination.defaultPageSize = size
        },
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      queryParams: {
      },
      loading: false,
      ybNoticeFile: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '文件名称',
        dataIndex: 'clientName'
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
    // this.search()
  },
  methods: {
    reset () {
      // 重置查询参数
      this.queryParams = {}
      this.dataSource = []
    },
    download (record) {
      let formData = {}
      formData.id = record.id
      formData.folderName = 'NoticeDoc'
      this.$download('comFile/downloadFile', {
        ...formData
      })
    },
    del (record) {
      let formData = {}
      formData.id = record.id
      formData.fileName = 'NoticeDoc'
      this.$post('comFile/delFile', {
        ...formData
      }).then((r) => {
        this.uploading = false
        if (r.data.data.success === 1) {
          this.$message.success('删除成功')
          let target = this.dataSource.filter(item => record.id === item.id)[0]
          const index = this.dataSource.indexOf(target)
          const newData = this.dataSource.slice()
          newData.splice(index, 1)
          const pagination = { ...this.pagination }
          pagination.total = newData.length
          this.dataSource = newData
          this.pagination = pagination
          this.$emit('delFile', record.id)
        } else {
          this.$message.error(r.data.data.message)
        }
      }).catch(() => {
        this.$message.error('删除失败.')
      })
    },
    searchPage (data) {
      this.pagination.defaultCurrent = 1
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
      }
      const pagination = { ...this.pagination }
      if (data === undefined) {
        pagination.total = 0
        this.dataSource = []
      } else {
        let ds = []
        for (var i in data) {
          let item = {
            id: data[i].id,
            clientName: data[i].clientName
          }
          ds.push(item)
        }
        pagination.total = ds.length
        this.dataSource = ds
      }
      this.pagination = pagination
    },
    add (obj) {
      this.pagination.defaultCurrent = 1
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
      }
      const pagination = { ...this.pagination }
      let ds = [{id: obj.id, clientName: obj.clientName}]
      for (var i in this.dataSource) {
        let item = {
          id: this.dataSource[i].id,
          clientName: this.dataSource[i].clientName
        }
        ds.push(item)
      }
      pagination.total = ds.length
      this.dataSource = ds
      this.pagination = pagination
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
