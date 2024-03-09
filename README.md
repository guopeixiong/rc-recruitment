### 项目结构
本项目项目结构模仿开源框架ruoyi, 基本思想大体一致
#### 主要模块
* `rc-application` 系统应用程序模块, 所有业务均在此, 开发时大部分代码主要在此编写, 系统所有接口也均在此, 打包部署时也只需将此模块的jar包取出部署即可, 其中controller目录下的h5为移动端接口, admin为后台管理平台接口
* `rc-common` 公共模块, 主要包含项目公用的注解, 常量, 枚举, 异常, model, 以及工具类, 另外有几个自定义注解, 分别是@Log日志注解, @RateLimiter 限流注解, @RepeatSubmit 防重复提交注解, 均使用在接口上, 具体注解用法查看源码
* `rc-framework` 框架模块, 主要包含项目中使用的第三方框架配置, 以及自定义注解的切面处理类, 过滤器, 全局异常捕捉处理类, 拦截器等都在此模块
* `rc-system` 系统模块, 主要包含用户, 系统配置等, 系统基础功能均在此模块
### 启动前准备
#### 确认环境
> 本项目使用了maven的多环境配置, 在rc-application模块下的pom.xml中, 有如下的配置, 可以进行配置文件的切换
```xml
<profile>
    <id>dev</id>
    <properties>
        <!-- 这里替换成你想要激活的环境, 可选值 dev/test/prod -->
        <env>dev</env>
    </properties>
    <activation>
        <activeByDefault>true</activeByDefault>
    </activation>
</profile>
```
> 在profile.properties.env配置项中选择自己要激活的环境, 后续maven会根据环境不同将不同的配置文件打包进jar包
#### 配置文件密码解密
> 本项目密码使用jasypt加密, 项目中已经内置jasypt的jar包, 无需额外安装.
加密步骤如下
1. cd到项目路径下, 和jasypt-1.9.2.jar在同一目录下运行如下命令
```shell
java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="这是你需要加密的密码" password=这是密码盐值 algorithm=PBEWithMD5AndDES 
```
2. 回车后会输出加密好的密文, 在springboot的配置文件中, 将原本的密码替换为  ENC(加密后的密文)
3. 接着系统环境变量中增加一个新的变量, RC_RECRUITMENT_PASSWORD,变量值就是使用jasypt加密时的密码盐, 添加完记得重启IDEA再启动项目
### 打包部署
> 打包时, 在项目的父模块也就是rc-recruitment下进行操作, 先进行clean操作再进行install, install结束后将rc-application模块下的jar包取出部署即可
`打包时注意区分环境, 选择test/prod环境, 才会启用对应的配置文件`
部署前在服务器系统环境变量中增加一个新的变量, `RC-RECRUITMENT-PASSWORD`,变量值就是使用jasypt加密时的密码盐, 启动后会自动读取该变量用户解密配置文件密码