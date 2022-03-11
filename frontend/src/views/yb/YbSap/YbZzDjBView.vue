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
              label="重症编号"
              v-bind="formItemLayout"
            >
              <a-input
                placeholder="重症编号"
                v-model="queryParams.zzbh"
              />
            </a-form-item>
          </a-col>
          <a-col :span=3>
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
          <a-col :span=4>
              <a-form-item
                label="医保性质"
                v-bind="formItemLayout"
              >
              <a-input
                placeholder="医保性质"
                v-model="queryParams.xz"
              />
              </a-form-item>
          </a-col>
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
          <a-col :span=4>
              <a-form-item
                label="病种编号"
                v-bind="formItemLayout"
              >
              <a-input
                placeholder="病种编号"
                v-model="queryParams.bzbh"
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
        size="small"
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
  name: 'YbZzDjBView',
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
        zzbh: '',
        xm: '',
        xz: '',
        kh: '',
        bzbh: ''
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
        title: '重症编号',
        dataIndex: 'zzbh',
        fixed: 'left',
        width: 100
      },
      {
        title: '姓名',
        dataIndex: 'xm',
        fixed: 'left',
        width: 100
      },
      {
        title: '联系方式',
        dataIndex: 'lxfs',
        fixed: 'left',
        width: 120
      },
      {
        title: '医保性质',
        dataIndex: 'ybxz',
        width: 100
      },
      {
        title: '医保卡号',
        dataIndex: 'ybkh',
        width: 100
      },
      {
        title: '病种编号',
        dataIndex: 'bzbh',
        width: 100
      },
      {
        title: '病种名称',
        dataIndex: 'bzmc',
        width: 120
      },
      {
        title: '转入时间',
        dataIndex: 'zrsj',
        width: 130
      },
      {
        title: '辖区',
        dataIndex: 'xq',
        width: 120
      },
      {
        title: '医保办经办人',
        dataIndex: 'ybjbr',
        width: 130
      },
      {
        title: '转出时间',
        dataIndex: 'zcsj',
        width: 130
      },
      {
        title: '转出经办人 ',
        dataIndex: 'zcjbr',
        width: 130
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
        this.$export('ybSap/excelZzDjB', {
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

      this.$get('ybSap/zzDjBList', {
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
