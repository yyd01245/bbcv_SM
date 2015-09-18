#!/bin/bash

if [ $# -lt 1 ]
then
	echo "ERROR:Request http url address!!!"
	exit 1
fi
	
url=$1
url_file="index.html"
plugin_file="plugin.tar.gz"


update_value=0
plugin_version="v0.0.00.0"
plugin_url=“http://127.0.0.1/plugin.tar.gz”
packet_size=0
md5sum="0"

#1.下载文件
wget $url -O $url_file >& /dev/null

if [ -f  $url_file   -a   -s  $url_file ]
then
	echo "INFO :Download $url_file success!!!"
else
	echo "ERROR:Download $url_file fail!!!"
	rm -rf $url_file
	exit 2
fi

#2.解析字段，判断是否下载插件文件
update_value=`cat $url_file|grep update_value|awk -F= '{print $2}'|tr -d '\r'|tr -d '\n'`
plugin_version=`cat $url_file|grep plugin_version|awk -F= '{print $2}'|tr -d '\r'|tr -d '\n'`
plugin_url=`cat $url_file|grep plugin_url|awk -F= '{print $2}'|tr -d '\r'|tr -d '\n'`
packet_size=`cat $url_file|grep packet_size|awk -F= '{print $2}'|tr -d '\r'|tr -d '\n'`
md5sum=`cat $url_file|grep md5sum|awk -F= '{print $2}'|tr -d '\r'|tr -d '\n'`

echo "---update_value=$update_value"
echo "---plugin_version=$plugin_version"
echo "---plugin_url=$plugin_url"
echo "---packet_size=$packet_size"
echo "---md5sum_remote=$md5sum"

rm -rf $url_file

if [ -f  $plugin_file ]
then
	md5sum_tmp=`md5sum $plugin_file|awk  '{print $1}'`
else
	md5sum_tmp="0"
fi
echo "---md5sum_local =$md5sum_tmp"

if [ $update_value -gt 0  -a  "$md5sum" != "$md5sum_tmp" ]
then
	echo "INFO :Should download plugin_file!!!"
else
	echo "INFO :Should not download plugin_file!!!"
	exit 0
fi

#3.下载插件文件
wget $plugin_url -O $plugin_file >& /dev/null

if [ -f  $plugin_file   -a   -s  $plugin_file ]
then
	echo "INFO :Download $plugin_file success!!!"
else
	echo "ERROR:Download $plugin_file fail!!!"
	rm -rf $plugin_file
	exit 3
fi

sudo tar -xzf $plugin_file -C /opt

if [ $? -ne 0 ]
then
	echo "ERROR:tar -xzf $plugin_file -C /opt fail!!!"
	rm -rf $plugin_file
	exit 4
fi

echo "INFO :Update plugin_file success!!!"

exit 0