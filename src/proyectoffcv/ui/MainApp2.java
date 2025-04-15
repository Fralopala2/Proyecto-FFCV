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
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class MainApp2 {

    private IFederacion federacion;
    private JFrame frame;
    private JPanel contentPanel;
    private JMenuBar menuBar;

    public MainApp2() {
        federacion = Federacion.getInstance();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Proyecto FFCV - Gestión Federativa");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setLocationRelativeTo(null);
        setFrameIcon("src/resources/logo.png");

        JPanel mainPanel = createGradientPanel();
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = createHeaderPanel();
        menuBar = createMenuBar();
        headerPanel.add(menuBar, BorderLayout.SOUTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.add(createWelcomePanel(), BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        frame.add(mainPanel);

        setupMenuActions();

        frame.setVisible(true);
    }

    private JPanel createGradientPanel() {
        return new JPanel(new BorderLayout()) {
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
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(166, 7, 7));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel headerLabel = new JLabel("Sistema de Gestión Federativa FFCV", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerLabel.setForeground(Color.WHITE);

        JLabel logoLabel = createLogoLabel("src/resources/logo.png");
        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        return headerPanel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(33, 37, 41));
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        Font menuFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color menuForeground = Color.WHITE;
        Color menuHover = new Color(66, 66, 66);

        JMenu gestionMenu = new JMenu("Gestión");
        styleMenu(gestionMenu, menuFont, menuForeground, menuHover);
        gestionMenu.setIcon(loadIcon("src/resources/iconos/gestion.png"));

        String[] menuItems = {"Categorías", "Clubes", "Personas", "Empleados", "Instalaciones", "Grupos", "Equipos", "Licencias"};
        String[] iconPaths = {"categorias.png", "club.png", "persona.png", "empleado.png", "instalaciones.png", "grupos.png", "teams.png", "licencia.png"};
        JMenuItem[] items = new JMenuItem[menuItems.length];

        for (int i = 0; i < menuItems.length; i++) {
            items[i] = new JMenuItem(menuItems[i], loadIcon("src/resources/iconos/" + iconPaths[i]));
            styleMenuItem(items[i], menuFont, menuForeground, menuHover);
            gestionMenu.add(items[i]);
        }

        menuBar.add(gestionMenu);
        return menuBar;
    }

    private void setupMenuActions() {
        JMenu gestionMenu = menuBar.getMenu(0);
        gestionMenu.getItem(0).addActionListener(e -> switchPanel(createCategoriaPanel()));
        gestionMenu.getItem(1).addActionListener(e -> switchPanel(createClubPanel()));
        gestionMenu.getItem(2).addActionListener(e -> switchPanel(createPersonaPanel()));
        gestionMenu.getItem(3).addActionListener(e -> switchPanel(createEmpleadoPanel()));
        gestionMenu.getItem(4).addActionListener(e -> switchPanel(createInstalacionPanel()));
        gestionMenu.getItem(5).addActionListener(e -> switchPanel(createGrupoPanel()));
        gestionMenu.getItem(6).addActionListener(e -> switchPanel(createEquipoPanel()));
        gestionMenu.getItem(7).addActionListener(e -> switchPanel(createLicenciaPanel()));
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

        JPanel createPanel = createTitledPanel("Crear Nueva Categoría");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 0);
        JTextField ordenField = addField(createPanel, gbc, "Nivel/Orden:", 1);
        JTextField precioField = addField(createPanel, gbc, "Precio Licencia:", 2);

        JButton crearButton = new JButton("Crear Categoría", loadIcon("src/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        crearButton.setToolTipText("Crea una nueva categoría con los datos ingresados");
        crearButton.addActionListener(event -> {
            if (!validateFields(nombreField, ordenField, precioField)) return;
            try {
                Categoria categoria = federacion.nuevaCategoria(nombreField.getText(), Integer.parseInt(ordenField.getText()), Double.parseDouble(precioField.getText()));
                JOptionPane.showMessageDialog(frame, "Categoría creada: " + categoria);
                clearFields(nombreField, ordenField, precioField);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Nivel y precio deben ser numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al guardar categoría: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);

        JPanel listPanel = createTitledPanel("Consultar Categorías", new BorderLayout());
        JButton listarButton = new JButton("Listar Categorías", loadIcon("src/resources/iconos/magnifier.png"));
        styleButton(listarButton, new Color(33, 37, 41), false);
        listarButton.setToolTipText("Muestra todas las categorías registradas");
        listarButton.addActionListener(event -> listCategorias(listPanel));
        listPanel.add(listarButton, BorderLayout.NORTH);

        panel.add(createPanel, BorderLayout.NORTH);
        panel.add(listPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createClubPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = createTitledPanel("Gestión de Clubes");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nombreField = addField(createPanel, gbc, "Nombre Club:", 0);
        JTextField fechaField = addField(createPanel, gbc, "Fecha Fundación (YYYY-MM-DD):", 1);
        JTextField dniPresField = addField(createPanel, gbc, "DNI Presidente:", 2);
        JTextField buscarField = addField(createPanel, gbc, "Buscar Club por Nombre:", 3);

        JButton crearClubButton = new JButton("Crear Club", loadIcon("src/resources/iconos/cross.png"));
        styleButton(crearClubButton, new Color(211, 47, 47), true);
        crearClubButton.setToolTipText("Crea un nuevo club con los datos ingresados");
        JButton buscarButton = new JButton("Buscar Club", loadIcon("src/resources/iconos/magnifier.png"));
        styleButton(buscarButton, new Color(33, 37, 41), false);
        buscarButton.setToolTipText("Busca un club por nombre");

        crearClubButton.addActionListener(event -> {
            if (!validateFields(nombreField, fechaField, dniPresField)) return;
            try {
                LocalDate fecha = LocalDate.parse(fechaField.getText());
                Persona presidente = federacion.buscaPersona(dniPresField.getText());
                if (presidente == null) {
                    JOptionPane.showMessageDialog(frame, "Presidente no encontrado. Regístrelo primero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Club club = federacion.nuevoClub(nombreField.getText(), fecha, presidente);
                JOptionPane.showMessageDialog(frame, "Club creado: " + club);
                clearFields(nombreField, fechaField, dniPresField);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al guardar club: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buscarButton.addActionListener(event -> {
            if (!validateFields(buscarField)) return;
            Club club = federacion.buscarClub(buscarField.getText());
            JOptionPane.showMessageDialog(frame, club != null ? "Club encontrado: " + club : "Club no encontrado.");
        });

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; createPanel.add(crearClubButton, gbc);
        gbc.gridy = 5; createPanel.add(buscarButton, gbc);

        JPanel listPanel = createTitledPanel("Listar Clubes", new BorderLayout());
        JButton listarButton = new JButton("Listar Clubes", loadIcon("src/resources/iconos/magnifier.png"));
        styleButton(listarButton, new Color(33, 37, 41), false);
        listarButton.setToolTipText("Muestra todos los clubes registrados");
        listarButton.addActionListener(event -> listClubes(listPanel));
        listPanel.add(listarButton, BorderLayout.NORTH);

        panel.add(createPanel, BorderLayout.NORTH);
        panel.add(listPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPersonaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = createTitledPanel("Crear Nueva Persona");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField dniField = addField(createPanel, gbc, "DNI:", 0);
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 1);
        JTextField apellido1Field = addField(createPanel, gbc, "Apellido 1:", 2);
        JTextField apellido2Field = addField(createPanel, gbc, "Apellido 2:", 3);
        JTextField fechaField = addField(createPanel, gbc, "Fecha Nacimiento (YYYY-MM-DD):", 4);
        JTextField poblacionField = addField(createPanel, gbc, "Población:", 5);
        JTextField usuarioField = addField(createPanel, gbc, "Usuario:", 6);
        JTextField passwordField = addField(createPanel, gbc, "Contraseña:", 7);
        JTextField buscarDniField = addField(createPanel, gbc, "Buscar por DNI:", 8);

        JButton crearButton = new JButton("Crear Persona", loadIcon("src/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        crearButton.setToolTipText("Crea una nueva persona con los datos ingresados");
        JButton buscarButton = new JButton("Buscar Persona", loadIcon("src/resources/iconos/magnifier.png"));
        styleButton(buscarButton, new Color(33, 37, 41), false);
        buscarButton.setToolTipText("Busca una persona por DNI");
        JButton buscarMultiButton = new JButton("Buscar por Nombre y Apellidos", loadIcon("src/resources/iconos/magnifier.png"));
        styleButton(buscarMultiButton, new Color(33, 37, 41), false);
        buscarMultiButton.setToolTipText("Busca personas por nombre y apellidos");

        crearButton.addActionListener(event -> {
            if (!validateFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, poblacionField, usuarioField, passwordField)) return;
            try {
                Persona persona = federacion.nuevaPersona(dniField.getText(), nombreField.getText(), apellido1Field.getText(), 
                        apellido2Field.getText(), LocalDate.parse(fechaField.getText()), usuarioField.getText(), 
                        passwordField.getText(), poblacionField.getText());
                if (persona != null) {
                    JOptionPane.showMessageDialog(frame, "Persona creada: " + persona);
                    clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, poblacionField, usuarioField, passwordField);
                } else {
                    JOptionPane.showMessageDialog(frame, "No se pudo crear la persona. Verifique el DNI.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al guardar persona: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buscarButton.addActionListener(event -> {
            if (!validateFields(buscarDniField)) return;
            Persona persona = federacion.buscaPersona(buscarDniField.getText());
            JOptionPane.showMessageDialog(frame, persona != null ? "Persona encontrada: " + persona : "Persona no encontrada.");
        });

        buscarMultiButton.addActionListener(event -> {
            if (!validateFields(nombreField, apellido1Field, apellido2Field)) return;
            List<Persona> personas = federacion.buscaPersonas(nombreField.getText(), apellido1Field.getText(), apellido2Field.getText());
            showListResult(personas, "Personas encontradas:");
        });

        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 10; createPanel.add(buscarButton, gbc);
        gbc.gridy = 11; createPanel.add(buscarMultiButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createEmpleadoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = createTitledPanel("Crear Nuevo Empleado");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField dniField = addField(createPanel, gbc, "DNI:", 0);
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 1);
        JTextField apellido1Field = addField(createPanel, gbc, "Apellido 1:", 2);
        JTextField apellido2Field = addField(createPanel, gbc, "Apellido 2:", 3);
        JTextField fechaField = addField(createPanel, gbc, "Fecha Nacimiento (YYYY-MM-DD):", 4);
        JTextField numEmpField = addField(createPanel, gbc, "Número Empleado:", 5);
        JTextField inicioContratoField = addField(createPanel, gbc, "Inicio Contrato (YYYY-MM-DD):", 6);
        JTextField segSocialField = addField(createPanel, gbc, "Seguridad Social:", 7);

        JButton crearButton = new JButton("Crear Empleado", loadIcon("src/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        crearButton.setToolTipText("Crea un nuevo empleado con los datos ingresados");

        crearButton.addActionListener(event -> {
            if (!validateFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, numEmpField, inicioContratoField, segSocialField)) return;
            try {
                Empleado empleado = federacion.nuevoEmpleado(dniField.getText(), nombreField.getText(), apellido1Field.getText(), 
                        apellido2Field.getText(), LocalDate.parse(fechaField.getText()), "user" + dniField.getText(), 
                        "pass" + dniField.getText(), "Población", Integer.parseInt(numEmpField.getText()), 
                        LocalDate.parse(inicioContratoField.getText()), segSocialField.getText());
                if (empleado != null) {
                    JOptionPane.showMessageDialog(frame, "Empleado creado: " + empleado);
                    clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, numEmpField, inicioContratoField, segSocialField);
                } else {
                    JOptionPane.showMessageDialog(frame, "No se pudo crear el empleado. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Número de empleado debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al guardar empleado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createInstalacionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = createTitledPanel("Crear Nueva Instalación");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 0);
        JTextField direccionField = addField(createPanel, gbc, "Dirección:", 1);
        JLabel superficieLabel = new JLabel("Tipo de Superficie:");
        superficieLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<Instalacion.TipoSuperficie> superficieCombo = new JComboBox<>(Instalacion.TipoSuperficie.values());
        superficieCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 2; createPanel.add(superficieLabel, gbc);
        gbc.gridx = 1; createPanel.add(superficieCombo, gbc);
        JTextField buscarField = addField(createPanel, gbc, "Buscar por Nombre:", 3);

        JButton crearButton = new JButton("Crear Instalación", loadIcon("src/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        crearButton.setToolTipText("Crea una nueva instalación con los datos ingresados");
        JButton buscarButton = new JButton("Buscar Instalaciones", loadIcon("src/resources/iconos/magnifier.png"));
        styleButton(buscarButton, new Color(33, 37, 41), false);
        buscarButton.setToolTipText("Busca instalaciones por nombre parcial");

        crearButton.addActionListener(event -> {
            if (!validateFields(nombreField, direccionField)) return;
            try {
                Instalacion instalacion = federacion.nuevaInstalacion(nombreField.getText(), direccionField.getText(), 
                        ((Instalacion.TipoSuperficie) superficieCombo.getSelectedItem()).name());
                JOptionPane.showMessageDialog(frame, "Instalación creada: " + instalacion);
                clearFields(nombreField, direccionField);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al guardar instalación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buscarButton.addActionListener(event -> {
            if (!validateFields(buscarField)) return;
            List<Instalacion> instalaciones = federacion.buscarInstalaciones(buscarField.getText());
            showListResult(instalaciones, "Instalaciones encontradas:");
        });

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 5; createPanel.add(buscarButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createGrupoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = createTitledPanel("Crear Nuevo Grupo");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField categoriaField = addField(createPanel, gbc, "Nombre Categoría:", 0);
        JTextField nombreField = addField(createPanel, gbc, "Nombre Grupo:", 1);

        JButton crearButton = new JButton("Crear Grupo", loadIcon("src/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        crearButton.setToolTipText("Crea un nuevo grupo con los datos ingresados");
        JButton listarButton = new JButton("Listar Grupos", loadIcon("src/resources/iconos/magnifier.png"));
        styleButton(listarButton, new Color(33, 37, 41), false);
        listarButton.setToolTipText("Lista los grupos de la categoría especificada");

        crearButton.addActionListener(event -> {
            if (!validateFields(categoriaField, nombreField)) return;
            try {
                Categoria categoria = Categoria.buscarPorNombre(categoriaField.getText());
                if (categoria == null) {
                    JOptionPane.showMessageDialog(frame, "Categoría no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Grupo grupo = federacion.nuevoGrupo(categoria, nombreField.getText());
                JOptionPane.showMessageDialog(frame, "Grupo creado: " + grupo);
                clearFields(categoriaField, nombreField);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al guardar grupo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        listarButton.addActionListener(event -> {
            if (!validateFields(categoriaField)) return;
            try {
                Categoria categoria = Categoria.buscarPorNombre(categoriaField.getText());
                if (categoria == null) {
                    JOptionPane.showMessageDialog(frame, "Categoría no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<Grupo> grupos = federacion.obtenerGrupos(categoria);
                showListResult(grupos, "Grupos en " + categoria.getNombre() + ":");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al listar grupos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 3; createPanel.add(listarButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createEquipoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = createTitledPanel("Crear Nuevo Equipo y Buscar Jugador");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField letraField = addField(createPanel, gbc, "Letra:", 0);
        JTextField instalacionField = addField(createPanel, gbc, "Nombre Instalación:", 1);
        JTextField grupoField = addField(createPanel, gbc, "Nombre Grupo:", 2);
        JTextField clubField = addField(createPanel, gbc, "Nombre Club:", 3);

        JButton crearButton = new JButton("Crear Equipo", loadIcon("src/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        crearButton.setToolTipText("Crea un nuevo equipo con los datos ingresados");

        JTextField buscarLetraField = addField(createPanel, gbc, "Letra Equipo para Buscar:", 5);
        JTextField dniField = addField(createPanel, gbc, "DNI Jugador:", 6);

        JButton buscarJugadorButton = new JButton("Buscar Jugador", loadIcon("src/resources/iconos/magnifier.png"));
        styleButton(buscarJugadorButton, new Color(33, 37, 41), false);
        buscarJugadorButton.setToolTipText("Busca un jugador en el equipo por DNI");

//        crearButton.addActionListener(event -> {
//            if (!validateFields(letraField, instalacionField, grupoField, clubField)) return;
//            try {
//                Instalacion instalacion = Instalacion.buscarPorNombre(instalacionField.getText());
//                if (instalacion == null) {
//                    JOptionPane.showMessageDialog(frame, "Instalación no encontrada. Cree una primero.", "Error", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                Grupo grupo = Grupo.buscarPorNombre(grupoField.getText());
//                if (grupo == null) {
//                    JOptionPane.showMessageDialog(frame, "Grupo no encontrado. Cree uno primero.", "Error", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                Club club = federacion.buscarClub(clubField.getText());
//                if (club == null) {
//                    JOptionPane.showMessageDialog(frame, "Club no encontrado. Cree uno primero.", "Error", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                Equipo equipo = federacion.nuevoEquipo(letraField.getText(), instalacion, grupo, club);
//                JOptionPane.showMessageDialog(frame, "Equipo creado: " + equipo);
//                clearFields(letraField, instalacionField, grupoField, clubField);
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(frame, "Error al guardar equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        });

        buscarJugadorButton.addActionListener(event -> {
            if (!validateFields(buscarLetraField, dniField)) return;
            try {
                Equipo equipo = Equipo.buscarPorLetra(buscarLetraField.getText());
                if (equipo == null) {
                    JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Persona jugador = equipo.buscarJugador(dniField.getText());
                JOptionPane.showMessageDialog(frame, jugador != null ? "Jugador encontrado: " + jugador : "Jugador no encontrado en el equipo.");
                clearFields(buscarLetraField, dniField);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error al buscar jugador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 7; createPanel.add(buscarJugadorButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createLicenciaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = createTitledPanel("Gestionar Licencias");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField dniField = addField(createPanel, gbc, "DNI Persona:", 0);
        JTextField equipoField = addField(createPanel, gbc, "Letra Equipo:", 1);
        JTextField calcularField = addField(createPanel, gbc, "Letra Equipo para Precio:", 2);

        JButton crearSimpleButton = new JButton("Crear Licencia Simple", loadIcon("src/resources/iconos/cross.png"));
        styleButton(crearSimpleButton, new Color(211, 47, 47), true);
        crearSimpleButton.setToolTipText("Crea una licencia simple para una persona");
        JButton crearEquipoButton = new JButton("Crear Licencia con Equipo", loadIcon("src/resources/iconos/cross.png"));
        styleButton(crearEquipoButton, new Color(211, 47, 47), true);
        crearEquipoButton.setToolTipText("Crea una licencia y asigna un equipo");
        JButton calcularButton = new JButton("Calcular Precio Licencia", loadIcon("src/resources/iconos/precio_licencia.png"));
        styleButton(calcularButton, new Color(33, 37, 41), false);
        calcularButton.setToolTipText("Calcula el precio de la licencia para un equipo");

        crearSimpleButton.addActionListener(event -> {
            if (!validateFields(dniField)) return;
            try {
                Persona persona = federacion.buscaPersona(dniField.getText());
                if (persona == null) {
                    JOptionPane.showMessageDialog(frame, "Persona no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Licencia licencia = federacion.nuevaLicencia(persona);
                JOptionPane.showMessageDialog(frame, "Licencia creada: " + licencia);
                clearFields(dniField);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al guardar licencia: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        crearEquipoButton.addActionListener(event -> {
            if (!validateFields(dniField, equipoField)) return;
            try {
                Persona persona = federacion.buscaPersona(dniField.getText());
                Equipo equipo = Equipo.buscarPorLetra(equipoField.getText());
                if (persona == null || equipo == null) {
                    JOptionPane.showMessageDialog(frame, persona == null ? "Persona no encontrada." : "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Licencia licencia = federacion.nuevaLicencia(persona, equipo);
                JOptionPane.showMessageDialog(frame, "Licencia con equipo creada: " + licencia);
                clearFields(dniField, equipoField);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al gestionar licencia: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        calcularButton.addActionListener(event -> {
            if (!validateFields(calcularField)) return;
            try {
                Equipo equipo = Equipo.buscarPorLetra(calcularField.getText());
                if (equipo == null) {
                    JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double precio = federacion.calcularPrecioLicencia(equipo);
                JOptionPane.showMessageDialog(frame, "Precio de la licencia: " + precio);
                clearFields(calcularField);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al calcular precio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; createPanel.add(crearSimpleButton, gbc);
        gbc.gridy = 4; createPanel.add(crearEquipoButton, gbc);
        gbc.gridy = 5; createPanel.add(calcularButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    private void setFrameIcon(String path) {
        ImageIcon icon = loadIcon(path);
        if (icon != null) frame.setIconImage(icon.getImage());
    }

    private JLabel createLogoLabel(String path) {
        ImageIcon icon = loadIcon(path);
        if (icon != null) {
            Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(scaledImage));
        }
        return new JLabel();
    }

    private ImageIcon loadIcon(String path) {
        try {
            return new ImageIcon(path);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + path);
            return null;
        }
    }

    private JPanel createTitledPanel(String title, LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                title, TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), new Color(33, 37, 41)));
        return panel;
    }

    private JPanel createTitledPanel(String title) {
        return createTitledPanel(title, new GridBagLayout());
    }

    private JTextField addField(JPanel panel, GridBagConstraints gbc, String labelText, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = row; panel.add(label, gbc);
        gbc.gridx = 1; panel.add(field, gbc);
        return field;
    }

    private boolean validateFields(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Todos los campos deben estar llenos.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void listCategorias(JPanel listPanel) {
        List<Categoria> categorias = federacion.obtenerCategorias();
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setText("Categorías:\n");
        for (Categoria c : categorias) {
            textArea.append(c.toString() + "\n");
        }
        textArea.setEditable(false);
        listPanel.removeAll();
        listPanel.add(new JButton("Listar Categorías", loadIcon("src/resources/iconos/magnifier.png")), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        listPanel.revalidate();
        listPanel.repaint();
    }

    private void listClubes(JPanel listPanel) {
        try {
            List<Club> clubes = Club.obtenerTodos();
            JTextArea textArea = new JTextArea(10, 30);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textArea.setText("Clubes:\n");
            for (Club c : clubes) {
                textArea.append(c.toString() + "\n");
            }
            textArea.setEditable(false);
            listPanel.removeAll();
            listPanel.add(new JButton("Listar Clubes", loadIcon("src/resources/iconos/magnifier.png")), BorderLayout.NORTH);
            listPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
            listPanel.revalidate();
            listPanel.repaint();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error al listar clubes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showListResult(List<?> list, String title) {
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setText(title + "\n");
        for (Object item : list) {
            textArea.append(item.toString() + "\n");
        }
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Resultados", JOptionPane.INFORMATION_MESSAGE);
    }

    private void styleMenu(JMenu menu, Font font, Color foreground, Color hoverColor) {
        menu.setFont(font);
        menu.setForeground(foreground);
        menu.setOpaque(true);
        menu.setBackground(new Color(33, 37, 41));
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { menu.setBackground(hoverColor); }
            @Override
            public void mouseExited(MouseEvent e) { menu.setBackground(new Color(33, 37, 41)); }
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
            public void mouseEntered(MouseEvent e) { item.setBackground(hoverColor); }
            @Override
            public void mouseExited(MouseEvent e) { item.setBackground(new Color(33, 37, 41)); }
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
            public void mouseEntered(MouseEvent e) { button.setBackground(hoverColor); }
            @Override
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
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