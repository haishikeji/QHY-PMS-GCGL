package com.px.controller.system;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.bean.dto.MenuData;
import com.px.common.util.CollUtils;
import com.px.system.entity.SysMenu;
import com.px.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sys/initData")
public class SysInitDataController {


    String str = "[\n" +
            "    {\n" +
            "        \"path\": \"/platform\",\n" +
            "        \"title\": \"平台中心\",\n" +
            "        \"header\": \"platform\",\n" +
            "        \"icon\": \"icon-home\",\n" +
            "        \"children\": [\n" +
            "            {\n" +
            "                \"title\": \"信息管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"消息中心\",\n" +
            "                        \"path\": \"/platform/notice\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"企业公告\",\n" +
            "                        \"path\": \"/platform/bulletin\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"审批中心\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"审批管理\",\n" +
            "                        \"path\": \"/approval/manage\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"审批模板\",\n" +
            "                        \"path\": \"/workspace/formsPanel\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"预警中心\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"预警中心\",\n" +
            "                        \"path\": \"/warning\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"日志中心\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"日志管理\",\n" +
            "                        \"path\": \"/platform/log\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"数据字典\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"数据字典\",\n" +
            "                        \"path\": \"/platform/dictionary\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"变更单\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"变更单\",\n" +
            "                        \"path\": \"/platform/change\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 3,\n" +
            "        \"path\": \"/resources\",\n" +
            "        \"title\": \"资源中心\",\n" +
            "        \"header\": \"resources\",\n" +
            "        \"icon\": \"icon-resources\",\n" +
            "        \"children\": [\n" +
            "            {\n" +
            "                \"title\": \"客户管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"客户档案\",\n" +
            "                        \"path\": \"/client/archives\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"物料中心\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"物资档案\",\n" +
            "                        \"path\": \"/material/archives\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"物资分类\",\n" +
            "                        \"path\": \"/material/category\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"供应商管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"供应商档案\",\n" +
            "                        \"path\": \"/provider/archives\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"项目管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"项目管理\",\n" +
            "                        \"path\": \"/project\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"施工班组\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"施工班组\",\n" +
            "                        \"path\": \"/construction\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"path\": \"/human-resources\",\n" +
            "        \"title\": \"人力资源\",\n" +
            "        \"header\": \"human-resources\",\n" +
            "        \"icon\": \"icon-human\",\n" +
            "        \"children\": [\n" +
            "            {\n" +
            "                \"title\": \"人事管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"员工管理\",\n" +
            "                        \"path\": \"/user/manage\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"组织架构\",\n" +
            "                        \"path\": \"/user/architecture\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"职位管理\",\n" +
            "                        \"path\": \"/user/post\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"部门调动申请\",\n" +
            "                        \"path\": \"/user/invoke\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"员工报表\",\n" +
            "                        \"path\": \"/user/report\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"证书资源管理\",\n" +
            "                        \"path\": \"/user/cert\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"行政物资管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"行政物料档案\",\n" +
            "                        \"path\": \"/human-resources/acceptance\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"行政需用计划\",\n" +
            "                        \"path\": \"/human-resources/plan\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"行政物资入库\",\n" +
            "                        \"path\": \"/human-resources/in\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"行政物资出库\",\n" +
            "                        \"path\": \"/human-resources/out\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"行政物资库存台账\",\n" +
            "                        \"path\": \"/human-resources/tz\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"path\": \"/market\",\n" +
            "        \"title\": \"市场管理\",\n" +
            "        \"header\": \"market\",\n" +
            "        \"icon\": \"icon-market\",\n" +
            "        \"children\": [\n" +
            "            {\n" +
            "                \"title\": \"客户档案\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"path\": \"/client/manage\",\n" +
            "                        \"title\": \"客户档案\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"投标管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"投标立项\",\n" +
            "                        \"path\": \"/bidding/tblx\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"标书评审\",\n" +
            "                        \"path\": \"/bidding/assessment\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"保证金管理\",\n" +
            "                        \"path\": \"/bidding/deposit\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"投标总结\",\n" +
            "                        \"path\": \"/bidding/summary\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"市场报表\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"投标结果报表\",\n" +
            "                        \"path\": \"/market/report\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"path\": \"/engineering\",\n" +
            "        \"title\": \"工程管理\",\n" +
            "        \"header\": \"engineering\",\n" +
            "        \"icon\": \"icon-engineering\",\n" +
            "        \"children\": [\n" +
            "            {\n" +
            "                \"title\": \"合同管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"合同登记\",\n" +
            "                        \"path\": \"/engineering/contract\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"工程项目\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"项目管理\",\n" +
            "                        \"path\": \"/project/manage\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"施工预算\",\n" +
            "                        \"path\": \"/project/sgys\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"需用计划\",\n" +
            "                        \"path\": \"/project/xyjh\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"租赁计划\",\n" +
            "                        \"path\": \"/project/zpjh\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"分包计划\",\n" +
            "                        \"path\": \"/project/fbjh\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"报表\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"收入合同台账\",\n" +
            "                        \"path\": \"/engineering/report\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"path\": \"/procure\",\n" +
            "        \"title\": \"采购管理\",\n" +
            "        \"header\": \"procure\",\n" +
            "        \"icon\": \"icon-procure\",\n" +
            "        \"children\": [\n" +
            "            {\n" +
            "                \"title\": \"采购管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"采购合同\",\n" +
            "                        \"path\": \"/procure/contract\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"采购合同结算\",\n" +
            "                        \"path\": \"/procure/settlement\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"采购订单\",\n" +
            "                        \"path\": \"/procure/order\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"采购订单结算\",\n" +
            "                        \"path\": \"/procure/orderSettle\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"报表\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"采购台账\",\n" +
            "                        \"path\": \"/report/procure\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"调拨台账\",\n" +
            "                        \"path\": \"/report/allocate\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"物料价格\",\n" +
            "                        \"path\": \"/report/price\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"租赁管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"租赁合同\",\n" +
            "                        \"path\": \"/lease/contract\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"租赁合同结算\",\n" +
            "                        \"path\": \"/lease/settlement\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"分包管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"分包合同\",\n" +
            "                        \"path\": \"/subpackage/contract\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"分包合同结算\",\n" +
            "                        \"path\": \"/subpackage/settlement\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"path\": \"/material\",\n" +
            "        \"title\": \"物资管理\",\n" +
            "        \"header\": \"material\",\n" +
            "        \"icon\": \"icon-material\",\n" +
            "        \"children\": [\n" +
            "            {\n" +
            "                \"title\": \"出入库管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"收料入库\",\n" +
            "                        \"path\": \"/discrepancy/slrk\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"领料出库\",\n" +
            "                        \"path\": \"/discrepancy/llck\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"直入直出\",\n" +
            "                        \"path\": \"/discrepancy/zrzc\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"调拨出库\",\n" +
            "                        \"path\": \"/discrepancy/dbck\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"调拨入库\",\n" +
            "                        \"path\": \"/discrepancy/dbrk\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"领料退货\",\n" +
            "                        \"path\": \"/discrepancy/tld\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"物资退货\",\n" +
            "                        \"path\": \"/discrepancy/wzth\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"仓库管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"仓库管理\",\n" +
            "                        \"path\": \"/warehouse/manage\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"库存明细\",\n" +
            "                        \"path\": \"/warehouse/ledger\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"盘点管理\",\n" +
            "                        \"path\": \"/warehouse/inventory\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"呆滞物料处理\",\n" +
            "                        \"path\": \"/warehouse/sluggish\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"报表\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"出入库台账\",\n" +
            "                        \"path\": \"/material/report\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"path\": \"/finance\",\n" +
            "        \"title\": \"财务管理\",\n" +
            "        \"header\": \"invoice\",\n" +
            "        \"icon\": \"icon-invoice\",\n" +
            "        \"children\": [\n" +
            "            {\n" +
            "                \"title\": \"发票管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"收票管理\",\n" +
            "                        \"path\": \"/invoice/manage\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"开票申请\",\n" +
            "                        \"path\": \"/invoice/application\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"报销管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"报销申请\",\n" +
            "                        \"path\": \"/reimbursement/application\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"报销报表\",\n" +
            "                        \"path\": \"/reimbursement/report\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"资金管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"工程收款登记\",\n" +
            "                        \"path\": \"/fund/registration\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"付款申请\",\n" +
            "                        \"path\": \"/fund/application\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"备用金管理\",\n" +
            "                        \"path\": \"/fund/spare-application\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"应收账款台账\",\n" +
            "                        \"path\": \"/fund/ysk\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"应付账款台账\",\n" +
            "                        \"path\": \"/fund/yfk\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"成本管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"成本科目\",\n" +
            "                        \"path\": \"/cost/subjects\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"项目成本分析\",\n" +
            "                        \"path\": \"/cost/registration\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"path\": \"/safe\",\n" +
            "        \"title\": \"安全与质量\",\n" +
            "        \"header\": \"safe\",\n" +
            "        \"icon\": \"icon-safe\",\n" +
            "        \"children\": [\n" +
            "            {\n" +
            "                \"title\": \"安全管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"安全处罚单\",\n" +
            "                        \"path\": \"/safe/punish\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"安全奖励单\",\n" +
            "                        \"path\": \"/safe/reward\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"安全培训\",\n" +
            "                        \"path\": \"/safe/training\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"安全检查与整改\",\n" +
            "                        \"path\": \"/safe/examine\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"安全事故汇报\",\n" +
            "                        \"path\": \"/safe/debrief\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"特殊作业人员\",\n" +
            "                        \"path\": \"/safe/workers\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"安全人员\",\n" +
            "                        \"path\": \"/safe/personnel\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"质量管理\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"质量处罚单\",\n" +
            "                        \"path\": \"/quality/punish\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"title\": \"质量奖励单\",\n" +
            "                        \"path\": \"/quality/reward\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"title\": \"安全报表\",\n" +
            "                \"children\": [\n" +
            "                    {\n" +
            "                        \"title\": \"安全投入台账\",\n" +
            "                        \"path\": \"/safe/report\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "]";

    String str2 = "[{\"path\":\"/platform/notice\",\"title\":\"平台中心\",\"header\":\"platform\",\"icon\":\"icon-home\",\"children\":[{\"title\":\"信息管理\",\"children\":[{\"title\":\"消息中心\",\"path\":\"/platform/notice\"},{\"title\":\"企业公告\",\"path\":\"/platform/bulletin\"}]},{\"title\":\"审批中心\",\"children\":[{\"title\":\"审批管理\",\"path\":\"/approval/manage\"},{\"title\":\"审批模板\",\"path\":\"/workspace/formsPanel\"}]},{\"title\":\"预警中心\",\"children\":[{\"title\":\"预警中心\",\"path\":\"/warning\"}]},{\"title\":\"日志中心\",\"children\":[{\"title\":\"日志管理\",\"path\":\"/platform/log\"}]},{\"title\":\"数据中心\",\"children\":[{\"title\":\"数据字典\",\"path\":\"/platform/dictionary\"}]},{\"title\":\"变更单\",\"children\":[{\"title\":\"变更单\",\"path\":\"/platform/change\"}]}]},{\"id\":3,\"path\":\"/resources\",\"title\":\"资源中心\",\"header\":\"resources\",\"icon\":\"icon-resources\",\"children\":[{\"title\":\"客户管理\",\"children\":[{\"title\":\"客户档案\",\"path\":\"/client/manage\"}]},{\"title\":\"物料中心\",\"children\":[{\"title\":\"物资档案\",\"path\":\"/material/archives\"},{\"title\":\"物资分类\",\"path\":\"/material/category\"}]},{\"title\":\"供应商管理\",\"children\":[{\"title\":\"供应商档案\",\"path\":\"/provider/archives\"}]},{\"title\":\"项目管理\",\"children\":[{\"title\":\"项目管理\",\"path\":\"/project/manage\"}]},{\"title\":\"施工班组\",\"children\":[{\"title\":\"施工班组\",\"path\":\"/construction\"}]}]},{\"path\":\"/human-resources\",\"title\":\"人力资源\",\"header\":\"human-resources\",\"icon\":\"icon-human\",\"children\":[{\"title\":\"人事管理\",\"children\":[{\"title\":\"员工管理\",\"path\":\"/user/manage\"},{\"title\":\"组织架构\",\"path\":\"/user/architecture\"},{\"title\":\"职位管理\",\"path\":\"/user/post\"},{\"title\":\"部门调动申请\",\"path\":\"/user/invoke\"},{\"title\":\"员工报表\",\"path\":\"/user/report\"},{\"title\":\"证书资源管理\",\"path\":\"/user/cert\"}]},{\"title\":\"行政物资管理\",\"children\":[{\"title\":\"行政物资分类\",\"path\":\"/material/category?type=4\"},{\"title\":\"行政物资档案\",\"path\":\"/human-resources/acceptance\"},{\"title\":\"行政领用申请\",\"path\":\"/human-resources/plan\"},{\"title\":\"行政物资入库\",\"path\":\"/human-resources/in\"},{\"title\":\"行政物资出库\",\"path\":\"/human-resources/out\"},{\"title\":\"行政物资库存台账\",\"path\":\"/human-resources/tz\"}]}]},{\"path\":\"/market\",\"title\":\"市场管理\",\"header\":\"market\",\"icon\":\"icon-market\",\"children\":[{\"title\":\"客户档案\",\"children\":[{\"path\":\"/client/manage\",\"title\":\"客户档案\"}]},{\"title\":\"投标管理\",\"children\":[{\"title\":\"投标立项\",\"path\":\"/bidding/tblx\"},{\"title\":\"标书评审\",\"path\":\"/bidding/assessment\"},{\"title\":\"保证金管理\",\"path\":\"/bidding/deposit\"},{\"title\":\"投标总结\",\"path\":\"/bidding/summary\"}]},{\"title\":\"市场报表\",\"children\":[{\"title\":\"投标结果报表\",\"path\":\"/market/report\"}]}]},{\"path\":\"/engineering/contract\",\"title\":\"工程管理\",\"header\":\"engineering\",\"icon\":\"icon-engineering\",\"children\":[{\"title\":\"合同管理\",\"children\":[{\"title\":\"合同登记\",\"path\":\"/engineering/contract\"}]},{\"title\":\"工程项目\",\"children\":[{\"title\":\"项目管理\",\"path\":\"/project/manage\"},{\"title\":\"施工预算\",\"path\":\"/project/sgys\"},{\"title\":\"需用计划\",\"path\":\"/project/xyjh\"},{\"title\":\"租赁计划\",\"path\":\"/project/zljh\"},{\"title\":\"分包计划\",\"path\":\"/project/fbjh\"},{\"title\":\"分包商入场审查\",\"path\":\"/project/fbs/inspection\"}]},{\"title\":\"报表\",\"children\":[{\"title\":\"预算报表\",\"path\":\"/engineering/report\"}]}]},{\"path\":\"/procure\",\"title\":\"采购管理\",\"header\":\"procure\",\"icon\":\"icon-procure\",\"children\":[{\"title\":\"采购管理\",\"children\":[{\"title\":\"大宗采购请示单\",\"path\":\"/procure/bulk\"},{\"title\":\"集采合同\",\"path\":\"/procure/cp/contract\"},{\"title\":\"集采合同结算\",\"path\":\"/procure/cp/settlement\"},{\"title\":\"采购合同\",\"path\":\"/procure/contract\"},{\"title\":\"采购合同结算\",\"path\":\"/procure/settlement\"},{\"title\":\"采购合同批量结算\",\"path\":\"/procure/settleBatch\"},{\"title\":\"采购订单\",\"path\":\"/procure/order\"},{\"title\":\"采购订单结算\",\"path\":\"/procure/orderSettle\"},{\"title\":\"采购订单批量结算\",\"path\":\"/procure/orderSettleBatch\"}]},{\"title\":\"报表\",\"children\":[{\"title\":\"采购台账\",\"path\":\"/report/procure\"},{\"title\":\"调拨台账\",\"path\":\"/report/allocate\"},{\"title\":\"物料价格\",\"path\":\"/report/price\"}]},{\"title\":\"租赁管理\",\"children\":[{\"title\":\"租赁合同\",\"path\":\"/lease/contract\"},{\"title\":\"租赁合同结算\",\"path\":\"/lease/settlement\"},{\"title\":\"租赁合同批量结算\",\"path\":\"/lease/settleBatch\"}]},{\"title\":\"分包管理\",\"children\":[{\"title\":\"分包合同\",\"path\":\"/subpackage/contract\"},{\"title\":\"分包合同结算\",\"path\":\"/subpackage/settlement\"},{\"title\":\"分包合同批量结算\",\"path\":\"/subpackage/settleBatch\"}]}]},{\"path\":\"/material\",\"title\":\"物资管理\",\"header\":\"material\",\"icon\":\"icon-material\",\"children\":[{\"title\":\"出入库管理\",\"children\":[{\"title\":\"收料入库\",\"path\":\"/discrepancy/slrk\"},{\"title\":\"入库折扣单\",\"path\":\"/discrepancy/rkzkd\"},{\"title\":\"领料出库\",\"path\":\"/discrepancy/llck\"},{\"title\":\"直入直出\",\"path\":\"/discrepancy/zrzc\"},{\"title\":\"调拨出库\",\"path\":\"/discrepancy/dbck\"},{\"title\":\"调拨入库\",\"path\":\"/discrepancy/dbrk\"},{\"title\":\"领料退货\",\"path\":\"/discrepancy/tld\"},{\"title\":\"物资退货\",\"path\":\"/discrepancy/wzth\"}]},{\"title\":\"仓库管理\",\"children\":[{\"title\":\"仓库管理\",\"path\":\"/warehouse/manage\"},{\"title\":\"库存明细\",\"path\":\"/warehouse/ledger\"},{\"title\":\"盘点管理\",\"path\":\"/warehouse/inventory\"},{\"title\":\"呆滞物料处理\",\"path\":\"/warehouse/sluggish\"},{\"title\":\"需用计划明细台账\",\"path\":\"/warehouse/requiredPlanLedger\"},{\"title\":\"入库明细台账\",\"path\":\"/warehouse/warehousingLedger\"},{\"title\":\"出库明细台账\",\"path\":\"/warehouse/outWarehouseLedger\"}]},{\"title\":\"报表\",\"children\":[{\"title\":\"出入库台账\",\"path\":\"/material/report\"}]}]},{\"path\":\"/invoice/manage\",\"title\":\"财务管理\",\"header\":\"invoice\",\"icon\":\"icon-invoice\",\"children\":[{\"title\":\"发票管理\",\"children\":[{\"title\":\"收票管理\",\"path\":\"/invoice/manage\"},{\"title\":\"开票申请\",\"path\":\"/invoice/application\"}]},{\"title\":\"报销管理\",\"children\":[{\"title\":\"费用申请\",\"path\":\"/reimbursement/application\"},{\"title\":\"费用报表\",\"path\":\"/reimbursement/report\"}]},{\"title\":\"资金管理\",\"children\":[{\"title\":\"工程收款登记\",\"path\":\"/fund/registration\"},{\"title\":\"付款申请\",\"path\":\"/fund/application\"},{\"title\":\"批量付款申请\",\"path\":\"/fund/settleBatch\"},{\"title\":\"备用金管理\",\"path\":\"/fund/spare-application\"},{\"title\":\"应收账款台账\",\"path\":\"/fund/ysk\"},{\"title\":\"应付账款台账\",\"path\":\"/fund/yfk\"}]},{\"title\":\"成本管理\",\"children\":[{\"title\":\"项目成本分析\",\"path\":\"/cost/registration\"}]}]},{\"path\":\"/safe\",\"title\":\"安全与质量\",\"header\":\"safe\",\"icon\":\"icon-safe\",\"children\":[{\"title\":\"安全管理\",\"children\":[{\"title\":\"安全处罚单\",\"path\":\"/safe/punish\"},{\"title\":\"安全奖励单\",\"path\":\"/safe/reward\"},{\"title\":\"安全培训\",\"path\":\"/safe/training\"},{\"title\":\"安全检查与整改\",\"path\":\"/safe/examine\"},{\"title\":\"安全事故汇报\",\"path\":\"/safe/debrief\"}]},{\"title\":\"质量管理\",\"children\":[{\"title\":\"质量处罚单\",\"path\":\"/quality/punish\"},{\"title\":\"质量奖励单\",\"path\":\"/quality/reward\"}]},{\"title\":\"安全报表\",\"children\":[{\"title\":\"安全投入台账\",\"path\":\"/safe/report\"}]}]}]";


    @Autowired
    private SysMenuService menuService;

    @GetMapping("/initMenuData")
    public void initMenuData() {
        List list = JSON.parseObject(str2, List.class);
        ArrayList<MenuData> menuDataArrayList = new ArrayList<>();
        for (Object obj : list) {
            MenuData menuData = JSON.parseObject(String.valueOf(obj), MenuData.class);
            menuDataArrayList.add(menuData);
        }
        menuService.remove(new LambdaQueryWrapper<>());
        for (MenuData menuData : menuDataArrayList) {
            SysMenu sysMenu = new SysMenu();
            sysMenu.setPlatformType(1);
            sysMenu.setParentId(0L);
            sysMenu.setMenuLevel(1);
            sysMenu.setMenuName(menuData.getTitle());
            sysMenu.setMenuPath(menuData.getPath());
            sysMenu.setMenuComponent("");
            sysMenu.setMenuIcon(menuData.getIcon());
            sysMenu.setIsShow(1);
            sysMenu.setMenuType(1);
            sysMenu.setIsDel(0);
            sysMenu.setCreateTime(new Date());
            menuService.save(sysMenu);
            Long menuId = sysMenu.getMenuId();
            List<MenuData> childrenList = menuData.getChildren();
            saveChildData(menuId, sysMenu.getMenuLevel(), childrenList);
        }
    }

    private void saveChildData(Long parentMenuId, Integer parentLevel, List<MenuData> childrenList) {
        if (CollUtils.isNotEmpty(childrenList)) {
            for (MenuData menuData : childrenList) {
                SysMenu sysMenu = new SysMenu();
                sysMenu.setPlatformType(1);
                sysMenu.setParentId(parentMenuId);
                sysMenu.setMenuLevel(parentLevel + 1);
                sysMenu.setMenuName(menuData.getTitle());
                sysMenu.setMenuPath(menuData.getPath());
                sysMenu.setMenuComponent("");
                sysMenu.setMenuIcon(menuData.getIcon());
                sysMenu.setIsShow(1);
                sysMenu.setMenuType(1);
                sysMenu.setIsDel(0);
                sysMenu.setCreateTime(new Date());
                menuService.save(sysMenu);
                Long menuId = sysMenu.getMenuId();
                List<MenuData> nextChildrenList = menuData.getChildren();
                saveChildData(menuId, sysMenu.getMenuLevel(), nextChildrenList);
            }
        }
    }

}
