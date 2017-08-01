package xuan.wen.zhi.qin;

import xuan.wen.zhi.qin.impl.AService;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Junit {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
//                InetAddress[] inetAddresses = InetAddress.getAllByName("www.spring.io");
                for (InetAddress inetAddress : InetAddress.getAllByName("www.spring.io")) {
                    System.err.println("inetAddress \t" + inetAddress);
                    System.err.println("CanonicalHostName \t" + inetAddress.getCanonicalHostName());
                    System.err.println("HostAddress \t" + inetAddress.getHostAddress());
                    System.err.println("HostName \t" + inetAddress.getHostName());
                    System.err.println("Address \t" + inetAddress.getAddress());
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
            System.err.println("networkInterface\t" + networkInterface);
            System.err.println("localhost\t" + localHost);
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()){
                System.err.println("enumeration\t" + enumeration.nextElement());
            }
        } catch (UnknownHostException |SocketException e) {
            e.printStackTrace();
        }
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName("eth0") ;
            System.err.println("networkInterface\t" + networkInterface);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String message = "合计大大";
        Junit j = new Junit();
        j.test((String it) -> {
            return it;
        }, message);
        j.test(new AService(), message);

    }

    public void test(IService iService, String params) {
        System.err.println(iService.say(params));
    }
}
