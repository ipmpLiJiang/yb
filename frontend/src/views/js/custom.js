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
  },
  excelContentSaveBlob (content, fileName) {
    const blob = new Blob([content])
    if ('download' in document.createElement('a')) {
      const elink = document.createElement('a')
      elink.download = fileName
      elink.style.display = 'none'
      elink.href = URL.createObjectURL(blob)
      document.body.appendChild(elink)
      elink.click()
      URL.revokeObjectURL(elink.href)
      document.body.removeChild(elink)
    } else {
      navigator.msSaveBlob(blob, fileName)
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
const fy = {
  getFyName (fyid) {
    // 1001 本院  1002 西院 1003 金银湖
    if (fyid === '1001') {
      return '本院'
    } else if (fyid === '1002') {
      return '西院'
    } else if (fyid === '1003') {
      return '金银湖'
    } else {
      return ''
    }
  },
  getDksFyName (dksName, fyid) {
    if (dksName) {
      return dksName + '(' + this.getFyName(fyid) + ')'
    } else {
      return dksName
    }
  }
}
export { custom, data, fy }
