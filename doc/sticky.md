## [MultiType-Adapter](https://github.com/LidongWen/MultiTypeAdapter)打造悬浮吸顶效果

配合RecyclerView快速打造一款 展示UI 悬浮吸顶效果，如 通讯录效果，时光轴效果等等，且支持触摸事件。

 **文章地址：[戳我!](http://www.jianshu.com/p/032a6773620b)**

[MultiType-Adapter](https://github.com/LidongWen/MultiTypeAdapter)
是一款轻量级支持多数据类型的 RecyclerView 适配器; 使用简单，完全解耦;

-----

悬浮吸顶功能分为两种模式，一种是自己添加布局作为头部，另一种则是从原有itemView中选取任意itemVIew作为悬浮头部布局；

- [总览](https://github.com/LidongWen/MultiTypeAdapter)
- [特性](https://github.com/LidongWen/MultiTypeAdapter)
- [基础用法](https://github.com/LidongWen/MultiTypeAdapter)
    - [单数据](https://github.com/LidongWen/MultiTypeAdapter)
    - [多数据-多类型](https://github.com/LidongWen/MultiTypeAdapter)
    - [单类型-多数据](https://github.com/LidongWen/MultiTypeAdapter)
    - [事件](https://github.com/LidongWen/MultiTypeAdapter)
- [高级用法](https://github.com/LidongWen/MultiTypeAdapter)
    - [网格布局与线性布局混合编排](https://github.com/LidongWen/MultiTypeAdapter)
    - [瀑布流布局](https://github.com/LidongWen/MultiTypeAdapter)
    - [上拉加载](https://github.com/LidongWen/MultiTypeAdapter)
    - [无数据时过度界面设置](https://github.com/LidongWen/MultiTypeAdapter)
    - [混合布局拖拽实现](https://github.com/LidongWen/MultiTypeAdapter)
    - 吸顶效果
    - [设置复用数量](https://github.com/LidongWen/MultiTypeAdapter)
- [扩展](https://github.com/LidongWen/MultiTypeAdapter)
- [Thrank](https://github.com/LidongWen/MultiTypeAdapter)
- [一些说明](https://github.com/LidongWen/MultiTypeAdapter)

# 悬浮吸顶效果
<img width="300" height="500" src="https://github.com/LidongWen/MultiTypeAdapter/blob/master/img/one.gif"></img>
<img width="300" height="500" src="https://github.com/LidongWen/MultiTypeAdapter/blob/master/img/two.gif"></img>


```
```groovy
// root build.gradle
repositories {
    jcenter()
    maven { url "https://www.jitpack.io" }
}
// yout project build.gradle
dependencies {
        compile 'com.github.LidongWen:MultiTypeAdapter:0.1.1'
}
```
# 使用 类型1
定义一个Adapter
```
     class StickySigleTwoAdapter extends MultiTypeAdapter implements StickyHeaderAdapter {

        @Override
        public boolean isHeader(int position) {

            if (position  == 0 || position == 8 || position == 15|| position == 21|| position == 28) {
                return true;
            } else
                return false;
        }
    }
```
使用
```
        adapter = new StickySigleTwoAdapter();
          RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rlv_multidata);
          recyclerView.setNestedScrollingEnabled(false);
          adapter.register(String.class, new ItemVIewNormal());
          adapter.register(Bean01.class, new ItemVIew01());
          adapter.register(Bean02.class, new ItemVIew02());
          adapter.register(Bean03.class, new ItemVIew03());
        recyclerView.setAdapter(adapter);
        StickyControl.anyHeader2()
                .adapter(adapter)
                .setRecyclerView(recyclerView)
//                .immersion()
                .togo();
```
# 使用 模式2
在内容itemView中选取悬浮 头部；下面这种效果
<img width="300" height="500" src="https://github.com/LidongWen/MultiTypeAdapter/blob/master/img/sticky_all.gif"></img>

```
public class StickySigleTwoAdapter extends StickyAnyAdapter {
    public StickySigleTwoAdapter(Context context, RecyclerView.Adapter mAdapter) {
        super(context, mAdapter);
    }
    @Override
    public boolean isHeader(int position) {  //选取悬浮item
        if (position  == 0 || position == 8 || position == 14|| position == 20 || position == 28|| position == 32) {
            return true;
        } else
            return false;
    }
}
```
activity
```
stickyTestAdapter = new StickySigleTwoAdapter(this, adapter);
recyclerView.setAdapter(stickyTestAdapter);
StickyControl.any()
        .adapter(stickyTestAdapter)
        .setRecyclerView(recyclerView)
//                .immersion()
        .togo();
```

# 使用模式3
将布局放入 StickyNestedScrollView 中   ，并将 需要黏贴的头部的view  设置 tag属性为  "sticky" "-nonconstant" "-hastransparency"中的任意属性
```
   <com.wenld.multitypeadapter.sticky.StickyNestedScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    <VewGroup>
        <view   sticky:tag="sticky"
        ...
        </view>
        <view   sticky:tag="-nonconstant"
            ...
        </view>
    </VewGroup>
    </com.wenld.multitypeadapter.sticky.StickyNestedScrollView>
```

# 使用 类型4
添加自顶布局作为头部，
添加分组
**1、 自定义一个 StickyAdapter**
 在这里你可以设置 header 布局，header的位置
```
public class StickySigleTwoAdapter extends StickyAdapter {
    public StickySigleTwoAdapter(Context context, RecyclerView.Adapter mAdapter) {
        super(context, mAdapter);
    }
    @Override
    public boolean isHeader(int position) {
         if (position  == 0 || position == 8 || position == 14|| position == 20 || position == 28|| position == 32) {
            return true;
        } else
            return false;
    }
    @Override
    public void onBindHeaderViewHolder(final ViewHolder viewholder, final int position) {
    }
    @Override
    protected int getLayoutId() {
        return R.layout.header_two;
    }
}
```
**2、在activity中设置**
```
//将 adapter 包裹进  StickyAdapter
// setAdapter
//配置生效
stickyTestAdapter = new StickySigleTwoAdapter(this, adapter);
recyclerView.setAdapter(stickyTestAdapter);
StickyControl.single()          // 设置单个
        .adapter(stickyTestAdapter)         //
        .setRecyclerView(recyclerView)
        .immersion()                    // 是否嵌入
        .togo();
```


三种方式优缺点：

 模式 | recyclerView | anyView |  优点 | 不足  | 使用场景
  --- | --- | --- | --- | ---  | ---
  mode 1 | ✔ | ✘ | 准确黏贴头部,支持glide等图片加载框架 | 开启复用 位置会变化 | 大数据量时，使用 LinearLayoutManager时 。比如说通讯录头部
  mode 2 | ✔ | ✘ | 准确黏贴头部 | 不支持glide等图片加载框架  | ，头部图片是取本地（非gilde等图片加载）
  mode 3 | ✔ | ✔ | 支持任意view，准确黏贴头部 | 不支持recyclerView复用，不支持黏贴布局根布局是ImageView时使用glide等加载框架 | 小数据量，平常布局
  mode 2 | ✔ | ✘ | 准确黏贴头部，准确黏贴头部 | 不支持recyclerView复用，否则失效 | 这个厉害了，布局头部大小不做限定的时候

**第一种复用方面有致命的问题。recyclerview中推荐使用第二种和四种方式。  scrollerview当中推荐使用第三种   ，鉴于黏贴头部这块事件比较多样性，后面我会将黏贴部分抽出来 不再放入包中。**

 **文章地址：[戳我!](http://www.jianshu.com/p/032a6773620b)**
 **代码传送门：[戳我!!!](https://github.com/LidongWen/MultiTypeAdapter)**

-----

希望我的文章不会误导在观看的你，如果有异议的地方欢迎讨论和指正。
如果能给观看的你带来收获，那就是最好不过了。

##### 人生得意须尽欢, 桃花坞里桃花庵
