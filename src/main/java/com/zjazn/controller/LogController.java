package com.zjazn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjazn.dao.LogMapper;
import com.zjazn.data.CommentAndUp;
import com.zjazn.data.MinDateLog;
import com.zjazn.interceptor.auth.AuthEnum;
import com.zjazn.interceptor.auth.AuthUtils;
import com.zjazn.interceptor.auth.Root;
import com.zjazn.pojo.Log;
import com.zjazn.pojo.Up;
import com.zjazn.service.CommentService;
import com.zjazn.service.LogService;
import com.zjazn.service.UpService;
import com.zjazn.utils.ClearRichTextStyleUtil;
import com.zjazn.utils.gt.TimeConsumingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("log")
public class LogController {
    @Autowired
    @Qualifier("LogServiceImpl")
    private LogService logService;

    @Autowired
    private LogMapper logMapper;

    @Autowired
    @Qualifier("CommentServiceImpl")
    private CommentService commentService;

    @Autowired
    @Qualifier("UpServiceImpl")
    private UpService upService;

    @RequestMapping("/queryByLogId/{logId}")
    public String inLog_RESTful(@PathVariable("logId") int logid, Model model, HttpServletRequest request){
        return inLog(logid, model, request);
    }
    @RequestMapping("/queryByLogId")
    public String inLog(int logid, Model model, HttpServletRequest request){
//        Map<String,Object> userData = (Map<String,Object>)request.getAttribute("userData");
        Log log = logService.queryByLogId(logid);
        List<CommentAndUp> logComments = commentService.queryByLogId(logid);

        model.addAttribute("logContent",log);
        model.addAttribute("logComments",logComments);

        return "LogContent";
    }
    @RequestMapping("/toUpLog_children")
    public String toUpLog_children(Integer upid, Model model,HttpServletRequest request) throws IOException {

        if (! Root.isSysUser(request,upid)) return "upLog_children";
//        // 封装一初始化数据
//        int nextPage = 1;
//        int pageNumber = 15;
//        List<Log> logs = logService.queryByUpidLimit(upid, (nextPage-1)*pageNumber , pageNumber);
//        //去除文字样式, 让网络传输更快
//        for (Log md:logs){
//            md.setLog_content(ClearRichTextStyleUtil.getShowCharacter(md.getLog_content(),35,"..."));
//        }
//        model.addAttribute("initLog",logs);
        // 查询要审核的条数
        Integer logCheckComplianceCount = logMapper.selectCheckComplianceCountByUpId(upid);
        model.addAttribute("logCheckComplianceCount",logCheckComplianceCount);

        return "upLog_children";
    }
    @RequestMapping("/toLogUpdate")
    public String toLogUpdate(String logid,Model model){
        Log log = logService.queryByLogId(Integer.valueOf(logid));
        //重要:带有样式的代码，一定要先将"替换为'否则，在前端显示会出问题
        log.setLog_content(log.getLog_content().replace("\"","'"));
        model.addAttribute("logData",log);
        return "updateLog";

    }
    //根据logid更新文章
    @RequestMapping("logUpdate")
    public String logUpdate(String logid, String logTitle, String logContent, Model model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Log newlog = new Log();
        newlog.setLog_id(Integer.valueOf(logid));
        newlog.setLog_title(logTitle);
        newlog.setLog_content(logContent);
        Date date = new Date();
        newlog.setLog_update_date(date);
        Log log = logService.queryByLogId(Integer.valueOf(logid));
        model.addAttribute("upid",log.getUp_id());
        int i = logService.updateLog(newlog);
        if(i>0){
            System.out.println("log修改成功");
            return "redirect:/log/toUpLog_children";
        }

        return  "";

    }
    //根据logid删除文章,遇到了一个坑，删除时如果用到旧数据一定要先查
    @RequestMapping("/toDeleteLog")
    public String toDeleteLog(String logid,Model model,HttpServletRequest request, HttpServletResponse response){
        Log log = logService.queryByLogId(Integer.valueOf(logid));
        model.addAttribute("upid",log.getUp_id());

        int i = logService.deleteLog(Integer.valueOf(logid));
        if (i>0){
            System.out.println("log删除成功！");
            return "redirect:/log/toUpLog_children";

        }

        return "";
    }
    //去添加文章页面
    @RequestMapping("/toAddLog")
    public String toAddLog(Model model,HttpServletRequest request){
        if (Root.isSysUser(request)){
            Integer upId = AuthUtils.getDataByHttpRequest(request, AuthEnum.USER_ID.getDataName(), Integer.class);
            model.addAttribute("upid",upId);
            return "AddLogPage";
        }else {
            System.out.println("没有添加权限");
            return "forward:/up/tologin";
        }
    }
    //添加文章程序
    @RequestMapping("/addLogToData")
    public String addLogToData(String upid,String logTitle,String logContent,Model model,HttpServletRequest request){
        if (! Root.isSysUser(request,Integer.parseInt(upid))) return "";
        System.out.println("到了添加文章处理程序");
        System.out.println(upid+";"+logTitle+";"+logContent);
        Log log = new Log();
        log.setUp_id(Integer.valueOf(upid));
        log.setLog_title(logTitle);
        log.setLog_content(logContent);

        Date thisDate = new Date();
        log.setLog_date(thisDate);
        log.setLog_update_date(thisDate);

        int i = logService.addLog(log);
        if(i>0){
            System.out.println("添加成功");
            model.addAttribute("upid",upid);
            return "redirect:/log/toUpLog_children";
        }else {
            System.out.println("添加失败");
        }
        return "";
    }

