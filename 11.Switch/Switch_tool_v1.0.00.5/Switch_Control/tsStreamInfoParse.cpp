#include "tsStreamInfoParse.h"
#include <stdio.h>
#include <sys/types.h>
#include <stdlib.h>




void Adjust_TS_packet_header(TS_packet_Header* pHeader,unsigned char *buffer)
{
	if(pHeader ==NULL)
		return ;
	pHeader->transport_error_indicator = buffer[1] >> 7;
	pHeader->payload_unit_start_indicator = buffer[1]>>6 &0x01;
	pHeader->transport_prority = buffer[1] >> 5 & 0x01;
	pHeader->PID  = (buffer[1] &0x1F)<<8 | buffer[2];
	pHeader->transport_scrambling_control = buffer[3]>>6;
	pHeader->adaption_field_control = buffer[3] >> 4 & 0x03;
	pHeader->continuity_counter = buffer[3] & 0x03;

}

bool Adjust_PES_Pakcet(unsigned char *p,int iseekLen)
{
	unsigned char *pEsData;
	if(p[0]==0x00&&p[1]==0x00&&p[2]==0x01)
	{
		if(p[3]==0xE0)
		{
			//video
			
			
		}
		else if(p[3]==0xC0)
		{
			//audio
			int i = 1;
			return false;
		}
		else
		{
			return false;
		}

		//pes len video
		int ilen = p[4]<<8 | p[5];
		//printf("-----------pes len =%d\n",ilen);
		int iptsflag = p[7]>>6;
		m_bHasDTS = false;
		m_bHasPTS = false;
		if(iptsflag == 3)
		{
			//both pts dts;
			m_bHasDTS = true;
			m_bHasPTS = true;
		}
		else if(iptsflag == 2)
		{
			m_bHasPTS = true;
		}
		else
		{
			// 01 forbidden 00 both no
		}
		if(m_bHasPTS)
		{
			m_llPts = Parse_PTS(p+9);
			m_iFramTotal++;
			fprintf(m_Mediafp,"PCR=%d \n",m_llPCR);
			fprintf(m_Mediafp,"PTS=%d \n",m_llPts);
			fprintf(m_Mediafp,"PTS-PCR=%d \n",m_llPts-m_llPCR);
		//	if(p[3]==0xE0)
			{
				double fps = 1000/((m_llPts - m_llLastPts)/90);
				if(m_iFrameRate != fps && m_llLastPts != 0 && fps <= 30 && fps >=15)
				{
					fprintf(m_Mediafp,"rate=%3.1f \n",fps);
					m_iFrameRate = fps;
				}
				m_llLastPts = m_llPts;
			}

			//fflush(m_Mediafp);
		}
		if(m_bHasDTS)
		{
			m_llDts = Parse_PTS(p+9+5);
			fprintf(m_Mediafp,"DTS=%d \n",m_llDts);
			fprintf(m_Mediafp,"DTS-PCR=%d \n",m_llDts-m_llPCR);
			
		}
		fflush(m_Mediafp);
		int iPesHeadLen=p[8] + 9;
		//printf("--------------pesheadlen=%d \n",iPesHeadLen);
		pEsData = p+iPesHeadLen;
		//
		//find 00000001 7 8 5
		int itempLen = TS_PACKET_SIZE - iseekLen - iPesHeadLen;

		//return GetVideoESInfo(pEsData,itempLen);
		return ParseH264ES(pEsData,itempLen);
	}

	return false;
}


static int tsGetPESLength(unsigned char* pBuff,int ilen)
{
	int iret = -1;
	int itsHead;
	int index = 0;
	unsigned char *packet;
	const uint8_t *pESH264_IDR= NULL;
	bool bFindPESLength = false;

	int iseekLen = 0; //ts head len
	int iPesHeadLen = 0; //pes head len
	int iEsSeekLen = 0;

	int pesHeadindex = 0;
	
	while(index < ilen-TS_PACKET_SIZE && !bFindPESLength)
	{
		if(pBuff[index]==0x47)
		{
			
			TS_packet_Header tmpPacketHeader;
			memset(&tmpPacketHeader,0,sizeof(tmpPacketHeader));
			Adjust_TS_packet_header(&tmpPacketHeader,packet);

			int pid = tmpPacketHeader.PID ;
			int iPoint_fielLen = tmpPacketHeader.payload_unit_start_indicator;

			if(pid == 0x1fff)
			{
				//NULL packet
				//跳过这个包
				//printf("---no palyload data \n");
				index += TS_PACKET_SIZE;
				continue;
			}
			if (tmpPacketHeader.payload_unit_start_indicator == 0x01) // 表示不 含有PSI或者PES头
			{	
					int len, cc,  afc, is_discontinuity,
					has_adaptation, has_payload;
					afc = tmpPacketHeader.adaption_field_control;
					uint8_t *p, *p_end,*pEsData;
					
					p = packet + 4;
					if (afc == 0) /* reserved value */
						return 0;
					has_adaptation = afc & 2;
					has_payload = afc & 1;

					if (has_adaptation) {
						/* skip adaptation field */
						//p += p[0] + 1;  p[0]为调整字段长度
						int iPCRflag = p[1]&0x10;

						p += p[0] + 1; // p[0]为调整字段长度
						/* if past the end of packet, ignore */
						p_end = packet + TS_PACKET_SIZE;
						if (p >= p_end)
						{
							//跳过这个包
							index += TS_PACKET_SIZE;
							continue;
						}
					}
					//查找 pes 
					iseekLen = p - packet;
					bFindPESLength = Adjust_PES_Pakcet(p,iseekLen);
					if(bFindPESLength)
						pesHeadindex = index;
			}	
			else
			{
							//printf("---no palyload data \n");
				//跳过这个包
				index += TS_PACKET_SIZE;
				continue;
			}	
				}
			
			if(!hasFindIframe)
				index += TS_PACKET_SIZE;
			
		}
	}

	return iret;
}


