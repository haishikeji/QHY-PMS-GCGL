<template>
  <div class="content">
    <el-tabs value="first">
      <el-tab-pane :label="title" name="first">
        <el-row>
          <el-button type="primary" plain @click="goBacknew('/reimbursement/application',infoForm.createFlag,'infoForm',infoForm.flowStatus,id)">返回上一页</el-button>
  <el-button type="primary" plain @click="gohome">返回首页</el-button>
          <el-button
            v-if="[0, 1, 3,5].includes(infoForm.flowStatus) && [1, 2].includes(infoForm.createFlag) || !id"
            type="primary"
            @click="submitInfo('infoForm')"
            >保存</el-button
          >
          <template v-if="id">
            <el-button
              v-if="[1, 3,5].includes(infoForm.flowStatus)&&[1, 2].includes(infoForm.createFlag)"
              type="primary"
              plain
			  :loading="auditRowloading"
              @click="newauditshow"
              >提审</el-button
            >
            <template v-if="infoForm.flowStatus == 2 && infoForm.taskId">
              <el-button
                v-if="[2].includes(infoForm.flowStatus) && [1, 2].includes(infoForm.createFlag)&& infoForm.taskId"
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
		  <template v-if="id">
		  <!-- 	  <newexportExcelBtn
		  		style=""
		  		url="/fm/paymentRequest/exportOne"
		  		:params="{
		  			id:id
		  		}"
		  		file-name="付款申请">
		  	  </newexportExcelBtn> -->
		  	    <Print :id="infoForm.id" :type="13" />
		    </template>
        </el-row>

        <div class="box">
          <div class="info-box">
            <p class="title">基本信息</p>
            <el-form
              label-width="150px"
              :inline="true"
              :model="infoForm"
              ref="infoForm"
              class="search-form"
            >
              <el-form-item
                label="费用类型"
                style="width: 100%"
                prop="reimburseType"
                :rules="[
                  {
                    required: true,
                    message: '请选择费用类型',
                    trigger: 'change',
                  },
                ]"
              >
                <el-radio-group
                  v-model="infoForm.reimburseType"
                  @input="resetType"
                >
                  <el-radio :label="1" value="">项目费用</el-radio>
                  <el-radio :label="2" value="">行政费用</el-radio>
                </el-radio-group>
              </el-form-item>
          <!--    <template v-if="infoForm.reimburseType == 1">
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
                    value-key="id"
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
              </template> -->
         <!--     <el-form-item v-if="infoForm.reimburseType == 1" label="下单部门">
                <el-input
                  disabled
                  v-model="infoForm.depName"
                  placeholder="请选择"
                ></el-input>
              </el-form-item>
              <el-form-item v-else label="下单部门">
                <el-input
                  disabled
                  v-model="depName"
                  placeholder="请选择"
                ></el-input>
              </el-form-item> -->
              <el-form-item
                label="收款人类型"
                prop="receiveType"
                :rules="[
                  {
                    required: true,
                    message: '请选择收款人类型',
                    trigger: ['change', 'blur'],
                  },
                ]"
              >
                <el-select
                  v-model="infoForm.receiveType"
                  placeholder="请选择"
                  @change="changeType"
                >
                  <el-option label="企业" :value="1"></el-option>
                  <el-option label="员工" :value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                :label="infoForm.receiveType == 1 ? '收款供应商' : '收款人'"
                prop="receiver"
                :rules="[
                  {
                    required: true,
                    message: '请选择收款人',
                    trigger: 'change',
                  },
                ]"
              >
                <el-select
                  v-model="infoForm.receiver"
                  filterable
                  value-key="receiveCode"
                  placeholder="请选择"
                  @change="changeReciver"
                >
                  <el-option
                    v-for="item in accountList"
                    :key="item.id"
                    :label="item.receiveName"
                    :value="item"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="收款账户" prop="receiveBankAccount">
                <el-input
                  v-model="infoForm.receiveBankAccount"
                  placeholder="选择收款人后自动填入"
                ></el-input>
              </el-form-item>
			  <el-form-item label="开户支行" prop="receiveBankName">
			    <el-input
			      v-model="infoForm.receiveBankName"
			      placeholder="选择收款人后自动填入"
			    ></el-input>
			  </el-form-item>
              <el-form-item label="费用金额">
                <el-input
                  disabled
                  v-model="reimbursableAmount"
                  onkeyup="value=value.replace(/[^\d\.]/g,'')"
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
            </el-form>
          </div>
		  
		  
