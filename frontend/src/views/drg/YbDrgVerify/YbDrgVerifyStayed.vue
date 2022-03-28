
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
    size="small"
    :bordered="bordered"
    :scroll="{ x: 900 }"
    >
    <template slot="operationLy" slot-scope="text, record, index">
      <span :title="record.ly">{{record.ly}}</span>
    </template>
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
          <div v-if="record.isPerson===0" style="color:#FF0000">
            <b>{{ text }}</b>
          </div>
          <div v-else>
            {{ text }}
          </div>
        </template>
        </div>
    </template>
    <template
        slot='verifyDksName'
        slot-scope="text, record, index"
    >
        <div key='verifyDksName'>
        <a-select
            v-if="record.editable"
            show-search
            :value="selectDksValue"
            placeholder="请输入关键词"
            style="width: 100%"
            :default-active-first-option="false"
            :filter-option="false"
            :not-found-content="null"
            @search="handleDksSearch"
            @change="e => handleDksChange(e, record.id, 'verifyDksName')"
        >
        <a-icon
        slot="suffixIcon"
        type="search"
        ></a-icon>
        <a-select-option
        v-for="d in selectDksDataSource"
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
  name: 'YbDrgVerifyStayed',
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
      selectDksDataSource: [], // 搜索事件
      selectDksValue: undefined,
      selectDoctorDataSource: [], // 搜索事件
      selectDoctorValue: undefined,
      ybDrgVerify: {},
      user: this.$store.state.account.user,
      tableFormat: 'YYYY-MM-DD'
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'orderNumber',
        fixed: 'left',
        width: 65
      },
      {
        title: '科室',
        dataIndex: 'ks',
        fixed: 'left',
        width: 120
      },
      {
        title: '就诊记录号',
        dataIndex: 'jzjlh',
        fixed: 'left',
        width: 90
      },
      {
        title: '病案号',
        dataIndex: 'bah',
        fixed: 'left',
        width: 80
      },
      {
        title: '违规类型',
        dataIndex: 'wglx',
        width: 120
      },
      {
        title: '问题描述',
        dataIndex: 'wtms',
        width: 350
      },
      {
        title: '医疗总费用',
        dataIndex: 'ylzfy',
        width: 100
      },
      {
        title: '违规金额',
        dataIndex: 'wgje',
        width: 100
      },
      {
        title: '是否编码造成直接错误',
        dataIndex: 'sfbmzczjcw',
        width: 110
      },
      {
        title: '理由',
        dataIndex: 'ly',
        scopedSlots: { customRender: 'operationLy' },
        ellipsis: true,
        width: 250
      },
      {
        title: '复议科室',
        dataIndex: 'verifyDksName',
        scopedSlots: { customRender: 'verifyDksName' },
        fixed: 'right',
        width: 220
      },
      {
        title: '复议医生',
        dataIndex: 'verifyDoctorName',
        scopedSlots: { customRender: 'verifyDoctorName' },
        fixed: 'right',
        width: 150
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 130
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
            verifyDoctorCode: target.verifyDoctorCode,
            verifyDoctorName: target.verifyDoctorName,
            verifyDksId: target.verifyDksId,
            verifyDksName: target.verifyDksName
          }
          data.push(arrData)
        }
        if (data.length > 0) {
          this.$emit('showImport', data)
        } else {
          this.$message.warning('未找到对象')
        }
      } else {
        this.$message.warning('未选择行')
      }
    },
    handImport (selectDate) {
      let selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length > 0) {
        let data = []
        for (let key of selectedRowKeys) {
          let target = this.dataSource.filter(item => key === item.id)[0]
          let arrData = {
            id: target.isVerify === 0 ? '' : target.id,
            applyDataId: target.applyDataId,
            verifyDoctorCode: selectDate.doctorCode,
            verifyDoctorName: selectDate.doctorName,
            verifyDksId: selectDate.dksId,
            verifyDksName: selectDate.dksName,
            applyDateStr: target.applyDateStr,
            orderNumber: target.orderNumber,
            orderNum: target.orderNum,
            areaType: this.user.areaType.value
          }
          data.push(arrData)
        }
        if (data.length > 0) {
          let jsonString = JSON.stringify(data)
          this.$put('ybDrgVerify/updateDrgVerifyImport', {
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
          this.$message.warning('未找到对象')
        }
      } else {
        this.$message.warning('未选择行')
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
            id: target.isVerify === 0 ? '' : target.id,
            applyDataId: target.applyDataId,
            verifyDoctorCode: target.verifyDoctorCode,
            verifyDoctorName: target.verifyDoctorName,
            verifyDksId: target.verifyDksId,
            verifyDksName: target.verifyDksName,
            applyDateStr: target.applyDateStr,
            orderNumber: target.orderNumber,
            orderNum: target.orderNum,
            areaType: this.user.areaType.value
          }
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
    batchVerifyA () {
      if (this.dataSource.length > 0) {
        this.$put('ybDrgVerify/updateAReviewerState', {
          applyDateStr: this.applyDate, areaType: this.user.areaType.value, state: 1
        }).then(() => {
          this.$message.success('全部核对审核')
          this.$emit('verifySpin')
          this.search()
        }).catch(() => {
          this.loading = false
          this.$emit('verifySpin')
        })
      } else {
        this.$message.warning('无数据，无法全部核对审核!')
        this.$emit('verifySpin')
      }
    },
    verifyService (data) {
      let jsonString = JSON.stringify(data)
      this.$put('ybDrgVerify/updateReviewerState', {
        dataJson: jsonString, areaType: this.user.areaType.value
      }).then(() => {
        this.$message.success('核对成功')
        this.$emit('verifySpin')
        this.search()
      }).catch(() => {
        this.loading = false
        this.$emit('verifySpin')
      })
    },
    // 模拟往服务器发送请求
    ajaxDks (keyword) {
      let dataSource = []
      let params = {comments: keyword, areaId: this.user.areaType.value}
      this.$get('ybDrgDks/findDksList', {
        ...params
      }).then((r) => {
        r.data.data.forEach((item, i) => {
          dataSource.push({
            value: item.dksId,
            text: item.dksId + '-' + item.dksName + '(' + item.areaName + ')'
          })
        })
      })
      return dataSource
    },
    ajaxDoctor (keyword) {
      let dataSource = []
      let params = {comments: keyword, deptName: '医生'}
      this.$get('ybPerson/findPersonList', {
        ...params
      }).then((r) => {
        r.data.data.forEach((item, i) => {
          dataSource.push({
            value: item.personCode,
            text: item.personCode + '-' + item.personName
          })
        })
      })
      return dataSource
    },
    // 输入框事件
    handleDoctorSearch (keyword) {
      // 模拟往服务器发送请求
      this.selectDoctorDataSource = this.ajaxDoctor(keyword)
    },
    handleDoctorChange (value, key, column) {
      const newData = [...this.dataSource]
      const target = newData.filter(item => key === item.id)[0]
      if (target) {
        target[column] = value
        const textData = this.selectDoctorDataSource.filter(item => value === item.value)[0]
        target.verifyDoctorCode = value
        target.verifyDoctorName = textData.text
        this.ybDrgVerify.verifyDoctorCode = target.verifyDoctorCode
        this.ybDrgVerify.verifyDoctorName = target.verifyDoctorName
        this.dataSource = newData
      }
      this.selectDoctorValue = value
    },
    // 输入框事件
    handleDksSearch (keyword) {
      // 模拟往服务器发送请求
      this.selectDksDataSource = this.ajaxDks(keyword)
    },
    handleDksChange (value, key, column) {
      const newData = [...this.dataSource]
      const target = newData.filter(item => key === item.id)[0]
      if (target) {
        target[column] = value
        const textData = this.selectDksDataSource.filter(item => value === item.value)[0]
        target.verifyDksId = textData.value
        target.verifyDksName = textData.text
        this.ybDrgVerify.verifyDksId = target.verifyDksId
        this.ybDrgVerify.verifyDksName = target.verifyDksName
        this.dataSource = newData
      }
      this.selectDksValue = value
    },
    edit (key) {
      this.ybDrgVerify = {}
      const newData = [...this.dataSource]
      const target = newData.filter(item => key === item.id)[0]
      if (target !== undefined) {
        this.selectDksDataSource = [{
          text: target.verifyDksName,
          value: target.verifyDksId
        }]
        this.selectDoctorDataSource = [{
          text: target.verifyDoctorName,
          value: target.verifyDoctorCode
        }]

        this.selectDoctorValue = target.verifyDoctorCode
        this.selectDksValue = target.verifyDksId

        this.ybDrgVerify.id = key
        this.ybDrgVerify.applyDataId = target.applyDataId
        this.ybDrgVerify.verifyDoctorCode = target.verifyDoctorCode
        this.ybDrgVerify.verifyDoctorName = target.verifyDoctorName
        this.ybDrgVerify.verifyDksId = target.verifyDksId
        this.ybDrgVerify.verifyDksName = target.verifyDksName

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
        this.selectDksDataSource = []
        this.selectDksValue = undefined
        let dtc = this.ybDrgVerify.verifyDoctorCode
        let dec = this.ybDrgVerify.verifyDksName
        if (dtc !== undefined && dec !== undefined && dtc !== null && dec !== null && dtc !== '' && dec !== '') {
          let arrData = [{
            id: target.isVerify === 0 ? '' : this.ybDrgVerify.id,
            applyDataId: this.ybDrgVerify.applyDataId,
            verifyDoctorCode: this.ybDrgVerify.verifyDoctorCode,
            verifyDoctorName: this.ybDrgVerify.verifyDoctorName,
            verifyDksId: this.ybDrgVerify.verifyDksId,
            verifyDksName: this.ybDrgVerify.verifyDksName,
            applyDateStr: target.applyDateStr,
            orderNumber: target.orderNumber,
            orderNum: target.orderNum,
            areaType: this.user.areaType.value
          }]
          this.verifyService(arrData)
        } else {
          this.$message.warning('未选择，参考复议科室 或 参考复议医生.')
        }
      } else {
        this.$message.warning('未找到对象.')
      }
      this.ybDrgVerify = {}
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
      this.ybDrgVerify = {}
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
      this.$emit('selectChangeKeyVerify', this.selectedRowKeys)
    },
    searchPage () {
      this.pagination.defaultCurrent = 1
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
      }
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
      this.editingKey = ''
      params.applyDateStr = this.applyDate
      params.state = 1
      params.areaType = this.user.areaType.value
      if (this.searchItem !== undefined) {
        if (this.searchItem.item.ks !== '') {
          params.ks = this.searchItem.item.ks
        }
        if (this.searchItem.item.jzjlh !== '') {
          params.jzjlh = this.searchItem.item.jzjlh
        }
        if (this.searchItem.item.bah !== '') {
          params.bah = this.searchItem.item.bah
        }
        if (this.searchItem.doctor.docCode !== '') {
          params.verifyDoctorCode = this.searchItem.doctor.docCode
        }
        if (this.searchItem.doctor.docName !== '') {
          params.verifyDoctorName = this.searchItem.doctor.docName
        }
        if (this.searchItem.dept.dksName !== '') {
          params.verifyDksName = this.searchItem.dept.dksName
        }
        if (this.searchItem.order.orderNumber !== '') {
          params.orderNumber = this.searchItem.order.orderNumber
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
      // applyDateStr asc,orderNum
      // params.sortField = 'rv.orderNum'
      // params.sortOrder = 'ascend'

      this.$get('ybDrgVerifyView', {
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
</style>
