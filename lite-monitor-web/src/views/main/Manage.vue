<script setup>
import PreviewCard from "@/component/PreviewCard.vue";
import {computed, reactive, ref} from "vue";
import {get} from '@/net'
import ClientDetails from "@/component/ClientDetails.vue";
import {Plus} from "@element-plus/icons-vue";
import RegisterCard from "@/component/RegisterCard.vue";
import {useRoute} from "vue-router";
import {useStore} from "@/store";
import TerminalWindow from "@/component/TerminalWindow.vue";

const store = useStore()

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
  if (route.name === 'manage') {
    get('/monitor/list', data => list.value = data)
  }
}
setInterval(updateList, 1000)
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
        <div class="title"><i class="fa-solid fa-server"></i> 管理主机列表</div>
        <div class="desc">在这里管理所有已经注册的主机实例，实时监控主机状态，快速管理</div>
      </div>
      <div>
        <el-button :icon="Plus" type="primary" plain :disabled="!store.isAdmin"
                      @click="register.show = true">添加新主机</el-button>
      </div>
    </div>
    <el-divider style="margin: 10px 0;"/>
    <div style="margin-bottom: 20px">
      <el-checkbox-group v-model="checkedNodes">
        <el-checkbox v-for="node in locations" :key="node" :label="node.name" border>
          <span :class="`fi fi-${node.name}`"></span>
          <span style="font-size: 13px;margin-left: 10px">{{node.desc}}</span>
        </el-checkbox>
      </el-checkbox-group>
    </div>
    <div class="card-list" v-if="list.length">
      <preview-card v-for="item in clientList" :data="item" :update="updateList"
                    @click="displayClientDetails(item.clientId)"/>
    </div>
    <el-empty description="还没有任何主机哦，点击右上角添加一个吧" v-else/>
    <el-drawer size="520" :show-close="false" v-model="detail.show"
               :with-header="false" v-if="list.length" @close="detail.clientId = -1">
      <client-details :client-id="detail.clientId" :update="updateList" @delete="updateList" @terminal="openTerminal(detail.clientId)"/>
    </el-drawer>
    <el-drawer v-model="register.show" direction="btt" :with-header="false"
               style="width: 600px;margin: 10px auto" size="320" @open="refreshToken">
      <register-card :token="register.token"/>
    </el-drawer>
    <el-drawer style="width: 800px; background-color: #E0E0E0" :size="520" direction="btt"
               @close="terminal.clientId = -1"
               v-model="terminal.show" :close-on-click-modal="false">
      <template #header>
        <div>
          <div style="font-size: 18px;color: dodgerblue;font-weight: bold;">SSH远程连接</div>
          <div style="font-size: 14px">
            远程连接的建立将由服务端完成，因此在内网环境下也可以正常使用。
          </div>
        </div>
      </template>
      <terminal-window :client-id="terminal.clientId"/>
    </el-drawer>
  </div>
</template>

<style scoped>

:deep(.el-drawer) {
  margin: 10px;
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
