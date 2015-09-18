#ifndef _CONFIG_H
#define _CONFIG_H

#include <fstream>
#include <string>
#include <vector>
#include <map>
#include <list>

using namespace std;

class Property
{
public:
	 string key;
	 string value;
	 Property& operator=(const Property& p)
		{
			if( this != &p )
			{
				key = p.key;
				value = p.value;
			}
		return *this;
	  }
};


class PropConfig
{
    public:
        PropConfig(){};
        bool    init(const char* pConfFile);  
        //按key获取Value，例如log4j.appender.C.File
				string  getValue(const char * key);
				string  getValue(const string &key);
				char * trimLeft(char *pStr);   
				char * trimRight(char *pStr);    
				char * trim(char * src);
	private:
        map<string,Property>  Props;
        bool     parse(const char * buf,Property &prop);
};


#endif
