package prod.nebula.test;
public class kg {
    public static void main(String args[]){
        try{
            java.net.InetAddress ad=java.net.InetAddress.getLocalHost();
            System.out.println(ad.getAddress());
            System.out.println(ad.getCanonicalHostName());
            System.out.println(ad.getHostName());
            System.out.println(ad.isMulticastAddress());
            System.out.println(ad.isLoopbackAddress());
            /*Windows output
             * [B@757aef
             * localhost
             * cpit-b524f2dfb2
             * false
             * true
             */
            java.util.Enumeration<java.net.NetworkInterface> en=java.net.NetworkInterface.getNetworkInterfaces();
            while(en.hasMoreElements()){
                java.net.NetworkInterface ni=en.nextElement();
                System.out.println("x:"+ni.getName());
                System.out.println("y:"+ni.getDisplayName());
                java.util.Enumeration<java.net.InetAddress> ads=ni.getInetAddresses();
                while(ads.hasMoreElements()){
                    java.net.InetAddress ip=ads.nextElement();
                    if(!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                            && !(ip.getHostAddress().indexOf(":")==-1)){
                        System.out.println("_My ip is:"+ip.getHostAddress());
                    }//end if
                }
            }
            /* Windows output
             * x:lo
             * y:MS TCP Loopback interface
             * x:eth0
             * y:Broadcom 440x 10/100 Integrated Controller
             */
        }catch(java.net.UnknownHostException e){
            /* Linux out all:
             * [B@19821f
             * localhost.localdomain
             * localhost.localdomain
             * false
             * true
             * x:lo
             * y:lo
             */    
        }catch(java.net.SocketException e){
            System.out.println("Socket exception");
        }
    }
}