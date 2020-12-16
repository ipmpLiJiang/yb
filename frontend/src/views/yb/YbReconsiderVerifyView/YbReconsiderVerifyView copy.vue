
<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <a-row>
          <div :class="advanced ? null: 'fold'">
            <a-row justify='center'>
              <a-col :span=24>
                <a-form-item
                  v-bind="formItemLayout"
                  label="复议年月"
                >
                  <a-month-picker
                    placeholder="请输入复议年月"
                    :default-value="formatDate()"
                    :format="monthFormat"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button
              type="primary"
              @click="searchTable"
            >刷新</a-button>
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
              <div style="text-align:right;margin:0px 30px 10px 0px">
                <a-button
                  type="primary"
                  @click="addImport"
                >自动匹配</a-button>
                <a-button
                  type="primary"
                  @click="batchVerify"
                >批量核对</a-button>
              </div>
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
                  slot='verifyDoctorName'
                  slot-scope="text, record, index"
                >
                  <div key='verifyDoctorName'>
                    <a-select
                      v-if="record.editable"
                      show-search
                      :value="selectDoctorValue"
                      placeholder="请输入关键词"
                      style="width: 100%"
                      :default-active-first-option="false"
                      :filter-option="false"
                      :not-found-content="null"
                      @search="handleDoctorSearch"
                      @change="e => handleDoctorChange(e, record.id, 'verifyDoctorName')"
                    >
                      <a-icon
                        slot="suffixIcon"
                        type="search"
                      ></a-icon>
                      <a-select-option
                        v-for="d in selectDoctorDataSource"
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
                  slot='verifyDeptName'
                  slot-scope="text, record, index"
                >
                  <div key='verifyDeptName'>
                    <a-select
                      v-if="record.editable"
                      show-search
                      :value="selectDeptValue"
                      placeholder="请输入关键词"
                      style="width: 100%"
                      :default-active-first-option="false"
                      :filter-option="false"
                      :not-found-content="null"
                      @search="handleDeptSearch"
                      @change="e => handleDeptChange(e, record.id, 'verifyDeptName')"
                    >
                      <a-icon
                        slot="suffixIcon"
                        type="search"
                      ></a-icon>
                      <a-select-option
                        v-for="d in selectDeptDataSource"
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
                        title="确定放弃编辑？"
                        @confirm="() => cancel(record.id)"
                      >
                        <a>取消</a>
                      </a-popconfirm>
                    </span>
                    <span v-else>
                      <a
                        :disabled="editingKey !== ''"
                        @click="() => detail(record)"
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
              <div style="text-align:right;margin:0px 30px 10px 0px">
                <a-button
                  type="primary"
                  @click="batchSend"
                >批量发送</a-button>
              </div>
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
                  <a @click="() => send(record.id)"
                      >发送</a>
                </template>
              </a-table>
            </a-tab-pane>
            <a-tab-pane
              key="3"
              tab="已完成"
            >
              <!-- 已完成 表格区域 -->
              <a-table
                ref="TableInfoThree"
                :columns="columnsThree"
                :rowKey="record => record.id"
                :dataSource="dataSourceThree"
                :pagination="paginationThree"
                :loading="loadingThree"
                :rowSelection="{selectedRowKeys: selectedRowKeysThree, onChange: onSelectChangeThree}"
                @change="handleTableChangeThree"
                :bordered="borderedThree"
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
                  <a @click="() => send(record.id)"
                      >发送</a>
                </template>
              </a-table>
            </a-tab-pane>
          </a-tabs>
        </div>
      </template>
    </div>
    <!-- 修改字典 -->
    <ybReconsiderVerifyView-detail
      ref="ybReconsiderVerifyViewDetail"
      @close="handleDetailClose"
      @success="handleDetailSuccess"
      :detailVisiable="detailVisiable"
    >
    </ybReconsiderVerifyView-detail>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbReconsiderVerifyViewDetail from './YbReconsiderVerifyViewDetail'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbReconsiderVerifyView',
  components: { YbReconsiderVerifyViewDetail },
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
      borderedTwo: true,
      dataSourceThree: [],
      selectedRowKeysThree: [],
      sortedInfoThree: null,
      paginationInfoThree: null,
      paginationThree: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      queryParamsThree: {
      },
      loadingThree: false,
      borderedThree: true,
      editingKey: '',
      cacheData: [],
      monthFormat: 'YYYY-MM',
      searchApplyDate: undefined,
      selectDeptDataSource: [], // 搜索事件
      selectDeptValue: undefined,
      selectDoctorDataSource: [], // 搜索事件
      selectDoctorValue: undefined,
      detailVisiable: false,
      ybReconsiderVerify: {},
      tableSelectKey: '1'
    }
  },
  computed: {
    columns () {
      return [{
        title: '核对申请',
        dataIndex: 'id',
        fixed: 'left',
        width: 100
      },
      {
        title: '意见书编码',
        dataIndex: 'proposalCode',
        fixed: 'left',
        width: 140
      },
      {
        title: '项目编码',
        dataIndex: 'projectCode',
        fixed: 'left',
        width: 120
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        fixed: 'left',
        width: 160
      },
      {
        title: '参考复议医生',
        dataIndex: 'verifyDoctorName',
        scopedSlots: { customRender: 'verifyDoctorName' },
        width: 160
      },
      {
        title: '参考复议科室',
        dataIndex: 'verifyDeptName',
        scopedSlots: { customRender: 'verifyDeptName' },
        width: 160
      },
      {
        title: '数量',
        dataIndex: 'num',
        width: 70
      },
      {
        title: '医保内金额',
        dataIndex: 'medicalPrice',
        width: 120
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
        title: '费用日期',
        dataIndex: 'costDate',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return moment(text).format('YYYY-MM-DD')
          } else {
            return text
          }
        },
        width: 110
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
        fixed: 'left',
        width: 100
      },
      {
        title: '意见书编码',
        dataIndex: 'proposalCode',
        fixed: 'left',
        width: 140
      },
      {
        title: '项目编码',
        dataIndex: 'projectCode',
        fixed: 'left',
        width: 120
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        fixed: 'left',
        width: 160
      },
      {
        title: '参考复议医生',
        dataIndex: 'verifyDoctorName',
        width: 160
      },
      {
        title: '参考复议科室',
        dataIndex: 'verifyDeptName',
        width: 160
      },
      {
        title: '数量',
        dataIndex: 'num',
        width: 70
      },
      {
        title: '医保内金额',
        dataIndex: 'medicalPrice',
        width: 120
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
        title: '费用日期',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return moment(text).format('YYYY-MM-DD')
          } else {
            return text
          }
        },
        width: 110
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
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 100
      }]
    },
    columnsThree () {
      return [{
        title: '核对申请',
        dataIndex: 'id',
        fixed: 'left',
        width: 100
      },
      {
        title: '意见书编码',
        dataIndex: 'proposalCode',
        fixed: 'left',
        width: 140
      },
      {
        title: '项目编码',
        dataIndex: 'projectCode',
        fixed: 'left',
        width: 120
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        fixed: 'left',
        width: 160
      },
      {
        title: '参考复议医生',
        dataIndex: 'verifyDoctorName',
        width: 140
      },
      {
        title: '参考复议科室',
        dataIndex: 'verifyDeptName',
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
        width: 120
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
        title: '费用日期',
        dataIndex: 'costDate',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return moment(text).format('YYYY-MM-DD')
          } else {
            return text
          }
        },
        width: 110
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
        title: '发送日期',
        dataIndex: 'sendDate',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return moment(text).format('YYYY-MM-DD')
          } else {
            return text
          }
        },
        fixed: 'right',
        width: 110
      }]
    }
  },
  mounted () {
    this.searchApplyDate = this.formatDate()
    this.fetch()
  },
  methods: {
    moment,
    formatDate () {
      return moment(Date.now()).format('YYYY-MM')
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.search()
      } else if (key === '2') {
        this.searchTwo()
      } else if (key === '3') {
        this.searchThree()
      } else {
        console.log('ok')
      }
    },
    handleDetailSuccess () {
      this.detailVisiable = false
      this.$message.success('核对成功')
      this.search()
    },
    handleDetailClose () {
      this.detailVisiable = false
    },
    detail (record) {
      this.$refs.ybReconsiderVerifyViewDetail.setFormValues(record)
      this.detailVisiable = true
    },
    // 模拟往服务器发送请求
    ajax (keyword) {
      let body = [{id: 1, code: '101A', name: 'AAA'}, {id: 2, code: '102A', name: 'BBB'}, {id: 3, code: '103A', name: 'CCC'}]
      return body.map(user => ({
        text: user.name,
        value: user.code
      }))
    },
    // 输入框事件
    handleDoctorSearch (keyword) {
      // 模拟往服务器发送请求
      this.selectDoctorDataSource = this.ajax(keyword)
    },
    handleDoctorChange (value, key, column) {
      const newData = [...this.dataSource]
      const target = newData.filter(item => key === item.id)[0]
      if (target) {
        target[column] = value
        const textData = this.selectDoctorDataSource.filter(item => value === item.value)[0]
        target.verifyDoctorCode = value
        target.verifyDoctorName = textData.text
        this.ybReconsiderVerify.verifyDoctorCode = target.verifyDoctorCode
        this.ybReconsiderVerify.verifyDoctorName = target.verifyDoctorName
        this.dataSource = newData
      }
      this.selectDoctorValue = value
    },
    // 输入框事件
    handleDeptSearch (keyword) {
      // 模拟往服务器发送请求
      this.selectDeptDataSource = this.ajax(keyword)
    },
    handleDeptChange (value, key, column) {
      const newData = [...this.dataSource]
      const target = newData.filter(item => key === item.id)[0]
      if (target) {
        target[column] = value
        const textData = this.selectDeptDataSource.filter(item => value === item.value)[0]
        target.verifyDeptCode = value
        target.verifyDeptName = textData.text
        this.ybReconsiderVerify.verifyDeptCode = target.verifyDeptCode
        this.ybReconsiderVerify.verifyDeptName = target.verifyDeptName
        this.dataSource = newData
      }
      this.selectDeptValue = value
    },
    edit (key) {
      this.ybReconsiderVerify = {}
      const newData = [...this.dataSource]
      const target = newData.filter(item => key === item.id)[0]
      if (target !== undefined) {
        this.selectDeptDataSource = [{
          text: target.verifyDeptName,
          value: target.verifyDeptCode
        }]
        this.selectDoctorDataSource = [{
          text: target.verifyDoctorName,
          value: target.verifyDoctorCode
        }]

        this.selectDoctorValue = target.verifyDoctorCode
        this.selectDeptValue = target.verifyDeptCode

        this.ybReconsiderVerify.id = key
        this.ybReconsiderVerify.applyDataId = target.applyDataId
        this.ybReconsiderVerify.verifyDoctorCode = target.verifyDoctorCode
        this.ybReconsiderVerify.verifyDoctorName = target.verifyDoctorName
        this.ybReconsiderVerify.verifyDeptCode = target.verifyDeptCode
        this.ybReconsiderVerify.verifyDeptName = target.verifyDeptName

        this.editingKey = key
        if (target) {
          target.editable = true
          this.dataSource = newData
        }
      } else {
        this.$message.warning('未找到对象')
      }
    },
    save (key) {
      const newData = [...this.dataSource]
      const newCacheData = [...this.cacheData]
      const target = newData.filter(item => key === item.id)[0]
      if (target !== undefined) {
        const targetCache = newCacheData.filter(item => key === item.id)[0]
        if (target && targetCache) {
          delete target.editable
          this.dataSource = newData
          Object.assign(targetCache, target)
          this.cacheData = newCacheData
        }
        this.editingKey = ''
        this.selectDoctorDataSource = []
        this.selectDoctorValue = undefined
        this.selectDeptDataSource = []
        this.selectDeptValue = undefined

        let arrData = [{
          id: this.ybReconsiderVerify.id,
          applyDataId: this.ybReconsiderVerify.applyDataId,
          verifyDoctorCode: this.ybReconsiderVerify.verifyDoctorCode,
          verifyDoctorName: this.ybReconsiderVerify.verifyDoctorName,
          verifyDeptCode: this.ybReconsiderVerify.verifyDeptCode,
          verifyDeptName: this.ybReconsiderVerify.verifyDeptName}]
        this.verifyService(arrData)
      } else {
        this.$message.warning('未找到对象')
      }
      this.ybReconsiderVerify = {}
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
      this.ybReconsiderVerify = {}
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
    searchTable () {
      this.callback(this.tableSelectKey)
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
    addImport () {
      this.$post('ybReconsiderVerify/addAddReconsiderVerifyImport', {
        applyDate: this.searchApplyDate
      }).then(() => {
        this.$message.success('匹配完成')
        this.search()
      }).catch(() => {
        this.loading = false
      })
    },
    batchVerify () {
      this.loading = true
      let selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length > 0) {
        let data = []
        for (let key of selectedRowKeys) {
          let target = this.dataSource.filter(item => key === item.id)[0]
          let arrData = {
            id: target.id,
            applyDataId: target.applyDataId,
            verifyDoctorCode: target.verifyDoctorCode,
            verifyDoctorName: target.verifyDoctorName,
            verifyDeptCode: target.verifyDeptCode,
            verifyDeptName: target.verifyDeptName}

          data.push(arrData)
        }
        if (data.length > 0) {
          this.verifyService(data)
        } else {
          this.$message.warning('未找到对象')
        }
      } else {
        this.$message.warning('未选择行')
      }
      this.selectedRowKeys = []
      this.loading = false
    },
    verifyService (data) {
      let jsonString = JSON.stringify(data)
      this.$put('ybReconsiderVerify/updateReviewerState', {
        dataJson: jsonString
      }).then(() => {
        this.$message.success('核对成功')
        this.search()
      }).catch(() => {
        this.loading = false
      })
    },
    batchSend () {
      this.loading = true
      let selectedRowKeys = this.selectedRowKeysTwo
      if (selectedRowKeys.length > 0) {
        let data = []
        for (let key of selectedRowKeys) {
          let target = this.dataSourceTwo.filter(item => key === item.id)[0]
          if (target !== undefined) {
            let arrData = {
              id: target.id,
              applyDataId: target.applyDataId,
              verifyDoctorCode: target.verifyDoctorCode,
              verifyDoctorName: target.verifyDoctorName,
              verifyDeptCode: target.verifyDeptCode,
              verifyDeptName: target.verifyDeptName}

            data.push(arrData)
          }
        }
        if (data.length > 0) {
          this.sendService(data)
        } else {
          this.$message.warning('未找到对象')
        }
      } else {
        this.$message.warning('未选择行')
      }
      this.selectedRowKeysTwo = []
      this.loading = false
    },
    send (key) {
      this.loading = true
      let target = this.dataSourceTwo.filter(item => key === item.id)[0]
      if (target !== undefined) {
        let data = [{
          id: target.id,
          applyDataId: target.applyDataId,
          verifyDoctorCode: target.verifyDoctorCode,
          verifyDoctorName: target.verifyDoctorName,
          verifyDeptCode: target.verifyDeptCode,
          verifyDeptName: target.verifyDeptName
        }]
        this.sendService(data)
      } else {
        this.$message.warning('未找到对象')
      }
    },
    sendService (data) {
      let jsonString = JSON.stringify(data)
      this.$put('ybReconsiderVerify/updateSendState', {
        dataJson: jsonString
      }).then(() => {
        this.$message.success('发送成功')
        this.searchTwo()
      }).catch(() => {
        this.loading = false
      })
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
      params.applyDateStr = this.searchApplyDate
      params.state = 1
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
      this.$get('ybReconsiderVerifyView/findVerifyViewNull', {
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
      params.applyDateStr = this.searchApplyDate
      params.state = 2
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
      this.$get('ybReconsiderVerifyView', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.paginationTwo }
        pagination.total = data.total
        this.loadingTwo = false
        this.dataSourceTwo = data.rows
        this.paginationTwo = pagination
      })
    },
    onSelectChangeThree (selectedRowKeys) {
      this.selectedRowKeysThree = selectedRowKeys
    },
    searchThree () {
      let { sortedInfoThree } = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfoThree) {
        sortField = sortedInfoThree.field
        sortOrder = sortedInfoThree.order
      }
      this.fetchThree({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParamsThree
      })
    },
    resetThree () {
      // 取消选中
      this.selectedRowKeysThree = []
      // 重置分页
      this.$refs.TableInfoThree.pagination.current = this.paginationThree.defaultCurrent
      if (this.paginationInfoThree) {
        this.paginationInfoThree.current = this.paginationThree.defaultCurrent
        this.paginationInfoThree.pageSize = this.paginationThree.defaultPageSize
      }
      // 重置列排序规则
      this.sortedInfoThree = null
      this.paginationInfoThree = null
      // 重置查询参数
      this.queryParamsThree = {}
      this.fetchThree()
    },
    handleTableChangeThree (pagination, filters, sorter) {
      this.sortedInfoThree = sorter
      this.paginationInfoThree = pagination
      this.fetchThree({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParamsThree
      })
    },
    fetchThree (params = {}) {
      this.loadingThree = true
      params.applyDateStr = this.searchApplyDate
      params.state = 3
      if (this.paginationInfoThree) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfoThree.pagination.current = this.paginationInfoThree.current
        this.$refs.TableInfoThree.pagination.pageSize = this.paginationInfoThree.pageSize
        params.pageSize = this.paginationInfoThree.pageSize
        params.pageNum = this.paginationInfoThree.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.paginationThree.defaultPageSize
        params.pageNum = this.paginationThree.defaultCurrent
      }
      this.$get('ybReconsiderVerifyView', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.paginationThree }
        pagination.total = data.total
        this.loadingThree = false
        this.dataSourceThree = data.rows
        this.paginationThree = pagination
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
