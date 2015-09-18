files=Switch_Main.cpp Switch_Manager.cpp CommonFun.cpp cJSON.c Switch_Control.cpp Switch_Stream.cpp Stream.cpp

libs=-lpthread 

DEBUG=-g -D_DEBUG
	
avencoder:
	-rm -f Switch_main
	#g++ $(files) -fPIC $(libs) $(DEBUG) -D__linux__ -o Switch_main
	g++ $(files) -static $(libs) $(DEBUG) -D__linux__ -o Switch_main
clean:
	-rm -f Switch_main
	-rm -f *.so

