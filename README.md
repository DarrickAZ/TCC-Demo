# Milo

#### 介绍
史上最简单的分布式事务TCC解决方案

#### 软件架构
milo采用java语言开发，是分布式事务TCC模式的落地解决方案，TCC模式下的分布式事务是所有分布式事务解决方案中实时性要求最高的，同时也是对业务开发成本要求最高的。所以TCC模式会在要求实时性较高的分布式事务场景中使用，例如订单支付这样的场景。

#### 测试样例
![输入图片说明](https://images.gitee.com/uploads/images/2019/1029/140552_d1ef13db_1500210.png "20190906154935615.png")


#### 特性

- 基于TCC模式实现的分布式事务框架。
- 基于mysql存储事务日志。
- 和springcloud微服务完美融合。
- 具有事务处理异常自恢复能力。
- 样例测试详细。
- 事务日志采用kryo序列化存储


#### 模块解析
- milo-common：框架公共代码模块
- milo-core：框架核心代码模块
- milo-springcloud：框架整合springcloud模块
- milo-spring-cloud-starter：框架支持springcloud starter模块
- cloud-eureka：测试样例注册中心
- cloud-order：测试样例订单服务
- cloud-stock：测试样例库存服务
- cloud-account：测试样例账户服务


#### 使用条件

1. 你必须使用jdk1.8及以上版本
2. 你必须使用的是springcloud框架
3. 你必须采用mysql存储事务日志

#### 源码解析

CSDN博客：[分布式事务 Milo源码解析](https://blog.csdn.net/u010739551/article/category/9282823)

#### 参与贡献

作者：luke
简介：深圳某IOT公司担任高级工程师。专注于Java、SpringBoot、SpringCloud、微服务、Docker、Kubernetes、分布式事务、多线程、DevOps等领域。
联系方式 QQ：347535420 电话：13247635446