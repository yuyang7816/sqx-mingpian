/**
 * 默认的城市数据格式如下
 */
export interface CityDataType {
  /** 省/直辖市行政编号 */
  id: string;
  /** 省/直辖市简称 */
  name: string;
  /** 省/直辖市拼音 */
  pinyin: string;
  /** 省/直辖市全称 */
  ext_name: string;
  /** 省/直辖市下级城市 */
  children: {
    /** 市行政编号 */
    id: string;
    /** 市简称 */
    name: string;
    /** 市拼音 */
    pinyin: string;
    /** 市全称 */
    ext_name: string;
  }[];
}

/**
 * 深拷贝内容
 * @param originData 拷贝对象
 * @author crlang(https://crlang.com)
 */
export function deepClone(originData) {
  const type = Object.prototype.toString.call(originData)
  let data
  if (type === '[object Array]') {
    data = []
    for (let i = 0; i < originData.length; i++) {
      data.push(deepClone(originData[i]))
    }
  } else if (type === '[object Object]') {
    data = {}
    for (const prop in originData) {
      // eslint-disable-next-line no-prototype-builtins
      if (originData.hasOwnProperty(prop)) { // 非继承属性
        data[prop] = deepClone(originData[prop])
      }
    }
  } else {
    data = originData
  }
  return data
}

/**
 * 初始化为合适的城市数据
 * @param CityDataType cityData
 * @author crlang(https://crlang.com)
 */
export function createCityData(cityData) {
  const newCityData = []
  if (!cityData || cityData.length === 0) {
    return newCityData
  }
  for (let i = 0; i < cityData.length; i++) {
    const pro = cityData[i]
    if (pro.children?.length) {
      for (let j = 0; j < pro.children.length; j++) {
        const cit = pro.children[j]
        const cit_first = cit.pinyin[0].toLocaleUpperCase()
        if (newCityData.length) {
          const ncd_idx = newCityData.findIndex(k => k.first_char === cit_first)
          const ncd_item = newCityData[ncd_idx]
          if (ncd_item) {
            if (!ncd_item.children) ncd_item.children = []
            ncd_item.children.push(creatCityItem(cit))
          } else {
            newCityData.push(creatCityItem(cit, cit_first))
          }
        } else {
          newCityData.push(creatCityItem(cit, cit_first))
        }
      }
    }
  }

  return sortCityData(newCityData)
}
/**
 * 排序字母及城市拼音
 * @param cityData
 * @author crlang(https://crlang.com)
 */
export function sortCityData(cityData) {
  cityData.sort(function(a, b) {
    if (a.first_char < b.first_char) {
      return -1
    }
    if (a.first_char > b.first_char) {
      return 1
    }
    return 0
  })

  cityData.forEach(k => {
    k.children.sort(function(a, b) {
      const ap = a.pinyin.split(' ')
      const bp = b.pinyin.split(' ')
      if (ap[1] && bp[1]) {
        if (ap[1][0] < bp[1][0]) {
          return -1
        }
        if (ap[1][0] > bp[1][0]) {
          return 1
        }
      }

      if (ap[2] && bp[2]) {
        if (ap[2][0] < bp[2][0]) {
          return -1
        }
        if (ap[2][0] > bp[2][0]) {
          return 1
        }
      }

      return 0
    })
  })

  return cityData
}

/**
 * 创建城市数据项
 * @param cit
 * @param first_char
 * @author crlang(https://crlang.com)
 */
export function creatCityItem(cit, first_char) {
  const basicItem = {
    id: cit.id,
    name: cit.ext_name,
    sname: cit.name,
    pinyin: cit.pinyin,
  }

  if (first_char) {
    const first_char_key = first_char.toLocaleLowerCase()

    return {
      first_char,
      first_char_key,
      children: [basicItem],
    }
  }

  return basicItem
}

/**
 * 创建索引数据
 * @param cityData
 * @author crlang(https://crlang.com)
 */
export function createCityABC(cityData) {
  const abcData = []
  if (!cityData || cityData.length === 0) {
    return abcData
  }
  for (let i = 0; i < cityData.length; i++) {
    const item = cityData[i]
    const size = (item.children?.length || 0) + 1
    abcData.push({
      key: item.first_char,
      isActived: i === 0,
      size,
    })
  }
  return abcData
}

/**
 * 显示弹窗
 * @param content
 * @author crlang(https://crlang.com)
 */
export function showModal(content) {
  uni.showModal({
    title: '温馨提示',
    content,
    showCancel: false,
  })
}

/**
 * 通过经纬度获取当前城市
 * @param latitude  维度
 * @param longitude 经度
 * @author crlang(https://crlang.com)
 */
export function getCityInfoByLatLng(latitude, longitude, mapKey) {
  return new Promise((resolve, reject) => {
    uni.request({
    // 必需
      url: 'https://apis.map.qq.com/ws/geocoder/v1/',
      data: {
        key: mapKey,
        location: `${latitude},${longitude}`,
        get_poi: '1',
      },
      header: {
        'Content-Type': 'application/json',
      },
      success: (res) => {
        const ad_info = res?.data?.result?.ad_info || null
        if (ad_info) {
          const cityId = (ad_info.adcode || '').substring(0, 4)
          return resolve({
            cityId: `${cityId}00`,
            cityName: ad_info.city,
          })
        }

        return reject(null)
      },
      fail: (err) => {
        console.error('DaCitySelector: getLocation fail', err)
        return reject(null)
      },
    })
  })
}

/**
 * 获取位置信息
 * @param mapKey
 */
export function getLocalionInfo(mapKey) {
  return new Promise((resolve, reject) => {
    uni.getLocation({
      type: 'gcj02',
      success(res) {
        getCityInfoByLatLng(res.latitude, res.longitude, mapKey).then(res => {
          return resolve(res)
        })
          .catch(err => {
            return reject(err)
          })
      },
      fail(err) {
        console.error('DaCitySelector: getLocation gcj02 fail', err)
        uni.getLocation({
          type: 'wgs84',
          success(res) {
            getCityInfoByLatLng(res.latitude, res.longitude, mapKey).then(res => {
              return resolve(res)
            })
              .catch(err => {
                return reject(err)
              })
          },
          fail(err) {
            console.error('DaCitySelector: getLocation wgs84 fail', err)
            // showModal('获取位置信息失败')
            return reject(err)
          },
          // complete() {
          // },
        })
      },
      // complete() {
      // },
    })
  })
}
