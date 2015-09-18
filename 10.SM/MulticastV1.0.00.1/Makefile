files=cMain.cpp cCommon.cpp cJSON.c cRecv.cpp cStartstream.cpp cStream.cpp cDatabase.cpp  cConfig.cpp cRead.cpp -pthread

libs=-lpthread -l mysqlclient -L /usr/lib/mysql/plugin/ -I /usr/include/mysql/

DEBUG=-g -D_DEBUG
	
avencoder:
	-rm -f Mul_main
	g++ $(files) -fPIC $(libs) $(DEBUG) -D__linux__ -o Mul_main
clean:
	-rm -f Mul_main
	-rm -f *.so

