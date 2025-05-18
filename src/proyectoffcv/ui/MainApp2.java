package proyectoffcv.ui;

import entidades.*;
import java.sql.*;
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
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import proyectoffcv.util.DatabaseConnection;

public class MainApp2 {

    private IFederacion federacion; // Interfaz para la logica de la federacion
    private JFrame frame; // Ventana principal de la aplicacion
    private JPanel contentPanel; // Panel que contiene el contenido de la ventana
    private JMenuBar menuBar; // Barra de menu de la aplicacion
    private JTextArea errorArea; // Area de texto para mostrar errores
    private JScrollPane errorScrollPane; // Scroll para el area de errores

    public MainApp2() {
        federacion = Federacion.getInstance(); // Inicializa la instancia de la federacion
        errorArea = new JTextArea(3, 30); // 3 filas, 30 columnas
        errorArea.setEditable(false); // No editable
        errorArea.setForeground(Color.WHITE); // Texto blanco
        errorArea.setBackground(Color.RED); // Fondo rojo para errores
        errorArea.setLineWrap(true); // Ajuste de linea
        errorArea.setWrapStyleWord(true); // Ajuste por palabras
        errorScrollPane = new JScrollPane(errorArea); // Anade scroll al area de errores
    }

    // Metodo para crear y mostrar la interfaz grafica
    private void createAndShowGUI() {
        frame = new JFrame("Proyecto FFCV - Gestion Federativa"); // Titulo de la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicacion al cerrar la ventana
        frame.setSize(1000, 750); // Tamano de la ventana
        frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setFrameIcon("/resources/logo.png"); // Establece el icono de la ventana

        JPanel mainPanel = createGradientPanel(); // Crea el panel principal con un fondo degradado
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Establece un borde vacio alrededor del panel

        JPanel headerPanel = createHeaderPanel(); // Crea el panel de encabezado
        menuBar = createMenuBar(); // Crea la barra de menu
        headerPanel.add(menuBar, BorderLayout.SOUTH); // Anade la barra de menu al panel de encabezado

        contentPanel = new JPanel(new BorderLayout()); // Crea el panel de contenido
        contentPanel.setBackground(Color.WHITE); // Establece el color de fondo del panel de contenido
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Establece un borde vacio alrededor del panel de contenido
        contentPanel.add(createWelcomePanel(), BorderLayout.CENTER); // Anade el panel de bienvenida al panel de contenido

        mainPanel.add(headerPanel, BorderLayout.NORTH); // Anade el panel de encabezado al panel principal
        mainPanel.add(contentPanel, BorderLayout.CENTER); // Anade el panel de contenido al panel principal
        frame.add(mainPanel); // Anade el panel principal a la ventana

        setupMenuActions(); // Configura las acciones de los elementos del menu

        frame.setVisible(true); // Hace visible la ventana
    }

