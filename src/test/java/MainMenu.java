import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu {
    private JFrame frame;
    private JButton showUserListButton;

    public MainMenu() {
        frame = new JFrame("Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create the "Show User List" button
        showUserListButton = new JButton("Show Online User List");
        showUserListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showUserList();
            }
        });

        // add the button to the frame
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.add(showUserListButton);
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        // set the frame size and make it visible
        frame.setPreferredSize(new Dimension(300, 100));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // show the online user list window
    private void showUserList() {
        JFrame userListFrame = new JFrame("Online User List");
        userListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // create the user list
        DefaultListModel<String> userListModel = new DefaultListModel<String>();
        userListModel.addElement("User1");
        userListModel.addElement("User2");
        userListModel.addElement("User3");

        JList<String> userList = new JList<String>(userListModel);

        // create the "Send File" button
        JButton sendFileButton = new JButton("Send File");
        sendFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedUser = userList.getSelectedValue();
                if (selectedUser != null) {
                    // send file to selected user
                    JOptionPane.showMessageDialog(userListFrame, "File sent to " + selectedUser);
                } else {
                    JOptionPane.showMessageDialog(userListFrame, "Please select a user first.");
                }
            }
        });

        // create the "Back" button
        JButton backButton = new JButton("Back");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainMenu();
            }
        });
    }
}