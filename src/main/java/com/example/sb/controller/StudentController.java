package com.example.sb.controller;

import com.example.sb.model.ClassData;
import com.example.sb.model.ClassDetails;
import com.example.sb.model.StudentDetails;
import com.example.sb.service.ClassDataService;
import com.example.sb.service.ClassDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.sb.service.StudentService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Controller
public class StudentController {
    String error = "";
    String success = "";

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassDetailService classDetailService;

    @Autowired
    private ClassDataService classDataService;

//    @RequestMapping("/")
//    public String viewHomePage() {

//        return "index";
//    }

    @RequestMapping("/second/{className}")
    public String secondPage(@PathVariable(value = "className") String className, Model model) {
        List<StudentDetails> studentDetailsList = studentService.getAllStudentDetails();
        List<ClassData> classDataList = classDataService.getAllClassData();
        List<ClassData> filterClassData = new ArrayList<>();
        for (int i = 0; i < classDataList.size(); i++) {
            if (Objects.equals(classDataList.get(i).getClassName(), className)) {
                filterClassData.add(classDataList.get(i));
            }
        }

        for (int j = 0; j < studentDetailsList.size(); j++)
            for (int i = 0; i < classDataList.size(); i++) {
                if (Objects.equals(classDataList.get(i).getRollNo(), studentDetailsList.get(j).getRollNo())) {
                    studentDetailsList.remove(j);
                }
            }
        List<List<Integer>> a = emptyArray();
        for (int i = 0; i < filterClassData.size(); i++) {
            int t = (filterClassData.get(i).getSeatNo() - 1) / 10;
            List<Integer> t1 = a.get(t);
            t1.set((filterClassData.get(i).getSeatNo() - 1) % 10, filterClassData.get(i).getRollNo().intValue());
            a.set(t, t1);
//            System.out.println(t1);
//            System.out.println(t);
        }
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        model.addAttribute("mois", a);
        model.addAttribute("className", className);
        model.addAttribute("listStudentDetails", studentDetailsList);
        return "second";
    }

    @RequestMapping("/")
    public String thirdPage(Model model) {
        List<ClassData> classDataList = classDataService.getAllClassData();
        List<ClassDetails> classDetailsList = classDetailService.getAllClassDetails();
        List<ClassDetails> temp = new ArrayList<>();
        for (int i = 0; i < classDetailsList.size(); i++) {
            int t = 0;
            for (int j = 0; j < classDataList.size(); j++) {
                if (Objects.equals(classDetailsList.get(i).getClassName(), classDataList.get(j).getClassName())) {
                    t++;
                }
            }
            ClassDetails classDetails = new ClassDetails();
            classDetails.setAvailability(100 - t);
            classDetails.setClassName(classDetailsList.get(i).getClassName());
            classDetailService.saveClassDetail(classDetails);
            temp.add(classDetails);
        }
        model.addAttribute("classDetails", temp);
        return "third";
    }

    List<List<Integer>> emptyArray() {
        List<List<Integer>> a = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<Integer> b = new ArrayList<>();
            for (int j = 0; j < 10; j++)
                b.add(0);
            a.add(b);
        }
        return a;
    }

    @RequestMapping("/saveDetails/{className}")
    public String saveDetails(@RequestParam MultiValueMap<String, String> parameters, @PathVariable(value = "className") String className) {
//        System.out.println(parameters.get("prop"));
//        System.out.println(className);
        List<String> data = new ArrayList<>(parameters.get("prop"));
        for (int i = 0; i < data.size(); i++)
            if (checkNumber(data.get(i))) {
                if (checkValidation(className, Long.parseLong(data.get(i)), i + 1)) {
                    error = "Seat No: " + (i + 1) + " unable to arrange the seat";
                } else {
                    success = "Successfully Allocated";
                    deleteClassData(className, i + 1);
                    saveClassData(className, Long.parseLong(data.get(i)), i + 1);
                }
            }
        return "redirect:/second/"+className;
    }

    void deleteClassData(String className, int seatNo) {
        List<ClassData> classDataList = classDataService.getAllClassData();
        for (ClassData classData : classDataList)
            if (Objects.equals(classData.getClassName(), className) && Objects.equals(classData.getSeatNo(), seatNo))
                classDataService.deleteClassData(classData.getRollNo());
    }

    boolean checkValidation(String className, Long rollNo, int seatNo) {
        // If both are same discipline id return true
        List<ClassData> classDataList = classDataService.getAllClassData();
        StudentDetails studentDetails = studentService.getStudent(rollNo);
        if (seatNo % 10 > 1) {
//            System.out.println("prev");
            for (int i = 0; i < classDataList.size(); i++) {
                if (Objects.equals(classDataList.get(i).getClassName(), className) && Objects.equals(classDataList.get(i).getSeatNo(), seatNo - 1) && classDataList.get(i).getDisciplineId() == studentDetails.getDisciplineId())
                    return true;
//                System.out.println("prev false");
            }
        }
        if (seatNo % 10 > 0) {
//            System.out.println("next");
            for (int i = 0; i < classDataList.size(); i++) {
                if (Objects.equals(classDataList.get(i).getClassName(), className) && Objects.equals(classDataList.get(i).getSeatNo(), seatNo + 1) && classDataList.get(i).getDisciplineId() == studentDetails.getDisciplineId())
                    return true;
//                System.out.println("next true");
            }
        }
        return false;
    }

    void saveClassData(String className, Long rollNo, int seatNo) {
        StudentDetails studentDetails = studentService.getStudent(rollNo);
//        System.out.println(rollNo);
        ClassData classData = new ClassData();
        classData.setRollNo(rollNo);
        classData.setClassName(className);
        classData.setDisciplineId(studentDetails.getDisciplineId());
        classData.setSeatNo(seatNo);
        classDataService.saveClassData(classData);
    }

    boolean checkNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
//            System.out.println(ex.getMessage());
            return false;
        }
    }


}
