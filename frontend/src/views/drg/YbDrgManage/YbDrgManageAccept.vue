<template>
  <div id="tab" style="margin: 0px!important">
        <!-- 接受申请 表格区域 -->
        <a-table
          ref="TableInfo"
          :columns="columns"
          :rowKey="record => record.id"
          :dataSource="dataSource"
          :pagination="pagination"
          :loading="loading"
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange, getCheckboxProps: getCheckboxProps}"
          @change="handleTableChange"
          size="small"
          :bordered="bordered"
          :customRow="handleClickRow"
          :scroll="{ x: 900 }"
        >
          <template slot="operationLy" slot-scope="text, record, index">
            <span :title="record.ly">{{record.ly}}</span>
          </template>
          <template
            slot="operation"
            slot-scope="text, record, index"
          >
            <div class="editable-row-operations">
              <span>
                <a
                  @click.stop="() => look(record,index)"
                >查看</a>
                <a-divider type="vertical" />
                <a-popconfirm
                title="确定接受？"
                :disabled="record.isEnd===1?true:false"
                @confirm="() => accept(record)"
                >
                <a :disabled="record.isEnd===1?true:false" href="#">接受申诉</a>
                </a-popconfirm>
                <a-divider type="vertical" />
                <a
                  @click.stop="() => reject(record,index)"
                  :disabled="record.isEnd===1?true: record.enableType===1?false:true"
                >拒绝申诉</a>
              </span>
            </div>
          </template>
        </a-table>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'YbDrgManageAccept',
  props: {
    applyDate: {
      default: ''
    },
    searchItem: {
      default () {
        return {}
      }
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
      isDisabled: false,
      ybDrgManage: {},
      user: this.$store.state.account.user,
      tableFormat1: 'YYYY-MM-DD HH:mm:ss',
      tableFormat: 'YYYY-MM-DD'
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'orderNumber',
        fixed: 'left',
        width: 65
      },
      {
        title: '科室',
        dataIndex: 'ks',
        fixed: 'left',
        width: 130
      },
      {
        title: '就诊记录号',
        dataIndex: 'jzjlh',
        fixed: 'left',
        width: 100
      },
      {
        title: '病案号',
        dataIndex: 'bah',
        fixed: 'left',
        width: 90
      },
      {
        title: '违规类型',
        dataIndex: 'wglx',
        width: 90
      },
      {
        title: '问题描述',
        dataIndex: 'wtms',
        width: 300
      },
      {
        title: '医疗总费用',
        dataIndex: 'ylzfy',
        width: 100
      },
      {
        title: '违规金额',
        dataIndex: 'wgje',
        width: 100
      },
      {
        title: '是否编码造成直接错误',
        dataIndex: 'sfbmzczjcw',
        width: 110
      },
      {
        title: '理由',
        dataIndex: 'ly',
        scopedSlots: { customRender: 'operationLy' },
        ellipsis: true,
        width: 250
      },
      {
        title: '确认截止时间',
        dataIndex: 'enableDate',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            if (row.isEnableDate === 1) {
              return moment(text).format(this.tableFormat) + ' 24:00'
            } else {
              return moment(row.applyEndDate).format(this.tableFormat1)
            }
          } else {
            return text
          }
        },
        fixed: 'right',
        width: 108
      },
      {
        title: '复议截止日期',
        dataIndex: 'applyEndDate',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            if (isNaN(text) && !isNaN(Date.parse(text))) {
              return moment(text).format(this.tableFormat1)
            } else {
              return text
            }
          } else {
            return text
          }
        },
        fixed: 'right',
        width: 108
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 220
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    moment,
    warning () {
      this.$warning({
        title: '操作提示',
        content: '当前时间已超过可操作时间'
      })
    },
    rowNo (index) {
      return (this.pagination.defaultCurrent - 1) *
            this.pagination.defaultPageSize + index + 1
    },
    handleClickRow (record, index) {
      return {
        on: {
          click: () => {
            if (record.isEnd !== 1) {
              let target = this.selectedRowKeys.filter(key => key === record.id)[0]
              if (target === undefined) {
                this.selectedRowKeys.push(record.id)
              } else {
                this.selectedRowKeys.splice(this.selectedRowKeys.indexOf(record.id), 1)
              }
              this.emitAcceptSelectedRow(this.selectedRowKeys)
            }
          }
        }
      }
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
      this.emitAcceptSelectedRow(this.selectedRowKeys)
    },
    emitAcceptSelectedRow (selectedRowKeys) {
      this.$emit('acceptSelectedRow', selectedRowKeys.length > 0)
    },
    look (record, index) {
      record.rowNo = this.rowNo(index)
      this.$emit('look', record)
    },
    onHistory () {
      let selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length === 1) {
        let target = this.dataSource.filter(item => selectedRowKeys[0] === item.id)[0]
        let indOf = this.dataSource.indexOf(target)
        target.rowNo = this.rowNo(indOf)
        this.$emit('onHistoryLook', target)
      } else if (selectedRowKeys.length === 0) {
        this.$message.warning('未选择行')
      } else {
        this.$message.warning('请选择单行')
      }
    },
    accept (record) {
      this.loading = true
      let ybDrgManage = record
      ybDrgManage.state = 1
      let data = [{
        id: ybDrgManage.id,
        state: 1
      }]
      this.acceptService(data)
    },
    batchAccept () {
      this.loading = true
      let selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length > 0) {
        let data = []
        for (let key of selectedRowKeys) {
          let target = this.dataSource.filter(item => key === item.id)[0]
          let arrData = {
            id: target.id,
            state: 1
          }
          data.push(arrData)
        }
        if (data.length > 0) {
          this.acceptService(data)
        }
      } else {
        this.$message.warning('未选择行')
      }
      this.selectedRowKeys = []
      this.loading = false
    },
    acceptService (data) {
      let jsonString = JSON.stringify(data)
      this.$put('ybDrgManage/updateAcceptRejectState', {
        dataJson: jsonString
      }).then(() => {
        this.$message.success('接受成功')
        this.search()
      }).catch(() => {
        this.loading = false
      })
    },
    reject (record, index) {
      record.rowNo = this.rowNo(index)
      if (record.enableType === 1) {
        this.$emit('reject', record)
      } else {
        this.warning()
      }
    },
    searchPage () {
      this.pagination.defaultCurrent = 1
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
      }
      this.search()
    },
    getCheckboxProps (record) {
      return {
        props: {
          disabled: record.isEnd === 1
        }
      }
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
      params.applyDateStr = this.applyDate
      params.state = 0
      params.currencyField = this.searchItem.value
      params.areaType = this.user.areaType.value
      params.keyField = this.searchItem.keyField
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
      // params.sortField = 'ad.orderNum'
      // params.sortOrder = 'ascend'
      this.$get('ybDrgManageView/drgManageUserView', {
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
      this.emitAcceptSelectedRow(this.selectedRowKeys)
    }
  }
}
</script>
<style scoped>
/* .editable-row-operations a {
  margin-right: 8px;
} */
</style>
