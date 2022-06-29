
<template>
  <!-- 已审核 表格区域 -->
  <a-table
    ref="TableInfo"
    :columns="columns"
    :rowKey="record => record.id"
    :dataSource="dataSource"
    :pagination="pagination"
    :loading="loading"
    :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
    @change="handleTableChange"
    size="small"
    :bordered="bordered"
    :scroll="{ x: 900 }"
  >
    <template slot="operationDeductReason" slot-scope="text, record, index">
      <span :title="record.deductReason">{{record.deductReason}}</span>
    </template>
    <template
      slot="remark"
      slot-scope="text, record"
    >
      <a-popover placement="topLeft">
        <template slot="content">
          <div style="max-width: 200px">{{text}}</div>
        </template>
        <p style="width: 200px;margin-bottom: 0">{{text}}</p>
      </a-popover>
    </template>
    <template
      slot="operation"
      slot-scope="text, record"
    >
      <a-popconfirm
      title="确定发送？"
      @confirm="() => send(record.id)"
      >
      <a>发送</a>
      </a-popconfirm>
    </template>
  </a-table>
</template>

<script>
import moment from 'moment'
export default {
  name: 'YbChsSendStayed',
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
      ybChsVerify: {},
      user: this.$store.state.account.user,
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
        width: 100
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
        width: 150,
        ellipsis: true
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
        width: 150,
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
        title: '复议科室',
        dataIndex: 'verifyDksName',
        fixed: 'right',
        width: 180
      },
      {
        title: '复议医生',
        dataIndex: 'verifyDoctorName',
        fixed: 'right',
        width: 120
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 70
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
    send (key) {
      this.loading = true
      let target = this.dataSource.filter(item => key === item.id)[0]
      if (target !== undefined) {
        let data = [{
          id: target.id,
          applyDataId: target.applyDataId,
          verifyDoctorCode: target.verifyDoctorCode,
          verifyDoctorName: target.verifyDoctorName,
          verifyDksId: target.verifyDksId,
          verifyDksName: target.verifyDksName,
          applyDateStr: target.applyDateStr,
          orderNum: target.orderNum,
          areaType: this.user.areaType.value
        }]
        this.sendService(data)
      } else {
        this.$message.error('未找到对象')
      }
    },
    batchSend () {
      this.loading = true
      let selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length > 0) {
        let data = []
        for (let key of selectedRowKeys) {
          let target = this.dataSource.filter(item => key === item.id)[0]
          if (target !== undefined) {
            let arrData = {
              id: target.id,
              applyDataId: target.applyDataId,
              verifyDoctorCode: target.verifyDoctorCode,
              verifyDoctorName: target.verifyDoctorName,
              verifyDksId: target.verifyDksId,
              verifyDksName: target.verifyDksName,
              applyDateStr: target.applyDateStr,
              orderNum: target.orderNum,
              areaType: this.user.areaType.value}

            data.push(arrData)
          }
        }
        if (data.length > 0) {
          this.sendService(data)
        } else {
          this.$message.warning('未找到对象')
          this.$emit('verifySpin')
        }
      } else {
        this.$message.warning('未选择行')
        this.$emit('verifySpin')
      }
      this.selectedRowKeys = []
      this.loading = false
    },
    sendService (data) {
      let jsonString = JSON.stringify(data)
      this.$put('ybChsVerify/updateSendState', {
        dataJson: jsonString, areaType: this.user.areaType.value
      }).then(() => {
        this.$message.success('发送成功')
        this.$emit('verifySpin')
        this.search()
      }).catch(() => {
        this.$emit('verifySpin')
        this.loading = false
      })
    },
    batchSendA () {
      if (this.dataSource.length > 0) {
        this.$put('ybChsVerify/updateASendState', {
          applyDateStr: this.applyDate, areaType: this.user.areaType.value, state: 2
        }).then(() => {
          this.$message.success('发送成功')
          this.$emit('verifySpin')
          this.search()
        }).catch(() => {
          this.$emit('verifySpin')
          this.loading = false
        })
      } else {
        this.$emit('verifySpin')
        this.$message.warning('无数据，无法全部发送!')
      }
    },
    batchAllBack () {
      if (this.dataSource.length > 0) {
        this.$put('ybChsVerify/updateBackState', {
          applyDateStr: this.applyDate, areaType: this.user.areaType.value
        }).then(() => {
          this.$message.success('返回状态成功')
          this.$emit('verifySpin')
          this.search()
        }).catch(() => {
          this.$emit('verifySpin')
          this.loading = false
        })
      } else {
        this.$emit('verifySpin')
        this.$message.warning('无数据，无法全部返回!')
      }
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
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
      params.applyDateStr = this.applyDate
      params.state = 2
      params.areaType = this.user.areaType.value
      if (this.searchItem !== undefined) {
        if (this.searchItem.item.zymzNumber !== '') {
          params.zymzNumber = this.searchItem.item.zymzNumber
        }
        if (this.searchItem.item.insuredName !== '') {
          params.insuredName = this.searchItem.item.insuredName
        }
        if (this.searchItem.item.projectName !== '') {
          params.projectName = this.searchItem.item.projectName
        }
        if (this.searchItem.doctor.docCode !== '') {
          params.verifyDoctorCode = this.searchItem.doctor.docCode
        }
        if (this.searchItem.doctor.docName !== '') {
          params.verifyDoctorName = this.searchItem.doctor.docName
        }
        if (this.searchItem.dept.dksName !== '') {
          params.verifyDksName = this.searchItem.dept.dksName
        }
        if (this.searchItem.order.orderNum !== '') {
          params.orderNum = this.searchItem.order.orderNum
        } else {
          params.orderNum = null
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
      // params.sortField = 'rv.orderNum'
      // params.sortOrder = 'ascend'
      this.$get('ybChsVerifyView', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
        this.cacheData = this.dataSource.map(item => ({ ...item }))
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
