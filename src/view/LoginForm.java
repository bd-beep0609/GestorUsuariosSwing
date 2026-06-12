package view;

import controller.UserController;
import model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de inicio de sesión del Sistema de Notas ESFE
 *
 * @author Bayron
 * @version 3.0 - Con funcionalidad de mostrar/ocultar contraseña
 */
public class LoginForm extends JDialog {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JCheckBox chkMostrarPassword;
    private JButton btnLogin, btnCancel;
    private UserController controller;

    public LoginForm() {
        setTitle("Login - Sistema de Notas ESFE");
        setModal(true);
        setSize(450, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        controller = new UserController();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel central con GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Título
        JLabel lblTitulo = new JLabel("SISTEMA DE NOTAS ESFE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(0, 100, 0));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblTitulo, gbc);

        // Email
        row++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("📧 Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);

        // Contraseña
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("🔒 Contraseña:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        panel.add(txtPassword, gbc);

        // Checkbox Mostrar contraseña
        row++;
        gbc.gridx = 1;
        gbc.gridy = row;
        chkMostrarPassword = new JCheckBox("👁️ Mostrar contraseña");
        chkMostrarPassword.addActionListener(e -> {
            if (chkMostrarPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0); // Muestra el texto
            } else {
                txtPassword.setEchoChar('•'); // Oculta con puntos
            }
        });
        panel.add(chkMostrarPassword, gbc);

        add(panel, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnLogin = new JButton("🔓 Iniciar Sesión");
        btnCancel = new JButton("❌ Cancelar");

        // Estilo de los botones
        btnLogin.setBackground(new Color(0, 150, 0));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        panelBotones.add(btnLogin);
        panelBotones.add(btnCancel);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnLogin.addActionListener(e -> login());
        btnCancel.addActionListener(e -> System.exit(0));

        // Enter key en campos
        txtEmail.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());
    }

    private void login() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ Complete todos los campos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = controller.authenticate(email, password);
        if (user != null) {
            dispose(); // Cerrar login
            new MainForm(controller).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "❌ Email o contraseña incorrectos\n\n" +
                            "📌 Usuario por defecto:\n" +
                            "   Email: admin@demo.com\n" +
                            "   Contraseña: admin123",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }
}