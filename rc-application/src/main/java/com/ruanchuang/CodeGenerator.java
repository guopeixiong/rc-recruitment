package com.ruanchuang;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Property;

/**
 * @Author guopeixiong
 * @Date 2023/7/29
 * @Email peixiongguo@163.com
 */
public class CodeGenerator {

    /**
     * !!! !!! !!! !!! !!!
     * 注意: 生成的mapper的xml文件默认在mapper包的xml子包中, 请手动将xml文件转移到resources目录下的mapper文件夹中
     * !!! !!! !!! !!! !!!
     */

    /**
     * 需要生成的表的名称
     */
    private static String[] tableName = {
            "consulting_and_reply",
            "email_send_record",
            "email_template",
            "index_rolling_image",
            "notice_send_record",
            "notice_template",
            "sign_up_form_question",
            "sign_up_form_template",
            "sign_up_from_answer",
            "sign_up_record_info",
            "template_question_options"};

    /**
     * 生成的文件的存放路径, 一般不用修改, 已经设置好了
     */
    private static String outputDir = System.getProperty("user.dir") + "/rc-application/src/main/java";

    /**
     * 数据库url, 修改为你本地的数据库ip和数据库名字即可
     */
    private static String database = "jdbc:mysql://127.0.0.1:3306/rc_recruitment?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=Asia/Shanghai";

    /**
     * 数据库用户, 默认root, 也可以修改为你自己的
     */
    private static String username = "root";

    /**
     * 密码
     */
    private static String password = "123456";

    /**
     * 设置生成的文件的注释中的作者的名字, 改成你自己的名字即可
     */
    private static String author = "guopeixiong";

    public static void main(String[] args) {
        FastAutoGenerator.create(database, username, password)
                .globalConfig(config -> {
                    config.author(author)
                            .enableSwagger()
                            .outputDir(outputDir)
                            .build();
                })
                .packageConfig(config -> {
                    config.parent("com.ruanchuang")
                            .entity("domain")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .build();
                })
                .strategyConfig(config -> {
                    config.addInclude(tableName)
                            .build();
                    config.entityBuilder()
                            .enableChainModel()
                            .enableLombok()
                            .versionPropertyName("version")
                            .logicDeletePropertyName("isDelete")
                            .columnNaming(NamingStrategy.underline_to_camel)
                            .addTableFills(new Property("createTime", FieldFill.INSERT))
                            .addTableFills(new Property("updateTime", FieldFill.UPDATE))
                            .addTableFills(new Property("createBy", FieldFill.INSERT))
                            .addTableFills(new Property("updateBy", FieldFill.UPDATE))
                            .idType(IdType.ASSIGN_ID)
                            .build();
                    config.controllerBuilder()
                            .enableHyphenStyle()
                            .enableRestStyle()
                            .formatFileName("%sController")
                            .build();
                    config.serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .build();
                    config.mapperBuilder()
                            .formatMapperFileName("%sMapper")
                            .build();
                }).execute();
    }

}
