package com.example.sprintgdemo.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.sprintgdemo.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sprintgdemo.entity.student;
import com.example.sprintgdemo.service.studentService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
        /*List<Map<String, Object>> list = CollUtil.newArrayList();
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
        IoUtil.close(System.out);*/
        return querylist;
    }

    @GetMapping("getlimit")
    public List<student> getlimit(){
        LambdaQueryWrapper<student> stuWrapper=new LambdaQueryWrapper<>();
        stuWrapper.orderByDesc(student::getId);
        stuWrapper.between(student::getAge,10,25);
        stuWrapper.like(student::getHoppy,"摇");
        String clientIpAddress = ServletUtil.getClientIP(request, null);
        String clientIpAddress1= IpUtil.getIpAddr(request);
        System.out.println(clientIpAddress);
        System.out.println(clientIpAddress1);
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

    @PostMapping("/update/import")
    public boolean updatebatch(@RequestParam(value = "File") MultipartFile file)throws Exception{
        String fileName=file.getOriginalFilename();
        //int result=studentservice.importExcel();
        List<student> stulist=new ArrayList<>();
        String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
        Workbook wb=null;
        try{
            InputStream ins=file.getInputStream();
            if(suffix.equals("xls")){
                wb=new HSSFWorkbook(ins);
            }else{
                wb=new XSSFWorkbook(ins);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        Sheet sheet=null;
        try {
            sheet = wb.getSheetAt(0);
        } catch (Exception e) {
            //log.error("sheet页不存在!");
            e.printStackTrace();
        }
        if(sheet!=null){
            for(int line=1;line<=sheet.getLastRowNum();line++){
                student stu=new student();
                Row row=sheet.getRow(line);
                if(row==null) continue;
                for(int i=0;i<4;i++){
                    if(row.getCell(i)==null){
                        row.createCell(i);
                    }
                    row.getCell(i).setCellType(CellType.STRING);
                }
                    String id = row.getCell(0).getStringCellValue();
                    String age = row.getCell(2).getStringCellValue();
                    String name = row.getCell(1).getStringCellValue();
                    String hoppy = row.getCell(3).getStringCellValue();
                    System.out.println(id);
                    if(id!="") {stu.setId(Integer.parseInt(id));}
                    stu.setName(name);
                    if(age!=""){stu.setAge(Integer.parseInt(age));}
                    stu.setHoppy(hoppy);
                    stulist.add(stu);

                   /* String age = row.getCell(2).getStringCellValue();
                    String name = row.getCell(1).getStringCellValue();
                    String hoppy = row.getCell(3).getStringCellValue();
                    stu.setName(name);
                    stu.setAge(Integer.parseInt(age));
                    stu.setHoppy(hoppy);
                    stulist.add(stu);*/


            }
        }

        return studentservice.saveOrUpdateBatch(stulist);
    }
}
