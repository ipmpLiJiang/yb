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
    :ybChsData="ybChsResultLook"
    >
    </ybChsData-module>
    <ybChsJk-module
    ref="ybChsJkModule"
    :ybChsData="ybChsResultLook"
    >
    </ybChsJk-module>
    <a-divider>复议信息</a-divider>
    <div>
      <div style="padding-bottom:20px;border: 1px solid #e8e8e8;">
        <a-row type="flex" justify="start">
          <a-col :span=20>
            <!--科室、医生-->
            <a-row>
              <a-col :span=10>
                <a-form-item
                  v-bind="formItemLayout"
                  label="汇总科室"
                >
                  {{ fy.getDksFyName(ybChsResultLook.resultDksName, ybChsResultLook.fyid) }}
                </a-form-item>
              </a-col>
              <a-col :span=14>
                <a-form-item
                  v-bind="formItemLayout"
                  label="医生姓名"
                >
                  {{ybChsResultLook.resultDoctorCode}}-{{ybChsResultLook.resultDoctorName}}
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
                {{ybChsResultLook.operateReason}}
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
      ybChsResultLook: {},
      fy,
      user: this.$store.state.account.user,
      showDownLoad: true
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
      this.ybChsResultLook = {}
      this.$emit('close')
    },
    setFormValues ({ ...ybChsResultLook }) {
      this.ybChsResultLook = ybChsResultLook
      setTimeout(() => {
        this.$refs.ybChsJkModule.search()
      }, 200)
      this.fileList = []
      this.findFileList(ybChsResultLook)
    },
    findFileList (ybChsResultLook) {
      let formData = {}
      formData.id = ybChsResultLook.id
      formData.applyDateStr = ybChsResultLook.applyDateStr
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
