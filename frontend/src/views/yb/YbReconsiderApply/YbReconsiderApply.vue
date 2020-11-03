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
                label="操作时间"
                v-bind="formItemLayout"
              >
                <a-date-picker v-model="queryParams.createTimeFrom" @change="oncreateTimeFromChange" />
              </a-form-item>
            </a-col>
            <a-col
              :md="8"
              :sm="24"
            >
              <a-form-item
                label="至"
                v-bind="formItemLayout"
              >
                <a-date-picker v-model="queryParams.createTimeTo" @change="oncreateTimeToChange" />
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
          v-hasPermission="['ybReconsiderApply:add']"
          type="primary"
          ghost
          @click="add"
        >新增</a-button>
        <a-button
          v-hasPermission="['ybReconsiderApply:delete']"
          @click="batchDelete"
        >删除</a-button>
        <a-dropdown v-hasPermission="['ybReconsiderApply:export']">
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
        :rowKey="record => record. id                      "
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
            v-hasPermission="['ybReconsiderApply:update']"
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
          >
            删除
          </a>
          <a-divider type="vertical" />
          <a @click="goto(record,1)">{{record.state==1||record.state==2?'上传审核一':'查看审核一'}}</a>
          <a-divider type="vertical" />
          <!-- 1待复议 2上传一 3申述一 4上传二 5申述二 6已剔除 7已还款 -->
          <a @click="goto(record,2)" :disabled="record.state==2||record.state==3||record.state==4||record.state==5||record.state==6||record.state==7?false:true">
            {{record.state==1||record.state==2||record.state==3||record.state==4?'上传审核二':'查看审核二'}}
          </a>
          <a-badge
            v-hasNoPermission="['ybReconsiderApply:update']"
            status="warning"
            text="无权限"
          ></a-badge>
        </template>
      </a-table>
    </div>
    <!-- 新增字典 -->
    <ybReconsiderApply-add
      ref="ybReconsiderApplyAdd"
      @close="handleAddClose"
      @success="handleAddSuccess"
      :addVisiable="addVisiable"
    >
    </ybReconsiderApply-add>
    <!-- 修改字典 -->
    <ybReconsiderApply-edit
      ref="ybReconsiderApplyEdit"
      @close="handleEditClose"
      @success="handleEditSuccess"
      :editVisiable="editVisiable"
    >
    </ybReconsiderApply-edit>
    <!-- 审核字典 -->
    <a-modal
      :title="uploadTitle"
      :visible="gotoVisiable"
      :footer="null"
      width="100%"
      style="padding-top:0px;"
      :maskClosable="false"
      @cancel="handleCancel"
    >
      <ybReconsiderApply-upload
      ref="ybReconsiderApplyUpload"
      @cancel="handleCancel"
      >
      </ybReconsiderApply-upload>
    </a-modal>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbReconsiderApplyAdd from './YbReconsiderApplyAdd'
import YbReconsiderApplyEdit from './YbReconsiderApplyEdit'
import YbReconsiderApplyUpload from './YbReconsiderApplyUpload'

const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbReconsiderApply',
  components: { YbReconsiderApplyAdd, YbReconsiderApplyEdit, YbReconsiderApplyUpload },
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
      addVisiable: false,
      editVisiable: false,
      loading: false,
      bordered: true,
      uploadTitle: '上传审核一',
      tableFormat: 'YYYY-MM-DD',
      gotoVisiable: false
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
        width: 100
      },
      {
        title: '审核一结束日期',
        dataIndex: 'endDateOne',
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
        width: 140
      },
      {
        title: '审核二结束日期',
        dataIndex: 'endDateTwo',
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
        width: 140
      },
      {
        title: '操作时间',
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
        width: 110
      },
      {
        title: '状态',
        dataIndex: 'state',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return '待复议'
            case 2:
              return '上传一'
            case 3:
              return '申述一'
            case 4:
              return '上传二'
            case 5:
              return '申述二'
            case 6:
              return '已剔除'
            case 7:
              return '还款中'
            default:
              return text
          }
        },
        width: 90
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' }
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
    oncreateTimeFromChange (date, dateString) {
      this.queryParams.createTimeFrom = dateString
    },
    oncreateTimeToChange (date, dateString) {
      this.queryParams.createTimeTo = dateString
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
      this.$refs.ybReconsiderApplyAdd.setFormValues()
      this.addVisiable = true
    },
    handleEditSuccess () {
      this.editVisiable = false
      // this.$message.success('修改成功')
      this.search()
    },
    handleEditClose () {
      this.editVisiable = false
    },
    edit (record) {
      this.$refs.ybReconsiderApplyEdit.setFormValues(record)
      this.editVisiable = true
    },
    handleCancel () {
      this.gotoVisiable = false
      // this.$message.success('审核上传成功')
      this.search()
    },
    handleGotoClose () {
      this.gotoVisiable = false
      // 审核上传页面关闭刷新列表
      this.search()
    },
    goto (record, typeno) {
      this.uploadTitle = typeno === 1 ? '上传审核一' : '上传审核二'
      setTimeout(() => {
        this.$refs.ybReconsiderApplyUpload.setFormValues(record, typeno)
      }, 200)
      this.gotoVisiable = true
    },
    del (record) {
      let that = this
      this.$confirm({
        title: '确定删除该记录?',
        content: '当您点击确定按钮后，这条记录将会被彻底删除',
        centered: true,
        onOk () {
          let ybReconsiderApplyIds = record.id
          that.$delete('ybReconsiderApply/' + ybReconsiderApplyIds).then(() => {
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
          let ybReconsiderApplyIds = that.selectedRowKeys.join(',')
          that.$delete('ybReconsiderApply/' + ybReconsiderApplyIds).then(() => {
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
      this.$export('ybReconsiderApply/excel', {
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
      params.sortField = 'create_Time'
      params.sortOrder = 'descend'
      // params.sortOrder = 'ascend'
      this.$get('ybReconsiderApply', {
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
<style scoped>
.ant-card-body {
    padding-top: 0px;
    zoom: 1;
}
</style>
