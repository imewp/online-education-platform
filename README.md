# online-education-platform
该项目属于运营类的项目，是一个专门针对成人职业技能教育的网络课堂系统，网站提供了成人职业技能培训的相关课程。

项目基于B2B2C的业务模式，培训机构可以在平台入驻、发布课程，运营人员对发布的课程进行审、

核，审核通过后课程才可以发布成功，课程包括免费和收费两种形式，对于免费课程可以直接选课学

习，对于收费课程在选课后需要支付成功才可以继续学习。

> **B2B2C**：一种电子商务类型的网络购物商业模式，B（Business），C（Consumer），第一个B指的是商品或服务的供应商，第二个B指的是从事电子商务的企业，C则是表示消费者。
>
> **B2B**：企业跟企业之间的电子商务运作方式
>
> **B2C**：企业跟消费者之间的电子商务运作方式

## 项目介绍

本项目采用前后端分离架构，后端采用SpringBoot、SpringCloud技术栈开发，数据库使用了MySQL，还使用的Redis、消息队列、分布式文件系统、Elasticsearch等中间件系统。

划分的微服务包括：内容管理服务、媒资管理服务、搜索服务、订单支付服务、 学习中心服务、系统管理服务、认证授权服务、网关服务、注册中心服务、配置中心服务等。

技术架构图：

<img src="https://s2.loli.net/2023/08/21/FzJk8dBQaTofVIt.png" alt="image.png"  />

各层说明：

|   名称   | 功能描述                                                     |
| :------: | :----------------------------------------------------------- |
|  用户层  | 用户层描述了本系统所支持的用户类型包括：PC用户、APP用户、H5用户。pc用户通过浏览器访问系统、APP用户通过Android、IOS手机访问系统，H5用户通过H5页面访问系统。 |
|   CDN    | **CDN**（Content Delivery NetWork）即内容分发网络，本系统所有静态资源全部通过CDN加速来提高访问速度。系统静态资源包括：html页面、js文件、css文件、image图片、pdf和ppt及doc教学文档、video视频等。 |
| 负载均衡 | 系统的CDN层、UI层、服务层及数据层均设置了负载均衡服务，上图仅在UI层前边标注了负载均衡。 每一层的负载均衡会根据系统的需求来确定负载均衡器的类型，系统支持4层负载均衡+7层负载均衡结合的方式，4层负载均衡是指在网络传输层进行流程转发，根据IP和端口进行转发，7层负载均衡完成HTTP协议负载均衡及反向代理的功能，根据url进行请求转发。 |
|   UI层   | UI层描述了系统向PC用户、APP用户、H5用户提供的产品界面。根据系统功能模块特点确定了UI层包括如下产品界面类型：<br/>1）面向PC用户的门户系统、学习中心系统、教学管理系统、系统管理中心。<br/>2）面向H5用户的门户系统、学习中心系统。 <br/>3）面向APP用户的门户系统、学习中心系统。 |
| 微服务层 | 微服务层将系统服务分类三类：业务服务、基础服务、第三方代理服务。<br/>业务服务：主要为学成在线核心业务提供服务，并与数据层进行交互获得数据。<br/>基础服务：主要管理学成在线系统运行所需的配置、日志、任务调度、短信等系统级别的服务。<br/>第三方代理服务：系统接入第三方服务完成业务的对接，例如认证、支付、视频点播/直播、用户认证和授权。 |
|  数据层  | 数据层描述了系统的数据存储的内容类型。<br/>关系性数据库：持久化的业务数据使用MySQL。 <br/>消息队列：存储系统服务间通信的消息，本身提供消息存取服务，与微服务层的系统服务连接。<br/>索引库：存储课程信息的索引信息，本身提供索引维护及搜索的服务，与微服务层的系统服务连接。<br/>缓存：作为系统的缓存服务，作为微服务的缓存数据便于查询。<br/>文件存储：提供系统静态资源文件的分布式存储服务，文件存储服务器作为CDN服务器的数据来源，CDN上的静态资源将最终在文件存储服务器上保存多份。 |

项目采用 **Spring Cloud Gateway** 作为网关，网关在请求路由时需要知道每个微服务实例的地址，项目使用 **Nacos** 作用服务发现中心和配置中心，整体的架构图如下：

<img src="https://s2.loli.net/2023/09/01/sfNmenAqkIu4pBO.png" alt="image.png" style="zoom:90%;" />

流程如下：

1. 微服务启动，将自己注册到Nacos，Nacos记录了各微服务实例的地址。

2. 网关从Nacos读取服务列表，包括服务名称、服务地址等。

3. 请求到达网关，网关将请求路由到具体的微服务。


## 功能设计与实现

### 1. 视频处理

#### 作业分片方案

**问题**：任务添加成功后，对于要处理的任务会添加到待处理任务表中，现在启动多个执行器实例去查询这些待处理任务，此时如何保证多个执行器不会查询到重复的任务呢？

