<template>
  <a-drawer
    title="申诉填报"
    :maskClosable="false"
    width=70%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="appealVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
  <appealData-module
  ref="appealDataModule"
  :ybAppealDataModule="ybAppealManageUpload"
  >
  </appealData-module>
    <template>
      <!-- 表格区域 -->
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
      <a slot="action" slot-scope="text">action</a>
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
      </a-table>
    </template>
    <template>
    </template>
    <div style="margin-top:20px;">
    <div style="padding-top:20px;padding-bottom:20px;border: 1px solid #e8e8e8;">
      <template>
        <a-form :form="form">
        <a-row type="flex" justify="start">
          <a-col :span=12>
            <!--复议科室、医生-->
            <a-row>
              <a-col :span=10>
                <a-form-item
                  v-bind="formItemLayout"
                  label="复议科室"
                >
                  {{ybAppealManageUpload.readyDeptName}}
                </a-form-item>
              </a-col>
              <a-col :span=14>
                <a-form-item
                  v-bind="formItemLayout"
                  label="复议医生"
                >
                  {{ybAppealManageUpload.readyDoctorName}}
                </a-form-item>
              </a-col>
            </a-row>
            <!--申诉理由-->
            <a-row type="flex" justify="start">
              <a-col :span=24>
                 <a-form-item
                  v-bind="{
                    labelCol: { span: 4 },
                    wrapperCol: { span: 19, offset: 1 }
                  }"
                  label="申诉理由"
                >
                <a-textarea
                  placeholder="请输入申诉理由"
                  style="width:100%;"
                  :rows="5"
                  v-decorator="['operateReason', {rules: [{ required: true, message: '申诉理由不能为空' }] }]"
                />
                </a-form-item>
              </a-col>
            </a-row>
            <!--按钮-->
            <a-row type="flex" justify="end">
              <a-col :span=5>
                <a-popconfirm
                  title="确定提交数据？提交后不可更改！"
                  @confirm="handleSubmit(1)"
                  okText="确定"
                  cancelText="取消"
                >
                  <a-button type="primary" style="margin-right: .8rem">提交</a-button>
                </a-popconfirm>
              </a-col>
              <a-col :span=5>
                <a-button
                  @click="handleSubmit(0)"
                  type="primary"
                  :loading="loading"
                >保存</a-button>
              </a-col>
              <a-col :span=3>
                <a-button
                  style="margin-right: .8rem"
                  @click="onClose"
                >
                  取消
                </a-button>
              </a-col>
            </a-row>
          </a-col>
          <a-col :span=12>
            <div style="color:red">*复议上传图片：</div>
            <div style="margin-left:20px;height:100%">
            <a-row type="flex" justify="center">
              <a-col>
                <template>
                  <!--上传图片-->
                  <div class="clearfix">
                    <a-upload
                      list-type="picture-card"
                      accept=".gif,.jpg, .jpeg,.png"
                      :file-list="fileList"
                      :remove="handleImageRemove"
                      :beforeUpload="beforeUpload"
                      @change="handleChange"
                      @preview="handlePreview"
                    >
                      <div v-if="fileList.length < 8">
                        <a-icon type="plus" />
                        <div class="ant-upload-text">
                          Upload
                        </div>
                      </div>
                    </a-upload>
                    <a-modal :width="600" :visible="previewVisible" :footer="null" @cancel="handleCancel">
                      <img alt="example" style="width: 100%" :src="previewImage" />
                    </a-modal>
                  </div>
                </template>
              </a-col>
            </a-row>
            </div>
          </a-col>
        </a-row>
        </a-form>
      </template>
    </div>
    </div>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import AppealDataModule from '../ybFunModule/AppealDataModule'
