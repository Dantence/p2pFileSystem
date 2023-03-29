package window;

import cache.Cache;
import common.message.MessageType;
import common.util.CommonUtil;
import common.util.FileScanner;
import common.util.PropertyParser;
import service.BroadcastSenderService;
import service.SendFileService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ClientWindow extends JFrame {

    private JPanel mainPanel, filePanel, buttonPanel;
    private JLabel fileLabel;
    private JTextField fileTextField;
    private JButton chooseFileButton, uploadButton, showUserListButton, showShareFileListButton, showDownloadFileButton;
    private JFileChooser fileChooser;
    private File selectedFile, selectedShareFile;

    private final SendFileService sendFileService = new SendFileService();

    public ClientWindow() {

        initUI();
    }

    private void initUI() {

        setTitle("局域网文件传输系统");
        setSize(400, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 执行关闭窗口事件时所需的操作
                BroadcastSenderService.sendMessage(MessageType.BROADCAST_LOGOUT_REQ);
                // 关闭程序
                //dispose();
                System.exit(0);
            }
        });

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        fileLabel = new JLabel("共享文件：");

        fileTextField = new JTextField();
        fileTextField.setPreferredSize(new Dimension(250, 24));
        fileTextField.setEditable(false);

        chooseFileButton = new JButton("选择文件");
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser = new JFileChooser();
                File desktopDir = new File(System.getProperty("user.home"), "Desktop");
                fileChooser.setCurrentDirectory(desktopDir);
                int returnVal = fileChooser.showOpenDialog(ClientWindow.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    selectedShareFile = fileChooser.getSelectedFile();
                    fileTextField.setText(selectedShareFile.getAbsolutePath());
                }
            }
        });

        uploadButton = new JButton("发送共享文件");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedShareFile != null && selectedShareFile.exists()) {
                    try {
                        FileInputStream inputStream = new FileInputStream(selectedShareFile);
                        FileOutputStream outputStream = new FileOutputStream(PropertyParser.getShareRoot() + "/" + selectedShareFile.getName());
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        inputStream.close();
                        outputStream.close();

                        for (String address : Cache.ipTable) {
                            if (!CommonUtil.checkSelf(address)) {
                                sendFileService.sendFile(selectedShareFile.getName(), Cache.localHost, address);
                            }
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }


                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "请先选择要发送的文件!");
                }
            }
        });

        showUserListButton = new JButton("在线用户列表");
        showUserListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showUserList();
            }
        });

        showShareFileListButton = new JButton("共享文件列表");
        showShareFileListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showShareFileList();
            }
        });


        showDownloadFileButton = new JButton("查看下载文件");
        showDownloadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = PropertyParser.getDownloadRoot();
                File file = new File(path);
                try {
                    Desktop.getDesktop().open(file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        filePanel.add(fileLabel);
        filePanel.add(fileTextField);
        filePanel.add(chooseFileButton);

        buttonPanel.add(uploadButton);
        buttonPanel.add(showUserListButton);
        buttonPanel.add(showShareFileListButton);
        buttonPanel.add(showDownloadFileButton);

        mainPanel.add(filePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        pack();
        setResizable(false);
        setVisible(true);
    }

    private void showUserList() {
        JFrame userListFrame = new JFrame("在线用户列表");
        userListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // create the user list
        DefaultListModel<String> userListModel = new DefaultListModel<String>();
        for (String ip : Cache.ipTable) {
            try {
                if (!CommonUtil.checkSelf(ip)) {
                    userListModel.addElement(ip);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JList<String> userList = new JList<String>(userListModel);

        // create the "Send File" button
        JButton sendFileButton = new JButton("发送文件");

        sendFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedUser = userList.getSelectedValue();
                if (selectedUser != null) {
                    fileChooser = new JFileChooser();
                    File desktopDir = new File(System.getProperty("user.home"), "Desktop");
                    fileChooser.setCurrentDirectory(desktopDir);
                    int returnVal = fileChooser.showOpenDialog(ClientWindow.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        selectedFile = fileChooser.getSelectedFile();
                    }
                    if (selectedFile != null && selectedFile.exists()) {
                        try {
                            FileInputStream inputStream = new FileInputStream(selectedFile);
                            FileOutputStream outputStream = new FileOutputStream(PropertyParser.getShareRoot() + "/" + selectedFile.getName());
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            inputStream.close();
                            outputStream.close();

                            sendFileService.sendFile(selectedFile.getName(), Cache.localHost, selectedUser);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(userListFrame, "文件 " + selectedFile.getAbsolutePath() + " 已发送至 " + selectedUser);
                    } else {
                        JOptionPane.showMessageDialog(userListFrame, "请先选择要发送的用户!");
                    }
                }
            }
        });

        // create the "Back" button
        JButton backButton = new JButton("返回");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userListFrame.dispose();
            }
        });

        // add the user list, send file button and back button to the frame
        JPanel userListPanel = new JPanel(new GridLayout(1, 1));
        userListPanel.add(userList);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(sendFileButton);
        buttonPanel.add(backButton);

        userListFrame.getContentPane().add(userListPanel, BorderLayout.CENTER);
        userListFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // set the frame size and make it visible
        userListFrame.setPreferredSize(new Dimension(300, 200));
        userListFrame.pack();
        userListFrame.setLocationRelativeTo(null);
        userListFrame.setVisible(true);
    }


    private void showShareFileList() {
        JFrame fileListFrame = new JFrame("共享文件列表");
        fileListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // create the user list
        DefaultListModel<String> fileListModel = new DefaultListModel<String>();

        for (String ip : Cache.ipTable) {
            try {
                if (CommonUtil.checkSelf(ip)) {
                    for(String file : Cache.ipRecourceMap.get(ip)) {
                        fileListModel.addElement(file);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JList<String> fileList = new JList<String>(fileListModel);

        // create the "Send File" button
        JButton sendFileButton = new JButton("删除");

        sendFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFile = fileList.getSelectedValue();
                if (selectedFile != null) {
                    File file = new File(PropertyParser.getShareRoot() + "/" + selectedFile);
                    if (file.exists()) {
                        boolean isDeleted = file.delete();
                        if (isDeleted) {
                            JOptionPane.showMessageDialog(fileListFrame, "文件删除成功!");
                            fileListModel.removeElement(selectedFile);
                        } else {
                            JOptionPane.showMessageDialog(fileListFrame, "文件删除失败!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(fileListFrame, "该文件已经被删除!");
                    }
                    Cache.add(Cache.localHost, FileScanner.getAllFiles(PropertyParser.getShareRoot()));
                } else {
                    JOptionPane.showMessageDialog(fileListFrame, "请先选择要删除的文件!");
                }
            }
        });

        // create the "Back" button
        JButton backButton = new JButton("返回");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileListFrame.dispose();
            }
        });

        // add the user list, send file button and back button to the frame
        JPanel userListPanel = new JPanel(new GridLayout(1, 1));
        userListPanel.add(fileList);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(sendFileButton);
        buttonPanel.add(backButton);

        fileListFrame.getContentPane().add(userListPanel, BorderLayout.CENTER);
        fileListFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // set the frame size and make it visible
        fileListFrame.setPreferredSize(new Dimension(300, 200));
        fileListFrame.pack();
        fileListFrame.setLocationRelativeTo(null);
        fileListFrame.setVisible(true);
    }
}
