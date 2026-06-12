package view;

import controller.UserController;
import model.User;
import javax.swing.*;
import java.awt.*;

public class ChangePasswordDialog extends JDialog {
    private UserController controller;
    private JPasswordField txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    private JButton btnChange, btnCancel;

    public ChangePasswordDialog(JFrame parent, UserController controller) {
        super(parent, "Cambiar Contraseña", true);
        this.controller = controller;
        setSize(400, 250);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        // Email (solo lectura)
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        JTextField txtEmail = new JTextField(controller.getCurrentUser().getEmail(), 15);
        txtEmail.setEditable(false);
        panel.add(txtEmail, gbc);

        // Contraseña actual
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Contraseña actual:*"), gbc);
        gbc.gridx = 1;
        txtCurrentPassword = new JPasswordField(15);
        panel.add(txtCurrentPassword, gbc);

        // Nueva contraseña
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nueva contraseña:*"), gbc);
        gbc.gridx = 1;
        txtNewPassword = new JPasswordField(15);
        panel.add(txtNewPassword, gbc);

        // Confirmar nueva contraseña
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Confirmar contraseña:*"), gbc);
        gbc.gridx = 1;
        txtConfirmPassword = new JPasswordField(15);
        panel.add(txtConfirmPassword, gbc);

        add(panel, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnChange = new JButton("Cambiar");
        btnCancel = new JButton("Cancelar");
        panelBotones.add(btnChange);
        panelBotones.add(btnCancel);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnChange.addActionListener(e -> changePassword());
        btnCancel.addActionListener(e -> dispose());
    }

    private void changePassword() {
        String currentPwd = new String(txtCurrentPassword.getPassword());
        String newPwd = new String(txtNewPassword.getPassword());
        String confirmPwd = new String(txtConfirmPassword.getPassword());

        User currentUser = controller.getCurrentUser();

        if (!currentUser.getPassword().equals(currentPwd)) {
            JOptionPane.showMessageDialog(this, "Contraseña actual incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nueva contraseña no puede estar vacía", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPwd.equals(confirmPwd)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas nuevas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (controller.changePassword(currentUser.getId(), newPwd)) {
            JOptionPane.showMessageDialog(this, "Contraseña cambiada exitosamente");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al cambiar contraseña", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}