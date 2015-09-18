files=audioencoder.cpp

libs=-lpthread -lavformat -lavcodec -lavutil -lswscale

DEBUG=-g -D_DEBUG

libaudioencoder.so: $(files)
	g++ -shared -fPIC $(files) $(libs) $(DEBUG) -D__linux__ -o libaudioencoder.so

install:libaudioencoder.so
	-strip -x libaudioencoder.so
	cp libaudioencoder.so /usr/local/lib/
	cp audioencoder.h /usr/local/include/
	
uninstall:
	rm -rf /usr/local/lib/libaudioencoder.so
	rm -rf /usr/local/include/audioencoder.h
	
audioencoder:
	-rm -f audioencoder
	g++ test.cpp -fPIC $(files) $(libs) $(DEBUG) -D__linux__ -o audioencoder
clean:
	-rm -f libaudioencoder
	-rm -f *.so

