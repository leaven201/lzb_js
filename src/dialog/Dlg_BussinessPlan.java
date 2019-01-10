package dialog;

import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import algorithm.ResourceAlloc;
import algorithm.RouteAlloc;
import data.DataSave;
import data.Traffic;

public class Dlg_BussinessPlan {
	public Dlg_BussinessPlan() {
		List<Traffic> traList = new LinkedList<Traffic>();
		traList.addAll(Traffic.trafficList);
		if (!RouteAlloc.isRouteEmpty(traList)) {// ��·�ɲ�Ϊ��
			int test = JOptionPane.showConfirmDialog(null, "�滮�ѽ��У��Ƿ����·�ɣ�");
			switch (test) {
			case 0:// ���·��
				RouteAlloc.clearAllTrafficRoute(traList);

				DataSave.separate = Dlg_PolicySetting.a;
				DataSave.separate1 = Dlg_PolicySetting.b;
				DataSave.OSNR = Dlg_PolicySetting.c;
				DataSave.locknum = Dlg_PolicySetting.d;
				if (Dlg_PolicySetting.f) {

					Thread thread = new Thread() {
						public void run() {
							ResourceAlloc.index = 1;
							ResourceAlloc.allocateResource1(traList, Dlg_PolicySetting.h);
							while (ResourceAlloc.index < traList.size()) {
							}
						}
					};
					TestProgressBar.show((Frame) null, thread, "·�ɹ滮�У����Ժ�", null, "Cancel");

				} else {

					Thread thread = new Thread() {
						public void run() {
							ResourceAlloc.index = 1;
							ResourceAlloc.allocateResource(traList, Dlg_PolicySetting.h);
							while (ResourceAlloc.index < traList.size()) {
							}
						}
					};
					TestProgressBar.show((Frame) null, thread, "·�ɹ滮�У����Ժ�", null, "Cancel");

				}
				new Dlg_DesignResult();
				break;
			case 1:
				DataSave.separate = Dlg_PolicySetting.a;
				DataSave.separate1 = Dlg_PolicySetting.b;
				DataSave.OSNR = Dlg_PolicySetting.c;
				DataSave.locknum = Dlg_PolicySetting.d;
				if (Dlg_PolicySetting.f) {

					Thread thread = new Thread() {
						public void run() {
							ResourceAlloc.index = 1;
							ResourceAlloc.allocateResource1(traList, Dlg_PolicySetting.h);
							while (ResourceAlloc.index < traList.size()) {
							}
						}
					};
					TestProgressBar.show((Frame) null, thread, "·�ɹ滮�У����Ժ�", null, "Cancel");

				} else {
					Thread thread = new Thread() {
						public void run() {
							ResourceAlloc.index = 1;
							ResourceAlloc.allocateResource(traList, Dlg_PolicySetting.h);
							while (ResourceAlloc.index < traList.size()) {
							}
						}
					};
					TestProgressBar.show((Frame) null, thread, "·�ɹ滮�У����Ժ�", null, "Cancel");

				}
				new Dlg_DesignResult();
				break;
			case 2:
				break;
			}

		} else {
			DataSave.separate = Dlg_PolicySetting.a;
			DataSave.separate1 = Dlg_PolicySetting.b;
			DataSave.OSNR = Dlg_PolicySetting.c;
			DataSave.locknum = Dlg_PolicySetting.d;
			if (Dlg_PolicySetting.f) {
				Thread thread = new Thread() {
					public void run() {
						ResourceAlloc.index = 1;
						ResourceAlloc.allocateResource1(traList, Dlg_PolicySetting.h);
						while (ResourceAlloc.index < traList.size()) {
							System.out.println("hello");
						}
					}
				};
				TestProgressBar.show((Frame) null, thread, "·�ɹ滮�У����Ժ�", null, "Cancel");
			} else {

				Thread thread = new Thread() {
					public void run() {
						ResourceAlloc.index = 1;
						ResourceAlloc.allocateResource(traList, Dlg_PolicySetting.h);
						while (ResourceAlloc.index < traList.size()) {
							System.out.println("hello");
						}
					}
				};
				TestProgressBar.show((Frame) null, thread, "·�ɹ滮�У����Ժ�", null, "Cancel");
			}
			new Dlg_DesignResult();

		}
	}
}
