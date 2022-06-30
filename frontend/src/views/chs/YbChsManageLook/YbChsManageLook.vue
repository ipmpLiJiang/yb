<template>
  <a-drawer
    title="查看"
    :maskClosable="false"
    width=80%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="lookVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <ybChsData-module
    ref="ybChsDataModule"
    :ybChsData="ybChsManageLook"
    >
    </ybChsData-module>
    <ybChsJk-module
    ref="ybChsJkModule"
    :ybChsData="ybChsManageLook"
    >
    </ybChsJk-module>
    <a-divider v-show="ybChsManageLook.state === 2?true:false">变更信息</a-divider>
    <div v-show="ybChsManageLook.state === 6 || ybChsManageLook.state === 1 || ybChsManageLook.state === 2?true:false">
      <div style="padding-bottom:20px;border: 1px solid #e8e8e8;">
        <br>
        <div v-show="ybChsManageLook.state === 6 || ybChsManageLook.state === 1 ?true:false">
          <a-row type="flex" justify="start">
          <a-col :span=20>
            <!--科室、医生-->
            <a-row>
              <a-col :span=10>
                <a-form-item
                  v-bind="formItemLayout"
                  label="科室名称"
                >
                  {{ ybChsManageLook.readyDksId }}-{{ybChsManageLook.readyDksName}}
                </a-form-item>
              </a-col>
              <a-col :span=14>
                <a-form-item
                  v-bind="formItemLayout"
                  label="医生姓名"
                >
                  {{ybChsManageLook.readyDoctorCode}}-{{ybChsManageLook.readyDoctorName}}
                </a-form-item>
              </a-col>
            </a-row>
            <!--医院意见-->
            <a-row type="flex" justify="start">
              <a-col :span=24>
                  <a-form-item
                  v-bind="{
                    labelCol: { span: 4 },
                    wrapperCol: { span: 19, offset: 1 }
                  }"
                  label="医院意见"
                >
                {{ybChsManageLook.operateReason}}
                </a-form-item>
              </a-col>
            </a-row>
          </a-col>
          </a-row>
          <br>
          <a-row>
            <a-col :span=24>
              <div style="margin:0px 60px">
              <a-row>
                <a-col>
                  <template>
                    <!--上传图片-->
                    <div>
                      <a-upload
                        list-type="picture"
                        :file-list="fileList"
                        disabled="disabled"
                        @preview="handlePreview"
                        class="upload-list-inline"
                      >
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
        </div>
        <!--拒绝-->
        <div v-show="ybChsManageLook.state === 2?true:false">
        <a-row type="flex" justify="start">
        <a-col :span=20>
          <!--科室、医生-->
          <a-row>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="申请科室"
              >
                {{ybChsManageLook.readyDksId}}-{{ybChsManageLook.readyDksName}}
              </a-form-item>
            </a-col>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="申请人"
              >
                {{ybChsManageLook.readyDoctorCode}}-{{ybChsManageLook.readyDoctorName}}
              </a-form-item>
            </a-col>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="更改科室"
              >
                {{ybChsManageLook.changeDksId}}-{{ybChsManageLook.changeDksName}}
              </a-form-item>
            </a-col>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="更改人"
              >
                {{ybChsManageLook.changeDoctorCode}}-{{ybChsManageLook.changeDoctorName}}
              </a-form-item>
            </a-col>
          </a-row>
          <!--医院意见-->
          <a-row type="flex" justify="start">
            <a-col :span=20>
                <a-form-item
                v-bind="{
                  labelCol: { span: 3 },
                  wrapperCol: { span: 19 }
                }"
                label="医院意见"
              >
              {{ybChsManageLook.operateReason}}
              </a-form-item>
            </a-col>
          </a-row>
        </a-col>
        </a-row>
        </div>
        <br>
        <!--按钮-->
        <a-row type="flex" justify="center">
          <a-col :span=3>
            <a-button
              style="margin-right: .8rem"
              @click="onClose"
            >
              取消
            </a-button>
          </a-col>
        </a-row>
      </div>
    </div>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import YbChsDataModule from '../YbChsFunModule/YbChsDataModule'
import YbChsJkModule from '../YbChsFunModule/YbChsJkModule'
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
  name: 'YbChsManageLook',
  components: {
    YbChsDataModule, YbChsJkModule},
  props: {
    lookVisiable: {
      default: false
    }
  },
  data () {
    return {
      formItemLayout,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      ybChsManageLook: {},
      user: this.$store.state.account.user,
      ybChsManage: {}
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    moment,
    onClose () {
      this.previewVisible = false
      this.fileList = []
      this.previewImage = ''
      this.ybChsManage = {}
      this.ybChsManageLook = {}
      this.$emit('close')
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
    setFormValues ({ ...ybChsManageLook }) {
      this.ybChsManageLook = ybChsManageLook
      setTimeout(() => {
        this.$refs.ybChsJkModule.search()
      }, 200)
      if (ybChsManageLook.state === 6 || ybChsManageLook.state === 1) {
        this.previewVisible = false
        this.fileList = []
        this.previewImage = ''
        this.findFileList(ybChsManageLook)
      }
    },
    findFileList (ybChsManageLook) {
      let formData = {}
      formData.id = ybChsManageLook.id
      formData.applyDateStr = ybChsManageLook.applyDateStr
      formData.orderNum = ybChsManageLook.orderNum
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
        if (r.data.data.length > 0) {
          this.showDownLoad = true
        } else {
          this.showDownLoad = false
        }
      }).catch(() => {
        console.log('异常')
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
