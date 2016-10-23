# XRecyclerView
![Demo](https://github.com/mzj21/xrecyclerview/blob/master/screenshots/xrecyclerview.gif?raw=true)

### 简介
自定义RecyclerView，支持添加多个head，支持加载更多。具体使用请参照xrecyclerviewsample。

### 目前
1. 支持添加多个head
2. 支持加载更多
3. 支持自定义分割线

### 特别注意
下来刷新需要自己添加，如SwipeRefreshLayout，详见xrecyclerviewsample。

### 使用
Step 1. Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

Step 2. Add the dependency
```
dependencies {
	    compile 'com.github.mzj21:XRecyclerView:1.0.0'
}
```


### 例子
```
<com.xing.xrecyclerview.XRecyclerView
    android:id="@+id/xrecyclerview"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```
添加数据
```
XRecyclerView xRecyclerView = (XRecyclerView) findViewById(R.id.xRecyclerView);
xRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
View view1 = LayoutInflater.from(this).inflate(R.layout.headview1, xRecyclerView, false);
View view2 = LayoutInflater.from(this).inflate(R.layout.headview2, xRecyclerView, false);
xRecyclerView.addHeaderView(view1);  //添加头部
xRecyclerView.addHeaderView(view2); 
xRecyclerView.setLoadMoreListener(this); //添加加载更多监听
xRecyclerView.addItemDecoration(new DividerListItemDecoration(this, DividerListItemDecoration.VERTICAL_LIST, R.drawable.item_divider_v));//添加分割线
```

### 感谢
* [http://blog.csdn.net/lmj623565791/article/details/45059587](http://blog.csdn.net/lmj623565791/article/details/45059587)
	参考GridLayoutManager分割线
* [https://github.com/hongyangAndroid/baseAdapter](https://github.com/hongyangAndroid/baseAdapter)
	参照添加多个头部
    再次感谢`鸿洋_`大神
