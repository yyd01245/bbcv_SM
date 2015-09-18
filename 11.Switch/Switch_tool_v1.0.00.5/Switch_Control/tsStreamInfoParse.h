#ifndef __TSSTREAMINFOPARSE_H_
#define __TSSTREAMINFOPARSE_H_


const int TS_PACKET_SIZE = 188;

typedef struct _TS_packet_Header
{
	unsigned sync_byte						:8;
	unsigned transport_error_indicator		:1;   //传输错误标志位，一般传输错误的话就不会处理这个包了
	unsigned payload_unit_start_indicator	:1;   //有效负载的开始标志，根据后面有效负载的内容不同功能也不同
	unsigned transport_prority				:1;   //传输优先级位，1表示高优先级
	unsigned PID							:13;
	unsigned transport_scrambling_control	:2;   //加密标志位,00表示未加密
	unsigned adaption_field_control			:2;   //调整字段控制,。01仅含有效负载，10仅含调整字段，11含有调整字段和有效负载。为00的话解码器不进行处理。
	unsigned continuity_counter				:4;    //一个4bit的计数器，范围0-15

}TS_packet_Header;

static int tsGetPESLength(unsigned char* pBuff,int iLen);


#endif
