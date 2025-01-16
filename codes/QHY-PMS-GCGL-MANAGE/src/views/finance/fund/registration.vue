<template>
  <div>
    <el-form
      :inline="true"
      label-width="120px"
      :model="searchForm"
      class="search-form"
      ref="searchForm"
    >
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
      <el-form-item label="合同编号" prop="contractCode">
        <el-input
          v-model="searchForm.contractCode"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item label="经办人" prop="createBy">
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
      <el-form-item label="收款编号" prop="receiptRegisterCode">
        <el-input
          v-model="searchForm.receiptRegisterCode"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item label="客户名称" prop="customerName">
        <el-input
          v-model="searchForm.customerName"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item label="收款金额" prop="receivePaymentMoney">
        <el-input
          v-model="searchForm.receivePaymentMoney"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item label="实际到账金额" prop="actualReceiveMoney">
        <el-input
          v-model="searchForm.actualReceiveMoney"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item label="到账日期" prop="receivedDate">
        <el-date-picker
          v-model="searchForm.receivedDate"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          style="width: 250px"
        >
        </el-date-picker>
      </el-form-item>
    </el-form>

    <div class="pagination-box">
      <el-row>
        <el-button type="primary" size="small" @click="toEdit('')"
          >新增</el-button
        >
        <!--导出-->
        <exportExcelBtn
          style="margin-left: 10px"
          url="fm/receiptRegister/export"
          :params="{
            pageNum: pageIndex,
            pageSize: pageSize,
            ...searchForm,
          }"
          :disabled="!tableData.length"
          file-name="工程收款登记"
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
        border
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="序号" type="index"> </el-table-column>
        <el-table-column label="收款编号" width="150">
          <template slot-scope="scope">
            <p>{{ scope.row.receiptRegisterCode || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="客户名称">
          <template slot-scope="scope">
            <p>{{ scope.row.customerName || "-" }}</p>
          </template>
        </el-table-column>
        <!-- <el-table-column label="收款日期">
          <template slot-scope="scope">
            <p>{{ scope.row.receivedDate || "-" }}</p>
          </template>
        </el-table-column> -->
        <el-table-column label="项目名称" width="230">
          <template slot-scope="scope">
            <p>{{ scope.row.projectName || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="项目编号" width="170">
          <template slot-scope="scope">
            <p>{{ scope.row.projectCode || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="合同编号" width="170">
          <template slot-scope="scope">
            <p>{{ scope.row.contractCode || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="合同金额" width="150">
          <template slot-scope="scope">
            <p>{{ scope.row.contractMoney | formatPrice }}</p>
          </template>
        </el-table-column>
        <el-table-column label="收款金额" width="180">
          <template slot-scope="scope">
            <p>{{ scope.row.receivePaymentMoney | formatPrice }}</p>
          </template>
        </el-table-column>
        <el-table-column label="实际到账金额" width="120">
          <template slot-scope="scope">
            <p>{{ scope.row.actualReceiveMoney | formatPrice }}</p>
          </template>
        </el-table-column>
        <el-table-column label="手续费">
          <template slot-scope="scope">
            <p>{{ scope.row.serviceCharge | formatPrice }}</p>
          </template>
        </el-table-column>
        <el-table-column label="罚款金额">
          <template slot-scope="scope">
            <p>{{ scope.row.forfeitMoney | formatPrice }}</p>
          </template>
        </el-table-column>
        <el-table-column label="累计收款金额（含本次）" width="180">
          <template slot-scope="scope">
            <p>{{ scope.row.totalGatheringMoney | formatPrice }}</p>
          </template>
        </el-table-column>
        <el-table-column label="累计收款比例（含本次）" width="180">
          <template slot-scope="scope">
            <p>{{ scope.row.totalReceiveRatio | formatPrice }}</p>
          </template>
        </el-table-column>
        <!-- <el-table-column label="经办人">
          <template slot-scope="scope">
            <p>{{ scope.row.createBy || "-" }}</p>
          </template>
        </el-table-column> -->
        <!--     <el-table-column label="流程状态">
          <template slot-scope="scope">
            <el-tag :type="scope.row.flowStatus | getFlowType">{{ scope.row.flowStatus | getFlowStatus }}</el-tag>
          </template>
        </el-table-column> -->
        <!-- <el-table-column label="备注">
          <template slot-scope="scope">
            <p>{{ scope.row.projectName || "-" }}</p>
          </template>
        </el-table-column> -->
        <el-table-column label="操作" width="240" fixed="right">
          <template slot-scope="scope">
            <el-button
              v-if="[1, 2].includes(scope.row.createFlag)"
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
            </el-button>
            <template >
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
            </template>
                  <el-button
              v-if="[4].includes(scope.row.flowStatus) && scope.row.createFlag ==1"
              type="warning"
              plain
              size="small"
              @click="submitAbandon(scope.row)"
            >
              弃审
            </el-button>
            <el-button
               v-if="[2, 3, 4].includes(scope.row.flowStatus)&&scope.row.createFlag ==1"
              type="primary"
              plain
              size="small"
              @click="handleHistory(scope.row.flowCode)"
            >
              审核历史
            </el-button> -->
            <el-button
              v-if="[1, 2].includes(scope.row.createFlag)"
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
import { getRegPage, deleteReg, submitRegAudit } from "@/api/finance";
import { setFlowStatus } from "@/utils/enumStatus";
import audit from "@/mixins/audit";

export default {
  name: "",
  mixins: [audit],
  data() {
    return {
      searchForm: {
        projectName: "",
        projectCode: "",
        contractCode: "",
        createBy: "",
        receiptRegisterCode: "",
        customerName: "",
        receivePaymentMoney: "",
        actualReceiveMoney: "",
        receivedDate: "",
        receivedDateStart: "",
        receivedDateEnd: "",
      },
      flowStatus: setFlowStatus(),
      tableData: [],
      index: 0,
      total: 0,
      pageSize: 20,
      pageIndex: 1,
      multipleSelection: [],
    };
  },

  computed: {
    ...mapState("optionInfo", ["userList", "dicObj"]),
  },

  mounted() {
    this.getList();
    this.getDictionary({
      name: "发票类型",
      key: "invoiceType",
    });
    this.getUsers();
  },

  methods: {
    ...mapActions("optionInfo", ["getUsers", "getDictionary"]),

    getList(index = 1) {
      let searchInfo = JSON.parse(JSON.stringify(this.searchForm));
      delete searchInfo.receivedDate;
      getRegPage({
        pageNum: index,
        pageSize: this.pageSize,
        ...searchInfo,
      }).then((res) => {
        this.pageIndex = index;
        const { records, total } = res.data;
        this.tableData = records || [];
        this.total = total;
      });
    },

    handleSizeChange(val) {
      this.pageSize = val;
      this.getList();
    },

    searchSubmit() {
      const { receivedDate } = this.searchForm;
      if (receivedDate) {
        this.searchForm.receivedDateStart = receivedDate[0];
        this.searchForm.receivedDateEnd = receivedDate[1];
      }
      this.getList(1);
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.searchForm.receivedDateStart = "";
      this.searchForm.receivedDateEnd = "";
      this.getList();
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    toEdit(id, isEdit = 1) {
      let url = "/fund/registration/edit";
      if (id) {
        url += `?id=${id}&isEdit=${isEdit}`;
      }
      this.$router.push(url);
    },

    submitAudit(id, index) {
      this.$confirm("确定要提交审核?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        submitRegAudit({ id }).then((res) => {
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
        deleteReg({ id }).then((res) => {
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
