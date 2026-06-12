package view;

import controller.EstudianteController;
import model.Estudiante;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo para crear/editar estudiantes
 *
 * @author Bayron
 * @version 4.0 - Carrera Software, niveles 1°, 2°, 4° Articulado
 */
public class EstudianteDialog extends JDialog {
    private EstudianteController controller;
    private Estudiante editingEstudiante;
    private JTextField txtCarnet, txtNombre, txtApellido;
    private JComboBox<String> cbCarrera;
    private JComboBox<String> cbNivel;
    private JComboBox<String> cbGrupo;
    private JComboBox<String> cbEstado;
    private JButton btnSave, btnCancel;

    public EstudianteDialog(JFrame parent, EstudianteController controller, Estudiante estudianteToEdit) {
        super(parent, estudianteToEdit == null ? "Nuevo Estudiante" : "Editar Estudiante", true);
        this.controller = controller;
        this.editingEstudiante = estudianteToEdit;
        setSize(500, 480);
        setLocationRelativeTo(parent);
        initComponents();
        if (estudianteToEdit != null) loadEstudianteData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Carnet
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Carnet:*"), gbc);
        gbc.gridx = 1;
        txtCarnet = new JTextField(15);
        panel.add(txtCarnet, gbc);

        // Nombre
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nombre:*"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(15);
        panel.add(txtNombre, gbc);

        // Apellido
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Apellido:*"), gbc);
        gbc.gridx = 1;
        txtApellido = new JTextField(15);
        panel.add(txtApellido, gbc);

        // Carrera (solo Software)
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Carrera:*"), gbc);
        gbc.gridx = 1;
        String[] carreras = {"Software"};
        cbCarrera = new JComboBox<>(carreras);
        cbCarrera.setEnabled(false); // Solo Software por ahora
        panel.add(cbCarrera, gbc);

        // Nivel (1°, 2°, 4° Articulado)
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nivel:*"), gbc);
        gbc.gridx = 1;
        String[] niveles = {"1°", "2°", "4° Articulado"};
        cbNivel = new JComboBox<>(niveles);
        panel.add(cbNivel, gbc);

        // Grupo (1 al 5)
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Grupo:*"), gbc);
        gbc.gridx = 1;
        String[] grupos = {"1", "2", "3", "4", "5"};
        cbGrupo = new JComboBox<>(grupos);
        panel.add(cbGrupo, gbc);

        // Estado
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        String[] estados = {"Activo", "Inactivo"};
        cbEstado = new JComboBox<>(estados);
        panel.add(cbEstado, gbc);

        add(panel, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnSave = new JButton(editingEstudiante == null ? "Guardar" : "Actualizar");
        btnCancel = new JButton("Cancelar");
        panelBotones.add(btnSave);
        panelBotones.add(btnCancel);
        add(panelBotones, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
    }

    private void loadEstudianteData() {
        txtCarnet.setText(editingEstudiante.getCarnet());
        txtNombre.setText(editingEstudiante.getNombre());
        txtApellido.setText(editingEstudiante.getApellido());
        cbCarrera.setSelectedItem(editingEstudiante.getCarrera());
        cbNivel.setSelectedItem(editingEstudiante.getNivel());
        cbGrupo.setSelectedItem(editingEstudiante.getGrupo());
        cbEstado.setSelectedIndex(editingEstudiante.isActivo() ? 0 : 1);
    }

    private void save() {
        String carnet = txtCarnet.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String carrera = (String) cbCarrera.getSelectedItem();
        String nivel = (String) cbNivel.getSelectedItem();
        String grupo = (String) cbGrupo.getSelectedItem();
        boolean activo = cbEstado.getSelectedIndex() == 0;

        if (carnet.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Carnet, Nombre y Apellido son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (editingEstudiante == null) {
            if (controller.addEstudiante(carnet, nombre, apellido, carrera, nivel, grupo, activo)) {
                JOptionPane.showMessageDialog(this, "Estudiante creado exitosamente");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Carnet ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            if (controller.updateEstudiante(editingEstudiante.getId(), carnet, nombre, apellido, carrera, nivel, grupo, activo)) {
                JOptionPane.showMessageDialog(this, "Estudiante actualizado");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}