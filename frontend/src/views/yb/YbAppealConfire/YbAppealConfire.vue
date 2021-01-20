<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <a-row>
          <div :class="advanced ? null: 'fold'">
            <a-col
              :md="8"
              :sm="24"
            >
              <a-form-item
                label="职工"
                v-bind="formItemLayout"
              >
                <a-input v-model="queryParams.doctorContent" />
              </a-form-item>
            </a-col>
            <a-col
              :md="8"
              :sm="24"
            >
              <a-form-item
                label="科室"
                v-bind="formItemLayout"
              >
                <a-input v-model="queryParams.deptContent" />
              </a-form-item>
            </a-col>
            <a-col
              :md="8"
              :sm="24"
            >
              <a-form-item
                label="管理员类型"
                v-bind="formItemLayout"
              >
                <a-select v-model="queryParams.adminType" style="width: 100px" @change="handleAdminTypeChange"
                >
                  <a-select-option
                  v-for="d in selectAdminTypeDataSource"
                  :key="d.value"
                  >
                  {{ d.text }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
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
          v-hasPermission="['ybAppealConfire:add']"
          type="primary"
          ghost
          @click="add"
        >新增</a-button>
        <a-button
          v-hasPermission="['ybAppealConfire:delete']"
          @click="batchDelete"
        >删除</a-button>
        <a-dropdown v-hasPermission="['ybAppealConfire:export']">
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
        :rowKey="record => record. id                        "
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
            v-hasPermission="['ybAppealConfire:update']"
            type="setting"
            theme="twoTone"
            twoToneColor="#4a9ff5"
            @click="edit(record)"
            title="编辑"
          ></a-icon>
          <a-divider type="vertical" />
          <a-icon
            v-hasPermission="['ybAppealConfire:delete']"
            type="delete"
            theme="twoTone"
            twoToneColor="#4a9ff5"
            @click="del(record)"
            title="删除"
          ></a-icon>
          <a-badge
            v-hasNoPermission="['ybAppealConfire:update']"
            status="warning"
            text="无权限"
          ></a-badge>
        </template>
      </a-table>
    </div>
    <!-- 编辑字典 -->
    <ybAppealConfire-edit
      ref="ybAppealConfireEdit"
      @close="handleEditClose"
      @success="handleEditSuccess"
      :editVisiable="editVisiable"
    >
    </ybAppealConfire-edit>
  </a-card>
</template>

<script>
import YbAppealConfireEdit from './YbAppealConfireEdit'

const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbAppealConfire',
  components: { YbAppealConfireEdit },
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
        onChange: (current, size) => {
          this.pagination.defaultCurrent = current
          this.pagination.defaultPageSize = size
        },
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      queryParams: {
        adminType: 0
      },
      selectAdminTypeDataSource: [{text: '全部', value: 0}, {text: '联络员', value: 1}, {text: '科主任', value: 2}, {text: '护士长', value: 3}],
      editVisiable: false,
      loading: false,
      bordered: true
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        customRender: (text, row, index) => {
          return this.rowNo(index)
        },
        width: 75,
        fixed: 'left'
      },
      {
        title: '职工名称',
        dataIndex: 'doctorName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.doctorCode + '-' + row.doctorName
          }
        },
        width: 180
      },
      {
        title: '管理员类型',
        dataIndex: 'adminType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return '联络员'
            case 2:
              return '科主任'
            case 3:
              return '护士长'
            default:
              return text
          }
        },
        width: 120
      },
      {
        title: '管理科室',
        dataIndex: 'currencyField'
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 110
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    rowNo (index) {
      return (this.pagination.defaultCurrent - 1) *
            this.pagination.defaultPageSize + index + 1
    },
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
      this.editVisiable = false
      this.search()
    },
    handleAddClose () {
      this.editVisiable = false
    },
    add () {
      this.editVisiable = true
      this.$refs.ybAppealConfireEdit.setFormValues()
    },
    handleEditSuccess () {
      this.editVisiable = false
      this.search()
    },
    handleEditClose () {
      this.editVisiable = false
    },
    edit (record) {
      this.$refs.ybAppealConfireEdit.setFormValues(record)
      this.editVisiable = true
    },
    del (record) {
      let that = this
      this.$confirm({
        title: '确定删除该记录?',
        content: '当您点击确定按钮后，这条记录将会被彻底删除',
        centered: true,
        onOk () {
          let ybAppealConfireIds = record.id
          that.$delete('ybAppealConfire/' + ybAppealConfireIds).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          }
          )
        }
      })
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
          let ybAppealConfireIds = that.selectedRowKeys.join(',')
          that.$delete('ybAppealConfire/' + ybAppealConfireIds).then(() => {
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
    handleAdminTypeChange (value) {
      this.queryParams.adminType = value
    },
    exportExcel () {
      let { sortedInfo } = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.$export('ybAppealConfire/excel', {
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
      if (params.adminType === 0 || params.adminType === undefined) {
        params.adminType = null
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
      params.sortField = 'p.CREATE_TIME'
      this.$get('ybAppealConfire', {
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
