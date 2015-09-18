#!/bin/bash

#--------------------- kernel optimize --------------------#
#bakup
cp  /etc/sysctl.conf  /etc/sysctl.conf_bak
#cover
cat ./switch_sysctl.conf >  /etc/sysctl.conf
#take effect
/sbin/sysctl -p

#-------------------- system connect numbers(centos\ubuntu 12.04)--------------#
echo "
* soft nofile 600000
* hard nofile 600000
* soft nproc 600000
* hard nproc 600000
"  >>  /etc/security/limits.conf 

#if ubuntu(before ubuntu 12.04)
#echo "ulimit -SHn 600000" >> /etc/profile
#source /etc/profile

service ipta
