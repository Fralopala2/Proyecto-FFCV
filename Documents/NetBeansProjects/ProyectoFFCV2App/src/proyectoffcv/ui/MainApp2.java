package proyectoffcv.ui;

import entidades.*;
import proyectoffcv.logica.Federacion;
import proyectoffcv.logica.IFederacion;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@SuppressWarnings("unused")
public class MainApp2 {

    private IFederacion federacion;
    private JFrame frame;
    private JPanel contentPanel;

    public MainApp2() {
        federacion = Federacion.getInstance();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Proyecto FFCV - Gestión Federativa");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("src/resources/logo.png").getImage());

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(245, 247, 250);
                Color color2 = new Color(200, 230, 201);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(166, 7, 7));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel headerLabel = new JLabel("Sistema de Gestión Federativa FFCV", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerLabel.setForeground(Color.WHITE);

        ImageIcon logoIcon = new ImageIcon("src/resources/logo.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        logoLabel.setPreferredSize(new Dimension(50, 50));
        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(33, 37, 41));
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        Font menuFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color menuForeground = Color.WHITE;
        Color menuHover = new Color(66, 66, 66);

        ImageIcon categoryIcon = new ImageIcon("src/resources/iconos/categorias.png");
        ImageIcon clubIcon = new ImageIcon("src/resources/iconos/club.png");
        ImageIcon personIcon = new ImageIcon("src/resources/iconos/persona.png");
        ImageIcon employeeIcon = new ImageIcon("src/resources/iconos/empleado.png");
        ImageIcon facilityIcon = new ImageIcon("src/resources/iconos/instalaciones.png");
        ImageIcon groupIcon = new ImageIcon("src/resources/iconos/grupos.png");
        ImageIcon teamIcon = new ImageIcon("src/resources/iconos/teams.png");
        ImageIcon licenseIcon = new ImageIcon("src/resources/iconos/licencia.png");

        JMenu gestionMenu = new JMenu("Gestión");
        styleMenu(gestionMenu, menuFont, menuForeground, menuHover);
        gestionMenu.setIcon(new ImageIcon("src/resources/iconos/gestion.png")); // Añade este icono si lo tienes

        JMenuItem categoriasItem = new JMenuItem("Categorías", categoryIcon);
        JMenuItem clubesItem = new JMenuItem("Clubes", clubIcon);
        JMenuItem personasItem = new JMenuItem("Personas", personIcon);
        JMenuItem empleadosItem = new JMenuItem("Empleados", employeeIcon);
        JMenuItem instalacionesItem = new JMenuItem("Instalaciones", facilityIcon);
        JMenuItem gruposItem = new JMenuItem("Grupos", groupIcon);
        JMenuItem equiposItem = new JMenuItem("Equipos", teamIcon);
        JMenuItem licenciasItem = new JMenuItem("Licencias", licenseIcon);

        styleMenuItem(categoriasItem, menuFont, menuForeground, menuHover);
        styleMenuItem(clubesItem, menuFont, menuForeground, menuHover);
        styleMenuItem(personasItem, menuFont, menuForeground, menuHover);
        styleMenuItem(empleadosItem, menuFont, menuForeground, menuHover);
        styleMenuItem(instalacionesItem, menuFont, menuForeground, menuHover);
        styleMenuItem(gruposItem, menuFont, menuForeground, menuHover);
        styleMenuItem(equiposItem, menuFont, menuForeground, menuHover);
        styleMenuItem(licenciasItem, menuFont, menuForeground, menuHover);

        gestionMenu.add(categoriasItem);
        gestionMenu.add(clubesItem);
        gestionMenu.add(personasItem);
        gestionMenu.add(empleadosItem);
        gestionMenu.add(instalacionesItem);
        gestionMenu.add(gruposItem);
        gestionMenu.add(equiposItem);
        gestionMenu.add(licenciasItem);

        menuBar.add(gestionMenu);
        headerPanel.add(menuBar, BorderLayout.SOUTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.add(createWelcomePanel(), BorderLayout.CENTER);

        categoriasItem.addActionListener(e -> switchPanel(createCategoriaPanel()));
        clubesItem.addActionListener(e -> switchPanel(createClubPanel()));
        personasItem.addActionListener(e -> switchPanel(createPersonaPanel()));
        empleadosItem.addActionListener(e -> switchPanel(createEmpleadoPanel()));
        instalacionesItem.addActionListener(e -> switchPanel(createInstalacionPanel()));
        gruposItem.addActionListener(e -> switchPanel(createGrupoPanel()));
        equiposItem.addActionListener(e -> switchPanel(createEquipoPanel()));
        licenciasItem.addActionListener(e -> switchPanel(createLicenciaPanel()));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void switchPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Bienvenido al Sistema de Gestión FFCV", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(33, 37, 41));
        
        JLabel infoLabel = new JLabel("<html><center>Seleccione una opción del menú 'Gestión' para comenzar.<br>" +
                "Gestione categorías, clubes, personas, equipos y más.</center></html>", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        infoLabel.setForeground(new Color(66, 66, 66));
        
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        welcomePanel.add(infoLabel, BorderLayout.CENTER);
        return welcomePanel;
    }

    private JPanel createCategoriaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = new JPanel(new GridBagLayout());
        createPanel.setBackground(Color.WHITE);
        createPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Crear Nueva Categoría",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(33, 37, 41)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField nombreField = new JTextField(20);
        nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel ordenLabel = new JLabel("Nivel/Orden:");
        ordenLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField ordenField = new JTextField(20);
        ordenField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel precioLabel = new JLabel("Precio Licencia:");
        precioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField precioField = new JTextField(20);
        precioField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton crearButton = new JButton("Crear Categoría", new ImageIcon("src/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);

        crearButton.addActionListener(event -> {
            try {
                String nombre = nombreField.getText();
                int orden = Integer.parseInt(ordenField.getText());
                double precio = Double.parseDouble(precioField.getText());
                Categoria categoria = federacion.nuevaCategoria(nombre, orden, precio);
                JOptionPane.showMessageDialog(frame, "Categoría creada: " + categoria);
                clearFields(nombreField, ordenField, precioField);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Nivel y precio deben ser numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; createPanel.add(nombreLabel, gbc);
        gbc.gridx = 1; createPanel.add(nombreField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; createPanel.add(ordenLabel, gbc);
        gbc.gridx = 1; createPanel.add(ordenField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; createPanel.add(precioLabel, gbc);
        gbc.gridx = 1; createPanel.add(precioField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Consultar Categorías",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(33, 37, 41)));

        JButton listarButton = new JButton("Listar Categorías", new ImageIcon("src/resources/iconos/magnifier.png"));
        styleButton(listarButton, new Color(33, 37, 41), false);

        listarButton.addActionListener(event -> {
            List<Categoria> categorias = federacion.obtenerCategorias();
            JTextArea textArea = new JTextArea(10, 30);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textArea.setText("Categorías:\n");
            for (Categoria c : categorias) {
                textArea.append(c.toString() + "\n");
            }
            textArea.setEditable(false);
            listPanel.removeAll();
            listPanel.add(listarButton, BorderLayout.NORTH);
            listPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
            listPanel.revalidate();
            listPanel.repaint();
        });

        listPanel.add(listarButton, BorderLayout.NORTH);

        panel.add(createPanel, BorderLayout.NORTH);
        panel.add(listPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createClubPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = new JPanel(new GridBagLayout());
        createPanel.setBackground(Color.WHITE);
        createPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Gestión de Clubes",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(33, 37, 41)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nombreLabel = new JLabel("Nombre Club:");
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField nombreField = new JTextField(20);
        nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel fechaLabel = new JLabel("Fecha Fundación (YYYY-MM-DD):");
        fechaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField fechaField = new JTextField(20);
        fechaField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel dniPresLabel = new JLabel("DNI Presidente:");
        dniPresLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField dniPresField = new JTextField(20);
        dniPresField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel buscarLabel = new JLabel("Buscar Club por Nombre:");
        buscarLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField buscarField = new JTextField(20);
        buscarField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton crearClubButton = new JButton("Crear Club", new ImageIcon("src/resources/iconos/cross.png"));
        styleButton(crearClubButton, new Color(211, 47, 47), true);
        JButton buscarButton = new JButton("Buscar Club", new ImageIcon("src/resources/iconos/magnifier.png"));
        styleButton(buscarButton, new Color(33, 37, 41), false);

        crearClubButton.addActionListener(event -> {
            try {
                String nombre = nombreField.getText();
                LocalDate fecha = LocalDate.parse(fechaField.getText());
                String dni = dniPresField.getText();
                Persona presidente = federacion.buscaPersona(dni);
                if (presidente == null) {
                    JOptionPane.showMessageDialog(frame, "Presidente no encontrado. Regístrelo primero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Club club = federacion.nuevoClub(nombre, fecha, presidente);
                JOptionPane.showMessageDialog(frame, "Club creado: " + club);
                clearFields(nombreField, fechaField, dniPresField);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buscarButton.addActionListener(event -> {
            String nombre = buscarField.getText();
            Club club = federacion.buscarClub(nombre);
            JOptionPane.showMessageDialog(frame, club != null ? "Club encontrado: " + club : "Club no encontrado.");
        });

        gbc.gridx = 0; gbc.gridy = 0; createPanel.add(nombreLabel, gbc);
        gbc.gridx = 1; createPanel.add(nombreField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; createPanel.add(fechaLabel, gbc);
        gbc.gridx = 1; createPanel.add(fechaField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; createPanel.add(dniPresLabel, gbc);
        gbc.gridx = 1; createPanel.add(dniPresField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; createPanel.add(buscarLabel, gbc);
        gbc.gridx = 1; createPanel.add(buscarField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; createPanel.add(crearClubButton, gbc);
        gbc.gridy = 5; createPanel.add(buscarButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createPersonaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = new JPanel(new GridBagLayout());
        createPanel.setBackground(Color.WHITE);
        createPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Crear Nueva Persona",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(33, 37, 41)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel dniLabel = new JLabel("DNI:");
        dniLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField dniField = new JTextField(20);
        dniField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField nombreField = new JTextField(20);
        nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel apellido1Label = new JLabel("Apellido 1:");
        apellido1Label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField apellido1Field = new JTextField(20);
        apellido1Field.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel apellido2Label = new JLabel("Apellido 2:");
        apellido2Label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField apellido2Field = new JTextField(20);
        apellido2Field.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel fechaLabel = new JLabel("Fecha Nacimiento (YYYY-MM-DD):");
        fechaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField fechaField = new JTextField(20);
        fechaField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel poblacionLabel = new JLabel("Población:");
        poblacionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField poblacionField = new JTextField(20);
        poblacionField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel buscarDniLabel = new JLabel("Buscar por DNI:");
        buscarDniLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField buscarDniField = new JTextField(20);
        buscarDniField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton crearButton = new JButton("Crear Persona", new ImageIcon("src/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        JButton buscarButton = new JButton("Buscar Persona", new ImageIcon("src/resources/iconos/magnifier.png"));
        styleButton(buscarButton, new Color(33, 37, 41), false);
        JButton buscarMultiButton = new JButton("Buscar por Nombre y Apellidos", new ImageIcon("src/resources/iconos/magnifier.png"));
        styleButton(buscarMultiButton, new Color(33, 37, 41), false);

        crearButton.addActionListener(event -> {
            try {
                String dni = dniField.getText();
                String nombre = nombreField.getText();
                String apellido1 = apellido1Field.getText();
                String apellido2 = apellido2Field.getText();
                LocalDate fecha = LocalDate.parse(fechaField.getText());
                String poblacion = poblacionField.getText();
                Persona persona = federacion.nuevaPersona(dni, nombre, apellido1, apellido2, fecha, "user" + dni, "pass" + dni, poblacion);
                JOptionPane.showMessageDialog(frame, "Persona creada: " + persona);
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, poblacionField);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buscarButton.addActionListener(event -> {
            String dni = buscarDniField.getText();
            Persona persona = federacion.buscaPersona(dni);
            JOptionPane.showMessageDialog(frame, persona != null ? "Persona encontrada: " + persona : "Persona no encontrada.");
        });

        buscarMultiButton.addActionListener(event -> {
            String nombre = nombreField.getText();
            String apellido1 = apellido1Field.getText();
            String apellido2 = apellido2Field.getText();
            List<Persona> personas = federacion.buscaPersonas(nombre, apellido1, apellido2);
            JTextArea textArea = new JTextArea(10, 30);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textArea.setText("Personas encontradas:\n");
            for (Persona p : personas) {
                textArea.append(p.toString() + "\n");
            }
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Resultados", JOptionPane.INFORMATION_MESSAGE);
        });

        gbc.gridx = 0; gbc.gridy = 0; createPanel.add(dniLabel, gbc);
        gbc.gridx = 1; createPanel.add(dniField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; createPanel.add(nombreLabel, gbc);
        gbc.gridx = 1; createPanel.add(nombreField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; createPanel.add(apellido1Label, gbc);
        gbc.gridx = 1; createPanel.add(apellido1Field, gbc);
        gbc.gridx = 0; gbc.gridy = 3; createPanel.add(apellido2Label, gbc);
        gbc.gridx = 1; createPanel.add(apellido2Field, gbc);
        gbc.gridx = 0; gbc.gridy = 4; createPanel.add(fechaLabel, gbc);
        gbc.gridx = 1; createPanel.add(fechaField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; createPanel.add(poblacionLabel, gbc);
        gbc.gridx = 1; createPanel.add(poblacionField, gbc);
        gbc.gridx = 0; gbc.gridy = 6; createPanel.add(buscarDniLabel, gbc);
        gbc.gridx = 1; createPanel.add(buscarDniField, gbc);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 8; createPanel.add(buscarButton, gbc);
        gbc.gridy = 9; createPanel.add(buscarMultiButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createEmpleadoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = new JPanel(new GridBagLayout());
        createPanel.setBackground(Color.WHITE);
        createPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Crear Nuevo Empleado",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(33, 37, 41)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel dniLabel = new JLabel("DNI:");
        dniLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField dniField = new JTextField(20);
        dniField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField nombreField = new JTextField(20);
        nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel apellido1Label = new JLabel("Apellido 1:");
        apellido1Label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField apellido1Field = new JTextField(20);
        apellido1Field.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel fechaLabel = new JLabel("Fecha Nacimiento (YYYY-MM-DD):");
        fechaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField fechaField = new JTextField(20);
        fechaField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel numEmpLabel = new JLabel("Número Empleado:");
        numEmpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField numEmpField = new JTextField(20);
        numEmpField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton crearButton = new JButton("Crear Empleado", new ImageIcon("src/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);

        crearButton.addActionListener(event -> {
            try {
                String dni = dniField.getText();
                String nombre = nombreField.getText();
                String apellido1 = apellido1Field.getText();
                LocalDate fecha = LocalDate.parse(fechaField.getText());
                int numEmp = Integer.parseInt(numEmpField.getText());
                Empleado empleado = federacion.nuevoEmpleado(dni, nombre, apellido1, "Apellido2", fecha,
                        "user" + numEmp, "pass" + numEmp, "Población", numEmp, LocalDate.now(), "SS" + numEmp);
                JOptionPane.showMessageDialog(frame, "Empleado creado: " + empleado);
                clearFields(dniField, nombreField, apellido1Field, fechaField, numEmpField);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error en los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; createPanel.add(dniLabel, gbc);
        gbc.gridx = 1; createPanel.add(dniField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; createPanel.add(nombreLabel, gbc);
        gbc.gridx = 1; createPanel.add(nombreField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; createPanel.add(apellido1Label, gbc);
        gbc.gridx = 1; createPanel.add(apellido1Field, gbc);
        gbc.gridx = 0; gbc.gridy = 3; createPanel.add(fechaLabel, gbc);
        gbc.gridx = 1; createPanel.add(fechaField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; createPanel.add(numEmpLabel, gbc);
        gbc.gridx = 1; createPanel.add(numEmpField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createInstalacionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = new JPanel(new GridBagLayout());
        createPanel.setBackground(Color.WHITE);
        createPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Crear Nueva Instalación",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(33, 37, 41)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField nombreField = new JTextField(20);
        nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel direccionLabel = new JLabel("Dirección:");
        direccionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField direccionField = new JTextField(20);
        direccionField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel superficieLabel = new JLabel("Tipo de Superficie:");
        superficieLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<Instalacion.TipoSuperficie> superficieCombo = new JComboBox<>(Instalacion.TipoSuperficie.values());
        superficieCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel buscarLabel = new JLabel("Buscar por Nombre:");
        buscarLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField buscarField = new JTextField(20);
        buscarField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton crearButton = new JButton("Crear Instalación", new ImageIcon("src/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        JButton buscarButton = new JButton("Buscar Instalaciones", new ImageIcon("src/resources/iconos/magnifier.png"));
        styleButton(buscarButton, new Color(33, 37, 41), false);

        crearButton.addActionListener(event -> {
            String nombre = nombreField.getText();
            String direccion = direccionField.getText();
            String superficie = superficieCombo.getSelectedItem().toString();
            Instalacion instalacion = federacion.nuevaInstalacion(nombre, direccion, superficie);
            JOptionPane.showMessageDialog(frame, "Instalación creada: " + instalacion);
            clearFields(nombreField, direccionField);
        });

        buscarButton.addActionListener(event -> {
            String nombre = buscarField.getText();
            List<Instalacion> instalaciones = federacion.buscarInstalaciones(nombre);
            JTextArea textArea = new JTextArea(10, 30);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textArea.setText("Instalaciones encontradas:\n");
            for (Instalacion i : instalaciones) {
                textArea.append(i.toString() + "\n");
            }
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Resultados", JOptionPane.INFORMATION_MESSAGE);
        });

        gbc.gridx = 0; gbc.gridy = 0; createPanel.add(nombreLabel, gbc);
        gbc.gridx = 1; createPanel.add(nombreField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; createPanel.add(direccionLabel, gbc);
        gbc.gridx = 1; createPanel.add(direccionField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; createPanel.add(superficieLabel, gbc);
        gbc.gridx = 1; createPanel.add(superficieCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 3; createPanel.add(buscarLabel, gbc);
        gbc.gridx = 1; createPanel.add(buscarField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 5; createPanel.add(buscarButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createGrupoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = new JPanel(new GridBagLayout());
        createPanel.setBackground(Color.WHITE);
        createPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Crear Nuevo Grupo",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(33, 37, 41)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel categoriaLabel = new JLabel("Nombre Categoría:");
        categoriaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField categoriaField = new JTextField(20);
        categoriaField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel nombreLabel = new JLabel("Nombre Grupo:");
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField nombreField = new JTextField(20);
        nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton crearButton = new JButton("Crear Grupo", new ImageIcon("src/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        JButton listarButton = new JButton("Listar Grupos", new ImageIcon("src/resources/iconos/magnifier.png"));
        styleButton(listarButton, new Color(33, 37, 41), false);

        crearButton.addActionListener(event -> {
            String nombreCategoria = categoriaField.getText();
            String nombreGrupo = nombreField.getText();
            Categoria categoria = federacion.obtenerCategorias().stream()
                    .filter(c -> c.getNombre().equalsIgnoreCase(nombreCategoria))
                    .findFirst().orElse(null);
            if (categoria == null) {
                JOptionPane.showMessageDialog(frame, "Categoría no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Grupo grupo = federacion.nuevoGrupo(categoria, nombreGrupo);
            JOptionPane.showMessageDialog(frame, "Grupo creado: " + grupo);
            clearFields(categoriaField, nombreField);
        });

        listarButton.addActionListener(event -> {
            String nombreCategoria = categoriaField.getText();
            Categoria categoria = federacion.obtenerCategorias().stream()
                    .filter(c -> c.getNombre().equalsIgnoreCase(nombreCategoria))
                    .findFirst().orElse(null);
            if (categoria == null) {
                JOptionPane.showMessageDialog(frame, "Categoría no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Grupo> grupos = federacion.obtenerGrupos(categoria);
            JTextArea textArea = new JTextArea(10, 30);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textArea.setText("Grupos en " + categoria.getNombre() + ":\n");
            for (Grupo g : grupos) {
                textArea.append(g.toString() + "\n");
            }
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Lista de Grupos", JOptionPane.INFORMATION_MESSAGE);
        });

        gbc.gridx = 0; gbc.gridy = 0; createPanel.add(categoriaLabel, gbc);
        gbc.gridx = 1; createPanel.add(categoriaField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; createPanel.add(nombreLabel, gbc);
        gbc.gridx = 1; createPanel.add(nombreField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 3; createPanel.add(listarButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createEquipoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = new JPanel(new GridBagLayout());
        createPanel.setBackground(Color.WHITE);
        createPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Crear Nuevo Equipo y Buscar Jugador",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(33, 37, 41)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos para crear equipo
        JLabel letraLabel = new JLabel("Letra:");
        letraLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField letraField = new JTextField(20);
        letraField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel instalacionLabel = new JLabel("Nombre Instalación:");
        instalacionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField instalacionField = new JTextField(20);
        instalacionField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel grupoLabel = new JLabel("Nombre Grupo:");
        grupoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField grupoField = new JTextField(20);
        grupoField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel clubLabel = new JLabel("Nombre Club (opcional):");
        clubLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField clubField = new JTextField(20);
        clubField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton crearButton = new JButton("Crear Equipo", new ImageIcon("src/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);

        // Campos para buscar jugador
        JLabel buscarLetraLabel = new JLabel("Letra Equipo para Buscar:");
        buscarLetraLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField buscarLetraField = new JTextField(20);
        buscarLetraField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel dniLabel = new JLabel("DNI Jugador:");
        dniLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField dniField = new JTextField(20);
        dniField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton buscarJugadorButton = new JButton("Buscar Jugador", new ImageIcon("src/resources/iconos/magnifier.png"));
        styleButton(buscarJugadorButton, new Color(33, 37, 41), false);

        // Acción del botón "Crear Equipo" (sin cambios)
        crearButton.addActionListener(event -> {
            String letra = letraField.getText();
            String nombreInstalacion = instalacionField.getText();
            String nombreGrupo = grupoField.getText();
            String nombreClub = clubField.getText();

            Instalacion instalacion = federacion.buscarInstalaciones(nombreInstalacion).stream()
                    .findFirst().orElse(null);
            if (instalacion == null) {
                instalacion = federacion.nuevaInstalacion(nombreInstalacion, "Dirección por defecto", "CESPED_NATURAL");
            }

            Categoria cat = federacion.obtenerCategorias().stream().findFirst().orElse(null);
            if (cat == null) {
                JOptionPane.showMessageDialog(frame, "No hay categorías disponibles. Cree una primero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Grupo grupo = federacion.obtenerGrupos(cat).stream()
                    .filter(g -> g.getNombre().equalsIgnoreCase(nombreGrupo))
                    .findFirst().orElse(null);
            if (grupo == null) {
                grupo = federacion.nuevoGrupo(cat, nombreGrupo);
            }

            Equipo equipo = federacion.nuevoEquipo(letra, instalacion, grupo);

            if (!nombreClub.isEmpty()) {
                Club club = federacion.buscarClub(nombreClub);
                if (club != null) {
                    try {
                        club.addEquipo(equipo);
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(frame, "El equipo ya está en el club.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Club no encontrado, equipo creado sin club.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }

            JOptionPane.showMessageDialog(frame, "Equipo creado: " + equipo);
            clearFields(letraField, instalacionField, grupoField, clubField);
        });

        // Acción del botón "Buscar Jugador"
        buscarJugadorButton.addActionListener(event -> {
            String letra = buscarLetraField.getText();
            String dni = dniField.getText();

            // Buscar el equipo por su letra
            Equipo equipo = federacion.obtenerCategorias().stream()
                    .flatMap(c -> federacion.obtenerGrupos(c).stream())
                    .flatMap(g -> g.getEquipos().stream())
                    .filter(e -> e.getLetra().equalsIgnoreCase(letra))
                    .findFirst().orElse(null);

            if (equipo == null) {
                JOptionPane.showMessageDialog(frame, "Equipo no encontrado con la letra: " + letra, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscar el jugador en el equipo
            Persona jugador = equipo.buscarJugador(dni);
            if (jugador != null) {
                JOptionPane.showMessageDialog(frame, "Jugador encontrado: " + jugador.toString(), "Resultado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "No se encontró un jugador con DNI: " + dni + " en el equipo " + letra, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }

            clearFields(buscarLetraField, dniField);
        });

        // Posicionar los componentes en la cuadrícula
        gbc.gridx = 0; gbc.gridy = 0; createPanel.add(letraLabel, gbc);
        gbc.gridx = 1; createPanel.add(letraField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; createPanel.add(instalacionLabel, gbc);
        gbc.gridx = 1; createPanel.add(instalacionField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; createPanel.add(grupoLabel, gbc);
        gbc.gridx = 1; createPanel.add(grupoField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; createPanel.add(clubLabel, gbc);
        gbc.gridx = 1; createPanel.add(clubField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);

        // Campos para buscar jugador
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; createPanel.add(buscarLetraLabel, gbc);
        gbc.gridx = 1; createPanel.add(buscarLetraField, gbc);
        gbc.gridx = 0; gbc.gridy = 6; createPanel.add(dniLabel, gbc);
        gbc.gridx = 1; createPanel.add(dniField, gbc);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; createPanel.add(buscarJugadorButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }
    
    private JPanel createLicenciaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = new JPanel(new GridBagLayout());
        createPanel.setBackground(Color.WHITE);
        createPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Gestionar Licencias",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(33, 37, 41)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel dniLabel = new JLabel("DNI Persona:");
        dniLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField dniField = new JTextField(20);
        dniField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel equipoLabel = new JLabel("Letra Equipo (opcional):");
        equipoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField equipoField = new JTextField(20);
        equipoField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel calcularLabel = new JLabel("Letra Equipo para Precio:");
        calcularLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField calcularField = new JTextField(20);
        calcularField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton crearSimpleButton = new JButton("Crear Licencia Simple", new ImageIcon("src/resources/iconos/cross.png"));
        styleButton(crearSimpleButton, new Color(211, 47, 47), true);
        JButton crearEquipoButton = new JButton("Crear Licencia con Equipo", new ImageIcon("src/resources/iconos/cross.png"));
        styleButton(crearEquipoButton, new Color(211, 47, 47), true);
        JButton calcularButton = new JButton("Calcular Precio Licencia", new ImageIcon("src/resources/iconos/precio_licencia.png"));
        styleButton(calcularButton, new Color(33, 37, 41), false);

        crearSimpleButton.addActionListener(event -> {
            String dni = dniField.getText();
            Persona persona = federacion.buscaPersona(dni);
            if (persona == null) {
                JOptionPane.showMessageDialog(frame, "Persona no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Licencia licencia = federacion.nuevaLicencia(persona);
            JOptionPane.showMessageDialog(frame, "Licencia creada: " + licencia);
            clearFields(dniField);
        });

        crearEquipoButton.addActionListener(event -> {
            String dni = dniField.getText();
            String letraEquipo = equipoField.getText();
            Persona persona = federacion.buscaPersona(dni);
            if (persona == null) {
                JOptionPane.showMessageDialog(frame, "Persona no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Equipo equipo = federacion.obtenerCategorias().stream()
                    .flatMap(c -> federacion.obtenerGrupos(c).stream())
                    .flatMap(g -> g.getEquipos().stream())
                    .filter(e -> e.getLetra().equalsIgnoreCase(letraEquipo))
                    .findFirst().orElse(null);
            if (equipo == null) {
                JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Licencia licencia = federacion.nuevaLicencia(persona, equipo);
            federacion.addLicencia(licencia, equipo);
            JOptionPane.showMessageDialog(frame, "Licencia con equipo creada: " + licencia);
            clearFields(dniField, equipoField);
        });

        calcularButton.addActionListener(event -> {
            String letraEquipo = calcularField.getText();
            Equipo equipo = federacion.obtenerCategorias().stream()
                    .flatMap(c -> federacion.obtenerGrupos(c).stream())
                    .flatMap(g -> g.getEquipos().stream())
                    .filter(e -> e.getLetra().equalsIgnoreCase(letraEquipo))
                    .findFirst().orElse(null);
            if (equipo == null) {
                JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double precio = federacion.calcularPrecioLicencia(equipo);
            JOptionPane.showMessageDialog(frame, "Precio de la licencia: " + precio);
        });

        gbc.gridx = 0; gbc.gridy = 0; createPanel.add(dniLabel, gbc);
        gbc.gridx = 1; createPanel.add(dniField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; createPanel.add(equipoLabel, gbc);
        gbc.gridx = 1; createPanel.add(equipoField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; createPanel.add(calcularLabel, gbc);
        gbc.gridx = 1; createPanel.add(calcularField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; createPanel.add(crearSimpleButton, gbc);
        gbc.gridy = 4; createPanel.add(crearEquipoButton, gbc);
        gbc.gridy = 5; createPanel.add(calcularButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    private void styleMenu(JMenu menu, Font font, Color foreground, Color hoverColor) {
        menu.setFont(font);
        menu.setForeground(foreground);
        menu.setOpaque(true);
        menu.setBackground(new Color(33, 37, 41));
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menu.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menu.setBackground(new Color(33, 37, 41));
            }
        });
    }

    private void styleMenuItem(JMenuItem item, Font font, Color foreground, Color hoverColor) {
        item.setFont(font);
        item.setForeground(foreground);
        item.setBackground(new Color(33, 37, 41));
        item.setOpaque(true);
        item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(33, 37, 41));
            }
        });
    }

    private void styleButton(JButton button, Color bgColor, boolean isCreateButton) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hoverColor = isCreateButton ? new Color(239, 83, 80) : new Color(66, 66, 66);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp2().createAndShowGUI());
    }
}