    @RequestMapping("/getPageNumber")
    @ResponseBody
    public String getPageNumber(Integer onePageNumber,HttpServletResponse response,HttpServletRequest request) throws JsonProcessingException {
        String userName = AuthUtils.getDataByHttpRequest(request, AuthEnum.USER_NAME.getDataName(), String.class);
        Boolean isSysUser = AuthUtils.getDataByHttpRequest(request, AuthEnum.IS_SYS_USER.getDataName(), Boolean.class);
        int colNumber=0;
        if (isSysUser) {
            Up up = upService.queryByUserName(userName);
            colNumber = logService.getOneUpTablePageNumber(up.getUp_id());
        }


        Integer hasPageNumber=colNumber/onePageNumber;
        if((colNumber%onePageNumber)!=0){
            hasPageNumber=Integer.parseInt((hasPageNumber+1)+"");
        }
        HashMap<String,Integer> hashMap = new HashMap();
        hashMap.put("pageNumber",hasPageNumber);
        ObjectMapper om = new ObjectMapper();

        String json = om.writeValueAsString(hashMap);
        System.out.println("后端返回总页数="+hasPageNumber);
        return json;

    }
    @RequestMapping("/myPageing")
    @ResponseBody
    public String myPageing(Integer nextPage,Integer pageNumber,HttpServletRequest request) throws JsonProcessingException {
        System.out.println("初始化后端...");
        System.out.println(nextPage+";"+pageNumber);

        if (Root.isSysUser(request)){
            Integer upId = AuthUtils.getDataByHttpRequest(request, AuthEnum.USER_ID.getDataName(), Integer.class);
            Up up = upService.queryById(upId);
            Integer upid = up.getUp_id();
            System.out.println("初始化分页数据="+upid+";"+nextPage+";"+pageNumber);
            List<Log> logs = logService.queryByUpidLimit(upid, (nextPage-1)*pageNumber , pageNumber);
            //去除文字样式
            for (Log md:logs){
                md.setLog_content(ClearRichTextStyleUtil.getShowCharacter(md.getLog_content(),30,"..."));
            }
            ObjectMapper om = new ObjectMapper();
            String json = om.writeValueAsString(logs);
            return json;

        }else {
            System.out.println("分页程序没有权限");
            return "{}";
        }


    }
    @RequestMapping("getMinDateNumber")
    @ResponseBody
    public String getMinDateNumber() throws JsonProcessingException {

        int minDatePageNumber = logService.getMinDatePageNumber();
        HashMap<String,Integer> numberData=new HashMap<>();
        numberData.put("minDateNumber",minDatePageNumber);
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(numberData);
        return json;

    }
    //获取最新数据
    @RequestMapping("/MinDateLog")
    @ResponseBody
    public String MinDateLog(Integer thisPage,Integer onePageNumber) throws JsonProcessingException {
        List<MinDateLog> logs = logService.queryByMinDate((thisPage-1)*onePageNumber, onePageNumber);
        //去除文字样式
        for (MinDateLog md:logs){
            md.setLog_content(ClearRichTextStyleUtil.getShowCharacter(md.getLog_content(),100));
        }
//        System.out.println((thisPage-1)*onePageNumber+";"+onePageNumber);
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(logs);
        return json;
    }
    @RequestMapping("getUpTopNumberFirst")
    @ResponseBody
    public String getUpTopNumberFirst(Integer upTopNumberFirstNumber) throws JsonProcessingException {
        List<MinDateLog> minDateLogs = logService.queryByUpTop(upTopNumberFirstNumber);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(minDateLogs);
        return json;
    }
    @RequestMapping("/getUpLog")
    @ResponseBody
    public String getUpLog(Integer upid) throws JsonProcessingException {
        List<Log> upLogs = logService.queryByUpId(upid);
        //去除文字样式
        for (Log md:upLogs){
            String showCharacter = ClearRichTextStyleUtil.getShowCharacter(md.getLog_content(), 100);
            md.setLog_content(showCharacter);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(upLogs);
        return json;
    }
    @RequestMapping("/getLogLikeData")
    @ResponseBody
    public String getLogLikeData(String likeValue,Integer requestPageNumber,Integer numberToPage) throws JsonProcessingException {
        System.out.println("后端接收到了前端的搜索内容="+likeValue);
        List<Log> logs = logService.queryByLike(likeValue.trim(),(requestPageNumber-1)*numberToPage,numberToPage);
        System.out.println(logs.toString());
        //去除文字样式
        for (Log md:logs){
            md.setLog_content(ClearRichTextStyleUtil.getShowCharacter(md.getLog_content(),40));
        }
        System.out.println(logs);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(logs);
        return json;

    }
    @RequestMapping("/getLikeNumber")
    @ResponseBody
    public String getLikeNumber(String likeValue) throws JsonProcessingException {
        System.out.println("进入了查询搜索条数的程序了..");
        int likeLogNumber = logService.getLikeLogNumber(likeValue);
        System.out.println(likeLogNumber);
        HashMap<String, Integer> map = new HashMap<>();
        map.put("likeNumber",likeLogNumber);
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(map);

        return json;
    }



}
@Controller
class IndexController {
    @Autowired
    @Qualifier("LogServiceImpl")
    private LogService logService;

    @Autowired
    @Qualifier("CommentServiceImpl")
    private CommentService commentService;

    @Autowired
    @Qualifier("UpServiceImpl")
    private UpService upService;

    @RequestMapping("/")
    public String toIndex(Model model) throws JsonProcessingException {
        System.out.println("准备跳转到首页！！");
        List<MinDateLog> pointTops = logService.queryByUpTop(10);

        /*http://localhost:8080/log/MinDateLog?thisPage=1&onePageNumber=15*/
        /*MinDateLog(Integer thisPage,Integer onePageNumber)*/

        List<MinDateLog> newLogs = logService.queryByMinDate(0, 15);
        //去除文字样式
        for (MinDateLog md:newLogs){
            md.setLog_content(ClearRichTextStyleUtil.getShowCharacter(md.getLog_content(),150));

        }

        model.addAttribute("newLogs",newLogs);
        model.addAttribute("pointTops",pointTops);
        return "index_";
    }

}
