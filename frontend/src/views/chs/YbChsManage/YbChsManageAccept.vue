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
  name: 'YbChsManageAccept',
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
      ybChsManage: {},
      user: this.$store.state.account.user,
      tableFormat1: 'YYYY-MM-DD HH:mm:ss',
      tableFormat: 'YYYY-MM-DD'
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'orderNum',
        fixed: 'left',
        width: 70
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
        width: 90
      },
      {
        title: '参保人',
        dataIndex: 'insuredName',
        fixed: 'left',
        width: 80
      },
      {
        title: '入院日期',
        dataIndex: 'enterHospitalDate',
        width: 100
      },
      {
        title: '出院日期',
        dataIndex: 'outHospitalDate',
        width: 100
      },
      {
        title: '结算日期',
        dataIndex: 'settlementDate',
        width: 100
      },
      {
        title: '身份证号',
        dataIndex: 'cardNumber',
        width: 120
      },
      {
        title: '医保项目名称',
        dataIndex: 'projectName',
        width: 220,
        ellipsis: true
      },
      {
        title: '规则名称',
        dataIndex: 'ruleName',
        width: 200
      },
      {
        title: '初审违规金额（元）',
        dataIndex: 'violateCsPrice',
        width: 90
      },
      {
        title: '违规金额（元）',
        dataIndex: 'violatePrice',
        width: 90
      },
      {
        title: '违规内容',
        dataIndex: 'violateReason',
        width: 200,
        ellipsis: true
      },
      {
        title: '费用日期',
        dataIndex: 'costDate',
        width: 100
      },
      {
        title: '险种类型',
        dataIndex: 'insuredType',
        width: 110
      },
      {
        title: '单价（元）',
        dataIndex: 'price',
        width: 90
      },
      {
        title: '数量',
        dataIndex: 'num',
        width: 90
      },
      {
        title: '金额（元）',
        dataIndex: 'medicalPrice',
        width: 90
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
      let ybChsManage = record
      ybChsManage.state = 1
      let data = [{
        id: ybChsManage.id,
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
      this.$put('ybChsManage/updateAcceptRejectState', {
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
      if (params.keyField === 'orderNum' && params.currencyField) {
        let number = params.currencyField
        var numReg = /^[0-9]*$/
        var numRe = new RegExp(numReg)
        if (!numRe.test(number)) {
          this.$message.warning('请输入正确序号.')
          this.loading = false
          return false
        }
      }
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
      this.$get('ybChsManageView/chsManageUserView', {
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
