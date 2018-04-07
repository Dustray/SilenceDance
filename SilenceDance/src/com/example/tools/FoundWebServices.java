package com.example.tools;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class FoundWebServices {

	private String result;

	public String getRemoteInfo(String teamName, String musicName, String methodName)
			throws Exception {
		String WSDL_URI = "http://192.168.137.2:8080/DanceMusicWebServices/MusicSynPort?wsdl";// wsdl��uri
		String namespace = "http://services.dustray.cn/";// namespace
		//methodName = "getSynTime";// Ҫ���õķ�������

		SoapObject request = new SoapObject(namespace, methodName);
		// ���������WebService�ӿ���Ҫ�������������mobileCode��userId
		request.addProperty("arg0", teamName);
		request.addProperty("arg1", musicName);
		// ����SoapSerializationEnvelope ����ͬʱָ��soap�汾��(֮ǰ��wsdl�п�����)
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapSerializationEnvelope.VER10);
		envelope.bodyOut = request;// �����Ƿ�����������������bodyOut
		envelope.dotNet = false;// �Ƿ���.net������webservice
		HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
		httpTransportSE.call(namespace + methodName, envelope);// ����
		// ��ȡ���ص�����
		SoapObject object = (SoapObject) envelope.bodyIn;
		// ��ȡ���صĽ��
		try {
			result = object.getProperty(0).toString();

		} catch (Exception e) {
			result = "ʧ��";

		}

		return result;

	}
}
