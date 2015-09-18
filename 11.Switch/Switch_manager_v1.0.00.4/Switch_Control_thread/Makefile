# Because all these files are sensitive to directory structure, It needs
# a Makefile to install all files to the working directory.

#---------------------------------------------------------------------
#switch,allow add by you self,null is false
#---------------------------------------------------------------------
DEBUG=
NOTHREAD=
UNIX_SYS =
ORA=
LINUX =1

#---------------------------------------------------------------------
#include make common options 
#---------------------------------------------------------------------
include ./Cmake.pure

#---------------------------------------------------------------------
#product globle var 
#---------------------------------------------------------------------
COMMON_INC = -I ../include ../include/hiredis
COMMON_LIB = -L ../lib 
PRODINCL= 
PRODLIBS= -L ../lib -lSwitch_Config -lhiredis 
STATICLIBS= ../lib/libSwitch_Config.a ../lib/libhiredis.a
SYSLIB = -lm $(THREADLIB) -ldl -pthread 

#---------------------------------------------------------------------
#product local information, use to make name
#---------------------------------------------------------------------
MODULE_NAME=
BUILDTAIL=
TARGETS= SWM

#---------------------------------------------------------------------
# build rules
#---------------------------------------------------------------------
all: $(TARGETS)
install: all install_header install_lib install_bin

#---------------------------------------------------------------------
# product targets build rules
#---------------------------------------------------------------------


$(TARGETS):cJSON.o CommonFun.o Switch_Control.o  Switch_Manager.o Switch_Stream.o \
			Stream.o Switch_worker_Sever.o Switch_Main.o redis_client.o \
			redis_instance.o redis_pool.o
	$(CC) $^ $(STATICLIBS)  -static $(SYSLIB) -lrt -o $@





#----------------------------------------------------------------------
# keeping
#----------------------------------------------------------------------

clean:
	-rm -rf  *.o *.so *.a core $(TARGETS)
tar:SWM
	tar -czf ../release/SWM.tar.gz SWM *.config *.sh *.conf *.txt

install_lib:

install_bin:
	cp SWM *.config *.sh *.conf *.txt ../SW_Manager
install_header:


#---------------------------------------------------------------------
# static targets build rules
#---------------------------------------------------------------------
AR=ar
ARFLAGS=-cru
static: $(util_objs)
	$(AR) $(ARFLAGS) libutil.a $^ 
