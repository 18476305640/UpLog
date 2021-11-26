# UpLog2
# 猿记网，一个笔记分享社区项目！

## 如何部署？

1、修改 src/main/resources/database.properties 下的数据库信息

2、将项目根目录下的 uplog.sql 导入数据库 

3、执行 “mvn package” 

4、启动tomcat服务器，将打包的war放在tomcat服务器下的webapps目录下即可 

5、根据http://ip:tomcat端口 访问即可！
