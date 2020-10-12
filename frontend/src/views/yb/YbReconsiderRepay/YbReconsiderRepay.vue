<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <a-row type="flex" justify="center">
            <a-col
              :span=5
            >
              <a-form-item
                label="操作时间"
                v-bind="formItemLayout"
              >
                <a-date-picker v-model="queryParams.createTimeFrom" @change="oncreateTimeFromChange" />
              </a-form-item>
            </a-col>
            <a-col
              :span=5
            >
              <a-form-item
                label="至"
                v-bind="formItemLayout"
              >
                <a-date-picker v-model="queryParams.createTimeTo" @change="oncreateTimeToChange" />
              </a-form-item>
            </a-col>
            <a-col
              :span=6
            >&nbsp;</a-col>
            <a-col
              :span=7
            >
            <a-button
              type="primary"
              @click="importShow"
            >上传还款单</a-button>
            <a-button
              style="margin-left: 18px"
              type="primary"
              @click="search"
            >查询</a-button>
            <a-button
              style="margin-left: 18px"
              @click="reset"
            >重置</a-button>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div>
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
          <a
            v-hasPermission="['ybReconsiderRepay:delete']"
            @click="del(record)"
            :disabled="record.state==0?false:true"
          >
            删除
          </a>
          <a-divider type="vertical" />
          <a
            v-hasPermission="['ybReconsiderRepay:view']"
            @click="look(record)"
          >
            查看
          </a>
        </template>
      </a-table>
    </div>
    <template>
      <a-modal
        :width="500"
        :maskClosable="false"
        :footer="null"
        v-model="importVisible"
        title="上传还款单"
      >
      <a-spin
        tip="Loading..."
        :spinning="spinning"
        :delay="delayTime"
      >
        <div>
          <a-row
            justify="center"
            type="flex"
          >
            <a-col :span=22>
              <a-form-item
                label="复议年月"
                v-bind="formItemLayout"
              >
                <a-month-picker
                  placeholder="请选择复议年月"
                  style="width: 120px"
                  @change="monthChange"
                  :default-value="formatDate()"
                  :format="monthFormat"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row
            justify="center"
            type="flex"
          >
            <a-col :span=22>
              <a-form-item
                label="扣款类型"
                v-bind="formItemLayout"
              >
                <a-select
                  :value="selectDataType"
                  style="width: 120px"
                  @change="handleDataTypeChange"
                >
                  <a-select-option
                    v-for="d in selectDataTypeSource"
                    :key="d.value"
                  >
                    {{ d.text }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row
            justify="center"
            type="flex"
          >
            <a-col :span=22>
              <a-form-item
                label="保险类型"
                v-bind="formItemLayout"
              >
                <a-select
                  :value="selectRepayType"
                  style="width: 120px"
                  @change="handleRepayTypeChange"
                >
                  <a-select-option
                    v-for="d in selectRepayTypeSource"
                    :key="d.value"
                  >
                    {{ d.text }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row
            justify="center"
            type="flex"
          >
            <a-col :span=22>
              <a-form-item
                label="文件名称"
                v-bind="formItemLayout"
              >
                <a-input
                  placeholder="上传文件名称"
                  v-model="uploadFileName"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row
            justify="center"
            type="flex"
          >
            <a-col>
              <a-upload
                name="file"
                accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                :fileList="fileList"
                :beforeUpload="beforeUpload"
              >
                <a-button type="primary">
                  <a-icon type="upload" /> 上传
                </a-button>
              </a-upload>
            </a-col>
          </a-row>
        </div>
      </a-spin>
      </a-modal>
    </template>
    <a-modal
      title="查看还款单明细"
      :visible="lookVisiable"
      :footer="null"
      width="100%"
      style="padding-top:0px;"
      :maskClosable="false"
      @cancel="handleCancel"
    >
      <ybReconsiderRepay-view
      ref="ybReconsiderRepayView"
      @cancel="handleCancel"
      >
      </ybReconsiderRepay-view>
    </a-modal>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbReconsiderRepayView from './YbReconsiderRepayView'
const formItemLayout = {
  labelCol: { span: 9 },
  wrapperCol: { span: 14, offset: 1 }
}
export default {
  name: 'YbReconsiderRepay',
  components: { YbReconsiderRepayView },
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
      loading: false,
      importVisible: false,
      selectRepayTypeSource: [
        { text: '居保', value: 0 },
        { text: '职保', value: 1 }
      ],
      selectRepayType: 0,
      selectDataTypeSource: [
        { text: '明细扣款', value: 0 },
        { text: '主单扣款', value: 1 }
      ],
      selectDataType: 0,
      uploadFileName: '',
      fileList: [],
      spinning: false,
      delayTime: 500,
      lookVisiable: false,
      bordered: true,
      selectApplyDateStr: this.formatDate(),
      monthFormat: 'YYYY-MM',
      tableFormat: 'YYYY-MM-DD'
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
        width: 80
      },
      {
        title: '复议年月',
        dataIndex: 'applyDateStr',
        width: 100
      },
      {
        title: '保险类型',
        dataIndex: 'repayType',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '居保'
            case 1:
              return '职保'
            case 2:
              return '无'
            default:
              return text
          }
        },
        width: 100
      },
      {
        title: '扣款类型',
        dataIndex: 'dataType',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '明细扣款'
            case 1:
              return '主单扣款'
            default:
              return text
          }
        },
        width: 100
      },
      {
        title: '文件名称',
        dataIndex: 'uploadFileName'
      },
      {
        title: '操作员',
        dataIndex: 'operatorName',
        width: 100
      },
      {
        title: '操作时间',
        customRender: (text, row, index) => {
          return moment(text).format(this.tableFormat)
        },
        dataIndex: 'createTime',
        width: 120
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 150
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    moment,
    formatDate () {
      let datemonth = moment().format('YYYY-MM')
      return datemonth
    },
    monthChange (date, dateString) {
      this.selectApplyDateStr = dateString
    },
    look (record) {
      this.lookVisiable = true
      setTimeout(() => {
        this.$refs.ybReconsiderRepayView.setFormValues(record)
      }, 200)
    },
    handleCancel (isUpdate) {
      this.lookVisiable = false
      if (isUpdate) {
        this.search()
      }
    },
    handleRepayTypeChange (value) {
      this.selectRepayType = value
    },
    handleDataTypeChange (value) {
      this.selectDataType = value
      if (value === 1) {
        this.selectRepayTypeSource = [
          { text: '无', value: 2 }
        ]
        this.selectRepayType = 2
      } else {
        this.selectRepayTypeSource = [
          { text: '居保', value: 0 },
          { text: '职保', value: 1 }
        ]
        this.selectRepayType = 0
      }
    },
    importShow () {
      this.importVisible = true
      this.uploadFileName = ''
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
    oncreateTimeFromChange (date, dateString) {
      this.queryParams.createTimeFrom = dateString
    },
    oncreateTimeToChange (date, dateString) {
      this.queryParams.createTimeTo = dateString
    },
    beforeUpload (file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      if (!(isExcel)) {
        this.$error({
          title: '只能上传.xlsx格式的Excel文档~'
        })
        return
      }
      const isLt2M = file.size / 1024 / 1024 < 5
      if (!isLt2M) {
        this.$error({
          title: '超5M限制，不允许上传~'
        })
        return
      }
      return (isExcel) && isLt2M && this.handleUpload(file)
    },
    handleUpload (file) {
      this.spinning = true
      // 点击删除文件调用removeUpload后会自动调用本方法handleUpload 待解决
      const formData = new FormData()
      formData.append('file', file)
      formData.append('repayType', this.selectRepayType)
      formData.append('dataType', this.selectDataType)
      formData.append('applyDateStr', this.selectApplyDateStr)
      this.$upload('ybReconsiderRepay/importReconsiderRepayData', formData).then((r) => {
        if (r.data.data.success === 1) {
          this.uploadFileName = r.data.data.fileName
          this.spinning = false
          this.importVisible = false
          this.search()
          this.$message.success('Excel导入成功.')
        } else {
          this.spinning = false
          this.$message.error(r.data.data.message)
        }
      }).catch(() => {
        this.fileList = []
        this.$message.error('Excel导入失败.')
        this.spinning = false
      })
    },
    del (record) {
      let that = this
      this.$confirm({
        title: '确定删除该记录?',
        content: '当您点击确定按钮后，这条记录将会被彻底删除',
        centered: true,
        onOk () {
          let ybReconsiderRepayIds = record.id
          that.$delete('ybReconsiderRepay/deleteReconsiderRepay/' + ybReconsiderRepayIds).then((r) => {
            if (r.data.data.success === 1) {
              that.$message.success('删除成功')
              that.selectedRowKeys = []
              that.search()
            } else {
              that.$message.error(r.data.data.message)
            }
          }
          )
        }
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
      this.$get('ybReconsiderRepay', {
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
