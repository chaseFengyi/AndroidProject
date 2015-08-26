package com.CollegeState.Task;

import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.CollegeState.Data.AdvertiseImage;
import com.CollegeState.Util.HttpUtils;
import com.CollegeState.Util.JSONUtils;

/**
 * 获取广告信息s
 * @author zc
 *
 */
public class GetAdImagePreTask extends Thread{
	private Handler handler;
	public static int GET_AD_PRE_SUCC = 201;
	public static int GET_AD_PRE_FAILED = 202;
	public GetAdImagePreTask(Handler handler){
		this.handler = handler;
	}
	@Override
	public void run() {
		String result = HttpUtils.queryStringForPost(HttpUtils.GET_AD_IMAGE, null);
		if(result.equals(HttpUtils.NETWORK_ERROR)){
			handler.sendEmptyMessage(GET_AD_PRE_FAILED);
		}else{
			List<AdvertiseImage> ads = JSONUtils.getAdImages(result);
			Message msg = Message.obtain();
			msg.what = GET_AD_PRE_SUCC;
			msg.obj = ads;
			handler.sendMessage(msg);
		}
	}
}
