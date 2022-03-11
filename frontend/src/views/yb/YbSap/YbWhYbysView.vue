<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <a-row>
          <a-col :span=5>
            <a-form-item
              label="所在期"
              v-bind="formItemLayout"
            >
              <a-month-picker
                placeholder="请选择复议年月"
                style="width: 105px"
                @change="monthChange"
                :default-value="defaultApplyDate"
                :format="monthFormat"
              />
            </a-form-item>
          </a-col>
          <a-col :span=6>
              <a-form-item
                label="院区"
                v-bind="formItemLayout"
              >
                <a-select
                  :value="selectYuanQu"
                   @change="handleYuanQuChange"
                  style="width: 130px"
                >
                  <a-select-option
                    v-for="d in selectYuanQuSource"
                    :key="d.value"
                  >
                    {{ d.text }}
                  </a-select-option>
                </a-select>
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
  name: 'YbWhYbysView',
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
      selectYuanQuSource: [
        { text: '本院', value: 1 },
        { text: '西院', value: 2 },
        { text: '金银湖', value: 3 }
      ],
      selectYuanQu: 1,
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
        title: '院区',
        dataIndex: 'zyq',
        fixed: 'left',
        width: 90
      },
      {
        title: '周期',
        dataIndex: 'iperi',
        fixed: 'left',
        width: 90
      },
      {
        title: '医保类型',
        dataIndex: 'ybfl',
        fixed: 'left',
        width: 90
      },
      {
        title: '辖区',
        dataIndex: 'jsxq',
        fixed: 'left',
        width: 100
      },
      {
        title: '应收合计',
        dataIndex: 'yshj',
        width: 120
      },
      {
        title: '回款合计',
        dataIndex: 'hkhj',
        width: 100
      },
      {
        title: '基本医疗-统1+统2',
        dataIndex: 't1T21',
        width: 150
      },
      {
        title: '基本医疗-个人账户、门诊统筹',
        dataIndex: 'grzh2',
        width: 150
      },
      {
        title: '基本医疗-回款额',
        dataIndex: 'hke12',
        width: 150
      },
      {
        title: '基本医疗-回款时间',
        dataIndex: 'hkrq12',
        width: 150
      },
      {
        title: '公务员补贴-应收款',
        dataIndex: 'ysk3',
        width: 150
      },
      {
        title: '公务员补贴-回款额',
        dataIndex: 'hke3',
        width: 150
      },
      {
        title: '公务员补贴-回款时间',
        dataIndex: 'hkrq3',
        width: 150
      },
      {
        title: '大额费用、大病保险-应收款',
        dataIndex: 'ysk4',
        width: 150
      },
      {
        title: '大额费用、大病保险-回款额',
        dataIndex: 'hke4',
        width: 150
      },
      {
        title: '大额费用、大病保险-回款时间',
        dataIndex: 'hkrq4',
        width: 150
      },
      {
        title: '门诊个人账户-应收款',
        dataIndex: 'ysk5',
        width: 150
      },
      {
        title: '门诊个人账户-回款额',
        dataIndex: 'hke5',
        width: 150
      },
      {
        title: '门诊个人账户-回款时间',
        dataIndex: 'hkrq5',
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
    handleYuanQuChange (val) {
      this.selectYuanQu = val
    },
    importExcel () {
      if (this.dataSource.length > 0) {
        this.queryParams.iperi = this.defaultApplyDate
        this.queryParams.yq = this.selectYuanQu
        this.queryParams.dataJson = JSON.stringify(this.columns)
        this.$export('ybSap/excelWhYbys', {
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
      params.iperi = this.defaultApplyDate
      params.yq = this.selectYuanQu
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

      this.$get('ybSap/whYbysList', {
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
