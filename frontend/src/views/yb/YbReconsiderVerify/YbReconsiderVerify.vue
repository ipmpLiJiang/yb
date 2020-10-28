
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
      <template>
        <div id="tab">
          <a-tabs
            type="card"
            @change="callback"
          >
            <a-tab-pane
              key="1"
              tab="待审核"
            >
              <!-- 待审核 表格区域 -->
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
                  slot='verifyDoctorCode'
                  slot-scope="text, record, index"
                >
                  <div key='verifyDoctorCode'>
                    <a-input
                      v-if="record.editable"
                      style="margin: -5px 0"
                      :value="text"
                      @change="e => handleChange(e.target.value, record.id, 'verifyDoctorCode')"
                    />
                    <template v-else>
                      {{ text }}
                    </template>
                  </div>
                </template>
                <template
                  slot='verifyDoctorName'
                  slot-scope="text, record, index"
                >
                  <div key='verifyDoctorName'>
                    <a-select
                      v-if="record.editable"
                      show-search
                      :value="selectValue"
                      placeholder="请输入关键词"
                      style="width: 100%"
                      :default-active-first-option="false"
                      :filter-option="false"
                      :not-found-content="null"
                      @search="handleSearch"
                      @change="e => handleChange(e, record.id, 'verifyDoctorName')"
                    >
                      <a-icon
                        slot="suffixIcon"
                        type="search"
                      ></a-icon>
                      <a-select-option
                        v-for="d in selectDataSource"
                        :key="d.value"
                      >
                        {{ d.text }}
                      </a-select-option>
                    </a-select>
                    <template v-else>
                      {{ text }}
                    </template>
                  </div>
                </template>
                <template
                  slot="operation"
                  slot-scope="text, record, index"
                >
                  <div class="editable-row-operations">
                    <span v-if="record.editable">
                      <a @click="() => save(record.id)">确定</a>
                      <a-divider type="vertical" />
                      <a-popconfirm
                        title="Sure to cancel?"
                        @confirm="() => cancel(record.id)"
                      >
                        <a>取消</a>
                      </a-popconfirm>
                    </span>
                    <span v-else>
                      <a
                        :disabled="editingKey !== ''"
                        @click="() => edit(record.id)"
                      >查看详情</a>
                      <a-divider type="vertical" />
                      <a
                        :disabled="editingKey !== ''"
                        @click="() => edit(record.id)"
                      >核对</a>
                    </span>
                  </div>
                </template>
              </a-table>
            </a-tab-pane>
            <a-tab-pane
              key="2"
              tab="已审核"
            >
              <!-- 已审核 表格区域 -->
              <a-table
                ref="TableInfoTwo"
                :columns="columnsTwo"
                :rowKey="record => record.id"
                :dataSource="dataSourceTwo"
                :pagination="paginationTwo"
                :loading="loadingTwo"
                :rowSelection="{selectedRowKeys: selectedRowKeysTwo, onChange: onSelectChangeTwo}"
                @change="handleTableChangeTwo"
                :bordered="borderedTwo"
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
                    v-hasPermission="['ybReconsiderVerify:update']"
                    type="setting"
                    theme="twoTone"
                    twoToneColor="#4a9ff5"
                    title="修改"
                  ></a-icon>
                  <a-badge
                    v-hasNoPermission="['ybReconsiderVerify:update']"
                    status="warning"
                    text="无权限"
                  ></a-badge>
                </template>
              </a-table>
            </a-tab-pane>
            <a-tab-pane
              key="3"
              tab="已完成"
            >
              Content of Tab Pane 3
            </a-tab-pane>
          </a-tabs>
        </div>
      </template>
    </div>
  </a-card>
</template>

<script>

