# da-city-selector

一个基于 Vue3 的城市选择弹窗组件，支持 App、小程序定位、单选、多选、筛选、主题换色。

组件一直在更新，遇到问题可在下方讨论。

### 功能概述

> 1. 支持单选
> 2. 支持多选
> 3. 支持主题换色
> 4. 支持字母索引
> 5. 支持拼音筛选
> 6. 支持拼音缩写筛选
> 7. 支持名称筛选
> 8. 支持 App、小程序定位到当前城市
> 9. 可直接使用自带城市数据或自定义业务城市数据

### 关于使用

可在右侧的`使用 HBuilderX 导入插件`或`下载示例项目ZIP`，方便快速上手。

可通过下方的示例及文档说明，进一步了解使用组件相关细节参数。

插件地址：https://ext.dcloud.net.cn/plugin?id=13596

### 组件示例

```jsx
<template>
  <DaCitySelector
    :visible="citySelectorVisible"
    themeColor="#dd524d"
    :multiple="true"
    :multipleLimit="5"
    :cityValue="citySelectedValue"
    @confirm="handleConfirm"
    @close="handleClose"></DaCitySelector>
  <button @click="onShowCitySelector()">选择城市</button>
  <view>{{ cityNames }}</view>
</template>
```

```js
import { defineComponent, ref } from 'vue'

import DaCitySelector from '@/components/da-city-selector/index.vue'
export default defineComponent({
  components: { DaCitySelector },
  setup() {
    const citySelectedValue = ref(null)

    const cityItems = ref([])
    const cityNames = ref('')

    const citySelectorVisible = ref(false)

    function onShowCitySelector() {
      // 加载已选内容
      citySelectedValue.value = cityItems.value?.map((k) => k.id)

      citySelectorVisible.value = true
    }

    function handleConfirm(val) {
      console.log('handleConfirm ==>', val)

      cityItems.value = val || []
      cityNames.value = cityItems.value.map((k) => k.name).join('、')

      citySelectorVisible.value = false
    }
    function handleClose() {
      console.log('handleClose ==>')
      citySelectorVisible.value = false
    }

    return {
      citySelectorVisible,
      citySelectedValue,

      cityItems,
      cityNames,

      onShowCitySelector,
      handleConfirm,
      handleClose,
    }
  },
})
```

### 组件参数

| 属性          | 类型                        | 默认值                | 必填 | 说明                                                         |
| :------------ | :-------------------------- | :-------------------- | :--- | :----------------------------------------------------------- |
| visible       | `Boolean`                   | `false`               | 是   | 是否显示弹窗                                                 |
| cityData      | `Array`                     | -                     | 否   | 城市数据，如果为空则使用组件自带的城市数据；数据格式参考下方 |
| cityValue     | `Array`\|`String`\|`Number` | -                     | 否   | 默认已选数据                                                 |
| multiple      | `Boolean`                   | `false`               | 否   | 是否多选                                                     |
| multipleLimit | `Number`                    | `0`                   | 否   | 多选个数限制，0 为不限制                                     |
| themeColor    | `String`                    | `#007aff`             | 否   | 主题色                                                       |
| placeholder   | `String`                    | `请输入城市名称/拼音` | 否   | 搜索提示文本                                                 |
| showLocate    | `Boolean`                   | `true`                | 否   | 是否显示定位城市，不支持 H5                                  |
| mapKey        | `String`                    | -                     | 否   | 腾讯地图 key，用来转换经纬度为城市信息                       |

**`cityData`数据格式为：**

```js
[
  {
    first_char: '字母',
    [
      {id: '城市Code',name: '城市名称',sname: '城市缩写',pinyin: '城市名称拼音'}
    ]
  }
]
```

`mapKey` 申请地址：https://lbs.qq.com/dev/console/key/add

### 组件事件

| 事件名称 | 回调参数           | 说明           |
| :------- | :----------------- | :------------- |
| confirm  | `(values) => void` | 确定选择时回调 |
| close    | `() => void`       | 返回时回调     |

### 组件版本

v1.0.1

### 差异化

已通过测试

> - H5 页面
> - 微信小程序
> - 支付宝、钉钉小程序
> - 字节跳动、抖音、今日头条小程序
> - 百度小程序
> - 飞书小程序
> - QQ 小程序
> - 京东小程序

未测试

> - 快手小程序由于非企业用户暂无演示
> - 快应用、360 小程序因 Vue3 支持的原因暂无演示
