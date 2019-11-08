package com.fr.design.extra;

/**
 * Created by richie on 16/3/18.
 * 应用商店构建器,需要负责去检查是否有最新的版本的应用商店,如果有且适合当前设计器版本,则弹窗提示用户是否升级到最新的应用商店.
 * 用户点击更新后,构建器把最新版本的JavaScript以及HTML代码下载到安装目录下的scripts/store目录下
 * 初步想法,设计器中所有的URL请求,都从http(s)://support.finereport.com/config获取,然后存入设计器中,防止修改了某个url导致一系列无法兼容问题
 * 而我们需要做的仅仅是保持兼容support这个服务器的持续维护和兼容,相对来说就简单多了
 */
public class AppStoreBuilder {

    public void checkStoreJavaSciptVersion() {
        //do nothing
    }

    public void updateStoreJavaScript() {
        //do nothing
    }
}
