<template>
  <a-drawer
    title="查看"
    :maskClosable="false"
    width=82%
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
                  label="汇总科室"
                >
                  {{ fy.getDksFyName(ybChsManageLook.readyDksName, ybChsManageLook.readyFyid) }}
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
            <!--复议理由-->
            <a-row type="flex" justify="start">
              <a-col :span=24>
                  <a-form-item
                  v-bind="{
                    labelCol: { span: 4 },
                    wrapperCol: { span: 19, offset: 1 }
                  }"
                  label="复议理由"
                >
                {{ybChsManageLook.operateReason}}
                </a-form-item>
              </a-col>
            </a-row>
            <br>
            <a-row type="flex" justify="start">
              <a-col :span=24>
                <a-form-item
                  v-bind="{
                    labelCol: { span: 4 },
                    wrapperCol: { span: 19, offset: 1 }
                  }"
                  label="复议附件"
                >
                  <a-upload
                    :file-list="fileList"
                    disabled="disabled"
                  >
                </a-upload>
              </a-form-item>
              </a-col>
            </a-row>
          </a-col>
          </a-row>
        </div>
        <!--拒绝-->
        <div v-show="ybChsManageLook.state === 2?true:false">
        <a-row type="flex" justify="start">
        <a-col :span=22>
          <!--科室、医生-->
          <a-row>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="申请科室"
              >
                {{ fy.getDksFyName(ybChsManageLook.readyDksName, ybChsManageLook.readyFyid) }}
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
                {{ fy.getDksFyName(ybChsManageLook.changeDksName, ybChsManageLook.changeFyid) }}
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
          <!--复议理由-->
          <a-row type="flex" justify="start">
            <a-col :span=20>
                <a-form-item
                v-bind="{
                  labelCol: { span: 3 },
                  wrapperCol: { span: 19 }
                }"
                label="复议理由"
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
import { fy } from '../../js/custom'
const formItemLayout = {
  labelCol: { span: 10 },
  wrapperCol: { span: 13, offset: 1 }
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
      ybChsManageLook: {},
      fy,
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
      this.fileList = []
      this.ybChsManage = {}
      this.ybChsManageLook = {}
      this.$emit('close')
    },
    setFormValues ({ ...ybChsManageLook }) {
      this.ybChsManageLook = ybChsManageLook
      setTimeout(() => {
        this.$refs.ybChsJkModule.search()
      }, 200)
      if (ybChsManageLook.state === 6 || ybChsManageLook.state === 1) {
        this.fileList = []
        this.findFileList(ybChsManageLook)
      }
    },
    findFileList (ybChsManageLook) {
      let formData = {}
      formData.id = ybChsManageLook.id
      formData.applyDateStr = ybChsManageLook.applyDateStr
      formData.areaType = this.user.areaType.value
      this.$post('comFile/uploadFileList', {
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
