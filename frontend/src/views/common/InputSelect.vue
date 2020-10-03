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
    type: {default: 1}
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
      this.dataSource = this.ajax(keyword)
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
      if (this.type === 1) {
        body = [{value: '101A', text: '测试科室1'}, {value: '102A', text: '测试科室2'}, {value: '103A', text: '测试科室3'}]
      } else {
        body = [{value: 'mrbird', text: '系统管理员'}, {value: '102A', text: '测试医生2'}, {value: '103A', text: '测试医生3'}]
      }
      return body.map(user => ({
        text: user.text,
        value: user.value
      }))
    },
    Load (value, data) {
      this.dataSource = data
      this.value = value
    }
  }
}
</script>
