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
              :md="1"
              :sm="24"
            >&nbsp;
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
            v-hasPermission="['ybNotice:userView']"
            type="eye" theme="twoTone"
            twoToneColor="#42b983"
            @click="view(record)"
            title="查看"></a-icon>
        </template>
        <template slot="operationExplain" slot-scope="text, record, index">
          <span :title="record.ntExplain">{{record.ntExplain}}</span>
        </template>
        <template slot="operationTitle" slot-scope="text, record, index">
          <span :title="record.ntTitle">{{record.ntTitle}}</span>
        </template>
      </a-table>
    </div>
    <!-- 查看字典 -->
    <ybNotice-look
      ref="ybNoticeLook"
      @close="handleLookClose"
      :lookVisiable="lookVisiable"
    >
    </ybNotice-look>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbNoticeLook from './YbNoticeLook'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbNotice',
  components: { YbNoticeLook },
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
      lookVisiable: false,
      loading: false,
      tableFormat: 'YYYY-MM-DD',
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
        scopedSlots: { customRender: 'operationTitle' },
        ellipsis: true,
        width: 180
      },
      {
        title: '内容简介',
        dataIndex: 'ntExplain',
        scopedSlots: { customRender: 'operationExplain' },
        ellipsis: true,
        width: 220
      },
      {
        title: '发布时间',
        dataIndex: 'releaseDate',
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
        width: 95
      },
      {
        title: '访问量',
        dataIndex: 'clickNum',
        width: 80
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
            case 0:
              return '待提交'
            case 1:
              return '已提交'
            case 2:
              return '已发布'
            default:
              return text
          }
        },
        width: 75
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 90
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    moment,
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
    view (record) {
      this.lookVisiable = true
      this.$refs.ybNoticeLook.setFormValues(record)
    },
    handleLookClose () {
      this.lookVisiable = false
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
      params.sortField = 'create_Time'
      params.sortOrder = 'descend'
      this.$get('ybNotice/findNoticeView', {
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
