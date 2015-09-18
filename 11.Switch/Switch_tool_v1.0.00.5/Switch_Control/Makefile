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
COMMON_INC = -I ../include
COMMON_LIB = -L ../lib 
PRODINCL= 
PRODLIBS= -L ../lib -lSwitch_Config
SYSLIB = -lm $(THREADLIB) -ldl -pthread

#---------------------------------------------------------------------
#product local information, use to make name
#---------------------------------------------------------------------
MODULE_NAME=
BUILDTAIL=
TARGETS= Switch_tool

#---------------------------------------------------------------------
# build rules
#---------------------------------------------------------------------
all: $(TARGETS)
install: all install_header install_lib install_bin

#---------------------------------------------------------------------
# product targets build rules
#---------------------------------------------------------------------


$(TARGETS):cJSON.o CommonFun.o Switch_Control.o  Switch_Manager.o Switch_Stream.o \
			Stream.o Switch_worker_Sever.o Switch_Main.o TSStreamInfo.o
	$(CC) $^  ../lib/libSwitch_Config.a -static $(SYSLIB) -lrt -o $@



#----------------------------------------------------------------------
# keeping
#----------------------------------------------------------------------

clean:
	-rm -rf  *.o *.so *.a core $(TARGETS)
tar:Switch_tool
	tar -czf ../release/Switch_tool.tar.gz Switch_tool *.config *.sh

install_lib:

install_bin:
	cp Switch_tool *.config *.sh ../SW_Tool
install_header:


#---------------------------------------------------------------------
# static targets build rules
#---------------------------------------------------------------------
AR=ar
ARFLAGS=-cru
static: $(util_objs)
	$(AR) $(ARFLAGS) libutil.a $^ 
