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
          @keyup.enter.native="searchSubmit"
          v-model="searchForm.projectName"
          placeholder="请输入"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="计划名称" prop="name">
        <el-input
          v-model="searchForm.name"
          placeholder="请输入"
          @keyup.enter.native="searchSubmit"
        ></el-input>
      </el-form-item>
      <el-form-item label="申请日期" prop="createTime">
        <el-date-picker
          @keyup.enter.native="searchSubmit"
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
      <el-form-item label="申请人" prop="applyUserCode">
        <!--      <el-select
		  @keyup.enter.native="searchSubmit"
          v-model="searchForm.applyUserCode"
          filterable
          placeholder="请选择"
        >
          <el-option
            v-for="item in userList"
            :key="item.id"
            :label="item.name"
            :value="item.userCode"
          ></el-option>
        </el-select> -->
        <el-input
          v-model="searchForm.applyUserCode"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item class="form-btns">
        <el-button type="primary" @click="searchSubmit">查询</el-button>
        <el-button @click="resetForm('searchForm')">重置</el-button>
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
          url="es/engineeringOtherPlan/export"
          :params="{
            pageNum: pageIndex,
            pageSize: pageSize,
            ...searchForm,
            dataType: 2,
          }"
          :disabled="!tableData.length"
          file-name="分包计划"
        >
        </exportExcelBtn>
        <el-button
          v-if="
            multipleSelection[0] && multipleSelection[0].pushDownStatus == 1
          "
          :disabled="!(multipleSelection.length == 1)"
          type="primary"
          plain
          size="small"
          @click="pushdowmts"
        >
          下推</el-button
        >

        <PushDown
          v-else
          typeKey="分包计划"
          :data="multipleSelection[0]"
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
        @selection-change="handleSelectionChange"
        class="table-box"
      >
        <el-table-column type="selection" width="55"> </el-table-column>

        <el-table-column label="计划编号">
          <template slot-scope="scope">
            <p>{{ scope.row.dataCode || "-" }}</p>
          </template>
        </el-table-column>

        <el-table-column label="计划名称">
          <template slot-scope="scope">
            <p>{{ scope.row.name || "-" }}</p>
          </template>
        </el-table-column>

        <el-table-column label="项目名称" width="230">
          <template slot-scope="scope">
            <p>{{ scope.row.projectName || "-" }}</p>
          </template>
        </el-table-column>

        <el-table-column label="申请人">
          <template slot-scope="scope">
            <p>{{ scope.row.applyUserName || "-" }}</p>
          </template>
        </el-table-column>

        <el-table-column label="申请日期">
          <template slot-scope="scope">
            <p>{{ scope.row.createTime || "-" }}</p>
          </template>
        </el-table-column>

        <el-table-column label="流程状态">
          <template slot-scope="scope">
            <el-tag :type="scope.row.flowStatus | getFlowType">{{
              scope.row.flowStatus | getFlowStatus
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240">
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
            <el-button
              type="danger"
              plain
              size="small"
              v-if="
                [1, 3, 5].includes(scope.row.flowStatus) &&
                [1, 2].includes(scope.row.createFlag)
              "
              @click.native.prevent="deleteRow(scope.row.id)"
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
import {
  getEOPlanPage,
  submitEOPlanAudit,
  removeEOPlan,
} from "@/api/engineering";
import { setFlowStatus } from "@/utils/enumStatus";
import audit from "@/mixins/audit";

export default {
  name: "project-fbjh",
  data() {
    return {
      searchForm: {
        projectName: "",
        name: "",
        applyUserCode: "",
        createTime: "",
        createTimeStart: "",
        createTimeEnd: "",
      },
      flowStatus: setFlowStatus(),
      tableData: [],
      total: 0,
      pageSize: 20,
      pageIndex: 1,
      multipleSelection: [],
    };
  },

  computed: {
    ...mapState("optionInfo", ["userList"]),
  },

  mixins: [audit],

  mounted() {
    this.getUsers();
    this.getList();
  },

  methods: {
    ...mapActions("optionInfo", ["getUsers"]),

    getList(index = 1) {
      let searchInfo = JSON.parse(JSON.stringify(this.searchForm));
      delete searchInfo.createTime;

      getEOPlanPage({
        pageNum: index,
        pageSize: this.pageSize,
        dataType: 2,
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

    submitAudit(id, index) {
      this.$confirm("确定要提审?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        submitEOPlanAudit({
          id,
        }).then((res) => {
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
        removeEOPlan({
          id,
        }).then((res) => {
          this.$message.success("删除成功");
          this.getList(this.pageIndex);
        });
      });
    },

    toEdit(id) {
      let url = "/project/fbjh/edit";
      if (id) {
        url += `?id=${id}`;
      }
      this.$router.push(url);
    },

    pushdowmts() {
      this.$message.error("一个合同只允许下推一次");
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
