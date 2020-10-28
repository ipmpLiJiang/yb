<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <a-row>
          <div :class="advanced ? null: 'fold'">
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button
              type="primary"
              @click="search"
            >查询</a-button>
            <a-button
              style="margin-left: 8px"
              @click="reset"
            >重置</a-button>
            <a
              @click="toggleAdvanced"
              style="margin-left: 8px"
            >
              {{advanced ? '收起' : '展开'}}
              <a-icon :type="advanced ? 'up' : 'down'" />
            </a>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button
          v-hasPermission="['ybReconsiderApplyData:add']"
          type="primary"
          ghost
          @click="add"
        >新增</a-button>
        <a-button
          v-hasPermission="['ybReconsiderApplyData:delete']"
          @click="batchDelete"
        >删除</a-button>
        <a-dropdown v-hasPermission="['ybReconsiderApplyData:export']">
          <a-menu slot="overlay">
            <a-menu-item
              key="export-data"
              @click="exportExcel"
            >导出Excel</a-menu-item>
          </a-menu>
          <a-button>
            更多操作
            <a-icon type="down" />
          </a-button>
        </a-dropdown>
      </div>
      <!-- 表格区域 -->
      <a-table
        ref="TableInfo"
        :columns="columns"
        :rowKey="record => record. id                                                                        "
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        :bordered="bordered"
        :scroll="{ x: 900 }"
      >
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
          <a-icon
            v-hasPermission="['ybReconsiderApplyData:update']"
            type="setting"
            theme="twoTone"
            twoToneColor="#4a9ff5"
            @click="edit(record)"
            title="修改"
          ></a-icon>
          <a-badge
            v-hasNoPermission="['ybReconsiderApplyData:update']"
            status="warning"
            text="无权限"
          ></a-badge>
        </template>
      </a-table>
    </div>
    <!-- 新增字典 -->
    <ybReconsiderApplyData-add
      @close="handleAddClose"
      @success="handleAddSuccess"
      :addVisiable="addVisiable"
    >
    </ybReconsiderApplyData-add>
    <!-- 修改字典 -->
    <ybReconsiderApplyData-edit
      ref="ybReconsiderApplyDataEdit"
      @close="handleEditClose"
      @success="handleEditSuccess"
      :editVisiable="editVisiable"
    >
    </ybReconsiderApplyData-edit>
  </a-card>
</template>

<script>
import YbReconsiderApplyDataAdd from './YbReconsiderApplyDataAdd'
import YbReconsiderApplyDataEdit from './YbReconsiderApplyDataEdit'

const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbReconsiderApplyData',
  components: { YbReconsiderApplyDataAdd, YbReconsiderApplyDataEdit },
  data () {
    return {
      advanced: false,
      dataSource: [],
      selectedRowKeys: [],
      sortedInfo: null,
      paginationInfo: null,
      formItemLayout,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      queryParams: {
      },
      addVisiable: false,
      editVisiable: false,
      loading: false,
      bordered: true
    }
  },
  computed: {
    columns () {
      return [{
        title: '申请数据明细',
        dataIndex: 'id',
        width: 100
      },
      {
        title: '申请编号',
        dataIndex: 'pid',
        width: 100
      },
      {
        title: '交易流水号',
        dataIndex: 'serialNo',
        width: 135
      },
      {
        title: '单据号',
        dataIndex: 'billNo',
        width: 100
      },
      {
        title: '意见书编码',
        dataIndex: 'proposalCode',
        width: 140
      },
      {
        title: '项目编码',
        dataIndex: 'projectCode',
        width: 100
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        width: 140
      },
      {
        title: '数量',
        dataIndex: 'num',
        width: 70
      },
      {
        title: '医保内金额',
        dataIndex: 'medicalPrice',
        width: 100
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
        dataIndex: 'deductReason',
        width: 250
      },
      {
        title: '还款原因',
        dataIndex: 'repaymentReason',
        width: 100
      },
      {
        title: '科室编码',
        dataIndex: 'deptCode',
        width: 100
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
        title: '入院日期',
        dataIndex: 'enterHospitalDate',
        width: 110
      },
      {
        title: '出院日期',
        dataIndex: 'outHospitalDate',
        width: 110
      },
      {
        title: '费用日期',
        dataIndex: 'costDate',
        width: 110
      },
      {
        title: '住院号',
        dataIndex: 'hospitalizedNo',
        width: 100
      },
      {
        title: '就医方式',
        dataIndex: 'treatmentMode',
        width: 100
      },
      {
        title: '结算日期',
        dataIndex: 'settlementDate',
        width: 110
      },
      {
        title: '个人编号',
        dataIndex: 'personalNo',
        width: 100
      },
      {
        title: '参保人姓名',
        dataIndex: 'insuredName',
        width: 100
      },
      {
        title: '医保卡号',
        dataIndex: 'cardNumber',
        width: 100
      },
      {
        title: '统筹区名称',
        dataIndex: 'areaName',
        width: 100
      },
      {
        title: '版本号',
        dataIndex: 'versionNumber',
        width: 100
      },
      {
        title: '反馈申诉',
        dataIndex: 'backAppeal',
        width: 100
      },
      {
        title: '版本类型',
        dataIndex: 'typeno',
        width: 100
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
    this.fetch()
  },
  methods: {
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
      if (!this.advanced) {
        this.queryParams.comments = ''
      }
    },
    handleAddSuccess () {
      this.addVisiable = false
      this.$message.success('新增成功')
      this.search()
    },
    handleAddClose () {
      this.addVisiable = false
    },
    add () {
      this.addVisiable = true
    },
    handleEditSuccess () {
      this.editVisiable = false
      this.$message.success('修改成功')
      this.search()
    },
    handleEditClose () {
      this.editVisiable = false
    },
    edit (record) {
      this.$refs.ybReconsiderApplyDataEdit.setFormValues(record)
      this.editVisiable = true
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ybReconsiderApplyDataIds = that.selectedRowKeys.join(',')
          that.$delete('ybReconsiderApplyData/' + ybReconsiderApplyDataIds).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          }
          )
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    exportExcel () {
      let { sortedInfo } = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.$export('ybReconsiderApplyData/excel', {
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams
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
      this.$get('ybReconsiderApplyData', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      }
      )
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
