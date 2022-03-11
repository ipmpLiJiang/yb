<template>
<a-drawer
    title="发送短信"
    :maskClosable="false"
    width=75%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="smsVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
  <a-row justify="start" type="flex">
    <a-col :span=2 >
      &nbsp;
    </a-col>
    <a-col :span=11 >
    <a-checkbox :checked="checked"  @change="onChange">
      是否发送
    </a-checkbox>
    <a-input-search placeholder="请输入关键字" v-model="searchText" style="width: 160px" enter-button @search="search" />
    <a-popconfirm
        title="确定发送短信？"
        @confirm="sendSms"
        okText="确定"
        style="margin-left: 15px"
        cancelText="取消"
      >
        <a-button type="primary">发送短信</a-button>
      </a-popconfirm>
    </a-col>
  </a-row>
  <br>
  <div id="tab" style="margin: 0px!important">
    <!-- 表格区域 -->
    <a-table
      ref="TableInfo"
      :columns="columns"
      :rowKey="record => record.id"
      :dataSource="dataSource"
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange"
      size="small"
      :bordered="bordered"
      :scroll="{ x: 900 }"
    >
      <template slot="operationSendcontent" slot-scope="text, record, index">
        <span :title="record.sendcontent">{{record.sendcontent}}</span>
      </template>
    </a-table>
  </div>
</a-drawer>
</template>

<script>
import moment from 'moment'
export default {
  name: 'YbNoticeSms',
  props: {
    smsVisiable: {
      default () {
        return false
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
      searchText: '',
      checked: false,
      ybReconsiderSms: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '发送人',
        dataIndex: 'sendcode',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.sendcode + '-' + row.sendname
          }
        },
        fixed: 'left',
        width: 200
      },
      {
        title: '发送号码',
        dataIndex: 'mobile',
        width: 100
      },
      {
        title: '发送内容',
        dataIndex: 'sendcontent',
        scopedSlots: { customRender: 'operationSendcontent' },
        ellipsis: true,
        width: 500
      },
      {
        title: '状态',
        dataIndex: 'state',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '未发送'
            case 1:
              return '已发送'
            default:
              return text
          }
        },
        width: 80
      }]
    }
  },
  mounted () {
  },
  methods: {
    moment,
    rowNo (index) {
      return (this.pagination.defaultCurrent - 1) *
            this.pagination.defaultPageSize + index + 1
    },
    onChange () {
      this.checked = !this.checked
      setTimeout(() => {
        this.search()
      }, 500)
    },
    sendSms () {
      this.$put('comSms/sendSms', {
        refTableId: this.ybReconsiderSms.id,
        areaType: this.ybReconsiderSms.areaType,
        sendType: 7,
        state: 0
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.$message.success('发送成功.')
          this.search()
        } else {
          this.$message.warning(r.data.data.message)
        }
      }).catch(() => {
        this.$message.error('发送失败.')
      })
    },
    onClose () {
      this.ybReconsiderSms = {}
      this.$emit('close')
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    searchPage (obj) {
      this.pagination.defaultCurrent = 1
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
      }
      this.ybReconsiderSms = obj
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
      params.refTableId = this.ybReconsiderSms.id
      params.areaType = this.ybReconsiderSms.areaType
      params.sendType = 7
      params.comments = this.searchText
      if (this.checked) {
        params.state = 1
      } else {
        params.state = 0
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
      params.sortField = 'state'
      params.sortOrder = 'ascend'
      this.$get('comSms', {
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
