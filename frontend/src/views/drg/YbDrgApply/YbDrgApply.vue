<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <a-row>
          <div :class="advanced ? null : 'fold'"></div>
          <span style="float: right; margin-top: 3px">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
            <a @click="toggleAdvanced" style="margin-left: 8px">
              {{ advanced ? "收起" : "展开" }}
              <a-icon :type="advanced ? 'up' : 'down'" />
            </a>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button
          v-hasPermission="['ybDrgApply:add']"
          type="primary"
          ghost
          @click="add"
          >新增</a-button
        >
        <a-button v-hasPermission="['ybDrgApply:delete']" @click="batchDelete"
          >删除</a-button
        >
        <a-dropdown v-hasPermission="['ybDrgApply:export']">
          <a-menu slot="overlay">
            <a-menu-item key="export-data" @click="exportExcel"
              >导出Excel</a-menu-item
            >
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
        :rowKey="(record) => record.id"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        :rowSelection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        @change="handleTableChange"
        :bordered="bordered"
        :scroll="{ x: 900 }"
      >
        <template slot="remark" slot-scope="text, record">
          <a-popover placement="topLeft">
            <template slot="content">
              <div style="max-width: 200px">{{ text }}</div>
            </template>
            <p style="width: 200px; margin-bottom: 0">{{ text }}</p>
          </a-popover>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon
            v-hasPermission="['ybDrgApply:update']"
            type="setting"
            theme="twoTone"
            twoToneColor="#4a9ff5"
            @click="edit(record)"
            title="修改"
          ></a-icon>
          <a-divider type="vertical" />
          <a
            v-hasPermission="['ybReconsiderApply:delete']"
            @click="del(record)"
            :disabled="record.state==1?false:true"
            title="删除"
          >
          <a-icon
            type="delete"
            theme="outlined"
            twoToneColor="record.state==1?#4a9ff5:#D3D3D3"
          ></a-icon>
          </a>
          <a-divider type="vertical" />
          <a @click="goto(record)">上传数据</a>
          <a-badge
            v-hasNoPermission="['ybDrgApply:update']"
            status="warning"
            text="无权限"
          ></a-badge>
        </template>
      </a-table>
    </div>
    <!-- 新增字典 -->
    <ybDrgApply-add
      ref="ybDrgApplyAdd"
      @close="handleAddClose"
      @success="handleAddSuccess"
      :addVisiable="addVisiable"
    >
    </ybDrgApply-add>
    <!-- 修改字典 -->
    <ybDrgApply-edit
      ref="ybDrgApplyEdit"
      @close="handleEditClose"
      @success="handleEditSuccess"
      :editVisiable="editVisiable"
    >
    </ybDrgApply-edit>
    <!-- 审核字典 -->
    <a-modal
      title="上传数据"
      :visible="gotoVisiable"
      :footer="null"
      width="99%"
      style="padding-top:0px;"
      :maskClosable="false"
      @cancel="handleCancel"
    >
      <ybDrgApply-upload
      ref="ybDrgApplyUpload"
      @cancel="handleCancel"
      >
      </ybDrgApply-upload>
    </a-modal>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbDrgApplyAdd from './YbDrgApplyAdd'
import YbDrgApplyEdit from './YbDrgApplyEdit'
import YbDrgApplyUpload from './YbDrgApplyUpload'

const formItemLayout = {
  labelCol: {
    span: 8
  },
  wrapperCol: {
    span: 15,
    offset: 1
  }
}
export default {
  name: 'YbDrgApply',
  components: {
    YbDrgApplyAdd,
    YbDrgApplyEdit,
    YbDrgApplyUpload
  },
  data () {
    return {
      advanced: false,
      dataSource: [],
      selectedRowKeys: [],
      sortedInfo: null,
      paginationInfo: null,
      formItemLayout,
      tableFormat: 'YYYY-MM-DD',
      tableFormat1: 'YYYY-MM-DD HH:mm:ss',
      user: this.$store.state.account.user,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      queryParams: {},
      addVisiable: false,
      editVisiable: false,
      gotoVisiable: false,
      loading: false,
      bordered: true
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        customRender: (text, record, index) =>
          `${(this.pagination.defaultCurrent - 1) *
            this.pagination.defaultPageSize +
            index +
            1}`,
        width: 70
      },
      {
        title: '复议年月',
        dataIndex: 'applyDateStr',
        width: 90
      },
      {
        title: '操作员',
        dataIndex: 'operatorName',
        width: 130
      },
      {
        title: '状态',
        dataIndex: 'state',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return '待上传'
            case 2:
              return '已上传'
            default:
              return text
          }
        },
        width: 80
      },
      {
        title: '截止日期',
        dataIndex: 'endDate',
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
        width: 160
      },
      {
        title: '确认日期',
        dataIndex: 'enableDate',
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
        width: 135
      },
      {
        title: '创建日期',
        dataIndex: 'createTime',
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
        width: 115
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 200
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    moment,
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
      if (!this.advanced) {
        this.queryParams.comments = ''
      }
    },
    handleCancel () {
      this.gotoVisiable = false
      // this.$message.success('审核上传成功')
      this.search()
    },
    goto (record) {
      setTimeout(() => {
        this.$refs.ybDrgApplyUpload.setFormValues(record)
      }, 200)
      this.gotoVisiable = true
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
      this.$refs.ybDrgApplyAdd.setFormValues()
      this.addVisiable = true
    },
    del (record) {
      let that = this
      this.$confirm({
        title: '确定删除该记录?',
        content: '当您点击确定按钮后，这条记录将会被彻底删除',
        centered: true,
        onOk () {
          let ybReconsiderApplyIds = record.id
          that.$delete('ybDrgApply/' + ybReconsiderApplyIds).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          }
          )
        }
      })
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
      this.$refs.ybDrgApplyEdit.setFormValues(record)
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
          let ybDrgApplyIds = that.selectedRowKeys.join(',')
          that.$delete('ybDrgApply/' + ybDrgApplyIds).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    exportExcel () {
      let {
        sortedInfo
      } = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.$export('ybDrgApply/excel', {
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams
      })
    },
    search () {
      let {
        sortedInfo
      } = this
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
      this.$get('ybDrgApply', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = {
          ...this.pagination
        }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
