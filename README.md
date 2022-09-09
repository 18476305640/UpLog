# UpLog
# 猿记网，一个笔记分享社区项目！

## 如何部署？

1、修改 src/main/resources/database.properties 下的数据库信息

2、将项目根目录下的 uplog.sql 导入数据库 

3、执行 “mvn package” 

4、启动tomcat服务器，将打包的war放在tomcat服务器下的webapps目录下即可 

5、根据http://ip:tomcat端口 访问即可！

详细教程：https://www.cnblogs.com/zjazn/p/15619456.html

效果：<a href="http://124.222.229.234/">点击查看演示</a>

6、未来规划
   1) 加入专辑,查看博主主页可以看到该博主的全部专辑,点击专辑查询替换当前最新的发布的博客, 让博客也有笔记的属性
     
   2) 查看一个博客可以看到该博客所属的专辑的其它博客标题
    
   3) 可以查看博客有多少浏读量, 点赞数

