package com.example.sprintgdemo.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sprintgdemo.entity.student;
import com.example.sprintgdemo.service.studentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class studentController {
    @Autowired
    private studentService studentservice;
    @Resource
    private HttpServletRequest request;

    @GetMapping("/pagetest")
    public IPage<student> getPage(){
        Page<student> stuPage=new Page<>(2,5);
        LambdaQueryWrapper<student> stuWrapper=new LambdaQueryWrapper<>();
        stuWrapper.orderByAsc(student ::getId);
        return studentservice.page(stuPage,stuWrapper);

    }

    @GetMapping("getById")
    public List<student> getAllStudent(HttpServletResponse response) throws IOException {
        Page<student> stuPage=new Page(1,5);
        LambdaQueryWrapper<student> stuWrapper=new LambdaQueryWrapper<>();
        stuWrapper.orderByAsc(student::getId);
        Page<student> page=studentservice.page(stuPage,stuWrapper);
        List<student> querylist=page.getRecords();
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for(student stu:querylist){
            Map<String,Object> row=new LinkedHashMap<>();
            row.put("学号",stu.getId());
            row.put("姓名",stu.getName());
            row.put("年龄",stu.getAge());
            row.put("爱好",stu.getHoppy());
            list.add(row);
        }
        ExcelWriter writer= ExcelUtil.getWriter(true);
        writer.write(list,true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName= URLEncoder.encode("学生信息表","UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        //log.info("导出Excel文件："+fileName+".xlsx");
        ServletOutputStream out=response.getOutputStream();
        writer.flush(out,true);
        writer.close();
        IoUtil.close(System.out);
        return querylist;
    }

    @GetMapping("getlimit")
    public List<student> getlimit(){
        LambdaQueryWrapper<student> stuWrapper=new LambdaQueryWrapper<>();
        stuWrapper.orderByDesc(student::getId);
        stuWrapper.between(student::getAge,10,25);
        stuWrapper.like(student::getHoppy,"摇");
        String clientIpAddress = ServletUtil.getClientIP(request, null);
        System.out.println(clientIpAddress);
        return studentservice.list(stuWrapper);
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
