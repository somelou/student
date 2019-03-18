# student

一个android初学者的demo app，简单学生信息管理应用

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

## 还存在的问题

1. MainActivity中刷新(获得全部数据)和查询（获得部分数据）的两个操纵数据库的方法只能写在MainActivity下，当我把他们改到DatabaseHelper类中，它们可以获取数据，但不能获取传递过去的list的值。
2. 登录界面很呆，以后把用户信息也保存到数据库，并且可以记住登录。
3. 随机生成的头像无法保存到数据库中；头像背景色根据id变化更好。
4. 类和方法的结构非常混乱，需要简单重构。