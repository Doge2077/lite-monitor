<script setup>
import {computed, reactive, ref} from "vue";
import {get} from '@/net'
import {useRoute} from "vue-router";
import ReportDetails from "@/component/ReportDetails.vue";
import ReportCard from "@/component/ReportCard.vue";


const list = ref([])

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

const checkedNodes = ref([])

const route = useRoute()

const clientList = computed(() => {
  if(checkedNodes.value.length === 0) {
    return list.value
  } else {
    return list.value.filter(item => checkedNodes.value.indexOf(item.location) >= 0)
  }
})

const updateList = () => {
  if (route.name === 'report') {
    get('/monitor/list', data => list.value = data)
  }
}
setInterval(updateList, 2000)
updateList()

const detail = reactive({
  show: false,
  clientId: -1
})

const displayClientDetails = (clientId) => {
  detail.show = true
  detail.clientId = clientId
}

const register = reactive({
  show: false,
  token: ''
})

const refreshToken = () => get('/monitor/register', token => register.token = token)

function openTerminal(clientId) {
  terminal.show = true
  terminal.clientId = clientId
  detail.show = false
}
const terminal = reactive({
  show: false,
  clientId: -1
})

</script>

<template>
  <div class="manage-main">
    <div style="display: flex; justify-content: space-between; align-items: end">
      <div>
        <div class="title"><i class="fa-brands fa-guilded"></i> 报告主机列表</div>
        <div class="desc">在这里查看主机健康报告，具备智能监控预警和人工智能风险预测功能</div>
      </div>
    </div>
    <el-divider style="margin: 10px 0;"/>
    <div style="margin-bottom: 20px">
      <el-checkbox-group v-model="checkedNodes">
        <el-checkbox v-for="node in locations" :key="node" :label="node.name" border>
          <span :class="`flag-icon flag-icon-${node.name}`"></span>
          <span style="font-size: 13px;margin-left: 10px">{{node.desc}}</span>
        </el-checkbox>
      </el-checkbox-group>
    </div>
    <div class="card-list" v-if="list.length">
      <report-card v-for="item in clientList" :data="item" :update="updateList"
                    @click="displayClientDetails(item.clientId)"/>
    </div>
    <el-drawer size="800" direction="btt" :show-close="false" v-model="detail.show"
               :with-header="false" v-if="list.length" @close="detail.clientId = -1">
      <report-details :client-id="detail.clientId" :update="updateList"/>
    </el-drawer>
  </div>
</template>

<style scoped>

:deep(.el-drawer) {
  margin-bottom: 10px;
  height: calc(100% - 20px);
  border-radius: 20px;
}

:deep(.el-drawer__body) {
  padding: 0;
}

.manage-main{
  margin: 0 50px;
  .title{
    font-size: 22px;
    font-weight: bold;
  }

  .desc{
    font-size: 15px;
    color: gray;
  }

  .card-list {
    display: flex;
    gap: 20px;
    flex-wrap: wrap;
  }
}
</style>