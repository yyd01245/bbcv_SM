package prod.nebula.service.socket.mgw;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import prod.nebula.commons.config.UcmsConfig;
import prod.nebula.commons.enums.msi.MgwEnum;
import prod.nebula.exception.msi.BbcvMsiException;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.socket.session.SocketSession;
import prod.nebula.socket.session.impl.SocketSessionTemplate;
@Service("vgwService")
public class VgwserviceImpl {
	private static final Logger logger=Logger.getLogger(VgwserviceImpl.class);
	
	private static SocketSession socketSession = new SocketSessionTemplate(UcmsConfig.vgw_tcpserver_ip, UcmsConfig.vgw_tcpserver_port, UcmsConfig.sm_service_time, MgwEnum.SUFFIX.getDesc());
	
	public String send(String reqStr) throws IOException, BbcvMsiException {
		if(null != socketSession) {
			String retStr = socketSession.sendStr(reqStr);
			if(StringUtils.isNotBlank(retStr)) {
				return retStr;
			}
			return null;
		} else {
			logger.error("[ucms-service] vgw tcp service is not available!");
			throw new BbcvMsiException(MsiErrorCodeEnum.SOCKE_NOT_READY);
		}
	}
}
