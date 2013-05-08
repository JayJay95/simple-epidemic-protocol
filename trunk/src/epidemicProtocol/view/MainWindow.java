package epidemicProtocol.view;

import epidemicProtocol.clients.BroadcastClient;
import epidemicProtocol.clients.MessagesSender;
import epidemicProtocol.control.TargetsList;
import epidemicProtocol.servers.SpreadServer;
import epidemicProtocol.servers.TargetsListServer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.text.DefaultCaret;

/**
 * Class that have de main method of this project and the user interface. It starts the 3 initial
 * threads of the application: {@link epidemicProtocol.servers.TargetsListServer}, {@link epidemicProtocol.clients.BroadcastClient},
 * {@link epidemicProtocol.servers.SpreadServer} and
 * {@link epidemicProtocol.clients.MessagesSender}.
 *
 * @author Lucas S Bueno
 */
public class MainWindow extends javax.swing.JFrame {

   /**
    * Port that will be used to discover other computers in the local network.
    */
   private static final int TARGETS_LIST_PORT = 60000;
   /**
    * Port that will be used to send messages from one computer to other.
    */
   private static final int SPREAD_SERVER_PORT = 60001;
   /**
    * IP's list that will be used in {@link epidemicProtocol.servers.TargetsListServer} to store
    * other computers in the network.
    */
   private static TargetsList targetsList;

   /**
    * Creates new form MainWindow
    */
   public MainWindow() {
      initComponents();
      DefaultCaret caret = (DefaultCaret) outputView.getCaret();
      caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
      redirectSystemStreams(); //redirect the System output to the TextArea in the interface
      messageTextField.requestFocus();
      this.setLocationRelativeTo(null);
      targetsList = new TargetsList(ipListTextArea);      
   }

   /**
    * This method is called from within the constructor to initialize the form. WARNING: Do NOT
    * modify this code. The content of this method is always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      outputScrollPane = new javax.swing.JScrollPane();
      outputView = new javax.swing.JTextArea();
      messageTextField = new javax.swing.JTextField();
      spreadLabel = new javax.swing.JLabel();
      sendButton = new javax.swing.JButton();
      ipListScroll = new javax.swing.JScrollPane();
      ipListTextArea = new javax.swing.JTextArea();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      setTitle("Epidemic Protocol");
      setResizable(false);

      outputView.setEditable(false);
      outputView.setColumns(20);
      outputView.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
      outputView.setRows(5);
      outputScrollPane.setViewportView(outputView);

      messageTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
      messageTextField.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyPressed(java.awt.event.KeyEvent evt) {
            messageTextFieldKeyPressed(evt);
         }
      });

      spreadLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
      spreadLabel.setText("Enter a message to spread:");

      sendButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
      sendButton.setText("Send");
      sendButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            sendButtonActionPerformed(evt);
         }
      });

      ipListTextArea.setEditable(false);
      ipListTextArea.setColumns(20);
      ipListTextArea.setRows(5);
      ipListScroll.setViewportView(ipListTextArea);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(messageTextField)
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                  .addGap(0, 0, Short.MAX_VALUE)
                  .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(layout.createSequentialGroup()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(layout.createSequentialGroup()
                        .addComponent(spreadLabel)
                        .addGap(0, 444, Short.MAX_VALUE))
                     .addComponent(outputScrollPane))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(ipListScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(ipListScroll)
               .addComponent(outputScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE))
            .addGap(18, 18, 18)
            .addComponent(spreadLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(messageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(sendButton)
            .addContainerGap())
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

   /**
    * Try to send the String in the TextField to the {@link epidemicProtocol.servers.SpreadServer}.
    * To make it we create a new {@link epidemicProtocol.clients.MessagesSender} thread with the
    * content of the TextField in the constructor. After that the TextField is cleared.
    */
   private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
      new MessagesSender(messageTextField.getText(), SPREAD_SERVER_PORT).start();
      messageTextField.setText("");
      messageTextField.requestFocus();
   }//GEN-LAST:event_sendButtonActionPerformed

   private void messageTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageTextFieldKeyPressed
      if (evt.getKeyChar() == 10) {
         sendButtonActionPerformed(null);
      }
   }//GEN-LAST:event_messageTextFieldKeyPressed

   /**
    * Entry point of the application. Starts the user's interface and the 3 initial threads.
    *
    * @param args Args in this main function are not used.
    */
   public static void main(String args[]) {
      /* Set the Nimbus look and feel */
      //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
       * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
       */
      try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (ClassNotFoundException ex) {
         java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (InstantiationException ex) {
         java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (IllegalAccessException ex) {
         java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (javax.swing.UnsupportedLookAndFeelException ex) {
         java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      //</editor-fold>

      /* Create and display the form */
      java.awt.EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            new MainWindow().setVisible(true);
            System.out.println("Starting Targets List Sever...");
            new TargetsListServer(targetsList, TARGETS_LIST_PORT).start();
            System.out.println("Starting Broadcast Client...");
            new BroadcastClient(TARGETS_LIST_PORT).start();
            System.out.println("Starting Spread Server...");
            System.out.println("Starting Epidemic Protocol...");
            new SpreadServer(targetsList, SPREAD_SERVER_PORT).start();
         }
      });
   }

   /**
    * Redirect the System output to the TextArea in the interface.
    */
   private void redirectSystemStreams() {
      OutputStream out = new OutputStream() {
         @Override
         public void write(int b) throws IOException {
            outputView.append(String.valueOf((char) b));
         }

         @Override
         public void write(byte[] b, int off, int len) throws IOException {
            outputView.append(new String(b, off, len));
         }

         @Override
         public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
         }
      };

      System.setOut(new PrintStream(out, true));
   }
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JScrollPane ipListScroll;
   private javax.swing.JTextArea ipListTextArea;
   private javax.swing.JTextField messageTextField;
   private javax.swing.JScrollPane outputScrollPane;
   private javax.swing.JTextArea outputView;
   private javax.swing.JButton sendButton;
   private javax.swing.JLabel spreadLabel;
   // End of variables declaration//GEN-END:variables
}
