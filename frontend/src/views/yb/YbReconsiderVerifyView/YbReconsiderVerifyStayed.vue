
<template>
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
            <a @click.stop="() => save(record.id)">确定</a>
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
            @click.stop="() => detail(record,index)"
            >查看详情</a>
            <a-divider type="vertical" />
            <a
            :disabled="editingKey !== ''"
            @click.stop="() => edit(record.id)"
            >核对</a>
        </span>
        </div>
    </template>
  </a-table>
</template>

<script>
import moment from 'moment'
export default {
  name: 'YbReconsiderVerifyStayed',
  props: {
    applyDate: {
      default: ''
    },
    searchItem: {
      default () {
        return {}
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
      editingKey: '',
      cacheData: [],
      monthFormat: 'YYYY-MM',
      selectDeptDataSource: [], // 搜索事件
      selectDeptValue: undefined,
      selectDoctorDataSource: [], // 搜索事件
      selectDoctorValue: undefined,
      ybReconsiderVerify: {},
      tableFormat: 'YYYY-MM-DD'
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'orderNumber',
        fixed: 'left',
        width: 70
      },
      {
        title: '意见书编码',
        dataIndex: 'proposalCode',
        fixed: 'left',
        width: 120
      },
      {
        title: '项目编码',
        dataIndex: 'projectCode',
        fixed: 'left',
        width: 100
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        fixed: 'left',
        width: 100
      },
      {
        title: '数量',
        dataIndex: 'num',
        width: 70
      },
      {
        title: '医保内金额',
        dataIndex: 'medicalPrice',
        width: 105
      },
      {
        title: '规则名称',
        dataIndex: 'ruleName',
        width: 100
      },
      {
        title: '扣除金额',
        dataIndex: 'deductPrice',
        width: 100
      },
      {
        title: '扣除原因',
        dataIndex: 'deductReason',
        width: 200
      },
      {
        title: '费用日期',
        dataIndex: 'costDateStr',
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
        width: 120
      },
      {
        title: '医生姓名',
        dataIndex: 'doctorName',
        width: 100
      },
      {
        title: '科室名称',
        dataIndex: 'deptName',
        width: 100
      },
      {
        title: '参考复议科室',
        dataIndex: 'verifyDeptName',
        scopedSlots: { customRender: 'verifyDeptName' },
        fixed: 'right',
        width: 125
      },
      {
        title: '参考复议医生',
        dataIndex: 'verifyDoctorName',
        scopedSlots: { customRender: 'verifyDoctorName' },
        fixed: 'right',
        width: 125
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 145
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
    handleClickRow (record, index) {
      return {
        on: {
          click: () => {
            let target = this.selectedRowKeys.filter(key => key === record.id)[0]
            if (target === undefined) {
              this.selectedRowKeys.push(record.id)
            } else {
              this.selectedRowKeys.splice(this.selectedRowKeys.indexOf(record.id), 1)
            }
          }
        }
      }
    },
    detail (record, index) {
      let rNo = this.rowNo(index)
      record.rowNo = rNo
      this.$emit('detail', record)
    },
    showImport () {
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
            verifyDeptName: target.verifyDeptName
          }
          data.push(arrData)
        }
        if (data.length > 0) {
          this.$emit('showImport', data)
        } else {
          this.$message.success('未找到对象')
        }
      } else {
        this.$message.success('未选择行')
      }
    },
    handImport (selectDate) {
      let selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length > 0) {
        let data = []
        for (let key of selectedRowKeys) {
          let target = this.dataSource.filter(item => key === item.id)[0]
          let arrData = {
            id: target.id,
            applyDataId: target.applyDataId,
            verifyDoctorCode: selectDate.doctorCode,
            verifyDoctorName: selectDate.doctorName,
            verifyDeptCode: selectDate.deptCode,
            verifyDeptName: selectDate.deptName
          }
          data.push(arrData)
        }
        if (data.length > 0) {
          let jsonString = JSON.stringify(data)
          this.$put('ybReconsiderVerify/updateReconsiderVerifyImport', {
            dataJson: jsonString
          }).then(() => {
            this.$message.success('匹配成功')
            this.selectedRowKeys = []
            this.$emit('handImport')
            this.search()
          }).catch(() => {
            this.loading = false
          })
        } else {
          this.$message.success('未找到对象')
        }
      } else {
        this.$message.success('未选择行')
      }
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
            verifyDeptName: target.verifyDeptName
          }
          data.push(arrData)
        }
        if (data.length > 0) {
          this.verifyService(data)
        } else {
          this.$message.success('未找到对象')
        }
      } else {
        this.$message.success('未选择行')
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
    // 模拟往服务器发送请求
    ajax (keyword) {
      let dataSource = [{value: '101A', text: '测试科室1'}, {value: '102A', text: '测试科室2'}, {value: '103A', text: '测试科室3'}]
      return dataSource
    },
    ajaxDoc (keyword) {
      let dataSource = [{value: 'mrbird', text: '系统管理员'}, {value: '102A', text: '测试医生2'}, {value: '103A', text: '测试医生3'}]
      return dataSource
    },
    // 输入框事件
    handleDoctorSearch (keyword) {
      // 模拟往服务器发送请求
      this.selectDoctorDataSource = this.ajaxDoc(keyword)
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
        this.$message.success('未找到对象')
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
        this.$message.success('未找到对象')
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
      this.$emit('selectChangeKeyVerify', this.selectedRowKeys)
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
      this.editingKey = ''
      params.applyDateStr = this.applyDate
      params.state = 1
      params.dataType = 0
      let searchType = [this.searchItem.project.type, this.searchItem.rule.type, this.searchItem.dept.type]
      params.searchType = searchType
      if (this.searchItem !== undefined) {
        if (this.searchItem.project.projectName !== '') {
          params.projectName = this.searchItem.project.projectName
        }
        if (this.searchItem.rule.ruleName !== '') {
          params.ruleName = this.searchItem.rule.ruleName
        }
        if (this.searchItem.dept.deptName !== '') {
          params.verifyDeptName = this.searchItem.dept.deptName
        }
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

      params.sortField = 'orderNumber'
      params.sortOrder = 'ascend'

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
      this.selectedRowKeys = []
      this.$emit('selectChangeKeyVerify', this.selectedRowKeys)
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
