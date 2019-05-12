# 软件工程专业
- J2EE开发「**JSP程序设计教程**」
  + ~~JSP概述~~
  + ~~JSP开发基础~~
  + ~~JSP语法~~
  + ~~内置对象~~
  + ~~JavaBean技术~~
  + ~~Servlet技术~~
  + ~~JSP实用组件~~
  + ~~JSP数据库应用开发~~
  + JSP高级程序设计
    + ~~Ajax~~
    + ~~EL标签~~
    + ~~JSTL标准标签库~~
  + 个人博客的搭建
  + 在线投票系统
  + Ajax聊天室

# 购物车功能
## 计划增加订单管理系统
### 实现功能
- 生成订单
  + 订单编号
  + 订单时间
  + 用户名
  + 商品名
  + 总金额
  + 派送状态
  + 是否被取消
- 订单管理
  + 删除订单-修改状态
  + 派送订单
- 前端显示订单页面
- 如果有时间,提供**修改密码**的功能

# 该项目的逻辑
### 购物逻辑参考了京东
- **使用了 Flat UI 「基于Boostrap」 、飞入购物车特效「来自GitHub」 、JSP 、JDBC for Mysql 、Ajax 、JS 、XML 等技术**
- 像常见的购物网页一样，打开页面首先显示的应该就是购物界面
- 如果需要购物，可以直接加入购物车，在购物车中进行相关操作
- 如果需要购买，此时就需要登陆账号
- **登陆后可以将未登陆时的购物车与登陆后账号的购物车合并**
- 添加到购物车的时候，对购物车中的商品数量和库存数量进行比较，如果超过了库存数，将不能添加到购物车中
  - *此处使用了Ajax功能修改了提示框中的内容 & 实现了页面不刷新的情况下，往后台添加数据*
- 在购物车中进行购买之后，可以实时刷新库存数量。
- 购买也针对当前用户的购物车进行操作
  - 相同的清空购物车操作也是针对当前用户进行操作


- 注册登陆功能
  - 注册时需要填写username password email & sex
  - 如果注册名在数据库中已存在，通过刷新一个本来无内容的span标签对其进行提示
    - *此处使用了Ajax*
  - 如果注册成功-跳转到登陆页面
    - 可以考虑补充自动登录功能
  - 如果注册失败-跳转回注册页面，并进行提示
    - *此处借用了Flat UI 对其进行错误提示*
  - 登陆页面进行登陆操作-功能判断与注册相似

### 进度
- [x] 生成订单并存持久化

- [x] 查询单个用户的订单,显示在view中

  - [x] show方法

  - [x] 输出到view的表格中

  - [x] 定义按钮功能

- [x] 已实现订单的增删查
  - [ ] 未完成搜索功能,如果要搜索,还要进行权限判断,然而此功能完全没做,如果需要增加此功能需要大改动程序,暂时承受不起改动的精力消耗了.

# 开发时遇到的问题，BUG或者待完善的地方
## BUG
- [x] 在index页面多次点击加入购物车后，页面将会不能控制「目前怀疑是数据库连接的问题，或者是TomCat的问题」，或许是忘了关闭数据库链接「补足了关闭数据库连接的操作，问题解决」
- [x] 使用Ajax向购物车加入商品，处在同一页面，（不刷新的情况下）一个商品只能加入一次「最后发现是添加时上坪数量在第一次添加成功后就写死为1,并且还做了库存数和购物车数的判断,则第二次添加是永远无法通过检测的,故只能怎加一次」
- [x] 购物车和用户绑定时，如果未登陆时购买的商品种类多于登陆用户的购物车种类，则默认删除未登陆用户的其他种类商品「解决办法：在修改登陆用户的商品数量时，加了一个else判断，如果该商品在已登陆用户的购物车中不存在，则将noLogin修改为登陆用户」
- [x] 在doCar中进行购买和清空操作会导致整个shopCar表被清空「解决办法：在sql语句中添加了针对username的判断，即可做到只删除指定用户」
- [ ] 有一定可能出现购物车商品种类重复的问题，目前没有还解决「可能的解决办法：将商品id设置为主键，并在修改shopCar表之前对“是否会出现重复种类”进行判断。」
- [ ] 删除订单的按钮有时候失灵 :+1:
- [ ] 删除订单之后,仍在页面中保留一行空值,很奇怪. :+1:
