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
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          @change="handleTableChange"
          :customRow="handleClickRow"
          :bordered="bordered"
          :scroll="{ x: 900 }"
        >
          <template
            slot="operation"
            slot-scope="text, record, index"
          >
            <div class="editable-row-operations">
              <span>
                <a
                  @click.stop="() => edit(record)"
                >录入还款方案</a>
              </span>
            </div>
          </template>
        </a-table>
  </div>
</template>

<script>
import moment from 'moment'
import { custom } from '../../js/custom'
export default {
  name: 'YbAppealResultRepaymentStayedEmp',
  props: {
    applyDateStr: {
      default: ''
    },
    applyDateToStr: {
      default: ''
    },
    defaultFormatDate: {
      default: ''
    },
    searchText: {
      default: ''
    },
    searchDataType: {
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
      ybAppealResultDeductImplement: {}
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
        title: '单据号',
        dataIndex: 'billNo',
        fixed: 'left',
        width: 110
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        fixed: 'left',
        width: 210
      },
      {
        title: '扣除金额',
        dataIndex: 'deductPrice',
        width: 100
      },
      {
        title: '费用日期',
        dataIndex: 'costDateStr',
        width: 130
      },
      {
        title: '还款金额',
        dataIndex: 'repaymentPrice',
        width: 100
      },
      {
        title: '科室名称',
        dataIndex: 'arDeptName',
        width: 130
      },
      {
        title: '医生姓名',
        dataIndex: 'arDoctorName',
        width: 110
      },
      {
        title: '扣款类型',
        dataIndex: 'dataType',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '明细扣款'
            case 1:
              return '主单扣款'
            default:
              return text
          }
        },
        fixed: 'right',
        width: 90
      },
      {
        title: '保险类型',
        dataIndex: 'repayType',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '居保'
            case 1:
              return '职保'
            default:
              return '无'
          }
        },
        fixed: 'right',
        width: 90
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
    moment,
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    handleClickRow (record, index) {
      return {
        on: {
          click: () => {
            let target = this.selectedRowKeys.filter(key => key === record.id)[0]
            if (target === undefined) {
              this.selectedRowKeys.push(record.id)
            } else {
              this.selectedRowKeys.splice(this.selectedRowKeys.indexOf(record.id), 1)
            }
          }
        }
      }
    },
    edit (record) {
      record.edit = 0
      this.$emit('edit', record)
    },
    batchRepayment () {
      this.loading = true
      let selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length > 0) {
        let data = []
        for (let key of selectedRowKeys) {
          let target = this.dataSource.filter(item => key === item.id)[0]
          if (target !== undefined) {
            let arrData = {
              resetDataId: target.resetDataId,
              resultId: target.resultId,
              deductImplementId: target.deductImplementId,
              applyDateStr: target.applyDateStr,
              dataType: target.dataType
            }

            data.push(arrData)
          }
        }
        if (data.length > 0) {
          this.repaymentService(data)
        } else {
          this.$message.warning('未找到对象')
        }
      } else {
        this.$message.warning('未选择行')
      }
      this.selectedRowKeys = []
      this.loading = false
    },
    repaymentService (data) {
      let jsonString = JSON.stringify(data)
      this.$put('ybAppealResultRepayment/createRepayment', {
        dataJson: jsonString
      }).then(() => {
        this.$message.success('还款成功')
        this.search()
      }).catch(() => {
        this.loading = false
      })
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
      let dateStr = this.applyDateStr
      let dateToStr = this.applyDateToStr

      let arrDateStr = custom.resetApplyDateStr(dateStr, dateToStr, this.defaultFormatDate)
      dateStr = arrDateStr[0]
      dateToStr = arrDateStr[1]

      let msg = custom.checkApplyDateStr(dateStr, dateToStr, 3)
      if (msg === '') {
        this.loading = true
        params.applyDateFrom = dateStr
        params.applyDateTo = dateToStr
        params.currencyField = this.searchText
        params.shareProgramme = 'null'
        if (this.searchDataType !== 2) {
          params.dataType = this.searchDataType
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
        params.sortField = 'applyDateStr asc,orderNum'
        // params.sortOrder = 'descend'
        params.sortOrder = 'ascend'
        this.$get('ybAppealResultRepaymentView', {
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
      } else {
        this.$message.error(msg)
      }
    }
  }
}
</script>

<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
