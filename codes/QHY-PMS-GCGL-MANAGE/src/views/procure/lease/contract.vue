<template>
  <div>
    <el-form
      :inline="true"
      label-width="120px"
      :model="searchForm"
      class="search-form"
      ref="searchForm"
    >
      <el-form-item label="合同编号" prop="leaseContractCode">
        <el-input
          v-model="searchForm.leaseContractCode"
          placeholder="请输入"
        ></el-input>
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
      <el-form-item label="创建日期" prop="createTime">
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
    </el-form>

    <div class="pagination-box">
      <el-row>
        <el-button type="primary" size="small" @click="toEdit('')"
          >新增</el-button
        >
        <!--导出-->
        <exportExcelBtn
          style="margin: 0 10px"
          url="pm/leaseContract/export"
          :params="{
            pageNum: pageIndex,
            pageSize: pageSize,
            ...searchForm,
          }"
          :disabled="!tableData.length"
          file-name="租赁合同"
        >
        </exportExcelBtn>
        <!-- <el-button
          :disabled="!tableData.length"
          type="primary"
          plain
          size="small"
          >变更</el-button
        > -->
        <PushDown
          typeKey="租赁合同"
          :data="{ ...multipleSelection[0], dataType: 3, dow: 1, prType: 2 }"
          size="small"
          :style="multipleSelection.length == 1 ? '' : 'display: none'"
        />
        <PushDown
          :data="ids"
          size="small"
          :batchType="4"
          :style="multipleSelection.length != 1 ? '' : 'display: none'"
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
        @select="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="合同编号" width="180">
          <template slot-scope="scope">
            <p>{{ scope.row.leaseContractCode || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="项目编号" width="180">
          <template slot-scope="scope">
            <p>{{ scope.row.projectCode || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="项目名称" width="230">
          <template slot-scope="scope">
            <p>{{ scope.row.projectName || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="供应商名称" width="200">
          <template slot-scope="scope">
            <p>{{ scope.row.supplierName || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="租赁金额">
          <template slot-scope="scope">
            <p>{{ scope.row.contractMoneyBeforeTaxes | formatPrice }}</p>
          </template>
        </el-table-column>
        <el-table-column label="签约时间">
          <template slot-scope="scope">
            <p>{{ scope.row.signDate || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="租赁计划编号">
          <template slot-scope="scope">
            <p>{{ scope.row.engineeringPlanCode || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="流程状态">
          <template slot-scope="scope">
            <el-tag :type="scope.row.flowStatus | getFlowType">{{
              scope.row.flowStatus | getFlowStatus
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间">
          <template slot-scope="scope">
            <p>{{ scope.row.createTime || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="创建人">
          <template slot-scope="scope">
            <p>{{ scope.row.createBy || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260">
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
            <!--   <el-button
              v-if="[4].includes(scope.row.flowStatus) && scope.row.startFlag"
              type="primary"
              plain
              size="small"
              @click="toEdit(scope.row.id, 1)"
            >
              变更
            </el-button> -->
            <!-- <el-button
			  type="primary"
			  plain
			  size="small"
			  @click="showpop(scope.row.leaseContractCode)"
			>
			  收票管理
			</el-button> -->
            <el-button
              v-if="
                [1, 3, 5].includes(scope.row.flowStatus) &&
                [1, 2].includes(scope.row.createFlag)
              "
              type="danger"
              plain
              size="small"
              @click="deleteRow(scope.row.id, scope.$index)"
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
        :settleType="0"
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
  getLConPage,
  settleLConBatch,
  submitLConAudit,
  settleLConApply,
  deleteLCon,
} from "@/api/procure";
import Plan from "@/components/common/Plan.vue";
import audit from "@/mixins/audit";
export default {
  name: "lease",
  components: {
    Plan,
  },
  data() {
    return {
      searchForm: {
        leaseContractCode: "",
        projectCode: "",
        createTime: "",
        supplierCode: "",
        createBy: "",
        projectName: "",
      },
      flowStatus: setFlowStatus(),
      tableData: [],
      engineeringPlanCode: null,
      index: 0,
      total: 0,
      pageSize: 20,
      pageIndex: 1,
      multipleSelection: [],
      ids: [],
      showPlan: false,
      fellref: false,
      settleCode: "",
    };
  },

  computed: {
    ...mapState("optionInfo", ["userList", "supList"]),
    ...mapState("user", ["info"]),
  },

  mixins: [audit],

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
      getLConPage({
        pageNum: index,
        pageSize: this.pageSize,
        ...searchInfo,
      }).then((res) => {
        this.pageIndex = index;
        const { records, total } = res.data;
        if (records) {
          records.forEach((ele) => {
            ele.details = [];
            ele.detailTotal = 0;
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
      this.getList();
    },

    handleSelectionChange(val) {
      this.multipleSelection = val.map((ele) => {
        return {
          ...ele,
          dataCode: ele.leaseContractCode,
        };
      });
      this.ids = val.map((ele) => {
        return {
          id: ele.id,
          flowStatus: ele.flowStatus,
          batchPrType: 3,
        };
      });
    },

    toEdit(id = "", changeFlag = 0) {
      let url = "/lease/contract/edit";
      if (id) {
        url += `?id=${id}`;
      }
      if (changeFlag) {
        url += `&changeFlag=${changeFlag}`;
      }
      this.$router.push(url);
    },

    submitAudit(id, index) {
      this.$confirm("该合同涉及施工预算，确定要提交审核?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        submitLConAudit({ id, comfirmFlag: 1 }).then((res) => {
          this.$message.success("提审成功");
          this.getList(this.pageIndex);
        });
      });
    },

    settleApply(id) {
      this.$confirm("确定要结算申请?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        settleLConApply({ id }).then((res) => {
          this.$message.success("申请成功");
        });
      });
    },

    deleteRow(id, index) {
      this.$confirm("确定要删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        deleteLCon({ id }).then((res) => {
          this.$message.success("删除成功！");
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
