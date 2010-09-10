/*
 * GBCamView.java
 */

package gbcam;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.image.BufferedImage;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.image4j.codec.bmp.*;
import gnu.io.*;
import java.io.*;

/**
 * The application's main frame.
 */
public class GBCamView extends FrameView {

    private SerialPort port = null;
    private InputStream in = null;
    private OutputStream out = null;
    private String serialPortName = null;
    private GBRegisters reg = new GBRegisters();
    private JFrame mainFrame = GBCamApp.getApplication().getMainFrame();
    private BufferedImage pic;
    private boolean getObjects=false;
    private boolean getPixels=true;

    public GBCamView(SingleFrameApplication app) {
        super(app);

        // TODO: Move all gnu.io stuff into Communications
        // TODO: give credit for RXTX
        initComponents();

        System.out.println("Creating BufferedImage");
        pic = new BufferedImage(128, 123, BufferedImage.TYPE_INT_RGB);
        System.out.println("Created BufferedImage");

        // TODO: fix Quit when closing window
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Preferences
        try {
            Preferences.load();
        }
        catch (Exception e) {
            // Silently fail if there's no preferences file.
            // it'll be created upon exit.
        }

        serialPortName = Preferences.loadSerialPortName();
        reg.setRegisters(Preferences.loadRegisters());

        // Prompt for serial port
        promptSerialPort();
        Preferences.saveSerialPortName(serialPortName);
        setupPort();
        showAll();

    }

    ////////////////////////////////////////////////////////////////////////
    // INTERFACE DISPLAY DATA
    ////////////////////////////////////////////////////////////////////////

    private void showAll() {
        showC0();
        showC1();
        showE();
        showG();
        showI();
        showM();
        showN();
        showO();
        showP();
        showV();
        showX();
        showZ();
        showStuffToGet();
    }

    public void showC0() {
        textC0.setText(Integer.toString(reg.getC0()));
    }

    public void showC1() {
        textC1.setText(Integer.toString(reg.getC1()));
    }

    private void showE() {
        comboBoxE.setSelectedIndex(reg.getE());
    }

    private void showG() {
        textG.setText(Integer.toString(reg.getG()));
    }

    private void showI() {
        checkBoxI.setSelected(reg.getI());
    }

    private void showM() {
        textM.setText(Integer.toString(reg.getM()));
    }

    private void showN() {
        checkBoxI.setSelected(reg.getN());
    }

    private void showO() {
        textO.setText(Integer.toString(reg.getO())); // b000XXXXX
    }

    private void showP() {
        textP.setText(Integer.toString(reg.getP()));
    }

    private void showV() {
        textV.setText(Integer.toString(reg.getV()));
    }

    private void showX() {
        textX.setText(Integer.toString(reg.getX()));
    }

    private void showZ() {
        int z = reg.getZ();

        if (z == 0x2) {
            comboBoxZ.setSelectedItem((Object) "Positive");
        }
        else if (z == 0x1) {
            comboBoxZ.setSelectedItem((Object) "Negative");
        }
        else {
            comboBoxZ.setSelectedItem((Object) "None");
        }
    }

    private void showStuffToGet() {
        if (getPixels && getObjects) {
            getWhatComboBox.setSelectedItem((Object) "Both");
        }
        else if (getPixels) {
            getWhatComboBox.setSelectedItem((Object) "Pixels");
        }
        else if (getObjects) {
            getWhatComboBox.setSelectedItem((Object) "Objects");
        }
    }

    ////////////////////////////////////////////////////////////////////////
    // SERIAL COMMUNICATIONS
    ////////////////////////////////////////////////////////////////////////

