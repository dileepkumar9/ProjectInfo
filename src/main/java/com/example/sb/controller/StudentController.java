package com.example.sb.controller;

import com.example.sb.model.ClassData;
import com.example.sb.model.StudentDetails;
import com.example.sb.service.ClassDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.sb.service.StudentService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassDataService classDataService;

//    @RequestMapping("/")
//    public String viewHomePage() {
//        return "index";
//    }

    @RequestMapping("/")
    public String secondPage(Model model) {
        List<StudentDetails> studentDetailsList = studentService.getAllStudentDetails();
        List<List<Integer>> a = emptyArray();
        List<ClassData> classDataList = classDataService.getAllClassData();
        for(int i=0; i<classDataList.size(); i++){
            int t = (classDataList.get(i).getSeatNo()-1)/10;
            List<Integer> t1 = a.get(t);
            t1.set((classDataList.get(i).getSeatNo()-1)%10, classDataList.get(i).getRollNo().intValue());
            a.set(t,  t1);
//            System.out.println(t1);
//            System.out.println(t);
        }
//        System.out.println(a);
        model.addAttribute("mois", a);
        model.addAttribute("listStudentDetails", studentDetailsList);
        return "second";
    }

    List<List<Integer>> emptyArray(){
        List<List<Integer>> a = new ArrayList<List<Integer>>();
        for(int i=0; i<10; i++) {
            List<Integer> b = new ArrayList<Integer>();
            for (int j = 0; j < 10; j++)
                b.add(0);
            a.add(b);
        }
        return a;
    }

    @RequestMapping("/saveDetails")
    public String saveDetails(@RequestParam MultiValueMap<String, String> parameters) {
        List<String> data = new ArrayList<>(parameters.get("prop"));
        for(int i=0; i<data.size(); i++)
        if(checkNumber(data.get(i))) {
            classDataService.deleteClassData(Long.parseLong(data.get(i)));
            saveClassData(Long.parseLong(data.get(i)), i + 1);
        }

        return "index";
    }

    void saveClassData(Long rollNo, int seatNo){
       StudentDetails studentDetails = studentService.getStudent(rollNo);
//        System.out.println(studentDetails);
            ClassData classData = new ClassData();
            classData.setRollNo(rollNo);
            classData.setClassName("class1");
            classData.setDisciplineId(studentDetails.getDisciplineId());
            classData.setSeatNo(seatNo);
            classDataService.saveClassData(classData);
    }

    boolean checkNumber(String value){
        try{
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException ex){
//            System.out.println(ex.getMessage());
            return false;
        }
    }


}