    // Metodo para crear un panel con fondo degradado
    private JPanel createGradientPanel() {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(245, 247, 250); // Color superior del degradado
                Color color2 = new Color(200, 230, 201); // Color inferior del degradado
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2); // Crea el degradado
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h); // Rellena el panel con el degradado
            }
        };
    }

    // Metodo para crear el panel de encabezado
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()); // Panel de encabezado con layout de BorderLayout
        headerPanel.setBackground(new Color(166, 7, 7)); // Establece el color de fondo del encabezado
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20)); // Establece un borde vacio alrededor del encabezado

        JLabel headerLabel = createStyledLabel("Sistema de Gestion Federativa FFCV"); // Etiqueta del encabezado
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centra el texto
        headerLabel.setForeground(Color.WHITE); // Establece el color del texto

        JLabel logoLabel = createLogoLabel("/resources/logo.png"); // Crea la etiqueta del logo
        headerPanel.add(logoLabel, BorderLayout.WEST); // Anade el logo al lado izquierdo del encabezado
        headerPanel.add(headerLabel, BorderLayout.CENTER); // Anade la etiqueta del encabezado al centro
        return headerPanel; // Devuelve el panel de encabezado
    }

    // Metodo para crear la barra de menu
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar(); // Crea la barra de menu
        menuBar.setBackground(new Color(245, 245, 245)); // Establece el color de fondo de la barra de menu
        menuBar.setOpaque(true); // Modo opaco para que se vea el rojo mas claro del fondo del menu gestion
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Establece un borde vacio alrededor de la barra de menu
        // Forzar el repintado del fondo
        menuBar.setUI(new javax.swing.plaf.basic.BasicMenuBarUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                g.setColor(new Color(230, 230, 230)); // Color blanco-gris
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        });

        Font menuFont = new Font("Segoe UI", Font.PLAIN, 16); // Fuente para los elementos del menu
        Color menuForeground = Color.WHITE; // Color del texto de los elementos del menu
        Color menuHover = new Color(66, 66, 66); // Color de fondo al pasar el raton sobre los elementos del menu

        JMenu gestionMenu = new JMenu("Gestion"); // Crea el menu de gestion
        styleMenu(gestionMenu, menuFont, menuForeground, menuHover); // Estiliza el menu
        gestionMenu.setIcon(loadIcon("/resources/iconos/gestion.png")); // Establece el icono del menu

        // Nombres de los elementos del menu
        String[] menuItems = {"Personas", "Empleados", "Categorias", "Instalaciones", "Clubes", "Grupos", "Equipos", "Licencias"};
        // Rutas de los iconos de los elementos del menu
        String[] iconPaths = {"persona.png", "empleado.png", "categorias.png", "instalaciones.png", "club.png", "grupos.png", "teams.png", "licencia.png"};
        JMenuItem[] items = new JMenuItem[menuItems.length]; // Array para almacenar los elementos del menu

        // Crea y anade los elementos del menu
        for (int i = 0; i < menuItems.length; i++) {
            items[i] = new JMenuItem(menuItems[i], loadIcon("/resources/iconos/" + iconPaths[i])); // Crea un nuevo elemento del menu
            styleMenuItem(items[i], menuFont, menuForeground, menuHover); // Estiliza el elemento del menu
            gestionMenu.add(items[i]); // Anade el elemento al menu de gestion
        }

        menuBar.add(gestionMenu); // Anade el menu de gestion a la barra de menu
        return menuBar; // Devuelve la barra de menu
    }

    // Metodo para configurar las acciones de los elementos del menu
    private void setupMenuActions() {
        JMenu gestionMenu = menuBar.getMenu(0); // Obtiene el primer menu de la barra de menu
        gestionMenu.getItem(2).addActionListener(e -> switchPanel(createCategoriaPanel())); // Accion para el item "Categorias"
        gestionMenu.getItem(4).addActionListener(e -> switchPanel(createClubPanel())); // Accion para el item "Clubes"
        gestionMenu.getItem(0).addActionListener(e -> switchPanel(createPersonaPanel())); // Accion para el item "Personas"
        gestionMenu.getItem(1).addActionListener(e -> switchPanel(createEmpleadoPanel())); // Accion para el item "Empleados"
        gestionMenu.getItem(3).addActionListener(e -> switchPanel(createInstalacionPanel())); // Accion para el item "Instalaciones"
        gestionMenu.getItem(5).addActionListener(e -> switchPanel(createGrupoPanel())); // Accion para el item "Grupos"
        gestionMenu.getItem(6).addActionListener(e -> switchPanel(createEquipoPanel())); // Accion para el item "Equipos"
        gestionMenu.getItem(7).addActionListener(e -> switchPanel(createLicenciaPanel())); // Accion para el item "Licencias"
    }

    // Metodo para cambiar el panel visible en el panel de contenido
    private void switchPanel(JPanel panel) {
        contentPanel.removeAll(); // Elimina todos los componentes del panel de contenido
        contentPanel.add(panel, BorderLayout.CENTER); // Anade el nuevo panel al centro del panel de contenido
        contentPanel.revalidate(); // Revalida el panel de contenido
        contentPanel.repaint(); // Repinta el panel de contenido
    }

    // Metodo para crear el panel de bienvenida
    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout()); // Crea el panel de bienvenida
        welcomePanel.setBackground(Color.WHITE); // Establece el color de fondo del panel de bienvenida

        JLabel welcomeLabel = createStyledLabel("Bienvenido al Sistema de Gestion FFCV"); // Etiqueta de bienvenida
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centra el texto

        JLabel infoLabel = new JLabel("<html><center>Seleccione una opcion del menu 'Gestion' para comenzar.<br>"
                + "Gestione categorias, clubes, personas, equipos y mas.</center></html>", SwingConstants.CENTER); // Etiqueta de informacion
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Establece la fuente de la etiqueta
        infoLabel.setForeground(new Color(66, 66, 66)); // Establece el color del texto de la etiqueta

        welcomePanel.add(welcomeLabel, BorderLayout.NORTH); // Anade la etiqueta de bienvenida al norte del panel
        welcomePanel.add(infoLabel, BorderLayout.CENTER); // Anade la etiqueta de informacion al centro del panel
        return welcomePanel; // Devuelve el panel de bienvenida
    }

    // Metodo para crear el panel de personas
    private JPanel createPersonaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel de personas
        panel.setBackground(Color.WHITE); // Establece el color de fondo del panel
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Establece un borde vacio alrededor del panel

        JPanel createPanel = createTitledPanel("Crear/Actualizar Persona"); // Crea el panel para crear/actualizar personas
        GridBagConstraints gbc = new GridBagConstraints(); // Configuracion de la cuadricula
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena el espacio horizontalmente
        gbc.weightx = 1.0;

        // Campos para ingresar datos de la persona
        JTextField dniField = addField(createPanel, gbc, "DNI:", 0); // Campo para el DNI
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 1); // Campo para el nombre
        JTextField apellido1Field = addField(createPanel, gbc, "Primer Apellido:", 2); // Campo para el primer apellido
        JTextField apellido2Field = addField(createPanel, gbc, "Segundo Apellido:", 3); // Campo para el segundo apellido
        JTextField fechaNacimientoField = addField(createPanel, gbc, "Fecha Nacimiento (YYYY-MM-DD):", 4); // Campo para la fecha de nacimiento
        JTextField usuarioField = addField(createPanel, gbc, "Usuario:", 5); // Campo para el usuario
        JTextField passwordField = addField(createPanel, gbc, "Contrasena:", 6); // Campo para la contrasena
        JTextField poblacionField = addField(createPanel, gbc, "Poblacion:", 7); // Campo para la poblacion

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Panel para los botones
        buttonPanel.setBackground(Color.WHITE); // Establece el color de fondo del panel de botones

        // Botones para crear, actualizar, eliminar y buscar personas
        JButton crearButton = new JButton("Crear Persona", loadIcon("/resources/iconos/cross.png")); // Boton para crear persona
        styleButton(crearButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Persona", loadIcon("/resources/iconos/edit.png")); // Boton para actualizar persona
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Persona", loadIcon("/resources/iconos/delete.png")); // Boton para eliminar persona
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton buscarButton = new JButton("Buscar Persona", loadIcon("/resources/iconos/magnifier.png")); // Boton para buscar persona
        styleButton(buscarButton, new Color(33, 37, 41), false); // Estiliza el boton

        // Anade los botones al panel de botones
        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(buscarButton);

        // Accion para crear una nueva persona
        crearButton.addActionListener(event -> {
            if (!validateFields(dniField, nombreField, apellido1Field, fechaNacimientoField, usuarioField, passwordField, poblacionField)) {
                showError("Todos los campos obligatorios deben estar completos.");
                System.out.println("Validation failed: Missing fields");
                return;
            }
            try {
                String dni = dniField.getText().trim();
                System.out.println("DNI entered: " + dni);
                if (!validateDni(dni)) {
                    showError("DNI inválido. Debe tener 8 dígitos y 1 letra.");
                    System.out.println("Validation failed: Invalid DNI");
                    return;
                }
                if (federacion.buscaPersona(dni) != null) {
                    showError("El DNI ya está registrado.");
                    System.out.println("Validation failed: DNI already exists");
                    return;
                }
                LocalDate fechaNacimiento;
                try {
                    fechaNacimiento = LocalDate.parse(fechaNacimientoField.getText().trim());
                    System.out.println("Fecha Nacimiento parsed: " + fechaNacimiento);
                } catch (DateTimeParseException ex) {
                    showError("Fecha de nacimiento inválida. Use formato YYYY-MM-DD.");
                    System.out.println("Validation failed: Invalid date format");
                    return;
                }
                Persona persona = federacion.nuevaPersona(
                    dni,
                    nombreField.getText().trim(),
                    apellido1Field.getText().trim(),
                    apellido2Field.getText().trim().isEmpty() ? null : apellido2Field.getText().trim(),
                    fechaNacimiento,
                    usuarioField.getText().trim(),
                    passwordField.getText().trim(),
                    poblacionField.getText().trim()
                );
                System.out.println("Attempting to save persona: " + dni);
                persona.guardar();
                System.out.println("Persona saved successfully: " + dni);
                JOptionPane.showMessageDialog(frame, "Persona creada con éxito: " + persona.getNombre(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaNacimientoField, usuarioField, passwordField, poblacionField);
            } catch (SQLException ex) {
                handleError(ex, "Error al guardar persona en la base de datos: " + ex.getMessage());
                System.out.println("SQLException: " + ex.getMessage());
            } catch (Exception ex) {
                handleError(ex, "Error inesperado al crear persona: " + ex.getMessage());
                System.out.println("Unexpected error: " + ex.getMessage());
            }
        });

        // Accion para actualizar una persona existente
        actualizarButton.addActionListener(event -> {
            if (!validateFields(dniField, nombreField, apellido1Field, fechaNacimientoField, usuarioField, passwordField, poblacionField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                Persona persona = federacion.buscaPersona(dniField.getText()); // Busca la persona por DNI
                if (persona == null) {
                    showError("Persona no encontrada.");
                    return;
                }
                LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoField.getText()); // Convierte la fecha a LocalDate
                persona.setNombre(nombreField.getText()); // Actualiza el nombre
                persona.setApellido1(apellido1Field.getText()); // Actualiza el primer apellido
                persona.setApellido2(apellido2Field.getText()); // Actualiza el segundo apellido
                persona.setFechaNacimiento(fechaNacimiento); // Actualiza la fecha de nacimiento
                persona.setUsuario(usuarioField.getText()); // Actualiza el usuario
                persona.setPassword(passwordField.getText()); // Actualiza la contrasena
                persona.setPoblacion(poblacionField.getText()); // Actualiza la poblacion
                persona.actualizar(); // Guarda los cambios en la base de datos
                JOptionPane.showMessageDialog(frame, "Persona actualizada con exito: " + persona.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE); // Muestra mensaje de exito
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaNacimientoField, usuarioField, passwordField, poblacionField); // Limpia los campos
            } catch (DateTimeParseException ex) {
                showError("Fecha de nacimiento invalida. Use formato YYYY-MM-DD.");
            } catch (SQLException ex) {
                handleError(ex, "Error al actualizar persona en la base de datos.");
            } catch (Exception ex) {
                handleError(ex, "Error al actualizar persona.");
            }
        });

        // Accion para eliminar una persona
        eliminarButton.addActionListener(event -> {
            if (!validateFields(dniField)) {
                return; // Valida que el campo no este vacio
            }
            try {
                Persona persona = federacion.buscaPersona(dniField.getText()); // Busca la persona por DNI
                if (persona == null) {
                    showError("Persona no encontrada.");
                    return;
                }
                persona.eliminar(); // Elimina la persona de la base de datos
                JOptionPane.showMessageDialog(frame, "Persona eliminada con exito: " + persona.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE); // Muestra mensaje de exito
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaNacimientoField, usuarioField, passwordField, poblacionField); // Limpia los campos
            } catch (SQLException ex) {
                handleError(ex, "Error al eliminar persona de la base de datos.");
            } catch (Exception ex) {
                handleError(ex, "Error al eliminar persona.");
            }
        });

        // Accion para buscar personas
        buscarButton.addActionListener(event -> {
            System.out.println("Buscar Persona button clicked");
            System.out.println("Nombre: " + nombreField.getText());
            System.out.println("Apellido1: " + apellido1Field.getText());
            System.out.println("Apellido2: " + apellido2Field.getText());
            if (!validateFields(nombreField, apellido1Field)) {
                showError("Nombre y primer apellido son obligatorios.");
                System.out.println("Validation failed: Missing fields");
                return;
            }
            try {
                List<Persona> personas = federacion.buscaPersonas(
                    nombreField.getText().trim(),
                    apellido1Field.getText().trim(),
                    apellido2Field.getText().trim().isEmpty() ? null : apellido2Field.getText().trim()
                );
                System.out.println("Personas encontradas: " + personas.size());
                showListResult(personas, "Personas encontradas:");
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaNacimientoField, usuarioField, passwordField, poblacionField);
            } catch (Exception ex) {
                handleError(ex, "Error al buscar personas: " + ex.getMessage());
                System.out.println("Exception: " + ex.getMessage());
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2; // Configura la posicion del panel de botones
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        createPanel.add(buttonPanel, gbc); // Anade el panel de botones al panel de crear/actualizar persona

        // Anade el panel de errores al final del formulario
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 10, 8, 10);
        createPanel.add(errorScrollPane, gbc);

        panel.add(createPanel, BorderLayout.NORTH); // Anade el panel de crear/actualizar persona al panel principal
        return panel; // Devuelve el panel de personas
    }

    // Metodo para crear el panel de empleados
    private JPanel createEmpleadoPanel() {
        // Crear panel principal con borde y fondo blanco
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Crear subpanel con titulo
        JPanel createPanel = createTitledPanel("Gestionar Empleados");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Agregar campos de texto
        JTextField dniField = addField(createPanel, gbc, "DNI:", 0);
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 1);
        JTextField apellido1Field = addField(createPanel, gbc, "Apellido 1:", 2);
        JTextField apellido2Field = addField(createPanel, gbc, "Apellido 2:", 3);
        JTextField fechaNacimientoField = addField(createPanel, gbc, "Fecha Nacimiento (YYYY-MM-DD):", 4);
        JTextField usuarioField = addField(createPanel, gbc, "Usuario:", 5);
        JTextField passwordField = addField(createPanel, gbc, "Password:", 6);
        JTextField poblacionField = addField(createPanel, gbc, "Poblacion:", 7);
        JTextField numEmpleadoField = addField(createPanel, gbc, "Numero Empleado:", 8);
        JTextField inicioContratoField = addField(createPanel, gbc, "Inicio Contrato (YYYY-MM-DD):", 9);
        JTextField segSocialField = addField(createPanel, gbc, "Seguridad Social:", 10);

        // Crear panel para botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        // Crear botones
        JButton crearButton = new JButton("Crear Empleado", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        JButton actualizarButton = new JButton("Actualizar Empleado", loadIcon("/resources/iconos/edit.png"));
        styleButton(actualizarButton, new Color(33, 37, 41), false);
        JButton eliminarButton = new JButton("Eliminar Empleado", loadIcon("/resources/iconos/delete.png"));
        styleButton(eliminarButton, new Color(211, 47, 47), true);
        JButton buscarButton = new JButton("Buscar Empleado", loadIcon("/resources/iconos/magnifier.png"));
        styleButton(buscarButton, new Color(33, 37, 41), false);

        // Agregar botones al panel
        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(buscarButton);

        // Boton crear empleado
        crearButton.addActionListener(event -> {
            // Validar campos
            if (!validateFields(dniField, nombreField, apellido1Field, fechaNacimientoField, usuarioField, passwordField, poblacionField, numEmpleadoField, inicioContratoField, segSocialField)) {
                return;
            }
            try {
                // Validar DNI
                if (!validateDni(dniField.getText())) {
                    showError("DNI invalido.");
                    return;
                }
                // Verificar si empleado existe
                Persona persona = federacion.buscaPersona(dniField.getText().trim());
                if (persona instanceof Empleado) {
                    showError("Empleado ya existe.");
                    return;
                }
                // Obtener datos
                String dni = dniField.getText().trim();
                String nombre = nombreField.getText().trim();
                String apellido1 = apellido1Field.getText().trim();
                String apellido2 = apellido2Field.getText().trim();
                LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoField.getText().trim());
                String usuario = usuarioField.getText().trim();
                String password = passwordField.getText().trim();
                String poblacion = poblacionField.getText().trim();
                int numEmpleado = Integer.parseInt(numEmpleadoField.getText().trim());
                LocalDate inicioContrato = LocalDate.parse(inicioContratoField.getText().trim());
                String segSocial = segSocialField.getText().trim();
                // Crear empleado
                Empleado empleado = federacion.nuevoEmpleado(dni, nombre, apellido1, apellido2, fechaNacimiento, usuario, password, poblacion, numEmpleado, inicioContrato, segSocial);
                empleado.guardar();
                JOptionPane.showMessageDialog(frame, "Empleado creado: " + empleado.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaNacimientoField, usuarioField, passwordField, poblacionField, numEmpleadoField, inicioContratoField, segSocialField);
            } catch (DateTimeParseException ex) {
                showError("Formato de fecha invalido. Use YYYY-MM-DD.");
            } catch (NumberFormatException ex) {
                showError("Numero de empleado invalido.");
            } catch (SQLException ex) {
                handleError(ex, "Error al guardar empleado.");
            }
        });

        // Boton actualizar empleado
        actualizarButton.addActionListener(event -> {
            // Validar campos
            if (!validateFields(dniField, nombreField, apellido1Field, fechaNacimientoField, usuarioField, passwordField, poblacionField, numEmpleadoField, inicioContratoField, segSocialField)) {
                return;
            }
            try {
                // Validar DNI
                if (!validateDni(dniField.getText())) {
                    showError("DNI invalido.");
                    return;
                }
                // Buscar empleado
                Persona persona = federacion.buscaPersona(dniField.getText().trim());
                if (!(persona instanceof Empleado)) {
                    showError("Empleado no encontrado.");
                    return;
                }
                Empleado empleado = (Empleado) persona;
                // Actualizar datos
                empleado.setNombre(nombreField.getText().trim());
                empleado.setApellido1(apellido1Field.getText().trim());
                empleado.setApellido2(apellido2Field.getText().trim());
                empleado.setFechaNacimiento(LocalDate.parse(fechaNacimientoField.getText().trim()));
                empleado.setUsuario(usuarioField.getText().trim());
                empleado.setPassword(passwordField.getText().trim());
                empleado.setPoblacion(poblacionField.getText().trim());
                empleado.setNumEmpleado(Integer.parseInt(numEmpleadoField.getText().trim()));
                empleado.setInicioContrato(LocalDate.parse(inicioContratoField.getText().trim()));
                empleado.setSegSocial(segSocialField.getText().trim());
                empleado.actualizar();
                JOptionPane.showMessageDialog(frame, "Empleado actualizado: " + empleado.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaNacimientoField, usuarioField, passwordField, poblacionField, numEmpleadoField, inicioContratoField, segSocialField);
            } catch (DateTimeParseException ex) {
                showError("Formato de fecha invalido. Use YYYY-MM-DD.");
            } catch (NumberFormatException ex) {
                showError("Numero de empleado invalido.");
            } catch (SQLException ex) {
                handleError(ex, "Error al actualizar empleado.");
            }
        });

        // Boton eliminar empleado
        eliminarButton.addActionListener(event -> {
            // Validar campo DNI
            if (!validateFields(dniField)) {
                return;
            }
            try {
                // Validar DNI
                if (!validateDni(dniField.getText())) {
                    showError("DNI invalido.");
                    return;
                }
                // Buscar empleado
                Persona persona = federacion.buscaPersona(dniField.getText().trim());
                if (!(persona instanceof Empleado)) {
                    showError("Empleado no encontrado.");
                    return;
                }
                Empleado empleado = (Empleado) persona;
                // Eliminar empleado
                empleado.eliminar();
                JOptionPane.showMessageDialog(frame, "Empleado eliminado: " + empleado.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaNacimientoField, usuarioField, passwordField, poblacionField, numEmpleadoField, inicioContratoField, segSocialField);
            } catch (SQLException ex) {
                handleError(ex, "Error al eliminar empleado.");
            }
        });

        // Boton buscar empleado
        buscarButton.addActionListener(event -> {
            // Validar campo DNI
            if (!validateFields(dniField)) {
                return;
            }
            // Validar DNI
            if (!validateDni(dniField.getText())) {
                showError("DNI invalido.");
                return;
            }
            // Buscar empleado
            Persona persona = federacion.buscaPersona(dniField.getText().trim());
            if (!(persona instanceof Empleado)) {
                showError("Empleado no encontrado.");
                return;
            }
            Empleado empleado = (Empleado) persona;
            // Mostrar resultado
            List<Empleado> empleados = new ArrayList<>();
            empleados.add(empleado);
            showListResult(empleados, "Empleado encontrado:");
            // Limpiar campos
            clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaNacimientoField, usuarioField, passwordField, poblacionField, numEmpleadoField, inicioContratoField, segSocialField);
        });

        // Configurar posicion de panel de botones
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        createPanel.add(buttonPanel, gbc);

        // Configurar posicion de panel de errores
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 10, 8, 10);
        createPanel.add(errorScrollPane, gbc);

        // Agregar subpanel al panel principal
        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    // Metodo para crear el panel de categorias
    private JPanel createCategoriaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel de categorias
        panel.setBackground(Color.WHITE); // Establece el color de fondo del panel
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Establece un borde vacio alrededor del panel

        JPanel createPanel = createTitledPanel("Crear/Actualizar Categoria"); // Crea el panel para crear/actualizar categorias
        GridBagConstraints gbc = new GridBagConstraints(); // Configuracion de la cuadricula
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena el espacio horizontalmente
        gbc.weightx = 1.0; // Permite que los campos se expandan horizontalmente

        // Campos para ingresar datos de la categoria
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 0); // Campo para el nombre
        JTextField ordenField = addField(createPanel, gbc, "Orden:", 1); // Campo para el orden
        JTextField precioField = addField(createPanel, gbc, "Precio Licencia:", 2); // Campo para el precio

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Panel para los botones
        buttonPanel.setBackground(Color.WHITE); // Establece el color de fondo del panel de botones

        // Botones para crear, actualizar, eliminar y listar categorias
        JButton crearButton = new JButton("Crear Categoria", loadIcon("/resources/iconos/cross.png")); // Boton para crear categoria
        styleButton(crearButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Categoria", loadIcon("/resources/iconos/edit.png")); // Boton para actualizar categoria
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Categoria", loadIcon("/resources/iconos/delete.png")); // Boton para eliminar categoria
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton listarButton = new JButton("Listar Categorias", loadIcon("/resources/iconos/magnifier.png")); // Boton para listar categorias
        styleButton(listarButton, new Color(33, 37, 41), false); // Estiliza el boton

        // Anade los botones al panel de botones
        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(listarButton);

        // Accion para crear una nueva categoria
        crearButton.addActionListener(event -> {
            if (!validateFields(nombreField, ordenField, precioField)) {
                showError("Todos los campos obligatorios deben estar completos.");
                return;
            }
            try {
                int orden = Integer.parseInt(ordenField.getText());
                double precio = Double.parseDouble(precioField.getText());
                if (orden <= 0) {
                    showError("El orden debe ser un número positivo.");
                    return;
                }
                if (precio < 0) {
                    showError("El precio no puede ser negativo.");
                    return;
                }
                Categoria categoria = federacion.nuevaCategoria(nombreField.getText(), orden, precio);
                categoria.guardar();
                JOptionPane.showMessageDialog(frame, "Categoría creada con éxito: " + categoria.getNombre(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                clearFields(nombreField, ordenField, precioField);
            } catch (NumberFormatException ex) {
                showError("Orden y precio deben ser numéricos.");
            } catch (SQLException ex) {
                handleError(ex, "Error al guardar categoría en la base de datos.");
            } catch (Exception ex) {
                handleError(ex, "Error al crear categoría.");
            }
        });

        // Accion para actualizar una categoria existente
        actualizarButton.addActionListener(event -> {
            if (!validateFields(nombreField, ordenField, precioField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                Categoria categoria = Categoria.buscarPorNombre(nombreField.getText()); // Busca la categoria por nombre
                if (categoria == null) {
                    showError("Categoria no encontrada.");
                    return;
                }
                int orden = Integer.parseInt(ordenField.getText()); // Convierte el orden a entero
                double precio = Double.parseDouble(precioField.getText()); // Convierte el precio a double
                if (orden <= 0) {
                    showError("El orden debe ser un numero positivo.");
                    return;
                }
                if (precio < 0) {
                    showError("El precio no puede ser negativo.");
                    return;
                }
                categoria.setOrden(orden); // Actualiza el orden
                categoria.setPrecioLicencia(precio); // Actualiza el precio
                categoria.actualizar(); // Guarda los cambios en la base de datos
                JOptionPane.showMessageDialog(frame, "Categoria actualizada con exito: " + categoria.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE); // Muestra mensaje de exito
                clearFields(nombreField, ordenField, precioField); // Limpia los campos
            } catch (NumberFormatException ex) {
                showError("Orden y precio deben ser numericos.");
            } catch (SQLException ex) {
                handleError(ex, "Error al actualizar categoria en la base de datos.");
            } catch (Exception ex) {
                handleError(ex, "Error al actualizar categoria.");
            }
        });

        // Accion para eliminar una categoria
        eliminarButton.addActionListener(event -> {
            if (!validateFields(nombreField)) {
                return; // Valida que el campo no este vacio
            }
            try {
                Categoria categoria = Categoria.buscarPorNombre(nombreField.getText()); // Busca la categoria por nombre
                if (categoria == null) {
                    showError("Categoria no encontrada.");
                    return;
                }
                categoria.eliminar(); // Elimina la categoria de la base de datos
                JOptionPane.showMessageDialog(frame, "Categoria eliminada con exito: " + categoria.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE); // Muestra mensaje de exito
                clearFields(nombreField, ordenField, precioField); // Limpia los campos
            } catch (SQLException ex) {
                handleError(ex, "Error al eliminar categoria de la base de datos.");
            } catch (Exception ex) {
                handleError(ex, "Error al eliminar categoria.");
            }
        });

        // Accion para listar categorias
        listarButton.addActionListener(event -> {
            clearFields(nombreField, ordenField, precioField); // Limpia campos y errores
            try {
                List<Categoria> categorias = federacion.obtenerCategorias(); // Obtiene la lista de categorias
                showListResult(categorias, "Categorias disponibles:"); // Muestra los resultados
                clearFields(nombreField, ordenField, precioField); // Limpia los campos
            } catch (Exception ex) {
                handleError(ex, "Error al listar categorias.");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Configura la posicion del panel de botones
        gbc.fill = GridBagConstraints.NONE; // Para que el panel de botones no se estire
        gbc.anchor = GridBagConstraints.CENTER; // Centra el panel de botones
        createPanel.add(buttonPanel, gbc); // Anade el panel de botones al panel de crear/actualizar categoria

        // Anade el panel de errores al final del formulario
        gbc.gridx = 0;
        gbc.gridy = 4; // Siguiente fila despues del panel de botones
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 0; // No permitir que crezca verticalmente
        gbc.insets = new Insets(10, 10, 8, 10); // Espaciado
        createPanel.add(errorScrollPane, gbc);

        panel.add(createPanel, BorderLayout.NORTH); // Anade el panel de crear/actualizar categoria al panel principal
        return panel; // Devuelve el panel de categorias
    }
    
    // Metodo para crear el panel de instalaciones
    private JPanel createInstalacionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel de instalaciones
        panel.setBackground(Color.WHITE); // Establece el color de fondo del panel
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Establece un borde vacio alrededor del panel

        JPanel createPanel = createTitledPanel("Crear/Actualizar Instalacion"); // Crea el panel para crear/actualizar instalaciones
        GridBagConstraints gbc = new GridBagConstraints(); // Configuracion de la cuadricula
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena el espacio horizontalmente
        gbc.weightx = 1.0;

        // Campos para ingresar datos de la instalacion
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 0); // Campo para el nombre
        JTextField direccionField = addField(createPanel, gbc, "Direccion:", 1); // Campo para la direccion
        JComboBox<entidades.Instalacion.TipoSuperficie> superficieComboBox = addComboBox(createPanel, gbc, "Superficie:", 2); // Combo box para la superficie

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Panel para los botones
        buttonPanel.setBackground(Color.WHITE); // Establece el color de fondo del panel de botones

        // Botones para crear, actualizar, eliminar y buscar instalaciones
        JButton crearButton = new JButton("Crear Instalacion", loadIcon("/resources/iconos/cross.png")); // Boton para crear instalacion
        styleButton(crearButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Instalacion", loadIcon("/resources/iconos/edit.png")); // Boton para actualizar instalacion
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Instalacion", loadIcon("/resources/iconos/delete.png")); // Boton para eliminar instalacion
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton buscarButton = new JButton("Buscar Instalaciones", loadIcon("/resources/iconos/magnifier.png")); // Boton para buscar instalaciones
        styleButton(buscarButton, new Color(33, 37, 41), false); // Estiliza el boton

        // Anade los botones al panel de botones
        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(buscarButton);

        // Accion para crear una nueva instalacion
        crearButton.addActionListener(event -> {
            if (!validateFields(nombreField, direccionField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                entidades.Instalacion.TipoSuperficie superficie = (entidades.Instalacion.TipoSuperficie) superficieComboBox.getSelectedItem(); // Obtiene la superficie seleccionada
                Instalacion instalacion = federacion.nuevaInstalacion(nombreField.getText(), direccionField.getText(), superficie.toString()); // Crea la instalacion
                instalacion.guardar(); // Persiste la instalacion en la base de datos
                JOptionPane.showMessageDialog(frame, "Instalacion creada con exito: " + instalacion.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE); // Muestra mensaje de exito
                clearFields(nombreField, direccionField); // Limpia los campos
                superficieComboBox.setSelectedIndex(0); // Resetea el combo box
            } catch (SQLException ex) {
                handleError(ex, "Error al guardar instalacion en la base de datos.");
            } catch (Exception ex) {
                handleError(ex, "Error al crear instalacion.");
            }
        });

        // Accion para actualizar una instalacion existente
        actualizarButton.addActionListener(event -> {
            if (!validateFields(nombreField, direccionField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                Instalacion instalacion = Instalacion.buscarPorNombre(nombreField.getText()); // Busca la instalacion por nombre
                if (instalacion == null) {
                    showError("Instalacion no encontrada.");
                    return;
                }
                entidades.Instalacion.TipoSuperficie superficie = (entidades.Instalacion.TipoSuperficie) superficieComboBox.getSelectedItem(); // Obtiene la superficie seleccionada
                instalacion.setDireccion(direccionField.getText()); // Actualiza la direccion
                instalacion.setSuperficie(superficie); // Actualiza la superficie
                instalacion.actualizar(); // Guarda los cambios en la base de datos
                JOptionPane.showMessageDialog(frame, "Instalacion actualizada con exito: " + instalacion.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE); // Muestra mensaje de exito
                clearFields(nombreField, direccionField); // Limpia los campos
                superficieComboBox.setSelectedIndex(0); // Resetea el combo box
            } catch (SQLException ex) {
                handleError(ex, "Error al actualizar instalacion en la base de datos.");
            } catch (Exception ex) {
                handleError(ex, "Error al actualizar instalacion.");
            }
        });

        // Accion para eliminar una instalacion
        eliminarButton.addActionListener(event -> {
            if (!validateFields(nombreField)) {
                return; // Valida que el campo no este vacio
            }
            try {
                Instalacion instalacion = Instalacion.buscarPorNombre(nombreField.getText()); // Busca la instalacion por nombre
                if (instalacion == null) {
                    showError("Instalacion no encontrada.");
                    return;
                }
                instalacion.eliminar(); // Elimina la instalacion de la base de datos
                JOptionPane.showMessageDialog(frame, "Instalacion eliminada con exito: " + instalacion.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE); // Muestra mensaje de exito
                clearFields(nombreField, direccionField); // Limpia los campos
                superficieComboBox.setSelectedIndex(0); // Resetea el combo box
            } catch (SQLException ex) {
                handleError(ex, "Error al eliminar instalacion de la base de datos.");
            } catch (Exception ex) {
                handleError(ex, "Error al eliminar instalacion.");
            }
        });

        // Accion para buscar instalaciones
        buscarButton.addActionListener(event -> {
            if (!validateFields(nombreField)) {
                return; // Valida que el campo no este vacio
            }
            try {
                List<Instalacion> instalaciones = federacion.buscarInstalaciones(nombreField.getText()); // Busca instalaciones por nombre
                showListResult(instalaciones, "Instalaciones encontradas:"); // Muestra los resultados
                clearFields(nombreField, direccionField); // Limpia los campos
                superficieComboBox.setSelectedIndex(0); // Resetea el combo box
            } catch (Exception ex) {
                handleError(ex, "Error al buscar instalaciones.");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Configura la posicion del panel de botones
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        createPanel.add(buttonPanel, gbc); // Anade el panel de botones al panel de crear/actualizar instalacion

        // Anade el panel de errores al final del formulario
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 10, 8, 10);
        createPanel.add(errorScrollPane, gbc);

        panel.add(createPanel, BorderLayout.NORTH); // Anade el panel de crear/actualizar instalacion al panel principal
        return panel; // Devuelve el panel de instalaciones
    }

    // Metodo para crear el panel de clubes
    private JPanel createClubPanel() {
        // Crear panel principal con borde y fondo blanco
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Crear subpanel con titulo
        JPanel createPanel = createTitledPanel("Gestionar Clubes");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Agregar campos de texto
        JTextField nombreField = addField(createPanel, gbc, "Nombre Club:", 0);
        JTextField fechaFundacionField = addField(createPanel, gbc, "Fecha Fundacion (YYYY-MM-DD):", 1);
        JTextField dniPresidenteField = addField(createPanel, gbc, "DNI Presidente:", 2);

        // Crear panel para botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        // Crear botones
        JButton crearButton = new JButton("Crear Club", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        JButton actualizarButton = new JButton("Actualizar Club", loadIcon("/resources/iconos/edit.png"));
        styleButton(actualizarButton, new Color(33, 37, 41), false);
        JButton eliminarButton = new JButton("Eliminar Club", loadIcon("/resources/iconos/delete.png"));
        styleButton(eliminarButton, new Color(211, 47, 47), true);
        JButton buscarButton = new JButton("Buscar Club", loadIcon("/resources/iconos/magnifier.png"));
        styleButton(buscarButton, new Color(33, 37, 41), false);

        // Agregar botones al panel
        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(buscarButton);

        // Boton crear club
        crearButton.addActionListener(event -> {
            // Validar campos
            if (!validateFields(nombreField, fechaFundacionField, dniPresidenteField)) {
                return;
            }
            try {
                // Obtener datos de campos
                String nombre = nombreField.getText().trim();
                LocalDate fechaFundacion = LocalDate.parse(fechaFundacionField.getText().trim());
                String dniPresidente = dniPresidenteField.getText().trim();
                // Buscar presidente
                Persona presidente = federacion.buscaPersona(dniPresidente);
                if (presidente == null) {
                    showError("Presidente no encontrado.");
                    return;
                }
                // Verificar si club existe
                Club existingClub = federacion.buscarClub(nombre);
                if (existingClub != null) {
                    showError("El club ya existe.");
                    return;
                }
                // Crear y guardar club
                Club club = federacion.nuevoClub(nombre, fechaFundacion, presidente);
                club.guardar();
                JOptionPane.showMessageDialog(frame, "Club creado: " + club.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                clearFields(nombreField, fechaFundacionField, dniPresidenteField);
            } catch (DateTimeParseException ex) {
                showError("Formato de fecha invalido. Use YYYY-MM-DD.");
            } catch (SQLException ex) {
                handleError(ex, "Error al guardar club.");
            }
        });

        // Boton actualizar club
        actualizarButton.addActionListener(event -> {
            // Validar campos
            if (!validateFields(nombreField, fechaFundacionField, dniPresidenteField)) {
                return;
            }
            try {
                // Obtener datos de campos
                String nombre = nombreField.getText().trim();
                LocalDate fechaFundacion = LocalDate.parse(fechaFundacionField.getText().trim());
                String dniPresidente = dniPresidenteField.getText().trim();
                // Buscar presidente
                Persona presidente = federacion.buscaPersona(dniPresidente);
                if (presidente == null) {
                    showError("Presidente no encontrado.");
                    return;
                }
                // Buscar club
                Club club = federacion.buscarClub(nombre);
                if (club == null) {
                    showError("Club no encontrado.");
                    return;
                }
                // Actualizar datos
                club.setFechaFundacion(fechaFundacion);
                club.setPresidente(presidente);
                club.actualizar();
                JOptionPane.showMessageDialog(frame, "Club actualizado: " + club.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                clearFields(nombreField, fechaFundacionField, dniPresidenteField);
            } catch (DateTimeParseException ex) {
                showError("Formato de fecha invalido. Use YYYY-MM-DD.");
            } catch (SQLException ex) {
                handleError(ex, "Error al actualizar club.");
            }
        });

        // Boton eliminar club
        eliminarButton.addActionListener(event -> {
            // Validar campo nombre
            if (!validateFields(nombreField)) {
                return;
            }
            try {
                // Obtener nombre
                String nombre = nombreField.getText().trim();
                // Buscar club
                Club club = federacion.buscarClub(nombre);
                if (club == null) {
                    showError("Club no encontrado.");
                    return;
                }
                // Eliminar club
                club.eliminar();
                JOptionPane.showMessageDialog(frame, "Club eliminado: " + club.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                clearFields(nombreField, fechaFundacionField, dniPresidenteField);
            } catch (SQLException ex) {
                handleError(ex, "Error al eliminar club.");
            }
        });

        // Boton buscar club
        buscarButton.addActionListener(event -> {
            // Validar campo nombre
            if (!validateFields(nombreField)) {
                return;
            }
            // Obtener nombre
            String nombre = nombreField.getText().trim();
            // Buscar club
            Club club = federacion.buscarClub(nombre);
            if (club == null) {
                showError("Club no encontrado.");
                return;
            }
            // Mostrar resultado
            List<Club> clubes = new ArrayList<>();
            clubes.add(club);
            showListResult(clubes, "Club encontrado:");
            // Limpiar campos
            clearFields(nombreField, fechaFundacionField, dniPresidenteField);
        });

        // Configurar posicion de panel de botones
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        createPanel.add(buttonPanel, gbc);

        // Configurar posicion de panel de errores
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 10, 8, 10);
        createPanel.add(errorScrollPane, gbc);

        // Agregar subpanel al panel principal
        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }
        
    // Metodo para crear el panel de grupos
    private JPanel createGrupoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel de grupos
        panel.setBackground(Color.WHITE); // Establece el color de fondo del panel
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Establece un borde vacio alrededor del panel

        JPanel createPanel = createTitledPanel("Crear/Actualizar Grupo"); // Crea el panel para crear/actualizar grupos
        GridBagConstraints gbc = new GridBagConstraints(); // Configuracion de la cuadricula
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena el espacio horizontalmente
        gbc.weightx = 1.0;

        // Campos para ingresar datos del grupo
        JTextField categoriaField = addField(createPanel, gbc, "Nombre Categoria:", 0); // Campo para la categoria
        JTextField nombreField = addField(createPanel, gbc, "Nombre Grupo:", 1); // Campo para el nombre

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Panel para los botones
        buttonPanel.setBackground(Color.WHITE); // Establece el color de fondo del panel de botones

        // Botones para crear, actualizar, eliminar y listar grupos
        JButton crearButton = new JButton("Crear Grupo", loadIcon("/resources/iconos/cross.png")); // Boton para crear grupo
        styleButton(crearButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Grupo", loadIcon("/resources/iconos/edit.png")); // Boton para actualizar grupo
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Grupo", loadIcon("/resources/iconos/delete.png")); // Boton para eliminar grupo
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton listarButton = new JButton("Listar Grupos", loadIcon("/resources/iconos/magnifier.png")); // Boton para listar grupos
        styleButton(listarButton, new Color(33, 37, 41), false); // Estiliza el boton

        // Anade los botones al panel de botones
        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(listarButton);

        // Accion para crear un nuevo grupo
        crearButton.addActionListener(event -> {
            if (!validateFields(categoriaField, nombreField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                Categoria categoria = Categoria.buscarPorNombre(categoriaField.getText()); // Busca la categoria por nombre
                if (categoria == null) {
                    showError("Categoria no encontrada.");
                    return;
                }
                Grupo grupo = federacion.nuevoGrupo(categoria, nombreField.getText()); // Crea un nuevo grupo
                grupo.guardar(); // Persiste el grupo en la base de datos
                JOptionPane.showMessageDialog(frame, "Grupo creado con exito: " + grupo.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE); // Muestra mensaje de exito
                clearFields(categoriaField, nombreField); // Limpia los campos
            } catch (SQLException ex) {
                handleError(ex, "Error al guardar grupo en la base de datos.");
            } catch (Exception ex) {
                handleError(ex, "Error al crear grupo.");
            }
        });

        // Accion para actualizar un grupo existente
        actualizarButton.addActionListener(event -> {
            if (!validateFields(categoriaField, nombreField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                Categoria categoria = Categoria.buscarPorNombre(categoriaField.getText()); // Busca la categoria por nombre
                if (categoria == null) {
                    showError("Categoria no encontrada.");
                    return;
                }
                Grupo grupo = Grupo.buscarPorNombre(nombreField.getText()); // Busca el grupo por nombre
                if (grupo == null) {
                    showError("Grupo no encontrado.");
                    return;
                }
                grupo.setCategoria(categoria); // Actualiza la categoria
                grupo.actualizar(); // Guarda los cambios en la base de datos
                JOptionPane.showMessageDialog(frame, "Grupo actualizado con exito: " + grupo.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE); // Muestra mensaje de exito
                clearFields(categoriaField, nombreField); // Limpia los campos
            } catch (SQLException ex) {
                handleError(ex, "Error al actualizar grupo en la base de datos.");
            } catch (Exception ex) {
                handleError(ex, "Error al actualizar grupo.");
            }
        });

        // Accion para eliminar un grupo
        eliminarButton.addActionListener(event -> {
            if (!validateFields(nombreField)) {
                return; // Valida que el campo no este vacio
            }
            try {
                Grupo grupo = Grupo.buscarPorNombre(nombreField.getText()); // Busca el grupo por nombre
                if (grupo == null) {
                    showError("Grupo no encontrado.");
                    return;
                }
                grupo.eliminar(); // Elimina el grupo de la base de datos
                JOptionPane.showMessageDialog(frame, "Grupo eliminado con exito: " + grupo.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE); // Muestra mensaje de exito
                clearFields(categoriaField, nombreField); // Limpia los campos
            } catch (SQLException ex) {
                handleError(ex, "Error al eliminar grupo de la base de datos.");
            } catch (Exception ex) {
                handleError(ex, "Error al eliminar grupo.");
            }
        });

        // Accion para listar grupos en una categoria
        listarButton.addActionListener(event -> {
            if (!validateFields(categoriaField)) {
                return; // Valida que el campo no este vacio
            }
            try {
                Categoria categoria = Categoria.buscarPorNombre(categoriaField.getText()); // Busca la categoria por nombre
                if (categoria == null) {
                    showError("Categoria no encontrada.");
                    return;
                }
                List<Grupo> grupos = federacion.obtenerGrupos(categoria); // Obtiene la lista de grupos
                showListResult(grupos, "Grupos en " + categoria.getNombre() + ":"); // Muestra los resultados
                clearFields(categoriaField, nombreField); // Limpia los campos
            } catch (SQLException ex) {
                handleError(ex, "Error al listar grupos de la base de datos.");
            } catch (Exception ex) {
                handleError(ex, "Error al listar grupos.");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Configura la posicion del panel de botones
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        createPanel.add(buttonPanel, gbc); // Anade el panel de botones al panel de crear/actualizar grupo

        // Anade el panel de errores al final del formulario
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 10, 8, 10);
        createPanel.add(errorScrollPane, gbc);

        panel.add(createPanel, BorderLayout.NORTH); // Anade el panel de crear/actualizar grupo al panel principal
        return panel; // Devuelve el panel de grupos
    }

    // Metodo para crear el panel de equipos
    private JPanel createEquipoPanel() {
        // Crear panel principal con borde y fondo blanco
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Crear subpanel con titulo
        JPanel createPanel = createTitledPanel("Gestionar Equipos");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Agregar campos de texto
        JTextField letraField = addField(createPanel, gbc, "Letra Equipo:", 0);
        JTextField instalacionField = addField(createPanel, gbc, "Instalacion:", 1);
        JTextField grupoField = addField(createPanel, gbc, "Grupo:", 2);
        JTextField clubField = addField(createPanel, gbc, "Club:", 3);
        JTextField buscarLetraField = addField(createPanel, gbc, "Buscar Letra Equipo:", 4);
        JTextField dniField = addField(createPanel, gbc, "DNI Entrenador:", 5);
        JTextField dniJugadorField = addField(createPanel, gbc, "DNI Jugador:", 6);

        // Crear combo box para equipos
        JComboBox<Equipo> equiposComboBox = new JComboBox<>();
        try {
            List<Equipo> equipos = Equipo.obtenerTodos();
            for (Equipo e : equipos) {
                equiposComboBox.addItem(e);
            }
        } catch (SQLException ex) {
            handleError(ex, "Error al cargar equipos.");
        }
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        createPanel.add(equiposComboBox, gbc);

        // Crear panel para botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        // Crear botones
        JButton crearButton = new JButton("Crear Equipo", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        JButton actualizarButton = new JButton("Actualizar Equipo", loadIcon("/resources/iconos/edit.png"));
        styleButton(actualizarButton, new Color(33, 37, 41), false);
        JButton eliminarButton = new JButton("Eliminar Equipo", loadIcon("/resources/iconos/delete.png"));
        styleButton(eliminarButton, new Color(211, 47, 47), true);
        JButton buscarButton = new JButton("Buscar Equipo", loadIcon("/resources/iconos/magnifier.png"));
        styleButton(buscarButton, new Color(33, 37, 41), false);

        // Agregar botones al panel
        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(buscarButton);

        // Boton crear equipo
        crearButton.addActionListener(event -> {
            // Validar campos
            if (!validateFields(letraField, instalacionField, grupoField, clubField)) {
                showError("Letra, instalacion, grupo y club son obligatorios.");
                return;
            }
            try {
                // Validar formato letra
                if (!letraField.getText().trim().matches("[A-Za-z]")) {
                    showError("Letra debe ser un caracter alfabetico.");
                    return;
                }
                // Buscar instalacion
                Instalacion instalacion = Instalacion.buscarPorNombre(instalacionField.getText().trim());
                if (instalacion == null) {
                    showError("Instalacion no encontrada.");
                    return;
                }
                // Buscar grupo
                Grupo grupo = Grupo.buscarPorNombre(grupoField.getText().trim());
                if (grupo == null) {
                    showError("Grupo no encontrado.");
                    return;
                }
                // Buscar club
                Club club = federacion.buscarClub(clubField.getText().trim());
                if (club == null) {
                    showError("Club no encontrado.");
                    return;
                }
                // Crear equipo
                Equipo equipo = federacion.nuevoEquipo(letraField.getText().trim(), instalacion, grupo);
                // Asignar club manualmente
                equipo.setClub(club);
                equipo.guardar();
                JOptionPane.showMessageDialog(frame, "Equipo creado: " + equipo.getLetra(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                clearFields(letraField, instalacionField, grupoField, clubField, buscarLetraField, dniField, dniJugadorField);
                // Actualizar combo box
                equiposComboBox.removeAllItems();
                List<Equipo> equipos = Equipo.obtenerTodos();
                for (Equipo e : equipos) {
                    equiposComboBox.addItem(e);
                }
            } catch (SQLException ex) {
                handleError(ex, "Error al guardar equipo.");
            }
        });

        // Boton actualizar equipo
        actualizarButton.addActionListener(event -> {
            // Validar campos
            if (!validateFields(letraField, instalacionField, grupoField, clubField)) {
                showError("Letra, instalacion, grupo y club son obligatorios.");
                return;
            }
            try {
                // Validar formato letra
                if (!letraField.getText().trim().matches("[A-Za-z]")) {
                    showError("Letra debe ser un caracter alfabetico.");
                    return;
                }
                // Buscar instalacion
                Instalacion instalacion = Instalacion.buscarPorNombre(instalacionField.getText().trim());
                if (instalacion == null) {
                    showError("Instalacion no encontrada.");
                    return;
                }
                // Buscar grupo
                Grupo grupo = Grupo.buscarPorNombre(grupoField.getText().trim());
                if (grupo == null) {
                    showError("Grupo no encontrado.");
                    return;
                }
                // Buscar club
                Club club = federacion.buscarClub(clubField.getText().trim());
                if (club == null) {
                    showError("Club no encontrado.");
                    return;
                }
                // Buscar equipo
                Equipo equipo = Equipo.buscarPorLetraYClub(letraField.getText().trim(), clubField.getText().trim());
                if (equipo == null) {
                    showError("Equipo no encontrado.");
                    return;
                }
                // Actualizar datos
                equipo.setInstalacion(instalacion);
                equipo.setGrupo(grupo);
                equipo.setClub(club);
                equipo.actualizar();
                JOptionPane.showMessageDialog(frame, "Equipo actualizado: " + equipo.getLetra(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                clearFields(letraField, instalacionField, grupoField, clubField, buscarLetraField, dniField, dniJugadorField);
                // Actualizar combo box
                equiposComboBox.removeAllItems();
                List<Equipo> equipos = Equipo.obtenerTodos();
                for (Equipo e : equipos) {
                    equiposComboBox.addItem(e);
                }
            } catch (SQLException ex) {
                handleError(ex, "Error al actualizar equipo.");
            }
        });

        // Boton eliminar equipo
        eliminarButton.addActionListener(event -> {
            // Validar campo letra
            if (!validateFields(letraField, clubField)) {
                showError("Letra y club son obligatorios.");
                return;
            }
            try {
                // Buscar equipo
                Equipo equipo = Equipo.buscarPorLetraYClub(letraField.getText().trim(), clubField.getText().trim());
                if (equipo == null) {
                    showError("Equipo no encontrado.");
                    return;
                }
                // Eliminar equipo
                equipo.eliminar();
                JOptionPane.showMessageDialog(frame, "Equipo eliminado: " + equipo.getLetra(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                clearFields(letraField, instalacionField, grupoField, clubField, buscarLetraField, dniField, dniJugadorField);
                // Actualizar combo box
                equiposComboBox.removeAllItems();
                List<Equipo> equipos = Equipo.obtenerTodos();
                for (Equipo e : equipos) {
                    equiposComboBox.addItem(e);
                }
            } catch (SQLException ex) {
                handleError(ex, "Error al eliminar equipo.");
            }
        });

        // Boton buscar equipo
        buscarButton.addActionListener(event -> {
            // Validar campo buscar letra
            if (!validateFields(buscarLetraField, clubField)) {
                showError("Letra y club son obligatorios.");
                return;
            }
            try {
                // Buscar equipo
                Equipo equipo = Equipo.buscarPorLetraYClub(buscarLetraField.getText().trim(), clubField.getText().trim());
                if (equipo == null) {
                    showError("Equipo no encontrado.");
                    return;
                }
                // Mostrar resultado
                List<Equipo> equipos = new ArrayList<>();
                equipos.add(equipo);
                showListResult(equipos, "Equipo encontrado:");
                // Limpiar campos
                clearFields(letraField, instalacionField, grupoField, clubField, buscarLetraField, dniField, dniJugadorField);
            } catch (SQLException ex) {
                handleError(ex, "Error al buscar equipo.");
            }
        });

        // Configurar posicion de panel de botones
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        createPanel.add(buttonPanel, gbc);

        // Configurar posicion de panel de errores
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 10, 8, 10);
        createPanel.add(errorScrollPane, gbc);

        // Agregar subpanel al panel principal
        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    // Metodo para crear el panel de licencias
    private JPanel createLicenciaPanel() {
        // Crear panel principal con borde y fondo blanco
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Crear subpanel con titulo
        JPanel createPanel = createTitledPanel("Gestionar Licencias");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Agregar campos de texto
        JTextField dniField = addField(createPanel, gbc, "DNI Jugador:", 0);
        JTextField letraField = addField(createPanel, gbc, "Letra Equipo:", 1);
        JTextField clubField = addField(createPanel, gbc, "Nombre Club:", 2);
        JTextField precioField = addField(createPanel, gbc, "Precio Estimado:", 3);
        precioField.setEditable(false); // Campo no editable

        // Crear panel para botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        // Crear botones
        JButton estimarButton = new JButton("Estimar Precio", loadIcon("/resources/iconos/calculator.png"));
        styleButton(estimarButton, new Color(33, 37, 41), false);
        JButton crearButton = new JButton("Crear Licencia", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        JButton actualizarButton = new JButton("Actualizar Licencia", loadIcon("/resources/iconos/edit.png"));
        styleButton(actualizarButton, new Color(33, 37, 41), false);
        JButton eliminarButton = new JButton("Eliminar Licencia", loadIcon("/resources/iconos/delete.png"));
        styleButton(eliminarButton, new Color(211, 47, 47), true);

        // Agregar botones al panel
        buttonPanel.add(estimarButton);
        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);

        // Boton estimar precio
        estimarButton.addActionListener(event -> {
            // Validar campos
            if (!validateFields(letraField, clubField)) {
                showError("Letra y club son obligatorios.");
                return;
            }
            try {
                // Buscar equipo por letra y club
                Equipo equipo = Equipo.buscarPorLetraYClub(letraField.getText().trim(), clubField.getText().trim());
                if (equipo == null) {
                    showError("Equipo no encontrado.");
                    return;
                }
                // Calcular precio y mostrar
                double precio = federacion.calcularPrecioLicencia(equipo);
                precioField.setText(String.format("%.2f", precio));
            } catch (Exception ex) {
                handleError(ex, "Error al estimar precio.");
            }
        });

        // Boton crear licencia
        crearButton.addActionListener(event -> {
            // Validar campos
            if (!validateFields(dniField, letraField, clubField)) {
                return;
            }
            try {
                // Validar formato DNI
                if (!validateDni(dniField.getText())) {
                    showError("DNI invalido.");
                    return;
                }
                // Buscar persona por DNI
                Persona jugador = federacion.buscaPersona(dniField.getText().trim());
                if (jugador == null) {
                    showError("Jugador no encontrado.");
                    return;
                }
                // Buscar equipo por letra y club
                Equipo equipo = Equipo.buscarPorLetraYClub(letraField.getText().trim(), clubField.getText().trim());
                if (equipo == null) {
                    showError("Equipo no encontrado.");
                    return;
                }
                // Crear y asociar licencia
                Licencia licencia = federacion.nuevaLicencia(jugador, equipo);
                federacion.addLicencia(licencia, equipo);
                JOptionPane.showMessageDialog(frame, "Licencia creada para: " + jugador.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                clearFields(dniField, letraField, clubField, precioField);
            } catch (SQLException ex) {
                handleError(ex, "Error al guardar licencia.");
            }
        });

        // Boton actualizar licencia
        actualizarButton.addActionListener(event -> {
            // Validar campos
            if (!validateFields(dniField, letraField, clubField)) {
                return;
            }
            try {
                // Validar formato DNI
                if (!validateDni(dniField.getText())) {
                    showError("DNI invalido.");
                    return;
                }
                // Buscar persona por DNI
                Persona jugador = federacion.buscaPersona(dniField.getText().trim());
                if (jugador == null) {
                    showError("Jugador no encontrado.");
                    return;
                }
                // Buscar equipo por letra y club
                Equipo equipo = Equipo.buscarPorLetraYClub(letraField.getText().trim(), clubField.getText().trim());
                if (equipo == null) {
                    showError("Equipo no encontrado.");
                    return;
                }
                // Buscar licencia existente
                Licencia licencia = Licencia.buscarPorJugadorYEquipo(jugador.getDni(), equipo.getId());
                if (licencia == null) {
                    showError("Licencia no encontrada.");
                    return;
                }
                // Actualizar licencia
                licencia.setEquipo(equipo);
                licencia.actualizar();
                JOptionPane.showMessageDialog(frame, "Licencia actualizada para: " + jugador.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                clearFields(dniField, letraField, clubField, precioField);
            } catch (SQLException ex) {
                handleError(ex, "Error al actualizar licencia.");
            }
        });

        // Boton eliminar licencia
        eliminarButton.addActionListener(event -> {
            // Validar campos
            if (!validateFields(dniField, letraField, clubField)) {
                return;
            }
            try {
                // Validar formato DNI
                if (!validateDni(dniField.getText())) {
                    showError("DNI invalido.");
                    return;
                }
                // Buscar persona por DNI
                Persona jugador = federacion.buscaPersona(dniField.getText().trim());
                if (jugador == null) {
                    showError("Jugador no encontrado.");
                    return;
                }
                // Buscar equipo por letra y club
                Equipo equipo = Equipo.buscarPorLetraYClub(letraField.getText().trim(), clubField.getText().trim());
                if (equipo == null) {
                    showError("Equipo no encontrado.");
                    return;
                }
                // Buscar licencia existente
                Licencia licencia = Licencia.buscarPorJugadorYEquipo(jugador.getDni(), equipo.getId());
                if (licencia == null) {
                    showError("Licencia no encontrada.");
                    return;
                }
                // Eliminar licencia
                licencia.eliminar();
                JOptionPane.showMessageDialog(frame, "Licencia eliminada para: " + jugador.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar campos
                clearFields(dniField, letraField, clubField, precioField);
            } catch (SQLException ex) {
                handleError(ex, "Error al eliminar licencia.");
            }
        });

        // Configurar posicion de panel de botones
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        createPanel.add(buttonPanel, gbc);

        // Configurar posicion de panel de errores
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 10, 8, 10);
        createPanel.add(errorScrollPane, gbc);

        // Agregar subpanel al panel principal
        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }

    // Metodo para estilizar un menu
    private void styleMenu(JMenu menu, Font font, Color foreground, Color hover) {
        menu.setFont(font); // Establece la fuente
        menu.setForeground(foreground); // Establece el color del texto
        menu.setOpaque(true); // Habilita el fondo opaco
        menu.setBackground(new Color(33, 37, 41)); // Establece el color de fondo
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menu.setBackground(hover); // Cambia el color de fondo al pasar el raton
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menu.setBackground(new Color(33, 37, 41)); // Restaura el color de fondo
            }
        });
    }

    // Metodo para estilizar un elemento del menu
    private void styleMenuItem(JMenuItem item, Font font, Color foreground, Color hover) {
        item.setFont(font); // Establece la fuente
        item.setForeground(foreground); // Establece el color del texto
        item.setOpaque(true); // Habilita el fondo opaco
        item.setBackground(new Color(33, 37, 41)); // Establece el color de fondo
        item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Establece un borde vacio
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(hover); // Cambia el color de fondo al pasar el raton
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(33, 37, 41)); // Restaura el color de fondo
            }
        });
    }

    // Metodo para estilizar un boton
    private void styleButton(JButton button, Color background, boolean isDanger) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Establece la fuente
        button.setForeground(Color.BLACK); // Establece el color del texto
        button.setBackground(background); // Establece el color de fondo
        button.setFocusPainted(false); // Deshabilita el borde de foco
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // Establece un borde vacio
        Color hoverColor = isDanger ? new Color(198, 40, 40) : new Color(66, 66, 66); // Color al pasar el raton
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor); // Cambia el color de fondo al pasar el raton
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(background); // Restaura el color de fondo
            }
        });
    }

    // Metodo para crear una etiqueta estilizada
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text); // Crea una nueva etiqueta
        label.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Establece la fuente
        label.setForeground(new Color(33, 37, 41)); // Establece el color del texto
        return label; // Devuelve la etiqueta
    }

    // Metodo para crear una etiqueta de logo
    private JLabel createLogoLabel(String path) {
        JLabel logoLabel = new JLabel(); // Crea una nueva etiqueta
        ImageIcon icon = loadIcon(path); // Carga el icono
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH); // Escala a 48x48 (ajusta según necesites)
            logoLabel.setIcon(new ImageIcon(img)); // Establece el icono escalado
        }
        logoLabel.setBorder(new EmptyBorder(0, 0, 0, 20)); // Establece un borde vacio
        return logoLabel; // Devuelve la etiqueta
    }

    // Metodo para cargar un icono
    private ImageIcon loadIcon(String path) {
        URL imgURL = getClass().getResource(path); // Obtiene la URL del recurso
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL); // Crea el icono
            Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Escala la imagen
            return new ImageIcon(img); // Devuelve el icono escalado
        }
        System.err.println("No se pudo encontrar el icono: " + path);
        return null; // Devuelve null si no se encuentra el icono
    }

    // Metodo para establecer el icono de la ventana
    private void setFrameIcon(String path) {
        URL imgURL = getClass().getResource(path); // Obtiene la URL del recurso
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL); // Crea el icono
            frame.setIconImage(icon.getImage()); // Establece el icono de la ventana
        } else {
            System.err.println("No se pudo encontrar el icono: " + path);
        }
    }

    // Metodo para crear un panel con un titulo
    private JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout()); // Crea un nuevo panel con GridBagLayout
        panel.setBackground(Color.WHITE); // Establece el color de fondo
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED, 1), title,
                        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 16), new Color(33, 37, 41)),
                new EmptyBorder(10, 10, 10, 10))); // Establece un borde compuesto
        return panel; // Devuelve el panel
    }

    // Metodo para anadir un campo de texto a un panel
    private JTextField addField(JPanel panel, GridBagConstraints gbc, String label, int row) {
        JLabel fieldLabel = new JLabel(label); // Crea una nueva etiqueta
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
        fieldLabel.setForeground(new Color(33, 37, 41)); // Establece el color del texto
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(fieldLabel, gbc); // Anade la etiqueta al panel

        JTextField field = new JTextField(20); // Crea un nuevo campo de texto
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc); // Anade el campo de texto al panel
        return field; // Devuelve el campo de texto
    }

    // Sobrecarga del metodo para anadir un campo de texto con etiqueta de error
    private JTextField addField(JPanel panel, GridBagConstraints gbc, String label, int row, JLabel errorLabel) {
        JLabel fieldLabel = new JLabel(label); // Crea una nueva etiqueta
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
        fieldLabel.setForeground(new Color(33, 37, 41)); // Establece el color del texto
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(fieldLabel, gbc); // Anade la etiqueta al panel

        JTextField field = new JTextField(20); // Crea un nuevo campo de texto
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc); // Anade el campo de texto al panel

        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Establece la fuente de la etiqueta de error
        errorLabel.setForeground(Color.RED); // Establece el color del texto
        gbc.gridx = 1;
        gbc.gridy = row + 1;
        gbc.weightx = 1.0;
        panel.add(errorLabel, gbc); // Anade la etiqueta de error al panel
        return field; // Devuelve el campo de texto
    }

    // Metodo para anadir un combo box a un panel
    private JComboBox<entidades.Instalacion.TipoSuperficie> addComboBox(JPanel panel, GridBagConstraints gbc, String label, int row) {
        JLabel fieldLabel = new JLabel(label); // Crea una nueva etiqueta
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
        fieldLabel.setForeground(new Color(33, 37, 41)); // Establece el color del texto
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(fieldLabel, gbc); // Anade la etiqueta al panel

        JComboBox<entidades.Instalacion.TipoSuperficie> comboBox = new JComboBox<>(entidades.Instalacion.TipoSuperficie.values()); // Crea un nuevo combo box
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(comboBox, gbc); // Anade el combo box al panel
        return comboBox; // Devuelve el combo box
    }

    // Metodo para validar que los campos no esten vacios
    private boolean validateFields(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                showError("Todos los campos obligatorios deben estar completos.");
                return false;
            }
        }
        return true;
    }

    // Metodo para validar el formato del DNI
    private boolean validateDni(String dni) {
        return dni.matches("\\d{8}[A-Za-z]"); // Valida 8 digitos y 1 letra
    }

    // Metodo para validar el formato de la fecha
    private boolean validateDateFormat(String date) {
        try {
            LocalDate.parse(date); // Intenta parsear la fecha
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Metodo para limpiar los campos y el area de errores
    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText(""); // Limpia el texto del campo
        }
        errorArea.setText(""); // Limpia el area de errores
    }

    // Metodo para mostrar un error en el area de errores
    private void showError(String message) {
        errorArea.setText(errorArea.getText() + message + "\n");
        errorArea.setCaretPosition(errorArea.getDocument().getLength()); // Desplazar al final
    }

    // Metodo para manejar excepciones
    private void handleError(Exception ex, String message) {
        String errorMessage = message + " " + ex.getMessage();
        errorArea.setText(errorArea.getText() + errorMessage + "\n");
        errorArea.setCaretPosition(errorArea.getDocument().getLength());
        ex.printStackTrace();
    }
    
    // Metodo para mostrar una lista de resultados
    private void showListResult(List<?> list, String title) {
        System.out.println("Showing list result: " + list.size() + " items");
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No se encontraron resultados.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JTextArea textArea = new JTextArea(15, 50);
        textArea.setEditable(false);
        textArea.append(title + "\n\n");
        for (Object item : list) {
            textArea.append(item.toString() + "\n");
        }
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JDialog dialog = new JDialog(frame, "Resultados", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.add(scrollPane);
        dialog.setSize(650, 450); // Ligeramente más grande para bordes
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    // Metodo principal para iniciar la aplicacion
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Establece el look and feel del sistema
            } catch (Exception e) {
                e.printStackTrace(); // Imprime el stack trace en caso de error
            }
            new MainApp2().createAndShowGUI(); // Crea y muestra la interfaz grafica
        });
    }
}