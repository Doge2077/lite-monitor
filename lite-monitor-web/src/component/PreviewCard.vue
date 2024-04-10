<script setup>
import {fitByUnit} from '@/tools'
import {useClipboard} from "@vueuse/core";
import {ElMessage, ElMessageBox} from "element-plus";
import {post} from "@/net";
const props = defineProps({
  data: Object,
  update: Function
})

const { copy } = useClipboard()
const copyId = () => copy(props.data.clientAddress).then(() => ElMessage.success('成功复制到剪切板'))

function rename() {
  ElMessageBox.prompt('请输入新的服务器主机名称', '修改名称', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputValue: props.data.clientName,
    inputPattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]{1,10}$/,
    inputErrorMessage: '名称只能包含中英文字符、数字和下划线',
  }).then(({ value }) => post('/api/monitor/rename',{
    clientId: props.data.clientId,
    clientName: value
  },() => {
      ElMessage.success('主机名称已更新')
      props.update()
    })
  )
}

</script>

<template>
  <div class="instance-card">
    <div style="display: flex; justify-content: space-between">
      <div>
        <div class="name">
          <span :class="`flag-icon flag-icon-${data.location}`"></span>
          <span style="margin: 0 5px"> {{data.clientName }} </span>
          <i class="fa-regular fa-pen-to-square interact-item" @click.stop="rename"></i>
        </div>
        <div class="os">
          操作系统：{{`${data.osName} ${data.osVersion}`}}
        </div>
      </div>
      <div class="status" v-if="data.online">
        <i style="color: #18cb18" class="fa-regular fa-circle-play"></i>
        <span style="margin-left: 5px">运行中</span>
      </div>
      <div class="status" v-else>
        <i style="color: red" class="fa-regular fa-circle-stop"></i>
        <span style="margin-left: 5px">离线</span>
      </div>
    </div>
    <el-divider style="margin: 10px 0; border-color: white"/>
    <div class="network">
      <span style="margin-right: 10px">公网IP：{{ data.clientAddress }}</span>
      <i class="fa-regular fa-copy interact-item" @click.stop="copyId" style="color: dodgerblue"></i>
    </div>
    <div class="cpu">
      <span style="margin-right: 10px">处理器：{{ data.cpuName }}</span>
    </div>
    <div class="hardware">
      <i class="fa-solid fa-microchip"></i>
      <span style="margin: 0 5px"> {{data.cpuCores}} CPU</span>
      <i class="fa-solid fa-memory"></i>
      <span style="margin: 0 5px"> {{ data.osMemory.toFixed(1)}} GB</span>
    </div>
    <div class="progress">
      <span style="margin-right: 10px;">CPU: {{ `${((1 - data.cpuUsage) * 100).toFixed(1)}`}} %</span>
      <el-progress status="success" :percentage="data.cpuUsage * 100" :stroke-width="5" :show-text="false"/>
    </div>
    <div class="progress">
      <span style="margin-right: 10px;">内存: {{ `${data.memoryUsage.toFixed(1)}`}} GB({{`${(data.memoryUsage / data.osMemory * 100).toFixed(1)}`}}%)</span>
      <el-progress status="success" :percentage="data.memoryUsage / data.osMemory * 100" :stroke-width="5" :show-text="false"/>
    </div>
    <div class="network-flow">
      <div>
        <span style="margin-right: 10px;">网络流量</span>
      </div>
      <div>
        <i class="fa-solid fa-upload"></i>
        <span style="margin: 0 5px"> {{ `${fitByUnit(data.networkUpload, 'KB')}/s` }} </span>
        <el-divider direction="vertical"/>
        <i class="fa-solid fa-download"></i>
        <span style="margin: 0 5px">{{ `${fitByUnit(data.networkDownload, 'KB')}/s` }}</span>
      </div>
    </div>
  </div>
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

.instance-card{
  width: 320px;
  padding: 15px;
  border-radius: 15px;
  box-sizing: border-box;
  color: #6b6b6b;
  background: #e0e0e0;
  box-shadow:  9px 9px 30px #bebebe,
  -9px -9px 30px #ffffff;

  .name{
    font-size: 15px;
    font-weight: bold;
  }

  .status {
    font-size: 14px;
  }

  .os{
    font-size: 13px;
    color: gray;
  }

  .network{
    font-size: 13px;
  }

  .cpu{
    font-size: 13px;
  }

  .hardware{
    margin-top: 10px;
    font-size: 13px;
  }

  .progress{
    margin-top: 10px;
    font-size: 12px;
  }

  .network-flow{
    margin-top: 10px;
    font-size: 12px;
    display: flex;
    justify-content: space-between;
  }

}
</style>