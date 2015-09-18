#ifndef _COMMONFUN_H_
#define _COMMONFUN_H_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>


/*!
* \brief  将字符串中的一段用另外一段替换,注意strbuf必须有足够的空间来存储替换后的串,否则可能内存溢出
*
* \param
*      strbuf : 字符串(输入/输出)
*      src_str : 目标字符子串
*      desc_str     : 替换后的子串
* \return
*      替换后的字符串
*
*/
char * replace(char *strbuf, const char *src_str, const char *desc_str);

int applyforUDPPort(char *ip,int *port);


int FindDataFromString(const char* strinput,char* strData1,char* strData2);


#endif
