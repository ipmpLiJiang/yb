const custom = {
  resetApplyDateStr (applyDateStr, applyDateStrTo, defaultFormatDate) {
    if (applyDateStr !== '' && applyDateStr !== null && applyDateStr !== undefined) {
      if (applyDateStrTo === '' || applyDateStrTo === null || applyDateStrTo === undefined) {
        applyDateStrTo = applyDateStr
      }
    }
    if (applyDateStrTo !== '' && applyDateStrTo !== null && applyDateStrTo !== undefined) {
      if (applyDateStr === '' || applyDateStr === null || applyDateStr === undefined) {
        applyDateStr = applyDateStrTo
      }
    }
    if ((applyDateStrTo === '' || applyDateStrTo === null || applyDateStrTo === undefined) && (applyDateStr === '' || applyDateStr === null || applyDateStr === undefined)) {
      applyDateStrTo = defaultFormatDate
      applyDateStr = defaultFormatDate
    }
    return [applyDateStr, applyDateStrTo]
  },
  checkApplyDateStr (applyDateStr, applyDateStrTo, maxMonth) {
    let ads1 = applyDateStr.split('-')
    let ads2 = applyDateStrTo.split('-')

    let nian1 = parseInt(ads1[0])
    let yue1 = parseInt(ads1[1])

    let nian = parseInt(ads2[0])
    let yue = parseInt(ads2[1])
    let y = 0
    let b = false

    if (nian >= nian1) {
      if (nian === nian1) {
        if (yue >= yue1) {
          if (yue === yue1) {
            y = 0
          } else {
            y = yue - yue1
          }
        } else {
          b = true
        }
      } else {
        y = (nian - nian1) * 12 + yue - yue1
      }
    } else {
      b = true
    }
    let msg = '复议年月选择区间有误，不能大于' + maxMonth + '个月，请重新选择.'
    if (b === true) {
      return msg
    } else {
      y = y + 1
      if (y > maxMonth) {
        return msg
      } else {
        return ''
      }
    }
  }
}
const data = {
  seachDataTypeSource: [
    {text: '全部', value: 2},
    {text: '明细扣款', value: 0},
    {text: '主单扣款', value: 1}
  ]

}
export { custom, data }
