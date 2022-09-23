<%--
  Created by IntelliJ IDEA.
  User: Admin_yfdsou
  Date: 2020/12/31
  Time: 21:23
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
    <title>猿记网</title>
    <link rel="icon" href="${pageContext.request.contextPath}/img/favicon.ico" mce_href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon" />
    <script src="${pageContext.request.contextPath}/js/jquery-2.0.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/ueditor/ueditor.config.js"></script>
    <script src="${pageContext.request.contextPath}/js/ueditor/ueditor.all.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/bootstrap/css/bootstrap.css">

    <style type="text/css">
        *,h3,p{
            margin: 0;
            padding: 0;
        }

        body{
            overflow-x:hidden;
            overflow-y:hidden;
        }

        #text_box{
            width: 100%;
            height: 70%;
            /*margin-bottom: 90px;*/
        }

        .main{
            width: 100%;
            height: 100%;
            margin-top:8px ;
            position: relative;
        }
        .mybut{
            position: fixed;
            bottom: 20px;
            right: 20px;
            z-index: 1000;

        }
    </style>
    <script type="text/javascript">
        $(function(){

            var ue = UE.getEditor('text_box');
            var logContent=$(".mylogcontent").val();
            ue.addListener("ready", function () {
               ue.setContent(logContent);
            });

            var but=$(".toUpdateLogBut")[0];
            but.onclick=function(){
                // 取消关闭退示
                window.onbeforeunload = window.onbeforeunload = function (e) {};

                var content=ue.getContent();//getContentTxt();获取结构文本
                $(".mylogcontent").val(content);

            }
            // 关闭窗口时提示
            window.onbeforeunload = function(e){

                e =  e || window.event;
                e.returnValue=("你写的内容还没有保存！");
            }
        });

    </script>
</head>
<body>

<div class="main">
    <form method="post" action="${pageContext.request.contextPath}/log/logUpdate">
        <input type="hidden" name="logid" value="${logData.log_id}">

        <p>标题</p>
        <input type="text" class="form-control" id="inputEmail3" name="logTitle" placeholder="Nickname" value="${logData.log_title}">
        <p>内容</p>
        <input type="hidden" class="form-control mylogcontent" id="inputPassword3 " placeholder="Personal signature" name=logContent value="${logData.log_content}">
        <div id="text_box"></div>
        <button type="submit" class="btn btn-default mybut toUpdateLogBut ">修改文章</button>
    </form>

</div>
</body>
</html>

