package com.example.sprintgdemo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sprintgdemo.mapper.studentMapper;
import com.example.sprintgdemo.entity.student;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class studentService extends ServiceImpl<studentMapper,student> {

}
