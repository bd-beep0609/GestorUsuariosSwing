package view;

import controller.EstudianteController;
import controller.NotaController;
import controller.UserController;
import model.Estudiante;
import model.Nota;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Ventana principal del Sistema de Notas ESFE
 * Muestra pestañas de Estudiantes y Notas registradas
 *
 * @author Bayron
 * @version 5.0 - SQL Server + Fechas formateadas
 */
public class MainForm extends JFrame {
    private UserController userController;
    private EstudianteController estudianteController;
    private NotaController notaController;
    private User currentUser;

    // Componentes principales
    private JTabbedPane tabbedPane;
    private JTable tableEstudiantes;
    private DefaultTableModel tableEstudiantesModel;
    private JTable tableNotas;
    private DefaultTableModel tableNotasModel;
    private JLabel lblStats;
    private JTextField txtBuscar;

    public MainForm(UserController userController) {
        this.userController = userController;
        this.currentUser = userController.getCurrentUser();
        this.estudianteController = new EstudianteController();
        this.notaController = new NotaController();

        setTitle("Sistema de Notas - ESFE (Software)");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initMenu();
        initComponents();
        loadEstudiantesTable();
        loadNotasTable();

        // Actualizar estadísticas después de cargar los datos
        updateStats();

        // Mostrar bienvenida con rol
        String rol = currentUser.isAdmin() ? "Administrador" : "Usuario";
        JOptionPane.showMessageDialog(this, "Bienvenido " + currentUser.getName() + " (" + rol + ")",
                "Sistema de Notas ESFE", JOptionPane.INFORMATION_MESSAGE);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Menú Mantenimiento
        JMenu menuMant = new JMenu("Mantenimiento");

        JMenuItem itemUsers = new JMenuItem("Usuarios");
        itemUsers.addActionListener(e -> openUserManagement());
        menuMant.add(itemUsers);

        JMenuItem itemEstudiantes = new JMenuItem("Estudiantes");
        itemEstudiantes.addActionListener(e -> {
            tabbedPane.setSelectedIndex(0);
            loadEstudiantesTable();
        });
        menuMant.add(itemEstudiantes);

        JMenuItem itemNotas = new JMenuItem("Registrar Nota");
        itemNotas.addActionListener(e -> openNotaDialog());
        menuMant.add(itemNotas);

        menuBar.add(menuMant);

        // Menú Reportes
        JMenu menuReportes = new JMenu("Reportes");

        JMenuItem itemPromedios = new JMenuItem("Promedios por Estudiante");
        itemPromedios.addActionListener(e -> showPromediosReport());
        menuReportes.add(itemPromedios);

        JMenuItem itemPromediosCarrera = new JMenuItem("Promedios por Carrera");
        itemPromediosCarrera.addActionListener(e -> showPromediosPorCarreraReport());
        menuReportes.add(itemPromediosCarrera);

        menuBar.add(menuReportes);

        // Menú Perfil
        JMenu menuPerfil = new JMenu("Perfil");

        JMenuItem itemChangePassword = new JMenuItem("Cambiar Contraseña");
        itemChangePassword.addActionListener(e -> new ChangePasswordDialog(this, userController).setVisible(true));
        menuPerfil.add(itemChangePassword);

        JMenuItem itemLogout = new JMenuItem("Cerrar Sesión");
        itemLogout.addActionListener(e -> {
            userController.logout();
            dispose();
            new LoginForm().setVisible(true);
        });
        menuPerfil.add(itemLogout);

        menuBar.add(menuPerfil);

        setJMenuBar(menuBar);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        // Pestaña 1: Estudiantes
        JPanel panelEstudiantes = createEstudiantesPanel();
        tabbedPane.addTab("📚 Estudiantes", panelEstudiantes);

        // Pestaña 2: Notas Registradas
        JPanel panelNotas = createNotasPanel();
        tabbedPane.addTab("📝 Notas Registradas", panelNotas);

        add(tabbedPane, BorderLayout.CENTER);

        // Barra de estado
        lblStats = new JLabel(" ");
        lblStats.setBorder(BorderFactory.createEtchedBorder());
        add(lblStats, BorderLayout.SOUTH);
    }

    private JPanel createEstudiantesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel superior con botones y búsqueda
        JPanel panelSuperior = new JPanel(new BorderLayout());

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNew = new JButton("➕ Nuevo Estudiante");
        JButton btnEdit = new JButton("✏️ Modificar");
        JButton btnDelete = new JButton("🗑️ Eliminar");
        JButton btnRefresh = new JButton("🔄 Actualizar");

