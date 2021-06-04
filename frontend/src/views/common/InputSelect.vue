<template>
  <a-select
    show-search
    :value="value"
    placeholder="请输入关键词"
    style="width: 100%"
    :default-active-first-option="false"
    :filter-option="false"
    :not-found-content="null"
    @search="handleSearch"
    @change="e => handleChange(e)"
  >
    <a-icon
      slot="suffixIcon"
      type="search"
    ></a-icon>
    <a-select-option
      v-for="d in dataSource"
      :key="d.value"
    >
      {{ d.text }}
    </a-select-option>
  </a-select>
</template>
<script>
export default {
  props: {
    type: {
      default: 1
    },
    dept: {
      default: ''
    }
  },
  data () {
    return {
      value: '',
      dataSource: []
    }
  },
  methods: {
    // 输入框事件
    handleSearch (keyword) {
      // 模拟往服务器发送请求
      if (keyword !== '') {
        this.dataSource = this.ajax(keyword)
      }
    },
    handleChange (value) {
      this.value = value
      this.selectChange(value)
    },
    selectChange (value) {
      const selectItem = this.dataSource.filter(item => value === item.value)[0]
      this.$emit('selectChange', selectItem)
    },
    // 模拟往服务器发送请求
    ajax (keyword) {
      let body = []
      let params = {comments: keyword}
      if (this.type === 1) {
        this.$get('ybDept/findDeptList', {
          ...params
        }).then((r) => {
          r.data.data.forEach((item, i) => {
            body.push({
              value: item.deptId,
              text: item.deptId + '-' + item.deptName
            })
          })
        })
        // body = [{value: '101A', text: '测试科室1'}, {value: '102A', text: '测试科室2'}, {value: '103A', text: '测试科室3'}]
      } else {
        params = {comments: keyword}
        if (this.dept !== '' && this.dept !== null) {
          params.deptName = this.dept
        }
        this.$get('ybPerson/findPersonList', {
          ...params
        }).then((r) => {
          r.data.data.forEach((item, i) => {
            body.push({
              value: item.personCode,
              text: item.personCode + '-' + item.personName
            })
          })
        })
        // body = [{value: 'mrbird', text: '系统管理员'}, {value: '102A', text: '测试医生2'}, {value: '103A', text: '测试医生3'}]
      }
      return body
    },
    Load (value, data) {
      this.dataSource = data
      this.value = value
    }
  }
}
</script>
