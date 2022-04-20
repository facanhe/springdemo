package com.example.sprintgdemo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sprintgdemo.entity.student;
import com.example.sprintgdemo.mapper.studentMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class lombokTest {

    @Autowired
    private studentMapper s;

    @Test
    public void test(){
        QueryWrapper<student> studentQueryWrapper=new QueryWrapper<>();
        studentQueryWrapper.eq("age",23);
        //s.selectOne(studentQueryWrapper);
        //s.selectList(studentQueryWrapper);
        //System.out.println(s.selectList(studentQueryWrapper));
        LambdaQueryWrapper<student> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(student::getAge,23);
        s.selectList(studentQueryWrapper);
        System.out.println(s.selectList(studentQueryWrapper));
    }
}
