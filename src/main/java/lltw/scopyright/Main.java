package lltw.scopyright;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
/**
 * @author Sakura
 */
public class Main {
    public static void main(String[] args) {
        //1、创建generator对象
        AutoGenerator autoGenerator = new AutoGenerator();
        //数据源
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setUrl("jdbc:mysql://127.0.0.1:3306/svcopyright?useUnicode=ture&characterEncoding=UTF-8" +
                "&serverTimezone=GMT%2B8");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("123456");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        //设置数据源
        autoGenerator.setDataSource(dataSourceConfig);
        //2、全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir")+"/src/main/java");
        //是否打开输出目录
        globalConfig.setOpen(false);
        //作者
        globalConfig.setAuthor("sakura");
        //设置生成的service接口的名字不带I
        globalConfig.setServiceName("%sService");
        //设置全局配置
        autoGenerator.setGlobalConfig(globalConfig);
        //3、包信息
        //包配置
        PackageConfig packageConfig = new PackageConfig();
        //设置父包名
        packageConfig.setParent("lltw");
        //  设置子包名
        packageConfig.setModuleName("scopyright");
        //设置控制器包名
        packageConfig.setController("controller");
        packageConfig.setService("service");
        packageConfig.setServiceImpl("service.impl");
        packageConfig.setMapper("mapper");
        packageConfig.setEntity("entity");
        //设置包配置
        autoGenerator.setPackageInfo(packageConfig);
        //4、配置策略
        //策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        //是否使用lombok
        strategyConfig.setEntityLombokModel(true);
        //数据库表映射到实体的命名策略
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        //设置策略配置
        autoGenerator.setStrategy(strategyConfig);
        //执行生成
        autoGenerator.execute();
    }
}
