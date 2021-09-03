<template>
  <a-drawer
    title="查看"
    :maskClosable="false"
    width=70%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="lookVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <appealData-module
    ref="appealDataModule"
    :ybAppealDataModule="ybAppealManageLook"
    >
    </appealData-module>
    <inpatientfees-module
    ref="inpatientfeesModule"
    :inpatientfeesModule="ybAppealManageLook"
    >
    </inpatientfees-module>
    <div v-show="ybAppealManageLook.acceptState === 6 || ybAppealManageLook.acceptState === 1 || ybAppealManageLook.acceptState === 2?true:false">
      <br>
      <div style="padding-top:20px;padding-bottom:20px;border: 1px solid #e8e8e8;">
        <div v-show="ybAppealManageLook.acceptState === 6 || ybAppealManageLook.acceptState === 1 ?true:false">
          <a-row type="flex" justify="start">
          <a-col :span=20>
            <!--科室、医生-->
            <a-row>
              <a-col :span=10>
                <a-form-item
                  v-bind="formItemLayout"
                  label="科室名称"
                >
                  {{ybAppealManageLook.readyDeptName}}
                </a-form-item>
              </a-col>
              <a-col :span=14>
                <a-form-item
                  v-bind="formItemLayout"
                  label="医生姓名"
                >
                  {{ybAppealManageLook.readyDoctorName}}
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
                {{ybAppealManageLook.operateReason}}
                </a-form-item>
              </a-col>
            </a-row>
          </a-col>
          </a-row>
          <br>
          <a-row>
            <a-col :span=24>
              <div style="margin:25px">
              <a-row type="flex" justify="center">
                <a-col>
                  <template>
                    <!--上传图片-->
                    <div>
                      <a-upload
                        list-type="picture-card"
                        :file-list="fileList"
                        disabled="disabled"
                        @preview="handlePreview"
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
        <div v-show="ybAppealManageLook.acceptState === 2?true:false">
        <a-row type="flex" justify="start">
        <a-col :span=20>
          <!--科室、医生-->
          <a-row>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="申请科室"
              >
                {{ybAppealManageLook.readyDeptName}}
              </a-form-item>
            </a-col>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="申请人"
              >
                {{ybAppealManageLook.readyDoctorName}}
              </a-form-item>
            </a-col>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="更改科室"
              >
                {{ybAppealManageLook.changeDeptName}}
              </a-form-item>
            </a-col>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="更改人"
              >
                {{ybAppealManageLook.changeDoctorName}}
              </a-form-item>
            </a-col>
          </a-row>
          <!--申请理由-->
          <a-row type="flex" justify="start">
            <a-col :span=20>
                <a-form-item
                v-bind="{
                  labelCol: { span: 3 },
                  wrapperCol: { span: 19 }
                }"
                label="申请理由"
              >
              {{ybAppealManageLook.operateReason}}
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
import AppealDataModule from '../ybFunModule/AppealDataModule'
import InpatientfeesModule from '../ybFunModule/InpatientfeesModule'
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
  name: 'YbAppealManageLook',
  components: {
    AppealDataModule, InpatientfeesModule},
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
      ybAppealManageLook: {},
      user: this.$store.state.account.user,
      ybAppealManage: {}
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
      this.ybAppealManage = {}
      this.ybAppealManageLook = {}
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
    setFormValues ({ ...ybAppealManageLook }) {
      this.ybAppealManageLook = ybAppealManageLook
      setTimeout(() => {
        this.$refs.inpatientfeesModule.search()
      }, 200)
      if (ybAppealManageLook.acceptState === 6 || ybAppealManageLook.acceptState === 1) {
        this.previewVisible = false
        this.fileList = []
        this.previewImage = ''
        this.findFileList(ybAppealManageLook)
      }
    },
    findFileList (ybAppealManageLook) {
      let formData = {}
      formData.id = ybAppealManageLook.id
      formData.deptId = ybAppealManageLook.readyDeptCode
      formData.applyDateStr = ybAppealManageLook.applyDateStr
      formData.sourceType = ybAppealManageLook.sourceType
      formData.areaType = this.user.areaType.value
      this.$post('comFile/listImgComFile', {
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
</style>
