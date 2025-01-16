package com.px.controller.system;

import com.px.system.business.CodeBusiness;
import com.px.system.service.SysCodeGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/codeGenerate")
public class SysCodeGenerateController {

    @Autowired
    private CodeBusiness codeBusiness;
    @Autowired
    private SysCodeGenerateService codeGenerateService;

    @GetMapping("/init")
    public void init() {
        codeGenerateService.init();
    }

    @GetMapping("/updateCodeConfig")
    public void updateCodeConfig() {
        codeGenerateService.updateCodeConfig();
    }

    @GetMapping("/genCode")
    public String genCode(String code) {
        return codeBusiness.genCode(code);
    }

    @GetMapping("/genCodes")
    public void genCodes(String code) {
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 10; i++) {
                String s = codeBusiness.genCode(code);
                System.out.println(s);
            }
        }

    }

}
