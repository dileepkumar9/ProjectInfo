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
import org.springframework.web.bind.annotation.*;
import com.example.sb.service.StudentService;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class StudentController {
    // Second Page error and success messages
    String error = "";
    String success = "";

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassDetailService classDetailService;

    @Autowired
    private ClassDataService classDataService;

    //Initial Page or Landing Page
    @RequestMapping("/")
    public String homePage(Model model) {
        error = "";
        success = "";
        List<ClassData> classDataList = classDataService.getAllClassData();
        List<ClassDetails> classDetailsList = classDetailService.getAllClassDetails();
        List<ClassDetails> temp = new ArrayList<>();

        //Calculate and update the available seat in a class
        for (ClassDetails details : classDetailsList) {
            int t = 0;
            for (ClassData classData : classDataList)
                if (Objects.equals(details.getClassName(), classData.getClassName()))
                    t++;
            ClassDetails classDetails = new ClassDetails();
            classDetails.setAvailability(100 - t);
            classDetails.setClassName(details.getClassName());
            classDetailService.saveClassDetail(classDetails);
            temp.add(classDetails);
        }
        model.addAttribute("classDetails", temp);
        return "index";
    }

    //Seat Management Page
    @RequestMapping("/second/{className}")
    public String secondPage(@PathVariable(value = "className") String className, Model model) {
        List<StudentDetails> studentDetailsList = studentService.getAllStudentDetails();
        List<ClassData> classDataList = classDataService.getAllClassData();
        List<ClassData> filterClassData = new ArrayList<>();
        for (ClassData data : classDataList)
            if (Objects.equals(data.getClassName(), className))
                filterClassData.add(data);

        for (int j = 0; j < studentDetailsList.size(); j++)
            for (ClassData classData : classDataList)
                if (Objects.equals(classData.getRollNo(), studentDetailsList.get(j).getRollNo()))
                    studentDetailsList.remove(j);

        //Get seat data from database, show in the page
        List<List<Integer>> a = emptyArray();
        for (ClassData filterClassDatum : filterClassData) {
            int t = (filterClassDatum.getSeatNo() - 1) / 10;
            List<Integer> t1 = a.get(t);
            t1.set((filterClassDatum.getSeatNo() - 1) % 10, filterClassDatum.getRollNo().intValue());
            a.set(t, t1);
        }
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        model.addAttribute("mois", a);
        model.addAttribute("className", className);
        model.addAttribute("listStudentDetails", studentDetailsList);
        return "second";
    }

    //Show Report Page
    @RequestMapping("/third/{className}")
    public String thirdPage(@PathVariable(value = "className") String className, Model model) {
        List<ClassData> classDataList = classDataService.getAllClassData();
        List<ClassData> temp = new ArrayList<>();

        //Show the allocated seat in the page
        for (ClassData classData : classDataList)
            if (Objects.equals(classData.getClassName(), className))
                temp.add(classData);

        model.addAttribute("listStudentDetails", temp);
        return "third";
    }

    //Unallocated the seat for the student
    @RequestMapping("/deleteSeat/{className}")
    public String deleteSeat(@RequestParam MultiValueMap<String, String> parameters, @PathVariable(value = "className") String className) {
        List<String> data = new ArrayList<>(parameters.get("prop"));
        success = "Successfully unallocated the seat";
        //Delete seat no and student data from classData(table)
        classDataService.deleteClassData(Long.parseLong(data.get(0)));
        return "redirect:/second/" + className;
    }

    //Initialize the class and student objects, show empty values.
    @RequestMapping("/addNewData")
    public String addNewData(Model model) {
        model.addAttribute("classData", new ClassDetails());
        model.addAttribute("student", new StudentDetails());
        return "addClass";
    }

    //Get the class details and save to database
    @PostMapping("/addClass")
    public String addClass(@ModelAttribute("classData") ClassDetails classDetails, Model model) {
        classDetails.setAvailability(100);
        classDetailService.saveClassDetail(classDetails);
        model.addAttribute("classData", new ClassDetails());
        model.addAttribute("student", new StudentDetails());
        return "addClass";
    }

    //Get the student details and save to database
    @PostMapping("/deleteClass")
    public String deleteClass(@RequestParam MultiValueMap<String, String> parameters, Model model) {
        classDetailService.deleteClassDetail(new ArrayList<>(parameters.get("prop")).get(0));
        model.addAttribute("classData", new ClassDetails());
        model.addAttribute("student", new StudentDetails());
        return "addClass";
    }

    //Delete student from studentDetails(Table)
    @PostMapping("/deleteStudent")
    public String deleteStudent(@RequestParam MultiValueMap<String, String> parameters, Model model) {
        String rollNo = new ArrayList<>(parameters.get("prop2")).get(0);
        if (checkNumber(rollNo))
            studentService.deleteStudentById(Long.parseLong(rollNo));
        model.addAttribute("classData", new ClassDetails());
        model.addAttribute("student", new StudentDetails());
        return "addClass";
    }

    //Add new student details to database
    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute("student") StudentDetails studentDetails, Model model) {
        studentService.saveStudent(studentDetails);
        model.addAttribute("classData", new ClassDetails());
        model.addAttribute("student", new StudentDetails());
        return "addClass";
    }


    //Initialize the seat with value zero
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
        error = "";
        success = "";
        List<String> data = new ArrayList<>(parameters.get("prop"));
        for (int i = 0; i < data.size(); i++)
            if (checkNumber(data.get(i)))
                if (!checkSameClass(className, Long.parseLong(data.get(i)))) {
                    error = "Already allocated seat in another class";
                } else if (checkSameSeat(Long.parseLong(data.get(i)), i + 1)) {
                    System.out.println("Same Class and seat "+(i+1));
                } else if (checkValidation(className, Long.parseLong(data.get(i)), i + 1)) {
                    error = "Seat No: " + (i + 1) + " unable to arrange the seat";
                } else {
                    success = "Successfully Allocated";
                    deleteClassData(className, i + 1);
                    //Allocate the seat
                    saveClassData(className, Long.parseLong(data.get(i)), i + 1);
                }
        return "redirect:/second/" + className;
    }

    boolean checkSameSeat(Long rollNo, int seatNo) {
        ClassData classData = classDataService.getClassData(rollNo);
        try {
            return Objects.equals(classData.getSeatNo(), seatNo);
        } catch (Exception e) {
            return false;
        }
    }

    void deleteClassData(String className, int seatNo) {
        List<ClassData> classDataList = classDataService.getAllClassData();
        for (ClassData classData : classDataList)
            if (Objects.equals(classData.getClassName(), className) && Objects.equals(classData.getSeatNo(), seatNo))
                classDataService.deleteClassData(classData.getRollNo());
    }

    //Validate the seat with adjacent seats
    boolean checkValidation(String className, Long rollNo, int seatNo) {
        // If both are same discipline id return true
        List<ClassData> classDataList = classDataService.getAllClassData();
        StudentDetails studentDetails = studentService.getStudent(rollNo);
        if (seatNo % 10 > 1) {
            for (ClassData classData : classDataList)
                if (Objects.equals(classData.getClassName(), className) && Objects.equals(classData.getSeatNo(), seatNo - 1) && classData.getDisciplineId() == studentDetails.getDisciplineId())
                    return true;
        }

        if (seatNo % 10 > 0) {
            for (ClassData classData : classDataList)
                 if(Objects.equals(classData.getClassName(), className) && Objects.equals(classData.getSeatNo(), seatNo + 1) && classData.getDisciplineId() == studentDetails.getDisciplineId())
                     return true;
        }
        return false;
    }

    boolean checkSameClass(String className, Long rollNO) {
        ClassData classData = classDataService.getClassData(rollNO);
        try {
            return Objects.equals(classData.getClassName(), className);
        } catch (Exception e) {
            return true;
        }
    }

    //Save the student and seat no in ClassData(Table)
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
