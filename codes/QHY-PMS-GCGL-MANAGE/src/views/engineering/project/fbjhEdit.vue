<template>
  <div class="content">
    <el-tabs value="first">
      <el-tab-pane :label="title" name="first">
        <el-row>
          <el-button type="primary" plain @click="goBacknew('/project/fbjh',infoForm.createFlag,'infoForm',infoForm.flowStatus,id)">返回上一页</el-button>
          <el-button type="primary" plain @click="gohome">返回首页</el-button>
          <template v-if="!crId">
            <el-button
              v-if="
                (isEdit && [1, 3,5].includes(infoForm.flowStatus)) && [1, 2].includes(infoForm.createFlag) ||
                !id || changeFlag == 1
              "
              type="primary"
              @click="submitInfo('infoForm')"
              >保存</el-button
            >
            <template v-if="id && !changeFlag">
              <el-button
                v-if="[1, 3,5].includes(infoForm.flowStatus)&&[1, 2].includes(infoForm.createFlag)"
                type="primary"
                plain
				:loading="auditRowloading"
                @click="newauditshow"
                >提审</el-button
              >
              <el-button
                 v-if="[2].includes(infoForm.flowStatus) && [0, 2].includes(infoForm.createFlag) && infoForm.taskId"
                type="primary"
                plain
                @click="selectAudit(infoForm)"
                >审核</el-button
              >
              <el-button
                v-if="[2, 3, 4].includes(infoForm.flowStatus)"
                type="primary"
                plain
                @click="showProcess = true"
                >审批历史</el-button
              >
              <template v-if="[4].includes(infoForm.flowStatus)">
                  <el-button
                    v-if="infoForm.pushDownStatus == 1"
                    type="primary"
                    plain
                    @click="pushdowmts"
                    >下推</el-button
                  >
                  <PushDown v-else typeKey="分包计划" :data="infoForm" />
         
              </template>
              
              <template v-if="[1, 2, 3, 4].includes(infoForm.flowStatus)">
                <Search
                  url="/other/upOrDownLook/esSubpackagePlanUpLook"
                  :code="infoForm.projectCode"
                />
                <Search
                  btnText="下查"
                  url="/other/upOrDownLook/esSubpackagePlanLookDown"
                  :code="infoForm.dataCode"
                />
              </template>
              <el-dropdown
                v-if="infoForm.flowStatus == 4"
                style="margin-left: 10px"
                @command="handleCommand"
              >
                <el-button type="primary" plain>
                  变更<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="1">变更历史</el-dropdown-item>
                  <el-dropdown-item command="2">新增变更</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </template>
        </el-row>

        <div class="box">
          <div class="info-box">
            <p class="title">基本信息</p>
            <el-form
              label-width="150px"
              :inline="true"
              :model="infoForm"
              class="search-form"
              ref="infoForm"
            >
              <el-form-item label="下单部门">
                <el-input
                  disabled
                  v-model="infoForm.depName"
                  placeholder="请选择"
                ></el-input>
              </el-form-item>
			  
			  <el-form-item label="客户名称" prop="customerName" >
			    <el-input
			  	  disabled
			      v-model="infoForm.customerName"
			      placeholder="请输入"
			    ></el-input>
			  </el-form-item>
			  
              <el-form-item
                label="项目名称"
                prop="projectName"
                :rules="[
                  {
                    required: true,
                    message: '请选择项目',
                    trigger: ['change', 'blur'],
                  },
                ]"
              >
				<el-tooltip class="item" effect="dark" :content="infoForm.projectName" placement="top">
				  <el-select
                  :disabled="Boolean(changeFlag)"
                  v-model="infoForm.projectName"
                  placeholder="请选择"
                  filterable
                  value-key="projectCode"
                  @change="changeProject"
                >
                  <el-option
                    v-for="item in proList"
                    :key="item.projectCode"
                    :label="item.projectName"
                    :value="item"
                  ></el-option>
                </el-select>
				</el-tooltip>
              
              </el-form-item>
              <el-form-item label="项目编号">
                <el-input
                  disabled
                  v-model="infoForm.projectCode"
                  placeholder="选择项目后显示"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="申请人"
                prop="applyUserCode"
                :rules="[
                  {
                    required: true,
                    message: '请选择申请人',
                    trigger: ['change', 'blur'],
                  },
                ]"
              >
                <el-select
                  v-model="infoForm.applyUserCode"
                  filterable
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in userList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.userCode"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="工程分包名称"
                prop="name"
                :rules="[
                  {
                    required: true,
                    message: '请输入工程分包名称',
                    trigger: 'blur',
                  },
                ]"
              >
                <el-input
                  v-model="infoForm.name"
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
              <el-form-item label="进场时间">
                <el-date-picker
                  type="date"
                  placeholder="选择日期"
                  v-model="infoForm.enterTime"
                  value-format="yyyy-MM-dd"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="备注" style="width: 100%">
                <el-input
                  type="textarea"
                  v-model="infoForm.remark"
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
            </el-form>
          </div>

          <div class="info-box">
            <p class="title">附件</p>
            <UploadFile @getFileList="getFiles" @setFileMark="setFileMark" :fileDataDtos="fileDataDtos"/>
          </div>

          <div class="info-box" v-if="id">
            <p class="title">单据基础信息</p>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="单据编号">{{
                infoForm.dataCode || "-"
              }}</el-descriptions-item>
              <el-descriptions-item label="单据状态"
                >已提交</el-descriptions-item
              >
              <el-descriptions-item label="创建人">{{
                infoForm.createBy || "-"
              }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">
                {{ infoForm.createTime || "-" }}
              </el-descriptions-item>
              <el-descriptions-item label="修改人">{{
                infoForm.updateBy || "-"
              }}</el-descriptions-item>
              <el-descriptions-item label="修改时间">
                {{ infoForm.updateTime || "-" }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <Audit
      :data="auditInfo"
      :showAudit.sync="showAudit"
      :showProcess.sync="showProcess"
      :code="infoForm.flowCode"
    />
	<WDialog
	  :value="auditshow"
	  title="请选择审批流程"
	  width="580px"
	  @cancel="auditshow = false"
	  @ok="auditok"
    :auditRowloading="auditRowloading"
	>
	  <el-table
	    :data="auditData"
	    border
		@selection-change="audileSelectionChange"
	    style="width: 100%">
		 <el-table-column type="selection" width="55"> </el-table-column>
	    <el-table-column
		 type="index"
	      label="序号"
	      >
	    </el-table-column>
		<el-table-column
		  prop="formName"
		  label="流程名称"
		 >
		</el-table-column>
	  </el-table>
	</WDialog>
  </div>
</template>
      
<script>
import { mapState, mapActions } from "vuex";
import mixins from "@/mixins/index";
import audit from "@/mixins/audit";
import {
  getEOPlanInfo,
  addOrUpdateEOPlan,
  submitEOPlanAudit,
} from "@/api/engineering";
import { getCRInfo } from "@/api/platform";

export default {
  name: "",
  data() {
    return {
      infoForm: {
		  depName:null,
	  },
      dataType: 2,
      details: [],
      fileDataDtos: [],
      id: null,
      isEdit: 1,
      title: "新增分包",
      changeFlag: 0,
      crId: null,
	  auditRowloading:false,
	  auditData:[],
	  choosecontent:[],
	  auditshow:false,	
    };
  },

  computed: {
    ...mapState("optionInfo", ["userList", "proList", "depList", "depName"]),
  },

  mixins: [mixins, audit],

  mounted() {
	this.auditgetlist('ES_ENGINEERING_SUBPACKAGE_PLAN_KEY') 
    this.getDepartment();
    this.getProject({ flowStatus: 4 });
    this.getUsers();
    const { id, crId,crIdtype } = this.$route.query;
    if (id) {
      this.id = id;
      this.title = "修改分包";
      if (crId) {
        this.crId = crId;
        this.getChangeInfo(crId,crIdtype);
      } else {
        this.getInfo(id);
      }
    }
  },

  methods: {
    ...mapActions("optionInfo", ["getProject", "getUsers", "getDepartment"]),
	// 选择审核模版
	audileSelectionChange(val) {
		this.choosecontent = val
	},
	getauditData(val) {
		this.auditData = val
	},
	auditok(){
		if(this.choosecontent.length !== 1){
			this.$message.error('请选择一个审批模板');
			return false
		}
		this.submitAudit()
	},
	
	pushdowmts() {
		this.$message.error('一个合同只允许下推一次');
	},

    getInfo(id) {
      getEOPlanInfo({
        id: this.id,
      }).then((res) => {
        let { data } = res;
        this.setInfo(data);
      });
    },

    setInfo(data) {
      if ([2, 4, 5].includes(data.flowStatus) || this.crId) {
        this.title = "分包详情";
      }
      if (data.fileDataDtos) {
        this.fileDataDtos = data.fileDataDtos;
        delete data.fileDataDtos;
      }
      this.infoForm = data;
      // if (data.projectCode) {
      //   this.infoForm.project = this.proList.filter(
      //     (ele) => ele.projectCode == data.projectCode
      //   )[0];
      // }
	  this.auditRowloading = false
    },

    submitInfo(formName,a,type) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          addOrUpdateEOPlan({
            ...this.infoForm,
            dataType: this.dataType,
            fileDataDtos: this.fileDataDtos,
            changeFlag: this.changeFlag,
          }).then((res) => {
			let id = res.data || null
			this.goBackVersion2(type,'/project/fbjh',id)
          });
        } else {
          return false;
        }
      });
    },

    submitAudit() {
      this.$confirm("确定要提审?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
		  this.auditRowloading = true
        submitEOPlanAudit({ id: this.infoForm.id,processDefId:this.choosecontent[0].processDefId }).then((res) => {
		  this.auditshow = false
          this.$message.success("提审成功");
          this.getInfo(this.infoForm.id);
        });
      });
    },

    changeProject(val) {
      this.infoForm.projectName = val.projectName;
      this.infoForm.projectCode = val.projectCode;
	  this.infoForm.customerName = val.customerName
	   this.infoForm.depName = val.depName
	    this.infoForm.depCode = val.depCode
    },

    deleteRow() {
      this.$confirm("确定要删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.details = this.details.filter(
          (ele) => !this.multipleSelection.includes(ele)
        );
      });
    },

    handleCommand(command) {
      if (command == 1) {
        this.$router.push(
          `/platform/change?dataType=6&path=${encodeURIComponent(
            this.$route.path
          )}`
        );
      } else {
        this.title = "变更分包";
        this.changeFlag = 1;
      }
    },

    getFiles(list) {
      this.fileDataDtos = list;
    },

    setFileMark(remark, index) {
      this.fileDataDtos[index].remark = remark;
    },
  },
};
</script>
      
<style lang="less" scoped>
@import "@/assets/edit.less";
</style>