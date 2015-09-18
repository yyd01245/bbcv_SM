#ifndef _ErrorMsg_H
#define _ErrorMsg_H

#include <string>

class ErrorMsg
{
protected:
        int m_sErrorCode;
        std:: string m_strMessage;

public:
        ErrorMsg()
        {
                m_sErrorCode = 0;
                m_strMessage = "" ;
        }
        ~ErrorMsg() {;}

        inline void set_errorCode(const int sErrorCode)
                { m_sErrorCode=sErrorCode; }
        inline int16 get_errorCode() const
                { return m_sErrorCode; }
        inline void set_errorMsg(const std:: string& strMessage )
                 { m_strMessage = strMessage ; }
        inline const std:: string & get_errorMsg() const
                { return m_strMessage ; }
        inline void clear()
                {
                m_sErrorCode = 0;
                m_strMessage = "";
                }
};                

#endif

