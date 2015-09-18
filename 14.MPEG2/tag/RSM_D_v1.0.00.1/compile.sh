#!/bin/bash

COMPILW_PATH=`pwd`

cd  ${COMPILW_PATH}/nebula

make clean;make;make install;make clean

cd ${COMPILW_PATH}/src

make clean;make;make install;make clean
#g++ env_check.cpp kyKEY.cpp -o kyKEY-creator
#g++ env_check.cpp security.cpp -o kyKEY-checker

#mv kyKEY-creator kyKEY-checker ${COMPILW_PATH}/release/

cd  ${COMPILW_PATH}
tar xzvf LibVNCServer-0.9.9.tar.gz
cd ${COMPILW_PATH}/LibVNCServer-0.9.9/
chmod +x configure
./configure
make clean;make -j 30
cp ./libvncclient/.libs/libvncclient.a ../lib
make clean

cd  ${COMPILW_PATH}/vncms

make clean;make;make install;make clean

tar -czf ../release/rsm.tar.gz ../rsm
cd  ${COMPILW_PATH}/
