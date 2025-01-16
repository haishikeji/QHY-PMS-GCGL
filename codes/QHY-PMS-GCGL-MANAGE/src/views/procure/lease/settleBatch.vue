<template>
  <div>
    <el-form
      :inline="true"
      label-width="120px"
      :model="searchForm"
      class="search-form"
      ref="searchForm"
    >
      <el-form-item label="结算单号" prop="batchSettleCode">
        <el-input
          v-model="searchForm.batchSettleCode"
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
      <el-form-item label="创建人" prop="createBy">
        <!-- <el-select
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
      <el-form-item label="流程状态" prop="flowStatus">
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
        <!-- <el-button type="primary" size="small" @click="toEdit('')"
          >新增</el-button
        > -->
        <!--导出-->
        <exportExcelBtn
          url="pm/batchSettle/export"
          :params="{
            pageNum: pageIndex,
            pageSize: pageSize,
            dataType: 3,
            ...searchForm,
          }"
          :disabled="!tableData.length"
          file-name="租赁合同批量结算"
        >
        </exportExcelBtn>
        <PushDown
          typeKey="租赁合同批量结算"
          :data="{ ...multipleSelection[0], isSettle: true }"
          :disabled="!(multipleSelection.length == 1)"
          size="small"
        />
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
        border
        height="100%"
        :data="tableData"
        style="width: 100%"
        class="table-box"
        @selection-change="handleSelectionChange"
        @expand-change="handleExpandChange"
      >
        <el-table-column type="expand">
          <template slot-scope="pScope">
            <el-table :data="pScope.row.details" border style="width: 100%">
              <el-table-column type="selection" width="55"> </el-table-column>
              <el-table-column type="index" label="序号" width="80">
              </el-table-column>
              <el-table-column label="合同编号" width="150">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.dataCode || "-" }}</p>
                </template>
              </el-table-column>
              <el-table-column label="供应商名称" width="150">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.supplierName || "-" }}</p>
                </template>
              </el-table-column>
              <el-table-column label="项目名称" width="230">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.projectName || "-" }}</p>
                </template>
              </el-table-column>
              <el-table-column label="项目编号">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.projectCode || "-" }}</p>
                </template>
              </el-table-column>
              <el-table-column label="合同金额">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.orderMoneyBeforeTaxes | formatPrice }}</p>
                </template>
              </el-table-column>
              <el-table-column label="结算金额">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.settleMoneyBeforeTaxes | formatPrice }}</p>
                </template>
              </el-table-column>
              <el-table-column label="结算类型">
                <template slot-scope="pScope">
                  <p v-if="pScope.row.settleType == 1">进度结算</p>
                  <p v-else-if="pScope.row.settleType == 2">完工结算</p>
                  <p v-else>-</p>
                </template>
              </el-table-column>
              <el-table-column label="成本科目" width="120">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.costSubject || "-" }}</p>
                </template>
              </el-table-column>
              <el-table-column label="是否收票" width="120">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.invoiceFlag ? "是" : "否" }}</p>
                </template>
              </el-table-column>
              <el-table-column label="收票金额" width="120">
                <template slot-scope="pScope">
                  <p>{{ pScope.row.currentReceiveMoney || "-" }}</p>
                </template>
              </el-table-column>
              <!-- <el-table-column label="操作" width="120">
			    <template slot-scope="pScope">
			     <el-button
			       type="primary"
			       plain
			       size="small"
			       @click="showpop(pScope.row.settleCode)"
			     >
			       收票管理
			     </el-button>
			    </template>
			  </el-table-column> -->
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
        <el-table-column label="批量结算单号" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.batchSettleCode || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="结算单数">
          <template slot-scope="scope">
            <span>{{ scope.row.num || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="供应商名称" width="150">
          <template slot-scope="pScope">
            <p>{{ pScope.row.supplierName || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="总金额">
          <template slot-scope="scope">
            <p>{{ scope.row.totalPrice || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="收票总金额">
          <template slot-scope="scope">
            <span>{{ scope.row.totalReceiveMoney || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="结算总金额">
          <template slot-scope="scope">
            <span>{{ scope.row.totalSettlePriceBeforeTaxes || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建人">
          <template slot-scope="scope">
            <p>{{ scope.row.createBy || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="流程状态">
          <template slot-scope="scope">
            <el-tag :type="scope.row.flowStatus | getFlowType">{{
              scope.row.flowStatus | getFlowStatus
            }}</el-tag>
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
            <!--            <el-button
              v-if="[1, 3].includes(scope.row.flowStatus) &&  [1, 2].includes(scope.row.createFlag)"
              type="primary"
              plain
              size="small"
              @click="submitAudit(scope.row.id, scope.$index)"
            >
              提审
            </el-button>
            <template
              
            >
              <el-button
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
              </el-button>
            </template> -->
            <!-- <el-button
              v-if="[4].includes(scope.row.flowStatus) && scope.row.createFlag ==1"
              type="warning"
              plain
              size="small"
              @click="submitAbandon(scope.row)"
            >
              弃审
            </el-button> -->
            <!--       <el-button
               v-if="[2, 3, 4].includes(scope.row.flowStatus)&&scope.row.createFlag ==1"
              type="primary"
              plain
              size="small"
              @click="handleHistory(scope.row.flowCode)"
            >
              审核历史
            </el-button> -->
            <el-button
              type="danger"
              plain
              size="small"
              v-if="
                [1, 2].includes(scope.row.createFlag) &&
                [1, 3, 5].includes(scope.row.flowStatus)
              "
              @click.native.prevent="deleteRow(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog title="" :visible.sync="fellref">
      <CommanageEdit
        @offdlo="offfellref($event)"
        :type="3"
        :getsettleCode="settleCode"
      ></CommanageEdit>
    </el-dialog>

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
  getBSettlePage,
  getBSettleDetails,
  submitBSettleAudit,
  removeBSettle,
} from "@/api/procure";
import audit from "@/mixins/audit";

export default {
  name: "",
  mixins: [audit],
  data() {
    return {
      searchForm: {
        purchaseContractCode: "",
        projectName: "",
        projectCode: "",
        createTimeStart: "",
        createTimeEnd: "",
        createTime: "",
        supplierCode: "",
        createBy: "",
        settleCode: "",
        flowStatus: "",
        batchSettleCode: "",
      },
      flowStatus: setFlowStatus(),
      tableData: [],
      engineeringPlanCode: null,
      total: 0,
      pageSize: 20,
      pageIndex: 1,
      pageCSize: 5,
      batchSettleCode: null,
      multipleSelection: [],
      ids: [],
      fellref: false,
      settleCode: "",
    };
  },

  computed: {
    ...mapState("optionInfo", ["userList", "supList"]),
    ...mapState("user", ["info"]),
  },

  mounted() {
    this.getList();
    this.getSupplier();
    this.getUsers();
  },

  methods: {
    ...mapActions("optionInfo", ["getUsers", "getSupplier"]),

    getList(index = 1) {
      let searchInfo = JSON.parse(JSON.stringify(this.searchForm));
      delete searchInfo.createTime;
      getBSettlePage({
        dataType: 3,
        pageNum: index,
        pageSize: this.pageSize,
        ...searchInfo,
      }).then((res) => {
        const { records, total } = res.data;
        this.pageIndex = index;
        if (records && records.length) {
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
      this.searchForm.createTimeStart = "";
      this.searchForm.createTimeEnd = "";
      this.searchForm.createTime = "";
      this.searchForm.batchSettleCode = "";
      this.$refs[formName].resetFields();
      this.getList();
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.ids = val.map((ele) => ele.id);
    },

    handleExpandChange(val) {
      const index = this.tableData.findIndex((ele) => ele.id == val.id);
      if (index == -1) return;
      this.$nextTick(() => {
        this.batchSettleCode = val.batchSettleCode;
        this.getDetails(1, index);
      });
    },

    getDetails(pageIndex, index) {
      getBSettleDetails({
        pageNum: pageIndex,
        pageSize: this.pageCSize,
        batchSettleCode: this.batchSettleCode,
      }).then((res) => {
        const { records, total } = res.data;
        this.tableData[index].details = records || [];
        this.tableData[index].total = total;
        this.tableData[index].pageIndex = pageIndex;
        this.$forceUpdate();
      });
    },

    toEdit(id = "") {
      let url = "/lease/settleBatch/edit";
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
        submitBSettleAudit({ id }).then((res) => {
          this.$message.success("提审成功");
          this.getList(this.pageIndex);
        });
      });
    },

    deleteRow(id) {
      this.$confirm("确定要删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        removeBSettle({ id }).then((res) => {
          this.$message.success("删除成功");
          this.getList(this.pageIndex);
        });
      });
    },
    // 收票登记
    offfellref(val) {
      this.fellref = false;
      this.getList(this.pageIndex);
    },

    showpop(settleCode) {
      this.fellref = true;
      this.settleCode = settleCode;
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
