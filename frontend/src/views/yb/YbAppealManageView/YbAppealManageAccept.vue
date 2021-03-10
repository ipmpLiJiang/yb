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
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          @change="handleTableChange"
          :bordered="bordered"
          :customRow="handleClickRow"
          :scroll="{ x: 900 }"
        >
          <template slot="operationDeductReason" slot-scope="text, record, index">
            <span :title="record.deductReason">{{record.deductReason}}</span>
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
  name: 'YbAppealManageAccept',
  props: {
    applyDate: {
      default: ''
    },
    searchText: {
      default: ''
    },
    searchTypeno: {
      default: 1
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
      ybAppealManage: {},
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
        width: 70
      },
      {
        title: '意见书编码',
        dataIndex: 'proposalCode',
        fixed: 'left',
        width: 140
      },
      {
        title: '项目编码',
        dataIndex: 'projectCode',
        fixed: 'left',
        width: 120
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        fixed: 'left',
        width: 160
      },
      {
        title: '数量',
        dataIndex: 'num',
        width: 70
      },
      {
        title: '医保内金额',
        dataIndex: 'medicalPrice',
        width: 105
      },
      {
        title: '规则名称',
        dataIndex: 'ruleName',
        width: 140
      },
      {
        title: '扣除金额',
        dataIndex: 'deductPrice',
        width: 100
      },
      {
        title: '扣除原因',
        scopedSlots: { customRender: 'operationDeductReason' },
        ellipsis: true,
        width: 250
      },
      {
        title: '费用日期',
        dataIndex: 'costDateStr',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            if (isNaN(text) && !isNaN(Date.parse(text))) {
              return moment(text).format(this.tableFormat)
            } else {
              return text
            }
          } else {
            return text
          }
        },
        width: 110
      },
      {
        title: '科室名称',
        dataIndex: 'deptName',
        width: 100
      },
      {
        title: '医生姓名',
        dataIndex: 'doctorName',
        width: 100
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
        width: 125
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
        width: 125
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 230
      }]
    }
  },
  mounted () {
    this.initSearch()
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
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
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
      let ybAppealManage = record
      ybAppealManage.acceptState = 1
      let data = [{
        id: ybAppealManage.id,
        // sourceType: ybAppealManage.sourceType,
        // dataType: ybAppealManage.dataType,
        // applyDataId: ybAppealManage.applyDataId,
        // verifyId: ybAppealManage.verifyId,
        // readyDeptCode: ybAppealManage.readyDeptCode,
        // readyDeptName: ybAppealManage.readyDeptName,
        // readyDoctorCode: ybAppealManage.readyDoctorCode,
        // readyDoctorName: ybAppealManage.readyDoctorName,
        acceptState: 1
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
            // sourceType: target.sourceType,
            // dataType: target.dataType,
            // applyDataId: target.applyDataId,
            // verifyId: target.verifyId,
            // readyDeptCode: target.readyDeptCode,
            // readyDeptName: target.readyDeptName,
            // readyDoctorCode: target.readyDoctorCode,
            // readyDoctorName: target.readyDoctorName,
            acceptState: 1
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
      this.$put('ybAppealManage/updateAcceptRejectState', {
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
    initSearch () {
      let params = { applyDateStr: this.applyDate }
      this.$get('ybReconsiderApply/getTypeno', {
        ...params
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.$emit('setTypeno', parseInt(r.data.data.data))
          this.searchTypeno = parseInt(r.data.data.data)
          this.fetch()
        } else {
          this.$emit('setTypeno', 1)
          this.searchTypeno = 1
          this.fetch()
        }
      })
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
      params.acceptState = 0
      params.currencyField = this.searchText
      params.typeno = this.searchTypeno
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
      this.$get('ybAppealManageView/appealManageUserView', {
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
