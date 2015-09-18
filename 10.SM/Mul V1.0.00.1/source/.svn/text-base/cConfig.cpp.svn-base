#include <string.h>
#include <iostream>
#include <ctype.h>
#include <errno.h>
#include <string.h>
#include <stdio.h>
#include <sys/stat.h>
#include <stdlib.h>
#include <dlfcn.h>
#include <unistd.h>
#include <fstream>
#include <string>
#include <vector>
#include <map>
#include <list>
#include "cConfig.h" 

using namespace std;
char * PropConfig::trimLeft(char *psrc)
{
	char * src,*tag;
  	if(psrc==NULL) return psrc;
  	tag=src=psrc;
  while(*src==' '||*src==0x09||*src==0x0a||*src==0x0d)  src++;
  if(tag!=src)
  {
  	while(*src!=0) *tag++=*src++;
  	*tag=0;
  }	
  return psrc;
}
char * PropConfig::trimRight(char *psrc)
{
	char *pos;

  	if(psrc==NULL) return psrc;
  	pos=psrc+strlen(psrc) -1 ;
  	while(pos>=psrc)
  	{
    	if(*pos==' '||*pos==0x09||*pos==0x0a||*pos==0x0d)
    	*pos=0;
    	else
    	break;
    	pos--;
  	};
  	return psrc;
}
char * PropConfig::trim (char * src)
{
 	trimLeft(src);
  	trimRight(src);
  	return src;
}

bool  PropConfig::parse(const char * data,Property &prop)
{
	  char buf[2048],tmp_buf[1024],*pos;
	  strncpy(buf,data,sizeof buf);
	  trimLeft(buf);
	  if(buf[0]=='#' or buf[0]=='/') return false;//ÊÇ×¢ÊÍ
	  if((pos=strstr(buf,"="))==NULL) return false;//±íÊ¾Ê§°Ü
	  *pos='\0';
	  strcpy(tmp_buf,buf);
	  trim(tmp_buf);
	  prop.key=tmp_buf;
	  
	  strcpy(tmp_buf,pos+1);
	  trim(tmp_buf);
	  prop.value=tmp_buf;
	  
	  return true;
}

bool PropConfig::init(const char* pConfFile)
{
	FILE *fp;
	char buf[2048];
	Property prop;
	if((fp=fopen(pConfFile,"r"))==NULL)
		return  false; 
	
	while(!feof(fp))
	{
		if(fgets(buf,2000,fp)==NULL)  break; 
		if(parse(buf,prop)) 
		 	Props[prop.key]=prop;
	}
	fclose(fp);
	return true;
}


string PropConfig::getValue(const char * key)
{
	string s_key=key;
  return getValue(s_key);
}

string PropConfig::getValue(const string &key)
{
   map<string,Property>::iterator it=Props.find(key);
  if(it!=Props.end())
  	return it->second.value;
  else
  	return string("");
}
