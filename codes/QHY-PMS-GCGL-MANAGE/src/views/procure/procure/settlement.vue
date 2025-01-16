<template>
  <div>
    <el-form
      :inline="true"
      label-width="120px"
      :model="searchForm"
      class="search-form"
      ref="searchForm"
    >
      <el-form-item label="合同编号" prop="dataCode">
        <el-input v-model="searchForm.dataCode" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input
          v-model="searchForm.projectName"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item label="项目编号" prop="projectCode">
        <el-input
          v-model="searchForm.projectCode"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item label="结算申请日期" prop="createTime">
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
      <el-form-item class="form-btns">
        <el-button type="primary" @click="searchSubmit">查询</el-button>
        <el-button @click="resetForm('searchForm')">重置</el-button>
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
      <el-form-item label="创建人" prop="createBy">
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
      <el-form-item label="结算编号" prop="settleCode">
        <el-input
          v-model="searchForm.settleCode"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item label="集采结算编号" prop="batchPurchaseContractSettleCode">
        <el-input
          v-model="searchForm.batchPurchaseContractSettleCode"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
    </el-form>

    <div class="pagination-box">
      <el-row>
        <el-button type="primary" size="small" @click="toEdit('')"
          >新增</el-button
        >
        <importByType :type="10" @import_success="getList(1)"></importByType>
        <exportExcelBtn
          url="pm/purchaseSettle/export"
          :params="{
            pageNum: pageIndex,
            pageSize: pageSize,
            dataType: 2,
            depCode: depCode ? depCode : null,
            ...searchForm,
          }"
          file-name="采购合同结算"
        >
        </exportExcelBtn>
        <PushDown
          typeKey="采购合同结算"
          :data="{ ...multipleSelection[0], dataType: 2 }"
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
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="序号" type="index"> </el-table-column>
        <el-table-column label="结算单号" show-overflow-tooltip min-width="230">
          <template slot-scope="scope">
            <span>{{ scope.row.settleCode || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="合同编号" width="230" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.dataCode || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="供应商名称" width="230">
          <template slot-scope="scope">
            <p>{{ scope.row.supplierName || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="项目名称" width="230" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.projectName || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目编号" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.projectCode || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单金额" width="150">
          <template slot-scope="scope">
            <p>{{ scope.row.orderMoneyBeforeTaxes | formatPrice }}</p>
          </template>
        </el-table-column>
        <el-table-column label="结算金额" width="150">
          <template slot-scope="scope">
            <p>{{ scope.row.settleMoneyBeforeTaxes | formatPrice }}</p>
          </template>
        </el-table-column>
        <el-table-column label="创建人">
          <template slot-scope="scope">
            <p>{{ scope.row.createBy || "-" }}</p>
          </template>
        </el-table-column>
        <!--     <el-table-column label="生效状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status" type="success">已生效</el-tag>
            <el-tag v-else type="info">未生效</el-tag>
          </template>
        </el-table-column> -->
        <el-table-column label="流程状态">
          <template slot-scope="scope">
            <el-tag :type="scope.row.flowStatus | getFlowType">{{
              scope.row.flowStatus | getFlowStatus
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="是否批量结算" width="200">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.batchFlag" type="success">是</el-tag>
            <el-tag v-else type="info">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="收票状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.invoiceFlag" type="success">是</el-tag>
            <el-tag v-else type="info">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="收票金额">
          <template slot-scope="scope">
            <p>{{ scope.row.invoiceMoneyBeforeTaxes || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="更新时间" width="150">
          <template slot-scope="scope">
            <p>{{ scope.row.updateTime || scope.row.createTime || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
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
            <!--       <el-button
              v-if="[1, 3].includes(scope.row.flowStatus) &&  [1, 2].includes(scope.row.createFlag)"
              type="primary"
              plain
              size="small"
              @click="submitAudit(scope.row.id, scope.$index)"
            >
              提审
            </el-button> -->
            <template>
              <!--         <el-button
                v-if="[2].includes(scope.row.flowStatus) && [1, 2].includes(scope.row.createFlag)&& scope.row.taskId"
                type="danger"
                plain
                size="small"
                @click="submitRevocation(scope.row)"
              >
                撤审
              </el-button> -->
              <!--              <el-button
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
            <!--        <el-button
               v-if="[2, 3, 4].includes(scope.row.flowStatus)&&scope.row.createFlag ==1"
              type="primary"
              plain
              size="small"
              @click="handleHistory(scope.row.flowCode)"
            >
              审核历史
            </el-button> -->
            <el-button
              type="primary"
              plain
              size="small"
              @click="showpop(scope.row.settleCode)"
            >
              收票管理
            </el-button>
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
        :type="2"
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
import { getSettlePage, submitSettleAudit, removeSettle } from "@/api/procure";
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
        createTime: "",
        supplierCode: "",
        createBy: "",
        settleCode: "",
        flowStatus: "",
        batchPurchaseContractSettleCode: "",
      },
      flowStatus: setFlowStatus(),
      tableData: [],
      engineeringPlanCode: null,
      total: 0,
      pageSize: 20,
      pageIndex: 1,
      multipleSelection: [],
      fellref: false,
      settleCode: "",
      depCode: "",
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
    this.depCode = JSON.parse(localStorage.getItem("depInfo")).depCode;
  },

  methods: {
    ...mapActions("optionInfo", ["getUsers", "getSupplier"]),

    getList(index = 1) {
      let searchInfo = JSON.parse(JSON.stringify(this.searchForm));
      delete searchInfo.createTime;
      getSettlePage({
        dataType: 2,
        pageNum: index,
        pageSize: this.pageSize,
        ...searchInfo,
      }).then((res) => {
        const { records, total } = res.data;
        this.pageIndex = index;
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
      this.getList();
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    toEdit(id = "") {
      let url = "/procure/settlement/edit";
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
        submitSettleAudit({ id }).then((res) => {
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
        removeSettle({ id }).then((res) => {
          this.$message.success("删除成功");
          this.getList(this.pageIndex);
        });
      });
    },

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
