# MultiType-Adapter
一款轻量级支持多数据类型的 RecyclerView 适配器; 使用简单，完全解耦;


- [总览](#总览)
- [特性](#特性)
- [基础用法](#基础用法)
    - [单数据](#单数据)
    - [多数据-多类型](#多数据-多类型)
    - [单类型-多数据](#单类型-多数据)
    - [事件](#事件)
- [高级用法](#高级用法)
    - [网格布局与线性布局混合编排](#网格布局与线性布局混合编排)
    - [瀑布流布局](#瀑布流布局)
    - [上拉加载](#上拉加载)
    - [无数据时过度界面设置](#无数据时过度界面设置)
    - [混合布局拖拽实现](#混合布局拖拽实现)
    - [吸顶效果](https://github.com/LidongWen/MultiTypeAdapter/blob/master/doc/sticky.md)
    - [设置复用数量](#设置复用数量)
- [扩展](#扩展)
- [Thrank](#Thrank)
- [一些说明](#一些说明)
# 总览
总体来讲支持以下效果

![总览](http://upload-images.jianshu.io/upload_images/1599843-94c00e9b66a4b00d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
# 特性
- 轻盈、整个类库只有9个文件
- 全面、支持 bean type 之间 一对一 和 一对多 的关系绑定
- 职责单一、只负责本分工作，专注多类型的列表视图 类型分发，不会影响 view 的内容或行为
- 内存、没有性能损失，内存友好
- 可读、代码清晰干净

# 基础用法
```groovy
// root build.gradle
repositories {
    jcenter()
    maven { url "https://www.jitpack.io" }
}
// yout project build.gradle
dependencies {
        compile 'com.github.LidongWen:MultiTypeAdapter:0.1.5'
}
```
# 单数据
```java
CommonAdapter adapter = new CommonAdapter<ItemClass>(this, ItemClass.class, R.layout.item_one) {
    @Override
    protected void convert(ViewHolder holder, final ItemClass item, int position) {
        holder.setText(R.id.tv_item01, item.name);
    }
};
....
recyclerView.setAdapter(adapter);
```
示例查看：[single example](https://github.com/LidongWen/MultiTypeAdapter/blob/master/app/src/main/java/com/wenld/app_multitypeadapter/MainActivity.java)

# 多数据-多类型
一个bean对应一种view

<img width="500" height="300" src="http://upload-images.jianshu.io/upload_images/1599843-e82c558dd9258376.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240"></img> 


创建一个或多个 `class` 继承`MultiItemView`，这边做某一种数据类型 对应的 `ItemView`的创建，与数据装配
```java
public class ItemVIew01 extends MultiItemView<Bean01> {

    @NonNull
    @Override
    public int getLayoutId() {
        return R.layout.item_one;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Bean01 item, int position) {
        holder.setText(R.id.tv_item01,item.title);
    }
}
public class Item ...
```

创建一个 适配器`MultiTypeAdapter` ，注册 `bean` 与 `MultiItemView` 将适配器设入`RecyclerView`;
```java
private MultiTypeAdapter adapter = new MultiTypeAdapter();
adapter.register(Bean01.class, new ItemVIew01());
adapter.register(Bean02.class, new ItemVIew02());
adapter.register(Bean03.class, new ItemVIew03());
//  restful 风格
//  adapter.register(Bean01.class, new ItemVIew01())
//          .register(Bean02.class, new ItemVIew02());
...
adapter.setItems(items);
recyclerView.setAdapter(adapter);
```
示例查看：[many2many Example](https://github.com/LidongWen/MultiTypeAdapter/blob/master/app/src/main/java/com/wenld/app_multitypeadapter/manyData/MultiDataActivity.java)
# 单数据-多类型
单数据-多类型
<img width="500" height="400" src="http://upload-images.jianshu.io/upload_images/1599843-e48d8a32ee92ea1f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240"></img> 

创建一个或多个 `class` 继承`MultiItemView`，其中 他们的数据类型要一致，重写 `isForViewType`方法
```java
public class ItemVIew04 extends MultiItemView<Bean04, ViewHolder> {
        ...
    @Override
    public boolean isForViewType(Bean04 item, int postion) {
        if (Bean04.TYPE_ONE.equals(item.type)) {
            return true;
        }
        return false;
    }
}

public class ItemVIew05 extends MultiItemView<Bean04, ViewHolder> {
        ...
    @Override
    public boolean isForViewType(Bean04 item, int postion) {
        if (Bean04.TYPE_TWO.equals(item.type)) {
            return true;
        }
        return false;
    }
}
```
创建一个入口  `class` 继承`MultiItemView`  , 构造函数时调用 `addChildeItemView`方法
```java
public class ItemVIew06 extends MultiItemView<Bean04, ViewHolder> {
    public ItemVIew06() {
        super();
        addChildeItemView(new ItemVIew04());
        addChildeItemView(new ItemVIew05());
        addChildeItemView(new ItemVIew07());
    }
}
```
activity
```java
       adapter = new MultiTypeAdapter();
       adapter.register(Bean04.class, new ItemVIew06());
       ...
```

示例查看：[one-many Example](https://github.com/LidongWen/MultiTypeAdapter/blob/master/app/src/main/java/com/wenld/app_multitypeadapter/one2many/One2ManyActivity.java)

# 事件
设置点击事件于长按事件
```java
       adapter.setOnItemClickListener(new OnItemClickListener<ItemClass>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, ItemClass itemClass, int position) {
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, ItemClass itemClass, int position) {
                return false;
            }
        });
```

# 高级用法
# 网格布局与线性布局混合编排
```java
        adapter.register(String.class, new ItemVIewNormal()); // 一对一
        adapter.register(Bean01.class, new ItemVIew01()); 
        adapter.register(Bean02.class, new ItemVIew02()); 
        adapter.register(Bean03.class, new ItemVIew03());  
        adapter.register(Bean04.class, new ItemVIew06());   // 一对多

```
示例查看：[mix Example](https://github.com/LidongWen/MultiTypeAdapter/blob/master/app/src/main/java/com/wenld/app_multitypeadapter/mix/MixActivity.java
)
# 瀑布流布局
示例查看：[WaterFall Example](https://github.com/LidongWen/MultiTypeAdapter/blob/master/app/src/main/java/com/wenld/app_multitypeadapter/mix/WaterFallActivity.java)
# 上拉加载
效果如下  

![loading.gif](https://github.com/LidongWen/MultiTypeAdapter/blob/master/img/loading.gif)

将我们的 初始化`LoadMoreWrapper2`,添加上拉UI
```java
    LoadMoreWrapper2 loadMoreWrapper2;
    private MultiTypeAdapter adapter;
    
    loadMoreWrapper2 = new LoadMoreWrapper2(adapter);
    loadMoreWrapper2.setLoadMoreView(LayoutInflater.from(this).inflate(R.layout.default_loading, recyclerView, false));
    recyclerView.setAdapter(loadMoreWrapper2);
    
    loadMoreWrapper2.setOnLoadMoreListener(new LoadMoreWrapper2.OnLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            loadMoreWrapper2.loadingComplete();//加载完毕
        }
    });
    
    // loadMoreWrapper2.setLoadMore(false); 开启或关闭加载功能
```

示例查看：[pull-load Example](https://github.com/LidongWen/MultiTypeAdapter/blob/master/app/src/main/java/com/wenld/app_multitypeadapter/pull_load/PullLoadActivity.java)
# 无数据时过度界面设置
```java
    EmptyWrapper emptyWrapper;
    private MultiTypeAdapter adapter;
    
    emptyWrapper = new EmptyWrapper(adapter);
    emptyWrapper.setEmptyView(R.layout.layout_empty);
    recyclerView.setAdapter(emptyWrapper);
```
示例查看：[Empty Example](https://github.com/LidongWen/MultiTypeAdapter/blob/master/app/src/main/java/com/wenld/app_multitypeadapter/empty/EmptyActivity.java)
# 混合布局拖拽实现
[混合布局拖拽实现](https://github.com/LidongWen/MultiTypeAdapter/tree/master/app/src/main/java/com/wenld/app_multitypeadapter/itemTouch)
# 吸顶效果
[悬浮吸顶](http://www.jianshu.com/p/bb4c8c16d894)
<img width="300" height="500" src="https://github.com/LidongWen/MultiTypeAdapter/blob/master/img/sticky_all.gif"></img>

# 设置复用数量
// 2.0版本

# 扩展
 [SasukeRecyclerView](https://github.com/LidongWen/SasukeRecyclerView):基于[MultiType-Adapter](https://github.com/LidongWen/MultiTypeAdapter)开发的一框下拉刷新上拉加载的库

# Thrank
- [鸿洋](https://github.com/hongyangAndroid/baseAdapter): 空白页功能与上拉加载功能 我拿过来稍微做了修改
- [Glide](https://github.com/bumptech/glide): 图片加载
- [drakeet](https://github.com/drakeet)学习其中思想

# 一些说明
大家可能咋一看，会认为我抄袭  [drakeet](https://github.com/drakeet)的代码，我虽然有学习过他的代码，其中也让我受益良多，不论是技术点还是架构松耦合方面的知识，但是，这份开源库虽然使用上与之相似，但完全手写，不存在抄袭 [drakeet](https://github.com/drakeet) , 而且 是否抄袭 请大家阅读完源码之后再做评论，谢谢。

> **V 0.1.5**
> - 悬浮吸顶滑动速度优化
>
> **V 0.1.4**
> - 悬浮吸顶效果增强：可随意搭配itemVIew为悬浮布局
> - 事件处理优化
>
> **V 0.1.3**
> - 检测并修复切换悬浮头部状态时发生内存泄漏；
>
![检测内存泄漏-内存占用图](http://upload-images.jianshu.io/upload_images/1599843-000f0148c0237c90.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
>
>**V 0.1.2**
> - 支持悬浮头部触摸事件，点击事件，长按事件等
> - multiTypeAdapter支持restful风格调用
>
> **V 0.1.1**
>  - MultiItemView 改变，更加简洁、直观
>  - 新增 悬浮吸顶头部功能 [快速打造Recyclerview悬浮吸顶头部](https://github.com/LidongWen/MultiTypeAdapter/blob/master/doc/sticky.md)
>
>![](http://upload-images.jianshu.io/upload_images/1599843-b2506d6cec4d9f8b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
>
>**V 0.0.1**
> - 实现一对一关系功能
> - 实现一对多关系功能
> - 事件监听
> - 支持网格、瀑布流、线性布局
> - 上拉加载功能
> - 数据为空时过渡界面展示功能

代码传送门：[戳我!!!](https://github.com/LidongWen/MultiTypeAdapter)

-----

希望我的文章不会误导在观看的你，如果有异议的地方欢迎讨论和指正。
如果能给观看的你带来收获，那就是最好不过了。

##### 人生得意须尽欢, 桃花坞里桃花庵
##### 点个关注呗，[对，不信你点试试？](http://www.jianshu.com/users/99f514ea81b3/timeline)