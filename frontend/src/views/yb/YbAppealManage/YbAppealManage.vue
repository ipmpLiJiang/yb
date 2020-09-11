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
          v-hasPermission="['ybAppealManage:add']"
          type="primary"
          ghost
          @click="add"
        >新增</a-button>
        <a-button
          v-hasPermission="['ybAppealManage:delete']"
          @click="batchDelete"
        >删除</a-button>
        <a-dropdown v-hasPermission="['ybAppealManage:export']">
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
        :rowKey="record => record. id                                                            "
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
            v-hasPermission="['ybAppealManage:update']"
            type="setting"
            theme="twoTone"
            twoToneColor="#4a9ff5"
            @click="edit(record)"
            title="修改"
          ></a-icon>
          <a-badge
            v-hasNoPermission="['ybAppealManage:update']"
            status="warning"
            text="无权限"
          ></a-badge>
        </template>
      </a-table>
    </div>
    <!-- 新增字典 -->
    <ybAppealManage-add
      @close="handleAddClose"
      @success="handleAddSuccess"
      :addVisiable="addVisiable"
    >
    </ybAppealManage-add>
    <!-- 修改字典 -->
    <ybAppealManage-edit
      ref="ybAppealManageEdit"
      @close="handleEditClose"
      @success="handleEditSuccess"
      :editVisiable="editVisiable"
    >
    </ybAppealManage-edit>
  </a-card>
</template>

<script>
import YbAppealManageAdd from './YbAppealManageAdd'
import YbAppealManageEdit from './YbAppealManageEdit'

const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbAppealManage',
  components: { YbAppealManageAdd, YbAppealManageEdit },
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
        title: '核对申请',
        dataIndex: 'id',
        width: 100
      },
      {
        title: '复议申请明细',
        dataIndex: 'applyDataId',
        width: 100
      },
      {
        title: '核对Id',
        dataIndex: 'verifyId',
        width: 100
      },
      {
        title: '复议医生编码',
        dataIndex: 'readyDoctorCode',
        width: 100
      },
      {
        title: '复议医生',
        dataIndex: 'readyDoctorName',
        width: 100
      },
      {
        title: '复议科室编码',
        dataIndex: 'readyDeptCode',
        width: 100
      },
      {
        title: '复议科室',
        dataIndex: 'readyDeptName',
        width: 100
      },
      {
        title: '变更医生编码',
        dataIndex: 'changDoctorCode',
        width: 100
      },
      {
        title: '变更医生',
        dataIndex: 'changDoctorName',
        width: 100
      },
      {
        title: '变更复议科室编码',
        dataIndex: 'changDeptCode',
        width: 100
      },
      {
        title: '变更复议科室',
        dataIndex: 'changDeptName',
        width: 100
      },
      {
        title: '接受状态',
        dataIndex: 'acceptState',
        width: 100
      },
      {
        title: '操作理由',
        dataIndex: 'operateReason',
        width: 100
      },
      {
        title: '操作日期',
        dataIndex: 'operateDate',
        width: 100
      },
      {
        title: '医保人代码',
        dataIndex: 'refuseId',
        width: 100
      },
      {
        title: '医保人',
        dataIndex: 'refuseName',
        width: 100
      },
      {
        title: '拒绝理由',
        dataIndex: 'refuseReason',
        width: 100
      },
      {
        title: '拒绝日期',
        dataIndex: 'refuseDate',
        width: 100
      },
      {
        title: '管理员代码',
        dataIndex: 'adminPersonId',
        width: 100
      },
      {
        title: '管理员',
        dataIndex: 'adminPersonName',
        width: 100
      },
      {
        title: '更改日期',
        dataIndex: 'adminChangeDate',
        width: 100
      },
      {
        title: '管理员理由',
        dataIndex: 'adminReason',
        width: 100
      },
      {
        title: '通用',
        dataIndex: 'currencyField',
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
      this.$refs.ybAppealManageEdit.setFormValues(record)
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
          let ybAppealManageIds = that.selectedRowKeys.join(',')
          that.$delete('ybAppealManage/' + ybAppealManageIds).then(() => {
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
      this.$export('ybAppealManage/excel', {
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
      this.$get('ybAppealManage', {
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
