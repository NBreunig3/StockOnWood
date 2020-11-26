import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    private JPasswordField password;
    public JPanel mainPanel;
    private JTextField username;
    private JButton logInBtn;
    private JButton signUpBtn;

    public Login(){
        Database.getInstance().connect();
        logInBtn.addActionListener(new LogInClick());
        signUpBtn.addActionListener(new SignupClick());
        username.setText("nbreunig");
        password.setText("password");
    }

    private class SignupClick implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Home.frame.setContentPane(new Signup().mainPanel);
            Home.frame.pack();
            Home.frame.setVisible(true);
        }
    }

    private class LogInClick implements ActionListener{
        private User user;
        public boolean processLogIn(){
            String u = username.getText();
            String p = password.getText();
            user = Facade.getUser(u, p);
            if (user == null){
                JOptionPane.showMessageDialog(null, "Username or password incorrect", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }else {
                return true;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (processLogIn()) {
                Home home = new Home(user);
                Home.frame.setContentPane(home.mainPanel);
                Home.frame.pack();
                Home.frame.setVisible(true);
            }
        }
    }
}
