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
              :md="10"
              :sm="24"
            >
              <a-form-item
                label="关键词"
                v-bind="formItemLayout"
              >
                <a-input v-model="queryParams.currencyField" />
              </a-form-item>
            </a-col>
            <a-col
              :md="3"
              :sm="24"
            >
              <a-button
                type="primary"
                @click="showModal"
              >管理员类型维护</a-button>
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
          v-hasPermission="['ybNotice:add']"
          type="primary"
          ghost
          @click="add"
        >新增</a-button>
        <a-button
          v-hasPermission="['ybNotice:delete']"
          @click="batchDelete"
        >删除</a-button>
        <a-dropdown v-hasPermission="['ybNotice:export']">
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
            v-hasPermission="['ybNotice:update']"
            type="setting"
            theme="twoTone"
            twoToneColor="#4a9ff5"
            @click="edit(record)"
            title="编辑"
          ></a-icon>
          <a-divider type="vertical" />
          <a-icon
            v-hasPermission="['ybNotice:delete']"
            type="delete"
            theme="twoTone"
            twoToneColor="#4a9ff5"
            @click="del(record)"
            title="删除"
          ></a-icon>
          <a-badge
            v-hasNoPermission="['ybNotice:update']"
            status="warning"
            text="无权限"
          ></a-badge>
        </template>
      </a-table>
    </div>
    <!-- 编辑字典 -->
    <ybNotice-edit
      ref="ybNoticeEdit"
      @close="handleEditClose"
      @success="handleEditSuccess"
      :editVisiable="editVisiable"
    >
    </ybNotice-edit>
    <template>
      <div>
        <a-modal width="60%" :maskClosable="false" :footer="null" v-model="adminTypeVisible" title="管理员类型维护" @cancel="handleOk">
          <comType-data
          ref="comTypeData"
          @close="handleOk"
          >
          </comType-data>
        </a-modal>
      </div>
    </template>
  </a-card>
</template>

<script>
import YbNoticeEdit from './YbNoticeEdit'
import ComTypeData from '../../com/ComType/ComTypeData'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbNotice',
  components: { YbNoticeEdit, ComTypeData },
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
      },
      selectAdminTypeDataSource: [],
      editVisiable: false,
      adminTypeVisible: false,
      loading: false,
      ctType: 1,
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
        title: '标题',
        dataIndex: 'ntTitle',
        width: 180
      },
      {
        title: '内容简介',
        dataIndex: 'ntExplain',
        width: 180
      },
      {
        title: '发布时间',
        dataIndex: 'releaseDate',
        width: 100
      },
      {
        title: '访问量',
        dataIndex: 'clickNum',
        width: 70
      },
      {
        title: '操作员',
        dataIndex: 'operatorName',
        width: 70
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
    this.findComType()
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
      this.$refs.ybNoticeEdit.setFormValues(null, this.selectAdminTypeDataSource)
    },
    handleEditSuccess () {
      this.editVisiable = false
      this.search()
    },
    handleEditClose () {
      this.editVisiable = false
    },
    findComType () {
      let ctParams = {ctType: this.ctType}
      this.selectAdminTypeDataSource = []
      this.$get('comType/findList', {
        ...ctParams
      }).then((r) => {
        if (r.data.data.length > 0) {
          for (var i in r.data.data) {
            var at = {text: r.data.data[i].ctName, value: r.data.data[i].id}
            this.selectAdminTypeDataSource.push(at)
          }
        }
      }
      )
    },
    edit (record) {
      this.$refs.ybNoticeEdit.setFormValues(record, this.selectAdminTypeDataSource)
      this.editVisiable = true
    },
    del (record) {
      let that = this
      this.$confirm({
        title: '确定删除该记录?',
        content: '当您点击确定按钮后，这条记录将会被彻底删除',
        centered: true,
        onOk () {
          let ybNoticeIds = record.id
          that.$delete('ybNotice/' + ybNoticeIds).then(() => {
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
          let ybNoticeIds = that.selectedRowKeys.join(',')
          that.$delete('ybNotice/' + ybNoticeIds).then(() => {
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
    showModal () {
      this.adminTypeVisible = true
      setTimeout(() => {
        this.$refs.comTypeData.searchPage(this.ctType)
      }, 200)
    },
    handleOk (e) {
      this.adminTypeVisible = false
      this.findComType()
    },
    exportExcel () {
      let { sortedInfo } = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.$export('ybNotice/excel', {
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
      this.$get('ybNotice', {
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
