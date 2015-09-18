files=avencoder.cpp pthread_atfork.o

libs=-lpthread -lavmuxer -laudioencoder -laudiosource -lvideoencoder -lvideosource -ltssmooth

DEBUG=-g -D_DEBUG
	
avencoder:
	-rm -f avencoder
	g++ $(files) -fPIC $(libs) $(DEBUG) -D__linux__ -o avencoder
	
install:avencoder
	cp avencoder /usr/local/bin/	
clean:
	-rm -f avencoder
	-rm -f *.so