const formItemLayout = {
  labelCol: { span: 10 },
  wrapperCol: { span: 13, offset: 1 }
}
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'YbAppealManageUpload',
  components: {
    AppealDataModule},
  props: {
    appealVisiable: {
      default: false
    }
  },
  data () {
    return {
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
      ybAppealManageUpload: {},
      ybAppealManage: {},
      previewVisible: false,
      previewImage: '',
      fileList: [],
      form: this.$form.createForm(this)
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'id',
        width: 70,
        fixed: 'left'
      },
      {
        title: '住院号',
        dataIndex: 'inpatientId',
        width: 100,
        fixed: 'left'
      },
      {
        title: '患者姓名',
        dataIndex: 'patientName',
        width: 100,
        fixed: 'left'
      },
      {
        title: 'HIS结算序号',
        dataIndex: 'settlementId',
        width: 120
      },
      {
        title: '单据号',
        dataIndex: 'billNo',
        width: 100
      },
      {
        title: '交易流水号',
        dataIndex: 'transNo',
        width: 120
      },
      {
        title: '项目代码',
        dataIndex: 'itemId',
        width: 100
      },
      {
        title: '项目医保编码',
        dataIndex: 'itemCode',
        width: 130
      },
      {
        title: '项目名称',
        dataIndex: 'itemName',
        width: 100
      },
      {
        title: '项目数量',
        dataIndex: 'itemCount',
        width: 100
      },
      {
        title: '项目单价',
        dataIndex: 'itemPrice',
        width: 100
      },
      {
        title: '项目金额',
        dataIndex: 'itemAmount',
        width: 100
      },
      {
        title: '费用日期',
        dataIndex: 'costDateStr',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return moment(text).format('YYYY-MM-DD')
          } else {
            return text
          }
        },
        width: 100
      },
      {
        title: '住院科室名称',
        dataIndex: 'deptName',
        width: 130
      },
      {
        title: '开方医生名称',
        dataIndex: 'orderDocName',
        width: 130
      },
      {
        title: '执行科室名称',
        dataIndex: 'excuteDeptName',
        width: 130
      },
      {
        title: '执行医生名称',
        dataIndex: 'excuteDocName',
        width: 130
      },
      {
        title: '结算时间',
        dataIndex: 'settlementDate',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return moment(text).format('YYYY-MM-DD')
          } else {
            return text
          }
        },
        width: 120
      }]
    }
  },
  mounted () {
  },
  methods: {
    moment,
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
      this.dataSource = []
      this.form.resetFields()
    },
    beforeUpload (file) {
      // 限制图片 格式、size、分辨率
      const isJPG = file.type === 'image/jpg'
      const isJPEG = file.type === 'image/jpeg'
      const isGIF = file.type === 'image/gif'
      const isPNG = file.type === 'image/png'
      if (!(isJPG || isJPEG || isGIF || isPNG)) {
        this.$error({
          title: '只能上传JPG 、JPEG 、GIF、 PNG格式的图片~'
        })
        return
      }
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$error({
          title: '超2M限制，不允许上传~'
        })
        return
      }
      return (isJPG || isJPEG || isGIF || isPNG) && isLt2M && this.customRequest(file)
    },
    handleChange (info) {
      if (info.file.status === 'done') {
        console.log(info)
      }
    },
    customRequest (file) {
      const formData = new FormData()
      formData.append('file', file)
      formData.append('id', this.ybAppealManageUpload.id)
      formData.append('refTab', 'yb_appeal_result')
      formData.append('proposalCode', this.ybAppealManageUpload.proposalCode)
      formData.append('deptName', this.ybAppealManageUpload.readyDeptName)
      formData.append('applyDateStr', this.ybAppealManageUpload.applyDateStr)
      formData.append('sourceType', this.ybAppealManageUpload.sourceType)
      this.uploading = true
      let that = this

      this.$upload('comFile/uploadImg', formData).then((r) => {
        that.fileList.push(r.data.data)
        this.uploading = false
        this.$message.success('上传成功.')
      }).catch(() => {
        this.uploading = false
        this.$message.error('上传失败.')
      })
    },
    handleImageRemove (file) {
      let formData = {}
      formData.id = file.uid
      formData.deptName = this.ybAppealManageUpload.readyDeptName
      formData.applyDateStr = this.ybAppealManageUpload.applyDateStr
      formData.sourceType = this.ybAppealManageUpload.sourceType
      formData.serName = file.serName
      this.$post('comFile/deleteImg', {
        ...formData
      }).then((r) => {
        if (r.data.success === true) {
          this.$message.success('删除成功')
          const index = this.fileList.indexOf(file)
          const newFileList = this.fileList.slice()
          newFileList.splice(index, 1)
          this.fileList = newFileList
        } else {
          this.$message.success('删除失败')
        }
      })
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    onClose () {
      this.loading = false
      this.ybAppealManage = {}
      this.ybAppealManageUpload = {}
      this.fileList = []
      this.previewVisible = false
      this.previewImage = ''
      this.reset()
      this.$emit('close')
    },
    handleSubmit (type) {
      this.form.validateFields((err, values) => {
        if (!err) {
          let fromData = this.form.getFieldsValue(['operateReason'])
          let acceptState = type === 0 ? 1 : 6 // 1 保存 6提交
          // 复议判断图片必须上传
          // if (acceptState === 6 && this.fileList.length < 1) {
          //   this.$message.warning('未上传复议图片，无法提交！')
          //   return false
          // }
          let data = {
            id: this.ybAppealManageUpload.id,
            acceptState: acceptState,
            sourceType: this.ybAppealManageUpload.sourceType,
            applyDataId: this.ybAppealManageUpload.applyDataId,
            verifyId: this.ybAppealManageUpload.verifyId,
            readyDeptCode: this.ybAppealManageUpload.readyDeptCode,
            readyDeptName: this.ybAppealManageUpload.readyDeptName,
            readyDoctorCode: this.ybAppealManageUpload.readyDoctorCode,
            readyDoctorName: this.ybAppealManageUpload.readyDoctorName,
            dataType: this.ybAppealManageUpload.dataType,
            operateReason: fromData.operateReason
          }
          let jsonString = JSON.stringify(data)
          this.$put('ybAppealManage/updateUploadState', {
            dataJson: jsonString
          }).then((r) => {
            if (acceptState === 6) {
              this.$message.success('提交成功！')
              this.onClose()
              this.$emit('success')
            } else {
              this.loading = false
              this.$message.success('保存成功！')
            }
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    setFormValues ({ ...ybAppealManageUpload }) {
      this.ybAppealManageUpload = ybAppealManageUpload

      this.form.getFieldDecorator('operateReason')
      this.form.setFieldsValue({
        'operateReason': ybAppealManageUpload.operateReason
      })

      this.findFileList(ybAppealManageUpload.id)
      this.search()
    },
    findCreate (ybAppealResult) {
      // 列表点击申诉时创建复议审核上传数据，业务需求变更不调用此方法,更改为提交时创建复议审核上传数据
      this.$post('ybAppealResult/findCreateAppealResult', {
        ...ybAppealResult
      }).then((r) => {
        if (r.data.data.success === 0) {
          this.$message.error('查询创建失败!')
          this.onClose()
        } else {
          ybAppealResult = r.data.data.data
          this.ybAppealManageUpload.operateReason = ybAppealResult.operateReason
          this.ybAppealManageUpload.readyDeptCode = ybAppealResult.deptCode
          this.ybAppealManageUpload.readyDeptName = ybAppealResult.deptName
          this.ybAppealManageUpload.readyDoctorCode = ybAppealResult.doctorCode
          this.ybAppealManageUpload.readyDoctorName = ybAppealResult.doctorName

          this.form.setFieldsValue({
            'operateReason': ybAppealResult.operateReason
          })
        }
      }).catch(() => {
        this.loading = false
      })
    },
    findFileList (id) {
      let formData = {}
      formData.id = id
      formData.deptName = this.ybAppealManageUpload.readyDeptName
      formData.applyDateStr = this.ybAppealManageUpload.applyDateStr
      formData.sourceType = this.ybAppealManageUpload.sourceType
      this.$post('comFile/listImgComFile', {
        ...formData
      }).then((r) => {
        for (let data of r.data.data) {
          this.fileList.push(data)
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
      params.billNo = this.ybAppealManageUpload.billNo
      params.transNo = this.ybAppealManageUpload.serialNo
      params.itemCode = this.ybAppealManageUpload.projectCode
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
      this.$get('ybReconsiderInpatientfees/ListEq', {
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
/* you can make up upload button and sample style by using stylesheets */
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
