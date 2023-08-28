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

本项目采用前后端分离架构，后端采用SpringBoot、SpringCloud技术栈开发，数据库使用了

MySQL，还使用的Redis、消息队列、分布式文件系统、Elasticsearch等中间件系统。

划分的微服务包括：内容管理服务、媒资管理服务、搜索服务、订单支付服务、 学习中心服务、系统

管理服务、认证授权服务、网关服务、注册中心服务、配置中心服务等。

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





