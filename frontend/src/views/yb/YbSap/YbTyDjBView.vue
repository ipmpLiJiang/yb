<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <a-row>
          <a-col :span=4>
              <a-form-item
                label="医保卡号"
                v-bind="formItemLayout"
              >
              <a-input
                placeholder="医保卡号"
                v-model="queryParams.kh"
              />
              </a-form-item>
          </a-col>
          <a-col :span=5>
              <a-form-item
                label="身份证号"
                v-bind="formItemLayout"
              >
              <a-input
                placeholder="身份证号"
                v-model="queryParams.sfzh"
              />
              </a-form-item>
          </a-col>
          <a-col :span=4>
              <a-form-item
                label="姓名"
                v-bind="formItemLayout"
              >
              <a-input
                placeholder="姓名"
                v-model="queryParams.xm"
              />
              </a-form-item>
          </a-col>
          <a-col :span=5>
            <a-button
              style="margin-left: 18px"
              type="primary"
              @click="search"
            >查询</a-button>
            <a-button
              style="margin-left: 18px"
              type="primary"
              @click="importExcel"
            >导出</a-button>
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
        :bordered="bordered"
        :scroll="{ x: 900 }"
      >
      </a-table>
    </div>
  </a-card>
</template>

<script>
import moment from 'moment'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbTyDjBView',
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
        kh: '',
        sfzh: '',
        xm: ''
      },
      loading: false,
      bordered: true,
      defaultApplyDate: this.formatDate(),
      user: this.$store.state.account.user,
      monthFormat: 'YYYYMM',
      tableFormat: 'YYYY-MM-DD'
    }
  },
  computed: {
    columns () {
      return [{
        title: '姓名',
        dataIndex: 'xm',
        fixed: 'left',
        width: 100
      },
      {
        title: '性别',
        dataIndex: 'xb',
        fixed: 'left',
        width: 90
      },
      {
        title: '联系电话',
        dataIndex: 'lxdh',
        fixed: 'left',
        width: 120
      },
      {
        title: '身份证号',
        dataIndex: 'sfzh',
        fixed: 'left',
        width: 150
      },
      {
        title: '医保卡号',
        dataIndex: 'ybkh',
        width: 120
      },
      {
        title: '特药名称',
        dataIndex: 'tymc',
        width: 150
      },
      {
        title: '商品名',
        dataIndex: 'spm',
        width: 150
      },
      {
        title: '用药依据',
        dataIndex: 'yyyj',
        width: 130
      },
      {
        title: '用法用量',
        dataIndex: 'yfyl',
        width: 130
      },
      {
        title: '诊断',
        dataIndex: 'zd',
        width: 150
      },
      {
        title: '科室',
        dataIndex: 'ks',
        width: 130
      },
      {
        title: '责任医生',
        dataIndex: 'zrys',
        width: 120
      },
      {
        title: '评估日期',
        dataIndex: 'pgrq',
        width: 130
      },
      {
        title: '确诊时间',
        dataIndex: 'qzsj',
        width: 130
      },
      {
        title: '备案时间',
        dataIndex: 'basj',
        width: 150
      },
      {
        title: '备注',
        dataIndex: 'bz',
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
      let datemonth = moment().subtract(1, 'months').format('YYYYMM')
      return datemonth
    },
    monthChange (date, dateString) {
      this.defaultApplyDate = dateString
    },
    importExcel () {
      if (this.dataSource.length > 0) {
        this.queryParams.dataJson = JSON.stringify(this.columns)
        this.$export('ybSap/excelTyDjB', {
          ...this.queryParams
        })
      } else {
        this.$message.warning('导出Excel，无数据!')
      }
    },
    search () {
      this.fetch({
        ...this.queryParams
      })
    },
    fetch (params = {}) {
      this.loading = true
      // if (this.paginationInfo) {
      //   // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
      //   this.$refs.TableInfo.pagination.current = this.paginationInfo.current
      //   this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
      //   params.pageSize = this.paginationInfo.pageSize
      //   params.pageNum = this.paginationInfo.current
      // } else {
      //   // 如果分页信息为空，则设置为默认值
      //   params.pageSize = this.pagination.defaultPageSize
      //   params.pageNum = this.pagination.defaultCurrent
      // }

      this.$get('ybSap/tyDjBList', {
        ...params
      }).then((r) => {
        if (r.data.success === 1) {
          let data = r.data.data
          this.loading = false
          this.dataSource = data
        } else {
          this.loading = false
          this.dataSource = []
          this.$message.error(r.data.message)
        }
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