const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbReconsiderVerify',
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
      loading: false,
      bordered: true,
      dataSourceTwo: [],
      selectedRowKeysTwo: [],
      sortedInfoTwo: null,
      paginationInfoTwo: null,
      paginationTwo: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      queryParamsTwo: {
      },
      loadingTwo: false,
      editingKey: '',
      cacheData: [],
      selectDataSource: [], // 搜索事件
      selectValue: undefined,
      borderedTwo: true
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
        title: '参考复议医生编码',
        dataIndex: 'verifyDoctorCode',
        scopedSlots: { customRender: 'verifyDoctorCode' },
        width: 150
      },
      {
        title: '参考复议医生',
        dataIndex: 'verifyDoctorName',
        scopedSlots: { customRender: 'verifyDoctorName' },
        width: 150
      },
      {
        title: '参考复议科室编码',
        dataIndex: 'verifyDeptCode',
        width: 100
      },
      {
        title: '参考复议科室',
        dataIndex: 'verifyDeptName',
        width: 100
      },
      {
        title: '变更复议医生编码',
        dataIndex: 'changVerifyDoctorCode',
        width: 100
      },
      {
        title: '变更复议医生',
        dataIndex: 'changVerifyDoctorName',
        width: 100
      },
      {
        title: '变更复议科室编码',
        dataIndex: 'changVerifyDeptCode',
        width: 100
      },
      {
        title: '变更复议科室',
        dataIndex: 'changVerifyDeptName',
        width: 100
      },
      {
        title: '接受状态',
        dataIndex: 'acceptState',
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
        width: 110
      },
      {
        title: '发送日期',
        dataIndex: 'sendDate',
        width: 110
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 150
      }]
    },
    columnsTwo () {
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
        title: '参考复议医生编码',
        dataIndex: 'verifyDoctorCode',
        width: 100
      },
      {
        title: '参考复议医生',
        dataIndex: 'verifyDoctorName',
        width: 100
      },
      {
        title: '参考复议科室编码',
        dataIndex: 'verifyDeptCode',
        width: 100
      },
      {
        title: '参考复议科室',
        dataIndex: 'verifyDeptName',
        width: 100
      },
      {
        title: '变更复议医生编码',
        dataIndex: 'changVerifyDoctorCode',
        width: 100
      },
      {
        title: '变更复议医生',
        dataIndex: 'changVerifyDoctorName',
        width: 100
      },
      {
        title: '变更复议科室编码',
        dataIndex: 'changVerifyDeptCode',
        width: 100
      },
      {
        title: '变更复议科室',
        dataIndex: 'changVerifyDeptName',
        width: 100
      },
      {
        title: '接受状态',
        dataIndex: 'acceptState',
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
        width: 110
      },
      {
        title: '发送日期',
        dataIndex: 'sendDate',
        width: 110
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
    callback (key) {
      if (key === '1') {
        this.search()
      } else if (key === '2') {
        this.searchTwo()
      } else {
        console.log('search3')
      }
    },
    // 输入框事件
    handleSearch (keyword) {
      // 模拟往服务器发送请求
      this.ajax(keyword)
    },
    // 模拟往服务器发送请求
    ajax (keyword) {
      let body = [{id: 1, code: '101A', name: 'AAA'}, {id: 2, code: '102A', name: 'BBB'}, {id: 3, code: '103A', name: 'CCC'}]
      this.selectDataSource = body.map(user => ({
        text: user.name,
        value: user.code
      }))
    },
    handleChange (value, key, column) {
      const newData = [...this.dataSource]
      const target = newData.filter(item => key === item.id)[0]
      if (target) {
        target[column] = value
        const textData = this.selectDataSource.filter(item => value === item.value)[0]
        target.verifyDoctorCode = value
        target.verifyDoctorName = textData.text
        this.dataSource = newData
      }
      this.selectValue = value
    },
    edit (key) {
      const newData = [...this.dataSource]
      const target = newData.filter(item => key === item.id)[0]
      this.selectDataSource = [{
        text: target.verifyDoctorName,
        value: target.verifyDoctorCode
      }]
      this.selectValue = target.verifyDoctorCode

      this.editingKey = key
      if (target) {
        target.editable = true
        this.dataSource = newData
      }
    },
    save (key) {
      const newData = [...this.dataSource]
      const newCacheData = [...this.cacheData]
      const target = newData.filter(item => key === item.id)[0]
      const targetCache = newCacheData.filter(item => key === item.id)[0]
      if (target && targetCache) {
        delete target.editable
        this.dataSource = newData
        Object.assign(targetCache, target)
        this.cacheData = newCacheData
      }
      this.editingKey = ''
      this.selectDataSource = []
      this.selectValue = undefined
    },
    cancel (key) {
      const newData = [...this.dataSource]
      const target = newData.filter(item => key === item.id)[0]
      this.editingKey = ''
      if (target) {
        Object.assign(target, this.cacheData.filter(item => key === item.id)[0])
        delete target.editable
        this.dataSource = newData
      }
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
      this.$get('ybReconsiderVerify', {
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
    },
    onSelectChangeTwo (selectedRowKeys) {
      this.selectedRowKeysTwo = selectedRowKeys
    },
    searchTwo () {
      let { sortedInfoTwo } = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfoTwo) {
        sortField = sortedInfoTwo.field
        sortOrder = sortedInfoTwo.order
      }
      this.fetchTwo({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParamsTwo
      })
    },
    resetTwo () {
      // 取消选中
      this.selectedRowKeysTwo = []
      // 重置分页
      this.$refs.TableInfoTwo.pagination.current = this.paginationTwo.defaultCurrent
      if (this.paginationInfoTwo) {
        this.paginationInfoTwo.current = this.paginationTwo.defaultCurrent
        this.paginationInfoTwo.pageSize = this.paginationTwo.defaultPageSize
      }
      // 重置列排序规则
      this.sortedInfoTwo = null
      this.paginationInfoTwo = null
      // 重置查询参数
      this.queryParamsTwo = {}
      this.fetchTwo()
    },
    handleTableChangeTwo (pagination, filters, sorter) {
      this.sortedInfoTwo = sorter
      this.paginationInfoTwo = pagination
      this.fetchTwo({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParamsTwo
      })
    },
    fetchTwo (params = {}) {
      this.loadingTwo = true
      if (this.paginationInfoTwo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfoTwo.pagination.current = this.paginationInfoTwo.current
        this.$refs.TableInfoTwo.pagination.pageSize = this.paginationInfoTwo.pageSize
        params.pageSize = this.paginationInfoTwo.pageSize
        params.pageNum = this.paginationInfoTwo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.paginationTwo.defaultPageSize
        params.pageNum = this.paginationTwo.defaultCurrent
      }
      this.$get('ybReconsiderVerify', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.paginationTwo }
        pagination.total = data.total
        this.loadingTwo = false
        this.dataSourceTwo = data.rows
        this.paginationTwo = pagination
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
