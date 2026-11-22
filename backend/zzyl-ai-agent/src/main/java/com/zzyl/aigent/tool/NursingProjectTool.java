package com.zzyl.aigent.tool;

import com.zzyl.nursing.domain.NursingProject;
import com.zzyl.nursing.service.INursingProjectService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component //把这个类加载到内存
public class NursingProjectTool {

    @Autowired
    private INursingProjectService nursingProjectService;

    @Tool(name="find_nursingproject_tool",description = "查询所有护理项目")
    public List<NursingProject> findNursingProject(){
        //调用业务
        return nursingProjectService.list();
    }
}