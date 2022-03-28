<template>
    <div style="margin:0px 10px 15px 15px">
      <a-divider>DRG管理系统</a-divider>
      <a-row v-if="isData">
        <a-row class="tbMarginBottom">
          <a-col :span=3>
            入&nbsp;&nbsp;院&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;期：
          </a-col>
          <a-col :span=8>
            {{ybDrgJkModule.ryDate}}
          </a-col>
          <a-col :span=1>
            &nbsp;
          </a-col>
          <a-col :span=3>
            出&nbsp;&nbsp;院&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;期：
          </a-col>
          <a-col :span=9>
            {{ybDrgJkModule.cyDate}}
          </a-col>
        </a-row>
        <a-row class="tbMarginBottom">
          <a-col :span=3>
              DRG分组编码：
          </a-col>
          <a-col :span=8>
            {{ybDrgJkModule.fzCode}}
          </a-col>
          <a-col :span=1>
            &nbsp;
          </a-col>
          <a-col :span=3>
            DRG分组名称：
          </a-col>
          <a-col :span=9>
            {{ybDrgJkModule.fzName}}
          </a-col>
        </a-row>
        <a-row class="tbMarginBottom">
          <a-col :span=3>
            医保主要诊断编码：
          </a-col>
          <a-col :span=8>
            {{ybDrgJkModule.zyzdCode}}
          </a-col>
          <a-col :span=1>
            &nbsp;
          </a-col>
          <a-col :span=3>
            医保主要诊断名称：
          </a-col>
          <a-col :span=9>
            {{ybDrgJkModule.zyzdName}}
          </a-col>
        </a-row>
        <a-row class="tbMarginBottom">
          <a-col :span=3>
            医保主手术编码：
          </a-col>
          <a-col :span=8>
            {{ybDrgJkModule.zssCode}}
          </a-col>
          <a-col :span=1>
            &nbsp;
          </a-col>
          <a-col :span=3>
            医保主手术名称：
          </a-col>
          <a-col :span=9>
            {{ybDrgJkModule.zssName}}
          </a-col>
        </a-row>
        <a-row class="tbMarginBottom">
          <a-col :span=3>
            其他诊断编码：
          </a-col>
          <a-col :span=8>
            {{ybDrgJkModule.qtzdCode}}
          </a-col>
          <a-col :span=1>
            &nbsp;
          </a-col>
          <a-col :span=3>
            其他诊断名称：
          </a-col>
          <a-col :span=9>
            {{ybDrgJkModule.qtzdName}}
          </a-col>
        </a-row>
        <a-row class="tbMarginBottom">
          <a-col :span=3>
            其他手术编码：
          </a-col>
          <a-col :span=8>
            {{ybDrgJkModule.qtssCode}}
          </a-col>
          <a-col :span=1>
            &nbsp;
          </a-col>
          <a-col :span=3>
              其他手术名称：
          </a-col>
          <a-col :span=9>
              {{ybDrgJkModule.qtssName}}
          </a-col>
        </a-row>
        <a-row class="tbMarginBottom">
          <a-col :span=2>
            科&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;室：
          </a-col>
          <a-col :span=4>
            {{ybDrgJkModule.deptName}}
          </a-col>
          <a-col :span=2>
            病&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;区：
          </a-col>
          <a-col :span=4>
            {{ybDrgJkModule.areaName}}
          </a-col>
          <a-col :span=2>
            权&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;重：
          </a-col>
          <a-col :span=4>
            {{ybDrgJkModule.qz}}
          </a-col>
          <a-col :span=2>
            统筹支付：
          </a-col>
          <a-col :span=4>
            {{ybDrgJkModule.tczf}}
          </a-col>
        </a-row>
        <a-row class="tbMarginBottom">
          <a-col :span=2>
            科&nbsp;&nbsp;主&nbsp;&nbsp;任：
          </a-col>
          <a-col :span=4>
            {{ybDrgJkModule.kzrDocId}}-{{ybDrgJkModule.kzrDocName}}
          </a-col>
          <a-col :span=2>
            主任医师：
          </a-col>
          <a-col :span=4>
            {{ybDrgJkModule.zrysDocId}}-{{ybDrgJkModule.zrysDocName}}
          </a-col>
          <a-col :span=2>
            主治医师：
          </a-col>
          <a-col :span=4>
            {{ybDrgJkModule.zzysDocId}}-{{ybDrgJkModule.zzysDocName}}
          </a-col>
          <a-col :span=2>
            住院医师：
          </a-col>
          <a-col :span=4>
            {{ybDrgJkModule.zyysDocId}}-{{ybDrgJkModule.zyysDocName}}
          </a-col>
        </a-row>
        <a-row class="tbMarginBottom">
          <a-col :span=2>
            医疗组科室：
          </a-col>
          <a-col :span=4>
            {{ybDrgJkModule.ylzDeptName}}
          </a-col>
          <a-col :span=2>
            医疗组医师：
          </a-col>
          <a-col :span=4>
            {{ybDrgJkModule.ylzDocId}}-{{ybDrgJkModule.ylzDocName}}
          </a-col>
          <a-col :span=2>
            院区：
          </a-col>
          <a-col :span=4>
            {{ybDrgJkModule.yq}}
          </a-col>
        </a-row>
      </a-row>
      <a-list :data-source="[]" v-else />
    </div>
</template>
<script>
import moment from 'moment'
export default {
  name: 'YbDrgJkModule',
  props: {
    ybDrgData: {}
  },
  data () {
    return {
      loading: false,
      isData: false,
      ybDrgJkModule: {
      }
    }
  },

  mounted () {
  },
  methods: {
    moment,
    search () {
      let params = { applyDataId: this.ybDrgData.applyDataId }
      this.$get('ybDrgJk/getJkData', {
        ...params
      }).then((r) => {
        if (r.data) {
          this.ybDrgJkModule = r.data
          this.isData = true
        } else {
          this.isData = false
          this.ybDrgJkModule = {}
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
.tbMarginBottom {
  margin-bottom:12px
}
</style>
