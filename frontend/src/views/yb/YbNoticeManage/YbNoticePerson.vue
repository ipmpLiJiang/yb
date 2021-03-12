<template>
  <div>
    <a-table :columns="columns" :data-source="dataSource" :pagination="pagination" bordered :scroll="{ x: 500 }">
      <template
        slot="operation"
        slot-scope="text, record"
      >
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
  name: 'YbNoticePerson',
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
      ybNoticePerson: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '人员',
        dataIndex: 'personName'
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 100
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
    del (record) {
      let target = this.dataSource.filter(item => record.personCode === item.personCode)[0]
      const index = this.dataSource.indexOf(target)
      const newData = this.dataSource.slice()
      newData.splice(index, 1)
      const pagination = { ...this.pagination }
      pagination.total = newData.length
      this.dataSource = newData
      this.pagination = pagination
      this.$emit('delPerson', record.personCode)
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
        debugger
        let ds = []
        for (var i in data) {
          let item = {
            id: data[i].id,
            pid: data[i].pid,
            personCode: data[i].personCode,
            personName: data[i].personCode + '-' + data[i].personName
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
      let ds = [{id: '', pid: '', personCode: obj.personCode, personName: obj.personName}]
      for (var i in this.dataSource) {
        let item = {
          id: this.dataSource[i].id,
          pid: this.dataSource[i].pid,
          personCode: this.dataSource[i].personCode,
          personName: this.dataSource[i].personName
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