        panelBotones.add(btnNew);
        panelBotones.add(btnEdit);
        panelBotones.add(btnDelete);
        panelBotones.add(btnRefresh);

        // Barra de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("🔍 Buscar:"));
        txtBuscar = new JTextField(25);
        panelBusqueda.add(txtBuscar);

        panelSuperior.add(panelBotones, BorderLayout.NORTH);
        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);

        panel.add(panelSuperior, BorderLayout.NORTH);

        // Tabla de estudiantes (con Curso combinado)
        tableEstudiantesModel = new DefaultTableModel(new String[]{"ID", "Carnet", "Nombre", "Apellido", "Curso", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableEstudiantes = new JTable(tableEstudiantesModel);
        tableEstudiantes.getTableHeader().setReorderingAllowed(false);
        panel.add(new JScrollPane(tableEstudiantes), BorderLayout.CENTER);

        // Eventos
        btnNew.addActionListener(e -> {
            EstudianteDialog dialog = new EstudianteDialog(this, estudianteController, null);
            dialog.setVisible(true);
            loadEstudiantesTable();
            updateStats();
        });

        btnEdit.addActionListener(e -> {
            int row = tableEstudiantes.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un estudiante", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableEstudiantesModel.getValueAt(row, 0);
            Estudiante est = estudianteController.findById(id);
            if (est != null) {
                EstudianteDialog dialog = new EstudianteDialog(this, estudianteController, est);
                dialog.setVisible(true);
                loadEstudiantesTable();
                updateStats();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = tableEstudiantes.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un estudiante", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableEstudiantesModel.getValueAt(row, 0);
            String nombre = (String) tableEstudiantesModel.getValueAt(row, 2);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar a " + nombre + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                estudianteController.deleteEstudiante(id);
                loadEstudiantesTable();
                updateStats();
            }
        });

        btnRefresh.addActionListener(e -> {
            loadEstudiantesTable();
            updateStats();
        });

        // Búsqueda en tiempo real
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterEstudiantes(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterEstudiantes(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterEstudiantes(); }
        });

        return panel;
    }

    private JPanel createNotasPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRegistrarNota = new JButton("➕ Registrar Nueva Nota");
        JButton btnRefresh = new JButton("🔄 Actualizar");

        panelBotones.add(btnRegistrarNota);
        panelBotones.add(btnRefresh);
        panel.add(panelBotones, BorderLayout.NORTH);

        // Tabla de notas
        tableNotasModel = new DefaultTableModel(new String[]{"ID", "Estudiante", "Carrera", "Materia", "Nota", "Fecha", "Observación"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableNotas = new JTable(tableNotasModel);
        tableNotas.getTableHeader().setReorderingAllowed(false);
        panel.add(new JScrollPane(tableNotas), BorderLayout.CENTER);

        // Eventos
        btnRegistrarNota.addActionListener(e -> openNotaDialog());
        btnRefresh.addActionListener(e -> loadNotasTable());

        return panel;
    }

    private void loadEstudiantesTable() {
        filterEstudiantes();
    }

    private void filterEstudiantes() {
        tableEstudiantesModel.setRowCount(0);
        String searchText = txtBuscar != null ? txtBuscar.getText().trim() : "";
        List<Estudiante> estudiantes = estudianteController.getAllEstudiantes();

        for (Estudiante e : estudiantes) {
            if (searchText.isEmpty() ||
                    e.getCarnet().toLowerCase().contains(searchText.toLowerCase()) ||
                    e.getNombre().toLowerCase().contains(searchText.toLowerCase()) ||
                    e.getApellido().toLowerCase().contains(searchText.toLowerCase()) ||
                    e.getCursoDisplay().toLowerCase().contains(searchText.toLowerCase())) {

                tableEstudiantesModel.addRow(new Object[]{
                        e.getId(),
                        e.getCarnet(),
                        e.getNombre(),
                        e.getApellido(),
                        e.getCursoDisplay(),
                        e.getActivoText()
                });
            }
        }
    }

    private void loadNotasTable() {
        tableNotasModel.setRowCount(0);

        // Formateador de fecha amigable
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Nota n : notaController.getAllNotas()) {
            Estudiante e = estudianteController.findById(n.getEstudianteId());
            String nombreEstudiante = e != null ? e.getNombreCompleto() : "Desconocido";
            String carrera = e != null ? e.getCarrera() : "Desconocida";
            String nombreMateria = "Módulo " + n.getMateriaId();

            // Formatear fecha
            String fechaFormateada = "";
            if (n.getFecha() != null) {
                fechaFormateada = sdf.format(n.getFecha());
            }

            tableNotasModel.addRow(new Object[]{
                    n.getId(),
                    nombreEstudiante,
                    carrera,
                    nombreMateria,
                    n.getValor(),
                    fechaFormateada,
                    n.getObservacion()
            });
        }
    }

    private void updateStats() {
        try {
            if (estudianteController == null) {
                lblStats.setText("📊 Cargando estadísticas...");
                return;
            }

            int total = estudianteController.getAllEstudiantes().size();
            int activos = estudianteController.getEstudiantesActivos().size();
            int inactivos = total - activos;

            int software = estudianteController.getCantidadEstudiantesByCarrera("Software");

            // Contar estudiantes por nivel
            int primerAnio = 0;
            int segundoAnio = 0;
            int cuartoArticulado = 0;

            for (Estudiante e : estudianteController.getAllEstudiantes()) {
                if (e.getNivel() != null) {
                    switch (e.getNivel()) {
                        case "1°":
                            primerAnio++;
                            break;
                        case "2°":
                            segundoAnio++;
                            break;
                        case "4° Articulado":
                            cuartoArticulado++;
                            break;
                    }
                }
            }

            lblStats.setText("📊 Total: " + total + " estudiantes | ✅ Activos: " + activos +
                    " | ❌ Inactivos: " + inactivos +
                    " | 💻 Software: " + software +
                    " | 📚 1° Año: " + primerAnio +
                    " | 📘 2° Año: " + segundoAnio +
                    " | 🎓 4° Articulado: " + cuartoArticulado);
        } catch (Exception e) {
            lblStats.setText("📊 Estadísticas no disponibles");
            System.err.println("Error en updateStats: " + e.getMessage());
        }
    }

    private void openUserManagement() {
        JOptionPane.showMessageDialog(this, "Gestión de Usuarios - Para administradores",
                "Usuarios", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openNotaDialog() {
        NotaDialog dialog = new NotaDialog(this, estudianteController, notaController);
        dialog.setVisible(true);
        loadNotasTable();
        updateStats();
    }

    private void showPromediosReport() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("📊 REPORTE DE PROMEDIOS POR ESTUDIANTE\n");
        reporte.append("══════════════════════════════════════════════════\n\n");

        for (Estudiante e : estudianteController.getAllEstudiantes()) {
            double promedio = notaController.getPromedioByEstudiante(e.getId());
            String estado = promedio >= 6.0 ? "✅ APROBADO" : "❌ REPROBADO";

            reporte.append(String.format("📌 %s - %s %s\n", e.getCarnet(), e.getNombreCompleto(), e.getCursoDisplay()));
            reporte.append(String.format("   Promedio: %.2f - %s\n\n", promedio, estado));
        }

        JTextArea textArea = new JTextArea(reporte.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(600, 500));
        JOptionPane.showMessageDialog(this, scroll, "Reporte de Promedios", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showPromediosPorCarreraReport() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("📊 REPORTE DE PROMEDIOS POR CARRERA\n");
        reporte.append("══════════════════════════════════════════════════\n\n");

        String[] carreras = {"Software", "Turismo", "Eléctrica", "Mercadeo"};

        for (String carrera : carreras) {
            List<Estudiante> estudiantesCarrera = estudianteController.getEstudiantesByCarrera(carrera);
            if (estudiantesCarrera.isEmpty()) {
                reporte.append(String.format("📌 %s: Sin estudiantes registrados\n\n", carrera));
                continue;
            }

            double sumaPromedios = 0;
            int aprobados = 0;
            int reprobados = 0;

            for (Estudiante e : estudiantesCarrera) {
                double promedio = notaController.getPromedioByEstudiante(e.getId());
                sumaPromedios += promedio;
                if (promedio >= 6.0) {
                    aprobados++;
                } else if (promedio > 0) {
                    reprobados++;
                }
            }

            double promedioCarrera = sumaPromedios / estudiantesCarrera.size();

            reporte.append(String.format("🎓 %s\n", carrera));
            reporte.append(String.format("   Estudiantes: %d\n", estudiantesCarrera.size()));
            reporte.append(String.format("   Promedio general: %.2f\n", promedioCarrera));
            reporte.append(String.format("   Aprobados: %d | Reprobados: %d\n\n", aprobados, reprobados));
        }

        JTextArea textArea = new JTextArea(reporte.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(this, scroll, "Reporte por Carrera", JOptionPane.INFORMATION_MESSAGE);
    }
}