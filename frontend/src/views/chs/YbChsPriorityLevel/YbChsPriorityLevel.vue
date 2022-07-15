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
                label="关键字"
                v-bind="formItemLayout"
              >
                <a-input v-model="queryParams.currencyField" />
              </a-form-item>
            </a-col>
            <a-col
              :md="8"
              :sm="24"
            >
              <a-form-item
                label="匹配类型"
                v-bind="formItemLayout"
              >
                <a-radio-group v-model="queryParams.zymzType" @change="zymzTypeChange">
                <a-radio value="2">
                  住院
                </a-radio>
                <a-radio value="1">
                  门诊
                </a-radio>
              </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col
              :md="8"
              :sm="24"
            >
              <a-form-item
                label="数据类型"
                v-bind="formItemLayout"
              >
                <a-select allowClear style="width: 120px" v-model="queryParams.plType" @change="handlePlTypeChange">
                  <a-select-option value="">
                    全部
                  </a-select-option>
                  <a-select-option value="1">
                    药品
                  </a-select-option>
                  <a-select-option value="2">
                    项目
                  </a-select-option>
                  <a-select-option value="3">
                    材料
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button
              type="primary"
              @click="searchPage"
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
          v-hasPermission="['ybChsPriorityLevel:add']"
          type="primary"
          ghost
          @click="add"
        >新增</a-button>
        <a-button
          v-hasPermission="['ybChsPriorityLevel:delete']"
          @click="batchDelete"
        >删除</a-button>
        <a-dropdown v-hasPermission="['ybChsPriorityLevel:export']">
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
        :rowKey="record => record. id                                "
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        size="small"
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
            v-hasPermission="['ybChsPriorityLevel:update']"
            type="setting"
            theme="twoTone"
            twoToneColor="#4a9ff5"
            @click="edit(record)"
            title="修改"
          ></a-icon>
          <a-badge
            v-hasNoPermission="['ybChsPriorityLevel:update']"
            status="warning"
            text="无权限"
          ></a-badge>
        </template>
      </a-table>
    </div>
    <!-- 新增字典 -->
    <ybChsPriorityLevel-add
      ref="ybChsPriorityLevelAdd"
      @close="handleAddClose"
      @success="handleAddSuccess"
      :addVisiable="addVisiable"
    >
    </ybChsPriorityLevel-add>
    <!-- 修改字典 -->
    <ybChsPriorityLevel-edit
      ref="ybChsPriorityLevelEdit"
      @close="handleEditClose"
      @success="handleEditSuccess"
      :editVisiable="editVisiable"
    >
    </ybChsPriorityLevel-edit>
  </a-card>
</template>

<script>
import YbChsPriorityLevelAdd from './YbChsPriorityLevelAdd'
import YbChsPriorityLevelEdit from './YbChsPriorityLevelEdit'

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
  name: 'YbChsPriorityLevel',
  components: {
    YbChsPriorityLevelAdd,
    YbChsPriorityLevelEdit
  },
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
      queryParams: {zymzType: '2', plType: ''},
      addVisiable: false,
      editVisiable: false,
      user: this.$store.state.account.user,
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
        fixed: 'left',
        width: 70
      },
      {
        title: '匹配类型',
        dataIndex: 'zymzType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return '门诊'
            case 2:
              return '住院'
            default:
              return text
          }
        },
        fixed: 'left',
        width: 90
      },
      {
        title: '数据类型',
        dataIndex: 'plType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return '药品'
            case 2:
              return '项目'
            case 3:
              return '材料'
            default:
              return text
          }
        },
        fixed: 'left',
        width: 90
      },
      // {
      //   title: '是否规则',
      //   dataIndex: 'isRule',
      //   customRender: (text, row, index) => {
      //     switch (text) {
      //       case 1:
      //         return '是'
      //       case 2:
      //         return '否'
      //       default:
      //         return text
      //     }
      //   },
      //   fixed: 'left',
      //   width: 80
      // },
      {
        title: '规则名称',
        dataIndex: 'ruleName',
        width: 200
      },
      // {
      //   title: '是否项目',
      //   dataIndex: 'isProject',
      //   customRender: (text, row, index) => {
      //     switch (text) {
      //       case 1:
      //         return '是'
      //       case 2:
      //         return '否'
      //       default:
      //         return text
      //     }
      //   },
      //   fixed: 'left',
      //   width: 80
      // },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        width: 200
      },
      {
        title: '复议科室类型',
        dataIndex: 'deptType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return '开单科室'
            case 2:
              return '执行科室'
            case 3:
              return '计费科室'
            case 4:
              return '固定科室'
            default:
              return text
          }
        },
        width: 110
      },
      {
        title: '科室名称',
        dataIndex: 'dksNameTo',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.dksIdTo + '-' + row.dksNameTo
          }
        },
        width: 160
      },
      {
        title: '复议医生类型',
        dataIndex: 'personType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return '开单人员'
            case 2:
              return '执行人员'
            case 3:
              return '计费人员'
            case 4:
              return '固定人员'
            default:
              return text
          }
        },
        width: 110
      },
      {
        title: '医生名称',
        dataIndex: 'doctorNameTo',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.doctorCodeTo + '-' + row.doctorNameTo
          }
        },
        width: 130
      },
      {
        title: '操作员',
        dataIndex: 'operatorName',
        width: 130
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {
          customRender: 'operation'
        },
        fixed: 'right',
        width: 80
      }
      ]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
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
      this.addVisiable = false
      this.$message.success('新增成功')
      this.search()
    },
    handleAddClose () {
      this.addVisiable = false
    },
    add () {
      this.$refs.ybChsPriorityLevelAdd.setFormValues(this.user.areaType.value, this.queryParams.zymzType)
      this.addVisiable = true
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
      this.$refs.ybChsPriorityLevelEdit.setFormValues(record)
      this.editVisiable = true
    },
    zymzTypeChange () {
      this.search()
    },
    handlePlTypeChange (v) {
      if (v === undefined) {
        this.queryParams.plType = ''
      }
      this.search()
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
          let ybChsPriorityLevelIds = that.selectedRowKeys.join(',')
          that.$delete('ybChsPriorityLevel/' + ybChsPriorityLevelIds).then(() => {
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
      this.$export('ybChsPriorityLevel/excel', {
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams
      })
    },
    searchPage () {
      this.pagination.defaultCurrent = 1
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
      }
      this.search()
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
      params.areaType = this.user.areaType.value
      params.state = 1
      params.zymzType = this.queryParams.zymzType
      this.$get('ybChsPriorityLevel', {
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
