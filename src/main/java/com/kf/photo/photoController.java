package com.kf.photo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class photoController {

    @RequestMapping("")
    public String index() {
        return "index";
    }

}
