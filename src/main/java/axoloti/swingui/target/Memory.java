package axoloti.swingui.target;

import axoloti.connection.IConnection;
import axoloti.target.PollHandler;
import axoloti.target.TargetController;
import axoloti.target.TargetModel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import qcmds.QCmdMemRead;

/**
 *
 * @author jtaelman
 */
public class Memory extends TJFrame implements ActionListener {

    final PollHandler poller;

    /**
     * Creates new form Memory
     */
    public Memory(TargetController controller) {
        super(controller);
        initComponents();
        setTitle("Memory viewer");
        jTextFieldAddr.setFont(Font.getFont(Font.MONOSPACED));
        jTextAreaMemoryContent.setFont(Font.getFont(Font.MONOSPACED));
        jTextAreaMemoryContent.setEditable(false);

        poller = new PollHandler() {
            @Override
            public void operation() {
                readmem();
            }
        };

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldAddr = new javax.swing.JTextField();
        jButtonUpdate = new javax.swing.JButton();
        jButtonInc = new javax.swing.JButton();
        jButtonDec = new javax.swing.JButton();
        jCheckBoxUpdate = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaMemoryContent = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(20, 20));

        jLabel1.setText("Address:");

        jTextFieldAddr.setText("0x00000000");

        jButtonUpdate.setText("Update");
        jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });

        jButtonInc.setText("+0x100");
        jButtonInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIncActionPerformed(evt);
            }
        });

        jButtonDec.setText("-0x100");
        jButtonDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDecActionPerformed(evt);
            }
        });

        jCheckBoxUpdate.setText("Update continuously");
        jCheckBoxUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldAddr, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonInc))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxUpdate)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonDec)
                .addContainerGap(99, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldAddr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonInc)
                    .addComponent(jButtonDec))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBoxUpdate)))
        );

        jTextAreaMemoryContent.setColumns(20);
        jTextAreaMemoryContent.setRows(5);
        jScrollPane1.setViewportView(jTextAreaMemoryContent);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    void readmem() {
        jTextAreaMemoryContent.setFont(new Font("monospaced", Font.PLAIN, 12));
        int length = 256;
        IConnection conn = getController().getModel().getConnection();
        conn.AppendToQueue(new QCmdMemRead(addr, length, new IConnection.MemReadHandler() {
            @Override
            public void Done(ByteBuffer mem) {
                String s = "";
                if (mem != null) {
                    for (int i = 0; i < (length / 16); i++) {
                        ByteBuffer bc = mem.duplicate();
                        int v1 = mem.getInt();
                        int v2 = mem.getInt();
                        int v3 = mem.getInt();
                        int v4 = mem.getInt();
                        String ascii = "";
                        for (int j = 0; j < 16; j++) {
                            char c = (char) bc.get();
                            if (c < 32) {
                                c = '.';
                            }
                            if (c > 128) {
                                c = '.';
                            }
                            ascii += c;
                        }
                        s += String.format("%08x : %08x %08x %08x %08x  %s\n", addr + (i * 16), v1, v2, v3, v4, ascii);
                    }
                }
                jTextAreaMemoryContent.setText(s);
            }
        }));
    }

    int addr = 0;

    void setAddr(int addr) {
        if (this.addr == addr) {
            return;
        }
        jTextFieldAddr.setText(String.format("0x%08x", addr));
        this.addr = addr;
    }

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
        String hex = jTextFieldAddr.getText();
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }
        setAddr((new BigInteger(hex, 16)).intValue());
        readmem();
    }//GEN-LAST:event_jButtonUpdateActionPerformed

    private void jButtonIncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIncActionPerformed
        setAddr(addr + 0x100);
        readmem();
    }//GEN-LAST:event_jButtonIncActionPerformed

    private void jButtonDecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDecActionPerformed
        setAddr(addr - 0x100);
        readmem();
    }//GEN-LAST:event_jButtonDecActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        dispose();
    }//GEN-LAST:event_formWindowClosed

    private void jCheckBoxUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxUpdateActionPerformed
        setPolling(jCheckBoxUpdate.isSelected());
    }//GEN-LAST:event_jCheckBoxUpdateActionPerformed

    void setPolling(boolean b) {
        if (b) {
            TargetModel.getTargetModel().addPoller(poller);
        } else {
            TargetModel.getTargetModel().removePoller(poller);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    void showConnect1(boolean connected) {
        jButtonDec.setEnabled(connected);
        jButtonInc.setEnabled(connected);
        jButtonUpdate.setEnabled(connected);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDec;
    private javax.swing.JButton jButtonInc;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JCheckBox jCheckBoxUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaMemoryContent;
    private javax.swing.JTextField jTextFieldAddr;
    // End of variables declaration//GEN-END:variables


    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (TargetModel.CONNECTION.is(evt)) {
            showConnect1(evt.getNewValue() != null);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        setPolling(false);
    }

}
