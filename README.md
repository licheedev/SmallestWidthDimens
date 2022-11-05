# SmallestWidthDimens [![](https://jitpack.io/v/licheedev/SmallestWidthDimens.svg)](https://jitpack.io/#licheedev/SmallestWidthDimens)

Android SmallestWidth屏幕适配方案

## 原理 smallestWidth

https://developer.android.com/guide/practices/screens_support.html#NewQualifiers

关键的地方就是sw后面数值的单位是`dp`

* 跟hongyang的方案不同，不会用绝对分辨率适配，而使用sw（最小宽度）限定符方案
* 只关注最小宽度，无视高度，不会受有/没有虚拟导航键影响，也可以适配18:9的全面屏
* 设备的sw不会有太多，没有合适的配置的话，会使用最接近设备最小宽度的那个较小的配置（如没有小米mix2的392的话，会用384这个配置，误差不会很大）
* 预览应该无压力，毕竟sw列表就是根据官方模拟器配置列表得来的

```java
// 获取最小宽度的代码
Configuration config=getResources().getConfiguration();
    int smallestScreenWidthDp=config.smallestScreenWidthDp;
```

## 使用

1. 在根build.gradle中添加

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. 子module添加依赖

```
dependencies {
        // 根据设计图的宽度选一个，没有就按照下面配置重新生成自己要的
        // 公司美工喜欢做iphone(750*1334)的图，就选sw_750
        implementation 'com.github.licheedev.SmallestWidthDimens:sw_750:1.0.0'
        implementation 'com.github.licheedev.SmallestWidthDimens:sw_1080:1.0.0'
}
```

3. 使用

```
// 正数dp
android:layout_width="@dimen/sw_16dp"
// 负数dp
android:layout_width="@dimen/_sw_10dp"
// sp
android:textSize="@dimen/sw_36sp"
```

没有合适的话，就按照下面配置重新生成一个module，复制到自己项目里面，然后 `compile project(':生成的模块名')`导入即可

## 生成新的尺寸配置

### 运行生成器

![generator](https://raw.githubusercontent.com/licheedev/SmallestWidthDimens/master/pics/generator.png)

### 生成器配置 generator_config.properties

最小宽度列表 [smallest_width_list.txt](https://github.com/licheedev/SmallestWidthDimens/blob/master/smallest_width_list.txt)

```properties
# 设计图参考宽度数值，单位不限，可以填多个，分别以英文逗号,隔开
design_width_list=750,1080
# 最小宽度列表路径
smallest_width_list=smallest_width_list.txt
# dp资源范围
dp_range=-360,1080
# sp资源范围
sp_range=1,100
# 资源文件名
file_name=sw_dimens.xml
# sp资源命名规则,{x}为要替换的数值,负数会在前面加上"_"
dp_res_reg=sw_{x}dp
# sp资源命名规则,{x}为要替换的数值
sp_res_reg=sw_{x}sp
# 只删除目标module
just_delete=false
# 缩进空格长度
intent_length=4
```