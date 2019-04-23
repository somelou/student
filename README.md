# student

一个android初学者的demo app，简单学生信息管理应用

用户名：admin，密码：123456

## 环境

IDE:Android Studio

minSdkVersion:19

targetSdkVersion:28

数据库:sqlite

Gradle Version:4.10.1

Plugin Version:3.3.2

jdk:1.8

##  功能

1. ActivityMain有一个Button和一个ListView控件，ListView用于显示若干个学生的基本信息（显示的内容包括：学生图片<随便>、学生姓名、学生学院及专业）。

2. 头像根据用户名生成

3. 点击Button控件，跳转到ActivityStudent。

4. ActivityStudent上实现学生信息的录入，包括：

   姓名（编辑框），

   学号（编辑框，只能输入数字），

   性别（两个RadioButton） ，

   学院（下拉框，可选：计算机学院、电气学院），

   专业（下拉框，与学院实现二级联动。计算机学院有软件工程、信息安全、物联网；电气学院有电气工程、电机工程），

   爱好（多个复选框：文学、体育、音乐、美术），

   出生日期（输入出生日期）。

5. 数据保存到数据库，数据库使用sqlite。

6. 在ActivityStudent设置一个提交按钮Button，点击该按键，跳转到ActivityMain，在ActivityMain的ListView上增加一个子项，显示（3）中输入的学生信息。上机3：使用复杂View编写程序

7. 增加一个Activity（ActivityLogin）实现用户登录，ActivityLogin上包含用户名（编辑框），密码（编辑框），进度条（圆形）和登录按钮。

8. 点击登录按钮时，进度条动画开始显示，3秒之后，验证用户名和密码（目前设为常量）。验证通过后进入ActivityMain界面。

9. 处理ActivityMain界面的ListView项长按事件，即长按ListView项时，弹出菜单。

10. 菜单上有两项功能：编辑和删除。

  点击编辑时跳转到ActivityStudent界面，修改学生信息；

  点击删除时，删除该条学生信息，删除之前让用户进行确认。

11. 为程序增加系统菜单，菜单上有：增加、刷新两项功能，用来增加学生和刷新学生列表。目前刷新并没有什么实际功能。

12. 在学生ActivityStudent上的出生日期改为DatePickerDialog选择学生生日。

13. 当添加一个学生记录成功后、删除一个学生成功后，用Toast向用户显示提示信息。要求Toast上带图标

14. 在ActivityMain界面上增加一个查询按钮，点击查询按钮，弹出对话框，输入查询关键词，点击确定后开始查询（搜索学生姓名、学院和专业字段）。如果查询到结果，ActivityMain上只保留查询结果；如果没有查询到结果，用Toast提示没有查询到结果。

15. 原来登录成功后会显示的是3秒进度条，现在要求改成：登录成功后，显示一个广告页面（ActivityAds)，页面右上角广告倒计时（5秒），用户也可以点击倒计时跳过广告。

16. 在ActivityMain界面向左划动，就进入ActivityStudent界面，增加一个学生，增加成功后仍然返回ActivityMain界面。

17. 在ActivityStudent界面向右划动，就进入ActivityMain界面（即使当前在ActivityStudent界面只输入了部分信息，没有点击确定按钮）。

18. 增加了BottomNavigationView，将原来的Activity改为Fragment。

19. 增加了记住密码和自动登录。

20. 增加了网络api的调用（查询手机归属地）

21. 数据库upgrade

## 还存在的问题

1. ~~MainActivity中刷新(获得全部数据)和查询（获得部分数据）的两个操纵数据库的方法只能写在MainActivity下，当我把他们改到DatabaseHelper类中，它们可以获取数据，但不能获取传递过去的list的值。~~

   > 使用**adapter.notifyDataSetChanged()**时，必须保证传进**Adapter**的数据List是**同一个List**而不能是其他对象，否则**无法更新listview**。
   > 即，你可以调用List的add()，remove(),clear()，addAll()等方法，这种情况下，List指向的始终是你**最开始new出来的ArrayList**，**然后调用adapter.notifyDataSetChanged()**方法，可以**更新ListView**；
   > 但是如果你**重新new了一个ArrayList**（*重新申请了堆内存*），那么这时候，List就**指向了另外一个ArrayLIst**，这时调用adapter.notifyDataSetChanged()方法，就**无法刷新listview**了。

2. ~~登录界面很呆~~，以后把用户信息也保存到数据库，并且可以记住登录。

   > 3.25 登录界面增加了记住密码和自动登录。

3. 随机生成的头像无法保存到数据库中；头像背景色根据id变化更好。

4. 类和方法的结构非常混乱，需要简单重构。

5. 在应用了activity+viewpager+fragment后，出现了以下问题：

   1. ~~StudentFragment进行更新之后，能够成功设置"专业"，但是不刷新在界面中；~~

      > 原来因为给spinner设置了监听器，来控制专业的entry。于是在initUpdateValue方法之后，在监听器的改变之下，spinnerSpeciality又被重新设置了。给设置监听器的方法加入了一个TAG参数，用来判断，使UPDATE情况下不执行。

   2. ~~"自动登录"后的AdActivity总是出现线程问题；~~

      > AdActivity中handler = new Handler()改为CountDown()方法之前

   3. 有时选中"update"跳转到StudentFragment不能成功传递数据。

      > 去掉重写onHiddenChanged()方法中的student=null，偶发性降低了，但还是有时会出现。

其他：

1. 一些函数的注释和说明尚未写出；
2. 一些资源文件和函数并没什么用。