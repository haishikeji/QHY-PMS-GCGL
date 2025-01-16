import BasicLayout from "@/layout/index";
const viewport = {
  content: "width=device-width, initial-scale=1.0, user-scalable=no",
};

const meta = {
  auth: true,
};

const pre = "workspace-";

export default {
  path: "/workspace",
  name: "workspace",
  redirect: {
    name: `forms`,
  },
  meta,
  component: BasicLayout,
  children: [
    {
      path: "forms",
      name: "forms",
      component: () => import("@/views/workspace/oa/FromsApp.vue"),
      meta: {
        ...meta,
        title: "工程项目管理系统-OA审批",
        viewport: viewport,
      },
    },
    {
      path: "submit",
      name: "submit",
      component: () => import("@/views/workspace/oa/MySubmit.vue"),
      meta: {
        ...meta,
        title: "工程项目管理系统-我发起的",
        viewport: viewport,
      },
    },
    {
      path: "cc",
      name: "cc",
      component: () => import("@/views/workspace/oa/CcMe.vue"),
      meta: {
        ...meta,
        title: "工程项目管理系统-抄送我的",
        viewport: viewport,
      },
    },
    {
      path: "unfinished",
      name: "unfinished",
      component: () => import("@/views/workspace/oa/UnFinished.vue"),
      meta: {
        ...meta,
        title: "工程项目管理系统-未完成的",
        viewport: viewport,
      },
    },
    {
      path: "finished",
      name: "finished",
      component: () => import("@/views/workspace/oa/Finished.vue"),
      meta: {
        ...meta,
        title: "工程项目管理系统-未完成的",
        viewport: viewport,
      },
    },
    {
      path: "instances",
      name: "instances",
      component: () => import("@/views/admin/ProcessInstanceManage.vue"),
      meta: {
        title: "工程项目管理系统-数据管理",
        viewport: viewport,
      },
    },
    {
      path: "formsPanel",
      name: "formsPanel",
      component: () => import("@/views/admin/FormsPanel.vue"),
      meta: {
        title: "工程项目管理系统-表单列表",
        viewport: viewport,
      },
    },
  ],
};
