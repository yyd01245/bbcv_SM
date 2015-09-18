#!/bin/bash

SOURCE_DIR=./
COMPILW_PATH=`pwd`

####### Output directory
OBJECTS_DIR_AUDIOENCODER=audioencoder
OBJECTS_DIR_AUDIOSOURCE=audiosource
OBJECTS_DIR_AVMUXER=avmuxer
OBJECTS_DIR_TSSMOOTH=tssmooth
OBJECTS_DIR_VIDEOSOURCE=videosource
OBJECTS_DIR_VIDEOENCODER=videoencoder_mpeg2
OBJECTS_DIR_AVENCODER=avencoder

TARGET_DIR=RELEASE

echo "uninstall  "

OBJECT_CLEAN_LIB_DIR=/usr/local/lib
OBJECT_CLEAN_EXE_DIR=/usr/local/bin/

sudo rm -f -r $OBJECT_CLEAN_LIB_DIR/libaudioencoder.so
sudo rm -f -r $OBJECT_CLEAN_LIB_DIR/libaudiosource.so
sudo rm -f -r $OBJECT_CLEAN_LIB_DIR/libavmuxer.so
sudo rm -f -r $OBJECT_CLEAN_LIB_DIR/libtssmooth.so
sudo rm -f -r $OBJECT_CLEAN_LIB_DIR/libvideosource.so
sudo rm -f -r $OBJECT_CLEAN_LIB_DIR/libvideoencoder.so

sudo rm -f -r $OBJECT_CLEAN_EXE_DIR/avencoder

echo "uninstall success"


echo "make "$OBJECTS_DIR_AUDIOENCODER 
cd ${COMPILW_PATH}/${OBJECTS_DIR_AUDIOENCODER} 
make clean;make;make install;
cp *.so ${COMPILW_PATH}/release/Avencoder/;
make clean
echo "success "$OBJECTS_DIR_AUDIOENCODER

echo "make "$OBJECTS_DIR_AUDIOSOURCE
cd ${COMPILW_PATH}/${OBJECTS_DIR_AUDIOSOURCE}
make clean;make;make install;
cp *.so ${COMPILW_PATH}/release/Avencoder/;
make clean
echo "success "$OBJECTS_DIR_AUDIOSOURCE

echo "make "$OBJECTS_DIR_AVMUXER
cd ${COMPILW_PATH}/${OBJECTS_DIR_AVMUXER}
make clean;make;make install;
cp *.so ${COMPILW_PATH}/release/Avencoder/;
make clean
echo "success "$OBJECTS_DIR_AVMUXER

echo "make "$OBJECTS_DIR_TSSMOOTH
cd ${COMPILW_PATH}/${OBJECTS_DIR_TSSMOOTH}
make clean;make;make install;
cp *.so ${COMPILW_PATH}/release/Avencoder/;
make clean
echo "success "$OBJECTS_DIR_TSSMOOTH

echo "make "$OBJECTS_DIR_VIDEOSOURCE
cd ${COMPILW_PATH}/${OBJECTS_DIR_VIDEOSOURCE}
make clean;make;make install;
cp *.so ${COMPILW_PATH}/release/Avencoder/;
make clean
echo "success "$OBJECTS_DIR_VIDEOSOURCE

echo "make "$OBJECTS_DIR_VIDEOENCODER
cd ${COMPILW_PATH}/${OBJECTS_DIR_VIDEOENCODER}
make clean;make;make install;
cp *.so ${COMPILW_PATH}/release/Avencoder/;
make clean
echo "success "$OBJECTS_DIR_VIDEOENCODER

echo "make "$OBJECTS_DIR_AVENCODER
cd ${COMPILW_PATH}/${OBJECTS_DIR_AVENCODER}
make clean;make;make install;
cp avencoder ${COMPILW_PATH}/release/Avencoder/;
make clean
echo "success "$OBJECTS_DIR_AVENCODER

tar -czf ${COMPILW_PATH}/release/Avencoder.tar.gz ${COMPILW_PATH}/release/Avencoder

echo "success ALL "

	
