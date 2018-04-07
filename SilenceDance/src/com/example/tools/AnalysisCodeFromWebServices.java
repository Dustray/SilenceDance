package com.example.tools;

public class AnalysisCodeFromWebServices {
	// �涨���з���ֵȫ��Ϊint�͵Ĵ���
	/*
	 * ����trueΪ-1111,����falseΪ-1000
	 * 
	 * ��ʼflagΪ-1010
	 * 
	 * ��ͣ״̬Ϊ-2001������״̬Ϊ-2002
	 * 
	 * ������룺 ������״̬����-3000 ��δ�ҵ�������ʼ����ʱ�䡱-3001 ��δ�ҵ��Ѳ���ʱ�䡱-3002 �����ʧ�ܡ�-3003
	 * 
	 * 
	 * �ɹ����룺 ����ͣ�ɹ���-4001 �������ɹ���-4002 ����ӳɹ���-4003
	 */
	public String analysisCode(int myCode) {
		String flag = "��Ч����";
		switch (myCode) {
		case -1111:
			flag = "true";
			break;
		case -1000:
			flag = "false";
			break;

		case -1010:
			flag = "��Ч��ʼ����";
			break;

		case -2001:
			flag = "��ǰ����״̬��ͣ";
			break;

		case -2002:
			flag = "��ǰ����״̬��ʼ";
			break;

		case -3000:
			flag = "����״̬����";
			break;

		case -3001:
			flag = "δ�ҵ�������ʼ����ʱ��";
			break;

		case -3002:
			flag = "δ�ҵ��Ѳ���ʱ��";
			break;

		case -3003:
			flag = "���ʧ��";
			break;

		case -4001:
			flag = "��ͣ�ɹ�";
			break;

		case -4002:
			flag = "�����ɹ�";
			break;
			
		case -4003:
			flag = "��ӳɹ�";
			break;

		}
		return flag;
	}
}
