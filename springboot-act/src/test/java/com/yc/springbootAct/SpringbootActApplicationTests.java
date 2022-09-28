package com.yc.springbootAct;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SpringbootActApplicationTests {

    @Autowired
    private ProcessEngine engine;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    private String key = "myProcess";
    private String assignee1 = "zl";
    private String group = "shr";

    @Test
    void contextLoads() {
    }

    @Test
    public void deploy(){
        //RepositoryService repositoryService = engine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/groupProcess.bpmn")
                .addClasspathResource("processes/groupProcess.png")
                .name("请假审核审批")
                .deploy();
        //4输出流程部署信息
        System.out.println("流程部署的id:" + deploy.getId());
        System.out.println("流程部署的名称：" + deploy.getName());
    }

    @Test
    public void start(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("assignee",assignee1);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, params);
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("流程实例id：" + processInstance.getProcessInstanceId());
        System.out.println("流程定义名称：" + processInstance.getProcessDefinitionName());
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程定义key：" + processInstance.getProcessDefinitionKey());

        System.out.println(123);
    }

    @Test
    public void findTask(){
        List<Task> list = taskService.createTaskQuery().taskAssignee(assignee1).list();
        for (Task task : list) {
            System.out.println("任务id:" + task.getId());
            System.out.println("任务执行人:" + task.getAssignee());
            System.out.println("流程定义ID:" + task.getProcessDefinitionId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务执行ID" + task.getExecutionId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("-----------------------------------------------------");
        }


    }

    @Test
    public void findTaskGroup(){

        List<Task> list = taskService.createTaskQuery().taskCandidateGroup(group).list();
        for (Task task : list) {
            System.out.println("任务id:" + task.getId());
            System.out.println("任务执行人:" + task.getAssignee());
            System.out.println("流程定义ID:" + task.getProcessDefinitionId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务执行ID" + task.getExecutionId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("-----------------------------------------------------");
        }


    }

    @Test
    public void claimTask(){
        String taskId = "0132787f-3899-11ed-94ef-00ff398a46ff";
        taskService.claim(taskId,assignee1);
    }

    @Test
    public void backTask(){
        String taskId = "0132787f-3899-11ed-94ef-00ff398a46ff";
        taskService.setAssignee(taskId,null);
    }

    @Test
    public void  complete(){
        String taskId = "f53e6476-3896-11ed-941f-00ff398a46ff";
        Task task = taskService.createTaskQuery().taskId(taskId).taskAssignee(assignee1).singleResult();
        if(task != null){
            taskService.complete(taskId);
            System.out.println("完成任务");
        }else{
            System.out.println("没有任务执行权限");
        }
    }



}
