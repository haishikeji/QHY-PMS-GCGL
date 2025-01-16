<template>
  <div>
    <el-form
      :inline="true"
      label-width="120px"
      :model="searchForm"
      class="search-form"
      ref="searchForm"
    >
      <el-form-item label="费用单号" prop="expenseReimburseCode">
        <el-input
          v-model="searchForm.expenseReimburseCode"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item label="收款人" prop="receiveName">
        <el-input
          v-model="searchForm.receiveName"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item label="部门" prop="depCode">
        <el-select v-model="searchForm.depCode" filterable placeholder="请选择">
          <el-option
            v-for="item in depList"
            :key="item.id"
            :label="item.name"
            :value="item.depCode"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="form-btns">
        <el-button type="primary" @click="seachSubmit">查询</el-button>
        <el-button @click="resetSearch('searchForm')">重置</el-button>
      </el-form-item>
      <el-form-item label="费用类型" prop="reimburseType">
        <el-select
          v-model="searchForm.reimburseType"
          filterable
          placeholder="请选择"
        >
          <el-option label="项目费用" :value="1"></el-option>
          <el-option label="行政费用" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="费用日期" prop="createTime">
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
    </el-form>

    <div class="pagination-box">
      <el-row>
        <exportExcelBtn
          url="statistics/getExpenseStatistics/export"
          :params="{
            pageNum: pageIndex,
            pageSize: pageSize,
            ...searchInfo,
          }"
          file-name="费用报表"
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
        border
      >
      </el-pagination>
    </div>

    <div class="content-table">
      <el-table
        :data="tableData"
        height="100%"
        style="width: 100%"
        class="table-box"
      >
        <el-table-column label="费用单号" width="180" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.expenseReimburseCode || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目名称" width="230" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.projectName || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目编号" width="180" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.projectCode || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目部">
          <template slot-scope="scope">
            <p>{{ scope.row.depName || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="收款人">
          <template slot-scope="scope">
            <p>{{ scope.row.receiveName || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="收款人类型" width="120">
          <template slot-scope="scope">
            <p>{{ scope.row.receiveType == 1 ? "企业" : "员工" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="收款账户">
          <template slot-scope="scope">
            <p>{{ scope.row.receiveBankAccount || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="费用金额">
          <template slot-scope="scope">
            <p>{{ scope.row.reimbursableAmount | formatPrice }}</p>
          </template>
        </el-table-column>
        <el-table-column label="预算成本科目">
          <template slot-scope="scope">
            <p>{{ scope.row.constructionBudgetCode || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="付款状态">
          <template slot-scope="scope">
            <p>{{ scope.row.status == 2 ? "已付款" : "未付款" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="费用日期" width="150">
          <template slot-scope="scope">
            <p>{{ scope.row.createTime || "-" }}</p>
          </template>
        </el-table-column>
        <el-table-column label="费用类型">
          <template slot-scope="scope">
            <p>{{ scope.row.reimburseType == 1 ? "项目费用" : "行政费用" }}</p>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { getExpenseStatistics } from "@/api/statistics";
export default {
  name: "",
  data() {
    return {
      searchForm: {
        expenseReimburseCode: "",
        receiveName: "",
        depCode: "",
        createTime: "",
        createTimeStart: "",
        createTimeEnd: "",
        reimburseType: "",
      },
      tableData: [],
      pageSize: 20,
      pageIndex: 1,
      total: 0,
      showDetail: false,
      details: [],
      searchInfo: {},
    };
  },

  computed: {
    ...mapState("optionInfo", ["depList"]),
  },

  mounted() {
    this.getDepartment();
    this.getList();
  },

  methods: {
    ...mapActions("optionInfo", ["getDepartment"]),

    getList(index = 1) {
      let searchInfo = JSON.parse(JSON.stringify(this.searchForm));
      delete searchInfo.createTime;
      this.searchInfo = searchInfo;
      getExpenseStatistics({
        pageNum: index,
        pageSize: this.pageSize,
        ...searchInfo,
      }).then((res) => {
        const { records, total } = res.data.page;
        this.pageIndex = index;
        this.tableData = records || [];
        this.total = total;
      });
    },

    handleSizeChange(val) {
      this.pageSize = val;
      this.getList();
    },

    seachSubmit() {
      const { createTime } = this.searchForm;
      if (createTime) {
        this.searchForm.createTimeBegin = createTime[0];
        this.searchForm.createTimeEnd = createTime[1];
      }
      this.getList(1, this.searchForm);
    },

    resetSearch(formName) {
      this.searchForm.createTimeBegin = "";
      this.searchForm.createTimeEnd = "";
      this.$refs[formName].resetFields();
      this.getList();
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
