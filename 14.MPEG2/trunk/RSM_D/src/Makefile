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
PRODLIBS= -L ../lib -lnebulautil -lxsdk -lvncmc

#---------------------------------------------------------------------
#product local information, use to make name
#---------------------------------------------------------------------
MODULE_NAME=
BUILDTAIL=
TARGETS= resourcemag 

#---------------------------------------------------------------------
# build rules
#---------------------------------------------------------------------
all: $(TARGETS)
install: all install_header install_lib install_bin

#---------------------------------------------------------------------
# product targets build rules
#---------------------------------------------------------------------

resourcemag:cJSON.o env_check.o res_register.o rsm_checker_server.o rsm_worker_vncms.o rsm_config.o rsm_worker_resource.o rsm_worker_down.o rsm_worker_up.o rsm_main.o
	$(CC) $^  ../lib/libnebulautil.a $(SYSLIB) -lrt -o $@





#----------------------------------------------------------------------
# keeping
#----------------------------------------------------------------------

clean:
	-rm -rf  *.o *.so *.a core $(TARGETS)
tar:resourcemag
	tar -czf ../release/resourcemag.tar.gz resourcemag resourcemag.config *.sh

install_lib:

install_bin:
	cp resourcemag resourcemag.config *.sh ../rsm
install_header:


#---------------------------------------------------------------------
# static targets build rules
#---------------------------------------------------------------------
AR=ar
ARFLAGS=-cru
static: $(util_objs)
	$(AR) $(ARFLAGS) libutil.a $^ 
