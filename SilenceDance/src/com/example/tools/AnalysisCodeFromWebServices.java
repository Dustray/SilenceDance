package com.example.tools;

public class AnalysisCodeFromWebServices {
	// 规定所有返回值全部为int型的代码
	/*
	 * 返回true为-1111,返回false为-1000
	 * 
	 * 初始flag为-1010
	 * 
	 * 暂停状态为-2001，播放状态为-2002
	 * 
	 * 错误代码： “播放状态错误”-3000 “未找到歌曲开始播放时间”-3001 “未找到已播放时间”-3002 “添加失败”-3003
	 * 
	 * 
	 * 成功代码： “暂停成功”-4001 “继续成功”-4002 “添加成功”-4003
	 */
	public String analysisCode(int myCode) {
		String flag = "无效代码";
		switch (myCode) {
		case -1111:
			flag = "true";
			break;
		case -1000:
			flag = "false";
			break;

		case -1010:
			flag = "无效初始代码";
			break;

		case -2001:
			flag = "当前播放状态暂停";
			break;

		case -2002:
			flag = "当前播放状态开始";
			break;

		case -3000:
			flag = "播放状态错误";
			break;

		case -3001:
			flag = "未找到歌曲开始播放时间";
			break;

		case -3002:
			flag = "未找到已播放时间";
			break;

		case -3003:
			flag = "添加失败";
			break;

		case -4001:
			flag = "暂停成功";
			break;

		case -4002:
			flag = "继续成功";
			break;
			
		case -4003:
			flag = "添加成功";
			break;

		}
		return flag;
	}
}
