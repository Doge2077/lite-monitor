<script setup>
import {computed, reactive, watch} from "vue";
import {get} from "@/net";
import RuntimeHistory from "@/component/RuntimeHistory.vue";
import aiFenxiA from '@/assets/img/manage-background.jpg'

const locations = [
  {name: 'cn', desc: '中国大陆'},
  {name: 'hk', desc: '中国香港'},
  {name: 'tw', desc: '中国台湾'},
  {name: 'jp', desc: '日本'},
  {name: 'us', desc: '美国'},
  {name: 'sg', desc: '新加坡'},
  {name: 'de', desc: '德国'},
  {name: 'kr', desc: '韩国'}
]

const props = defineProps({
  clientId: Number,
  update: Function
})

const emits = defineEmits(['delete', 'terminal'])

const details = reactive({
  base: {},
  runtime: {
    list: []
  },
  editNode: false
})

const nodeEdit = reactive({
  name: '',
  location: ''
})

const enableNodeEdit = () => {
  details.editNode = true
  nodeEdit.name = details.base.node
  nodeEdit.location = details.base.location
}


function updateDetails() {
  props.update()
  init(props.clientId)
}

const init = clientId =>{
    if (clientId !== -1) {
      details.base = {}
      details.runtime = { list: [] }
      get(`/monitor/details?clientId=${clientId}`, data => Object.assign(details.base, data))
      get(`/monitor/runtime-history?clientId=${clientId}`, data => Object.assign(details.runtime, data))
    }
}

setInterval(() => {
  if (props.clientId !== -1 && details.runtime) {
    get(`/monitor/runtime-now?clientId=${props.clientId}`, data => {
      if (details.runtime.list.length >= 3600) {
        details.runtime.list.splice(0, 1)
      }
      details.runtime.list.push(data)
    })
  }
}, 100000000)

const now = computed(() => details.runtime.list[details.runtime.list.length - 1])

watch(() => props.clientId, init, {immediate: true})

</script>

<template>
  <el-scrollbar>
    <div class="client-details" v-loading="Object.keys(details.base).length === 0">
      <div v-if="Object.keys(details.base).length">
        <div class="title">
          <i class="fa-solid fa-clock-rotate-left"></i>
          历史监控信息
        </div>
        <el-divider style="margin: 10px 0"/>
        <div v-if="details.base.online" v-loading="!details.runtime.list.length"
             style="min-height: 400px">
          <runtime-history :data="details.runtime.list"/>
        </div>
        <el-empty description="服务器已离线" v-else/>
      </div>
      <div v-if="Object.keys(details.base).length">
        <div class="title" style="margin-top: 20px">
          <i class="fa-solid fa-robot"></i>
          AI 恶意攻击预测
        </div>
        <el-divider style="margin: 10px 0"/>
        <div v-if="details.base.online" v-loading="!details.runtime.list.length"
             style="min-height: 400px">
          <div style="display: flex; justify-content: space-between">
            <el-image style="margin: 10px" :src="aiFenxiA"/>
            <el-image style="margin: 10px" :src="aiFenxiA"/>
          </div>
        </div>
        <el-empty description="服务器已离线" v-else/>
      </div>
    </div>

  </el-scrollbar>
</template>

<style scoped>

.interact-item{
  transition: .3s;
  &:hover{
    cursor: pointer;
    scale: 1.1;
    opacity: 0.8;
  }
}

.client-details{
  height: 100%;
  padding: 20px;
  background: linear-gradient(145deg, #cacaca, #f0f0f0);
  box-shadow:  20px 20px 60px #bebebe,
  -20px -20px 60px #ffffff;

  .title{
    color: dodgerblue;
    font-size: 18px;
    font-weight: bold;
  }

  .details-list{
    font-size: 14px;

    & div{
      margin-bottom: 10px;

      & span:first-child{
        color: gray;
        font-size: 13px;
        font-weight: normal;
        width: 120px;
        display: inline-block;
      }

      & span{
        font-weight: bold;
      }
    }
  }

}
</style>