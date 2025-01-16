<template>
  <div class="content">
    <el-tabs value="first">
      <el-tab-pane :label="title" name="first">
        <el-row>
          <el-button type="primary" plain @click="goBacknew('/project/sgys',infoForm.createFlag,'infoForm',infoForm.flowStatus,id)">返回上一页</el-button>
          <el-button type="primary" plain @click="gohome">返回首页</el-button>
          <template >

            <el-button
			  v-if="isEdit &&[1, 3,5].includes(infoForm.flowStatus)&&[1, 2].includes(infoForm.createFlag)|| !id|| changeFlag == 1&&!crId||isEdit &&[1, 3,5].includes(infoForm.changeRecordFlowStatus)&&[1, 2].includes(infoForm.createFlag)"
              type="primary"
              @click="submitInfo('infoForm')"
              >保存</el-button
            >
			<el-button
			  v-if="[1, 3,5].includes(infoForm.changeRecordFlowStatus)&&[1, 2].includes(infoForm.createFlag)|| changeFlag == 1&&!crId"
			  type="primary"
			  plain
			  :loading="auditRowloading"
			  @click="BGsubmitAudit(crId,id)"
			  >变更提审</el-button
			>
			<el-button
			  v-if="[2].includes(infoForm.changeRecordFlowStatus)&&[1, 2].includes(infoForm.createFlag)"
			  type="danger"
			  plain
			  @click="submitRevocationBG(infoForm,BGtaskId,BGflowCode)"
			  >撤审</el-button>
			  <el-button v-if="[2].includes(infoForm.changeRecordFlowStatus) && [0, 2].includes(infoForm.createFlag) && infoForm.taskId"
			       type="primary" plain @click="selectAudit(infoForm)"
			      >审核</el-button
			   >
            <template>
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
                v-if="[2].includes(infoForm.flowStatus) && [1, 2].includes(infoForm.createFlag)&& infoForm.taskId"
                type="danger"
                plain
                @click="submitRevocation(infoForm)"
                >撤审</el-button
              >
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
              <template v-if="[1,2,3,4].includes(infoForm.flowStatus)">
                <Search
                  url="/other/upOrDownLook/esConstructionBudgetUpLook"
                  :code="infoForm.projectCode"
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
              <!-- <el-form-item
                label="预算编号"
                prop="constructionBudgetCode"
                :rules="[
                  {
                    required: true,
                    message: '请输入预算编号',
                    trigger: 'blur',
                  },
                ]"
              >
                <el-input
                  v-model="infoForm.constructionBudgetCode"
                  placeholder="请输入"
                ></el-input>
              </el-form-item> -->
			  <el-form-item label="下单部门">
			    <el-input
			      disabled
			      v-model="infoForm.depName"
			      placeholder="请选择"
			    ></el-input>
			  </el-form-item>
			  <el-form-item label="客户名称" prop="customerName" >
			    <el-input
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
              
			  <el-form-item
			    label="编制日期"	
				v-if="crId"
			  >
			   <el-input
			     v-model="bztime"
			     disabled
			   ></el-input>
			  </el-form-item>
			  <el-form-item
				v-else
			    label="编制日期"
			    prop="compileDate"
			    :rules="[
			      {
			        required: true,
			        message: '请选择编制日期',
			        trigger: ['change', 'blur'],
			      },
			    ]"
			  >
			    <el-date-picker
			      type="date"
			      placeholder="选择日期"
			      v-model="infoForm.compileDate"
			      value-format="yyyy-MM-dd"
			    ></el-date-picker>
			  </el-form-item>
              <el-form-item label="预算总金额">
                <el-input
                  v-model="principal"
                  disabled
                  onkeyup="value=value.replace(/[^\d\.]/g,'')"
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
			  
			  <el-form-item label="备注" style="width: 100%">
			    <el-input
			      type="textarea"
			      v-model="infoForm.remark"
			      placeholder="请输入"
			    ></el-input>
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
            <p class="title">预算明细</p>
      <!--      <el-row>
              <el-button type="primary" @click="addRow">导出</el-button>
            </el-row> -->
            <el-table
              :data="constructionBudgetDetails"
              border
              style="width: 100%; margin-top: 15px"
              class="table-box"
            >
              <el-table-column type="index" label="序号" width="80">
              </el-table-column>
              <el-table-column label="科目">
                <template slot-scope="scope">
                  {{ scope.row.costSubject }}
                </template>
              </el-table-column>
			   <!-- :disabled="!(([1, 3,5].includes(infoForm.flowStatus) && changeFlag != 1 ) || !id )" -->
              <el-table-column label="成本控制科目">
                <template slot-scope="scope">
                  <el-switch
                    v-if="scope.row.isEdit"
                    :disabled="true"
                    v-model="scope.row.costControlFlag"
                    :active-value="1"
                    :inactive-value="0"
                  >
                  </el-switch>
                  <p v-else>-</p>
                </template>
              </el-table-column>
              <el-table-column label="单位">
                <template slot-scope="scope">
                  <p v-if="!isEdit || !scope.row.isEdit">
                    {{ scope.row.unit }}
                  </p>
                  <el-select
                    v-else
                    v-model="scope.row.unit"
                    filterable
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                    <el-option
                      v-if="scope.row.costSubject == '人工费'"
                      label="人"
                      value="人"
                    >
                    </el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="工程量">
                <template slot-scope="scope">
                  <p v-if="!isEdit || !scope.row.isEdit">{{ scope.row.num }}</p>
                  <el-input
                    v-else
                    v-model="scope.row.num"
                    placeholder="请输入"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column label="控制总金额（元）">
                <template slot-scope="scope">
                  <p v-if="!isEdit || !scope.row.isEdit">
                    {{ scope.row.unitPrice }}
                  </p>
                  <el-input
                    v-else
                    v-model="scope.row.unitPrice"
                    @input="handleInput(scope.row, scope.$index,1)"
                    placeholder="请输入"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column label="含税金额">
                <template slot-scope="scope">
                  <template v-if="isEdit">
                    <el-input
                      v-if="scope.row.isMoney"
                      v-model="scope.row.totalPrice"
                      @input="handleInput(scope.row, scope.$index,2)"
                      placeholder="请输入"
                    ></el-input>
                    <p v-else-if="scope.row.isPorices">{{ zprices }}</p>
                    <p v-else-if="scope.row.isPrincipal">
                      {{ principal || "-" }}
                    </p>
                    <p v-else-if="scope.row.isPro">
                      {{
                        zprices && profit
                          ? ((profit / zprices) * 100).toFixed(2) + "%"
                          : "-"
                      }}
                    </p>
                    <p v-else>{{ scope.row.totalPrice }}</p>
                  </template>
                  <p v-else>{{ scope.row.totalPrice || "-" }}</p>
                </template>
              </el-table-column>
              <el-table-column label="预警比例">
                <template slot-scope="scope">
					<!-- v-if="!scope.row.costControlFlag" -->
                  <p>
                    {{ scope.row.earlyWarningRatio || "0" }}%
                  </p>
             <!--     <el-input
                    v-else
                    v-model="scope.row.earlyWarningRatio"
                    placeholder="请输入"
                  ></el-input> -->
                </template>
              </el-table-column>
              <el-table-column label="预警金额">
                <template slot-scope="scope">
					<!-- v-if="!scope.row.costControlFlag" -->
                  <p >
                    {{ scope.row.earlyWarningMoney || "-" }}
                  </p>
         <!--         <el-input
                    v-else
                    v-model="scope.row.earlyWarningMoney"
                    onkeyup="value=value.replace(/[^\d\.]/g,'')"
                    placeholder="请输入"
                  ></el-input> -->
                </template>
              </el-table-column>
              <el-table-column label="强控比例">
                <template slot-scope="scope">
					<!-- v-if="!scope.row.costControlFlag" -->
                  <p >
                    {{ scope.row.forceControlRatio || "0" }}%
                  </p>
           <!--       <el-input
                    v-else
                    v-model="scope.row.forceControlRatio"
                    placeholder="请输入"
                  ></el-input> -->
                </template>
              </el-table-column>
              <el-table-column label="强控金额">
                <template slot-scope="scope">
					<!-- v-if="!scope.row.costControlFlag" -->
                  <p >
                    {{ scope.row.forceControlMoney || "-" }}
                  </p>
             <!--     <el-input
                    v-else
                    v-model="scope.row.forceControlMoney"
                    onkeyup="value=value.replace(/[^\d\.]/g,'')"
                    placeholder="请输入"
                  ></el-input> -->
                </template>
              </el-table-column>
              <el-table-column label="备注">
                <template slot-scope="scope">
					<!-- v-if="!isEdit"> -->
                  <!-- <p> {{ scope.row.remark || "-" }}</p> -->
                <el-input
                    v-model="scope.row.remark"
                    placeholder="请输入"
                  ></el-input>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <div class="info-box">
            <p class="title">附件</p>
            <UploadFile
              @getFileList="getFiles"
              @setFileMark="setFileMark"
              :fileDataDtos="fileDataDtos"
			   :showUpdata="true"
			  :crId="crId"
			  :changeFlag="changeFlag"
            />
          </div>

          <div class="info-box" v-if="id">
            <p class="title">单据基础信息</p>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="单据编号">{{
                infoForm.constructionBudgetCode || "-"
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
	  :crId="crId"
	  @getChangeInfo="getChangeInfo"
	  @getInfo="getInfo(id)"
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
import moment from'moment';
import { mapState, mapActions } from "vuex";
import mixins from "@/mixins";
import audit from "@/mixins/audit";

import {
  getSGYYInfo,
  addOrUpdateSGYY,
  getSGYYDetail,
  submitSGYSAudit,
} from "@/api/engineering";
import { getCRInfo } from "@/api/platform";

export default {
  name: "",
  data() {
    return {
      infoForm: {},
      constructionBudgetDetails: [
        {
          costSubject: "材料费",
          isEdit: true,
        },
        {
          costSubject: "人工费",
          isEdit: true,
        },
        {
          costSubject: "机械费",
          isEdit: true,
        },
        {
          costSubject: "税金",
          isMoney: true,
		  // isPorices:true,
          isEdit: false,
        },
        {
          costSubject: "成本",
          // isPorices:true,
		  isMoney: true,
          isEdit: false,
        },
        {
          costSubject: "利润",
          isMoney: true,
		  // isPorices:true,
		  isProfit: true,
         // isPro:true,
          isEdit: false,
        },
        {
          costSubject: "利润比",
          isPro:true,
		  // isPro: true,
		   // isPorices:true,
          isEdit: false,
        },
        {
          costSubject: "总金额",
          isPorices: true,
          isEdit: false,
        },
      ],
      options: [
        {
          value: "元",
        },
        {
          value: "天",
        },
      ],
      prices: 0,
	  zprices:0,
      profit: 0,
      proportion: 0,
      principal: 0,
      fileDataDtos: [],
      id: null,
      isEdit: 1,
      title: "新增预算",
      changeFlag: 0,
      changeRecordCode: null,
	  bztime:null,
	  auditRowloading:false,
	  oppen:false,
	  auditData:[],
	  choosecontent:[],
	  auditshow:false,	
	  crId:null,
	  crIdtype:'',
	  BGflowCode:'',
	  BGtaskId:'',
    };
  },

  watch: {
    constructionBudgetDetails: {
      handler(val) {
        val.forEach((ele, index) => {
          if (ele.num || ele.unitPrice) {
            let n = ele.num || 1;
            let p = ele.unitPrice || 1;
            ele.totalPrice = Number(
              (n * p).toString().match(/^\d+(?:\.\d{0,2})?/)
            );
          }
          if (ele.isProfit && ele.totalPrice) {
            this.profit = Number(ele.totalPrice);
          }
          if (ele.costControlFlag && ele.earlyWarningRatio) {
            ele.earlyWarningMoney = Math.round(
              (ele.totalPrice * ele.earlyWarningRatio) / 100
            );
          }
          if (ele.costControlFlag && ele.forceControlRatio) {
            ele.forceControlMoney = Math.round(
              (ele.totalPrice * ele.forceControlRatio) / 100
            );
          }
        });

        this.prices = val.reduce((total, item, index) => {
          let price = Number(item.totalPrice) || 0;
          if (index < 3) {
            this.principal = Number(
              (total + price).toString().match(/^\d+(?:\.\d{0,4})?/)
            );
          }
          if (index == 3) {
            this.principal = Number(
              (total + price).toString().match(/^\d+(?:\.\d{0,4})?/)
            );
          }
          return Number((total + price).toString().match(/^\d+(?:\.\d{0,4})?/));
        }, 0);
		if(val[4].totalPrice || val[5].totalPrice){
			this.zprices = (Number(val[4].totalPrice) + Number(val[5].totalPrice)).toFixed(2)
		}
      },
      deep: true,
    },
  },

  computed: {
    ...mapState("optionInfo", ["proList"]),
  },

  mixins: [mixins, audit],

  mounted() {
	this.auditgetlist('ES_CONSTRUCTION_BUDGET_KEY') 
    this.getProject({ flowStatus: 4 });
    const { id, crId, pdId,crIdtype } = this.$route.query;
    if (id) {
      this.id = id;
      this.title = "修改预算";
      if (crId) {
        this.crId = Number(crId);
        this.crIdtype = crIdtype
        this.getChangeInfo(crId,crIdtype);
      } else {
        this.getInfo(id);
      }
    }else{
		if (crId) {
		  this.title = "修改预算";
		  this.crId = Number(crId);
		  this.crIdtype = crIdtype
		  this.getChangeInfo(crId,crIdtype);
		}
	}

    if (pdId) {
      this.getPDInfo(pdId);
    }
  },

  methods: {
    ...mapActions("optionInfo", ["getProject"]),
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
	// 验证小数后两位
	handleInput(row,index,num){
		if(num == 1){
			row.unitPrice = ("" + row.unitPrice)
				.replace(/[^\d^\.]+/g, "") // 第二步：把不是数字，不是小数点的过滤掉
			  .replace(/^0+(\d)/, "$1") // 第三步：第一位0开头，0后面为数字，则过滤掉，取后面的数字
			  .replace(/^\./, "0.") // 第四步：如果输入的第一位为小数点，则替换成 0. 实现自动补全
			  .match(/^\d*(\.?\d{0,2})/g)[0] || ""; // 第五步：最终匹配得到结果 以数字开头，只有一个小数点，而且小数点后面只能有0到2位小数
			// // 更新tableData数组
			this.constructionBudgetDetails[index].unitPrice = row.unitPrice
		}else if(num ==2){
			row.totalPrice = ("" + row.totalPrice)
			 .replace(/[^\d^\.^\-]+/g, "") // 允许负号，把不是数字，不是小数点的过滤掉
			 .replace(/^0+(\d)/, "$1")
			 .replace(/^\./, "0.")
			 .replace(/^-\./, "-0.") // 处理负号开头且只有一个点的情况
			 .match(/^-?\d*(\.?\d{0,2})/g)[0] || "";
			// // 更新tableData数组
			this.constructionBudgetDetails[index].totalPrice = row.totalPrice
		}
		
	},

    getInfo(id) {
      getSGYYInfo({
        id: this.id,
      }).then((res) => {
		  let { data } = res;
		  this.setInfo(data);
     
      });
    },

    // getChangeInfo(id) {
    //   getCRInfo({
    //     id,
    //   }).then((res) => {
    //     let { data } = res;
    //     let lastRecord = JSON.parse(data.lastRecord);
    //     this.setInfo(lastRecord.data);
    //   });
    // },

    setInfo(data) {
		
      if ([2, 4, 5].includes(data.flowStatus) || this.crId) {
        this.title = "预算详情";
      }
	  if(data.compileDate){
		  this.bztime = data.compileDate
	  }
	  if(data.createTime){
		  this.bztime = moment(data.createTime).format('YYYY-MM-DD');
	  }
      if (data.fileDataDtos) {
        this.fileDataDtos = data.fileDataDtos;
        delete data.fileDataDtos;
      }
      if (data.constructionBudgetDetails) {
        const details = data.constructionBudgetDetails;
		
        this.constructionBudgetDetails = this.constructionBudgetDetails.map(
          (ele, index) => {
            return {
			 ...ele,
              num: details[index].num,
              totalPrice:
                ele.isPorices || ele.isPrincipal || ele.isPro
                  ? 0
                  : details[index].totalPrice,
				  costControlFlag: details[index].costControlFlag,
				  earlyWarningMoney: details[index].earlyWarningMoney,
				  earlyWarningRatio: details[index].earlyWarningRatio,
				  forceControlMoney: details[index].forceControlMoney,
				  forceControlRatio: details[index].forceControlRatio,
				  remark: details[index].remark,
				  unit: details[index].unit,
				  unitPrice: details[index].unitPrice || null,
            };
          }
        );
		
		 delete data.constructionBudgetDetails;
      }
      this.setInfoForm(data);
    },

    setInfoForm(data) {
		// console.log(this.constructionBudgetDetails,'查看数据')
      this.infoForm = data;
  //     if (data.projectCode) {
  //       this.infoForm.project = this.proList.filter(
  //         (ele) => ele.projectCode == data.projectCode
  //       )[0];
		// this.infoForm.customerName = this.infoForm.project.customerName
  //     }
	  
	  // 修改变更状态
	  if(this.infoForm.changeFlag) {
	  	this.changeFlag = this.infoForm.changeFlag
	  }
	  
	  this.auditRowloading = false
    },
// changeFlag=0
    submitInfo(formName, a,type) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const details = JSON.parse(
            JSON.stringify(this.constructionBudgetDetails)
          );
          details.forEach((ele) => {
            if (ele.isPorices && this.zprices) {
              ele.totalPrice = this.zprices;
            }
            if (ele.isPrincipal && this.principal) {
              ele.totalPrice = this.principal;
            }
            if (ele.isPro && this.prices && this.zprices) {
				// console.log('进来了嘛？',ele.costSubject)
              ele.totalPrice = Math.round((this.profit / this.zprices) * 100);
            }
          });
		  
          addOrUpdateSGYY({
            ...this.infoForm,
            budgetAmountL: this.principal,
            constructionBudgetDetails: details,
            fileDataDtos: this.fileDataDtos,
            changeFlag: this.changeFlag,
			crSubmitAudit:a == 101 ? 1:2,
			changeRecordId: Number(this.crId ) || null
          }).then((res) => {
			  let id = res.data || null
			  this.goBackVersion2(type,'/project/sgys',id)
    //       if(type){
			 //    // this.saveSuccess();
				//   this.$message.success("操作成功!");
				//   let url = "/project/sgys";
				//    // this.$router.push(url);
				//    this.$router.go(-1); //标记返回
		  // }else{
			 //  this.$message.success("操作成功!");
			 //  let url = "/project/sgys/edit";
			 //  if (res.data) {
			 //    url += `?id=${res.data}`;
			 //  }
			 //  this.$router.push(url);
		  // }
          });
        } else {
          return false;
        }
      });
    },
	
	goBack1(){
		this.$confirm("确定返回吗?", "提示", {
		    confirmButtonText: "确定",
		    cancelButtonText: "取消",
		    type: "warning",
		}).then(() => {
		   let url = "/project/sgys";
		   this.$router.push(url);
		});
		
	},
	

    submitAudit(id, index) {
      this.$confirm("确定要提审?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
		this.auditRowloading = true
        submitSGYSAudit({ id: this.infoForm.id,processDefId:this.choosecontent[0].processDefId }).then((res) => {
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
	  this.infoForm.depName = val.depName;
	  this.infoForm.depCode = val.depCode
    },

    addRow() {
      this.constructionBudgetDetails.push({
        name: "",
        data: "",
        address: "",
        isEdit: false,
      });
    },

    editRow(index) {
      this.constructionBudgetDetails[index].isEdit = true;
    },

    deleteRow(index) {
      this.$confirm("确定要删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.constructionBudgetDetails.splice(index, 1);
      });
    },

    handleCommand(command) {
      if (command == 1) {
        this.$router.push(
          `/platform/change?dataCode=${
            this.infoForm.constructionBudgetCode
          }&dataType=3&path=${encodeURIComponent(this.$route.path)}`
        );
      } else {
        this.changeFlag = 1;
        this.title = "变更预算";
		this.oppen = !this.oppen
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
