# 开发运行

## 前置条件
1. 在IDEA中安装并配置`Lombok`插件
    1. (安装`Lombok`插件)[https://projectlombok.org/setup/intellij]
    2. 在IDEA中左上角点击 `file` -> `setting` -> `Build, Execution, Deployment` -> `Compiler, Annotation Processors`。勾选`Enable Annotation Processing`
2. 系统中已安装和配置`Gradle`
    1. (官网)[https://gradle.org/]
    
# 服务器部署

## 服务器环境
```
操作系统：Ubuntu 16.04
JDK版本：1.8
Mysql版本：5.7
```

## 数据库配置
- 初始化数据库：执行`resources`文件夹下的`sql.sql`文件
- 修改数据库配置
  - 开发环境配置修改：`resources`文件夹下的`application-dev.yml`文件
  - 本地环境配置修改：`resources`文件夹下的`application-pord.yml`文件
  - 修改字段为`spring.datasource.druid.url`
- 注意同时修改power-mqtt项目中的数据库配置，相同位置
- 注意同时修改power-mqtt项目中的数据库配置，相同位置
- 注意同时修改power-mqtt项目中的数据库配置，相同位置

## 步骤
1. 将项目打包成Jar包
2. 上传到服务器
3. 运行`nohup java -jar [文件名].jar &`命令启动应用