<!-- 费用明细 -->
          <div class="info-box">
            <p class="title">费用明细</p>
            <el-row>
              <el-button type="primary" @click="addRow">添加</el-button>
			  <ComUpdealflie
			     :Upurl="upexurl"
			     @sonUpdata="dealdata"
			   ></ComUpdealflie>
			   <el-button
			     :disabled="!multipleSelection.length"
			     type="danger"
			     plain
			     @click="deleteRow2"
			     >删除</el-button
			   >
            </el-row>
            <el-table
              :data="details"
              border
              style="width: 100%; margin-top: 15px"
              class="table-box"
			  @selection-change="handleSelectionChange"
            >
			 <el-table-column type="selection" width="55"> </el-table-column>
              <el-table-column type="index" label="序号" width="80">
              </el-table-column>
			  
			  <el-table-column label="项目编号" width="150" v-if="infoForm.reimburseType == 1">
			    <template slot-scope="scope">
			      <el-select
			        v-model="scope.row.projectCode"
			        filterable
			        placeholder="请选择"
					@change="choosepcode(scope.row.projectCode,scope.$index)"
			      >
			        <el-option
			          v-for="item in proList"
			          :key="item.id"
			          :label="item.projectCode"
			          :value="item.projectCode"
			        ></el-option>
			      </el-select>
			    </template>
			  </el-table-column>
			  
			  
			  <el-table-column label="项目名称" width="150" v-if="infoForm.reimburseType == 1">
			    <template slot-scope="scope">
			      <el-select
			        v-model="scope.row.projectName"
			        filterable
			        placeholder="请选择"
					@change="choosepname(scope.row.projectName,scope.$index)"
			      >
			        <el-option
			          v-for="item in proList"
			          :key="item.id"
			          :label="item.projectName"
			          :value="item.projectName"
			        ></el-option>
			      </el-select>
			    </template>
			  </el-table-column>
			  
		
			  
              <el-table-column label="费用大类">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.expenseType"
                    filterable
                    placeholder="请选择"
                    @change="getBudgetInfo($event, scope.$index)"
                  >
                    <el-option
                      v-for="item in dicObj.invoicePayType"
                      :key="item.id"
                      :label="item.dictionaryValue"
                      :value="item.dictionaryValue"
                    ></el-option>
                  </el-select>
                </template>
              </el-table-column>
			  
			  <el-table-column label="预算成本科目" v-if="infoForm.reimburseType == 1">
			    <template slot-scope="scope">
			      <el-select
			        v-model="scope.row.constructionBudgetName"
			        filterable
			        placeholder="请选择"
			      >
			    <!--    <el-option
			          v-for="item in dicObj.constructionBudgetName"
			          :key="item.id"
			          :label="item.dictionaryValue"
			          :value="item.dictionaryValue"
			        ></el-option> -->
					<el-option
					  label="人工费"
					  value="人工费"
					></el-option>
					<el-option
					  label="机械费"
					  value="机械费"
					></el-option>
					<el-option
					  label="材料费"
					  value="材料费"
					></el-option>
			      </el-select>
			    </template>
			  </el-table-column>
			  
			  
              <el-table-column label="内容">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.content"
                    placeholder="请输入"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column label="发票类型">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.invoiceType"
                    placeholder="请选择"
                    @change="resetTaxAmounts(scope.$index)"
                  >
                    <el-option
                      label="增值税专用发票"
                      value="增值税专用发票"
                    ></el-option>
                    <el-option
                      label="增值税普通发票"
                      value="增值税普通发票"
                    ></el-option>
                  </el-select>
                </template>
              </el-table-column>
			  
			  <el-table-column label="收票金额">
			    <template slot-scope="scope">
			      <el-input
			        v-model="scope.row.invoiceNum"
			        @input="handleInputnew(scope.row.invoiceNum,scope.$index,2)"
			        placeholder="请输入"
			      ></el-input>
			    </template>
			  </el-table-column>
			  
             <!-- <el-table-column label="发票数量">
                <template slot-scope="scope">
                  <p v-if="scope.row.isEdit">
                    {{ scope.row.invoiceNum || "-" }}
                  </p>
                  <el-input
                    v-else
                    v-model="scope.row.invoiceNum"
                    placeholder="请输入"
                  ></el-input>
                </template>
              </el-table-column> -->
              <el-table-column label="金额（含税）">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.invoiceMoney"
                    @input="handleInputnewCG(scope.row.invoiceMoney,scope.$index,1)"
                    placeholder="请输入"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column label="进项税率">
                <template slot-scope="scope">
                  <p v-if="scope.row.invoiceType == '增值税普通发票'">-%</p>
                  <el-select
                    v-else
                    v-model="scope.row.taxAmounts"
                    placeholder="请选择"
                    @change="$forceUpdate()"
                  >
                    <el-option label="销售商品13%" value="13%"></el-option>
                    <el-option label="建筑服务9%" value="9%"></el-option>
                    <el-option label="建筑服务3%" value="3%"></el-option>
                    <el-option label="销售服务6%" value="6%"></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="备注">
                <template slot-scope="scope">
                  <p v-if="scope.row.isEdit">{{ scope.row.remark || "-" }}</p>
                  <el-input
                    v-else
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
            />
          </div>

          <div class="info-box" v-if="id">
            <p class="title">单据基础信息</p>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="单据编号">{{
                infoForm.expenseReimburseCode || "-"
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
  getReiInfo,
  addOrUpdateRei,
  submitReiAudit,
  getRecAccount,
} from "@/api/finance";
import { getSGYYBudget } from "@/api/engineering";
import { setSubjects } from "@/utils/enumStatus";
import Setting from '@/setting';

export default {
  name: "",
  data() {
    return {
      infoForm: {
        constructionBudgetDetailId: "",
        receiver: {},
        depName: null,
      },
      reimbursableAmount: 0,
      details: [],
      fileDataDtos: [],
      id: null,
      title: "新增费用",
      subjects: setSubjects(),
      budgetList: [],
      accountList: [],
	  auditRowloading:false,
	  auditData:[],
	  choosecontent:[],
	  auditshow:false,
	  newdetails:[],
	  upexurl: `${Setting.request.apiDaseURL}/fm/expenseReimburse/detailImport`,
	  multipleSelection:[],
    };
  },

  watch: {
    details: {
      handler(val) {
        this.reimbursableAmount = val.reduce((total, item) => {
          let price = Number(item.invoiceMoney) || 0;
          return total + price;
        }, 0).toFixed(2);
      },
      deep: true,
    },
  },

  computed: {
    ...mapState("optionInfo", [
      "userList",
      "proList",
      "depList",
      "depName",
      "dicObj",
      "supList",
    ]),
  },

  mixins: [mixins, audit],

  mounted() {
	this.auditgetlist('FM_EXPENSE_REIMBURSE_KEY')
    this.getDepartment();
    this.getProject({ flowStatus: 4 });
    this.getUsers();
    this.getSupplier();
    this.getDictionary({
      name: "发票类型",
      key: "invoiceType",
    });
    this.getDictionary({
      name: "成本科目",
      key: "constructionBudgetName",
    });
	this.getDictionary({
	  name: "费用大类",
	  key: "invoicePayType",
	});
    const { id } = this.$route.query;
    if (id) {
      this.id = id;
      this.title = "修改费用";
      this.getDetail(id);
    }
  },

  methods: {
    ...mapActions("optionInfo", [
      "getProject",
      "getUsers",
      "getDepartment",
      "getDictionary",
      "getSupplier",
    ]),
	handleSelectionChange(val) {
	  this.multipleSelection = val;
	},
	// 
	dealdata(data) {
	  this.details = data.data;
	},
	
	// 选择项目名称
	choosepname(e,index) {
		this.details[index].projectCode = this.proList.filter(
		  (ele) => ele.projectName == e
		)[0].projectCode;
		
	},
	// 选择项目编号
	choosepcode(e,index) {
		this.details[index].projectName = this.proList.filter(
		  (ele) => ele.projectCode == e
		)[0].projectName;
	},
	
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
	
	// 验证后两位小数
	yzinput(val,index,type){
		if(type == 1){this.details[index].invoiceMoney = val}
		if(type == 2){this.details[index].invoiceNum = val}
	},

    getDetail(id) {
      getReiInfo({
        id,
      }).then((res) => {
        let { data } = res;
        this.setInfo(data);
      });
    },

    setInfo(data) {
      if ([2, 4, 5].includes(data.flowStatus)) {
        this.title = "费用详情";
      }
      if (data.fileDataDtos) {
        this.fileDataDtos = data.fileDataDtos;
        delete data.fileDataDtos;
      }
      if (data.details && data.details.length) {
        this.details = data.details;
        delete data.details;
      }
      this.infoForm = data;
      if (data.projectCode) {
        this.infoForm.project = this.proList.filter(
          (ele) => ele.projectCode == data.projectCode
        )[0];
        this.getBudget(data.projectCode);
      }
      if (data.receiveType) {
        this.changeType(data.receiveType, 1);
      }
	  if(data.receiveName) {
		  this.infoForm.receiver = data.receiveName
	  }
    },

    submitInfo(formName,a,type) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          addOrUpdateRei({
            ...this.infoForm,
            reimbursableAmount: this.reimbursableAmount,
            details: this.details,
            fileDataDtos: this.fileDataDtos,
          }).then((res) => {
            // this.saveSuccess();
			if(type){
			  this.$message.success("操作成功!");
			  let url = "/reimbursement/application";
        //  this.$router.push(url);
          this.$router.go(-1); //标记返回
			}else{
			  this.$message.success("操作成功!");
			  let url = "/reimbursement/application/edit";
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
      this.infoForm.constructionBudgetDetailId = "";
      this.getBudget(val.projectCode);
      this.infoForm.depName = val.depName;
      this.infoForm.depCode = val.depCode;
    },

    getBudget(projectCode) {
      getSGYYBudget({ projectCode }).then((res) => {
        this.budgetList = res.data;
        this.$forceUpdate();
      });
    },

    getBudgetInfo(val, index) {
      const info = this.budgetList.filter((ele) => ele.id == val)[0];
      this.details[index].content = info.remark;
      // this.details[index].invoiceMoney = info.totalPrice;
      this.details[index].taxAmounts = info.earlyWarningRatio;
    },

    resetType() {
      // this.infoForm.receiveType = "";
      // this.infoForm.receiver = "";
      this.details = [];
      delete this.infoForm.depName;
      delete this.infoForm.depCode;
    },

    changeType(receiveType, state) {
      getRecAccount({ receiveType }).then((res) => {
        this.accountList = res.data;
        this.infoForm.receiver = [];
  //       this.infoForm.receiveBankAccount = "";
		// this.infoForm.receiveBankName="";
        if (state && this.accountList.length && this.infoForm.receiveCode) {
          this.infoForm.receiver = this.accountList.filter(
            (ele) => ele.receiveCode == this.infoForm.receiveCode
          )[0];
        }
      });
    },

    changeReciver(val) {
      this.infoForm = {
        ...this.infoForm,
        ...val,
      };
    },

    resetTaxAmounts(index) {
      this.details.index.taxAmounts = "";
      this.$forceUpdate();
    },

    auditRow() {
      this.$confirm("确定要提交审核?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
		this.auditRowloading = true
        submitReiAudit({
          id: this.id,
		  processDefId:this.choosecontent[0].processDefId
        }).then((res) => {
			this.auditRowloading = false
		  this.auditshow = false
          this.$message.success("提审成功");
          this.getDetail(this.id);
		  
        });
      });
    },

    addRow() {
      this.details.push({
        isEdit: false,
      });
    },

    editRow(index) {
      this.details[index].isEdit = !this.details[index].isEdit;
    },

    deleteRow(index) {
      this.$confirm("确定要删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.details.splice(index, 1);
      });
    },
	deleteRow2() {
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
