<template>
  <a-drawer
    title="查看"
    :maskClosable="false"
    width=65%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="lookVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
  <div style="text-align:center">
    <b><font :size="titleSize">
        {{ybNoticeLook.ntTitle}}
      </font></b>
  </div>
  <br>
  <a-row
    justify="start"
    type="flex"
  >
    <a-col :span=1>&nbsp;</a-col>
    <a-col :span=17>
      <font :size="othSize">
      发布时间：{{ybNoticeLook.releaseDate}}
      </font>
    </a-col>
    <a-col :span=4>
      <font :size="othSize">
      访问次数：{{ybNoticeLook.clickNum}}
      </font>
    </a-col>
    <a-col :span=1>&nbsp;</a-col>
  </a-row>
  <hr />
  <br>
  <a-row
    justify="center"
    type="flex"
  >
    <a-col :span=2>
      <b>
      <font :size="othSize">
      简介：
      </font>
      </b>
    </a-col>
    <a-col :span=17>
      <div style="border:3px solid #e8e8e8">
        <font :size="othSize">
        {{ybNoticeLook.ntExplain}}
        </font>
      </div>
    </a-col>
  </a-row>
  <br>
  <a-row
    justify="center"
    type="flex"
  >
    <a-col :span=19>
      <div id="divDetail">
      <font :size="othSize">
         <div v-html="ybNoticeLook.ntDetail"></div>
      </font>
      </div>
    </a-col>
  </a-row>
  <br>
  <a-row
    justify="center"
    type="flex"
  >
    <a-col :span=19>
      <a-button @click="onClose" type="primary" style="margin-right: .8rem">返回列表</a-button>
    </a-col>
  </a-row>
  <br>
  <a-row
    justify="center"
    type="flex"
  >
    <a-col :span=19>
    <a-table :columns="columns" :data-source="dataSource" :rowKey="record => record.id" :pagination="pagination" bordered :scroll="{ x: 500, y:400 }">
      <template
        slot="operation"
        slot-scope="text, record"
      >
        <a-popconfirm
        title="确定下载文件？"
        @confirm="() => download(record)"
        >
        <a>下载文件</a>
        </a-popconfirm>
      </template>
    </a-table>
    </a-col>
  </a-row>
  </a-drawer>
</template>

<script>
import moment from 'moment'
export default {
  name: 'YbNoticeLook',
  props: {
    lookVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      dataSource: [],
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
      titleSize: 6,
      othSize: 4,
      dataFormat: 'YYYY-MM-DD',
      ybNoticeLook: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '文件名称',
        dataIndex: 'clientName'
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 120
      }]
    }
  },
  mounted () {
  },
  methods: {
    moment,
    onClose () {
      this.ybNoticeLook = {}
      this.$emit('close')
    },
    frd () {
      let date = this.ybNoticeLook.releaseDate
      if (date !== '' && date !== null) {
        if (isNaN(date) && !isNaN(Date.parse(date))) {
          return moment(date).format(this.dataFormat)
        } else {
          return date
        }
      } else {
        return date
      }
    },
    findFileData () {
      this.pagination.defaultCurrent = 1
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
      }
      const pagination = { ...this.pagination }

      let params = {
        refTabId: this.ybNoticeLook.id
      }
      let defaultFile = []
      this.$get('comFile/findFileList', {
        ...params
      }).then((r) => {
        if (r.data.data.length > 0) {
          let ntData = r.data.data
          for (var i in ntData) {
            let item = {
              id: ntData[i].uid,
              clientName: ntData[i].name
            }
            defaultFile.push(item)
          }
          pagination.total = defaultFile.length
          this.dataSource = defaultFile
        } else {
          pagination.total = 0
          this.dataSource = []
        }
      })
      this.pagination = pagination
    },
    download (record) {
      let formData = {}
      formData.id = record.id
      formData.folderName = 'NoticeDoc'
      this.$download('comFile/downloadFile', {
        ...formData
      }, record.clientName)
    },
    setFormValues (obj) {
      this.ybNoticeLook = obj
      setTimeout(() => {
        this.findFileData()
        this.ybNoticeLook.releaseDate = this.frd()
        if (this.ybNoticeLook.state === 2) {
          this.upClickNum()
        }
      }, 200)
    },
    upClickNum () {
      let notice = {id: this.ybNoticeLook.id}
      this.$post('ybNotice/updateClickNum', {
        ...notice
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.ybNoticeLook.clickNum = r.data.data.data
        }
      }).catch(() => {
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
<style lang="css">
  #divDetail p {
    overflow: hidden;
    text-overflow: ellipsis;
    word-break: break-all;
    white-space: normal;
  }
</style>
