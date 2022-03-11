<template>
  <div id="tab" style="margin: 0px!important">
        <!-- 表格区域 -->
        <a-table
          ref="TableInfo"
          :columns="columns"
          :rowKey="record => record.id"
          :dataSource="dataSource"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
          size="small"
          :bordered="bordered"
          :scroll="{ x: 900 }"
        >
        </a-table>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'YbDrgJk',
  props: {
    applyDateStr: {
      default: ''
    },
    areaType: {
      default: 0
    }
  },
  data () {
    return {
      dataSource: [],
      selectedRowKeys: [],
      sortedInfo: null,
      paginationInfo: null,
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
      bordered: true,
      ybDrgApplyTask: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'orderNumber',
        width: 70
      },
      {
        title: '入院日期',
        dataIndex: 'ryDate',
        width: 120
      },
      {
        title: '出院日期',
        dataIndex: 'cyDate',
        width: 120
      },
      {
        title: '统筹支付',
        dataIndex: 'tczf',
        width: 120
      },
      {
        title: 'DRG分组编码',
        dataIndex: 'fzCode',
        width: 150
      },
      {
        title: 'DRG分组名称',
        dataIndex: 'fzName',
        width: 250
      },
      {
        title: '医保主要诊断编码',
        dataIndex: 'zyzdCode',
        width: 150
      },
      {
        title: '医保主要诊断名称',
        dataIndex: 'zyzdName',
        width: 250
      },
      {
        title: '医保主手术编码',
        dataIndex: 'zssCode',
        width: 150
      },
      {
        title: '医保主手术名称',
        dataIndex: 'zssName',
        width: 250
      },
      {
        title: '其他诊断编码',
        dataIndex: 'qtzdCode',
        width: 200
      },
      {
        title: '其他诊断名称',
        dataIndex: 'qtzdName',
        width: 300
      },
      {
        title: '其他手术编码',
        dataIndex: 'qtssCode',
        width: 150
      },
      {
        title: '其他手术名称',
        dataIndex: 'qtssName',
        width: 250
      },
      {
        title: '科室',
        dataIndex: 'deptName',
        width: 180
      },
      {
        title: '病区',
        dataIndex: 'areaName',
        width: 180
      },
      {
        title: '权重',
        dataIndex: 'qz',
        width: 100
      },
      {
        title: '科主任',
        dataIndex: 'kzrDocName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.kzrDocId + '-' + row.kzrDocName
          }
        },
        width: 140
      },
      {
        title: '主任医师',
        dataIndex: 'zrysDocName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.zrysDocId + '-' + row.zrysDocName
          }
        },
        width: 140
      },
      {
        title: '主治医师',
        dataIndex: 'zzysDocName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.zzysDocId + '-' + row.zzysDocName
          }
        },
        width: 140
      },
      {
        title: '住院医师',
        dataIndex: 'zyysDocName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.zyysDocId + '-' + row.zyysDocName
          }
        },
        width: 140
      },
      {
        title: '医疗组科室',
        dataIndex: 'ylzDeptName',
        width: 120
      },
      {
        title: '医疗组医师',
        dataIndex: 'ylzDocName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.ylzDocId + '-' + row.ylzDocName
          }
        },
        width: 140
      }]
    }
  },
  mounted () {
    // this.fetch()
  },
  methods: {
    moment,
    rowNo (index) {
      return (this.pagination.defaultCurrent - 1) *
            this.pagination.defaultPageSize + index + 1
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    edit (record, index) {
      record.rowNo = this.rowNo(index)
      this.$emit('edit', record)
    },
    searchPage () {
      this.pagination.defaultCurrent = 1
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
      }
      this.search()
    },
    search () {
      let { sortedInfo } = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.pagination.defaultCurrent = 1
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列排序规则
      this.sortedInfo = null
      this.paginationInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      this.sortedInfo = sorter
      this.paginationInfo = pagination
      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams
      })
    },
    fetch (params = {}) {
      this.loading = true
      params.applyDateStr = this.applyDateStr
      params.areaType = this.areaType
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.pageSize = this.paginationInfo.pageSize
        params.pageNum = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.pagination.defaultPageSize
        params.pageNum = this.pagination.defaultCurrent
      }
      params.sortField = 'orderNum'
      params.sortOrder = 'ascend'
      this.$get('ybDrgJk/findDrgJkList', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      })
      this.selectedRowKeys = []
    }
  }
}
</script>

<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
