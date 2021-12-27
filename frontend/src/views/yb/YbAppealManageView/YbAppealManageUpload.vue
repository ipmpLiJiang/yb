<template>
  <a-drawer
    title="申诉填报"
    :maskClosable="false"
    width=75%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="appealVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
  <appealData-module
  ref="appealDataModule"
  :ybAppealDataModule="ybAppealManageUpload"
  >
  </appealData-module>
    <inpatientfees-module
    ref="inpatientfeesModule"
    :inpatientfeesModule="ybAppealManageUpload"
    >
    </inpatientfees-module>
    <div style="margin-top:20px;">
      <a-popconfirm
        title="确定获取上次复议数据！"
        @confirm="loadLastData"
        v-show="isShow"
        okText="确定"
        cancelText="取消"
      >
        <a-button type="primary" style="margin-right: .8rem">获取上次复议数据</a-button>
      </a-popconfirm>
    <div style="padding-top:20px;padding-bottom:20px;border: 1px solid #e8e8e8;">
      <template>
        <a-form :form="form">
        <a-row type="flex" justify="start">
          <a-col :span=12>
            <!--复议科室、医生-->
            <a-row>
              <a-col :span=13>
                <a-form-item
                  v-bind="formItemLayout"
                  label="复议科室"
                >
                  {{ybAppealManageUpload.readyDeptCode}}-{{ybAppealManageUpload.readyDeptName}}
                </a-form-item>
              </a-col>
              <a-col :span=11>
                <a-form-item
                  v-bind="formItemLayout"
                  label="复议医生"
                >
                  {{ybAppealManageUpload.readyDoctorCode}}-{{ybAppealManageUpload.readyDoctorName}}
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
                  :rows="6"
                  v-decorator="['operateReason', {rules: [{ required: true, message: '申诉理由不能为空' }] }]"
                />
                </a-form-item>
              </a-col>
            </a-row>
            <!--按钮-->
            <a-row type="flex" justify="center">
              <p style="color:red">{{lableErr}}</p>
            </a-row>
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
            <div style="color:red">*复议上传图片：最多上传8张，图片大小不得大于300KB，图片格式为.jpg</div>
            <div style="margin-top:10px;margin-left:20px;height:100%">
            <a-row type="flex" justify="center">
              <a-col>
                <template>
                  <!--上传图片-->
                  <div class="clearfix">
                    <a-upload
                      list-type="picture-card"
                      accept=".jpg"
                      :file-list="fileList"
                      :remove="handleImageRemove"
                      :beforeUpload="beforeUpload"
                      @preview="handlePreview"
                    >
                      <div v-if="fileList.length < 8">
                        <a-icon type="plus" />
                        <div class="ant-upload-text">
                          Upload
                        </div>
                      </div>
                    </a-upload>
                    <a-modal width="85%" :visible="previewVisible" :footer="null" @cancel="handleCancel">
                      <div style="text-align:center">
                        <img alt="example" style="width: auto; height: auto; max-width: 100%; max-height: 100%;" :src="previewImage" />
                      </div>
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
import InpatientfeesModule from '../ybFunModule/InpatientfeesModule'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 14, offset: 1 }
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
    AppealDataModule, InpatientfeesModule},
  props: {
    appealVisiable: {
      default: false
    }
  },
  data () {
    return {
      formItemLayout,
      loading: false,
      ybAppealManageUpload: {},
      ybAppealManage: {},
      previewVisible: false,
      previewImage: '',
      isShow: false,
      fileList: [],
      lableErr: '',
      user: this.$store.state.account.user,
      form: this.$form.createForm(this)
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    moment,
    reset () {
      this.form.resetFields()
    },
    beforeUpload (file) {
      // 限制图片 格式、size、分辨率
      const isJPG = file.type === 'image/jpg'
      const isJPEG = file.type === 'image/jpeg'
      // const isGIF = file.type === 'image/gif'
      // const isPNG = file.type === 'image/png'
      if (!(isJPG || isJPEG)) {
        this.$error({
          title: '只能上传JPG 格式的图片~'
        })
        return
      }
      const isLt2M = file.size / 1024 < 301
      if (!isLt2M) {
        this.$error({
          title: '超300KB限制，不允许上传~'
        })
        return
      }
      return (isJPG || isJPEG) && isLt2M && this.customRequest(file)
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
      formData.append('typeno', this.ybAppealManageUpload.typeno)
      formData.append('isCheck', 1)
      formData.append('areaType', this.user.areaType.value)
      this.uploading = true
      let that = this
      this.$upload('comFile/uploadImg', formData).then((r) => {
        if (r.data.data.success === 1) {
          that.fileList.push(r.data.data)
          this.uploading = false
          this.$message.success('上传成功.')
          this.lableErr = ''
        } else {
          this.$message.error(r.data.data.message)
        }
      }).catch(() => {
        this.uploading = false
        this.$message.error('上传失败.')
      })
    },
    handleImageRemove (file) {
      if (this.fileList.length === 1) {
        this.$message.warning('复议图片无法删除，请确认保，至少存在一张复议图片！')
        this.lableErr = '复议图片无法删除，请确认保，至少存在一张复议图片！'
        return false
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的附件?',
        content: '当您点击确定按钮后，这些附件将会被彻底删除',
        centered: true,
        onOk () {
          let formData = {}
          formData.id = file.uid
          formData.deptName = that.ybAppealManageUpload.readyDeptName
          formData.applyDateStr = that.ybAppealManageUpload.applyDateStr
          formData.sourceType = that.ybAppealManageUpload.sourceType
          formData.serName = file.serName
          formData.areaType = that.user.areaType.value
          that.$post('comFile/deleteImg', {
            ...formData
          }).then((r) => {
            that.uploading = false
            if (r.data.data.success === 1) {
              that.$message.success('删除成功')
              const index = that.fileList.indexOf(file)
              const newFileList = that.fileList.slice()
              newFileList.splice(index, 1)
              that.fileList = newFileList
            } else {
              that.$message.error(r.data.data.message)
            }
          }).catch(() => {
            that.$message.error('删除失败.')
          })
        },
        onCancel () {
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
      this.isShow = false
      this.previewImage = ''
      this.$emit('close')
    },
    loadLastData () {
      if (this.ybAppealManageUpload.sourceType === 1) {
        let formData = {
          verifyId: this.ybAppealManageUpload.verifySendId,
          applyDataId: this.ybAppealManageUpload.applyDataId,
          id: this.ybAppealManageUpload.id,
          applyDateStr: this.ybAppealManageUpload.applyDateStr,
          areaType: this.user.areaType.value
        }
        this.$post('ybAppealResult/findLoadLastAppealResul', {
          ...formData
        }).then((r) => {
          if (r.data.data.success === 1) {
            this.findFileList(formData.id)
          } else {
            if (r.data.data.message !== '') {
              this.$message.error(r.data.data.message)
            } else {
              this.$message.error('获取上次复议数据操作失败.')
            }
          }
          if (r.data.data.data !== '') {
            this.form.getFieldDecorator('operateReason')
            this.form.setFieldsValue({
              'operateReason': r.data.data.data
            })
          }
        })
      }
    },
    handleSubmit (type) {
      this.lableErr = ''
      this.form.validateFields((err, values) => {
        if (!err) {
          let fromData = this.form.getFieldsValue(['operateReason'])
          let acceptState = type === 0 ? 1 : 6 // 1 保存 6提交
          // 复议判断图片必须上传
          if (acceptState === 6 && this.fileList.length < 1) {
            this.$message.warning('未上传复议图片，无法提交！')
            this.lableErr = '未上传复议图片，无法保存！'
            return false
          }
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
            if (r.data.data.success === 1) {
              if (acceptState === 6) {
                this.$message.success('提交成功！')
                this.onClose()
                this.$emit('success')
              } else {
                this.loading = false
                this.$message.success('保存成功！')
              }
            } else {
              this.$message.error(r.data.data.message)
            }
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    setFormValues ({ ...ybAppealManageUpload }) {
      this.lableErr = ''
      this.ybAppealManageUpload = ybAppealManageUpload

      if (this.ybAppealManageUpload.sourceType === 1) {
        this.isShow = true
      } else {
        this.isShow = false
      }

      this.form.getFieldDecorator('operateReason')
      this.form.setFieldsValue({
        'operateReason': ybAppealManageUpload.operateReason
      })

      this.findFileList(ybAppealManageUpload.id)

      setTimeout(() => {
        this.$refs.inpatientfeesModule.search()
      }, 200)
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
      formData.deptId = this.ybAppealManageUpload.readyDeptCode
      formData.applyDateStr = this.ybAppealManageUpload.applyDateStr
      formData.sourceType = this.ybAppealManageUpload.sourceType
      formData.areaType = this.user.areaType.value
      this.$post('comFile/listImgComFile', {
        ...formData
      }).then((r) => {
        for (let data of r.data.data) {
          data.url = this.$baseURL + data.url
          data.thumbUrl = this.$baseURL + data.thumbUrl
          this.fileList.push(data)
        }
      })
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
