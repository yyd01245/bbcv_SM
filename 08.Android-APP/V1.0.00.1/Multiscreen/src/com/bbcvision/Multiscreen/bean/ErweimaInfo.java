package com.bbcvision.Multiscreen.bean;

/**
 * Created by Nestor on 12月8日.
 */
public class ErweimaInfo {
    private String retcode;
    private eMsg msg;
    private String url;
    private String area;

    public class eMsg{
        private String channel;
        private String vodid;

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getVodid() {
            return vodid;
        }

        public void setVodid(String vodid) {
            this.vodid = vodid;
        }
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public eMsg getMsg() {
        return msg;
    }

    public void setMsg(eMsg msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
