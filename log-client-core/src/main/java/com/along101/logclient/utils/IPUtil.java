package com.along101.logclient.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IPUtil {
	private static String ip;
	static{
		ip = localIP();
	}
	private static final String NETWORK_CARD = "eth0";
	private static final String NETWORK_CARD_BAND = "bond0";

	public static String getLocalHostName() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getLinuxLocalIP() {
		String ip = "";
		try {
			Enumeration<NetworkInterface> e1 = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface ni = e1.nextElement();
				if ((NETWORK_CARD.equals(ni.getName())) || (NETWORK_CARD_BAND.equals(ni.getName()))) {
					Enumeration<InetAddress> e2 = ni.getInetAddresses();
					while (e2.hasMoreElements()) {
						InetAddress ia = e2.nextElement();
						if (ia instanceof Inet6Address) {
							continue;
						}
						ip = ia.getHostAddress();
					}
					break;
				} else {
					continue;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ip;
	}

	public static String getWinLocalIP() {
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress().toString();
		} finally {
			return ip;
		}
	}

	public static String getLocalIP() {
		return ip;
	}
	public static String localIP() {
		String ip = null;
		if (!System.getProperty("os.name").contains("Win")) {
			ip = getLinuxLocalIP();
		} else {
			ip = getWinLocalIP();
		}
		return ip;
	}
}