XXL-JOB 并不直接提供数据处理的功能，它只会给执行器分配好分片序号，在执行器任务调度的同时下发*分片总数*以及*分片序号*等参数，执行器收到这些参数根据自己的业务需求取利用这些参数。

下图表示了多个执行器获取视频处理任务的结构：

<img src="https://s2.loli.net/2024/01/21/TVGBkpvr2DN3hga.png" alt="image.png" style="zoom:50%;" />

每个执行器收到广播任务有两个参数：分片总数、分片序号。每个执行从数据表取任务时可以让任务id模上分片总数，如果等于分片总数则执行此任务。

#### 保证任务不重复执行

**问题**：通过作业分片方案保证了执行器之间查询到不重复的任务，如果一个执行器在处理一个视频还没有完成，此时调用中心又一次请求调度，为了不重复处理同一个视频该怎么办？

1. 配置**调度过期策略**：调度中心错过调度时间的补偿处理策略，包括：忽略、立即补偿触发一次等；

   * **忽略**：调度过期后，忽略过期的任务，从当前时间开始重新计算下次触发时间；
   * **立即执行一次**：调度过期后，立即执行一次，并从当前时间开始重新计算下次触发时间；

2. 配置**阻塞处理策略**：调度过于密集执行器来不及处理时的处理策略（当前执行器正在执行任务还没有结束时调度中心进行任务调度）；

   * **单机串行**（默认）：调度请求进入单机执行器后，调度请求进入FIFO队列并以串行方式运行；
   * **丢弃后续**调度：调度请求进入单机执行器后，发现执行器存在运行的调度任务，本次请求将会被丢弃并标记为失败；
   * **覆盖之前**调度：调度请求进入单机执行器后，发现执行器存在运行的调度任务，将会终止运行中的调度任务并清空队列，然后运行本地调度任务；

3. 保证任务处理的**幂等性**：对于数据的操作不论多少次，操作的结果始终是一致的。

   > 幂等性：描述了一次和多次请求某一个资源对于资源本身应该具有同样的结果。是为了解决重复提交的问题。比如：恶意刷单，重复支付等。

   * 数据库约束：比如：唯一索引，主键。
   * 乐观锁，常用于数据库，更新数据时根据乐观锁状态去更新。
   * 唯一序列号，操作传递一个唯一序列号，操作时判断与该序列号相等则执行。

#### 视频处理方案

视频上传以处理的业务流程：

<img src="https://s2.loli.net/2024/01/21/m7HFpag9ZeIuEkT.png" alt="image.png" style="zoom:100%;" />


















## 知识点

### 模型类

1. `DTO`（数据传输对象）：用于接口层与业务层之间传输对象
2. `PO`（持久化对象）：用于业务层与持久层之间传输对象
3. `VO`（数据表示对象）：用于前端与接口层之间传输对象

<img src="https://s2.loli.net/2023/08/25/NnIrMw2ATEckb6U.png" alt="image.png" style="zoom:80%;" />

### 跨域问题

`CORS` 全称是 cross origin resource share 表示跨域资源共享。

出现 *No 'Access-Control-Allow-Origin' header is present on the requested resource.* 是因为基于浏览器的同源策略，去判断是否跨域请求，同源策略是浏览器的一种安全机制。从一个地址请求另一个地址，如果协议、主机、端口三者全部一致则不属于跨域，否则又一个不一致就是跨域请求。

> 比如：
>
> 从http://localhost:8601 到  http://localhost:8602 由于端口不同，是跨域。
>
> 从http://192.168.101.10:8601 到  http://192.168.101.11:8601 由于主机不同，是跨域。
>
> 从http://192.168.101.10:8601 到  [https://192.168.101.10:8601](https://192.168.101.11:8601) 由于协议不同，是跨域。
>
> 注：**服务器之间不存在跨域请求**。

解决跨域的方法：

