package view;

import controller.EstudianteController;
import controller.NotaController;
import model.Estudiante;
import model.Materia;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo para registrar notas de estudiantes
 *
 * @author Bayron
 * @version 3.0 - Contexto ESFE Articulado
 */
public class NotaDialog extends JDialog {
    private EstudianteController estudianteController;
    private NotaController notaController;
    private JComboBox<Estudiante> cbEstudiante;
    private JComboBox<Materia> cbMateria;
    private JTextField txtNota;
    private JTextArea txtObservacion;
    private JButton btnSave, btnCancel;

    // Lista de materias para ARTICULADO (Módulo de Software e Inglés)
    private Materia[] materias = {
            new Materia("Módulo de Software", "Articulado", "1°", "2"),
            new Materia("Módulo de Software", "Articulado", "2°", "1"),
            new Materia("Módulo de Software", "Articulado", "4°", "3"),
            new Materia("Inglés", "Articulado", "1°", "2"),
            new Materia("Inglés", "Articulado", "2°", "1"),
            new Materia("Inglés", "Articulado", "4°", "3")
    };

    public NotaDialog(JFrame parent, EstudianteController estudianteController, NotaController notaController) {
        super(parent, "Registrar Nota", true);
        this.estudianteController = estudianteController;
        this.notaController = notaController;
        setSize(550, 450);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Estudiante
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Estudiante:*"), gbc);
        gbc.gridx = 1;
        cbEstudiante = new JComboBox<>();
        cargarEstudiantes();
        cbEstudiante.setPreferredSize(new Dimension(300, 25));
        panel.add(cbEstudiante, gbc);

        // Materia
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Materia:*"), gbc);
        gbc.gridx = 1;
        cbMateria = new JComboBox<>(materias);
        cbMateria.setPreferredSize(new Dimension(300, 25));
        panel.add(cbMateria, gbc);

        // Nota
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nota (0-10):*"), gbc);
        gbc.gridx = 1;
        txtNota = new JTextField(10);
        txtNota.setPreferredSize(new Dimension(100, 25));
        panel.add(txtNota, gbc);

        // Observación
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Observación:"), gbc);
        gbc.gridx = 1;
        txtObservacion = new JTextArea(3, 25);
        txtObservacion.setLineWrap(true);
        txtObservacion.setWrapStyleWord(true);
        JScrollPane scrollObs = new JScrollPane(txtObservacion);
        scrollObs.setPreferredSize(new Dimension(300, 60));
        panel.add(scrollObs, gbc);

        add(panel, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnSave = new JButton("Registrar Nota");
        btnCancel = new JButton("Cancelar");
        panelBotones.add(btnSave);
        panelBotones.add(btnCancel);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
    }

    private void cargarEstudiantes() {
        cbEstudiante.removeAllItems();
        for (Estudiante e : estudianteController.getEstudiantesActivos()) {
            cbEstudiante.addItem(e);
        }

        // Personalizar cómo se muestra el estudiante en el comboBox
        cbEstudiante.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Estudiante) {
                    Estudiante e = (Estudiante) value;
                    value = e.getCarnet() + " - " + e.getNombreCompleto() + " (" + e.getCursoDisplay() + ")";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
    }

    private void save() {
        Estudiante estudiante = (Estudiante) cbEstudiante.getSelectedItem();
        Materia materia = (Materia) cbMateria.getSelectedItem();

        if (estudiante == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double nota;
        try {
            nota = Double.parseDouble(txtNota.getText().trim());
            if (nota < 0 || nota > 10) {
                JOptionPane.showMessageDialog(this, "La nota debe estar entre 0 y 10", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido para la nota", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String observacion = txtObservacion.getText().trim();
        if (observacion.isEmpty()) {
            observacion = "Sin observación";
        }

        if (notaController.addNota(estudiante.getId(), materia.getId(), nota, observacion)) {
            JOptionPane.showMessageDialog(this, "Nota registrada exitosamente para " + estudiante.getNombreCompleto());
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar nota", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}