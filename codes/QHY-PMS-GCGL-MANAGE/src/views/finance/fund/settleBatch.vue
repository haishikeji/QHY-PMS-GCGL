<template>
  <div>
    <el-form
      :inline="true"
      label-width="120px"
      :model="searchForm"
      class="search-form"
      ref="searchForm"
    >
      <el-form-item label="批量付款单号" prop="batchCode">
        <el-input
          v-model="searchForm.batchCode"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item label="操作时间" prop="createTime">
        <el-date-picker
          v-model="searchForm.createTime"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          style="width: 250px"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="操作人" prop="createBy">
        <!--   <el-select
          v-model="searchForm.createBy"
          filterable
          placeholder="请选择"
        >
          <el-option
            v-for="item in userList"
            :key="item.id"
            :label="item.name"
            :value="item.name"
          ></el-option>
        </el-select> -->
        <el-input v-model="searchForm.createBy" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item class="form-btns">
        <el-button type="primary" @click="searchSubmit">查询</el-button>
        <el-button @click="resetForm('searchForm')">重置</el-button>
      </el-form-item>
      <el-form-item label="审核状态" prop="flowStatus">
        <el-select
          v-model="searchForm.flowStatus"
          filterable
          placeholder="请选择"
        >
          <el-option
            v-for="item in flowStatus"
            :key="item.id"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="付款状态" prop="status">
        <el-select v-model="searchForm.status" filterable placeholder="请选择">
          <el-option label="未付款" :value="0"></el-option>
          <el-option label="已付款" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="供应商" prop="supplierCode">
        <el-select
          v-model="searchForm.supplierCode"
          placeholder="请选择"
          filterable
        >
          <el-option
            v-for="item in supList"
            :key="item.id"
            :label="item.supplierName"
            :value="item.supplierCode"
          ></el-option>
        </el-select>
      </el-form-item>
    </el-form>

    <div class="pagination-box">
      <el-row>
        <el-button type="primary" size="small" @click="toEdit('')"
          >新增</el-button
        >
        <exportExcelBtn
          url="fm/batchPaymentRequest/export"
          :params="{
            pageNum: pageIndex,
            pageSize: pageSize,
            depCode: depCode ? depCode : null,
            ...searchForm,
          }"
          file-name="批量付款申请"
        >
        </exportExcelBtn>
      </el-row>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="getList"
        @prev-click="getList"
        @next-click="getList"
        :page-sizes="[20, 30, 40, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        background
      >
      </el-pagination>
    </div>

    <div class="content-table">
      <el-table
        :data="tableData"
        height="100%"
        style="width: 100%"
        class="table-box"
        @selection-change="handleSelectionChange"
        @expand-change="handleExpandChange"
        border
      >
        <el-table-column type="expand">
          <template slot-scope="pScope">
            <el-table :data="pScope.row.details" border style="width: 100%">
              <el-table-column type="selection" width="55"> </el-table-column>
              <el-table-column type="index" label="序号" width="80">
              </el-table-column>
              <el-table-column label="付款编号" width="150">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.paymentRequestCode || "-" }}</p>
                </template>
              </el-table-column>
              <el-table-column label="供应商名称" width="150">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.supplierName || "-" }}</p>
                </template>
              </el-table-column>
              <el-table-column label="结算单号" width="150">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.settleCode || "-" }}</p>
                </template>
              </el-table-column>
              <el-table-column label="本期结算金额">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.settleMoney | formatPrice }}</p>
                </template>
              </el-table-column>
              <el-table-column label="本期发票金额">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.invoiceMoney | formatPrice }}</p>
                </template>
              </el-table-column>
              <el-table-column label="本期付款比例">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.paymentRatio | formatPrice }}%</p>
                </template>
              </el-table-column>
              <el-table-column label="本期付款金额">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.paymentMoney | formatPrice }}</p>
                </template>
              </el-table-column>
              <el-table-column label="付款类型" width="120">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.paymentType || "-" }}</p>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination
              style="margin-top: 10px; padding: unset"
              small
              background
              layout="prev, pager, next"
              :total="pScope.row.total"
              :current-page.sync="pScope.row.pageIndex"
              :page-size="pageCSize"
              @current-change="getDetails($event, pScope.$index)"
              @prev-click="getDetails($event, pScope.$index)"
              @next-click="getDetails($event, pScope.$index)"
            >
            </el-pagination>
          </template>
        </el-table-column>
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="序号" type="index"> </el-table-column>
        <el-table-column label="批量付款编号" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.batchCode || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="付款单数">
          <template slot-scope="scope">
            <span>{{ scope.row.num || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="供应商名称" width="150">
          <template slot-scope="pScope">
            <p>{{ pScope.row.supplierName || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="合同总金额">
          <template slot-scope="scope">
            <p>{{ scope.row.contractMoney || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="结算总金额">
          <template slot-scope="scope">
            <span>{{ scope.row.settleMoney || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="收票总金额">
          <template slot-scope="scope">
            <span>{{ scope.row.invoiceMoney || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="单据类型">
          <template slot-scope="scope">
            <p>{{ scope.row.batchPrType | getDJstaut }}</p>
          </template>
        </el-table-column>
        <!-- <el-table-column label="付款总金额">
          <template slot-scope="scope">
            <span>{{ scope.row.totalSettlePriceBeforeTaxes || "-" }}</span>
          </template>
        </el-table-column> -->
        <el-table-column label="操作人">
          <template slot-scope="scope">
            <p>{{ scope.row.createBy || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="审批状态">
          <template slot-scope="scope">
            <el-tag :type="scope.row.flowStatus | getFlowType">{{
              scope.row.flowStatus | getFlowStatus
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="付款状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status == 1" type="warning"
              >部分付款</el-tag
            >
            <el-tag v-else-if="scope.row.status == 2" type="success"
              >完成付款</el-tag
            >
            <el-tag v-else type="danger">未付款</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作时间" width="150">
          <template slot-scope="scope">
            <p>{{ scope.row.updateTime || scope.row.createTime || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template slot-scope="scope">
            <el-button
              v-if="
                [1, 3, 5].includes(scope.row.flowStatus) &&
                [1, 2].includes(scope.row.createFlag)
              "
              @click.native.prevent="toEdit(scope.row.id)"
              type="primary"
              plain
              size="small"
            >
              编辑
            </el-button>
            <el-button
              v-else
              @click.native.prevent="toEdit(scope.row.id)"
              type="primary"
              plain
              size="small"
            >
              详情
            </el-button>
            <!-- <el-button
              v-if="[1, 3].includes(scope.row.flowStatus) &&  [1, 2].includes(scope.row.createFlag)"
              type="primary"
              plain
              size="small"
              @click="submitAudit(scope.row.id, scope.$index)"
            >
              提审
            </el-button> -->
            <template>
              <!-- <el-button
                v-if="[2].includes(scope.row.flowStatus) && [1, 2].includes(scope.row.createFlag)&& scope.row.taskId"
                type="danger"
                plain
                size="small"
                @click="submitRevocation(scope.row)"
              >
                撤审
              </el-button>
              <el-button
                v-if="[2].includes(scope.row.flowStatus) && scope.row.taskId &&[0, 2].includes(scope.row.createFlag)"
                type="primary"
                plain
                size="small"
                @click="selectAudit(scope.row)"
              >
                审核
              </el-button> -->
            </template>
            <!-- <el-button
              v-if="[4].includes(scope.row.flowStatus) && scope.row.createFlag ==1"
              type="warning"
              plain
              size="small"
              @click="submitAbandon(scope.row)"
            >
              弃审
            </el-button> -->
            <!-- <el-button
               v-if="[2, 3, 4].includes(scope.row.flowStatus)&&scope.row.createFlag ==1"
              type="primary"
              plain
              size="small"
              @click="handleHistory(scope.row.flowCode)"
            >
              审核历史
            </el-button> -->

            <el-button
              v-if="
                [4].includes(scope.row.flowStatus) && scope.row.fillPermission
              "
              @click.native.prevent="submitPay(scope.row)"
              type="primary"
              plain
              size="small"
            >
              确认付款
            </el-button>
            <el-button
              v-if="
                [1, 3, 5].includes(scope.row.flowStatus) &&
                [1, 2].includes(scope.row.createFlag)
              "
              type="danger"
              plain
              size="small"
              @click="deleteRow(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--    <WDialog
      :value="showPay"
      title="确认付款"
      width="450px"
      @cancel="cancelModal"
      @ok="submitRow('payForm')"
    >
      <el-form label-width="auto" :model="payForm" ref="payForm">
        <el-form-item label="合同总金额" prop="contractMoney">
          <span>{{ payForm.contractMoney || 0 }}</span>
        </el-form-item>
        <el-form-item label="收票总金额" prop="invoiceMoney">
          <span>{{ payForm.invoiceMoney || 0 }}</span>
        </el-form-item>
        <el-form-item label="结算总金额" prop="settleMoney">
          <span>{{ payForm.settleMoney || 0 }}</span>
        </el-form-item>
        <el-form-item
          label="本次付款金额"
          prop="payMoney"
          :rules="[
            {
              required: true,
              message: '请输入付款金额',
              trigger: 'blur',
            },
          ]"
        >
          <el-input
            v-model.number="payForm.payMoney"
            placeholder="请输入"
            onkeyup="value=value.replace(/[^\d\.]/g,'')"
          ></el-input>
        </el-form-item>
      </el-form>
    </WDialog> -->
    <paylistBach
      :show.sync="showPay"
      title="付款记录"
      :data="payinfo"
      @getMaterial="getList"
    >
    </paylistBach>

    <Audit
      :data="auditInfo"
      :showAudit.sync="showAudit"
      :showProcess.sync="showProcess"
      :code="code"
    />
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { setFlowStatus } from "@/utils/enumStatus";
import {
  getPSBPage,
  getPSBDetails,
  submitPSBAudit,
  deletePSB,
  confirmPSBPayment,
} from "@/api/finance";
import audit from "@/mixins/audit";
import paylistBach from "./components/paylistBach";

export default {
  name: "fund-settleBatch",
  mixins: [audit],
  data() {
    return {
      searchForm: {
        batchCode: "",
        createTime: "",
        createTimeStart: "",
        createTimeEnd: "",
        createBy: "",
        status: "",
        flowStatus: "",
        supplierCode: "",
      },
      flowStatus: setFlowStatus(),
      tableData: [],
      engineeringPlanCode: null,
      batchCode: null,
      total: 0,
      pageSize: 20,
      pageIndex: 1,
      pageCSize: 5,
      multipleSelection: [],
      payForm: {
        code: "",
        payMoney: "",
      },
      depCode: "",
      showPay: false,
      payinfo: {},
    };
  },

  filters: {
    getPaymentType(type) {
      switch (type) {
        case 1:
          return "预付款";
        case 2:
          return "进度款";
        case 3:
          return "结算款";
        case 4:
          return "质保金";
        default:
          return "-";
      }
    },
  },
  components: { paylistBach },

  computed: {
    ...mapState("optionInfo", ["userList", "supList"]),
    ...mapState("user", ["info"]),
  },

  mounted() {
    this.getList();
    this.getSupplier();
    this.getUsers();
    this.depCode = JSON.parse(localStorage.getItem("depInfo")).depCode;
  },

  methods: {
    ...mapActions("optionInfo", ["getUsers", "getSupplier"]),

    getList(index = 1) {
      let searchInfo = JSON.parse(JSON.stringify(this.searchForm));
      delete searchInfo.createTime;
      getPSBPage({
        dataType: 3,
        pageNum: index,
        pageSize: this.pageSize,
        ...searchInfo,
      }).then((res) => {
        const { records, total } = res.data;
        this.pageIndex = index;
        if (records) {
          this.pageIndex = index;
          records.forEach((ele) => {
            ele.details = [];
            ele.detailTotal = 0;
            ele.pageIndex = 1;
          });
        }
        this.tableData = records || [];
        this.total = total;
      });
    },

    handleSizeChange(val) {
      this.pageSize = val;
      this.getList();
    },

    searchSubmit() {
      const { createTime } = this.searchForm;
      if (createTime) {
        this.searchForm.createTimeStart = createTime[0];
        this.searchForm.createTimeEnd = createTime[1];
      }
      this.getList(1);
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.searchForm.createTimeStart = "";
      this.searchForm.createTimeEnd = "";
      (this.searchForm.createTime = ""),
        (this.searchForm.batchCode = ""),
        this.getList();
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    handleExpandChange(val) {
      const index = this.tableData.findIndex((ele) => ele.id == val.id);
      if (index == -1) return;
      this.$nextTick(() => {
        this.batchCode = val.batchCode;
        this.getDetails(1, index);
      });
    },

    getDetails(pageIndex, index) {
      getPSBDetails({
        pageNum: pageIndex,
        pageSize: this.pageCSize,
        batchCode: this.batchCode,
      }).then((res) => {
        const { records, total } = res.data;
        this.tableData[index].details = records || [];
        this.tableData[index].total = total;
        this.tableData[index].pageIndex = pageIndex;
        this.$forceUpdate();
      });
    },

    toEdit(id = "") {
      let url = "/fund/settleBatch/edit";
      if (id) {
        url += `?id=${id}`;
      }
      this.$router.push(url);
    },

    submitAudit(id, index) {
      this.$confirm("确定要提交审核?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        submitPSBAudit({ id }).then((res) => {
          this.$message.success("提审成功");
          this.getList(this.pageIndex);
        });
      });
    },

    submitPay(info) {
      this.showPay = true;
      console.log(info, "详情");
      // this.$nextTick(() => {
      this.payForm = {
        code: info.batchCode,
        contractMoney: info.contractMoney,
        settleMoney: info.settleMoney,
        invoiceMoney: info.invoiceMoney,
        payMoney: "",
      };
      this.payinfo = {
        ...info,
        payMoney: "",
        payForm: this.payForm,
      };
      // });
    },

    submitRow(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          confirmPSBPayment({
            code: this.payForm.code,
            payMoney: this.payForm.payMoney,
          }).then((res) => {
            this.cancelModal();
            this.$message.success("操作成功!");
            this.getList();
          });
        } else {
          return false;
        }
      });
    },

    cancelModal() {
      this.$refs.payForm.resetFields();
      this.payForm.code = "";
      this.showPay = false;
    },

    deleteRow(id) {
      this.$confirm("确定要删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        deletePSB({ id }).then((res) => {
          this.$message.success("删除成功！");
          this.getList(this.pageIndex);
        });
      });
    },
  },
};
</script>

<style lang="less" scoped>
/deep/ .el-table__expanded-cell,
.el-table__expanded-cell:hover {
  background: #f7f9fc;
}
</style>
