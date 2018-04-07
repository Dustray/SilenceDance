package com.example.tools;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class FoundWebServices {

	private String result;

	public String getRemoteInfo(String teamName, String musicName, String methodName)
			throws Exception {
		String WSDL_URI = "http://192.168.137.2:8080/DanceMusicWebServices/MusicSynPort?wsdl";// wsdl的uri
		String namespace = "http://services.dustray.cn/";// namespace
		//methodName = "getSynTime";// 要调用的方法名称

		SoapObject request = new SoapObject(namespace, methodName);
		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
		request.addProperty("arg0", teamName);
		request.addProperty("arg1", musicName);
		// 创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapSerializationEnvelope.VER10);
		envelope.bodyOut = request;// 由于是发送请求，所以是设置bodyOut
		envelope.dotNet = false;// 是否是.net开发的webservice
		HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
		httpTransportSE.call(namespace + methodName, envelope);// 调用
		// 获取返回的数据
		SoapObject object = (SoapObject) envelope.bodyIn;
		// 获取返回的结果
		try {
			result = object.getProperty(0).toString();

		} catch (Exception e) {
			result = "失败";

		}

		return result;

	}
}
