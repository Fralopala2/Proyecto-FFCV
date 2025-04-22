package proyectoffcv.ui;

// Importaciones necesarias para la interfaz grafica y manejo de datos
import entidades.*; // Importa todas las clases de entidades
import proyectoffcv.logica.Federacion; // Clase Federacion para la logica del negocio
import proyectoffcv.logica.IFederacion; // Interfaz para Federacion
import java.time.LocalDate; // Para manejar fechas
import java.time.format.DateTimeParseException; // Excepcion para errores en formato de fecha
import javax.swing.*; // Componentes de la interfaz grafica
import javax.swing.border.EmptyBorder; // Borde vacio para paneles
import javax.swing.border.TitledBorder; // Borde con titulo
import java.awt.*; // Componentes graficos basicos
import java.awt.event.MouseAdapter; // Adaptador para eventos de raton
import java.awt.event.MouseEvent; // Evento de raton
import java.net.URL; // Para manejar recursos como imagenes
import java.sql.SQLException; // Excepcion para errores de base de datos
import java.util.List; // Para manejar listas de objetos
import java.util.regex.Pattern; // Para validaciones con expresiones regulares

// Suprime advertencias de codigo no utilizado
@SuppressWarnings("unused")
public class MainApp2 {

    // Atributos de la clase
    private IFederacion federacion; // Instancia de la interfaz IFederacion
    private JFrame frame; // Ventana principal de la aplicacion
    private JPanel contentPanel; // Panel donde se muestra el contenido dinamico
    private JMenuBar menuBar; // Barra de menus

    // Constructor de la clase
    public MainApp2() {
        federacion = Federacion.getInstance(); // Obtiene la instancia unica de Federacion (patron Singleton)
    }

    // Metodo para crear y mostrar la interfaz grafica
    private void createAndShowGUI() {
        frame = new JFrame("Proyecto FFCV - Gestion Federativa"); // Crea la ventana principal
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicacion al cerrar la ventana
        frame.setSize(1000, 750); // Establece el tamano de la ventana
        frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setFrameIcon("/resources/logo.png"); // Establece el icono de la ventana

        // Crea el panel principal con fondo degradado
        JPanel mainPanel = createGradientPanel();
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Agrega un borde vacio al panel

        // Crea el panel de cabecera
        JPanel headerPanel = createHeaderPanel();
        menuBar = createMenuBar(); // Crea la barra de menus
        headerPanel.add(menuBar, BorderLayout.SOUTH); // Agrega la barra de menus al panel de cabecera

        // Crea el panel de contenido
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE); // Establece el fondo blanco
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Agrega un borde vacio
        contentPanel.add(createWelcomePanel(), BorderLayout.CENTER); // Agrega el panel de bienvenida

        // Organiza los paneles en el panel principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        frame.add(mainPanel); // Agrega el panel principal a la ventana

        setupMenuActions(); // Configura las acciones de los menus

