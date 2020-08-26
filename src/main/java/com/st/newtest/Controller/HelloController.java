package com.st.newtest.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {
    @RequestMapping("/index")
    public String sayHi() {
        return "index";
    }

    @RequestMapping("/form-common")
    public String newForm1() {
        return "form-common";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/openIndex")
    public String openIndex() {
        return "index-de";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dropIndex")
    public String dropIndex() {
        return "dropIndex";
    }

    @RequestMapping("/tableIndex")
    public String tableIndex() {
        return "tableIndex";
    }

    @RequestMapping("/dispathtcher_item")
    public String item_dispatcher() {
        return "item_dispatcher";
    }

    @RequestMapping("/charts")
    public String charts() {
        return "charts";
    }

    @RequestMapping("/tables")
    public String tables() {
        return "tables";
    }
}
