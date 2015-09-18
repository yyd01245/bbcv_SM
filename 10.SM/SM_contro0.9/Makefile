files=SM_main.cpp SM_Manager.cpp Navigation.cpp CommonFun.cpp cJSON.c Base64.cpp SM_Control.cpp DBInterface.cpp Stream.cpp  Advertisement.cpp MSI_SMStream.cpp VGW_VodStream.cpp Config.cpp CommonData.cpp

libs=-lpthread -l mysqlclient -L /usr/lib/mysql/plugin/ -I /usr/include/mysql/ 

DEBUG=-g -D_DEBUG
	
avencoder:
	-rm -f SM_main
	g++ $(files) -fPIC $(libs) $(DEBUG) -D__linux__ -o SM_main
clean:
	-rm -f SM_main
	-rm -f *.so

