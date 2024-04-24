<script setup>
import {copyId, fitByUnit, percentageToStatus, rename} from '@/tools'

const props = defineProps({
  data: Object,
  update: Function
})

</script>

<template>
  <div class="instance-card">
    <div style="display: flex; justify-content: space-between">
      <div>
        <div class="name">
          <span :class="`flag-icon flag-icon-${data.location}`"></span>
          <span style="margin: 0 5px"> {{data.clientName }} </span>
          <i class="fa-regular fa-pen-to-square interact-item" @click.stop="rename(props.data.clientId, props.data.clientName, update)"></i>
        </div>
        <div class="os">
          操作系统：{{`${data.osName} ${data.osVersion}`}}
        </div>
      </div>
      <div class="status" v-if="data.online">
        <div class="status" v-if="data.cpuUsage > 0.5">
          <i style="color: orange" class="fa-solid fa-heart-circle-exclamation"></i>
          <span style="margin-left: 5px">存在风险</span>
        </div>
        <div class="status" v-else>
          <i style="color: #18cb18" class="fa-solid fa-heart-circle-check"></i>
          <span style="margin-left: 5px">状态健康</span>
        </div>
      </div>
      <div class="status" v-else="data.online">
        <i style="color: red" class="fa-solid fa-heart-circle-minus"></i>
        <span style="margin-left: 5px">暂停监测</span>
      </div>
    </div>
    <el-divider style="margin: 10px 0; border-color: white"/>
    <div class="network">
      <span style="margin-right: 10px">公网IP：{{ data.clientAddress }}</span>
      <i class="fa-regular fa-copy interact-item" @click.stop="copyId(props)" style="color: dodgerblue"></i>
    </div>
    <div class="cpu">
      <span style="margin-right: 10px">处理器：{{ data.cpuName }}</span>
    </div>
    <div class="hardware">
      <i class="fa-solid fa-microchip"></i>
      <span style="margin: 0 5px"> {{data.cpuCores}} CPU</span>
      <i class="fa-solid fa-memory"></i>
      <span style="margin: 0 5px"> {{ data.osMemory?.toFixed(1) ?? '未知'}} GB</span>
    </div>
    <div style="display: flex; justify-content: space-between; margin-top: 10px">
      <span style="font-size: 13px">
        预警提醒阈值
         {{ `${(data.cpuUsage * 100 * 1.20212)?.toFixed(1) ?? '未知'}`}} %
        <el-progress :status="percentageToStatus(data.cpuUsage * 100 * 1.20212)" :percentage="data.cpuUsage * 100 * 1.20212" :stroke-width="5" :show-text="false"/>
      </span>
      <div v-if="data.online">
        <el-button class="el-button--primary" style="height: 30px; width: 100px">
          人工智能分析
        </el-button>
      </div>
      <div v-else>
        <el-button class="el-button--danger" style="height: 30px; width: 100px">
          分析功能受限
        </el-button>
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
  box-shadow:  20px 20px 60px #bebebe,
  -20px -20px 60px #ffffff;
  transition: .3s;
  &:hover{
    cursor: pointer;
    scale: 1.02;
  }



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