import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Signup {
    private JTextField firstName;
    private JTextField middleName;
    private JTextField lastName;
    private JTextField ssn;
    private JTextField phoneNumber;
    private JTextField address;
    private JTextField username;
    private JTextField password;
    private JTextField email;
    private JButton signupButton;
    public JPanel mainPanel;

    public Signup(){
        signupButton.addActionListener(new SignupClick());
    }

    private class SignupClick implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            User user = new User(Facade.getNextUserId(), firstName.getText(), middleName.getText(), lastName.getText(),
                    ssn.getText(), phoneNumber.getText(), address.getText(), username.getText(), password.getText(), email.getText());
            Facade.addUser(user);
            Home home = new Home(user);
            Home.frame.setContentPane(home.mainPanel);
            Home.frame.pack();
            Home.frame.setVisible(true);
        }
    }
}
