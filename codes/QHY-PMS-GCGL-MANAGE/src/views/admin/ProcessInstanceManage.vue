<template>
  <div v-loading="loading">
<!--    <div style="text-align: right">
      <el-select size="small" filterable placeholder="筛选表单" v-model="params.code">
        <el-option :value="form.id" :label="form.formName" v-for="form in formList" :key="form.id"></el-option>
      </el-select>
    </div>-->
    <el-table :data="dataList" :header-cell-style="{background: '#e8e8e8'}" style="width: 100%; margin: 20px 0" @row-click="showProcess">
      <el-table-column fixed prop="processDefName" label="审批类型" show-overflow-tooltip></el-table-column>
      <el-table-column prop="staterUser" show-overflow-tooltip label="发起人" min-width="100px">
        <template slot-scope="scope">
          <avatar :size="35" :name="scope.row.staterUser.name" :src="scope.row.staterUser.avatar"/>
        </template>
      </el-table-column>
      <el-table-column show-overflow-tooltip prop="startTime" label="提交时间" min-width="120px"></el-table-column>
      <el-table-column show-overflow-tooltip prop="finishTime" label="结束时间" min-width="120px"></el-table-column>
      <el-table-column show-overflow-tooltip prop="taskName" label="当前节点">

      </el-table-column>
      <el-table-column prop="status" label="审批状态">
        <template slot-scope="scope">
          <el-tag :type="getProcTag(scope.row.result)">{{scope.row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column show-overflow-tooltip prop="duration" label="已耗时" min-width="120px">
        <template slot-scope="scope">
          {{getDuration(scope.row)}}
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" min-width="90">
        <template slot-scope="scope">
          <el-button type="text" @click.stop="delInstance(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align: right">
      <el-pagination hide-on-single-page background layout="prev, pager, next" :total="total"
                     :page-size="params.pageSize" :current-page.sync="params.pageNo"></el-pagination>
    </div>
    <el-drawer :size="isMobile ? '100%': '500px'" direction="rtl" title="审批详情" :visible.sync="processVisible">
      <instance-preview v-if="processVisible" :instance-id="selectInstance" @handler-after="handlerAfter"></instance-preview>
    </el-drawer>
  </div>
</template>

<script>
import taskApi from '@/api/processTask'
import {delProcessInst} from '@/api/process'
import moment from "moment";
import InstancePreview from '../workspace/approval/ProcessInstancePreview'
import {getProcTag} from "@/utils/ProcessUtil";

export default {
  name: "ProcessInstancePreview",
  components: {InstancePreview},
  data() {
    return {
      total: 0,
      params:{
        pageSize: 10,
        pageNo: 1,
        finished: null,
        code: ''
      },
      selectInstance: '',
      loading: false,
      processVisible: false,
      formList: [],
      dataList: []
    }
  },
  mounted() {
    this.getSubmittedList()
  },
  computed:{
    isMobile(){
      return window.screen.width < 450
    },
  },
  methods: {
    getProcTag,
    getSubmittedList(){
      this.loading = true
      taskApi.getSubmittedList(this.params).then(rsp => {
        this.loading = false
        this.total = rsp.data.total
        this.dataList = rsp.data.records
      }).catch(e => {
        this.loading = false
      })
    },
    showProcess(row){
      this.processVisible = true
      this.selectInstance = row.instanceId
    },
    delInstance(row){
      this.$confirm('删除后将失去该流程实例所有数据且无法恢复，是否继续 ?', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delProcessInst(row.instanceId).then(rsp => {
          this.getSubmittedList()
          this.$ok(rsp, '删除流程实例成功')
        }).catch(err => {
          this.$err(err, '删除流程实例失败')
        })
      })
    },
    getDuration(row){
      let end = this.$isNotEmpty(row.finishTime) ? row.finishTime : moment().format("YYYY-MM-DD HH:mm:ss")
      return this.$timeCoverStr(row.startTime, end)
    },
    handlerAfter(){
      this.processVisible = false
      this.getSubmittedList()
    }
  },
  watch:{
    params:{
      deep: true,
      handler(){
        this.getSubmittedList()
      }
    }
  }
}
</script>

<style scoped>

</style>
