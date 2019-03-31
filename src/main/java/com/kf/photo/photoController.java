package com.kf.photo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class photoController {

    @RequestMapping("")
    public String index() {
        return "index";
    }

    @RequestMapping("upload")
    @ResponseBody
    public String upload(MultipartFile photo) {
        return "aa";
    }

}
