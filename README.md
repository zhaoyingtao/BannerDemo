# BannerDemo
使用RecyclerView，自定义LayoutManager实现旋转木马相册效果

根据github大佬的demo做了优化修改：https://github.com/ChenLittlePing/RecyclerCoverFlow

原github项目缺点：  
1、无限循环时，不支持smoothScrollToPosition滑动  
2、不支持类似viewpager滑动，滑动后有惯性  
3、设置Item间隔和item缩放混合在一起了  
4、不支持自动轮播效果  


再其基础上对上面四个不足都做了优化，使用还不错;  


优化后几点重要设置方法：  
```
   recyclerCoverFlow.setLoop();//设置无限循环滚动
   recyclerCoverFlow.setIntervalRatio(0.17f);//设置Item的间隔比例
   recyclerCoverFlow.setZoomRatio(1.2f);//设置Item的缩放比例
   recyclerCoverFlow.setAutoCarouselInterval(3000);//设置轮播间隔时间
   recyclerCoverFlow.setIsInertiaScroll(true);//设置是否需要惯性滑动,默认false
   
   recyclerCoverFlow.startBanner();
   recyclerCoverFlow.stopBanner();
```


简单说下使用，也就不上图了，感谢大佬的轮子，只是做了优化，感觉有用，点个星支持下呗
