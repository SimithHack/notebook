# 天气预报接口

## 免费天气接口

> 不是特别稳定，参数是传递城市编号

* 获取实时天气预报

  ```bash
  curl http://www.weather.com.cn/data/sk/101190408.html
  {"weatherinfo":{"city":"太仓","cityid":"101190408","temp":"22.8","WD":"东风","WS":"小于3级","SD":"81%","AP":"1005.5hPa","njd":"暂无实况","WSE":"<3","time":"17:55","sm":"3.2","isRadar":"0","Radar":""}} 
  ```

* 获取城市编号

  > 一级级往下获取

  ```bash
  http://flash.weather.com.cn/wmaps/xml/china.xml
  ```

## 付费天气API接口

> API 文档 https://www.tianqiapi.com/index/doc?version=v61 包含基本天气信息、气象预警、湿度、能见度、气压、日出日落、6大生活指数、pm2.5、pm10、o3、no2、so2、是否需要带口罩、外出适宜、开窗适宜、是否需要打开净化器等，可按地名、城市编号、IP查询。

* 企业收费 365一年
* 个人免费
* 参数传递城市编号，可以通过API去查询

## 爬虫

> 稳定，可能会涉及到版权问题

参考网站：

[中国天气](http://www.weather.com.cn/forecast/) 

* 参数
  * 查询城市编号 http://toy1.weather.com.cn/search?cityname=shuangliu&callback=success_jsonpCallback&_=1589013202539
  * 查询天气 http://www.weather.com.cn/weather1d/101270106.shtml#input

* 特点

  稳定，免费

* 缺点

  需要连外网，可能有版权问题