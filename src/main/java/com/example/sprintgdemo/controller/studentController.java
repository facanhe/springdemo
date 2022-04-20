package com.example.sprintgdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sprintgdemo.entity.student;
import com.example.sprintgdemo.service.studentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class studentController {
    @Autowired
    private studentService studentservice;

    @GetMapping("/pagetest")
    public IPage<student> getPage(){
        Page<student> stuPage=new Page<>(2,5);
        LambdaQueryWrapper<student> stuWrapper=new LambdaQueryWrapper<>();
        stuWrapper.orderByAsc(student ::getId);
        return studentservice.page(stuPage,stuWrapper);

    }

    @GetMapping("getById")
    public List<student> getAllStudent(){
        Page<student> stuPage=new Page(1,5);
        LambdaQueryWrapper<student> stuWrapper=new LambdaQueryWrapper<>();
        stuWrapper.orderByAsc(student::getId);
        Page<student> page=studentservice.page(stuPage,stuWrapper);
        return page.getRecords();
    }

    @GetMapping("/findById/{id}")
    public student findById(@PathVariable("id") int id){
        return studentservice.getById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    public boolean deleteById(@PathVariable("id") int id){
        return studentservice.removeById(id);
    }

    @PostMapping("/add")
    public boolean addd(@RequestBody student s){
        return  studentservice.save(s);
    }

    @PutMapping("/update")
    public  boolean update(@RequestBody student s){
        return studentservice.updateById(s);
    }
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
}
