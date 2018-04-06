# XRecyclerView
![Demo](https://github.com/mzj21/xrecyclerview/blob/master/screenshots/1.gif?raw=true)![Demo](https://github.com/mzj21/xrecyclerview/blob/master/screenshots/2.gif?raw=true)
![Demo](https://github.com/mzj21/xrecyclerview/blob/master/screenshots/3.png?raw=true)![Demo](https://github.com/mzj21/xrecyclerview/blob/master/screenshots/4.png?raw=true)

### 简介
自定义RecyclerView，支持添加多个head，支持加载更多，支持自定义分割线，支持自定义加载更多各种状态的文字、颜色、字体大小。如需添加刷新请使用XRecyclerViewRefresh，参照xrecyclerviewsample。
完美支持GridLayoutManager的分割线
### 目前
1. 支持添加多个head
2. 支持加载更多
3. 支持自定义分割线
4. 支持自定义属性

### 特别注意
如需添加刷新请使用XRecyclerViewRefresh，参照xrecyclerviewsample。

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
	    compile 'com.github.mzj21:XRecyclerView:1.4.6'
}
```

### 下载simpleApk
[地址](https://github.com/mzj21/xrecyclerview/blob/master/xrecyclerviewsample.apk?raw=true)

### 例子
```
<com.xing.xrecyclerview.XRecyclerView
    android:id="@+id/xRecyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```
添加头部
```
xRecyclerView.addHeaderView(view);
```
添加加载更多监听
```
xRecyclerView.setLoadMoreListener(listener); 
```
添加分割线，同时支持LinearLayoutManager，GridLayoutManager，支持头部，可自定义颜色
```
xRecyclerView.addItemDecoration(new XItemDecoration(xRecyclerView.getHeadViewSize(), spacing, getResources().getColor(R.color.colorAccent)));
```

### xml 属性
- xrv_footview_textsize: 				底部加载更多的字体大小，默认14sp
- xrv_footview_textcolor: 				底部加载更多的颜色，默认cccccc
- xrv_footview_loading: 				底部加载更多加载时的文字，默认'加载中&#8230;'
- xrv_footview_loaderror: 				底部加载更多错误时的文字，默认'请点击重新加载'
- xrv_footview_loadfinish:  			底部加载更多加载完成时的文字，默认'没有更多了'

### 感谢
* [http://blog.csdn.net/lmj623565791/article/details/45059587](http://blog.csdn.net/lmj623565791/article/details/45059587)
	参考GridLayoutManager分割线
* [https://github.com/hongyangAndroid/baseAdapter](https://github.com/hongyangAndroid/baseAdapter)
	参照添加多个头部写法
    再次感谢`鸿洋_`大神
