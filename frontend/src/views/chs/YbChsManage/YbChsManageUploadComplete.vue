<template>
  <a-drawer
    title="申诉更改"
    :maskClosable="false"
    width="82%"
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="chsCompleteVisiable"
    style="height: calc(100% - 15px); overflow: auto; padding-bottom: 53px"
  >
    <ybChsData-module ref="ybChsDataModule" :ybChsData="ybChsManageUpload">
    </ybChsData-module>
    <ybChsJk-module ref="ybChsJkModule" :ybChsData="ybChsManageUpload">
    </ybChsJk-module>
    <div style="margin-top: 10px">
      <div
        style="
          padding-top: 15px;
          padding-bottom: 15px;
          border: 1px solid #e8e8e8;
        "
      >
        <template>
          <a-form :form="form">
            <a-row type="flex" justify="start">
              <a-col :span="23">
                <!--复议科室、医生-->
                <a-row type="flex" justify="center">
                  <a-col :span="8">
                    <a-form-item v-bind="formItemLayout" label="复议科室">
                      {{ ybChsManageUpload.readyDksId }}-{{ ybChsManageUpload.readyDksName }}
                    </a-form-item>
                  </a-col>
                  <a-col :span="8">
                    <a-form-item v-bind="formItemLayout" label="复议医生">
                      {{ ybChsManageUpload.readyDoctorCode }}-{{ ybChsManageUpload.readyDoctorName }}
                    </a-form-item>
                  </a-col>
                  <a-col :span="1"> &nbsp; </a-col>
                  <a-col :span="6" style="color:red">
                    *复议上传附件：大小不得大于4MB，格式为.doc, .docx
                  </a-col>
                </a-row>
                <!--医院意见-->
                <a-row type="flex" justify="center">
                  <a-col :span="16">
                    <a-form-item
                      v-bind="{
                        labelCol: { span: 4 },
                        wrapperCol: { span: 19, offset: 1 },
                      }"
                      label="医院意见"
                    >
                      <a-textarea
                        placeholder="请输入医院意见"
                        style="width: 100%"
                        :rows="5"
                        v-decorator="[
                          'operateReason',
                          {
                            rules: [
                              { required: true, message: '医院意见不能为空' },
                            ],
                          },
                        ]"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :span="1"> &nbsp; </a-col>
                  <a-col :span="6">
                    <a-upload
                      accept=".doc,.docx"
                      :file-list="fileList"
                      :remove="handleImageRemove"
                      :beforeUpload="beforeUpload"
                    >
                      <a-button
                        v-if="fileBtnVisiable"
                      >
                        <a-icon type="upload" /> 上传附件
                      </a-button>
                    </a-upload>
                  </a-col>
                </a-row>
                <br />
                <a-row type="flex" justify="center">
                  <p style="color:red">{{lableErr}}</p>
                </a-row>
                <!--按钮-->
                <a-row type="flex" justify="center">
                  <a-col :span="5">
                    <a-button
                      @click="handleSubmit"
                      type="primary"
                      :loading="loading"
                      >保存</a-button
                    >
                  </a-col>
                  <a-col :span="3">
                    <a-button style="margin-right: 0.8rem" @click="onClose">
                      取消
                    </a-button>
                  </a-col>
                </a-row>
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
import YbChsDataModule from '../YbChsFunModule/YbChsDataModule'
import YbChsJkModule from '../YbChsFunModule/YbChsJkModule'
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
  name: 'YbChsManageUpload',
  components: {
    YbChsDataModule,
    YbChsJkModule
  },
  props: {
    chsCompleteVisiable: {
      default: false
    }
  },
  data () {
    return {
      formItemLayout,
      loading: false,
      ybChsManageUpload: {},
      ybChsManage: {},
      fileList: [],
      fileBtnVisiable: true,
      lableErr: '',
      user: this.$store.state.account.user,
      form: this.$form.createForm(this)
    }
  },
  computed: {},
  mounted () { },
  methods: {
    moment,
    reset () {
      this.form.resetFields()
    },
    beforeUpload (file) {
      var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
      testmsg = testmsg.toLowerCase()

      const isfile1 = testmsg === 'doc'
      const isfile2 = testmsg === 'docx'
      if (!(isfile1 || isfile2)) {
        this.$error({
          title: '只能上传.doc, .docx 格式的文件~'
        })
        return
      }
      const isLt = file.size / 1024 < 4001
      if (!isLt) {
        this.$error({
          title: '超4MB限制，不允许上传~'
        })
        return
      }
      return (isfile1 || isfile2) && isLt && this.customRequest(file)
    },
    customRequest (file) {
      const formData = new FormData()
      formData.append('file', file)
      formData.append('id', this.ybChsManageUpload.id)
      formData.append('refTab', 'yb_chs_result')
      formData.append('refType', 'chs')
      formData.append('fileType', 'doc')
      formData.append('applyDateStr', this.ybChsManageUpload.applyDateStr)
      formData.append('areaType', this.user.areaType.value)
      this.uploading = true
      let that = this
      this.$upload('comFile/uploadCheck', formData).then((r) => {
        if (r.data.data.success === 1) {
          that.fileList.unshift(r.data.data)
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
        this.$message.warning('申诉附件无法删除，申诉复议必须上传附件！')
        this.lableErr = '申诉附件无法删除，申诉复议必须上传附件！'
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
          formData.applyDateStr = that.ybChsManageUpload.applyDateStr
          formData.areaType = that.user.areaType.value
          that.$post('comFile/deleteFile', {
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
        onCancel () { }
      })
    },
    onClose () {
      this.loading = false
      this.ybChsManage = {}
      this.ybChsManageUpload = {}
      this.fileList = []
      this.$emit('close')
    },
    handleSubmit () {
      this.lableErr = ''
      // 复议判断图片必须上传
      if (this.fileList.length === 0) {
        this.$message.warning('未上传申诉附件，无法保存！')
        this.lableErr = '未上传申诉附件，无法保存！'
        return false
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          let fromData = this.form.getFieldsValue(['operateReason'])
          let data = {
            id: this.ybChsManageUpload.id,
            operateReason: fromData.operateReason
          }
          let jsonString = JSON.stringify(data)
          this.$put('ybChsManage/updateUploadStateCompleted', {
            dataJson: jsonString
          }).then((r) => {
            if (r.data.data.success === 1) {
              this.loading = false
              this.$message.success('保存成功！')
            } else {
              this.$message.error(r.data.data.message)
            }
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    setFormValues ({
      ...ybChsManageUpload
    }) {
      this.lableErr = ''
      this.ybChsManageUpload = ybChsManageUpload

      this.form.getFieldDecorator('operateReason')
      this.form.setFieldsValue({
        'operateReason': ybChsManageUpload.operateReason
      })

      this.findFileList(ybChsManageUpload.id)

      setTimeout(() => {
        this.$refs.ybChsJkModule.search()
      }, 200)
    },
    findFileList (id) {
      let formData = {}
      this.fileList = []
      formData.id = id
      formData.applyDateStr = this.ybChsManageUpload.applyDateStr
      formData.areaType = this.user.areaType.value
      this.$post('comFile/uploadFileList', {
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
