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
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class MainApp2 {

    private IFederacion federacion; // Interfaz para la logica de la federacion
    private JFrame frame; // Ventana principal de la aplicacion
    private JPanel contentPanel; // Panel que contiene el contenido de la ventana
    private JMenuBar menuBar; // Barra de menu de la aplicacion

    public MainApp2() {
        federacion = Federacion.getInstance(); // Inicializa la instancia de la federacion
    }

    // Metodo para crear y mostrar la interfaz grafica
    private void createAndShowGUI() {
        frame = new JFrame("Proyecto FFCV - Gestion Federativa"); // Titulo de la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicacion al cerrar la ventana
        frame.setSize(1000, 750); // Tamaño de la ventana
        frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setFrameIcon("/resources/logo.png"); // Establece el icono de la ventana

        JPanel mainPanel = createGradientPanel(); // Crea el panel principal con un fondo degradado
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Establece un borde vacio alrededor del panel

        JPanel headerPanel = createHeaderPanel(); // Crea el panel de encabezado
        menuBar = createMenuBar(); // Crea la barra de menu
        headerPanel.add(menuBar, BorderLayout.SOUTH); // Añade la barra de menu al panel de encabezado

        contentPanel = new JPanel(new BorderLayout()); // Crea el panel de contenido
        contentPanel.setBackground(Color.WHITE); // Establece el color de fondo del panel de contenido
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Establece un borde vacio alrededor del panel de contenido
        contentPanel.add(createWelcomePanel(), BorderLayout.CENTER); // Añade el panel de bienvenida al panel de contenido

        mainPanel.add(headerPanel, BorderLayout.NORTH); // Añade el panel de encabezado al panel principal
        mainPanel.add(contentPanel, BorderLayout.CENTER); // Añade el panel de contenido al panel principal
        frame.add(mainPanel); // Añade el panel principal a la ventana

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

        JLabel headerLabel = new JLabel("Sistema de Gestion Federativa FFCV", SwingConstants.CENTER); // Etiqueta del encabezado
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Establece la fuente de la etiqueta
        headerLabel.setForeground(Color.WHITE); // Establece el color del texto de la etiqueta

        JLabel logoLabel = createLogoLabel("/resources/logo.png"); // Crea la etiqueta del logo
        headerPanel.add(logoLabel, BorderLayout.WEST); // Añade el logo al lado izquierdo del encabezado
        headerPanel.add(headerLabel, BorderLayout.CENTER); // Añade la etiqueta del encabezado al centro
        return headerPanel; // Devuelve el panel de encabezado
    }

    // Metodo para crear la barra de menu
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar(); // Crea la barra de menu
        menuBar.setBackground(new Color(33, 37, 41)); // Establece el color de fondo de la barra de menu
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Establece un borde vacio alrededor de la barra de menu

        Font menuFont = new Font("Segoe UI", Font.PLAIN, 16); // Fuente para los elementos del menu
        Color menuForeground = Color.WHITE; // Color del texto de los elementos del menu
        Color menuHover = new Color(66, 66, 66); // Color de fondo al pasar el raton sobre los elementos del menu

        JMenu gestionMenu = new JMenu("Gestion"); // Crea el menu de gestion
        styleMenu(gestionMenu, menuFont, menuForeground, menuHover); // Estiliza el menu
        gestionMenu.setIcon(loadIcon("/resources/iconos/gestion.png")); // Establece el icono del menu

        // Nombres de los elementos del menu
        String[] menuItems = {"Categorias", "Clubes", "Personas", "Empleados", "Instalaciones", "Grupos", "Equipos", "Licencias"};
        // Rutas de los iconos de los elementos del menu
        String[] iconPaths = {"categorias.png", "club.png", "persona.png", "empleado.png", "instalaciones.png", "grupos.png", "teams.png", "licencia.png"};
        JMenuItem[] items = new JMenuItem[menuItems.length]; // Array para almacenar los elementos del menu

        // Crea y añade los elementos del menu
        for (int i = 0; i < menuItems.length; i++) {
            items[i] = new JMenuItem(menuItems[i], loadIcon("/resources/iconos/" + iconPaths[i])); // Crea un nuevo elemento del menu
            styleMenuItem(items[i], menuFont, menuForeground, menuHover); // Estiliza el elemento del menu
            gestionMenu.add(items[i]); // Añade el elemento al menu de gestion
        }

        menuBar.add(gestionMenu); // Añade el menu de gestion a la barra de menu
        return menuBar; // Devuelve la barra de menu
    }

    // Metodo para configurar las acciones de los elementos del menu
    private void setupMenuActions() {
        JMenu gestionMenu = menuBar.getMenu(0); // Obtiene el primer menu de la barra de menu
        gestionMenu.getItem(0).addActionListener(e -> switchPanel(createCategoriaPanel())); // Accion para el item "Categorias"
        gestionMenu.getItem(1).addActionListener(e -> switchPanel(createClubPanel())); // Accion para el item "Clubes"
        gestionMenu.getItem(2).addActionListener(e -> switchPanel(createPersonaPanel())); // Accion para el item "Personas"
        gestionMenu.getItem(3).addActionListener(e -> switchPanel(createEmpleadoPanel())); // Accion para el item "Empleados"
        gestionMenu.getItem(4).addActionListener(e -> switchPanel(createInstalacionPanel())); // Accion para el item "Instalaciones"
        gestionMenu.getItem(5).addActionListener(e -> switchPanel(createGrupoPanel())); // Accion para el item "Grupos"
        gestionMenu.getItem(6).addActionListener(e -> switchPanel(createEquipoPanel())); // Accion para el item "Equipos"
        gestionMenu.getItem(7).addActionListener(e -> switchPanel(createLicenciaPanel())); // Accion para el item "Licencias"
    }

    // Metodo para cambiar el panel visible en el panel de contenido
    private void switchPanel(JPanel panel) {
        contentPanel.removeAll(); // Elimina todos los componentes del panel de contenido
        contentPanel.add(panel, BorderLayout.CENTER); // Añade el nuevo panel al centro del panel de contenido
        contentPanel.revalidate(); // Revalida el panel de contenido
        contentPanel.repaint(); // Repinta el panel de contenido
    }

    // Metodo para crear el panel de bienvenida
    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout()); // Crea el panel de bienvenida
        welcomePanel.setBackground(Color.WHITE); // Establece el color de fondo del panel de bienvenida

        JLabel welcomeLabel = new JLabel("Bienvenido al Sistema de Gestion FFCV", SwingConstants.CENTER); // Etiqueta de bienvenida
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Establece la fuente de la etiqueta
        welcomeLabel.setForeground(new Color(33, 37, 41)); // Establece el color del texto de la etiqueta

        JLabel infoLabel = new JLabel("<html><center>Seleccione una opcion del menu 'Gestion' para comenzar.<br>"
                + "Gestione categorias, clubes, personas, equipos y mas.</center></html>", SwingConstants.CENTER); // Etiqueta de informacion
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Establece la fuente de la etiqueta
        infoLabel.setForeground(new Color(66, 66, 66)); // Establece el color del texto de la etiqueta

        welcomePanel.add(welcomeLabel, BorderLayout.NORTH); // Añade la etiqueta de bienvenida al norte del panel
        welcomePanel.add(infoLabel, BorderLayout.CENTER); // Añade la etiqueta de informacion al centro del panel
        return welcomePanel; // Devuelve el panel de bienvenida
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

        // Campos para ingresar datos de la categoria
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 0); // Campo para el nombre de la categoria
        JTextField ordenField = addField(createPanel, gbc, "Nivel/Orden:", 1); // Campo para el nivel/orden de la categoria
        JTextField precioField = addField(createPanel, gbc, "Precio Licencia:", 2); // Campo para el precio de la licencia

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Panel para los botones
        buttonPanel.setBackground(Color.WHITE); // Establece el color de fondo del panel de botones

        // Botones para crear, actualizar y eliminar categorias
        JButton crearButton = new JButton("Crear Categoria", loadIcon("/resources/iconos/cross.png")); // Boton para crear categoria
        styleButton(crearButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Categoria", loadIcon("/resources/iconos/edit.png")); // Boton para actualizar categoria
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Categoria", loadIcon("/resources/iconos/delete.png")); // Boton para eliminar categoria
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton

        // Añade los botones al panel de botones
        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);

        // Accion para crear una nueva categoria
        crearButton.addActionListener(event -> {
            if (!validateFields(nombreField, ordenField, precioField)) {
                return; // Valida que los campos no estén vacíos
            }
            try {
                int orden = Integer.parseInt(ordenField.getText()); // Convierte el texto del campo de orden a entero
                double precio = Double.parseDouble(precioField.getText()); // Convierte el texto del campo de precio a double
                if (precio < 0) {
                    throw new IllegalArgumentException("El precio no puede ser negativo."); // Verifica que el precio no sea negativo
                }
                Categoria categoria = federacion.nuevaCategoria(nombreField.getText(), orden, precio); // Crea una nueva categoría
                JOptionPane.showMessageDialog(frame, "Categoría creada: " + categoria); // Muestra un mensaje de éxito
                clearFields(nombreField, ordenField, precioField); // Limpia los campos
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Nivel y precio deben ser numéricos.", "Error", JOptionPane.ERROR_MESSAGE); // Error de conversión numérica
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Error en parámetros
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Error de categoría existente
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Otros errores
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
                    JOptionPane.showMessageDialog(frame, "Categoria no encontrada.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra la categoria
                    return;
                }
                categoria.setOrden(Integer.parseInt(ordenField.getText())); // Actualiza el orden de la categoria
                categoria.setPrecioLicencia(Double.parseDouble(precioField.getText())); // Actualiza el precio de la categoria
                categoria.actualizar(); // Guarda los cambios en la categoria
                JOptionPane.showMessageDialog(frame, "Categoria actualizada: " + categoria); // Muestra un mensaje de exito
                clearFields(nombreField, ordenField, precioField); // Limpia los campos
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Nivel y precio deben ser numericos.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si la conversion falla
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
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
                    JOptionPane.showMessageDialog(frame, "Categoria no encontrada.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra la categoria
                    return;
                }
                categoria.eliminar(); // Elimina la categoria
                JOptionPane.showMessageDialog(frame, "Categoria eliminada: " + categoria); // Muestra un mensaje de exito
                clearFields(nombreField, ordenField, precioField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Configura la posicion del panel de botones
        createPanel.add(buttonPanel, gbc); // Añade el panel de botones al panel de crear/actualizar categoria

        JPanel listPanel = createTitledPanel("Consultar Categorias", new BorderLayout()); // Crea el panel para listar categorias
        JButton listarButton = new JButton("Listar Categorias", loadIcon("/resources/iconos/magnifier.png")); // Boton para listar categorias
        styleButton(listarButton, new Color(33, 37, 41), false); // Estiliza el boton
        listarButton.addActionListener(event -> listCategorias(listPanel)); // Accion para listar categorias
        listPanel.add(listarButton, BorderLayout.NORTH); // Añade el boton al panel de listar categorias

        panel.add(createPanel, BorderLayout.NORTH); // Añade el panel de crear/actualizar categoria al panel principal
        panel.add(listPanel, BorderLayout.CENTER); // Añade el panel de listar categorias al panel principal
        return panel; // Devuelve el panel de categorias
    }

    // Metodo para crear el panel de clubes
    private JPanel createClubPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel de clubes
        panel.setBackground(Color.WHITE); // Establece el color de fondo del panel
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Establece un borde vacio alrededor del panel

        JPanel createPanel = createTitledPanel("Gestion de Clubes"); // Crea el panel para gestionar clubes
        GridBagConstraints gbc = new GridBagConstraints(); // Configuracion de la cuadricula
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena el espacio horizontalmente

        // Campos para ingresar datos del club
        JTextField nombreField = addField(createPanel, gbc, "Nombre Club:", 0); // Campo para el nombre del club
        JTextField fechaField = addField(createPanel, gbc, "Fecha Fundacion (YYYY-MM-DD):", 1); // Campo para la fecha de fundacion
        JTextField dniPresField = addField(createPanel, gbc, "DNI Presidente:", 2); // Campo para el DNI del presidente
        JTextField buscarField = addField(createPanel, gbc, "Buscar Club por Nombre:", 3); // Campo para buscar clubes por nombre

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Panel para los botones
        buttonPanel.setBackground(Color.WHITE); // Establece el color de fondo del panel de botones

        // Botones para crear, actualizar, eliminar y buscar clubes
        JButton crearClubButton = new JButton("Crear Club", loadIcon("/resources/iconos/cross.png")); // Boton para crear club
        styleButton(crearClubButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Club", loadIcon("/resources/iconos/edit.png")); // Boton para actualizar club
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Club", loadIcon("/resources/iconos/delete.png")); // Boton para eliminar club
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton buscarButton = new JButton("Buscar Club", loadIcon("/resources/iconos/magnifier.png")); // Boton para buscar club
        styleButton(buscarButton, new Color(33, 37, 41), false); // Estiliza el boton

        // Añade los botones al panel de botones
        buttonPanel.add(crearClubButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(buscarButton);

        // Accion para crear un nuevo club
        crearClubButton.addActionListener(event -> {
            if (!validateFields(nombreField, fechaField, dniPresField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                if (!validateDateFormat(fechaField.getText())) {
                    throw new DateTimeParseException("Formato de fecha invalido.", fechaField.getText(), 0); // Verifica el formato de la fecha
                }
                if (!validateDni(dniPresField.getText())) {
                    throw new IllegalArgumentException("DNI invalido."); // Verifica el formato del DNI
                }
                LocalDate fecha = LocalDate.parse(fechaField.getText()); // Convierte el texto de la fecha a LocalDate
                Persona presidente = federacion.buscaPersona(dniPresField.getText()); // Busca al presidente por su DNI
                if (presidente == null) {
                    JOptionPane.showMessageDialog(frame, "Presidente no encontrado. Registrelo primero.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra el presidente
                    return;
                }
                Club club = federacion.nuevoClub(nombreField.getText(), fecha, presidente); // Crea un nuevo club
                JOptionPane.showMessageDialog(frame, "Club creado: " + club, "Éxito", JOptionPane.INFORMATION_MESSAGE); // Muestra un mensaje de exito
                clearFields(nombreField, fechaField, dniPresField); // Limpia los campos
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha invalido.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el formato de la fecha es invalido
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el DNI es invalido
            }
        });

        // Accion para actualizar un club existente
        actualizarButton.addActionListener(event -> {
            if (!validateFields(nombreField, fechaField, dniPresField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                Club club = federacion.buscarClub(nombreField.getText()); // Busca el club por nombre
                if (club == null) {
                    JOptionPane.showMessageDialog(frame, "Club no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra el club
                    return;
                }
                if (!validateDateFormat(fechaField.getText())) {
                    throw new DateTimeParseException("Formato de fecha invalido.", fechaField.getText(), 0); // Verifica el formato de la fecha
                }
                if (!validateDni(dniPresField.getText())) {
                    throw new IllegalArgumentException("DNI invalido."); // Verifica el formato del DNI
                }
                LocalDate fecha = LocalDate.parse(fechaField.getText()); // Convierte el texto de la fecha a LocalDate
                Persona presidente = federacion.buscaPersona(dniPresField.getText()); // Busca al presidente por su DNI
                if (presidente == null) {
                    JOptionPane.showMessageDialog(frame, "Presidente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra el presidente
                    return;
                }
                club.setFechaFundacion(fecha); // Actualiza la fecha de fundacion del club
                club.setPresidente(presidente); // Actualiza el presidente del club
                club.actualizar(); // Guarda los cambios en el club
                JOptionPane.showMessageDialog(frame, "Club actualizado: " + club); // Muestra un mensaje de exito
                clearFields(nombreField, fechaField, dniPresField); // Limpia los campos
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha invalido.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el formato de la fecha es invalido
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el DNI es invalido
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        // Accion para eliminar un club
        eliminarButton.addActionListener(event -> {
            if (!validateFields(nombreField)) {
                return; // Valida que el campo no este vacio
            }
            try {
                Club club = federacion.buscarClub(nombreField.getText()); // Busca el club por nombre
                if (club == null) {
                    JOptionPane.showMessageDialog(frame, "Club no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra el club
                    return;
                }
                club.eliminar(); // Elimina el club
                JOptionPane.showMessageDialog(frame, "Club eliminado: " + club); // Muestra un mensaje de exito
                clearFields(nombreField, fechaField, dniPresField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        // Accion para buscar un club por nombre
        buscarButton.addActionListener(event -> {
            if (!validateFields(buscarField)) {
                return; // Valida que el campo no este vacio
            }
            Club club = federacion.buscarClub(buscarField.getText()); // Busca el club por nombre
            JOptionPane.showMessageDialog(frame, club != null ? "Club encontrado: " + club : "Club no encontrado."); // Muestra un mensaje indicando si se encontro el club
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Configura la posicion del panel de botones
        createPanel.add(buttonPanel, gbc); // Añade el panel de botones al panel de crear/actualizar club

        JPanel listPanel = createTitledPanel("Listar Clubes", new BorderLayout()); // Crea el panel para listar clubes
        JButton listarButton = new JButton("Listar Clubes", loadIcon("/resources/iconos/magnifier.png")); // Boton para listar clubes
        styleButton(listarButton, new Color(33, 37, 41), false); // Estiliza el boton
        listarButton.addActionListener(event -> listClubes(listPanel)); // Accion para listar clubes
        listPanel.add(listarButton, BorderLayout.NORTH); // Añade el boton al panel de listar clubes

        panel.add(createPanel, BorderLayout.NORTH); // Añade el panel de crear/actualizar club al panel principal
        panel.add(listPanel, BorderLayout.CENTER); // Añade el panel de listar clubes al panel principal
        return panel; // Devuelve el panel de clubes
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

        // Campos para ingresar datos de la persona
        JTextField dniField = addField(createPanel, gbc, "DNI:", 0); // Campo para el DNI
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 1); // Campo para el nombre
        JTextField apellido1Field = addField(createPanel, gbc, "Apellido 1:", 2); // Campo para el primer apellido
        JTextField apellido2Field = addField(createPanel, gbc, "Apellido 2:", 3); // Campo para el segundo apellido
        JTextField fechaField = addField(createPanel, gbc, "Fecha Nacimiento (YYYY-MM-DD):", 4); // Campo para la fecha de nacimiento
        JTextField poblacionField = addField(createPanel, gbc, "Poblacion:", 5); // Campo para la poblacion
        JTextField usuarioField = addField(createPanel, gbc, "Usuario:", 6); // Campo para el usuario
        JTextField passwordField = addField(createPanel, gbc, "Contrasena:", 7); // Campo para la contrasena
        JTextField buscarDniField = addField(createPanel, gbc, "Buscar por DNI:", 8); // Campo para buscar personas por DNI

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
        JButton buscarMultiButton = new JButton("Buscar por Nombre y Apellidos", loadIcon("/resources/iconos/magnifier.png")); // Boton para buscar por nombre y apellidos
        styleButton(buscarMultiButton, new Color(33, 37, 41), false); // Estiliza el boton

        // Añade los botones al panel de botones
        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(buscarButton);
        buttonPanel.add(buscarMultiButton);

        // Accion para crear una nueva persona
        crearButton.addActionListener(event -> {
            if (!validateFields(dniField, nombreField, apellido1Field, fechaField, usuarioField, passwordField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                if (!validateDni(dniField.getText())) {
                    throw new IllegalArgumentException("DNI invalido."); // Verifica el formato del DNI
                }
                if (!validateDateFormat(fechaField.getText())) {
                    throw new DateTimeParseException("Formato de fecha invalido.", fechaField.getText(), 0); // Verifica el formato de la fecha
                }
                Persona persona = federacion.nuevaPersona(dniField.getText(), nombreField.getText(), apellido1Field.getText(),
                        apellido2Field.getText(), LocalDate.parse(fechaField.getText()), usuarioField.getText(),
                        passwordField.getText(), poblacionField.getText()); // Crea una nueva persona
                if (persona != null) {
                    JOptionPane.showMessageDialog(frame, "Persona creada: " + persona); // Muestra un mensaje de exito al crear la persona
                    clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, poblacionField, usuarioField, passwordField); // Limpia los campos
                } else {
                    JOptionPane.showMessageDialog(frame, "No se pudo crear la persona. Verifique el DNI.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se pudo crear la persona
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha invalido.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el formato de la fecha es invalido
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el DNI es invalido
            }
        });

        // Accion para actualizar una persona existente
        actualizarButton.addActionListener(event -> {
            if (!validateFields(dniField, nombreField, apellido1Field, fechaField, usuarioField, passwordField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                if (!validateDni(dniField.getText())) {
                    throw new IllegalArgumentException("DNI inválido."); // Verifica el formato del DNI
                }
                if (!validateDateFormat(fechaField.getText())) {
                    throw new DateTimeParseException("Formato de fecha inválido.", fechaField.getText(), 0); // Verifica el formato de la fecha
                }
                Persona persona = federacion.buscaPersona(dniField.getText()); // Busca la persona por su DNI
                if (persona == null) {
                    JOptionPane.showMessageDialog(frame, "Persona no encontrada.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra la persona
                    return;
                }
                // Actualiza los datos de la persona
                persona.setNombre(nombreField.getText());
                persona.setApellido1(apellido1Field.getText());
                persona.setApellido2(apellido2Field.getText());
                persona.setFechaNacimiento(LocalDate.parse(fechaField.getText()));
                persona.setPoblacion(poblacionField.getText());
                persona.setUsuario(usuarioField.getText());
                persona.setPassword(passwordField.getText());
                persona.actualizar(); // Guarda los cambios en la persona
                JOptionPane.showMessageDialog(frame, "Persona actualizada: " + persona); // Muestra un mensaje de exito
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, poblacionField, usuarioField, passwordField); // Limpia los campos
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha inválido.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el formato de la fecha es invalido
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el DNI es invalido
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        // Accion para eliminar una persona
        eliminarButton.addActionListener(event -> {
            if (!validateFields(dniField)) {
                return; // Valida que el campo no este vacio
            }
            try {
                if (!validateDni(dniField.getText())) {
                    throw new IllegalArgumentException("DNI inválido."); // Verifica el formato del DNI
                }
                Persona persona = federacion.buscaPersona(dniField.getText()); // Busca la persona por su DNI
                if (persona == null) {
                    JOptionPane.showMessageDialog(frame, "Persona no encontrada.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra la persona
                    return;
                }
                persona.eliminar(); // Elimina la persona
                JOptionPane.showMessageDialog(frame, "Persona eliminada: " + persona); // Muestra un mensaje de exito
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, poblacionField, usuarioField, passwordField); // Limpia los campos
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el DNI es invalido
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        // Accion para buscar una persona por DNI
        buscarButton.addActionListener(event -> {
            if (!validateFields(buscarDniField)) {
                return; // Valida que el campo no este vacio
            }
            try {
                if (!validateDni(buscarDniField.getText())) {
                    throw new IllegalArgumentException("DNI invalido."); // Verifica el formato del DNI
                }
                Persona persona = federacion.buscaPersona(buscarDniField.getText()); // Busca la persona por su DNI
                JOptionPane.showMessageDialog(frame, persona != null ? "Persona encontrada: " + persona : "Persona no encontrada."); // Muestra un mensaje indicando si se encontro la persona
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el DNI es invalido
            }
        });

        // Accion para buscar personas por nombre y apellidos
        buscarMultiButton.addActionListener(event -> {
            List<Persona> personas = federacion.buscaPersonas(nombreField.getText(), apellido1Field.getText(), apellido2Field.getText()); // Busca personas por nombre y apellidos
            showListResult(personas, "Personas encontradas:"); // Muestra los resultados de la busqueda
        });

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2; // Configura la posicion del panel de botones
        createPanel.add(buttonPanel, gbc); // Añade el panel de botones al panel de crear/actualizar persona

        panel.add(createPanel, BorderLayout.NORTH); // Añade el panel de crear/actualizar persona al panel principal
        return panel; // Devuelve el panel de personas
    }

    // Metodo para crear el panel de empleados
    private JPanel createEmpleadoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel de empleados
        panel.setBackground(Color.WHITE); // Establece el color de fondo del panel
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Establece un borde vacio alrededor del panel

        JPanel createPanel = createTitledPanel("Crear/Actualizar Empleado"); // Crea el panel para crear/actualizar empleados
        GridBagConstraints gbc = new GridBagConstraints(); // Configuracion de la cuadricula
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena el espacio horizontalmente

        // Campos para ingresar datos del empleado
        JTextField dniField = addField(createPanel, gbc, "DNI:", 0); // Campo para el DNI
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 1); // Campo para el nombre
        JTextField apellido1Field = addField(createPanel, gbc, "Apellido 1:", 2); // Campo para el primer apellido
        JTextField apellido2Field = addField(createPanel, gbc, "Apellido 2:", 3); // Campo para el segundo apellido
        JTextField fechaField = addField(createPanel, gbc, "Fecha Nacimiento (YYYY-MM-DD):", 4); // Campo para la fecha de nacimiento
        JTextField numEmpField = addField(createPanel, gbc, "Numero Empleado:", 5); // Campo para el numero de empleado
        JTextField inicioContratoField = addField(createPanel, gbc, "Inicio Contrato (YYYY-MM-DD):", 6); // Campo para la fecha de inicio del contrato
        JTextField segSocialField = addField(createPanel, gbc, "Seguridad Social:", 7); // Campo para el numero de seguridad social
        JTextField puestoField = addField(createPanel, gbc, "Puesto:", 8); // Campo para el puesto
        JTextField usuarioField = addField(createPanel, gbc, "Usuario:", 9); // Campo para el usuario
        JTextField passwordField = addField(createPanel, gbc, "Contrasena:", 10); // Campo para la contrasena
        JTextField poblacionField = addField(createPanel, gbc, "Poblacion:", 11); // Campo para la poblacion

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Panel para los botones
        buttonPanel.setBackground(Color.WHITE); // Establece el color de fondo del panel de botones

        // Botones para crear, actualizar y eliminar empleados
        JButton crearButton = new JButton("Crear Empleado", loadIcon("/resources/iconos/cross.png")); // Boton para crear empleado
        styleButton(crearButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Empleado", loadIcon("/resources/iconos/edit.png")); // Boton para actualizar empleado
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Empleado", loadIcon("/resources/iconos/delete.png")); // Boton para eliminar empleado
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton

        // Añade los botones al panel de botones
        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);

        // Accion para crear un nuevo empleado
        crearButton.addActionListener(event -> {
            if (!validateFields(dniField, nombreField, apellido1Field, fechaField, numEmpField, inicioContratoField, segSocialField, usuarioField, passwordField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                if (federacion.buscaPersona(dniField.getText()) != null) {
                    throw new IllegalArgumentException("El DNI ya está registrado."); // Verifica que el DNI no este ya registrado
                }
                if (!validateDni(dniField.getText())) {
                    throw new IllegalArgumentException("DNI inválido."); // Verifica el formato del DNI
                }
                if (!validateDateFormat(fechaField.getText()) || !validateDateFormat(inicioContratoField.getText())) {
                    throw new DateTimeParseException("Formato de fecha inválido.", "", 0); // Verifica el formato de las fechas
                }
                if (!segSocialField.getText().matches("\\d{2}\\d{8}\\d{2}")) {
                    throw new IllegalArgumentException("Seguridad Social debe seguir el formato: 2 digitos (provincia), 8 digitos (numero), 2 digitos (control)."); // Verifica el formato del numero de seguridad social
                }
                int numEmp = Integer.parseInt(numEmpField.getText()); // Convierte el texto del campo de numero de empleado a entero
                if (numEmp <= 0) {
                    throw new IllegalArgumentException("Número de empleado debe ser mayor a cero."); // Verifica que el numero de empleado sea mayor a cero
                }
                Empleado empleado = federacion.nuevoEmpleado(dniField.getText(), nombreField.getText(), apellido1Field.getText(),
                        apellido2Field.getText(), LocalDate.parse(fechaField.getText()), usuarioField.getText(),
                        passwordField.getText(), poblacionField.getText(), numEmp, LocalDate.parse(inicioContratoField.getText()),
                        segSocialField.getText()); // Crea un nuevo empleado
                if (empleado != null) {
                    empleado.setPuesto(puestoField.getText()); // Establece el puesto del empleado
                    empleado.actualizar(); // Guarda los cambios en el empleado
                    JOptionPane.showMessageDialog(frame, "Empleado creado: " + empleado); // Muestra un mensaje de exito
                    clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, numEmpField, inicioContratoField, segSocialField, puestoField, usuarioField, passwordField, poblacionField); // Limpia los campos
                } else {
                    JOptionPane.showMessageDialog(frame, "No se pudo crear el empleado. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se pudo crear el empleado
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha inválido.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el formato de la fecha es invalido
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Número de empleado debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el numero de empleado no es numerico
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con los datos ingresados
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        // Accion para actualizar un empleado existente
        actualizarButton.addActionListener(event -> {
            if (!validateFields(dniField, nombreField, apellido1Field, fechaField, numEmpField, inicioContratoField, segSocialField, usuarioField, passwordField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                if (!validateDni(dniField.getText())) {
                    throw new IllegalArgumentException("DNI invalido."); // Verifica el formato del DNI
                }
                if (!validateDateFormat(fechaField.getText()) || !validateDateFormat(inicioContratoField.getText())) {
                    throw new DateTimeParseException("Formato de fecha invalido.", "", 0); // Verifica el formato de las fechas
                }
                int numEmp = Integer.parseInt(numEmpField.getText()); // Convierte el texto del campo de numero de empleado a entero
                if (numEmp <= 0) {
                    throw new IllegalArgumentException("Numero de empleado debe ser mayor a cero."); // Verifica que el numero de empleado sea mayor a cero
                }
                Persona persona = federacion.buscaPersona(dniField.getText()); // Busca la persona por su DNI
                if (persona == null || !(persona instanceof Empleado)) {
                    JOptionPane.showMessageDialog(frame, "No se encontró un empleado con ese DNI.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra el empleado
                    return;
                }
                Empleado empleado = (Empleado) persona; // Convierte la persona a empleado
                // Actualiza los datos del empleado
                empleado.setNombre(nombreField.getText());
                empleado.setApellido1(apellido1Field.getText());
                empleado.setApellido2(apellido2Field.getText());
                empleado.setFechaNacimiento(LocalDate.parse(fechaField.getText()));
                empleado.setPoblacion(poblacionField.getText());
                empleado.setUsuario(usuarioField.getText());
                empleado.setPassword(passwordField.getText());
                empleado.setNumEmpleado(numEmp); // Establece el numero de empleado
                empleado.setInicioContrato(LocalDate.parse(inicioContratoField.getText())); // Establece la fecha de inicio del contrato
                empleado.setSegSocial(segSocialField.getText()); // Establece el numero de seguridad social
                empleado.setPuesto(puestoField.getText()); // Establece el puesto del empleado
                empleado.actualizar(); // Guarda los cambios en el empleado
                JOptionPane.showMessageDialog(frame, "Empleado actualizado: " + empleado); // Muestra un mensaje de exito
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, numEmpField, inicioContratoField, segSocialField, puestoField, usuarioField, passwordField, poblacionField); // Limpia los campos
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha invalido.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el formato de la fecha es invalido
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Numero de empleado debe ser numerico.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el numero de empleado no es numerico
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con los datos ingresados
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        // Accion para eliminar un empleado
        eliminarButton.addActionListener(event -> {
            if (!validateFields(dniField)) {
                return; // Valida que el campo no este vacio
            }
            try {
                if (!validateDni(dniField.getText())) {
                    throw new IllegalArgumentException("DNI invalido."); // Verifica el formato del DNI
                }
                Persona persona = federacion.buscaPersona(dniField.getText()); // Busca la persona por su DNI
                if (persona == null || !(persona instanceof Empleado)) {
                    JOptionPane.showMessageDialog(frame, "No se encontró un empleado con ese DNI.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra el empleado
                    return;
                }
                Empleado empleado = (Empleado) persona; // Convierte la persona a empleado
                empleado.eliminar(); // Elimina el empleado
                JOptionPane.showMessageDialog(frame, "Empleado eliminado: " + empleado); // Muestra un mensaje de exito
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, numEmpField, inicioContratoField, segSocialField, puestoField, usuarioField, passwordField, poblacionField); // Limpia los campos
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el DNI es invalido
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2; // Configura la posicion del panel de botones
        createPanel.add(buttonPanel, gbc); // Añade el panel de botones al panel de crear/actualizar empleado

        panel.add(createPanel, BorderLayout.NORTH); // Añade el panel de crear/actualizar empleado al panel principal
        return panel; // Devuelve el panel de empleados
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

        // Campos para ingresar datos de la instalacion
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 0); // Campo para el nombre
        JTextField direccionField = addField(createPanel, gbc, "Direccion:", 1); // Campo para la direccion
        JLabel superficieLabel = new JLabel("Tipo de Superficie:"); // Etiqueta para el tipo de superficie
        superficieLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente de la etiqueta
        JComboBox<Instalacion.TipoSuperficie> superficieCombo = new JComboBox<>(Instalacion.TipoSuperficie.values()); // ComboBox para seleccionar el tipo de superficie
        superficieCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente del ComboBox
        gbc.gridx = 0;
        gbc.gridy = 2;
        createPanel.add(superficieLabel, gbc); // Añade la etiqueta al panel
        gbc.gridx = 1;
        createPanel.add(superficieCombo, gbc); // Añade el ComboBox al panel
        JTextField buscarField = addField(createPanel, gbc, "Buscar por Nombre:", 3); // Campo para buscar instalaciones por nombre

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

        // Añade los botones al panel de botones
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
                Instalacion instalacion = federacion.nuevaInstalacion(nombreField.getText(), direccionField.getText(),
                        ((Instalacion.TipoSuperficie) superficieCombo.getSelectedItem()).name()); // Crea una nueva instalacion
                JOptionPane.showMessageDialog(frame, "Instalacion creada: " + instalacion); // Muestra un mensaje de exito
                clearFields(nombreField, direccionField); // Limpia los campos
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con los datos ingresados
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
                    JOptionPane.showMessageDialog(frame, "Instalacion no encontrada.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra la instalacion
                    return;
                }
                instalacion.setDireccion(direccionField.getText()); // Actualiza la direccion de la instalacion
                instalacion.setSuperficie((Instalacion.TipoSuperficie) superficieCombo.getSelectedItem()); // Actualiza el tipo de superficie
                instalacion.actualizar(); // Guarda los cambios en la instalacion
                JOptionPane.showMessageDialog(frame, "Instalacion actualizada: " + instalacion); // Muestra un mensaje de exito
                clearFields(nombreField, direccionField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
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
                    JOptionPane.showMessageDialog(frame, "Instalacion no encontrada.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra la instalacion
                    return;
                }
                instalacion.eliminar(); // Elimina la instalacion
                JOptionPane.showMessageDialog(frame, "Instalacion eliminada: " + instalacion); // Muestra un mensaje de exito
                clearFields(nombreField, direccionField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        // Accion para buscar instalaciones por nombre
        buscarButton.addActionListener(event -> {
            if (!validateFields(buscarField)) {
                return; // Valida que el campo no este vacio
            }
            List<Instalacion> instalaciones = federacion.buscarInstalaciones(buscarField.getText()); // Busca instalaciones por nombre
            showListResult(instalaciones, "Instalaciones encontradas:"); // Muestra los resultados de la busqueda
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Configura la posicion del panel de botones
        createPanel.add(buttonPanel, gbc); // Añade el panel de botones al panel de crear/actualizar instalacion

        panel.add(createPanel, BorderLayout.NORTH); // Añade el panel de crear/actualizar instalacion al panel principal
        return panel; // Devuelve el panel de instalaciones
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

        // Campos para ingresar datos del grupo
        JTextField categoriaField = addField(createPanel, gbc, "Nombre Categoria:", 0); // Campo para el nombre de la categoria
        JTextField nombreField = addField(createPanel, gbc, "Nombre Grupo:", 1); // Campo para el nombre del grupo

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

        // Añade los botones al panel de botones
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
                    JOptionPane.showMessageDialog(frame, "Categoria no encontrada.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra la categoria
                    return;
                }
                Grupo grupo = federacion.nuevoGrupo(categoria, nombreField.getText()); // Crea un nuevo grupo
                JOptionPane.showMessageDialog(frame, "Grupo creado: " + grupo); // Muestra un mensaje de exito
                clearFields(categoriaField, nombreField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
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
                    JOptionPane.showMessageDialog(frame, "Categoria no encontrada.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra la categoria
                    return;
                }
                Grupo grupo = Grupo.buscarPorNombre(nombreField.getText()); // Busca el grupo por nombre
                if (grupo == null) {
                    JOptionPane.showMessageDialog(frame, "Grupo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra el grupo
                    return;
                }
                grupo.setCategoria(categoria); // Actualiza la categoria del grupo
                grupo.actualizar(); // Guarda los cambios en el grupo
                JOptionPane.showMessageDialog(frame, "Grupo actualizado: " + grupo); // Muestra un mensaje de exito
                clearFields(categoriaField, nombreField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
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
                    JOptionPane.showMessageDialog(frame, "Grupo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra el grupo
                    return;
                }
                grupo.eliminar(); // Elimina el grupo
                JOptionPane.showMessageDialog(frame, "Grupo eliminado: " + grupo); // Muestra un mensaje de exito
                clearFields(categoriaField, nombreField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
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
                    JOptionPane.showMessageDialog(frame, "Categoria no encontrada.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra la categoria
                    return;
                }
                List<Grupo> grupos = federacion.obtenerGrupos(categoria); // Obtiene la lista de grupos en la categoria
                showListResult(grupos, "Grupos en " + categoria.getNombre() + ":"); // Muestra los resultados de la busqueda
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Configura la posicion del panel de botones
        createPanel.add(buttonPanel, gbc); // Añade el panel de botones al panel de crear/actualizar grupo

        panel.add(createPanel, BorderLayout.NORTH); // Añade el panel de crear/actualizar grupo al panel principal
        return panel; // Devuelve el panel de grupos
    }

    // Metodo para crear el panel de equipos
    private JPanel createEquipoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel principal para la gestion de equipos con un layout BorderLayout y un espacio de 10 pixeles entre componentes
        panel.setBackground(Color.WHITE); // Establece el color de fondo del panel a blanco
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Anade un borde vacio de 15 pixeles en todos los lados del panel

        JPanel createPanel = createTitledPanel("Crear/Actualizar Equipo y Buscar Jugador"); // Crea un subpanel titulado para la creacion, actualizacion y busqueda de equipos/jugadores
        GridBagConstraints gbc = new GridBagConstraints(); // Crea un objeto GridBagConstraints para configurar el layout del subpanel
        gbc.insets = new Insets(8, 10, 8, 10); // Establece margenes de 8 pixeles arriba/abajo y 10 pixeles izquierda/derecha entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Configura los componentes para que se expandan horizontalmente

        // Campos para ingresar datos del equipo
        JTextField letraField = addField(createPanel, gbc, "Letra:", 0); // Campo de texto para ingresar la letra del equipo, colocado en la fila 0
        JTextField instalacionField = addField(createPanel, gbc, "Nombre Instalacion:", 1); // Campo de texto para el nombre de la instalacion, colocado en la fila 1
        JTextField grupoField = addField(createPanel, gbc, "Nombre Grupo:", 2); // Campo de texto para el nombre del grupo, colocado en la fila 2
        JTextField clubField = addField(createPanel, gbc, "Nombre Club:", 3); // Campo de texto para el nombre del club, colocado en la fila 3

        JPanel buttonPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Crea un panel para los botones de crear/actualizar/eliminar con un layout centrado y espacios de 10 pixeles
        buttonPanel1.setBackground(Color.WHITE); // Establece el color de fondo del panel de botones a blanco

        JButton crearButton = new JButton("Crear Equipo", loadIcon("/resources/iconos/cross.png")); // Boton para crear un equipo con un icono de cruz
        styleButton(crearButton, new Color(211, 47, 47), true); // Estiliza el boton con color rojo y estilo de boton de creacion
        JButton actualizarButton = new JButton("Actualizar Equipo", loadIcon("/resources/iconos/edit.png")); // Boton para actualizar un equipo con un icono de edicion
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton con color gris oscuro y estilo de boton no-creacion
        JButton eliminarButton = new JButton("Eliminar Equipo", loadIcon("/resources/iconos/delete.png")); // Boton para eliminar un equipo con un icono de eliminacion
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton con color rojo y estilo de boton de creacion

        buttonPanel1.add(crearButton); // Anade el boton de crear al panel de botones
        buttonPanel1.add(actualizarButton); // Anade el boton de actualizar al panel de botones
        buttonPanel1.add(eliminarButton); // Anade el boton de eliminar al panel de botones

        gbc.gridx = 0; // Configura la columna del panel de botones
        gbc.gridy = 4; // Configura la fila del panel de botones
        gbc.gridwidth = 2; // Configura que el panel de botones ocupe 2 columnas
        createPanel.add(buttonPanel1, gbc); // Anade el panel de botones al subpanel de creacion/actualizacion

        // Campos para busqueda de jugador
        JTextField buscarLetraField = addField(createPanel, gbc, "Letra Eq. Busqueda:", 5); // Campo de texto para buscar equipos por letra, colocado en la fila 5
        JTextField dniField = addField(createPanel, gbc, "DNI Jugador:", 6); // Campo de texto para el DNI del jugador, colocado en la fila 6

        // Asegurar que el boton "Buscar Jugador" sea visible
        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Crea un segundo panel para el boton de busqueda con un layout centrado y espacios de 10 pixeles
        buttonPanel2.setBackground(Color.WHITE); // Establece el color de fondo del segundo panel de botones a blanco

        JButton buscarJugadorButton = new JButton("Buscar Jugador", loadIcon("/resources/iconos/magnifier.png")); // Boton para buscar un jugador con un icono de lupa
        styleButton(buscarJugadorButton, new Color(33, 37, 41), false); // Estiliza el boton con color gris oscuro y estilo de boton no-creacion

        buttonPanel2.add(buscarJugadorButton); // Anade el boton de buscar jugador al segundo panel de botones

        gbc.gridx = 0; // Configura la columna del segundo panel de botones
        gbc.gridy = 7; // Configura la fila del segundo panel de botones
        gbc.gridwidth = 2; // Configura que el segundo panel de botones ocupe 2 columnas
        createPanel.add(buttonPanel2, gbc); // Anade el segundo panel de botones al subpanel de creacion/actualizacion

        // Campos para anadir un jugador a un equipo
        JTextField letraEquipoField = addField(createPanel, gbc, "Añadir jugador (Letra):", 8); // Campo de texto para la letra del equipo, colocado en la fila 8
        JTextField dniJugadorField = addField(createPanel, gbc, "Añadir jugador (DNI):", 9); // Campo de texto para el DNI del jugador, colocado en la fila 9

        JPanel buttonPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Crea un tercer panel para el boton de anadir jugador con un layout centrado y espacios de 10 pixeles
        buttonPanel3.setBackground(Color.WHITE); // Establece el color de fondo del tercer panel de botones a blanco

        JButton anadirJugadorButton = new JButton("Anadir Jugador a Equipo", loadIcon("/resources/iconos/cross.png")); // Boton para anadir un jugador con un icono de cruz
        styleButton(anadirJugadorButton, new Color(211, 47, 47), true); // Estiliza el boton con color rojo y estilo de boton de creacion

        buttonPanel3.add(anadirJugadorButton); // Anade el boton de anadir jugador al tercer panel de botones

        gbc.gridx = 0; // Configura la columna del tercer panel de botones
        gbc.gridy = 10; // Configura la fila del tercer panel de botones
        gbc.gridwidth = 2; // Configura que el tercer panel de botones ocupe 2 columnas
        createPanel.add(buttonPanel3, gbc); // Anade el tercer panel de botones al subpanel de creacion/actualizacion

        // Acciones de los botones
        crearButton.addActionListener(event -> { // Anade un listener al boton de crear equipo
            if (!validateFields(letraField, instalacionField, grupoField, clubField)) { // Valida que todos los campos necesarios esten llenos
                return; // Si la validacion falla, termina la ejecucion del evento
            }
            try {
                Instalacion instalacion = Instalacion.buscarPorNombre(instalacionField.getText()); // Busca la instalacion por el nombre ingresado
                if (instalacion == null) { // Verifica si la instalacion existe
                    JOptionPane.showMessageDialog(frame, "Instalacion no encontrada. Cree una primero.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra
                    return; // Termina la ejecucion del evento
                }
                Grupo grupo = Grupo.buscarPorNombre(grupoField.getText()); // Busca el grupo por el nombre ingresado
                if (grupo == null) { // Verifica si el grupo existe
                    JOptionPane.showMessageDialog(frame, "Grupo no encontrado. Cree uno primero.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra
                    return; // Termina la ejecucion del evento
                }
                Club club = federacion.buscarClub(clubField.getText()); // Busca el club por el nombre ingresado
                if (club == null) { // Verifica si el club existe
                    JOptionPane.showMessageDialog(frame, "Club no encontrado. Cree uno primero.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra
                    return; // Termina la ejecucion del evento
                }
                Equipo equipo = federacion.nuevoEquipo(letraField.getText(), instalacion, grupo, club); // Crea un nuevo equipo con los datos ingresados
                JOptionPane.showMessageDialog(frame, "Equipo creado: " + equipo); // Muestra un mensaje de exito con los datos del equipo creado
                clearFields(letraField, instalacionField, grupoField, clubField); // Limpia los campos de texto despues de crear el equipo
            } catch (SQLException ex) { // Captura excepciones de SQL
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        actualizarButton.addActionListener(event -> { // Anade un listener al boton de actualizar equipo
            if (!validateFields(letraField, instalacionField, grupoField, clubField)) { // Valida que todos los campos necesarios esten llenos
                return; // Si la validacion falla, termina la ejecucion del evento
            }
            try {
                Equipo equipo = Equipo.buscarPorLetra(letraField.getText()); // Busca el equipo por la letra ingresada
                if (equipo == null) { // Verifica si el equipo existe
                    JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra
                    return; // Termina la ejecucion del evento
                }
                Instalacion instalacion = Instalacion.buscarPorNombre(instalacionField.getText()); // Busca la instalacion por el nombre ingresado
                if (instalacion == null) { // Verifica si la instalacion existe
                    JOptionPane.showMessageDialog(frame, "Instalacion no encontrada.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra
                    return; // Termina la ejecucion del evento
                }
                Grupo grupo = Grupo.buscarPorNombre(grupoField.getText()); // Busca el grupo por el nombre ingresado
                if (grupo == null) { // Verifica si el grupo existe
                    JOptionPane.showMessageDialog(frame, "Grupo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra
                    return; // Termina la ejecucion del evento
                }
                Club club = federacion.buscarClub(clubField.getText()); // Busca el club por el nombre ingresado
                if (club == null) { // Verifica si el club existe
                    JOptionPane.showMessageDialog(frame, "Club no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra
                    return; // Termina la ejecucion del evento
                }
                equipo.setInstalacion(instalacion); // Actualiza la instalacion del equipo
                equipo.setGrupo(grupo); // Actualiza el grupo del equipo
                equipo.setClubId(club.obtenerIdClub()); // Actualiza el ID del club del equipo
                equipo.actualizar(); // Guarda los cambios del equipo en la base de datos
                JOptionPane.showMessageDialog(frame, "Equipo actualizado: " + equipo); // Muestra un mensaje de exito con los datos del equipo actualizado
                clearFields(letraField, instalacionField, grupoField, clubField); // Limpia los campos de texto despues de actualizar
            } catch (SQLException ex) { // Captura excepciones de SQL
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        eliminarButton.addActionListener(event -> { // Anade un listener al boton de eliminar equipo
            if (!validateFields(letraField)) { // Valida que el campo de letra no este vacio
                return; // Si la validacion falla, termina la ejecucion del evento
            }
            try {
                Equipo equipo = Equipo.buscarPorLetra(letraField.getText()); // Busca el equipo por la letra ingresada
                if (equipo == null) { // Verifica si el equipo existe
                    JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra
                    return; // Termina la ejecucion del evento
                }
                equipo.eliminar(); // Elimina el equipo de la base de datos
                JOptionPane.showMessageDialog(frame, "Equipo eliminado: " + equipo); // Muestra un mensaje de exito con los datos del equipo eliminado
                clearFields(letraField, instalacionField, grupoField, clubField); // Limpia los campos de texto despues de eliminar
            } catch (SQLException ex) { // Captura excepciones de SQL
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        buscarJugadorButton.addActionListener(event -> { // Anade un listener al boton de buscar jugador
            if (!validateFields(buscarLetraField, dniField)) { // Valida que los campos de busqueda no esten vacios
                return; // Si la validacion falla, termina la ejecucion del evento
            }
            try {
                if (!validateDni(dniField.getText())) { // Valida que el DNI ingresado tenga un formato valido
                    throw new IllegalArgumentException("DNI invalido."); // Lanza una excepcion si el DNI no es valido
                }
                Equipo equipo = Equipo.buscarPorLetra(buscarLetraField.getText()); // Busca el equipo por la letra ingresada
                if (equipo == null) { // Verifica si el equipo existe
                    JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra
                    return; // Termina la ejecucion del evento
                }
                Persona jugador = equipo.buscarJugador(dniField.getText()); // Busca al jugador por DNI dentro del equipo
                JOptionPane.showMessageDialog(frame, jugador != null ? "Jugador encontrado: " + jugador : "Jugador no encontrado en el equipo."); // Muestra un mensaje indicando si se encontro o no al jugador
                clearFields(buscarLetraField, dniField); // Limpia los campos de busqueda despues de buscar
            } catch (IllegalArgumentException ex) { // Captura excepciones de argumentos invalidos
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el DNI es invalido
            } catch (SQLException ex) { // Captura excepciones de SQL
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        anadirJugadorButton.addActionListener(event -> { // Anade un listener al boton de anadir jugador
            if (!validateFields(letraEquipoField, dniJugadorField)) { // Valida que los campos de anadir no esten vacios
                return; // Si la validacion falla, termina la ejecucion del evento
            }
            try {
                if (!validateDni(dniJugadorField.getText())) { // Valida que el DNI ingresado tenga un formato valido
                    throw new IllegalArgumentException("DNI invalido."); // Lanza una excepcion si el DNI no es valido
                }
                Equipo equipo = Equipo.buscarPorLetra(letraEquipoField.getText()); // Busca el equipo por la letra ingresada
                if (equipo == null) { // Verifica si el equipo existe
                    JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra
                    return; // Termina la ejecucion del evento
                }
                Persona jugador = federacion.buscaPersona(dniJugadorField.getText()); // Busca al jugador por DNI
                if (jugador == null) { // Verifica si el jugador existe
                    JOptionPane.showMessageDialog(frame, "Jugador no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra
                    return; // Termina la ejecucion del evento
                }
                Licencia licencia = federacion.nuevaLicencia(jugador, equipo); // Crea una nueva licencia asociada al jugador y al equipo
                federacion.addLicencia(licencia, equipo); // Anade la licencia y al jugador al equipo
                JOptionPane.showMessageDialog(frame, "Jugador anadido al equipo: " + jugador); // Muestra un mensaje de exito
                clearFields(letraEquipoField, dniJugadorField); // Limpia los campos despues de anadir
            } catch (IllegalArgumentException ex) { // Captura excepciones de argumentos invalidos
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si el DNI es invalido
            } catch (SQLException ex) { // Captura excepciones de SQL
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        panel.add(createPanel, BorderLayout.NORTH); // Anade el subpanel de creacion/actualizacion/busqueda al panel principal en la posicion norte
        return panel; // Devuelve el panel principal de equipos
    }

    // Metodo para crear el panel de licencias
    private JPanel createLicenciaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel de licencias
        panel.setBackground(Color.WHITE); // Establece el color de fondo del panel
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Establece un borde vacio alrededor del panel

        JPanel createPanel = createTitledPanel("Gestionar Licencias"); // Crea el panel para gestionar licencias
        GridBagConstraints gbc = new GridBagConstraints(); // Configuracion de la cuadricula
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena el espacio horizontalmente

        // Campos para ingresar datos de la licencia
        JTextField dniField = addField(createPanel, gbc, "DNI Persona:", 0); // Campo para el DNI de la persona
        JTextField equipoField = addField(createPanel, gbc, "Letra Equipo:", 1); // Campo para la letra del equipo
        JTextField calcularField = addField(createPanel, gbc, "Letra Equipo para Precio:", 2); // Campo para calcular el precio de la licencia

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Panel para los botones
        buttonPanel.setBackground(Color.WHITE); // Establece el color de fondo del panel de botones

        // Botones para crear licencias simples, crear licencias con equipo y calcular precio de licencia
        JButton crearSimpleButton = new JButton("Crear Licencia Simple", loadIcon("/resources/iconos/cross.png")); // Boton para crear licencia simple
        styleButton(crearSimpleButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton crearEquipoButton = new JButton("Crear Licencia con Equipo", loadIcon("/resources/iconos/cross.png")); // Boton para crear licencia con equipo
        styleButton(crearEquipoButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton calcularButton = new JButton("Calcular Precio Licencia", loadIcon("/resources/iconos/precio_licencia.png")); // Boton para calcular precio de licencia
        styleButton(calcularButton, new Color(33, 37, 41), false); // Estiliza el boton

        // Añade los botones al panel de botones
        buttonPanel.add(crearSimpleButton);
        buttonPanel.add(crearEquipoButton);
        buttonPanel.add(calcularButton);

        // Accion para crear una licencia simple
        crearSimpleButton.addActionListener(event -> {
            if (!validateFields(dniField)) {
                return; // Valida que el campo no este vacio
            }
            try {
                if (!validateDni(dniField.getText())) {
                    throw new IllegalArgumentException("DNI invalido."); // Verifica el formato del DNI
                }
                Persona persona = federacion.buscaPersona(dniField.getText()); // Busca la persona por su DNI
                if (persona == null) {
                    JOptionPane.showMessageDialog(frame, "Persona no encontrada.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra la persona
                    return;
                }
                Licencia licencia = federacion.nuevaLicencia(persona); // Crea una nueva licencia simple
                JOptionPane.showMessageDialog(frame, "Licencia creada: " + licencia); // Muestra un mensaje de exito
                clearFields(dniField); // Limpia los campos
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con los datos ingresados
            }
        });

        // Accion para crear una licencia con equipo
        crearEquipoButton.addActionListener(event -> {
            if (!validateFields(dniField, equipoField)) {
                return; // Valida que los campos no esten vacios
            }
            try {
                if (!validateDni(dniField.getText())) {
                    throw new IllegalArgumentException("DNI invalido."); // Verifica el formato del DNI
                }
                Persona persona = federacion.buscaPersona(dniField.getText()); // Busca la persona por su DNI
                Equipo equipo = Equipo.buscarPorLetra(equipoField.getText()); // Busca el equipo por letra
                if (persona == null || equipo == null) {
                    JOptionPane.showMessageDialog(frame, persona == null ? "Persona no encontrada." : "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra la persona o el equipo
                    return;
                }
                Licencia licencia = federacion.nuevaLicencia(persona, equipo); // Crea una nueva licencia con equipo
                JOptionPane.showMessageDialog(frame, "Licencia con equipo creada: " + licencia); // Muestra un mensaje de exito
                clearFields(dniField, equipoField); // Limpia los campos
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con los datos ingresados
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        // Accion para calcular el precio de la licencia
        calcularButton.addActionListener(event -> {
            if (!validateFields(calcularField)) {
                return; // Valida que el campo no este vacio
            }
            try {
                Equipo equipo = Equipo.buscarPorLetra(calcularField.getText()); // Busca el equipo por letra
                if (equipo == null) {
                    JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se encuentra el equipo
                    return;
                }
                double precio = federacion.calcularPrecioLicencia(equipo); // Calcula el precio de la licencia
                JOptionPane.showMessageDialog(frame, "Precio de la licencia: " + precio); // Muestra el precio de la licencia
                clearFields(calcularField); // Limpia el campo
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Configura la posicion del panel de botones
        createPanel.add(buttonPanel, gbc); // Añade el panel de botones al panel de gestionar licencias

        panel.add(createPanel, BorderLayout.NORTH); // Añade el panel de gestionar licencias al panel principal
        return panel; // Devuelve el panel de licencias
    }

    // Metodo para establecer el icono de la ventana
    private void setFrameIcon(String path) {
        ImageIcon icon = loadIcon(path); // Carga el icono desde la ruta especificada
        if (icon != null) {
            frame.setIconImage(icon.getImage()); // Establece el icono de la ventana si se carga correctamente
        }
    }

    // Metodo para crear una etiqueta con el logo
    private JLabel createLogoLabel(String path) {
        ImageIcon icon = loadIcon(path); // Carga el icono desde la ruta especificada
        if (icon != null) {
            Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Escala la imagen del logo
            return new JLabel(new ImageIcon(scaledImage)); // Devuelve una etiqueta con el logo escalado
        }
        return new JLabel(); // Devuelve una etiqueta vacia si no se carga el icono
    }

    // Metodo para cargar un icono desde una ruta
    private ImageIcon loadIcon(String path) {
        try {
            URL resource = getClass().getResource(path); // Obtiene la URL del recurso
            if (resource == null) {
                throw new IllegalArgumentException("Recurso no encontrado: " + path); // Lanza una excepcion si no se encuentra el recurso
            }
            return new ImageIcon(resource); // Devuelve el icono cargado
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage()); // Imprime el mensaje de error en la consola
            return null; // Devuelve null si hay un error
        }
    }

    // Metodo para crear un panel con un titulo
    private JPanel createTitledPanel(String title, LayoutManager layout) {
        JPanel panel = new JPanel(layout); // Crea un nuevo panel con el layout especificado
        panel.setBackground(Color.WHITE); // Establece el color de fondo del panel
        panel.setBorder(BorderFactory.createTitledBorder( // Establece un borde titulado para el panel
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                title, TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), new Color(33, 37, 41))); // Establece el estilo del borde
        return panel; // Devuelve el panel creado
    }

    // Metodo para crear un panel titulado sin layout especificado
    private JPanel createTitledPanel(String title) {
        return createTitledPanel(title, new GridBagLayout()); // Devuelve un panel titulado con un layout de GridBagLayout
    }

    // Metodo para añadir un campo de texto al panel
    private JTextField addField(JPanel panel, GridBagConstraints gbc, String labelText, int row) {
        JLabel label = new JLabel(labelText); // Crea una etiqueta con el texto especificado
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente de la etiqueta
        JTextField field = new JTextField(20); // Crea un campo de texto con un tamaño de 20 columnas
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente del campo de texto
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc); // Añade la etiqueta al panel en la posicion especificada
        gbc.gridx = 1;
        panel.add(field, gbc); // Añade el campo de texto al panel en la posicion especificada
        return field; // Devuelve el campo de texto creado
    }

    // Metodo para validar que los campos no esten vacios
    private boolean validateFields(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Todos los campos deben estar llenos.", "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay campos vacios
                return false; // Devuelve false si hay campos vacios
            }
        }
        return true; // Devuelve true si todos los campos son validos
    }

    // Metodo para validar el formato del DNI
    private boolean validateDni(String dni) {
        return Pattern.matches("\\d{8}[A-Za-z]", dni); // Verifica que el DNI tenga 8 digitos seguidos de una letra
    }

    // Metodo para validar el formato de la fecha
    private boolean validateDateFormat(String date) {
        if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", date)) {
            return false; // Devuelve false si el formato no es correcto
        }
        try {
            LocalDate.parse(date); // Intenta parsear la fecha
            return true; // Devuelve true si el formato es correcto
        } catch (DateTimeParseException e) {
            return false; // Devuelve false si hay un error al parsear la fecha
        }
    }

    // Metodo para listar categorias
    private void listCategorias(JPanel listPanel) {
        List<Categoria> categorias = federacion.obtenerCategorias(); // Obtiene la lista de categorias
        JTextArea textArea = new JTextArea(10, 30); // Crea un area de texto para mostrar las categorias
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente del area de texto
        textArea.setText("Categorias:\n"); // Establece el texto inicial del area de texto
        for (Categoria c : categorias) {
            textArea.append(c.toString() + "\n"); // Añade cada categoria al area de texto
        }
        textArea.setEditable(false); // Hace que el area de texto no sea editable
        listPanel.removeAll(); // Elimina todos los componentes del panel de lista
        JButton listarButton = new JButton("Listar Categorias", loadIcon("/resources/iconos/magnifier.png")); // Boton para listar categorias
        styleButton(listarButton, new Color(33, 37, 41), false); // Estiliza el boton
        listarButton.addActionListener(event -> listCategorias(listPanel)); // Accion para listar categorias
        listPanel.add(listarButton, BorderLayout.NORTH); // Añade el boton al panel de lista
        listPanel.add(new JScrollPane(textArea), BorderLayout.CENTER); // Añade el area de texto al panel de lista
        listPanel.revalidate(); // Revalida el panel de lista
        listPanel.repaint(); // Repinta el panel de lista
    }

    // Metodo para listar clubes
    private void listClubes(JPanel listPanel) {
        try {
            List<Club> clubes = Club.obtenerTodos(); // Obtiene la lista de clubes
            JTextArea textArea = new JTextArea(10, 30); // Crea un area de texto para mostrar los clubes
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente del area de texto
            textArea.setText("Clubes:\n"); // Establece el texto inicial del area de texto
            for (Club c : clubes) {
                textArea.append(c.toString() + "\n"); // Añade cada club al area de texto
            }
            textArea.setEditable(false); // Hace que el area de texto no sea editable
            listPanel.removeAll(); // Elimina todos los componentes del panel de lista
            JButton listarButton = new JButton("Listar Clubes", loadIcon("/resources/iconos/magnifier.png")); // Boton para listar clubes
            styleButton(listarButton, new Color(33, 37, 41), false); // Estiliza el boton
            listarButton.addActionListener(event -> listClubes(listPanel)); // Accion para listar clubes
            listPanel.add(listarButton, BorderLayout.NORTH); // Añade el boton al panel de lista
            listPanel.add(new JScrollPane(textArea), BorderLayout.CENTER); // Añade el area de texto al panel de lista
            listPanel.revalidate(); // Revalida el panel de lista
            listPanel.repaint(); // Repinta el panel de lista
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si hay un problema con la base de datos
        }
    }

    // Metodo para mostrar resultados de busqueda
    private void showListResult(List<?> list, String title) {
        JTextArea textArea = new JTextArea(10, 30); // Crea un area de texto para mostrar los resultados
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente del area de texto
        textArea.setText(title + "\n"); // Establece el texto inicial del area de texto
        for (Object item : list) {
            textArea.append(item.toString() + "\n"); // Añade cada item de la lista al area de texto
        }
        textArea.setEditable(false); // Hace que el area de texto no sea editable
        JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Resultados", JOptionPane.INFORMATION_MESSAGE); // Muestra los resultados en un cuadro de dialogo
    }

    // Metodo para estilizar un menu
    private void styleMenu(JMenu menu, Font font, Color foreground, Color hoverColor) {
        menu.setFont(font); // Establece la fuente del menu
        menu.setForeground(foreground); // Establece el color del texto del menu
        menu.setOpaque(true); // Hace que el menu sea opaco
        menu.setBackground(new Color(33, 37, 41)); // Establece el color de fondo del menu
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor al pasar sobre el menu
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menu.setBackground(hoverColor);
            } // Cambia el color de fondo al pasar el raton

            @Override
            public void mouseExited(MouseEvent e) {
                menu.setBackground(new Color(33, 37, 41));
            } // Restaura el color de fondo al salir el raton
        });
    }

    // Metodo para estilizar un item de menu
    private void styleMenuItem(JMenuItem item, Font font, Color foreground, Color hoverColor) {
        item.setFont(font); // Establece la fuente del item
        item.setForeground(foreground); // Establece el color del texto del item
        item.setBackground(new Color(33, 37, 41)); // Establece el color de fondo del item
        item.setOpaque(true); // Hace que el item sea opaco
        item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Establece un borde vacio alrededor del item
        item.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor al pasar sobre el item
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(hoverColor);
            } // Cambia el color de fondo al pasar el raton

            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(33, 37, 41));
            } // Restaura el color de fondo al salir el raton
        });
    }

    // Metodo para estilizar un boton
    private void styleButton(JButton button, Color bgColor, boolean isCreateButton) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Establece la fuente del boton
        button.setBackground(bgColor); // Establece el color de fondo del boton
        button.setForeground(Color.WHITE); // Establece el color del texto del boton
        button.setFocusPainted(false); // Desactiva el borde de enfoque
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25)); // Establece un borde vacio alrededor del boton
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor al pasar sobre el boton
        Color hoverColor = isCreateButton ? new Color(239, 83, 80) : new Color(66, 66, 66); // Establece el color de fondo al pasar el raton dependiendo si es un boton de crear
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            } // Cambia el color de fondo al pasar el raton

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            } // Restaura el color de fondo al salir el raton
        });
    }

    // Metodo para limpiar los campos de texto
    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText(""); // Establece el texto de cada campo a vacio
        }
    }

    // Metodo principal que inicia la aplicacion
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp2().createAndShowGUI()); // Ejecuta la creacion y visualizacion de la GUI en el hilo de eventos de Swing
    }
}