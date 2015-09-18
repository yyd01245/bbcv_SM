package prod.nebula.vrc.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRtspM {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Logger logger = LoggerFactory.getLogger(TestRtspM.class);
		// TODO Auto-generated method stub
		String rtspAddr = "rtsp://125.210.227.48:5542/hdds_ip//icms_icds_pub05/opnewsps05/Video/"
				+ "20120920/20120503140950_yuguo_386909601_386953990.ts?Contentid=CP23010020120503043607"
				+ "&token=CC5F13BA3D3D0E6503915AF070CB549D625A69E4F1F9079DCFFB2EDCB36262AEB96C31B63AFD29"
				+ "441C10B9BA3B7E159F701A8BA5037C58E194E1FAC99C252FDF9DFB0004CB4B4B12B9008A0771F91D76796"
				+ "FE2A90B13172AC31897CCF3244CBD62C0157C7FE155783B603EC8513DE5C01A335DE872C690936080CE1C"
				+ "30AEFEAEFBF92674142B4C3E14335194F0768C315E242CE767A11F7822B93413D43364BA7DED4FB26FD7D"
				+ "AB2E9A5EFE5C79E0995CDD422E00FE64E0FED11C540F57DA29A797EF5EAA61C72CCB5D29558C7D37DE471"
				+ "F25CC1BCC0441BCFF5FC7CDF3DCB80465B8CEC87C9B76E5758519D66F6&isHD=0&isIpqam=0&jishu=0"
				+ "&assetId=64883805&assetType=36&vip=0"; //hdds_ipqam4

		int index = rtspAddr.indexOf("hdds_ip//");
		logger.info("index=" + index);
		if (index != -1) {
			if ("5542".equals(rtspAddr.substring(index - 5, index - 1))) {
				rtspAddr = rtspAddr.substring(0, index - 2) + "1"
						+ rtspAddr.substring(index - 1, rtspAddr.length());
				logger.info(rtspAddr);
			}
			index = rtspAddr.indexOf("hdds_ip");
			rtspAddr = rtspAddr.substring(0, index) + "hdds_ipqam4"
					+ rtspAddr.substring(index + 7, rtspAddr.length());
			logger.info(rtspAddr);
			index = rtspAddr.indexOf("isIpqam");
			rtspAddr = rtspAddr.substring(0, index + 8) + "1"
					+ rtspAddr.substring(index + 9, rtspAddr.length());
			logger.info(rtspAddr);
		}

	}
}
