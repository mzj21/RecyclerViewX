# XRecyclerView
![Demo](https://github.com/mzj21/xrecyclerview/blob/master/screenshots/xrecyclerview1.gif?raw=true)
![Demo](https://github.com/mzj21/xrecyclerview/blob/master/screenshots/xrecyclerview2.gif?raw=true)

### 简介
自定义RecyclerView，支持添加多个head，支持加载更多，支持自定义分割线，支持自定义加载更多各种状态的文字、颜色、字体大小。如需添加刷新请使用XRecyclerViewRefresh，参照xrecyclerviewsample。

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
	    compile 'com.github.mzj21:XRecyclerView:1.3.0'
}
```

### 下载simpleApk
[地址](https://github.com/mzj21/xrecyclerview/blob/master/xrecyclerviewsample.apk?raw=true)

### 例子
```
<com.xing.xrecyclerview.XRecyclerView
	xmlns:app="http://schemas.android.com/apk/res-auto"
    	android:id="@+id/xrecyclerview"
    	android:layout_width="match_parent"
    	android:layout_height="wrap_content"
	app:xrv_footview_textsize="@dimen/text_size_14sp"
    	app:xrv_footview_textcolor="@color/color_cccccc"
	app:xrv_footview_loading="@string/footer_hint_loading"
    	app:xrv_footview_loaderror="@string/footer_hint_load_error"
    	app:xrv_footview_loadfinish="@string/footer_hint_load_finish"/>
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
xRecyclerView.addItemDecoration(new DividerListItemDecoration(DividerListItemDecoration.VERTICAL,
                getResources().getDimensionPixelSize(R.dimen._1dp), getResources().getColor(R.color.colorPrimary)));//添加分割线
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