    private void setupPort() {

        try {
            CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(serialPortName);

            port = (SerialPort) cpi.open("Camera Image Viewer", 3000);
            port.setSerialPortParams(38400, SerialPort.DATABITS_8,
                                            SerialPort.STOPBITS_1,
                                            SerialPort.PARITY_NONE);
            port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            port.disableReceiveThreshold();
            port.disableReceiveTimeout();
            in = port.getInputStream();
            out = port.getOutputStream();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Unable to setup serial port: " + e, "Error",
            JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }


    private void promptSerialPort() {
        Vector v = new Vector();

        // Obtain a list of serial ports
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        while(en.hasMoreElements()) {
            CommPortIdentifier cpi = (CommPortIdentifier) en.nextElement();
            if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL)
                v.addElement(cpi.getName());
        }

        if (v.size() == 0) {
            // No serial ports found
            JOptionPane.showMessageDialog(mainFrame,
            "No serial ports found", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        serialPortName = (String) JOptionPane.showInputDialog(mainFrame,
            "Please confirm serial port selection", "Camera Image Viewer",
        JOptionPane.QUESTION_MESSAGE, null, v.toArray(), serialPortName);

        if (serialPortName == null)
            System.exit(1);
    }

    ////////////////////////////////////////////////////////////////////////
    // MORE INTERFACE STUFF
    ////////////////////////////////////////////////////////////////////////

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            aboutBox = new GBCamAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        GBCamApp.getApplication().show(aboutBox);
    }

    public File showSaveDialog() {
        if (saveDialog == null) {
            saveDialog = new SaveDialog(mainFrame, true);
            saveDialog.setLocationRelativeTo(mainFrame);
        }
        GBCamApp.getApplication().show(saveDialog);
        return saveDialog.file;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        takePicture = new javax.swing.JButton();
        imagePanel = new gbcam.ImagePanel();
        exposurePanel = new javax.swing.JPanel();
        textC1 = new javax.swing.JTextField();
        LabelC1 = new javax.swing.JLabel();
        textC0 = new javax.swing.JTextField();
        LabelC0 = new javax.swing.JLabel();
        checkBoxI = new javax.swing.JCheckBox();
        textG = new javax.swing.JTextField();
        LabelG = new javax.swing.JLabel();
        edgePanel = new javax.swing.JPanel();
        comboBoxE = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        textX = new javax.swing.JTextField();
        LabelG3 = new javax.swing.JLabel();
        textM = new javax.swing.JTextField();
        LabelG2 = new javax.swing.JLabel();
        textP = new javax.swing.JTextField();
        LabelG1 = new javax.swing.JLabel();
        checkBoxN = new javax.swing.JCheckBox();
        calibrationPanel = new javax.swing.JPanel();
        textO = new javax.swing.JTextField();
        LabelO = new javax.swing.JLabel();
        LabelO1 = new javax.swing.JLabel();
        textV = new javax.swing.JTextField();
        comboBoxZ = new javax.swing.JComboBox();
        takePicture1 = new javax.swing.JButton();
        histPanel = new gbcam.HistPanel();
        getWhatComboBox = new javax.swing.JComboBox();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setMaximumSize(new java.awt.Dimension(540, 300));
        mainPanel.setMinimumSize(new java.awt.Dimension(540, 300));
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(540, 300));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(gbcam.GBCamApp.class).getContext().getActionMap(GBCamView.class, this);
        takePicture.setAction(actionMap.get("takePicture")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gbcam.GBCamApp.class).getContext().getResourceMap(GBCamView.class);
        takePicture.setText(resourceMap.getString("takePicture.text")); // NOI18N
        takePicture.setToolTipText(resourceMap.getString("takePicture.toolTipText")); // NOI18N
        takePicture.setName("takePicture"); // NOI18N

        imagePanel.setForeground(resourceMap.getColor("imagePanel.foreground")); // NOI18N
        imagePanel.setToolTipText(resourceMap.getString("imagePanel.toolTipText")); // NOI18N
        imagePanel.setMaximumSize(new java.awt.Dimension(256, 246));
        imagePanel.setMinimumSize(new java.awt.Dimension(256, 246));
        imagePanel.setName("imagePanel"); // NOI18N
        imagePanel.setPreferredSize(new java.awt.Dimension(256, 246));

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 246, Short.MAX_VALUE)
        );

        exposurePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("exposurePanel.border.title"))); // NOI18N
        exposurePanel.setToolTipText(resourceMap.getString("exposurePanel.toolTipText")); // NOI18N
        exposurePanel.setName("exposurePanel"); // NOI18N
        exposurePanel.setPreferredSize(new java.awt.Dimension(200, 180));

        textC1.setText(resourceMap.getString("textC1.text")); // NOI18N
        textC1.setAction(actionMap.get("setC1")); // NOI18N
        textC1.setMaximumSize(new java.awt.Dimension(60, 20));
        textC1.setMinimumSize(new java.awt.Dimension(60, 20));
        textC1.setName("textC1"); // NOI18N
        textC1.setPreferredSize(new java.awt.Dimension(60, 20));
        textC1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textC1FocusLost(evt);
            }
        });

        LabelC1.setText(resourceMap.getString("LabelC1.text")); // NOI18N
        LabelC1.setName("LabelC1"); // NOI18N

        textC0.setText(resourceMap.getString("textC0.text")); // NOI18N
        textC0.setAction(actionMap.get("setC0")); // NOI18N
        textC0.setMaximumSize(new java.awt.Dimension(60, 20));
        textC0.setMinimumSize(new java.awt.Dimension(60, 20));
        textC0.setName("textC0"); // NOI18N
        textC0.setPreferredSize(new java.awt.Dimension(60, 20));
        textC0.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textC0FocusLost(evt);
            }
        });

        LabelC0.setText(resourceMap.getString("LabelC0.text")); // NOI18N
        LabelC0.setName("LabelC0"); // NOI18N

        checkBoxI.setAction(actionMap.get("toggleI")); // NOI18N
        checkBoxI.setText(resourceMap.getString("checkBoxI.text")); // NOI18N
        checkBoxI.setToolTipText(resourceMap.getString("checkBoxI.toolTipText")); // NOI18N
        checkBoxI.setName("checkBoxI"); // NOI18N

        textG.setAction(actionMap.get("setG")); // NOI18N
        textG.setMaximumSize(null);
        textG.setMinimumSize(null);
        textG.setName("textG"); // NOI18N
        textG.setPreferredSize(null);
        textG.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textGFocusLost(evt);
            }
        });

        LabelG.setText(resourceMap.getString("LabelG.text")); // NOI18N
        LabelG.setName("LabelG"); // NOI18N

        javax.swing.GroupLayout exposurePanelLayout = new javax.swing.GroupLayout(exposurePanel);
        exposurePanel.setLayout(exposurePanelLayout);
        exposurePanelLayout.setHorizontalGroup(
            exposurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exposurePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(exposurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(exposurePanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(LabelC0)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textC1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(exposurePanelLayout.createSequentialGroup()
                        .addComponent(LabelG)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(exposurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(exposurePanelLayout.createSequentialGroup()
                        .addComponent(LabelC1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textC0, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkBoxI))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        exposurePanelLayout.setVerticalGroup(
            exposurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exposurePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(exposurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(exposurePanelLayout.createSequentialGroup()
                        .addGroup(exposurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textC0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelC1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBoxI))
                    .addGroup(exposurePanelLayout.createSequentialGroup()
                        .addGroup(exposurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textC1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelC0))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(exposurePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelG)
                            .addComponent(textG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        edgePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("edgePanel.border.title"))); // NOI18N
        edgePanel.setToolTipText(resourceMap.getString("edgePanel.toolTipText")); // NOI18N
        edgePanel.setName("edgePanel"); // NOI18N

        comboBoxE.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "50%", "75%", "100%", "125%", "200%", "300%", "400%", "500%" }));
        comboBoxE.setAction(actionMap.get("setE")); // NOI18N
        comboBoxE.setName("comboBoxE"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Extract", "Enhance" }));
        jComboBox1.setToolTipText(resourceMap.getString("jComboBox1.toolTipText")); // NOI18N
        jComboBox1.setName("jComboBox1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        textX.setAction(actionMap.get("setX")); // NOI18N
        textX.setMaximumSize(new java.awt.Dimension(60, 20));
        textX.setMinimumSize(new java.awt.Dimension(60, 20));
        textX.setName("textX"); // NOI18N
        textX.setPreferredSize(new java.awt.Dimension(60, 20));
        textX.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textXFocusLost(evt);
            }
        });

        LabelG3.setText(resourceMap.getString("LabelG3.text")); // NOI18N
        LabelG3.setName("LabelG3"); // NOI18N

        textM.setAction(actionMap.get("setM")); // NOI18N
        textM.setMaximumSize(new java.awt.Dimension(60, 20));
        textM.setMinimumSize(new java.awt.Dimension(60, 20));
        textM.setName("textM"); // NOI18N
        textM.setPreferredSize(new java.awt.Dimension(60, 20));
        textM.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textMFocusLost(evt);
            }
        });

        LabelG2.setText(resourceMap.getString("LabelG2.text")); // NOI18N
        LabelG2.setName("LabelG2"); // NOI18N

        textP.setAction(actionMap.get("setP")); // NOI18N
        textP.setMaximumSize(new java.awt.Dimension(60, 20));
        textP.setMinimumSize(new java.awt.Dimension(60, 20));
        textP.setName("textP"); // NOI18N
        textP.setPreferredSize(new java.awt.Dimension(60, 20));
        textP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textPFocusLost(evt);
            }
        });

        LabelG1.setText(resourceMap.getString("LabelG1.text")); // NOI18N
        LabelG1.setName("LabelG1"); // NOI18N

        checkBoxN.setAction(actionMap.get("toggleN")); // NOI18N
        checkBoxN.setText(resourceMap.getString("checkBoxN.text")); // NOI18N
        checkBoxN.setName("checkBoxN"); // NOI18N

        javax.swing.GroupLayout edgePanelLayout = new javax.swing.GroupLayout(edgePanel);
        edgePanel.setLayout(edgePanelLayout);
        edgePanelLayout.setHorizontalGroup(
            edgePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(edgePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(edgePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(edgePanelLayout.createSequentialGroup()
                        .addComponent(checkBoxN)
                        .addGap(11, 11, 11)
                        .addComponent(LabelG1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textP, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LabelG2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textM, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LabelG3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textX, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(edgePanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        edgePanelLayout.setVerticalGroup(
            edgePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(edgePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(edgePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(edgePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelG1)
                    .addComponent(textP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkBoxN)
                    .addComponent(LabelG2)
                    .addComponent(textM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelG3)
                    .addComponent(textX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        calibrationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("calibrationPanel.border.title"))); // NOI18N
        calibrationPanel.setToolTipText(resourceMap.getString("calibrationPanel.toolTipText")); // NOI18N
        calibrationPanel.setName("calibrationPanel"); // NOI18N

        textO.setAction(actionMap.get("setO")); // NOI18N
        textO.setMaximumSize(new java.awt.Dimension(60, 20));
        textO.setMinimumSize(new java.awt.Dimension(60, 20));
        textO.setName("textO"); // NOI18N
        textO.setPreferredSize(new java.awt.Dimension(60, 20));
        textO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textOFocusLost(evt);
            }
        });

        LabelO.setText(resourceMap.getString("LabelO.text")); // NOI18N
        LabelO.setName("LabelO"); // NOI18N

        LabelO1.setText(resourceMap.getString("LabelO1.text")); // NOI18N
        LabelO1.setName("LabelO1"); // NOI18N

        textV.setAction(actionMap.get("setV")); // NOI18N
        textV.setMaximumSize(new java.awt.Dimension(60, 20));
        textV.setMinimumSize(new java.awt.Dimension(60, 20));
        textV.setName("textV"); // NOI18N
        textV.setPreferredSize(new java.awt.Dimension(60, 20));
        textV.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textVFocusLost(evt);
            }
        });

        comboBoxZ.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Positive", "Negative" }));
        comboBoxZ.setAction(actionMap.get("assignZ")); // NOI18N
        comboBoxZ.setName("comboBoxZ"); // NOI18N

        javax.swing.GroupLayout calibrationPanelLayout = new javax.swing.GroupLayout(calibrationPanel);
        calibrationPanel.setLayout(calibrationPanelLayout);
        calibrationPanelLayout.setHorizontalGroup(
            calibrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calibrationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelO)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textO, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LabelO1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textV, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        calibrationPanelLayout.setVerticalGroup(
            calibrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calibrationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(calibrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelO)
                    .addComponent(textO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelO1)
                    .addComponent(textV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        takePicture1.setAction(actionMap.get("savePicture")); // NOI18N
        takePicture1.setText(resourceMap.getString("takePicture1.text")); // NOI18N
        takePicture1.setToolTipText(resourceMap.getString("takePicture1.toolTipText")); // NOI18N
        takePicture1.setName("takePicture1"); // NOI18N

        histPanel.setName("histPanel"); // NOI18N

        javax.swing.GroupLayout histPanelLayout = new javax.swing.GroupLayout(histPanel);
        histPanel.setLayout(histPanelLayout);
        histPanelLayout.setHorizontalGroup(
            histPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );
        histPanelLayout.setVerticalGroup(
            histPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 51, Short.MAX_VALUE)
        );

        getWhatComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Both", "Pixels", "Objects" }));
        getWhatComboBox.setAction(actionMap.get("assignStuffToGet")); // NOI18N
        getWhatComboBox.setName("getWhatComboBox"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(histPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(exposurePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                    .addComponent(calibrationPanel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(edgePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(takePicture, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(getWhatComboBox, 0, 69, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(takePicture1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(exposurePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edgePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calibrationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(takePicture)
                            .addComponent(takePicture1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(getWhatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(histPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem1.setAction(actionMap.get("savePicture")); // NOI18N
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setToolTipText(resourceMap.getString("jMenuItem1.toolTipText")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        fileMenu.add(jMenuItem1);

        exitMenuItem.setAction(actionMap.get("saveAndQuit")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setToolTipText(resourceMap.getString("exitMenuItem.toolTipText")); // NOI18N
        exitMenuItem.setLabel(resourceMap.getString("exitMenuItem.label")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void textC0FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textC0FocusLost
        assignC0();
    }//GEN-LAST:event_textC0FocusLost

    private void textC1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textC1FocusLost
        assignC1();
    }//GEN-LAST:event_textC1FocusLost

    private void textGFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textGFocusLost
        assignG();
    }//GEN-LAST:event_textGFocusLost

    private void textOFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textOFocusLost
        assignO();
    }//GEN-LAST:event_textOFocusLost

    private void textVFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textVFocusLost
        assignV();
    }//GEN-LAST:event_textVFocusLost

    private void textPFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textPFocusLost
        assignP();
    }//GEN-LAST:event_textPFocusLost

    private void textMFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textMFocusLost
        assignM();
    }//GEN-LAST:event_textMFocusLost

    private void textXFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textXFocusLost
        assignX();
    }//GEN-LAST:event_textXFocusLost

    // TODO: Display communications timeout error dialog
    @Action
    public void takePicture() {
        int timeout=5000; // 5s timeout for serial reads
        int min;
        int max;
        int c;
        int[] hist = new int[256];
        int hist2bucket = 4;
        int[] hist2 = new int[256>>hist2bucket];

        // TODO: move take picture / mcu protocol stuff into new class
        min = 255;
        max = 0;

        /*
         * Protocol Summary
         *   All integers are 1 byte HEX
         *   All characters are 1 byte ASCII
         *
         * Set Registers:
         * PC: R...\n -- set registers by sending R followed by all register
         *     values and threshold value
         * AVR: K -- got registers
         *
         * Take picture+objects:
         * PC:  T\n -- tell Arduino to take a picture and send pixels + objects
         * AVR: S -- start data
         * AVR: R -- start pixels
         * AVR: ... -- each pixel sent as 0..255 value.  128x123 pixels sent
         * AVR: O. -- start objects; object count sent after O
         * AVR: ... -- each object sent as:
         *      object index
         *      left, top, right, bottom -- dimensions of bounding box
         * AVR: ! -- all done
         */

        //while (true){
            try {
                // make sure input buffer is empty
                System.out.println("Flushing buffer");
                while (in.available() > 0)
                    in.read();
                // transmit 'p' command character to take a picture
                System.out.println("Sending R command");
                out.write('R');
                int[] r = reg.getRegisters();
                for (int i=0; i < r.length; i++) {
                    // print registers
                    out.write(r[i]);
                    System.out.print(Integer.toString(r[i]) + " ");
                }
                System.out.println();

                /*
                 * TODO: enable control of threshold
                 */
                System.out.println("Sending threshold");
                out.write(220);

                System.out.println("Waiting for K response");
                c = Communication.getByte(in, timeout);
                System.out.println("Received "+Character.toString((char) c)+" response");

                /*
                 * Retrieves pixels and objects
                 */
                System.out.println("Sending T command");
                out.write('T');

                /*
                 * Retrieve objects only
                 */
                /*
                System.out.println("Sending O command");
                out.write('O');
                */

                System.out.println("Waiting for S response");
                c = Communication.getByte(in, timeout);
                System.out.println("Received "+Character.toString((char) c)+" response");

                // TODO: need special character for getting greyscale
                // TODO: or need to fix AVR to not always send stream of pixels
                System.out.println("Waiting for R response");
                while ((c = Communication.getByte(in, timeout)) != 'R')
                    ;
                System.out.println("Received "+Character.toString((char) c)+" response");

                if (getPixels) {
                    for (int y = 0; y < 123; y++) {
                        for (int x = 0; x < 128; x++) {
                            int pixel = Communication.getByte(in, timeout) & 0xFF;
                            //System.out.println("x="+Integer.toString(x)+" y="+Integer.toString(y)+" pixel="+Integer.toString(pixel));
                            if (pixel < 0 || pixel > 255)
                                throw new Exception("Illegal pixel value");
                            imagePanel.setXY(x, y, pixel);
                            hist[pixel]++;
                            hist2[pixel>>hist2bucket]++;
                            // TODO: fix setRGB
                            pic.setRGB(x, y, pixel << 16 | pixel << 8 | pixel);
                            if (pixel < min) min = pixel;
                            if (pixel > max) max = pixel;
                        } // for x
                        // read newline character at end of each row
                        Communication.getByte(in, timeout);
                    } // for y
                    System.out.println("Done reading");
                    System.out.println("Min: " + Integer.toString(min));
                    System.out.println("Max: " + Integer.toString(max));
                } // if getPixels

                if (getObjects) {
                    // Now read in all the objects
                    int oi = 0;
                    int i = 0;
                    int left;
                    int right;
                    int top;
                    int bottom;
                    imagePanel.clearHighlight(); // wipe out previous highlights on image

                    System.out.println("Waiting for O response");
                    c = Communication.getByte(in, timeout);
                    System.out.println("Received "+Character.toString((char) c)+" response");

                    // read in object count
                    c = Communication.getByte(in, timeout);
                    System.out.println("Reading "+Integer.toString(c)+" objects");
                    for (i = 0; i < c; i++) {
                        oi = Communication.getByte(in, timeout);
                        left = Communication.getByte(in, timeout);
                        top = Communication.getByte(in, timeout);
                        right = Communication.getByte(in, timeout);
                        bottom = Communication.getByte(in, timeout);

                        int width = right - left + 1;
                        int height = bottom - top + 1;
                        int ratio = (100*width) / height;

                        System.out.println("Object #" + Integer.toString((int) oi)+" / "+
                                           Integer.toString(i));
                        System.out.println("left:         " + Integer.toString(left));
                        System.out.println("top:          " + Integer.toString(top));
                        System.out.println("right:        " + Integer.toString(right));
                        System.out.println("bottom:       " + Integer.toString(bottom));
                        System.out.println("Dimension:    " + Integer.toString(width)+
                                            " x " + Integer.toString(height));
                        System.out.println("Width:Height: " + Integer.toString(ratio)+"%");
                        System.out.println();

                        // Draw bounding box

                        for (int x = left; x <= right; x++) {
                            imagePanel.setHighlightXY(x, top);
                            imagePanel.setHighlightXY(x, bottom);
                        }
                        for (int y = top; y <= bottom; y++) {
                            imagePanel.setHighlightXY(left, y);
                            imagePanel.setHighlightXY(right, y);
                        }

                    } // for
                    System.out.println("Done reading");
                } // if getObjects

            } catch (Exception e) {
                System.out.println("Exception occurred: " + e);
                //break;
            }
            // TODO: figure out how to paint BufferedImage instead
            // paint the image
            imagePanel.repaint();
        //} // while

        // cumulative sum of bin %s in hist2
        int sum = 0;
        for (int i=hist2.length-1; i >= 0; i--) {
            sum += ((1000 * hist2[i]) / 15744);
            //System.out.println(Integer.toString(i) + ": " + Integer.toString(hist2[i]) + " " + Integer.toString(sum/10) + "%");
            if (sum > 20) break;
        }

        // TODO: Img Histogram class
        // determine max value for histogram
        // and scale accordingly
        int hmax=-1;
        double scale;
        for (int x = 0; x < 256; x++)
            if (hist[x] > hmax) hmax = hist[x];
        scale = 50.0 / (double) hmax;
        for (int x = 0; x < 256; x++) {
            // determine height to draw histo line
            int height = (int) Math.ceil(hist[x] * scale);
            for (int y = 0; y < 50; y++) {
                // height in pixels, but hist starts at y=0 at top
                // so need to think a little backwards
                // paint black if above the height mark, white if below
                if (y < 50-height)
                    histPanel.setXY(x, y, 0);
                else
                    histPanel.setXY(x, y, 220);
            }
        }
        histPanel.repaint();

    }

    // TODO: actions for setting vertical/horizontal edge detection

    @Action
    private void assignC0() {
        String text = textC0.getText();

        if (text != null && Utility.isDigit( text ))
            reg.setC0(Integer.parseInt(text));
        showC0();
    }

    @Action
    private void assignC1() {
        String text = textC1.getText();

        if (text != null && Utility.isDigit( text ))
            reg.setC1(Integer.parseInt(text));
        showC1();
    }

    @Action
    public void assignG() {
        String text = textG.getText();

        if (text != null && Utility.isDigit( text )) {
            int gain = Integer.parseInt( text );
            if (gain >= 0 && gain <= 0x1F) {
                reg.setG(gain);
            }
        }
        showG();
    }

    @Action
    public void assignO() {
        String text = textO.getText();

        if (text != null && Utility.isDigit( text )) {
            int offset = Integer.parseInt( text );
            if (-31 <= offset && offset <= 31) {
                reg.setO(offset);
            }
        }
        showO();
    }

    @Action
    public void assignV() {
        String text = textV.getText();

        if (text != null && Utility.isDigit( text )) {
            int vref = Integer.parseInt( text );
            if (vref >= 0 && vref <= 0x07) {
                reg.setV(vref);
            }
        }
        showV();
    }

    @Action
    public void toggleI() {
        reg.setI(checkBoxI.isSelected());
        System.out.print("I: ");
        System.out.println(reg.getI());
    }

    @Action
    public void toggleN() {
        reg.setN(checkBoxN.isSelected());
        System.out.print("N: ");
        System.out.println(reg.getN());
    }

    @Action
    public void assignZ() {
        String text = (String) comboBoxZ.getSelectedItem();
        // If selection is None, don't do anything else
        // If Positive, set 10, if Negative, set 01
        if (text != null) {
            if (text.equals("Positive")) {
                reg.setZ(0x02);
            }
            else if (text.equals("Negative")) {
                reg.setZ(0x01);
            }
            else {
                reg.setZ(0);
            }
        }
    }

    @Action
    public void assignE() {
        int which = (int) comboBoxE.getSelectedIndex();
        // Clear out E
        if (which >= 0 && which <= 7) {
            reg.setE(which);
        }
        //TODO: error
        showE();
    }

    @Action
    public void assignP() {
        String text = textP.getText();

        if (text != null && Utility.isDigit( text ))
            reg.setP(Integer.parseInt(text));
        showP();
    }

    @Action
    public void assignM() {
        String text = textM.getText();

        if (text != null && Utility.isDigit( text ))
            reg.setM(Integer.parseInt(text));
        showM();
    }

    @Action
    public void assignX() {
        String text = textX.getText();

        if (text != null && Utility.isDigit( text ))
            reg.setX(Integer.parseInt(text));
        showX();
    }

    @Action
    public void saveAndQuit() {
        // Preferences

        // TODO: save preferences dialog
        Preferences.saveSerialPortName(serialPortName);
        Preferences.saveRegisters(reg.getRegisters());
        try {
            Preferences.save();
        }
        catch (Exception e) {
            JFrame mainFrame = GBCamApp.getApplication().getMainFrame();
            JOptionPane.showMessageDialog(mainFrame, e.getMessage());
        }
        // call application quit; really should pass event
        GBCamApp.getApplication().quit(null);
    }

    @Action
    public void displayHistogram() {
        // go through the pixels
        // count up
        // open dialog
        // draw
    }

    @Action
    public void savePicture() {
        // save file dialog
        File file = showSaveDialog();

        // TODO: move file save routine into Picture
        if (file != null) {
            // convert pixels to bitmap format and save
            System.out.println("saving...");
            InfoHeader iheader = new InfoHeader();
            BMPImage img = new BMPImage(pic, BMPEncoder.createInfoHeader(pic));
            try {
                BMPEncoder.write(pic, file);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(mainFrame, e.getMessage());

            }
            System.out.println("saved");
        }
    }

    // TODO: add code to implement getWhatComboBox for getting obj, pixel, or both
    @Action
    public void assignStuffToGet() {
        String text = (String) getWhatComboBox.getSelectedItem();

        if (text == "Pixels") {
            getPixels = true;
            getObjects = false;
        }
        else if (text == "Objects") {
            getPixels = false;
            getObjects = true;
        }
        else if (text == "Both") {
            getPixels = true;
            getObjects = true;
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelC0;
    private javax.swing.JLabel LabelC1;
    private javax.swing.JLabel LabelG;
    private javax.swing.JLabel LabelG1;
    private javax.swing.JLabel LabelG2;
    private javax.swing.JLabel LabelG3;
    private javax.swing.JLabel LabelO;
    private javax.swing.JLabel LabelO1;
    private javax.swing.JPanel calibrationPanel;
    private javax.swing.JCheckBox checkBoxI;
    private javax.swing.JCheckBox checkBoxN;
    private javax.swing.JComboBox comboBoxE;
    private javax.swing.JComboBox comboBoxZ;
    private javax.swing.JPanel edgePanel;
    private javax.swing.JPanel exposurePanel;
    private javax.swing.JComboBox getWhatComboBox;
    private gbcam.HistPanel histPanel;
    private gbcam.ImagePanel imagePanel;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton takePicture;
    private javax.swing.JButton takePicture1;
    private javax.swing.JTextField textC0;
    private javax.swing.JTextField textC1;
    private javax.swing.JTextField textG;
    private javax.swing.JTextField textM;
    private javax.swing.JTextField textO;
    private javax.swing.JTextField textP;
    private javax.swing.JTextField textV;
    private javax.swing.JTextField textX;
    // End of variables declaration//GEN-END:variables

    private JDialog aboutBox;
    private SaveDialog saveDialog;
}
