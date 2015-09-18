files=audiosource.cpp

libs=-lpthread

DEBUG=-g -D_DEBUG

libaudiosource.so: $(files)
	g++ -shared -fPIC $(files) $(libs) $(DEBUG) -D__linux__ -o libaudiosource.so

install:libaudiosource.so
	-strip -x libaudiosource.so
	cp libaudiosource.so /usr/local/lib/
	cp audiosource.h /usr/local/include/
	
uninstall:
	rm -rf /usr/local/lib/libaudiosource.so
	rm -rf /usr/local/include/audiosource.h
	
audiosource:
	-rm -f audiosource
	g++ test.cpp -fPIC $(files) $(libs) $(DEBUG) -D__linux__ -o audiosource
clean:
	-rm -f libaudiosource
	-rm -f *.so

