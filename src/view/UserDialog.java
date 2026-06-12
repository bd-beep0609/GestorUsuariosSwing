package view;

import controller.UserController;
import model.User;
import javax.swing.*;
import java.awt.*;

public class UserDialog extends JDialog {
    private UserController controller;
    private User editingUser;
    private JTextField txtName, txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cbStatus;
    private JButton btnSave, btnCancel;

    public UserDialog(JFrame parent, UserController controller, User userToEdit) {
        super(parent, userToEdit == null ? "Nuevo Usuario" : "Editar Usuario", true);
        this.controller = controller;
        this.editingUser = userToEdit;
        setSize(400, 350);
        setLocationRelativeTo(parent);
        initComponents();
        if (userToEdit != null) loadUserData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Nombre
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nombre:*"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(15);
        panel.add(txtName, gbc);

        // Email
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Email:*"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        panel.add(txtEmail, gbc);

        // Contraseña (solo si es nuevo usuario)
        if (editingUser == null) {
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Contraseña:*"), gbc);
            gbc.gridx = 1;
            txtPassword = new JPasswordField(15);
            panel.add(txtPassword, gbc);
        }

        // Estado
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        String[] estados = {"Activo", "Inactivo"};
        cbStatus = new JComboBox<>(estados);
        panel.add(cbStatus, gbc);

        add(panel, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnSave = new JButton(editingUser == null ? "Guardar" : "Actualizar");
        btnCancel = new JButton("Cancelar");
        panelBotones.add(btnSave);
        panelBotones.add(btnCancel);
        add(panelBotones, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
    }

    private void loadUserData() {
        txtName.setText(editingUser.getName());
        txtEmail.setText(editingUser.getEmail());
        cbStatus.setSelectedIndex(editingUser.isActive() ? 0 : 1);
    }

    private void save() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        boolean active = cbStatus.getSelectedIndex() == 0;

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y Email son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (editingUser == null) {
            String password = new String(txtPassword.getPassword());
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Contraseña es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (controller.addUser(name, email, password, active)) {
                JOptionPane.showMessageDialog(this, "Usuario creado exitosamente");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            if (controller.updateUser(editingUser.getId(), name, email, active, editingUser.getRole())) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}