<script setup>
import {reactive, watch} from "vue";
import {get} from "@/net";
import {copyId, rename} from "@/tools";

const props = defineProps({
  clientId: Number,
  update: Function
})

const details = reactive({
  base: {},
  runtime: {}
})

function updateDetails() {
  props.update()
  init(props.clientId)
}

const init = clientId =>{
    if (clientId !== -1) {
      details.base = {}
      get(`/api/monitor/details?clientId=${clientId}`, data => Object.assign(details.base, data))
    }
}

watch(() => props.clientId, init, {immediate: true})

</script>

<template>
  <div class="client-details" v-loading="Object.keys(details.base).length === 0">
    <div v-if="Object.keys(details.base).length">
      <div class="title">
        <i class="fa-solid fa-server"></i>
        服务器详情信息
      </div>
      <el-divider style="margin: 10px 0"/>
      <div class="details-list">
        <div>
          <span>服务器 ID</span>
          <span>{{ details.base.clientId }}</span>
        </div>
        <div>
          <span>服务器名称</span>
          <span>{{ details.base.clientName }}</span>&nbsp;
          <i class="fa-regular fa-pen-to-square interact-item" @click.stop="rename(details.base.clientId, details.base.clientName, updateDetails)"></i>
        </div>
        <div>
          <span>运行状态</span>
          <span>
            <i style="color: #18cb18" class="fa-regular fa-circle-play" v-if="details.base.online"></i>
            <i style="color: red" class="fa-regular fa-circle-stop" v-else></i>
            {{ details.base.online ? '运行中' : '离线' }}
          </span>
        </div>
        <div>
          <span>服务器节点</span>
          <span :class="`flag-icon flag-icon-${details.base.location}`"></span> &nbsp;
          <span>未命名节点</span>
        </div>
        <div>
          <span>公网 IP</span>
          <span>
            {{ details.base.clientAddress }}
            <i class="fa-regular fa-copy interact-item" @click.stop="copyId(details.base)" style="color: dodgerblue"></i>
          </span>
        </div>
        <div>
          <span>处理器</span>
          <span>{{ details.base.cpuName }}</span>
        </div>
        <div>
          <span>硬件配置信息</span>
          <span>
            <i class="fa-solid fa-microchip"></i>
            {{details.base.cpuCores}} 核 CPU |
            <i class="fa-solid fa-memory"></i>
            {{ details.base.osMemory.toFixed(1)}} GB 内存
          </span>
        </div>
        <div>
          <span>操作系统</span>
          <span> {{`${details.base.osName} ${details.base.osVersion}`}} </span>
        </div>
      </div>
      <div class="title">
        <i class="fa-solid fa-gauge-high"></i>
        实时监控信息
      </div>
      <el-divider style="margin: 10px 0"/>
      <div style="display: flex">
        <el-progress type="dashboard" :width="100" :percentage="20" status="success">
          <div style="font-size: 17px; font-weight: bold; color: initial">CPU</div>
          <div style="font-size: 13px; color: grey; margin-top: 5px">20%</div>
        </el-progress>
        <el-progress style="margin-left: 20px"
                     type="dashboard" :width="100" :percentage="60" status="success">
          <div style="font-size: 17px; font-weight: bold; color: initial">内存</div>
          <div style="font-size: 13px; color: grey; margin-top: 5px">28.6GB</div>
        </el-progress>
        <div style="flex: 1; margin-left: 30px; display: flex; flex-direction: column; height: 80px">
          <div style="flex: 1; font-size: 14px">
            <div>实时网络速度</div>
            <div>
              <i style="color: orange" class="fa-solid fa-arrow-up"></i>
              <span>{{`20KB/s`}}</span>
              <el-divider direction="vertical"/>
              <i style="color: dodgerblue" class="fa-solid fa-arrow-down"></i>
              <span>{{`0KB/s`}}</span>
            </div>
          </div>
          <div>
            <div style="font-size: 13px; display: flex; justify-content: space-between">
              <div>
                <i class="fa-solid fa-hard-drive"></i>
                <span>磁盘总容量</span>
              </div>
              <div>6.6 GB /  40.0 GB</div>
            </div>
            <el-progress type="line" status="success" :percentage="24" :show-text="false"/>
          </div>
        </div>
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