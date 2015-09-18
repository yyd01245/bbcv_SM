# Because all these files are sensitive to directory structure, It needs
# a Makefile to install all files to the working directory.

#---------------------------------------------------------------------
#switch,allow add by you self,null is false
#---------------------------------------------------------------------
export PROJECT_HOME=../

DEBUG=
NOTHREAD=
UNIX_SYS =
ORA=

#---------------------------------------------------------------------
#include make common options 
#---------------------------------------------------------------------
include $(PROJECT_HOME)/make/Cmake.pure

#---------------------------------------------------------------------
#product globle var 
#---------------------------------------------------------------------
COMMON_INC = -I${PROJECT_HOME}/include
COMMON_LIB = ${PROJECT_HOME}/lib
PRODINCL   = 
PRODLIBS   = -L$(PROJECT_HOME)/lib -lnebulautil lvncclient

#---------------------------------------------------------------------
#product local information, use to make name
#---------------------------------------------------------------------
MODULE_NAME=
BUILDTAIL=
TARGETS=bvbcm PluKill

#---------------------------------------------------------------------
# build rules
#---------------------------------------------------------------------
all: ${TARGETS}
install: all install_header install_lib install_bin
tar:


#---------------------------------------------------------------------
# product targets build rules
#---------------------------------------------------------------------
bvbcm:vnc_config.o vnc_worker_Xvnc.o vnc_worker_rsm.o vnc_main.o 
	$(CC) $^ $(COMMON_LIB)/libnebulautil.a $(COMMON_LIB)/libvncclient.a -ljpeg -lz  $(SYSLIB) -o $@
PluKill:vlc_plu_fork.o
	$(CC) $^ $(COMMON_LIB)/libnebulautil.a $(SYSLIB) -o $@

#----------------------------------------------------------------------
# keeping
#----------------------------------------------------------------------

clean:
	#-rm -rf  *.o *.so *.a *.log core $(TARGETS)
	-rm -rf  *.o *.so *.log core $(TARGETS)

install_lib:

install_bin:
	cp -r ${TARGETS} .chrome .Interchrome *sh *ini *.html $(PROJECT_HOME)/rsm
install_header:
