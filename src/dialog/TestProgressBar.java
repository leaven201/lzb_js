package dialog;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
 
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
 
/**
 * 杩涘害鏉℃祴璇曠▼搴�
 *
 * @author ygh
 *
 */
public class TestProgressBar implements ActionListener {
 
    private static final String DEFAULT_STATUS = "Please Waiting";
    private JDialog dialog;
    private JProgressBar progressBar;
    private JLabel lbStatus;
    private JButton btnCancel;
    private Window parent;
    private Thread thread; // 澶勭悊涓氬姟鐨勭嚎绋�
    private String statusInfo;
    private String resultInfo;
    private String cancelInfo;
    /**
     * 鏄剧ず杩涘害鏉℃祴璇曞璇濇
     * @param parent
     * @param thread
     */
    public static void show(Window parent, Thread thread) {
        new TestProgressBar(parent, thread, DEFAULT_STATUS, null, null);
    }
    /**
     * 鏄剧ず杩涘害鏉℃祴璇曞璇濇
     * @param parent
     * @param thread
     * @param statusInfo
     */
    public static void show(Window parent, Thread thread, String statusInfo) {
        new TestProgressBar(parent, thread, statusInfo, null, null);
    }
 
    /**
     * 鏄剧ず瀵硅瘽妗�
     * @param parent
     * @param thread
     * @param statusInfo
     * @param resultInfo
     * @param cancelInfo
     */
    public static void show(Window parent, Thread thread, String statusInfo,
            String resultInfo, String cancelInfo) {
        new TestProgressBar(parent, thread, statusInfo, resultInfo, cancelInfo);
    }
 
    /**
     * 瀵硅瘽妗嗘瀯閫犲嚱鏁�
     * @param parent
     * @param thread
     * @param statusInfo
     * @param resultInfo
     * @param cancelInfo
     */
    private TestProgressBar(Window parent, Thread thread, String statusInfo,
            String resultInfo, String cancelInfo) {
        this.parent = parent;
        this.thread = thread;
        this.statusInfo = statusInfo;
        this.resultInfo = resultInfo;
        this.cancelInfo = cancelInfo;
        initUI();
        startThread();
        dialog.setVisible(true);
    }
    /**
     * 鏋勫缓鏄剧ず杩涘害鏉＄殑瀵硅瘽妗�
     */
    private void initUI() {
        if (parent instanceof Dialog) {
            dialog = new JDialog((Dialog) parent, true);
        } else if (parent instanceof Frame) {
            dialog = new JDialog((Frame) parent, true);
        } else {
            dialog = new JDialog((Frame) null, true);
        }
 
        final JPanel mainPane = new JPanel(null);
        progressBar = new JProgressBar();
        lbStatus = new JLabel("" + statusInfo);
        btnCancel = new JButton("Cancel");
        progressBar.setIndeterminate(true);
        btnCancel.addActionListener(this);
 
        mainPane.add(progressBar);
        mainPane.add(lbStatus);
        // mainPane.add(btnCancel);
 
        dialog.getContentPane().add(mainPane);
        dialog.setUndecorated(true); // 闄ゅ幓title
        dialog.setResizable(true);
        dialog.setSize(390, 100);
        dialog.setLocationRelativeTo(parent); // 璁剧疆姝ょ獥鍙ｇ浉瀵逛簬鎸囧畾缁勪欢鐨勪綅缃�
 
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // 涓嶅厑璁稿叧闂�
 
        mainPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                layout(mainPane.getWidth(), mainPane.getHeight());
            }
        });
    }
    /**
     * 鍚姩绾跨▼锛屾祴璇曡繘搴︽潯
     */
    private void startThread() {
        new Thread() {
            public void run() {
                try {
                    thread.start(); // 澶勭悊鑰楁椂浠诲姟
                    // 绛夊緟浜嬪姟澶勭悊绾跨▼缁撴潫
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 鍏抽棴杩涘害鎻愮ず妗�
                    dialog.dispose();
 
                    if (resultInfo != null && !resultInfo.trim().equals("")) {
                        String title = "Information";
                        JOptionPane.showMessageDialog(parent, resultInfo,
                                title,
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }.start();
    }
    /**
     * 璁剧疆鎺т欢鐨勪綅缃拰澶у皬
     * @param width
     * @param height
     */
    private void layout(int width, int height) {
        progressBar.setBounds(20, 20, 350, 15);
        lbStatus.setBounds(20, 50, 350, 25);
        btnCancel.setBounds(width - 85, height - 31, 75, 21);
    }
 
    @SuppressWarnings("deprecation")
    public void actionPerformed(ActionEvent e) {
        resultInfo = cancelInfo;
        thread.stop();
    }
 
    /**
     * 鍏ュ彛涓诲嚱鏁�
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 鐢ㄤ簬娴嬭瘯杩涘害鏉＄殑绾跨▼
        Thread thread = new Thread() {
            public void run() {
                int index = 0;
 
                while (index < 5) {
                    try {
                        sleep(1000);
                        System.out.println(++index);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        //鏄剧ず杩涘害鏉℃祴璇曞璇濇
        TestProgressBar.show((Frame) null, thread, "Status", "Result", "Cancel");
 
    }
}
