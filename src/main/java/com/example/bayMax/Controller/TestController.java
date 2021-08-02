package com.example.bayMax.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test")
    public String getTestPage(){
        return "test";
    }


    @GetMapping("/about")
    public String getaboutPage(){
        return "about";
    }
}
