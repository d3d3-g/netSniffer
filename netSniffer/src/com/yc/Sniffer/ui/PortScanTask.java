package com.yc.Sniffer.ui;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class PortScanTask implements Runnable {
	private String ip;
	private List<Integer> freePort = new ArrayList<Integer>();
	private NotifyInfo notifyFinished;
	private int startPort = 0;
	private int endPort = 65536;
	private NotifyFinished nf;
	private boolean flag = true;

	/**
	 * Ҫɨ���ip��ַ
	 * 
	 * @param ip
	 */
	public PortScanTask(String ip, NotifyInfo notifyFinished, NotifyFinished nf) {
		this.ip = ip;
		this.notifyFinished = notifyFinished;
		this.nf = nf;
	}

	public PortScanTask(String ip, NotifyInfo notifyFinished, int startPort,
			int endPort, NotifyFinished nf) {
		this.ip = ip;
		this.notifyFinished = notifyFinished;
		this.startPort = startPort;
		this.endPort = endPort;
		this.nf = nf;
	}

	@Override
	public void run() {
		Socket s = null;
		for (int i = startPort; i < endPort; i++) {
			if (!flag) {
				if (this.nf != null) {
					this.nf.notify(true);
				}
				break;
			}
			try {
				s = new Socket(this.ip, i);
				// �ص�֪ͨ
				if (notifyFinished != null) {
					MachineInfo mi = new MachineInfo();
					mi.setIp(this.ip);
					mi.setPort(i);
					mi.setIsBusy("ռ��");
					notifyFinished.notify(mi);
				}
			} catch (Exception e) {
				freePort.add(i);
				// �ص�֪ͨ
				if (notifyFinished != null) {
					MachineInfo mi = new MachineInfo();
					mi.setIp(this.ip);
					mi.setPort(i);
					mi.setIsBusy("����");
					notifyFinished.notify(mi);
				}
			}
		}
		if (this.nf != null) {
			this.nf.notify(true);
		}

	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
