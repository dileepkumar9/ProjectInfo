package com.example.sb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.sb.service.StudentService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

//    @RequestMapping("/")
//    public String viewHomePage() {
//        return "index";
//    }

    @RequestMapping("/")
    public String secondPage(Model model) {
        List<List<Integer>> a = new ArrayList<List<Integer>>();
        for(int i=0; i<10; i++) {
            List<Integer> b = new ArrayList<Integer>();
            for (int j = 0; j < 10; j++)
                b.add(0);
            a.add(b);
        }
//        System.out.println(a);
        model.addAttribute("mois", a);
        return "second";
    }

    @RequestMapping("/saveDetails")
    public String saveDetails(@RequestParam MultiValueMap<String, String> parameters) {
        System.out.println(new ArrayList<>(parameters.get("prop")).get(1));
        return "index";
    }


}
