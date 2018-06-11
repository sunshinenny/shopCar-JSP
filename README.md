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


# 该项目的思维逻辑
- **使用了 Flat UI 、飞入购物车特效「来自GitHub」 、JSP 、JDBC for Mysql 、Ajax 、JS 、XML 等技术**
- 像常见的购物网页一样，打开页面首先显示的应该就是购物界面
- 如果需要购物，可以直接加入购物车，在购物车中进行相关操作
- 如果需要购买，此时需要登陆账号「注：这个操作在本项目中被有意识的忽视了。原因-暂时无法做到购物车和个人用户的数据绑定 & 偷懒」
- 添加到购物车的时候，对购物车中的商品数量和库存数量进行比较，如果超过了库存数，将不能添加到购物车中「此处使用了Ajax功能修改了提示框中的内容 & 实现了页面不刷新的情况下，往后台添加数据」
- 在购物车中进行购买之后，可以实时刷新库存数量。


- 再说一下注册登陆功能
  - 注册时需要填写username password email & sex
  - 实际数据库设计的时候 还准备了favorite & introduction「但是事实就是没有做，因为懒」
  - 如果注册名在数据库中已存在，通过刷新一个本来无内容的span标签对其进行提示
    - 此处使用了Ajax技术
  - 如果注册成功-跳转到登陆页面「**或许可以考虑实现注册成功后自动登陆**」
  - 如果注册失败-跳转回注册页面，并进行提示
    - 此处借用了Flat UI 对其进行错误提示
  - 登陆页面进行登陆操作-功能判断与注册相似


# 开发时遇到的问题，bug或者待完善的地方
#开发
## BUG
- [x] 在index页面多次点击加入购物车后，页面将会不能控制::「目前怀疑是数据库连接的问题，或者是TomCat的问题」，或许是忘了关闭数据库链接::「最终添加了关闭数据库连接的操作」
- [x] 使用Ajax向购物车加入商品，处在同一页面，（不刷新的情况下）一个商品只能加入一次::「最后发现是添加时上坪数量在第一次添加成功后就写死为1,并且还做了库存数和购物车数的判断,则第二次添加是永远无法通过检测的,故只能怎加一次」::

## 优化
- [x] 使用Ajax辅助index页面添加商品进入购物车，操作难度应该不大
- [x] Ajax对用户名查重时，没有将post和get方法进行整合
- [x] 使用Ajax，将zc和login页面进行简化，减少::重定向::的使用
- [x] 页面布局仍存在一些问题，参照Flat的开发文档对其进行修改
- [ ] 在shopCarOptionDao中，有关商品数量操作中，只判断了「只限于该商品只存在一种」的情况，如果有多个商品名称不一样，会出现bug::解决办法：使用ID对其进行定位，而不是name::
``` java
while (getSingle.next()) {
					// 遍历ReaultSet对象
					// 获取已知的商品数量
					int singleNum = getSingle.getInt("num") + 1;
					/**
					 * 直接写在循环中是因为相信同样的商品在数据库中只会出现一次
					 * 但也有问题：如果这个数据库存在“编号不相同，但是名字相同的商品时，此处就会出现修改数量错误的bug”
					 */
					Statement wantAddOneSingleStatement = conn.createStatement();
					wantAddOneSingleStatement
							.executeUpdate("UPDATE shopCar SET num=" + singleNum + " where name='" + name + "' ");
					return true;
				}
```
- [ ] shopCarOptionDao中连接数据库的语句出现了大量重复「每个方法中都存在」，在类定义的时候也存在::调试解决::

## Java
- Unknown initial character set index '255' received from server. Initial client character set can be forced via the 'characterEncoding' property.
	- 解决方法：**将过时的JDBC驱动更新到了最新版**
