<template>
  <a-drawer
    title="申诉填报"
    :maskClosable="false"
    width="80%"
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="chsVisiable"
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
              <a-col :span="20">
                <!--复议科室、医生-->
                <a-row type="flex" justify="center">
                  <a-col :span="9">
                    <a-form-item v-bind="formItemLayout" label="复议科室">
                      {{ ybChsManageUpload.readyDksId }}-{{ ybChsManageUpload.readyDksName }}
                    </a-form-item>
                  </a-col>
                  <a-col :span="9">
                    <a-form-item v-bind="formItemLayout" label="复议医生">
                      {{ ybChsManageUpload.readyDoctorCode }}-{{ ybChsManageUpload.readyDoctorName }}
                    </a-form-item>
                  </a-col>
                  <a-col :span="5"> &nbsp; </a-col>
                </a-row>
                <!--医院意见-->
                <a-row type="flex" justify="center">
                  <a-col :span="18">
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
                  <a-col :span="5"> &nbsp; </a-col>
                </a-row>
                <!--上传显示图片-->
                <a-row type="flex" justify="start">
                  <a-col :span="1"> &nbsp; </a-col>
                  <a-col :span="23">
                    <div style="color: red">
                      *&nbsp;复议上传图片：图片大小不得大于300KB，图片格式为.jpg
                    </div>
                    <div
                      style="margin-top: 10px; margin-left: 10px; height: 100%"
                    >
                      <a-row type="flex" justify="center">
                        <a-col :span="1"> &nbsp; </a-col>
                        <a-col :span="20">
                          佐证资料：<br />资料类型(勾选)：
                          <a-radio-group
                            v-model="ftype"
                            @change="typeChange"
                            :options="typeList"
                            size="large"
                          />
                        </a-col>
                        <a-col :span="3"> &nbsp; </a-col>
                      </a-row>
                      <br />
                      <a-row type="flex" justify="center">
                        <a-col :span="24">
                          <template>
                            <!--上传图片-->
                            <div class="clearfix">
                              <a-upload
                                list-type="picture"
                                accept=".jpg"
                                :file-list="fileList"
                                :remove="handleImageRemove"
                                :beforeUpload="beforeUpload"
                                @preview="handlePreview"
                                class="upload-list-inline"
                              >
                                <a-button>
                                  <a-icon type="upload" /> 上传图片
                                </a-button>
                                <span style="color: red">{{ ftypeName }}</span>
                              </a-upload>
                              <a-modal
                                width="85%"
                                :visible="previewVisible"
                                :footer="null"
                                @cancel="handleCancel"
                              >
                                <div style="text-align: center">
                                  <img
                                    alt="example"
                                    style="
                                      width: auto;
                                      height: auto;
                                      max-width: 100%;
                                      max-height: 100%;
                                    "
                                    :src="previewImage"
                                  />
                                </div>
                              </a-modal>
                            </div>
                          </template>
                        </a-col>
                      </a-row>
                    </div>
                  </a-col>
                </a-row>
                <br />
                <a-row type="flex" justify="center">
                  <p style="color:red">{{lableErr}}</p>
                </a-row>
                <!--按钮-->
                <a-row type="flex" justify="center">
                  <a-col :span="5">
                    <a-popconfirm
                      title="确定提交数据？提交后不可更改！"
                      @confirm="handleSubmit(1)"
                      okText="确定"
                      cancelText="取消"
                    >
                      <a-button type="primary" style="margin-right: 0.8rem"
                        >提交</a-button
                      >
                    </a-popconfirm>
                  </a-col>
                  <a-col :span="5">
                    <a-button
                      @click="handleSubmit(0)"
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

