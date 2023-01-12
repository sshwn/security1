package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({"","/"})
    public String index() {
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정 : templates (prefix), .mustache (suffix) (dependency로 넣었을경우 생략가능)
        return "index"; // src/main/resources/templates/index.mustache
    }

}
