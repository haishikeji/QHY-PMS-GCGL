<template>
  <div>
    <div class="pagination-box">
      <el-row>
        <el-button type="primary" size="small" @click="modalInfo.show = true"
          >新增</el-button
        >
        <el-button
          :disabled="!tableData.length"
          type="primary"
          plain
          size="small"
          >导出</el-button
        >
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
      <el-table :data="tableData" height="100%" style="width: 100%" class="table-box" border>
        <el-table-column label="科目名称" prop="name"> </el-table-column>
        <el-table-column label="科目类型" prop="desc"> </el-table-column>
        <el-table-column label="备注说明" prop="desc"> </el-table-column>
        <el-table-column label="最近更新人" prop="desc"> </el-table-column>
        <el-table-column label="创建日期" prop="desc"> </el-table-column>
      </el-table>
    </div>

    <WDialog
      :value="modalInfo.show"
      :title="modalInfo.title"
      width="650px"
      @cancel="modalInfo.show = false"
      @ok="submitRow('infoForm')"
    >
      <el-form label-width="80px" :model="infoForm" ref="infoForm">
        <el-form-item label="科目名称">
          <el-input
            v-model="infoForm.name"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
        <el-form-item label="收支类型" prop="type">
          <el-select v-model="infoForm.type" placeholder="请选择">
            <el-option label="区域一" value="shanghai"></el-option>
            <el-option label="区域二" value="beijing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注说明">
          <el-input
            v-model="infoForm.remark"
            placeholder="请输入"
            type="textarea"
          ></el-input>
        </el-form-item>
      </el-form>
    </WDialog>
  </div>
</template>
  
<script>
import { mapState, mapActions } from "vuex";
import { getSubPage, addOrUpdateSub, deleteSub } from "@/api/finance";

export default {
  name: "",
  data() {
    return {
      modalInfo: {
        show: false,
        title: "新增科目",
      },
      infoForm: {
        user: "",
        region: "",
      },
      tableData: [],
      total: 0,
      pageSize: 20,
      pageIndex: 1,
      multipleSelection: [],
    };
  },

  mounted() {
    this.getList();
  },


  methods: {

    getList(index = 1) {
      getReiPage({
        pageNum: index,
        pageSize: this.pageSize,
      }).then((res) => {
        this.pageIndex = index;
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

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    deleteRow(id) {
      this.$confirm("确定要删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        deleteRei({id}).then((res) => {
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