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
    - [吸顶效果](#吸顶效果)
    - [设置复用数量](#设置复用数量)
- [Thrank](#Thrank)

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
        compile 'com.github.LidongWen:MultiTypeAdapter:0.0.2'
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
public class ItemVIew01 extends MultiItemView<Bean01,ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_one, parent, false);
        return new ViewHolder(inflater.getContext(),view);
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

![loading.gif]("https://github.com/LidongWen/MultiTypeAdapter/blob/master/doc/loading.gif") 

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
// todo  待做
# 吸顶效果
// todo  待做
# 设置复用数量
// todo  待做
# Thrank
- [鸿洋](https://github.com/hongyangAndroid/baseAdapter): 空白页功能与上拉加载功能 我拿过来稍微做了修改
- [Glide](https://github.com/bumptech/glide): 图片加载


> ##  V 0.0.1
>  - 实现一对一关系功能
>  - 实现一对多关系功能
>  - 事件监听
>  - 支持网格、瀑布流、线性布局
>  - 上拉加载功能
>  - 数据为空时过渡界面展示功能

