<template>
  <div class="content">
    <el-tabs value="first">
      <el-tab-pane :label="title" name="first">
        <el-row>
          <el-button type="primary" plain @click="goBacknew('/invoice/manage',infoForm.createFlag,'infoForm',1,id)">返回上一页</el-button>
  <el-button type="primary" plain @click="gohome">返回首页</el-button>
          <el-button type="primary" @click="submitInfo('infoForm')"
            >保存</el-button
          >
          <template v-if="id">
            <el-button
             v-if="[1, 3,5].includes(infoForm.flowStatus)&&[1, 2].includes(infoForm.createFlag)"
              type="primary"
              plain
              >提审</el-button
            >
            <el-button
              v-if="[2].includes(infoForm.flowStatus) && [0, 2].includes(infoForm.createFlag) && infoForm.taskId"
              type="primary"
              plain
              >审核</el-button
            >
            <!-- <el-button
              v-if="[2].includes(infoForm.flowStatus) && [1, 2].includes(infoForm.createFlag)&& infoForm.taskId"
              type="danger"
              plain
              >撤审</el-button
            >
            <el-button
              v-if="[4].includes(infoForm.flowStatus)"
              type="primary"
              plain
              >弃审</el-button
            > -->
            <el-button
              v-if="[2, 3, 4].includes(infoForm.flowStatus)"
              type="primary"
              plain
              >审批历史</el-button
            >
            <template v-if="infoForm.flowStatus == 4">
              <PushDown typeKey="收票登记" :data="infoForm" />
    
            </template>

            
               <template v-if="[1, 2, 3, 4].includes(infoForm.flowStatus)">
                    <Search
                      url="/other/upOrDownLook/fmInvoiceRegisterUpLook"
                      :code="infoForm.invoiceRegisterCode"
                    />
                    <Search
                      btnText="下查"
                      url="/other/upOrDownLook/fmInvoiceRegisterLookDown"
                      :code="infoForm.invoiceRegisterCode"
                    />
              
              </template>
          </template>
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
              <el-form-item
                label="合同/订单类型"
                prop="dataType"
                :rules="[
                  {
                    required: true,
                    message: '请选择合同/订单类型',
                    trigger: ['change', 'blur'],
                  },
                ]"
              >
                <el-select
                  v-model="infoForm.dataType"
                  placeholder="请选择"
                  @change="changeType"
                >
                  <el-option label="采购订单" :value="1"></el-option>
                  <el-option label="采购合同" :value="2"></el-option>
                  <el-option label="租赁合同" :value="3"></el-option>
                  <el-option label="分包合同" :value="4"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="合同/订单编号"
                prop="contract"
                :rules="[
                  {
                    required: true,
                    message: '请选择合同/订单编号',
                    trigger: ['change', 'blur'],
                  },
                ]"
              >
                <el-select
                  :disabled="!infoForm.dataType"
                  v-model="infoForm.contract"
                  placeholder="请先选择合同类型"
                  filterable
                  value-key="id"
                  @change="changeContract"
                >
                  <el-option
                    v-for="item in contractList"
                    :key="item.id"
                    :label="item.dataCode"
                    :value="item"
                  ></el-option>
                </el-select>
              </el-form-item>
			  
              <el-form-item label="供应商名称">
                <el-input
                  disabled
                  v-model="infoForm.supplierName"
                  placeholder="根据合同/订单显示"
                ></el-input>
              </el-form-item>
			  <el-form-item
			      label="订单/合同结算单号"
			    prop="settleCode"
			  >
			    <el-select
			    :disabled="!infoForm.contract"
			     v-model="infoForm.settleCode"
			      placeholder="请选择订单/合同结算单号"
			      filterable
			      value-key="id"
			      @change="changesettleCode"
			    >
			    <el-option
			    v-for="item in settleCodeList"
			    :label="item.settleCode"
			    :value="item"
			    ></el-option>
			    </el-select>
			  </el-form-item>
			  
			  <el-form-item
			    label="收票日期"
			    prop="billDate"
			
			  >
			    <el-date-picker
			      type="date"
			      placeholder="选择日期"
			      v-model="infoForm.billDate"
			      value-format="yyyy-MM-dd"
			    ></el-date-picker>
			  </el-form-item>
			  
            </el-form>
          </div>

          <div class="info-box">
            <p class="title">发票信息</p>
            <el-form
              label-width="150px"
              :inline="true"
              :model="infoForm"
              class="search-form"
              ref="infoForm2"
            >
              <el-form-item
                label="税率"
                :rules="[
                  {
                    required: true,
                    message: '请选择税率',
                    trigger: ['change', 'blur'],
                  },
                ]"
              >
                <el-select v-model="infoForm.taxesRatio" @change="changRatio" placeholder="请选择" filterable>
                  <el-option
                    v-for="item in dicObj.taxesRatio"
                    :key="item.id"
                    :label="item.dictionaryValue"
                    :value="item.dictionaryValue"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="金额（含税）"
                prop="invoiceMoney"
                :rules="[
                  {
                    required: true,
                    message: '请输入金额（含税）',
                    trigger: 'blur',
                  },
                ]"
              >
                <el-input
                  v-model="infoForm.invoiceMoney"
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
              <el-form-item label="附件上传" style="width: 100%">
                <UploadFile
                  :isTable="false"
                  title="点击上传"
                  :isDesc="false"
                  :showFile="true"
                  :fileDataDtos="fileDataDtos"
                  @getFileList="getFileList"
                />
              </el-form-item>
            </el-form>
          </div>

          <div class="info-box" v-if="id">
            <p class="title">单据基础信息</p>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="单据编号">{{
                infoForm.invoiceRegisterCode || "-"
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
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import mixins from "@/mixins/index";
import audit from "@/mixins/audit";
import {
  getSettleList,
  getOrderList,
  getConList,
  getLConList,
  getSConList,
} from "@/api/procure";
import { getInvInfo, addOrUpdateInv } from "@/api/finance";

export default {
  name: "",
  data() {
    return {
      infoForm: {
        contract: null,
        dataCode: "",
        settleCode: "",
        supplierName: "",
        supplierCode: "",
        paymentType: 1,
		invoiceMoney:'',
		billDate:'',
      },
      fileDataDtos: [],
      id: null,
      title: "收票管理",
      contractList: [],
	  settleCodeList:[],
	  moenynum:null,
	  selejsnum:null,
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
    this.getDepartment();
    this.getProject({ flowStatus: 4 });
    this.getUsers();
    this.getDictionary({
      name: "税率",
      key: "taxesRatio",
    });
    const { id, pdId } = this.$route.query;
    if (id) {
      this.id = id;
      this.title = "修改收票管理";
      this.getInfo(id);
    }
    if (pdId) {
      this.getPDInfo(pdId);
    }
  },

  methods: {
    ...mapActions("optionInfo", [
      "getProject",
      "getUsers",
      "getDepartment",
      "getDictionary",
    ]),

    getInfo(id) {
      getInvInfo({
        id: this.id,
      }).then((res) => {
        let { data } = res;
        this.setInfo(data);
      });
    },

    getChangeInfo(id) {
      getCRInfo({
        id,
      }).then((res) => {
        let { data } = res;
        let lastRecord = JSON.parse(data.lastRecord);
        this.setInfo(lastRecord.data);
      });
    },

    setInfo(data) {
      if (data.fileDataDtos) {
        this.fileDataDtos = data.fileDataDtos;
        delete data.fileDataDtos;
      }
      this.setInfoForm(data);
    },

    setInfoForm(data) {
		
      this.infoForm = JSON.parse(JSON.stringify(data));
	

      if (data.projectCode) {
        this.infoForm.project = this.proList.filter(
          (ele) => ele.projectCode == data.projectCode
        )[0];
      }
	  // 下推过来的数据
	  if(this.infoForm.projectName){
	  	this.infoForm.project = this.infoForm.projectName
	  }
      if (data.dataType) {
        let dataCode = data.dataCode;
        let contract = data.contract;
        if (contract && contract.purchaseContractCode) {
          dataCode = contract.purchaseContractCode;
        }
        if (contract && contract.purchaseOrderCode) {
          dataCode = contract.purchaseOrderCode;
        }
        if (contract && contract.leaseContractCode) {
          dataCode = contract.leaseContractCode;
        }
        if (contract && contract.subpackageContractCode) {
          dataCode = contract.subpackageContractCode;
        }
        this.changeType(this.infoForm.dataType, dataCode);
      }
	  if(data.settleCode){
		  setTimeout(()=>{
			  	 this.infoForm.settleCode = data.settleCode
				 this.infoForm.invoiceMoney =data.invoiceMoneyBeforeTaxes
				 if(data.invoiceMoney){
					 this.infoForm.invoiceMoney = data.invoiceMoney
				 }
		  },1000)
	  
	  }
	  if(this.id){
	  	 this.selejsnum = data.settleCode
	  }
    },

    submitInfo(formName,a,type) {
		if(this.moenynum && this.infoForm.invoiceMoney>this.moenynum){
			this.$message.error('填写金额不能大于结算金额');
			return
		}
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$refs[formName + "2"].validate((valid2) => {
            if (valid2) {
              addOrUpdateInv({
                ...this.infoForm,
                fileDataDtos: this.fileDataDtos,
              }).then((res) => {
                // this.saveSuccess();
				if(type){
				  this.$message.success("操作成功!");
				  let url = "/invoice/manage";
          //  this.$router.push(url);
          this.$router.go(-1); //标记返回
				}else{
				  this.$message.success("操作成功!");
				  let url = "/invoice/manage/edit";
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
        } else {
          return false;
        }
      });
    },

    changeProject(val) {
      this.infoForm.projectName = val.projectName;
      this.infoForm.projectCode = val.projectCode;
      this.changeType(this.infoForm.dataType);
    },

    changeType(dataType, dataCode) {
      this.resetContract();
      this.getContractList(dataType, dataCode);
    },

    async getContractList(type, dataCode) {
      let res = null;
      let params = {
        flowStatus: 4,
        projectCode: this.infoForm.projectCode,
      };
      if (type == 1) {
        res = await getOrderList(params);
      } else if (type == 2) {
        res = await getConList(params);
      } else if (type == 3) {
        res = await getLConList(params);
      } else {
        res = await getSConList(params);
      }
      const { data } = res;
      data.forEach((ele) => {
        ele.dataCode =
          ele.purchaseOrderCode ||
          ele.purchaseContractCode ||
          ele.leaseContractCode ||
          ele.subpackageContractCode;
      });
	  
      this.contractList = data || [];
	  
      if (dataCode && this.contractList.length) {
        this.infoForm.contract = this.contractList.filter(
          (ele) => ele.dataCode == dataCode
        )[0];
        this.changeContract(this.infoForm.contract);
      }
	  if(this.id) {
	  	this.infoForm.settleCode = this.selejsnum
	  }
    },
	//税率选择
	changRatio(val){
		this.infoForm.taxesRatio = val
		this.$forceUpdate()
	},

    changeContract(data) {
		// this.settleCodeList = this.contractList
		this.moenynum = null
	 
		if(data.settleCodeList) {
			this.settleCodeList = data.settleCodeList
		}    
	   this.infoForm.taxesRatio = data.taxesRatio || "";
      this.infoForm.dataCode = data.dataCode;
      this.infoForm.settleCode = data.settleCode || "";
      this.infoForm.supplierName = data.supplierName || "";
      this.infoForm.supplierCode = data.supplierCode || "";
    },

    resetContract() {
      this.contractList = [];
      this.infoForm.contract = null;
      this.infoForm.dataCode = "";
      this.infoForm.settleCode = "";
      this.infoForm.supplierName = "";
      this.infoForm.supplierCode = "";
    },

    getFileList(list) {
      this.fileDataDtos = list;
    },
	
	// 选择订单合同结算号
	changesettleCode(val){
		this.infoForm.settleCode = val.settleCode
		
		if(val.settleMoneyBeforeTaxes) {
			this.infoForm.invoiceMoney = val.settleMoneyBeforeTaxes
			this.moenynum = val.settleMoneyBeforeTaxes
		}
		
	},
	goBack1(){
		this.$confirm("确定要返回?", "提示", {
		  confirmButtonText: "确定",
		  cancelButtonText: "取消",
		  type: "warning",
		}).then(() => {
			let url = "/invoice/manage";
			this.$router.push(url);
		})
	}
  },
};
</script>

<style lang="less" scoped>
@import "@/assets/edit.less";
</style>
