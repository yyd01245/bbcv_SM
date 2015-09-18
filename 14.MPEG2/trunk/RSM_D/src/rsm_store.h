#ifndef RSM_STORE_H_
#define RSM_STORE_H_

#include <stdlib.h>
#include "Singleton.h"
#include "PropConfig.h"
#include "Socket.h"

using namespace std;

class Rsm_Session_Msg
{
public:
	string session_id;
	string ipqam_ip;
	int encoder_id;
	int ipqam_port;
	int vnc_add;
	int key_add;
	int record_add;
	int record_user_id;
};

typedef struct {
	char session_id[16];
    char ipqam_ip[16];
    int encoder_id;
    int vnc_add;
    int ipqam_port;
    int key_add;
    int record_add;
	int record_user_id;
}SaveSessionMsg;


class Rsm_Session_Map
{
public:
	map<string,Rsm_Session_Msg> map_session;
	map<int,Rsm_Session_Msg> map_encoder;
	bool addUser(const char* session_id, int encoder_id, Rsm_Session_Msg msg) {
		ThreadLocker locker(m_session_Mutex);
		map_session[session_id] = msg;
		map_encoder[encoder_id] = msg;
		return true;
	}
	bool delUser(const char* session_id, int encoder_id) {
		ThreadLocker locker(m_session_Mutex);
		map_session.erase(session_id);
		map_encoder.erase(encoder_id);
		return true;
	}
	int checkUser() {
		ThreadLocker locker(m_session_Mutex);
		return map_encoder.size();
	}
protected:
	ThreadMutex  m_session_Mutex;
};

typedef Singleton<Rsm_Session_Map>  Rsm_Session;

#endif