        frame.setVisible(true); // Hace visible la ventana
    }

    // Crea un panel con fondo degradado
    private JPanel createGradientPanel() {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g; // Convierte Graphics a Graphics2D para efectos avanzados
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); // Mejora la calidad del renderizado
                int w = getWidth(), h = getHeight(); // Obtiene dimensiones del panel
                Color color1 = new Color(245, 247, 250); // Color inicial del degradado
                Color color2 = new Color(200, 230, 201); // Color final del degradado
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2); // Crea el degradado
                g2d.setPaint(gp); // Aplica el degradado
                g2d.fillRect(0, 0, w, h); // Rellena el panel con el degradado
            }
        };
    }

    // Crea el panel de cabecera
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()); // Crea el panel con diseno BorderLayout
        headerPanel.setBackground(new Color(166, 7, 7)); // Establece el color de fondo
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20)); // Agrega un borde vacio

        // Crea la etiqueta del titulo
        JLabel headerLabel = new JLabel("Sistema de Gestion Federativa FFCV", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Establece la fuente
        headerLabel.setForeground(Color.WHITE); // Establece el color del texto

        // Crea la etiqueta con el logo
        JLabel logoLabel = createLogoLabel("/resources/logo.png");
        headerPanel.add(logoLabel, BorderLayout.WEST); // Agrega el logo a la izquierda
        headerPanel.add(headerLabel, BorderLayout.CENTER); // Agrega el titulo al centro
        return headerPanel;
    }

    // Crea la barra de menus
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar(); // Crea la barra de menus
        menuBar.setBackground(new Color(33, 37, 41)); // Establece el color de fondo
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Agrega un borde vacio

        Font menuFont = new Font("Segoe UI", Font.PLAIN, 16); // Fuente para los menus
        Color menuForeground = Color.WHITE; // Color del texto
        Color menuHover = new Color(66, 66, 66); // Color al pasar el raton

        // Crea el menu Gestion
        JMenu gestionMenu = new JMenu("Gestion");
        styleMenu(gestionMenu, menuFont, menuForeground, menuHover); // Aplica estilos
        gestionMenu.setIcon(loadIcon("/resources/iconos/gestion.png")); // Agrega un icono

        // Elementos del menu
        String[] menuItems = {"Categorias", "Clubes", "Personas", "Empleados", "Instalaciones", "Grupos", "Equipos", "Licencias"};
        String[] iconPaths = {"categorias.png", "club.png", "persona.png", "empleado.png", "instalaciones.png", "grupos.png", "teams.png", "licencia.png"};
        JMenuItem[] items = new JMenuItem[menuItems.length];

        // Crea los elementos del menu
        for (int i = 0; i < menuItems.length; i++) {
            items[i] = new JMenuItem(menuItems[i], loadIcon("/resources/iconos/" + iconPaths[i]));
            styleMenuItem(items[i], menuFont, menuForeground, menuHover); // Aplica estilos
            gestionMenu.add(items[i]); // Agrega el elemento al menu
        }

        menuBar.add(gestionMenu); // Agrega el menu a la barra
        return menuBar;
    }

    // Configura las acciones de los elementos del menu
    private void setupMenuActions() {
        JMenu gestionMenu = menuBar.getMenu(0); // Obtiene el menu Gestion
        gestionMenu.getItem(0).addActionListener(e -> switchPanel(createCategoriaPanel())); // Accion para Categorias
        gestionMenu.getItem(1).addActionListener(e -> switchPanel(createClubPanel())); // Accion para Clubes
        gestionMenu.getItem(2).addActionListener(e -> switchPanel(createPersonaPanel())); // Accion para Personas
        gestionMenu.getItem(3).addActionListener(e -> switchPanel(createEmpleadoPanel())); // Accion para Empleados
        gestionMenu.getItem(4).addActionListener(e -> switchPanel(createInstalacionPanel())); // Accion para Instalaciones
        gestionMenu.getItem(5).addActionListener(e -> switchPanel(createGrupoPanel())); // Accion para Grupos
        gestionMenu.getItem(6).addActionListener(e -> switchPanel(createEquipoPanel())); // Accion para Equipos
        gestionMenu.getItem(7).addActionListener(e -> switchPanel(createLicenciaPanel())); // Accion para Licencias
    }

    // Cambia el panel de contenido
    private void switchPanel(JPanel panel) {
        contentPanel.removeAll(); // Elimina el contenido actual
        contentPanel.add(panel, BorderLayout.CENTER); // Agrega el nuevo panel
        contentPanel.revalidate(); // Revalida el diseno
        contentPanel.repaint(); // Redibuja el panel
    }

    // Crea el panel de bienvenida
    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout()); // Crea el panel
        welcomePanel.setBackground(Color.WHITE); // Establece el fondo blanco

        // Etiqueta de bienvenida
        JLabel welcomeLabel = new JLabel("Bienvenido al Sistema de Gestion FFCV", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Establece la fuente
        welcomeLabel.setForeground(new Color(33, 37, 41)); // Establece el color

        // Etiqueta de informacion
        JLabel infoLabel = new JLabel("<html><center>Seleccione una opcion del menu 'Gestion' para comenzar.<br>" +
                "Gestione categorias, clubes, personas, equipos y mas.</center></html>", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Establece la fuente
        infoLabel.setForeground(new Color(66, 66, 66)); // Establece el color

        welcomePanel.add(welcomeLabel, BorderLayout.NORTH); // Agrega la etiqueta de bienvenida
        welcomePanel.add(infoLabel, BorderLayout.CENTER); // Agrega la etiqueta de informacion
        return welcomePanel;
    }

    // Crea el panel para gestionar categorias
    private JPanel createCategoriaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel
        panel.setBackground(Color.WHITE); // Establece el fondo blanco
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Agrega un borde vacio

        // Panel para crear/actualizar categorias
        JPanel createPanel = createTitledPanel("Crear/Actualizar Categoria");
        GridBagConstraints gbc = new GridBagConstraints(); // Configura las restricciones para el diseno
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena horizontalmente

        // Campos de entrada
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 0); // Campo para el nombre
        JTextField ordenField = addField(createPanel, gbc, "Nivel/Orden:", 1); // Campo para el nivel
        JTextField precioField = addField(createPanel, gbc, "Precio Licencia:", 2); // Campo para el precio

        // Botones
        JButton crearButton = new JButton("Crear Categoria", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Categoria", loadIcon("/resources/iconos/edit.png"));
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Categoria", loadIcon("/resources/iconos/delete.png"));
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton

        // Accion del boton Crear
        crearButton.addActionListener(event -> {
            if (!validateFields(nombreField, ordenField, precioField)) return; // Valida los campos
            try {
                int orden = Integer.parseInt(ordenField.getText()); // Convierte el nivel a entero
                double precio = Double.parseDouble(precioField.getText()); // Convierte el precio a double
                if (precio < 0) throw new IllegalArgumentException("El precio no puede ser negativo.");
                Categoria categoria = federacion.nuevaCategoria(nombreField.getText(), orden, precio); // Crea la categoria
                JOptionPane.showMessageDialog(frame, "Categoria creada: " + categoria); // Muestra mensaje
                clearFields(nombreField, ordenField, precioField); // Limpia los campos
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Nivel y precio deben ser numericos.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Actualizar
        actualizarButton.addActionListener(event -> {
            if (!validateFields(nombreField, ordenField, precioField)) return; // Valida los campos
            try {
                Categoria categoria = Categoria.buscarPorNombre(nombreField.getText()); // Busca la categoria
                if (categoria == null) {
                    JOptionPane.showMessageDialog(frame, "Categoria no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                categoria.setOrden(Integer.parseInt(ordenField.getText())); // Actualiza el nivel
                categoria.setPrecioLicencia(Double.parseDouble(precioField.getText())); // Actualiza el precio
                categoria.actualizar(); // Guarda los cambios
                JOptionPane.showMessageDialog(frame, "Categoria actualizada: " + categoria); // Muestra mensaje
                clearFields(nombreField, ordenField, precioField); // Limpia los campos
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Nivel y precio deben ser numericos.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Eliminar
        eliminarButton.addActionListener(event -> {
            if (!validateFields(nombreField)) return; // Valida el campo nombre
            try {
                Categoria categoria = Categoria.buscarPorNombre(nombreField.getText()); // Busca la categoria
                if (categoria == null) {
                    JOptionPane.showMessageDialog(frame, "Categoria no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                categoria.eliminar(); // Elimina la categoria
                JOptionPane.showMessageDialog(frame, "Categoria eliminada: " + categoria); // Muestra mensaje
                clearFields(nombreField, ordenField, precioField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Agrega los botones al panel
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 4; createPanel.add(actualizarButton, gbc);
        gbc.gridy = 5; createPanel.add(eliminarButton, gbc);

        // Panel para listar categorias
        JPanel listPanel = createTitledPanel("Consultar Categorias", new BorderLayout());
        JButton listarButton = new JButton("Listar Categorias", loadIcon("/resources/iconos/magnifier.png"));
        styleButton(listarButton, new Color(33, 37, 41), false); // Estiliza el boton
        listarButton.addActionListener(event -> listCategorias(listPanel)); // Asigna la accion
        listPanel.add(listarButton, BorderLayout.NORTH); // Agrega el boton

        panel.add(createPanel, BorderLayout.NORTH); // Agrega el panel de creacion
        panel.add(listPanel, BorderLayout.CENTER); // Agrega el panel de lista
        return panel;
    }

    // Crea el panel para gestionar clubes
    private JPanel createClubPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel
        panel.setBackground(Color.WHITE); // Establece el fondo blanco
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Agrega un borde vacio

        // Panel para gestionar clubes
        JPanel createPanel = createTitledPanel("Gestion de Clubes");
        GridBagConstraints gbc = new GridBagConstraints(); // Configura las restricciones
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena horizontalmente

        // Campos de entrada
        JTextField nombreField = addField(createPanel, gbc, "Nombre Club:", 0); // Campo para el nombre
        JTextField fechaField = addField(createPanel, gbc, "Fecha Fundacion (YYYY-MM-DD):", 1); // Campo para la fecha
        JTextField dniPresField = addField(createPanel, gbc, "DNI Presidente:", 2); // Campo para el DNI del presidente
        JTextField buscarField = addField(createPanel, gbc, "Buscar Club por Nombre:", 3); // Campo para buscar

        // Botones
        JButton crearClubButton = new JButton("Crear Club", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearClubButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Club", loadIcon("/resources/iconos/edit.png"));
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Club", loadIcon("/resources/iconos/delete.png"));
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton buscarButton = new JButton("Buscar Club", loadIcon("/resources/iconos/magnifier.png"));
        styleButton(buscarButton, new Color(33, 37, 41), false); // Estiliza el boton

        /// Accion del boton Crear
        crearClubButton.addActionListener(event -> {
            if (!validateFields(nombreField, fechaField, dniPresField)) return; // Valida los campos
            try {
                if (!validateDateFormat(fechaField.getText())) throw new DateTimeParseException("Formato de fecha invalido.", fechaField.getText(), 0);
                if (!validateDni(dniPresField.getText())) throw new IllegalArgumentException("DNI invalido.");
                LocalDate fecha = LocalDate.parse(fechaField.getText()); // Convierte la fecha
                Persona presidente = federacion.buscaPersona(dniPresField.getText()); // Busca al presidente
                if (presidente == null) {
                    JOptionPane.showMessageDialog(frame, "Presidente no encontrado. Registrelo primero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Club club = federacion.nuevoClub(nombreField.getText(), fecha, presidente); // Crea el club
                JOptionPane.showMessageDialog(frame, "Club creado: " + club, "Éxito", JOptionPane.INFORMATION_MESSAGE); // Muestra mensaje
                clearFields(nombreField, fechaField, dniPresField); // Limpia los campos
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha invalido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Actualizar
        actualizarButton.addActionListener(event -> {
            if (!validateFields(nombreField, fechaField, dniPresField)) return; // Valida los campos
            try {
                Club club = federacion.buscarClub(nombreField.getText()); // Busca el club
                if (club == null) {
                    JOptionPane.showMessageDialog(frame, "Club no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!validateDateFormat(fechaField.getText())) throw new DateTimeParseException("Formato de fecha invalido.", fechaField.getText(), 0);
                if (!validateDni(dniPresField.getText())) throw new IllegalArgumentException("DNI invalido.");
                LocalDate fecha = LocalDate.parse(fechaField.getText()); // Convierte la fecha
                Persona presidente = federacion.buscaPersona(dniPresField.getText()); // Busca al presidente
                if (presidente == null) {
                    JOptionPane.showMessageDialog(frame, "Presidente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                club.setFechaFundacion(fecha); // Actualiza la fecha
                club.setPresidente(presidente); // Actualiza el presidente
                club.actualizar(); // Guarda los cambios
                JOptionPane.showMessageDialog(frame, "Club actualizado: " + club); // Muestra mensaje
                clearFields(nombreField, fechaField, dniPresField); // Limpia los campos
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha invalido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Eliminar
        eliminarButton.addActionListener(event -> {
            if (!validateFields(nombreField)) return; // Valida el campo nombre
            try {
                Club club = federacion.buscarClub(nombreField.getText()); // Busca el club
                if (club == null) {
                    JOptionPane.showMessageDialog(frame, "Club no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                club.eliminar(); // Elimina el club
                JOptionPane.showMessageDialog(frame, "Club eliminado: " + club); // Muestra mensaje
                clearFields(nombreField, fechaField, dniPresField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Buscar
        buscarButton.addActionListener(event -> {
            if (!validateFields(buscarField)) return; // Valida el campo de busqueda
            Club club = federacion.buscarClub(buscarField.getText()); // Busca el club
            JOptionPane.showMessageDialog(frame, club != null ? "Club encontrado: " + club : "Club no encontrado.");
        });

        // Agrega los botones al panel
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; createPanel.add(crearClubButton, gbc);
        gbc.gridy = 5; createPanel.add(actualizarButton, gbc);
        gbc.gridy = 6; createPanel.add(eliminarButton, gbc);
        gbc.gridy = 7; createPanel.add(buscarButton, gbc);

        // Panel para listar clubes
        JPanel listPanel = createTitledPanel("Listar Clubes", new BorderLayout());
        JButton listarButton = new JButton("Listar Clubes", loadIcon("/resources/iconos/magnifier.png"));
        styleButton(listarButton, new Color(33, 37, 41), false); // Estiliza el boton
        listarButton.addActionListener(event -> listClubes(listPanel)); // Asigna la accion
        listPanel.add(listarButton, BorderLayout.NORTH); // Agrega el boton

        panel.add(createPanel, BorderLayout.NORTH); // Agrega el panel de creacion
        panel.add(listPanel, BorderLayout.CENTER); // Agrega el panel de lista
        return panel;
    }

    // Crea el panel para gestionar personas
    private JPanel createPersonaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel
        panel.setBackground(Color.WHITE); // Establece el fondo blanco
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Agrega un borde vacio

        // Panel para crear/actualizar personas
        JPanel createPanel = createTitledPanel("Crear/Actualizar Persona");
        GridBagConstraints gbc = new GridBagConstraints(); // Configura las restricciones
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena horizontalmente

        // Campos de entrada
        JTextField dniField = addField(createPanel, gbc, "DNI:", 0); // Campo para el DNI
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 1); // Campo para el nombre
        JTextField apellido1Field = addField(createPanel, gbc, "Apellido 1:", 2); // Campo para el primer apellido
        JTextField apellido2Field = addField(createPanel, gbc, "Apellido 2:", 3); // Campo para el segundo apellido
        JTextField fechaField = addField(createPanel, gbc, "Fecha Nacimiento (YYYY-MM-DD):", 4); // Campo para la fecha
        JTextField poblacionField = addField(createPanel, gbc, "Poblacion:", 5); // Campo para la poblacion
        JTextField usuarioField = addField(createPanel, gbc, "Usuario:", 6); // Campo para el usuario
        JTextField passwordField = addField(createPanel, gbc, "Contrasena:", 7); // Campo para la contrasena
        JTextField buscarDniField = addField(createPanel, gbc, "Buscar por DNI:", 8); // Campo para buscar por DNI

        // Botones
        JButton crearButton = new JButton("Crear Persona", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Persona", loadIcon("/resources/iconos/edit.png"));
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Persona", loadIcon("/resources/iconos/delete.png"));
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton buscarButton = new JButton("Buscar Persona", loadIcon("/resources/iconos/magnifier.png"));
        styleButton(buscarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton buscarMultiButton = new JButton("Buscar por Nombre y Apellidos", loadIcon("/resources/iconos/magnifier.png"));
        styleButton(buscarMultiButton, new Color(33, 37, 41), false); // Estiliza el boton

        // Accion del boton Crear
        crearButton.addActionListener(event -> {
            if (!validateFields(dniField, nombreField, apellido1Field, fechaField, usuarioField, passwordField)) return; // Valida los campos
            try {
                if (!validateDni(dniField.getText())) throw new IllegalArgumentException("DNI invalido.");
                if (!validateDateFormat(fechaField.getText())) throw new DateTimeParseException("Formato de fecha invalido.", fechaField.getText(), 0);
                Persona persona = federacion.nuevaPersona(dniField.getText(), nombreField.getText(), apellido1Field.getText(),
                        apellido2Field.getText(), LocalDate.parse(fechaField.getText()), usuarioField.getText(),
                        passwordField.getText(), poblacionField.getText()); // Crea la persona
                if (persona != null) {
                    JOptionPane.showMessageDialog(frame, "Persona creada: " + persona); // Muestra mensaje
                    clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, poblacionField, usuarioField, passwordField); // Limpia los campos
                } else {
                    JOptionPane.showMessageDialog(frame, "No se pudo crear la persona. Verifique el DNI.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha invalido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Actualizar
        actualizarButton.addActionListener(event -> {
            if (!validateFields(dniField, nombreField, apellido1Field, fechaField, usuarioField, passwordField)) return; // Valida los campos
            try {
                if (!validateDni(dniField.getText())) throw new IllegalArgumentException("DNI invalido.");
                if (!validateDateFormat(fechaField.getText())) throw new DateTimeParseException("Formato de fecha invalido.", fechaField.getText(), 0);
                Persona persona = federacion.buscaPersona(dniField.getText()); // Busca la persona
                if (persona == null) {
                    JOptionPane.showMessageDialog(frame, "Persona no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                persona.setNombre(nombreField.getText()); // Actualiza el nombre
                persona.setApellido1(apellido1Field.getText()); // Actualiza el primer apellido
                persona.setApellido2(apellido2Field.getText()); // Actualiza el segundo apellido
                persona.setFechaNacimiento(LocalDate.parse(fechaField.getText())); // Actualiza la fecha
                persona.setPoblacion(poblacionField.getText()); // Actualiza la poblacion
                persona.setUsuario(usuarioField.getText()); // Actualiza el usuario
                persona.setPassword(passwordField.getText()); // Actualiza la contrasena
                persona.Persistencia(); // Guarda los cambios
                JOptionPane.showMessageDialog(frame, "Persona actualizada: " + persona); // Muestra mensaje
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, poblacionField, usuarioField, passwordField); // Limpia los campos
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha invalido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Eliminar
        eliminarButton.addActionListener(event -> {
            if (!validateFields(dniField)) return; // Valida el campo DNI
            try {
                if (!validateDni(dniField.getText())) throw new IllegalArgumentException("DNI invalido.");
                Persona persona = federacion.buscaPersona(dniField.getText()); // Busca la persona
                if (persona == null) {
                    JOptionPane.showMessageDialog(frame, "Persona no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Nota: La eliminacion no esta implementada en Persona.java
                JOptionPane.showMessageDialog(frame, "Eliminacion no implementada en Persona.java.", "Error", JOptionPane.ERROR_MESSAGE);
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, poblacionField, usuarioField, passwordField); // Limpia los campos
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Buscar por DNI
        buscarButton.addActionListener(event -> {
            if (!validateFields(buscarDniField)) return; // Valida el campo de busqueda
            try {
                if (!validateDni(buscarDniField.getText())) throw new IllegalArgumentException("DNI invalido.");
                Persona persona = federacion.buscaPersona(buscarDniField.getText()); // Busca la persona
                JOptionPane.showMessageDialog(frame, persona != null ? "Persona encontrada: " + persona : "Persona no encontrada.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Buscar por nombre y apellidos
        buscarMultiButton.addActionListener(event -> {
            List<Persona> personas = federacion.buscaPersonas(nombreField.getText(), apellido1Field.getText(), apellido2Field.getText()); // Busca personas
            showListResult(personas, "Personas encontradas:"); // Muestra los resultados
        });

        // Agrega los botones al panel
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 10; createPanel.add(actualizarButton, gbc);
        gbc.gridy = 11; createPanel.add(eliminarButton, gbc);
        gbc.gridy = 12; createPanel.add(buscarButton, gbc);
        gbc.gridy = 13; createPanel.add(buscarMultiButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH); // Agrega el panel de creacion
        return panel;
    }

    // Crea el panel para gestionar empleados
    private JPanel createEmpleadoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel createPanel = createTitledPanel("Crear/Actualizar Empleado");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField dniField = addField(createPanel, gbc, "DNI:", 0);
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 1);
        JTextField apellido1Field = addField(createPanel, gbc, "Apellido 1:", 2);
        JTextField apellido2Field = addField(createPanel, gbc, "Apellido 2:", 3);
        JTextField fechaField = addField(createPanel, gbc, "Fecha Nacimiento (YYYY-MM-DD):", 4);
        JTextField numEmpField = addField(createPanel, gbc, "Numero Empleado:", 5);
        JTextField inicioContratoField = addField(createPanel, gbc, "Inicio Contrato (YYYY-MM-DD):", 6);
        JTextField segSocialField = addField(createPanel, gbc, "Seguridad Social:", 7);
        JTextField puestoField = addField(createPanel, gbc, "Puesto:", 8);
        JTextField usuarioField = addField(createPanel, gbc, "Usuario:", 9);
        JTextField passwordField = addField(createPanel, gbc, "Contrasena:", 10);
        JTextField poblacionField = addField(createPanel, gbc, "Poblacion:", 11);

        JButton crearButton = new JButton("Crear Empleado", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true);
        JButton actualizarButton = new JButton("Actualizar Empleado", loadIcon("/resources/iconos/edit.png"));
        styleButton(actualizarButton, new Color(33, 37, 41), false);
        JButton eliminarButton = new JButton("Eliminar Empleado", loadIcon("/resources/iconos/delete.png"));
        styleButton(eliminarButton, new Color(211, 47, 47), true);

        crearButton.addActionListener(event -> {
            if (!validateFields(dniField, nombreField, apellido1Field, fechaField, numEmpField, inicioContratoField, segSocialField, usuarioField, passwordField)) return;
            try {
                if (federacion.buscaPersona(dniField.getText()) != null) {
                    throw new IllegalArgumentException("El DNI ya está registrado.");
                }
                if (!validateDni(dniField.getText())) throw new IllegalArgumentException("DNI invalido.");
                if (!validateDateFormat(fechaField.getText()) || !validateDateFormat(inicioContratoField.getText())) {
                    throw new DateTimeParseException("Formato de fecha invalido.", "", 0);
                }
                if (!segSocialField.getText().matches("\\d{12}")) {
                    throw new IllegalArgumentException("Seguridad Social debe tener 12 dígitos.");
                }
                int numEmp = Integer.parseInt(numEmpField.getText());
                if (numEmp <= 0) throw new IllegalArgumentException("Numero de empleado debe ser mayor a cero.");
                Empleado empleado = federacion.nuevoEmpleado(dniField.getText(), nombreField.getText(), apellido1Field.getText(),
                        apellido2Field.getText(), LocalDate.parse(fechaField.getText()), usuarioField.getText(),
                        passwordField.getText(), poblacionField.getText(), numEmp, LocalDate.parse(inicioContratoField.getText()),
                        segSocialField.getText());
                if (empleado != null) {
                    empleado.setPuesto(puestoField.getText());
                    empleado.actualizar();
                    JOptionPane.showMessageDialog(frame, "Empleado creado: " + empleado);
                    clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, numEmpField, inicioContratoField, segSocialField, puestoField, usuarioField, passwordField, poblacionField);
                } else {
                    JOptionPane.showMessageDialog(frame, "No se pudo crear el empleado. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha invalido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Numero de empleado debe ser numerico.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        actualizarButton.addActionListener(event -> {
            if (!validateFields(dniField, nombreField, apellido1Field, fechaField, numEmpField, inicioContratoField, segSocialField, usuarioField, passwordField)) return;
            try {
                if (!validateDni(dniField.getText())) throw new IllegalArgumentException("DNI invalido.");
                if (!validateDateFormat(fechaField.getText()) || !validateDateFormat(inicioContratoField.getText())) {
                    throw new DateTimeParseException("Formato de fecha invalido.", "", 0);
                }
                int numEmp = Integer.parseInt(numEmpField.getText());
                if (numEmp <= 0) throw new IllegalArgumentException("Numero de empleado debe ser mayor a cero.");
                Persona persona = federacion.buscaPersona(dniField.getText());
                if (persona == null || !(persona instanceof Empleado)) {
                    JOptionPane.showMessageDialog(frame, "No se encontró un empleado con ese DNI.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Empleado empleado = (Empleado) federacion.buscaPersona(dniField.getText());
                if (empleado == null) {
                    JOptionPane.showMessageDialog(frame, "Empleado no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                empleado.setNombre(nombreField.getText());
                empleado.setApellido1(apellido1Field.getText());
                empleado.setApellido2(apellido2Field.getText());
                empleado.setFechaNacimiento(LocalDate.parse(fechaField.getText()));
                empleado.setPoblacion(poblacionField.getText());
                empleado.setUsuario(usuarioField.getText());
                empleado.setPassword(passwordField.getText());
                empleado.setNumEmpleado(numEmp);
                empleado.setInicioContrato(LocalDate.parse(inicioContratoField.getText()));
                empleado.setSegSocial(segSocialField.getText());
                empleado.setPuesto(puestoField.getText());
                empleado.actualizar();
                JOptionPane.showMessageDialog(frame, "Empleado actualizado: " + empleado);
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, numEmpField, inicioContratoField, segSocialField, puestoField, usuarioField, passwordField, poblacionField);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha invalido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Numero de empleado debe ser numerico.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        eliminarButton.addActionListener(event -> {
            if (!validateFields(dniField)) return;
            try {
                if (!validateDni(dniField.getText())) throw new IllegalArgumentException("DNI invalido.");
                Persona persona = federacion.buscaPersona(dniField.getText());
                if (persona == null || !(persona instanceof Empleado)) {
                    JOptionPane.showMessageDialog(frame, "No se encontró un empleado con ese DNI.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Empleado empleado = (Empleado) federacion.buscaPersona(dniField.getText());
                if (empleado == null) {
                    JOptionPane.showMessageDialog(frame, "Empleado no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                empleado.eliminar();
                JOptionPane.showMessageDialog(frame, "Empleado eliminado: " + empleado);
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, numEmpField, inicioContratoField, segSocialField, puestoField, usuarioField, passwordField, poblacionField);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 12; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 13; createPanel.add(actualizarButton, gbc);
        gbc.gridy = 14; createPanel.add(eliminarButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH);
        return panel;
    }
    
    // Crea el panel para gestionar instalaciones
    private JPanel createInstalacionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel
        panel.setBackground(Color.WHITE); // Establece el fondo blanco
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Agrega un borde vacio

        // Panel para crear/actualizar instalaciones
        JPanel createPanel = createTitledPanel("Crear/Actualizar Instalacion");
        GridBagConstraints gbc = new GridBagConstraints(); // Configura las restricciones
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena horizontalmente

        // Campos de entrada
        JTextField nombreField = addField(createPanel, gbc, "Nombre:", 0); // Campo para el nombre
        JTextField direccionField = addField(createPanel, gbc, "Direccion:", 1); // Campo para la direccion
        JLabel superficieLabel = new JLabel("Tipo de Superficie:"); // Etiqueta para el tipo de superficie
        superficieLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
        JComboBox<Instalacion.TipoSuperficie> superficieCombo = new JComboBox<>(Instalacion.TipoSuperficie.values()); // Combo para seleccionar superficie
        superficieCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
        gbc.gridx = 0; gbc.gridy = 2; createPanel.add(superficieLabel, gbc); // Agrega la etiqueta
        gbc.gridx = 1; createPanel.add(superficieCombo, gbc); // Agrega el combo
        JTextField buscarField = addField(createPanel, gbc, "Buscar por Nombre:", 3); // Campo para buscar

        // Botones
        JButton crearButton = new JButton("Crear Instalacion", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Instalacion", loadIcon("/resources/iconos/edit.png"));
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Instalacion", loadIcon("/resources/iconos/delete.png"));
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton buscarButton = new JButton("Buscar Instalaciones", loadIcon("/resources/iconos/magnifier.png"));
        styleButton(buscarButton, new Color(33, 37, 41), false); // Estiliza el boton

        // Accion del boton Crear
        crearButton.addActionListener(event -> {
            if (!validateFields(nombreField, direccionField)) return; // Valida los campos
            try {
                Instalacion instalacion = federacion.nuevaInstalacion(nombreField.getText(), direccionField.getText(),
                        ((Instalacion.TipoSuperficie) superficieCombo.getSelectedItem()).name()); // Crea la instalacion
                JOptionPane.showMessageDialog(frame, "Instalacion creada: " + instalacion); // Muestra mensaje
                clearFields(nombreField, direccionField); // Limpia los campos
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Actualizar
        actualizarButton.addActionListener(event -> {
            if (!validateFields(nombreField, direccionField)) return; // Valida los campos
            try {
                Instalacion instalacion = Instalacion.buscarPorNombre(nombreField.getText()); // Busca la instalacion
                if (instalacion == null) {
                    JOptionPane.showMessageDialog(frame, "Instalacion no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                instalacion.setDireccion(direccionField.getText()); // Actualiza la direccion
                instalacion.setSuperficie((Instalacion.TipoSuperficie) superficieCombo.getSelectedItem()); // Actualiza la superficie
                instalacion.actualizar(); // Guarda los cambios
                JOptionPane.showMessageDialog(frame, "Instalacion actualizada: " + instalacion); // Muestra mensaje
                clearFields(nombreField, direccionField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Eliminar
        eliminarButton.addActionListener(event -> {
            if (!validateFields(nombreField)) return; // Valida el campo nombre
            try {
                Instalacion instalacion = Instalacion.buscarPorNombre(nombreField.getText()); // Busca la instalacion
                if (instalacion == null) {
                    JOptionPane.showMessageDialog(frame, "Instalacion no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                instalacion.eliminar(); // Elimina la instalacion
                JOptionPane.showMessageDialog(frame, "Instalacion eliminada: " + instalacion); // Muestra mensaje
                clearFields(nombreField, direccionField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Buscar
        buscarButton.addActionListener(event -> {
            if (!validateFields(buscarField)) return; // Valida el campo de busqueda
            List<Instalacion> instalaciones = federacion.buscarInstalaciones(buscarField.getText()); // Busca instalaciones
            showListResult(instalaciones, "Instalaciones encontradas:"); // Muestra los resultados
        });

        // Agrega los botones al panel
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 5; createPanel.add(actualizarButton, gbc);
        gbc.gridy = 6; createPanel.add(eliminarButton, gbc);
        gbc.gridy = 7; createPanel.add(buscarButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH); // Agrega el panel de creacion
        return panel;
    }

    // Crea el panel para gestionar grupos
    private JPanel createGrupoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel
        panel.setBackground(Color.WHITE); // Establece el fondo blanco
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Agrega un borde vacio

        // Panel para crear/actualizar grupos
        JPanel createPanel = createTitledPanel("Crear/Actualizar Grupo");
        GridBagConstraints gbc = new GridBagConstraints(); // Configura las restricciones
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena horizontalmente

        // Campos de entrada
        JTextField categoriaField = addField(createPanel, gbc, "Nombre Categoria:", 0); // Campo para la categoria
        JTextField nombreField = addField(createPanel, gbc, "Nombre Grupo:", 1); // Campo para el nombre del grupo

        // Botones
        JButton crearButton = new JButton("Crear Grupo", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Grupo", loadIcon("/resources/iconos/edit.png"));
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Grupo", loadIcon("/resources/iconos/delete.png"));
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton listarButton = new JButton("Listar Grupos", loadIcon("/resources/iconos/magnifier.png"));
        styleButton(listarButton, new Color(33, 37, 41), false); // Estiliza el boton

        // Accion del boton Crear
        crearButton.addActionListener(event -> {
            if (!validateFields(categoriaField, nombreField)) return; // Valida los campos
            try {
                Categoria categoria = Categoria.buscarPorNombre(categoriaField.getText()); // Busca la categoria
                if (categoria == null) {
                    JOptionPane.showMessageDialog(frame, "Categoria no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Grupo grupo = federacion.nuevoGrupo(categoria, nombreField.getText()); // Crea el grupo
                JOptionPane.showMessageDialog(frame, "Grupo creado: " + grupo); // Muestra mensaje
                clearFields(categoriaField, nombreField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Actualizar
        actualizarButton.addActionListener(event -> {
            if (!validateFields(categoriaField, nombreField)) return; // Valida los campos
            try {
                Categoria categoria = Categoria.buscarPorNombre(categoriaField.getText()); // Busca la categoria
                if (categoria == null) {
                    JOptionPane.showMessageDialog(frame, "Categoria no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Grupo grupo = Grupo.buscarPorNombre(nombreField.getText()); // Busca el grupo
                if (grupo == null) {
                    JOptionPane.showMessageDialog(frame, "Grupo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                grupo.setCategoria(categoria); // Actualiza la categoria
                grupo.actualizar(); // Guarda los cambios
                JOptionPane.showMessageDialog(frame, "Grupo actualizado: " + grupo); // Muestra mensaje
                clearFields(categoriaField, nombreField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Eliminar
        eliminarButton.addActionListener(event -> {
            if (!validateFields(nombreField)) return; // Valida el campo nombre
            try {
                Grupo grupo = Grupo.buscarPorNombre(nombreField.getText()); // Busca el grupo
                if (grupo == null) {
                    JOptionPane.showMessageDialog(frame, "Grupo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                grupo.eliminar(); // Elimina el grupo
                JOptionPane.showMessageDialog(frame, "Grupo eliminado: " + grupo); // Muestra mensaje
                clearFields(categoriaField, nombreField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Listar
        listarButton.addActionListener(event -> {
            if (!validateFields(categoriaField)) return; // Valida el campo categoria
            try {
                Categoria categoria = Categoria.buscarPorNombre(categoriaField.getText()); // Busca la categoria
                if (categoria == null) {
                    JOptionPane.showMessageDialog(frame, "Categoria no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<Grupo> grupos = federacion.obtenerGrupos(categoria); // Obtiene los grupos
                showListResult(grupos, "Grupos en " + categoria.getNombre() + ":"); // Muestra los resultados
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Agrega los botones al panel
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 3; createPanel.add(actualizarButton, gbc);
        gbc.gridy = 4; createPanel.add(eliminarButton, gbc);
        gbc.gridy = 5; createPanel.add(listarButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH); // Agrega el panel de creacion
        return panel;
    }

    // Crea el panel para gestionar equipos
    private JPanel createEquipoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel
        panel.setBackground(Color.WHITE); // Establece el fondo blanco
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Agrega un borde vacio

        // Panel para crear/actualizar equipos
        JPanel createPanel = createTitledPanel("Crear/Actualizar Equipo y Buscar Jugador");
        GridBagConstraints gbc = new GridBagConstraints(); // Configura las restricciones
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena horizontalmente

        // Campos de entrada
        JTextField letraField = addField(createPanel, gbc, "Letra:", 0); // Campo para la letra del equipo
        JTextField instalacionField = addField(createPanel, gbc, "Nombre Instalacion:", 1); // Campo para la instalacion
        JTextField grupoField = addField(createPanel, gbc, "Nombre Grupo:", 2); // Campo para el grupo
        JTextField clubField = addField(createPanel, gbc, "Nombre Club:", 3); // Campo para el club

        // Botones
        JButton crearButton = new JButton("Crear Equipo", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton actualizarButton = new JButton("Actualizar Equipo", loadIcon("/resources/iconos/edit.png"));
        styleButton(actualizarButton, new Color(33, 37, 41), false); // Estiliza el boton
        JButton eliminarButton = new JButton("Eliminar Equipo", loadIcon("/resources/iconos/delete.png"));
        styleButton(eliminarButton, new Color(211, 47, 47), true); // Estiliza el boton

        // Campos adicionales para buscar jugador
        JTextField buscarLetraField = addField(createPanel, gbc, "Letra Equipo para Buscar:", 5); // Campo para la letra del equipo
        JTextField dniField = addField(createPanel, gbc, "DNI Jugador:", 6); // Campo para el DNI del jugador

        JButton buscarJugadorButton = new JButton("Buscar Jugador", loadIcon("/resources/iconos/magnifier.png"));
        styleButton(buscarJugadorButton, new Color(33, 37, 41), false); // Estiliza el boton

        // Accion del boton Crear
        crearButton.addActionListener(event -> {
            if (!validateFields(letraField, instalacionField, grupoField, clubField)) return; // Valida los campos
            try {
                Instalacion instalacion = Instalacion.buscarPorNombre(instalacionField.getText()); // Busca la instalacion
                if (instalacion == null) {
                    JOptionPane.showMessageDialog(frame, "Instalacion no encontrada. Cree una primero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Grupo grupo = Grupo.buscarPorNombre(grupoField.getText()); // Busca el grupo
                if (grupo == null) {
                    JOptionPane.showMessageDialog(frame, "Grupo no encontrado. Cree uno primero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Club club = federacion.buscarClub(clubField.getText()); // Busca el club
                if (club == null) {
                    JOptionPane.showMessageDialog(frame, "Club no encontrado. Cree uno primero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Equipo equipo = federacion.nuevoEquipo(letraField.getText(), instalacion, grupo, club); // Crea el equipo
                JOptionPane.showMessageDialog(frame, "Equipo creado: " + equipo); // Muestra mensaje
                clearFields(letraField, instalacionField, grupoField, clubField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Actualizar
        actualizarButton.addActionListener(event -> {
            if (!validateFields(letraField, instalacionField, grupoField, clubField)) return; // Valida los campos
            try {
                Equipo equipo = Equipo.buscarPorLetra(letraField.getText()); // Busca el equipo
                if (equipo == null) {
                    JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Instalacion instalacion = Instalacion.buscarPorNombre(instalacionField.getText()); // Busca la instalacion
                if (instalacion == null) {
                    JOptionPane.showMessageDialog(frame, "Instalacion no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Grupo grupo = Grupo.buscarPorNombre(grupoField.getText()); // Busca el grupo
                if (grupo == null) {
                    JOptionPane.showMessageDialog(frame, "Grupo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Club club = federacion.buscarClub(clubField.getText()); // Busca el club
                if (club == null) {
                    JOptionPane.showMessageDialog(frame, "Club no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                equipo.setInstalacion(instalacion); // Actualiza la instalacion
                equipo.setGrupo(grupo); // Actualiza el grupo
                equipo.setClubId(club.obtenerIdClub()); // Actualiza el club
                equipo.actualizar(); // Guarda los cambios
                JOptionPane.showMessageDialog(frame, "Equipo actualizado: " + equipo); // Muestra mensaje
                clearFields(letraField, instalacionField, grupoField, clubField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Eliminar
        eliminarButton.addActionListener(event -> {
            if (!validateFields(letraField)) return; // Valida el campo letra
            try {
                Equipo equipo = Equipo.buscarPorLetra(letraField.getText()); // Busca el equipo
                if (equipo == null) {
                    JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                equipo.eliminar(); // Elimina el equipo
                JOptionPane.showMessageDialog(frame, "Equipo eliminado: " + equipo); // Muestra mensaje
                clearFields(letraField, instalacionField, grupoField, clubField); // Limpia los campos
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Buscar Jugador
        buscarJugadorButton.addActionListener(event -> {
            if (!validateFields(buscarLetraField, dniField)) return; // Valida los campos
            try {
                if (!validateDni(dniField.getText())) throw new IllegalArgumentException("DNI invalido.");
                Equipo equipo = Equipo.buscarPorLetra(buscarLetraField.getText()); // Busca el equipo
                if (equipo == null) {
                    JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Persona jugador = equipo.buscarJugador(dniField.getText()); // Busca el jugador
                JOptionPane.showMessageDialog(frame, jugador != null ? "Jugador encontrado: " + jugador : "Jugador no encontrado en el equipo.");
                clearFields(buscarLetraField, dniField); // Limpia los campos
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Agrega los botones al panel
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; createPanel.add(crearButton, gbc);
        gbc.gridy = 8; createPanel.add(actualizarButton, gbc);
        gbc.gridy = 9; createPanel.add(eliminarButton, gbc);
        gbc.gridy = 10; createPanel.add(buscarJugadorButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH); // Agrega el panel de creacion
        return panel;
    }

    // Crea el panel para gestionar licencias
    private JPanel createLicenciaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Crea el panel
        panel.setBackground(Color.WHITE); // Establece el fondo blanco
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Agrega un borde vacio

        // Panel para gestionar licencias
        JPanel createPanel = createTitledPanel("Gestionar Licencias");
        GridBagConstraints gbc = new GridBagConstraints(); // Configura las restricciones
        gbc.insets = new Insets(8, 10, 8, 10); // Espaciado
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena horizontalmente

        // Campos de entrada
        JTextField dniField = addField(createPanel, gbc, "DNI Persona:", 0); // Campo para el DNI
        JTextField equipoField = addField(createPanel, gbc, "Letra Equipo:", 1); // Campo para la letra del equipo
        JTextField calcularField = addField(createPanel, gbc, "Letra Equipo para Precio:", 2); // Campo para calcular precio

        // Botones
        JButton crearSimpleButton = new JButton("Crear Licencia Simple", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearSimpleButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton crearEquipoButton = new JButton("Crear Licencia con Equipo", loadIcon("/resources/iconos/cross.png"));
        styleButton(crearEquipoButton, new Color(211, 47, 47), true); // Estiliza el boton
        JButton calcularButton = new JButton("Calcular Precio Licencia", loadIcon("/resources/iconos/precio_licencia.png"));
        styleButton(calcularButton, new Color(33, 37, 41), false); // Estiliza el boton

        // Accion del boton Crear Licencia Simple
        crearSimpleButton.addActionListener(event -> {
            if (!validateFields(dniField)) return; // Valida el campo DNI
            try {
                if (!validateDni(dniField.getText())) throw new IllegalArgumentException("DNI invalido.");
                Persona persona = federacion.buscaPersona(dniField.getText()); // Busca la persona
                if (persona == null) {
                    JOptionPane.showMessageDialog(frame, "Persona no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Licencia licencia = federacion.nuevaLicencia(persona); // Crea la licencia
                JOptionPane.showMessageDialog(frame, "Licencia creada: " + licencia); // Muestra mensaje
                clearFields(dniField); // Limpia el campo
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Crear Licencia con Equipo
        crearEquipoButton.addActionListener(event -> {
            if (!validateFields(dniField, equipoField)) return; // Valida los campos
            try {
                if (!validateDni(dniField.getText())) throw new IllegalArgumentException("DNI invalido.");
                Persona persona = federacion.buscaPersona(dniField.getText()); // Busca la persona
                Equipo equipo = Equipo.buscarPorLetra(equipoField.getText()); // Busca el equipo
                if (persona == null || equipo == null) {
                    JOptionPane.showMessageDialog(frame, persona == null ? "Persona no encontrada." : "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Licencia licencia = federacion.nuevaLicencia(persona, equipo); // Crea la licencia
                JOptionPane.showMessageDialog(frame, "Licencia con equipo creada: " + licencia); // Muestra mensaje
                clearFields(dniField, equipoField); // Limpia los campos
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion del boton Calcular Precio
        calcularButton.addActionListener(event -> {
            if (!validateFields(calcularField)) return; // Valida el campo
            try {
                Equipo equipo = Equipo.buscarPorLetra(calcularField.getText()); // Busca el equipo
                if (equipo == null) {
                    JOptionPane.showMessageDialog(frame, "Equipo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double precio = federacion.calcularPrecioLicencia(equipo); // Calcula el precio
                JOptionPane.showMessageDialog(frame, "Precio de la licencia: " + precio); // Muestra mensaje
                clearFields(calcularField); // Limpia el campo
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Agrega los botones al panel
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; createPanel.add(crearSimpleButton, gbc);
        gbc.gridy = 4; createPanel.add(crearEquipoButton, gbc);
        gbc.gridy = 5; createPanel.add(calcularButton, gbc);

        panel.add(createPanel, BorderLayout.NORTH); // Agrega el panel de creacion
        return panel;
    }

    // Establece el icono de la ventana
    private void setFrameIcon(String path) {
        ImageIcon icon = loadIcon(path); // Carga el icono
        if (icon != null) frame.setIconImage(icon.getImage()); // Establece el icono
    }

    // Crea una etiqueta con el logo
    private JLabel createLogoLabel(String path) {
        ImageIcon icon = loadIcon(path); // Carga el icono
        if (icon != null) {
            Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Escala la imagen
            return new JLabel(new ImageIcon(scaledImage)); // Devuelve la etiqueta con el icono
        }
        return new JLabel(); // Devuelve una etiqueta vacia si no hay icono
    }

    // Carga un icono desde un recurso
    private ImageIcon loadIcon(String path) {
        try {
            URL resource = getClass().getResource(path); // Obtiene el recurso
            if (resource != null) {
                return new ImageIcon(resource); // Devuelve el icono
            } else {
                System.err.println("Recurso no encontrado: " + path); // Muestra error
                return null;
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + path); // Muestra error
            return null;
        }
    }

    // Crea un panel con un titulo y un diseno especifico
    private JPanel createTitledPanel(String title, LayoutManager layout) {
        JPanel panel = new JPanel(layout); // Crea el panel
        panel.setBackground(Color.WHITE); // Establece el fondo blanco
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                title, TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), new Color(33, 37, 41))); // Agrega un borde con titulo
        return panel;
    }

    // Crea un panel con un titulo y diseno GridBagLayout
    private JPanel createTitledPanel(String title) {
        return createTitledPanel(title, new GridBagLayout());
    }

    // Agrega un campo de texto con etiqueta a un panel
    private JTextField addField(JPanel panel, GridBagConstraints gbc, String labelText, int row) {
        JLabel label = new JLabel(labelText); // Crea la etiqueta
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
        JTextField field = new JTextField(20); // Crea el campo de texto
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
        gbc.gridx = 0; gbc.gridy = row; panel.add(label, gbc); // Agrega la etiqueta
        gbc.gridx = 1; panel.add(field, gbc); // Agrega el campo
        return field;
    }

    // Valida que los campos no esten vacios
    private boolean validateFields(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Todos los campos deben estar llenos.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    // Valida el formato del DNI
    private boolean validateDni(String dni) {
        return Pattern.matches("\\d{8}[A-Za-z]", dni); // Valida 8 digitos y una letra
    }

    // Valida el formato de la fecha
    private boolean validateDateFormat(String date) {
        return Pattern.matches("\\d{4}-\\d{2}-\\d{2}", date); // Valida el formato YYYY-MM-DD
    }

    // Lista las categorias en un panel
    private void listCategorias(JPanel listPanel) {
        List<Categoria> categorias = federacion.obtenerCategorias(); // Obtiene las categorias
        JTextArea textArea = new JTextArea(10, 30); // Crea un area de texto
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
        textArea.setText("Categorias:\n"); // Inicializa el texto
        for (Categoria c : categorias) {
            textArea.append(c.toString() + "\n"); // Agrega cada categoria al area de texto
        }
        textArea.setEditable(false); // Hace el area de texto no editable
        listPanel.removeAll(); // Elimina el contenido actual del panel
        listPanel.add(new JButton("Listar Categorias", loadIcon("/resources/iconos/magnifier.png")), BorderLayout.NORTH); // Agrega el boton
        listPanel.add(new JScrollPane(textArea), BorderLayout.CENTER); // Agrega el area de texto con barra de desplazamiento
        listPanel.revalidate(); // Revalida el diseno
        listPanel.repaint(); // Redibuja el panel
    }

    // Lista los clubes en un panel
    private void listClubes(JPanel listPanel) {
        try {
            List<Club> clubes = Club.obtenerTodos(); // Obtiene todos los clubes
            JTextArea textArea = new JTextArea(10, 30); // Crea un area de texto
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
            textArea.setText("Clubes:\n"); // Inicializa el texto
            for (Club c : clubes) {
                textArea.append(c.toString() + "\n"); // Agrega cada club al area de texto
            }
            textArea.setEditable(false); // Hace el area de texto no editable
            listPanel.removeAll(); // Elimina el contenido actual del panel
            listPanel.add(new JButton("Listar Clubes", loadIcon("/resources/iconos/magnifier.png")), BorderLayout.NORTH); // Agrega el boton
            listPanel.add(new JScrollPane(textArea), BorderLayout.CENTER); // Agrega el area de texto con barra de desplazamiento
            listPanel.revalidate(); // Revalida el diseno
            listPanel.repaint(); // Redibuja el panel
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra mensaje de error
        }
    }

    // Muestra una lista de resultados en un cuadro de dialogo
    private void showListResult(List<?> list, String title) {
        JTextArea textArea = new JTextArea(10, 30); // Crea un area de texto
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Establece la fuente
        textArea.setText(title + "\n"); // Inicializa el texto con el titulo
        for (Object item : list) {
            textArea.append(item.toString() + "\n"); // Agrega cada elemento al area de texto
        }
        textArea.setEditable(false); // Hace el area de texto no editable
        JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Resultados", JOptionPane.INFORMATION_MESSAGE); // Muestra el dialogo con los resultados
    }

    // Aplica estilos a un menu
    private void styleMenu(JMenu menu, Font font, Color foreground, Color hoverColor) {
        menu.setFont(font); // Establece la fuente
        menu.setForeground(foreground); // Establece el color del texto
        menu.setOpaque(true); // Hace el menu opaco
        menu.setBackground(new Color(33, 37, 41)); // Establece el color de fondo
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor a mano
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { menu.setBackground(hoverColor); } // Cambia el fondo al pasar el raton
            @Override
            public void mouseExited(MouseEvent e) { menu.setBackground(new Color(33, 37, 41)); } // Restaura el fondo al salir
        });
    }

    // Aplica estilos a un elemento de menu
    private void styleMenuItem(JMenuItem item, Font font, Color foreground, Color hoverColor) {
        item.setFont(font); // Establece la fuente
        item.setForeground(foreground); // Establece el color del texto
        item.setBackground(new Color(33, 37, 41)); // Establece el color de fondo
        item.setOpaque(true); // Hace el elemento opaco
        item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Agrega un borde vacio
        item.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor a mano
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { item.setBackground(hoverColor); } // Cambia el fondo al pasar el raton
            @Override
            public void mouseExited(MouseEvent e) { item.setBackground(new Color(33, 37, 41)); } // Restaura el fondo al salir
        });
    }

    // Aplica estilos a un boton
    private void styleButton(JButton button, Color bgColor, boolean isCreateButton) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Establece la fuente
        button.setBackground(bgColor); // Establece el color de fondo
        button.setForeground(Color.WHITE); // Establece el color del texto
        button.setFocusPainted(false); // Elimina el borde de foco
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25)); // Agrega un borde vacio
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor a mano
        Color hoverColor = isCreateButton ? new Color(239, 83, 80) : new Color(66, 66, 66); // Define el color al pasar el raton
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { button.setBackground(hoverColor); } // Cambia el fondo al pasar el raton
            @Override
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); } // Restaura el fondo al salir
        });
    }

    // Limpia los campos de texto
    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText(""); // Establece el texto a vacio
        }
    }

    // Metodo principal para ejecutar la aplicacion
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp2().createAndShowGUI()); // Ejecuta la creacion de la interfaz en el hilo de eventos
    }
}