function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'YbChsManageUpload',
  components: {
    YbChsDataModule,
    YbChsJkModule
  },
  props: {
    chsVisiable: {
      default: false
    }
  },
  data () {
    return {
      formItemLayout,
      loading: false,
      ybChsManageUpload: {},
      ybChsManage: {},
      previewVisible: false,
      previewImage: '',
      fileList: [],
      ftype: '',
      ftypeName: '',
      typeList: [],
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
      formData.append('id', this.ybChsManageUpload.id)
      formData.append('refTab', 'yb_chs_result')
      formData.append('refType', this.ftype)
      formData.append('refTypeName', this.ftypeName)
      formData.append('orderNum', this.ybChsManageUpload.orderNum)
      formData.append('applyDateStr', this.ybChsManageUpload.applyDateStr)
      formData.append('isCheck', 1)
      formData.append('areaType', this.user.areaType.value)
      this.uploading = true
      let that = this
      this.$upload('comFile/uploadChsImg', formData).then((r) => {
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
          formData.applyDateStr = that.ybChsManageUpload.applyDateStr
          // formData.refType = that.ftype
          formData.serName = file.serName
          formData.orderNum = that.ybChsManageUpload.orderNum
          formData.areaType = that.user.areaType.value
          that.$post('comFile/deleteChsImg', {
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
      this.ybChsManage = {}
      this.ybChsManageUpload = {}
      this.typeList = []
      this.fileList = []
      this.previewVisible = false
      this.previewImage = ''
      this.$emit('close')
    },
    handleSubmit (type) {
      this.lableErr = ''
      this.form.validateFields((err, values) => {
        if (!err) {
          let fromData = this.form.getFieldsValue(['operateReason'])
          let state = type === 0 ? 1 : 6 // 1 保存 6提交
          // 复议判断图片必须上传
          if (state === 6 && this.fileList.length < 1) {
            this.$message.warning('未上传复议图片，无法提交！')
            this.lableErr = '未上传复议图片，无法提交！'
            return false
          }
          let data = {
            id: this.ybChsManageUpload.id,
            state: state,
            applyDataId: this.ybChsManageUpload.applyDataId,
            verifyId: this.ybChsManageUpload.verifyId,
            readyDksId: this.ybChsManageUpload.readyDksId,
            readyDksName: this.ybChsManageUpload.readyDksName,
            readyDoctorCode: this.ybChsManageUpload.readyDoctorCode,
            readyDoctorName: this.ybChsManageUpload.readyDoctorName,
            operateReason: fromData.operateReason
          }
          let jsonString = JSON.stringify(data)
          this.$put('ybChsManage/updateUploadState', {
            dataJson: jsonString
          }).then((r) => {
            if (r.data.data.success === 1) {
              if (state === 6) {
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
    setFormValues ({
      ...ybChsManageUpload
    }) {
      this.lableErr = ''
      this.ybChsManageUpload = ybChsManageUpload

      this.form.getFieldDecorator('operateReason')
      this.form.setFieldsValue({
        'operateReason': ybChsManageUpload.operateReason
      })

      setTimeout(() => {
        this.$refs.ybChsJkModule.search()
        this.getType(this.ybChsManageUpload.id)
      }, 200)
    },
    getType (id) {
      this.ftype = ''
      this.typeList = []
      this.$get('comType/getComTypeList', {
        ctType: 2, isDeletemark: 1
      }).then((r) => {
        if (r.data.length > 0) {
          for (var i in r.data) {
            var type = {
              label: r.data[i].ctName,
              value: r.data[i].id.toString()
            }
            this.typeList.push(type)
            if (this.ftype === '') {
              this.ftype = type.value
              this.ftypeName = type.label
              this.findFileList(id)
            }
          }
        } else {
          this.typeList = []
        }
      }).catch(() => {
        this.typeList = []
      })
    },
    typeChange (e) {
      this.ftype = e.target.value
      let target = this.typeList.filter(item => item.value === this.ftype)[0]
      this.ftypeName = target.label
      // this.findFileList(this.ybChsManageUpload.id)
    },
    findCreate (ybChsResult) {
      // 列表点击申诉时创建复议审核上传数据，业务需求变更不调用此方法,更改为提交时创建复议审核上传数据
      this.$post('ybChsResult/findCreateChsResult', {
        ...ybChsResult
      }).then((r) => {
        if (r.data.data.success === 0) {
          this.$message.error('查询创建失败!')
          this.onClose()
        } else {
          ybChsResult = r.data.data.data
          this.ybChsManageUpload.operateReason = ybChsResult.operateReason
          this.ybChsManageUpload.readyDksId = ybChsResult.dksId
          this.ybChsManageUpload.readyDksName = ybChsResult.dksName
          this.ybChsManageUpload.readyDoctorCode = ybChsResult.doctorCode
          this.ybChsManageUpload.readyDoctorName = ybChsResult.doctorName

          this.form.setFieldsValue({
            'operateReason': ybChsResult.operateReason
          })
        }
      }).catch(() => {
        this.loading = false
      })
    },
    findFileList (id) {
      let formData = {}
      this.fileList = []
      formData.id = id
      formData.applyDateStr = this.ybChsManageUpload.applyDateStr
      // formData.refType = this.ftype
      formData.orderNum = this.ybChsManageUpload.orderNum
      formData.areaType = this.user.areaType.value
      formData.isOn = 1
      this.$post('comFile/listChsImgComFile', {
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
</style><style scoped>
.upload-list-inline >>> .ant-upload-list-item {
  float: left;
  width: 230px;
  margin-right: 8px;
}

.upload-list-inline >>> .ant-upload-animate-enter {
  animation-name: uploadAnimateInlineIn;
}

.upload-list-inline >>> .ant-upload-animate-leave {
  animation-name: uploadAnimateInlineOut;
}
</style>