1. `JSONP`：通过script标签的src属性进行跨域请求，如果服务端要响应内容则首先读取请求参数callback的值，callback是一个回调函数的名称，服务端读取callback的

   值后将响应内容通过调用callback函数的方式告诉请求方。如下图：

   ![image.png](https://s2.loli.net/2023/08/25/EBQ2RvYty5hrXHA.png)

2. **添加响应头**：服务端在响应头添加 `Access-Control-Allow-Origin：*`
3. **通过nginx代理跨域**：由于服务端之间没有跨域，浏览器通过nginx去访问跨域地址。

### Nacos配置

SpringBoot读取配置文件的顺序如下：

![image.png](https://s2.loli.net/2023/09/03/nsDS5actQrE6FZV.png)

引入配置文件的形式有：

1. 以项目应用名方式引入
2. 以**扩展配置**文件方式引入
3. 以**共享配置**文件方式引入
4. 本地配置文件

> 各配置文件的优先级：项目应用名配置文件 > 扩展配置文件 > 共享配置文件 > 本地配置文件

### spring事务问题

todo：第三章 媒资管理模块 **Service事务优化**  待完善文档

### 分布式任务调度

**任务调度顾名思义，就是对任务的调度，它是指系统为了完成特定业务，基于给定时间点，给定时间间隔或者给定执行次数自动执行任务。**

**分布式任务调度**：通常任务调用的程序是集成在应用中的，比如：优惠卷服务中包括了定时发放优惠卷的的调度程序，结算服务中包括了定期生成报表的任务调度程序。由于采用分布式架构，一个服务往往会部署多个冗余实例来运行我们的业务，在这种分布式系统环境下运行任务调度，我们称之为**分布式任务调度**，如下图：

<img src="https://s2.loli.net/2023/12/18/yGCPH2F8Yn5UZka.png" alt="image.png" style="zoom:40%;" />

不管是任务调度程序集成在应用程序中，还是单独构建的任务调度系统，如果采用分布式调度任务的方式就相当于将任务调度程序分布式构建，这样就可以具有分布式系统的特点，并且提高任务的调度处理能力：

1. 并行任务调度

   * 并行任务调度实现靠多线程，如果有大量任务需要调度，此时光靠多线程就会有瓶颈了，因为一台计算机CPU的处理能力是有限的。

   * 如果将任务调度程序分布式部署，每个结点还可以部署为集群，这样就可以让多台计算机共同去完成任务调度，我们可以将任务分割为若干个分片，由不同的实例并行执行，来提高任务调度的处理效率。

2. 高可用

   若某一个实例宕机，不影响其他实例来执行任务。

3. 弹性扩容

   当集群中增加实例就可以提高并执行任务的处理效率。

4. 任务管理与监测

   对系统中存在的所有定时任务进行统一的管理及监测。让开发人员及运维人员能够时刻了解任务执行情况，从而做出快速的应急处理响应。

5. 避免任务重复执行

   当任务调度以集群方式部署，同一个任务调度可能会执行多次，比如在上面提到的电商系统中到点发优惠券的例子，就会发放多次优惠券，对公司造成很多损失，所以我们需要控制==相同的任务在多个运行实例上只执行一次==。

#### XXL-JOB

[XXL-JOB](https://www.xuxueli.com/xxl-job/#%E3%80%8A%E5%88%86%E5%B8%83%E5%BC%8F%E4%BB%BB%E5%8A%A1%E8%B0%83%E5%BA%A6%E5%B9%B3%E5%8F%B0XXL-JOB%E3%80%8B) 是一个轻量级分布式任务调度平台，其核心设计目标是开发迅速、学习简单、轻量级、易扩展。

XXL-JOB主要有调度中心、执行器、任务：

<img src="https://s2.loli.net/2023/12/27/HIMif9htdBn3cQa.png" alt="image.png" style="zoom:50%;" />

**调度中心：**

* 负责管理调度信息，按照调度配置发出调度请求，自身不承担业务代码；
* 主要职责为执行器管理、任务管理、监控运维、日志管理等

**任务执行器：**

* 负责接收调度请求并执行任务逻辑；
* 只要职责是注册服务、任务执行服务（接收到任务后会放入线程池中的任务队列）、执行结果上报、日志服务等

**任务：**负责执行具体的业务处理。

调度中心与执行器之间的工作流程如下：

<img src="https://s2.loli.net/2023/12/27/Xa9QunRV5BbwgCc.png" alt="image.png" style="zoom:50%;" />

执行流程如下：

1. 任务执行器根据配置的调度中心的地址，自动注册到调度中心
2. 达到任务触发条件，调度中心下发任务
3. 执行器基于线程池执行任务，并把执行结果放入内存队列中、把执行日志写入日志文件中
4. 执行器消费内存队列中的执行结果，主动上报给调度中心
5. 当用户在调度中心查看任务日志，调度中心请求任务执行器，任务执行器读取任务日志并返回日志详情

> 面试题：XXL-JOB的工作原理是什么？XXL-JOB是怎么工作？
>
> XXL-JOB 分布式任务调度服务由调用中心和执行器组成，调用中心负责按任务调度策略向执行器发任务，执行器负责接收任务、执行任务。
>
> 1. 首先部署并启动XXL-JOB调度中心。
> 2. 在微服务添加XXL-JOB依赖，在微服务中配置执行器。
> 3. 启动微服务，执行器向调度中心上报自己。
> 4. 在微服务中写一个方法并用XXL-JOB注解去标记执行任务的方法名称。
> 5. 在调度中心配置任务调度策略，调度策略就是每隔多长时间执行还是在每天或每月的固定时间去执行，比如每天0点执行，或每隔1小时执行一次等。
> 6. 在调度中心启动任务。
> 7. 调度中心根据任务调度策略，到达时间就开始下发任务给执行器。
> 8. 执行器收到任务就开始执行任务。
