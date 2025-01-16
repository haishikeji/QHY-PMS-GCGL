<template>
  <div class="content">
    <el-tabs value="first">
      <el-tab-pane :label="title" name="first">
        <el-row>
          <el-button type="primary" plain @click="goBacknew('/quality/examine',infoForm.createFlag,'infoForm',infoForm.flowStatus,id)">返回上一页</el-button>
  <el-button type="primary" plain @click="gohome">返回首页</el-button>
          <el-button type="primary" @click="submitInfo('infoForm')"
		   v-if="[1, 3].includes(infoForm.flowStatus) && [1, 2].includes(infoForm.createFlag) || !id|| changeFlag == 1"
            >保存</el-button
          >
		  <template v-if="id && !changeFlag">
		  <el-button
		    v-if="[1, 3,5].includes(infoForm.flowStatus)&&[1, 2].includes(infoForm.createFlag)&&infoForm.batchFlag != 1"
		    type="primary"
		    plain
		    :loading="auditRowloading"
		    @click="newauditshow"
		    >提审</el-button
		  >
		  <template>
		    <el-button
		      v-if="[2].includes(infoForm.flowStatus) && [1, 2].includes(infoForm.createFlag)&& infoForm.taskId &&infoForm.batchFlag != 1"
		      type="danger"
		      plain
		      @click="submitRevocation(infoForm)"
		      >撤审</el-button
		    >
		    <el-button
		     v-if="[2].includes(infoForm.flowStatus) && [0, 2].includes(infoForm.createFlag) && infoForm.taskId"
		      type="primary"
		      plain
		      @click="selectAudit(infoForm)"
		      >审核</el-button
		    >
		  </template>
		  <!-- <el-button
		    v-if="[4].includes(infoForm.flowStatus)"
		    type="warning"
		    plain
		    @click="submitAbandon(infoForm)"
		    >弃审</el-button
		  > -->
		  <el-button
		    v-if="[2, 3, 4].includes(infoForm.flowStatus)"
		    type="primary"
		    plain
		    @click="showProcess = true"
		    >审批历史</el-button
		  >
		  </template>
		  
		  <!-- 变更单 -->
		  <el-dropdown
		    v-if="infoForm.flowStatus == 4 && [1, 2].includes(infoForm.createFlag)"
		    style="margin-left: 10px"
		    @command="(e) => handleCommand(e, '变更质量检查与整改', 29,infoForm.sirCode)"
		  >
		    <el-button type="primary" plain>
		      变更<i class="el-icon-arrow-down el-icon--right"></i>
		    </el-button>
		    <el-dropdown-menu slot="dropdown">
		      <el-dropdown-item command="1">变更历史</el-dropdown-item>
		      <el-dropdown-item command="2">新增变更</el-dropdown-item>
		    </el-dropdown-menu>
		  </el-dropdown>
		  
        </el-row>

        <div class="box">
          <div class="info-box">
            <p class="title">基本信息</p>
            <el-form
              label-width="160px"
              :inline="true"
              :model="infoForm"
              class="search-form"
              ref="infoForm"
            >
              <el-form-item
                label="项目名称"
                prop="project"
                :rules="[
                  {
                    required: true,
                    message: '请选择项目',
                    trigger: ['change', 'blur'],
                  },
                ]"
              >
                <el-select
                  v-model="infoForm.project"
                  placeholder="请选择"
                  filterable
                  value-key="projectCode"
                  @change="changeProject"
                >
                  <el-option
                    v-for="item in proList"
                    :key="item.id"
                    :label="item.projectName"
                    :value="item"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="项目编号">
                <el-input
                  disabled
                  v-model="infoForm.projectCode"
                  placeholder="选择项目后显示"
                ></el-input>
              </el-form-item>
              <el-form-item label="下单部门">
                <el-input
                  disabled
                  v-model="infoForm.depName"
                  placeholder="请选择"
                ></el-input>
              </el-form-item>
              <el-form-item label="检查日期">
                <el-date-picker
                  type="date"
                  placeholder="选择日期"
                  v-model="infoForm.checkDate"
                  value-format="yyyy-MM-dd"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="安检负责人">
                <el-input
                  v-model="infoForm.checkPrincipal"
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
              <el-form-item label="检查类型">
                <el-select
                  v-model="infoForm.checkAcceptType"
                  filterable
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in dicObj.checkAcceptType"
                    :key="item.id"
                    :label="item.dictionaryValue"
                    :value="item.dictionaryValue"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="隐患名称">
                <el-input
                  v-model="infoForm.hiddenDangerName"
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
              <el-form-item label="隐患描述">
                <el-input
                  v-model="infoForm.hiddenDangerDesc"
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
              <el-form-item label="隐患等级">
                <el-select
                  v-model="infoForm.hiddenDangerLevel"
                  filterable
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in dicObj.hiddenDangerLevel"
                    :key="item.id"
                    :label="item.dictionaryValue"
                    :value="item.dictionaryValue"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="隐患图片上传" style="display: flex">
                <UploadFile
                  :isTable="false"
                  title="点击上传"
                  :isDesc="false"
                  :showFile="true"
				  :isImg="true"
                  :fileDataDtos="infoForm.hiddenDangerImgList"
                  @getFileList="getDangerImg"
                />
              </el-form-item>
              <el-form-item label="计划整改完成时间">
                <el-date-picker
                  type="date"
                  placeholder="选择日期"
                  v-model="infoForm.planRectificationFinishDate"
                  value-format="yyyy-MM-dd"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="责任人">
                <el-input
                  v-model="infoForm.rectificationPrincipal"
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
              <el-form-item label="整改图片上传" style="display: flex">
                <UploadFile
                  :isTable="false"
                  title="点击上传"
                  :isDesc="false"
                  :showFile="true"
                  :fileDataDtos="infoForm.abarbeitungImgList"
				   :isImg="true"
                  @getFileList="getAbaImg"
                />
              </el-form-item>
              <el-form-item label="整改完成日期">
                <el-date-picker
                  type="date"
                  placeholder="选择日期"
                  v-model="infoForm.rectificationFinishDate"
                  value-format="yyyy-MM-dd"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="验收人">
                <el-input
                  v-model="infoForm.rectificationCheckPrincipal"
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
<!--              <el-form-item label="整改完成情况" style="width: 100%">
                <el-input
                  v-model="infoForm.rectificationFinishCondition"
                  placeholder="请输入"
                ></el-input>
              </el-form-item> -->
			  <el-form-item label="整改完成情况">
			    <el-select
			      v-model="infoForm.rectificationFinishCondition"
            filterable
			      placeholder="请选择"
			    >
			      <el-option
			        v-for="item in dicObj.rectificationFinishCondition"
			        :key="item.id"
			        :label="item.dictionaryValue"
			        :value="item.dictionaryValue"
			      ></el-option>
			    </el-select>
			  </el-form-item>
			  <el-form-item
			  label="变更单备注" 
			  v-if="infoForm.changeRemark || changeFlag == 1"
			  prop="changeRemark"
			  :rules="[
			    {
			      required: true,
			      message: '请输入您改动的地方',
			    },
			  ]"
			  >
			    <el-input
			     type="textarea"
			     :rows="6"
			     v-model="infoForm.changeRemark"
			     placeholder="请输入您改动的地方"
			    ></el-input>
			  </el-form-item>
            </el-form>
          </div>
		  
		  <div class="info-box">
		    <p class="title">附件</p>
		    <UploadFile
		      @getFileList="getFiles"
		      @setFileMark="setFileMark"
		      :fileDataDtos="fileDataDtos"
		    />
		  </div>

          <div class="info-box" v-if="id">
            <p class="title">单据基础信息</p>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="单据编号">{{
                infoForm.sirCode || "-"
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
	<!-- 审批模版 -->
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
import { addOrUpdateIR, getIRInfo,submitRectification } from "@/api/safe";

export default {
  name: "",
  data() {
    return {
      infoForm: {
		  depName:null,
	  },
      id: null,
      title: "新增质量检查与整改",
      hiddenDangerImgList: [],
      abarbeitungImgList: [],
	  fileDataDtos: [],
	  auditRowloading:false,
	  auditshow:false,
	  auditData:[],
	  choosecontent:[],
	  changeFlag:0,
    };
  },

  computed: {
    ...mapState("optionInfo", [
      "userList",
      "proList",
      "depList",
      "depName",
      "dicObj",
    ]),
  },

   mixins: [mixins, audit],

  mounted() {
	this.auditgetlist('SAQ_QUALITY_INSPECTION_RECTIFICATION_KEY') 
    this.getDepartment();
    this.getProject({ flowStatus: 4 });
    this.getUsers();
    this.getDictionary({
      name: "隐患等级",
      key: "hiddenDangerLevel",
    });
    this.getDictionary({
      name: "检查类型",
      key: "checkAcceptType",
    });
	this.getDictionary({
	  name: "整改完成情况",
	  key: "rectificationFinishCondition",
	});
    const { id,crId,crIdtype} = this.$route.query;
    if (id) {
      this.id = id;
      this.title = "修改质量检查与整改";
      if (crId) {
        this.crId = crId;
        this.getChangeInfo(crId,crIdtype);
      } else {
        this.getDetail(id);
      }
    }
  },

  methods: {
    ...mapActions("optionInfo", [
      "getProject",
      "getUsers",
      "getDepartment",
      "getDictionary",
    ]),
	
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
		this.auditRow()
	},
	auditRow() {
	  this.$confirm("确定要提交审核?", "提示", {
	    confirmButtonText: "确定",
	    cancelButtonText: "取消",
	    type: "warning",
	  }).then(() => {
		  this.auditRowloading = true
	    submitRectification({
	      id: this.id,
		  comfirmFlag: 1,
		  processDefId:this.choosecontent[0].processDefId
	    }).then((res) => {
	      this.$message.success("提审成功");
	     this.auditRowloading = false
	      this.auditshow = false
	     this.getDetail(this.id);
	    });
	  });
	},
	
	// 上传附件
	getFiles(list) {
	  this.fileDataDtos = list;
	},
	
	setFileMark(remark, index) {
	  this.fileDataDtos[index].remark = remark;
	},

    getDetail(id) {
      getIRInfo({
        id,
      }).then((res) => {
        let { data } = res;
        this.setInfo(data);
      });
    },

    setInfo(data) {
      if ([2, 4, 5].includes(data.flowStatus)) {
        this.title = "质量检查与整改详情";
      }
	  if (data.fileDataDtos) {
	    this.fileDataDtos = data.fileDataDtos;
	    delete data.fileDataDtos;
	  }
      this.infoForm = data;
      if (data.projectCode) {
        this.infoForm.project = this.proList.filter(
          (ele) => ele.projectCode == data.projectCode
        )[0];
      }
    },

    submitInfo(formName,a,type) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          addOrUpdateIR({
            ...this.infoForm,
			fileDataDtos: this.fileDataDtos,
			dataType:1,
			changeFlag: this.changeFlag,
          }).then((res) => {
            // this.saveSuccess();
			if(type){
			  this.$message.success("操作成功!");
			  let url = "/quality/examine";
        //  this.$router.push(url);
          this.$router.go(-1); //标记返回
			} else if (this.changeFlag == 1) {
              this.gochanelist()
            } else{
			  this.$message.success("操作成功!");
			  let url = "/quality/examine/edit";
			  if (res.data) {
				url += `?id=${res.data}`;
			  }
			  this.$router.push(url);
			}
          });
        } else {
          return false;
        }
      });
    },

    changeProject(val) {
      this.infoForm.projectName = val.projectName;
      this.infoForm.projectCode = val.projectCode;
	   this.infoForm.depName = val.depName
	   this.infoForm.depCode = val.depCode
    },

    getDangerImg(list) {
      this.infoForm.hiddenDangerImgList = list.map(ele => ele.fileUrl);
    },

    getAbaImg(list) {
      this.infoForm.abarbeitungImgList = list.map(ele => ele.fileUrl);
    }
  },
};
</script>
      
<style lang="less" scoped>
@import "@/assets/edit.less";
</style>