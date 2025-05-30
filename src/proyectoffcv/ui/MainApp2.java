package proyectoffcv.ui;

import entidades.*;
import java.awt.BorderLayout;
import proyectoffcv.logica.Federacion;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;
import javax.swing.border.TitledBorder;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Cursor;
import javax.swing.UnsupportedLookAndFeelException;
import java.time.LocalDate;
import java.awt.BasicStroke;
import java.awt.AlphaComposite;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.RadialGradientPaint;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.Icon;
import javax.swing.Timer; 

public class MainApp2 {

    private Federacion federacion;
    private JFrame frame;
    private JPanel contentPanel;
    private JMenuBar menuBar;
    private JTextArea errorArea;
    private JScrollPane errorScrollPane;
    
    // Colores modernos y profesionales
    private static final Color PRIMARY_COLOR = new Color(26, 38, 57);      // #1A2639
    private static final Color SECONDARY_COLOR = new Color(59, 130, 246);   // #3B82F6
    private static final Color ACCENT_COLOR = new Color(248, 113, 113);    // #F87171
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250); // #F5F7FA
    private static final Color TEXT_PRIMARY = new Color(31, 42, 68);        // #1F2A44
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);   // #6B7280
    private static final Color ERROR_COLOR = new Color(231, 76, 60);        // #E74C3C
    private static final Color BORDER_COLOR = new Color(229, 231, 235);     // #E5E7EB  
    
    public MainApp2() {
        federacion = Federacion.getInstance();

        errorArea = new JTextArea(3, 30);
        errorArea.setEditable(false);
        errorArea.setForeground(ERROR_COLOR);
        errorArea.setBackground(new Color(255, 248, 248));
        errorArea.setFont(new Font("Inter", Font.PLAIN, 11));
        errorArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(254, 226, 226), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        errorScrollPane = new JScrollPane(errorArea);
        errorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        errorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        errorScrollPane.setBorder(BorderFactory.createEmptyBorder());
        errorScrollPane.getViewport().setBackground(new Color(255, 248, 248));

        initialize();
    }
    
    // Método para crear un menú con estilo
    private JMenu createMenu(String title, String iconPath) {
        JMenu menu = new JMenu(" " + title + " ");
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Inter", Font.BOLD, 13));
        menu.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        URL iconURL = getClass().getResource("/resources/iconos/" + iconPath);
        if (iconURL != null) {
            ImageIcon originalIcon = new ImageIcon(iconURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            menu.setIcon(new ImageIcon(scaledImage));
            menu.setIconTextGap(10);
        }

        return menu;
    }
    
    public static void main(String[] args) {
        try {
            // Configurar Look and Feel moderno
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Configuraciones adicionales para mejorar la apariencia
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                MainApp2 app = new MainApp2();
                app.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error al inicializar la aplicación: " + e.getMessage(),
                    "Error de Inicialización",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void initialize() {
        frame = new JFrame("Gestión de Federación FFCV - Sistema Profesional");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 900);
        frame.setMinimumSize(new Dimension(1200, 800));
        frame.setLocationRelativeTo(null);
        
        // Añadir ícono a la ventana
        URL iconURL = getClass().getResource("/resources/logo.png");
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            Image scaledImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // Escala a 32x32
            frame.setIconImage(scaledImage);
        } else {
            System.err.println("No se pudo cargar el ícono: /resources/logo.png");
        }
       
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        menuBar = new JMenuBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    0, getHeight(), new Color(44, 62, 80)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        menuBar.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        menuBar.setOpaque(false);
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 40, 0));

        JMenu clubsMenu = createMenu("Clubs", "club.png");
        addMenuItem(clubsMenu, "Alta Club", e -> showAltaClubPanel(), "cross.png");
        addMenuItem(clubsMenu, "Listar Clubes", e -> showListarClubesPanel(), "list.png");
        menuBar.add(clubsMenu);
        menuBar.revalidate();

        JMenu equiposMenu = createMenu("Equipos", "teams.png");
        addMenuItem(equiposMenu, "Alta Equipo", e -> showAltaEquipoPanel(), "cross.png");
        addMenuItem(equiposMenu, "Listar Equipos", e -> showListarEquiposPanel(), "list.png");
        menuBar.add(equiposMenu);
        menuBar.revalidate();

        JMenu categoriasGruposMenu = createMenu("Categorías y Grupos", "categorias.png");
        addMenuItem(categoriasGruposMenu, "Alta Categoría", e -> showAltaCategoriaPanel(), "cross.png");
        addMenuItem(categoriasGruposMenu, "Alta Grupo", e -> showAltaGrupoPanel(), "cross.png");
        addMenuItem(categoriasGruposMenu, "Listar Categorías", e -> showListarCategoriasPanel(), "list.png");
        addMenuItem(categoriasGruposMenu, "Listar Grupos por Categoría", e -> showListarGruposPorCategoriaPanel(), "list.png");
        menuBar.add(categoriasGruposMenu);
        menuBar.revalidate();

        JMenu personasMenu = createMenu("Personas", "persona.png");
        addMenuItem(personasMenu, "Alta Persona", e -> showAltaPersonaPanel(), "cross.png");
        addMenuItem(personasMenu, "Alta Empleado", e -> showAltaEmpleadoPanel(), "cross.png");
        addMenuItem(personasMenu, "Buscar Persona por DNI", e -> showBuscarPersonaDniPanel(), "magnifier.png");
        addMenuItem(personasMenu, "Buscar Personas por Nombre", e -> showBuscarPersonasPorNombrePanel(), "magnifier.png");
        addMenuItem(personasMenu, "Listar Personas", e -> showListarPersonasPanel(), "list.png");
        addMenuItem(personasMenu, "Listar Empleados", e -> showListarEmpleadosPanel(), "list.png");
        menuBar.add(personasMenu);
        menuBar.revalidate();

        JMenu licenciasMenu = createMenu("Licencias", "licencia.png");
        addMenuItem(licenciasMenu, "Alta Licencia (sin equipo)", e -> showAltaLicenciaSinEquipoPanel(), "cross.png");
        addMenuItem(licenciasMenu, "Alta Licencia (con equipo)", e -> showAltaLicenciaConEquipoPanel(), "cross.png");
        addMenuItem(licenciasMenu, "Añadir Licencia a Equipo", e -> showAddLicenciaAEquipoPanel(), "cross.png");
        addMenuItem(licenciasMenu, "Calcular Precio Licencia", e -> showCalcularPrecioLicenciaPanel(), "calculator.png");
        addMenuItem(licenciasMenu, "Listar Licencias", e -> showListarLicenciasPanel(), "list.png");
        menuBar.add(licenciasMenu);
        menuBar.revalidate();

        JMenu instalacionesMenu = createMenu("Instalaciones", "instalaciones.png");
        addMenuItem(instalacionesMenu, "Alta Instalación", e -> showAltaInstalacionPanel(), "cross.png");
        addMenuItem(instalacionesMenu, "Buscar Instalaciones", e -> showBuscarInstalacionesPanel(), "magnifier.png");
        menuBar.add(instalacionesMenu);
        menuBar.revalidate();
        
        JMenu utilidadesMenu = createMenu("Utilidades", "tools.png");
        addMenuItem(utilidadesMenu, "Limpiar Base de Datos", e -> limpiarBaseDeDatos(), "trash.png");
        menuBar.add(utilidadesMenu);
        menuBar.revalidate();

        frame.setJMenuBar(menuBar);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(20, 20));
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(BACKGROUND_COLOR);

        JPanel southPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(252, 253, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            }
        };
        southPanel.setOpaque(false);
        southPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 0, 0),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                BorderFactory.createEmptyBorder(),
                                "💬 Mensajes del Sistema",
                                TitledBorder.LEFT, TitledBorder.TOP,
                                new Font("Inter", Font.BOLD, 16), PRIMARY_COLOR
                        ),
                        BorderFactory.createEmptyBorder(10, 20, 20, 20)
                )
        ));
        southPanel.add(errorScrollPane, BorderLayout.CENTER);
        southPanel.setPreferredSize(new Dimension(frame.getWidth(), 140));

        frame.add(contentPanel, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);
        menuBar.revalidate();
        menuBar.repaint();

        showWelcomePanel();
    }

    // Método para añadir elementos al menú
    private void addMenuItem(JMenu menu, String text, ActionListener listener, String iconPath) {
        JMenuItem menuItem = new JMenuItem("  " + text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color textColor; // Variable para almacenar el color del texto

                g2d.setColor(PRIMARY_COLOR);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                if (getModel().isArmed() || getModel().isSelected()) {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(30, 144, 255), // Azul más oscuro
                        getWidth(), 0, new Color(0, 120, 215, 200) // Azul más claro con transparencia
                    );
                    g2d.setPaint(gradient);
                    // Utiliza fillRoundRect para las esquinas redondeadas del resalte
                    g2d.fillRoundRect(4, 2, getWidth() - 8, getHeight() - 4, 6, 6);

                    // El texto será blanco cuando esté resaltado
                    textColor = Color.WHITE; 
                } else {
                    // El texto será gris claro en estado normal
                    textColor = new Color(236, 240, 241);
                }

                g2d.setColor(textColor);
                g2d.setFont(getFont()); // Usa la fuente que ya está configurada en menuItem

                Insets insets = getInsets(); // Obtiene los bordes/relleno del JMenuItem
                int iconWidth = 0;
                int textX = insets.left; // La posición inicial del texto (izquierda + insets)

                Icon icon = getIcon();
                if (icon != null) {
                    iconWidth = icon.getIconWidth();
                    int iconY = (getHeight() - icon.getIconHeight()) / 2; // Centrar el icono verticalmente
                    icon.paintIcon(this, g2d, insets.left, iconY); // Dibuja el icono
                    textX += iconWidth + getIconTextGap(); // Mueve la posición del texto para dejar espacio al icono
                }
                
                // Dibuja el texto
                String itemText = getText();
                FontMetrics fm = g2d.getFontMetrics();
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent(); // Centrar el texto verticalmente

                g2d.drawString(itemText, textX, textY);
                }
        };

        menuItem.addActionListener(listener);
        menuItem.setForeground(new Color(236, 240, 241));
        menuItem.setFont(new Font("Inter", Font.PLAIN, 13));
        menuItem.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        menuItem.setOpaque(true); 

        URL iconURL = getClass().getResource("/resources/iconos/" + iconPath);
        if (iconURL != null) {
            ImageIcon originalIcon = new ImageIcon(iconURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            menuItem.setIcon(new ImageIcon(scaledImage)); // Sigue configurando el icono para que getIcon() lo obtenga
            menuItem.setIconTextGap(12); // Sigue configurando el espaciado para que getIconTextGap() lo obtenga
        }

        menu.add(menuItem);
    }
    
    // Método para actualizar el panel de contenido principal
    private void updateContentPanel(JPanel panel, String title) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout(20, 20));

        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(248, 250, 252)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(20, getHeight() - 1, getWidth() - 20, getHeight() - 1);
            }
        };
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(10, 0, 15, 0));

        JLabel panelTitle = new JLabel(title, SwingConstants.CENTER);
        panelTitle.setFont(new Font("Inter", Font.BOLD, 20));
        panelTitle.setForeground(PRIMARY_COLOR);
        panelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel decorativeLine = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                LinearGradientPaint gradient = new LinearGradientPaint(
                    0, getHeight() / 2f, getWidth(), getHeight() / 2f,
                    new float[]{0.0f, 0.3f, 0.7f, 1.0f},
                    new Color[]{
                        new Color(52, 152, 219, 0),
                        SECONDARY_COLOR,
                        ACCENT_COLOR,
                        new Color(46, 204, 113, 0)
                    }
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
            }
        };
        decorativeLine.setPreferredSize(new Dimension(300, 6));
        decorativeLine.setMaximumSize(new Dimension(300, 6));
        decorativeLine.setAlignmentX(Component.CENTER_ALIGNMENT);
        decorativeLine.setOpaque(false);

        titlePanel.add(panelTitle);
        titlePanel.add(Box.createVerticalStrut(15));
        titlePanel.add(decorativeLine);

        contentPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Envuelve el panel de contenido en un JScrollPane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Eliminar el borde predeterminado
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR); // Coincidir el color de fondo
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Generalmente no es necesario para formularios

        contentPanel.add(scrollPane, BorderLayout.CENTER); // Añadir el JScrollPane en lugar del panel directamente
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Panel de bienvenida con animación
    private void showWelcomePanel() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout(30, 30));

        // Instancia la nueva clase AnimatedWelcomePanel
        AnimatedWelcomePanel welcomePanel = new AnimatedWelcomePanel();

        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(80, 100, 80, 100));
        // welcomePanel.setOpaque(false); // Ya está en el constructor de AnimatedWelcomePanel

        // Logo si existe
        URL logoURL = getClass().getResource("/resources/logo.png");
        if (logoURL != null) {
            ImageIcon originalIcon = new ImageIcon(logoURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            welcomePanel.add(logoLabel);
            welcomePanel.add(Box.createVerticalStrut(40));
        }

        // Título principal
        JLabel welcomeTitle = new JLabel("🏆 Federación FFCV");
        welcomeTitle.setFont(new Font("Inter", Font.BOLD, 36));
        welcomeTitle.setForeground(PRIMARY_COLOR);
        welcomeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo
        JLabel subtitle = new JLabel("Sistema de Gestión Profesional");
        subtitle.setFont(new Font("Inter", Font.PLAIN, 18));
        subtitle.setForeground(SECONDARY_COLOR);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Mensaje de bienvenida
        JLabel welcomeMessage = new JLabel("<html><div style='text-align: center; color: #7f8c8d;'>" +
                "Bienvenido al sistema de gestión integral de la FFCV.<br>" +
                "Utilice el menú superior para gestionar clubes, equipos, personas,<br>" +
                "licencias e instalaciones de manera eficiente y profesional." +
                "</div></html>");
        welcomeMessage.setFont(new Font("Inter", Font.PLAIN, 14));
        welcomeMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeMessage.setBorder(new EmptyBorder(20, 0, 30, 0));
        welcomeMessage.setHorizontalAlignment(SwingConstants.CENTER); // Asegurar alineación horizontal

        // Indicadores de funcionalidades
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.X_AXIS));
        featuresPanel.setOpaque(false);
        featuresPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] features = {"👥 Gestión de Personas", "🏟️ Control de Instalaciones", "📋 Administración de Licencias"};
        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Inter", Font.PLAIN, 12));
            featureLabel.setForeground(TEXT_SECONDARY);
            featureLabel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            featuresPanel.add(featureLabel);
            if (!feature.equals(features[features.length - 1])) {
                featuresPanel.add(Box.createHorizontalStrut(15));
            }
        }

        welcomePanel.add(welcomeTitle);
        welcomePanel.add(Box.createVerticalStrut(15));
        welcomePanel.add(subtitle);
        welcomePanel.add(Box.createVerticalStrut(25));
        welcomePanel.add(welcomeMessage);
        welcomePanel.add(featuresPanel);

        contentPanel.add(welcomePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();

        // Animación de fade-in
        Timer timer = new Timer(20, new ActionListener() {
            private float alpha = 0.0f;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                alpha += 0.02f; // Incrementar la transparencia
                if (alpha > 1.0f) {
                    alpha = 1.0f;
                    ((Timer)e.getSource()).stop(); // Detener el timer cuando la animación termina
                }
                welcomePanel.setAlpha(alpha); // Ahora welcomePanel es de tipo AnimatedWelcomePanel y tiene setAlpha
            }
        });
        timer.start();
    }

    // Método para crear un panel de formulario con estilo
    private JPanel createFormPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(BACKGROUND_COLOR); // Usar BACKGROUND_COLOR en lugar de blanco
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                g2d.setColor(Color.BLACK);
                for (int i = 1; i <= 6; i++) {
                    g2d.drawRoundRect(-i, -i, getWidth() + 2*i, getHeight() + 2*i, 16 + i, 16 + i);
                }
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setOpaque(false);
        return panel;
    }
    
    private static final Dimension TEXT_FIELD_SIZE = new Dimension(300, 36);
    private JPanel createFormFieldPanel(JLabel label, JTextField textField) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridBagLayout());
        fieldPanel.setOpaque(false);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0)); // Espacio vertical entre cada par de etiqueta-campo

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espacio entre la etiqueta y el campo
        gbc.anchor = GridBagConstraints.WEST; // Alineación a la izquierda para la etiqueta

        // Configuración para la etiqueta (Label)
        label.setFont(new Font("Inter", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        gbc.gridx = 0; // Columna 0 para la etiqueta
        gbc.gridy = 0; // Fila 0
        gbc.weightx = 0.0; // La etiqueta no se estira horizontalmente
        gbc.fill = GridBagConstraints.NONE; // No rellenar espacio adicional
        fieldPanel.add(label, gbc);

        // Configuración para el campo de texto (JTextField)
        // Establecer el tamaño mínimo, preferido y máximo para forzar la uniformidad
        textField.setPreferredSize(TEXT_FIELD_SIZE);
        textField.setMinimumSize(TEXT_FIELD_SIZE);
        textField.setMaximumSize(TEXT_FIELD_SIZE);
        
        textField.setFont(new Font("Inter", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        textField.setBackground(new Color(252, 253, 255));

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 2),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });

        gbc.gridx = 1; // Columna 1 para el campo de texto
        gbc.weightx = 1.0; // Permitir que el campo de texto use el espacio restante en su columna (aunque su tamaño ya está fijado)
        gbc.fill = GridBagConstraints.HORIZONTAL; // Permitir que el campo de texto rellene horizontalmente (hasta su tamaño máximo)
        fieldPanel.add(textField, gbc);

        return fieldPanel;
    }

    // Método para crear un botón de acción con estilo
    private JButton createActionButton(String text, ActionListener listener) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color startColor, endColor;
                if (getModel().isPressed()) {
                    startColor = new Color(41, 128, 185);
                    endColor = new Color(52, 152, 219);
                } else if (getModel().isRollover()) {
                    startColor = new Color(52, 152, 219);
                    endColor = new Color(74, 144, 226);
                } else {
                    startColor = SECONDARY_COLOR;
                    endColor = new Color(41, 128, 185);
                }

                GradientPaint gradient = new GradientPaint(
                    0, 0, startColor,
                    0, getHeight(), endColor
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);

                super.paintComponent(g);
            }
        };

        button.addActionListener(listener);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Inter", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(280, 48));
        button.setMaximumSize(new Dimension(280, 48));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(Cursor.getDefaultCursor());
                button.repaint(); // Repintar para restaurar el color si se cambia con el rollover
            }
        });
        return button;
    }
   
    // Método para mostrar mensajes en el área de errores
    private void showMessage(String message, boolean isError) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = java.time.format.DateTimeFormatter
                .ofPattern("HH:mm:ss")
                .format(java.time.LocalTime.now());

            String icon = isError ? "❌" : "✅";
            String formattedMessage = String.format("[%s] %s %s\n", timestamp, icon, message);

            errorArea.append(formattedMessage);
            errorArea.setCaretPosition(errorArea.getDocument().getLength());

            if (isError) {
                errorArea.setBackground(new Color(255, 248, 248));
                errorScrollPane.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(254, 226, 226), 2),
                    BorderFactory.createEmptyBorder()
                ));
            } else {
                errorArea.setBackground(new Color(248, 255, 248));
                errorScrollPane.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 255, 220), 2),
                    BorderFactory.createEmptyBorder()
                ));
            }
        });
    }
    
    // Panel para listar clubes con diseño tipo tarjetas
    private void showListarClubesPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);

        try {
            List<Club> clubes = federacion.obtenerClubes(); // Corregido el nombre del método

            if (clubes.isEmpty()) {
                JPanel emptyPanel = createEmptyStatePanel("🏆", "No hay clubes registrados",
                    "Comience agregando un nuevo club desde el menú superior.");
                mainPanel.add(emptyPanel, BorderLayout.CENTER);
            } else {
                JPanel clubesPanel = new JPanel();
                clubesPanel.setLayout(new BoxLayout(clubesPanel, BoxLayout.Y_AXIS));
                clubesPanel.setOpaque(false);

                for (Club club : clubes) {
                    JPanel clubCard = createClubCard(club);
                    clubesPanel.add(clubCard);
                    clubesPanel.add(Box.createVerticalStrut(15));
                }

                JScrollPane scrollPane = new JScrollPane(clubesPanel);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                mainPanel.add(scrollPane, BorderLayout.CENTER);
            }

        } catch (Exception ex) { // Capturar Exception genérica ya que obtenerClubes no lanza SQLException
            showMessage("Error al cargar la lista de clubes: " + ex.getMessage(), true);
            JPanel errorPanel = createErrorPanel("Error al cargar clubes", ex.getMessage());
            mainPanel.add(errorPanel, BorderLayout.CENTER);
        }

        updateContentPanel(mainPanel, "📋 Lista de Clubes");
    } 

    // Crear tarjeta visual para cada club
    private JPanel createClubCard(Club club) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 12, 12); // Reducido de 16 a 12
                GradientPaint gradient = new GradientPaint(
                        0, 0, Color.WHITE,
                        0, getHeight(), new Color(252, 253, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 12, 12);
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 12, 12);
                g2d.setColor(SECONDARY_COLOR);
                g2d.setStroke(new BasicStroke(4)); // Reducido de 6 a 4
                g2d.drawLine(6, 12, 6, getHeight() - 12); // Ajustado de 16 a 12
            }
        };

        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20)); // Reducido de 20, 30, 20, 20
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Reducido de 120 a 100

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // Reducido de 5, 15, 5, 15

        JLabel nombreLabel = new JLabel("🏆 " + club.getNombre());
        nombreLabel.setFont(new Font("Inter", Font.BOLD, 16)); // Cambiado de 20 a 16
        nombreLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(nombreLabel, gbc);

        JLabel presidenteLabel = new JLabel("👤 " + club.getPresidente().getNombre() + " " + club.getPresidente().getApellido1());
        presidenteLabel.setFont(new Font("Inter", Font.PLAIN, 14)); // Cambiado de 16 a 14
        presidenteLabel.setForeground(TEXT_SECONDARY);
        gbc.gridx = 1; gbc.gridy = 1;
        card.add(presidenteLabel, gbc);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setCursor(Cursor.getDefaultCursor());
                card.repaint();
            }
        });

        return card;
    }

    // Panel de estado vacío elegante
    private JPanel createEmptyStatePanel(String icon, String title, String message) {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setOpaque(false);
        emptyPanel.setBorder(BorderFactory.createEmptyBorder(120, 50, 40, 50));
        
        // Asegurar que el panel mismo intente centrar sus componentes
        emptyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Icono grande
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar el icono
        iconLabel.setForeground(new Color(189, 195, 199));
        
        // Título
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar el título
        
        // Mensaje
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        messageLabel.setForeground(TEXT_SECONDARY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar el mensaje
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Asegurar alineación horizontal del texto HTML
        
        emptyPanel.add(iconLabel);
        emptyPanel.add(Box.createVerticalStrut(10));
        emptyPanel.add(titleLabel);
        emptyPanel.add(Box.createVerticalStrut(5));
        emptyPanel.add(messageLabel);
        
        return emptyPanel;
    }

    // Panel de error elegante
    private JPanel createErrorPanel(String title, String errorMessage) {
        JPanel errorPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente de error sutil
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 248, 248),
                    0, getHeight(), new Color(255, 235, 235)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                
                // Borde de error
                g2d.setColor(new Color(254, 226, 226));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            }
        };
        
        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
        errorPanel.setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));
        errorPanel.setOpaque(false);
        
        // Icono de error
        JLabel iconLabel = new JLabel("⚠️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Título del error
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
        titleLabel.setForeground(ERROR_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Mensaje de error
        JLabel messageLabel = new JLabel("<html><div style='text-align: center; color: #e74c3c;'>" + 
                                        errorMessage + "</div></html>");
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        errorPanel.add(iconLabel);
        errorPanel.add(Box.createVerticalStrut(15));
        errorPanel.add(titleLabel);
        errorPanel.add(Box.createVerticalStrut(10));
        errorPanel.add(messageLabel);
        
        return errorPanel;
    }

    // Panel para Alta Equipo
    private Club selectedClub;
    private void showAltaEquipoPanel() {
        JPanel formPanel = createFormPanel();

        JTextField letraField = new JTextField();
        JTextField clubSeleccionadoField = new JTextField();
        clubSeleccionadoField.setEditable(false);
        JButton seleccionarClubButton = createActionButton("Seleccionar Club", e -> {
            List<Club> clubes = federacion.obtenerClubes();
            if (clubes.isEmpty()) {
                showMessage("No hay clubes registrados. Por favor, cree un club primero.", true);
                return;
            }
            String[] nombresClubes = clubes.stream().map(Club::getNombre).toArray(String[]::new);
            String selectedClubName = (String) JOptionPane.showInputDialog(
                frame,
                "Seleccione un Club:",
                "Seleccionar Club",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombresClubes,
                nombresClubes[0]);

            if (selectedClubName != null) {
                selectedClub = federacion.buscarClub(selectedClubName);
                if (selectedClub != null) {
                    clubSeleccionadoField.setText(selectedClub.getNombre());
                    showMessage("Club '" + selectedClub.getNombre() + "' seleccionado.", false);
                }
            }
        });

        JTextField instalacionField = new JTextField();
        JTextField grupoField = new JTextField();
        JTextField categoriaField = new JTextField();

        formPanel.add(createFormFieldPanel(new JLabel("Letra del Equipo:"), letraField));
        formPanel.add(createFormFieldPanel(new JLabel("Club Seleccionado:"), clubSeleccionadoField));
        formPanel.add(seleccionarClubButton);
        formPanel.add(createFormFieldPanel(new JLabel("Nombre de Instalación:"), instalacionField));
        formPanel.add(createFormFieldPanel(new JLabel("Nombre de Categoría del Grupo:"), categoriaField));
        formPanel.add(createFormFieldPanel(new JLabel("Nombre del Grupo:"), grupoField));

        formPanel.add(Box.createVerticalStrut(30));

        JButton altaButton = createActionButton("⚽ Dar de Alta Equipo", e -> {
            String letra = letraField.getText().trim();
            String nombreInstalacion = instalacionField.getText().trim();
            String nombreCategoria = categoriaField.getText().trim();
            String nombreGrupo = grupoField.getText().trim();

            if (letra.isEmpty() || nombreInstalacion.isEmpty() || nombreCategoria.isEmpty() || nombreGrupo.isEmpty()) {
                showMessage("Por favor, complete todos los campos obligatorios.", true);
                return;
            }

            if (selectedClub == null) {
                showMessage("Por favor, seleccione un Club primero.", true);
                return;
            }

            try {
                List<Instalacion> instalacionesEncontradas = federacion.buscarInstalaciones(nombreInstalacion);
                Instalacion instalacion = null;
                if (!instalacionesEncontradas.isEmpty()) {
                    instalacion = instalacionesEncontradas.get(0);
                } else {
                    showMessage("Instalación no encontrada: " + nombreInstalacion + ". Cree la instalación primero.", true);
                    return;
                }

                Categoria categoria = null;
                for (Categoria c : federacion.obtenerCategorias()) {
                    if (c.getNombre().equalsIgnoreCase(nombreCategoria)) {
                        categoria = c;
                        break;
                    }
                }
                if (categoria == null) {
                    showMessage("Categoría no encontrada: " + nombreCategoria + ". Cree la categoría primero.", true);
                    return;
                }

                Grupo grupo = null;
                List<Grupo> gruposEncontrados = federacion.obtenerGrupos(categoria);
                for (Grupo g : gruposEncontrados) {
                    if (g.getNombre().equalsIgnoreCase(nombreGrupo)) {
                        grupo = g;
                        break;
                    }
                }
                if (grupo == null) {
                    showMessage("Grupo no encontrado en la categoría '" + nombreCategoria + "': " + nombreGrupo + ". Cree el grupo primero.", true);
                    return;
                }

                Equipo nuevoEquipo = federacion.nuevoEquipo(letra, instalacion, grupo, selectedClub);
                showMessage("Equipo '" + letra + "' dado de alta exitosamente para el club '" + selectedClub.getNombre() + "'.", false);

                letraField.setText("");
                instalacionField.setText("");
                categoriaField.setText("");
                grupoField.setText("");
                clubSeleccionadoField.setText("");
                selectedClub = null; // Limpiar selección
            } catch (IllegalStateException ex) {
                showMessage("Error: " + ex.getMessage(), true);
            } catch (RuntimeException ex) {
                showMessage("Error al dar de alta el equipo: " + ex.getMessage(), true);
            } catch (Exception ex) {
                showMessage("Error inesperado al dar de alta el equipo: " + ex.getMessage(), true);
            }
        });

        formPanel.add(altaButton);
        updateContentPanel(formPanel, "⚽ Alta de Equipo");
    }

    // Panel para Alta Persona
    private void showAltaPersonaPanel() {
        JPanel formPanel = createFormPanel();

        JTextField dniField = new JTextField();
        JTextField nombreField = new JTextField();
        JTextField apellido1Field = new JTextField();
        JTextField apellido2Field = new JTextField();
        JTextField fechaNacimientoField = new JTextField();
        JTextField usuarioField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField poblacionField = new JTextField();
        

        fechaNacimientoField.setToolTipText("Formato: YYYY-MM-DD (ej: 1990-12-25)");

        formPanel.add(createFormFieldPanel(new JLabel("DNI:"), dniField));
        formPanel.add(createFormFieldPanel(new JLabel("Nombre:"), nombreField));
        formPanel.add(createFormFieldPanel(new JLabel("Primer Apellido:"), apellido1Field)); // Actualizado el label
        formPanel.add(createFormFieldPanel(new JLabel("Segundo Apellido:"), apellido2Field)); // Nuevo campo
        formPanel.add(createFormFieldPanel(new JLabel("Fecha de Nacimiento (YYYY-MM-DD):"), fechaNacimientoField));
        formPanel.add(createFormFieldPanel(new JLabel("Usuario:"), usuarioField));
        formPanel.add(createFormFieldPanel(new JLabel("Contraseña:"), passwordField));
        formPanel.add(createFormFieldPanel(new JLabel("Población:"), poblacionField));
        formPanel.add(Box.createVerticalStrut(30));

        JButton altaButton = createActionButton("👤 Dar de Alta Persona", e -> {
            String dni = dniField.getText().trim();
            String nombre = nombreField.getText().trim();
            String apellido1 = apellido1Field.getText().trim();
            String apellido2 = apellido2Field.getText().trim();
            String fechaStr = fechaNacimientoField.getText().trim();
            String usuario = usuarioField.getText().trim();
            String password = passwordField.getText().trim();
            String poblacion = poblacionField.getText().trim();


            if (dni.isEmpty() || nombre.isEmpty() || apellido1.isEmpty() || fechaStr.isEmpty() ||
                usuario.isEmpty() || password.isEmpty() || poblacion.isEmpty()) {
                showMessage("Por favor, complete todos los campos obligatorios.", true);
                return;
            }

            try {
                LocalDate fechaNacimiento = LocalDate.parse(fechaStr);
                Persona nuevaPersona = federacion.nuevaPersona(dni, nombre, apellido1, apellido2,
                                                              fechaNacimiento, usuario, password, poblacion); // Corregido
                showMessage("Persona '" + nombre + " " + apellido1 + "' dada de alta exitosamente.", false);

                // Limpiar campos
                dniField.setText("");
                nombreField.setText("");
                apellido1Field.setText("");
                apellido2Field.setText("");
                fechaNacimientoField.setText("");
                usuarioField.setText("");
                passwordField.setText("");
                poblacionField.setText("");

            } catch (DateTimeParseException ex) {
                showMessage("Formato de fecha de nacimiento incorrecto. Use: YYYY-MM-DD", true);
            } catch (RuntimeException ex) { // Capturar las RuntimeException lanzadas desde Federacion
                showMessage("Error al dar de alta la persona: " + ex.getMessage(), true);
            } catch (Exception ex) {
                showMessage("Error inesperado: " + ex.getMessage(), true);
            }
        });

        formPanel.add(altaButton);
        updateContentPanel(formPanel, "👤 Alta de Persona");
    }

    // Panel para Alta Club
    private void showAltaClubPanel() {
        JPanel formPanel = createFormPanel();

        JTextField nombreField = new JTextField();
        JTextField fechaFundacionField = new JTextField();
        JTextField presidenteDNIField = new JTextField();
        JTextField presidenteNombreField = new JTextField();
        JTextField presidenteApellido1Field = new JTextField();
        JTextField presidenteApellido2Field = new JTextField();
        JTextField presidenteFechaNacimientoField = new JTextField();
        JTextField presidenteUsuarioField = new JTextField();
        JTextField presidentePasswordField = new JTextField();
        JTextField presidentePoblacionField = new JTextField();


        fechaFundacionField.setToolTipText("Formato: YYYY-MM-DD (ej: 2000-01-01)");
        presidenteFechaNacimientoField.setToolTipText("Formato: YYYY-MM-DD (ej: 1980-05-15)");

        formPanel.add(createFormFieldPanel(new JLabel("Nombre del Club:"), nombreField));
        formPanel.add(createFormFieldPanel(new JLabel("Fecha Fundación (YYYY-MM-DD):"), fechaFundacionField));
        formPanel.add(createFormFieldPanel(new JLabel("DNI Presidente:"), presidenteDNIField));
        formPanel.add(createFormFieldPanel(new JLabel("Nombre Presidente:"), presidenteNombreField));
        formPanel.add(createFormFieldPanel(new JLabel("Primer Apellido Presidente:"), presidenteApellido1Field));
        formPanel.add(createFormFieldPanel(new JLabel("Segundo Apellido Presidente:"), presidenteApellido2Field));
        formPanel.add(createFormFieldPanel(new JLabel("Fecha Nac. Presidente (YYYY-MM-DD):"), presidenteFechaNacimientoField));
        formPanel.add(createFormFieldPanel(new JLabel("Usuario Presidente:"), presidenteUsuarioField));
        formPanel.add(createFormFieldPanel(new JLabel("Password Presidente:"), presidentePasswordField));
        formPanel.add(createFormFieldPanel(new JLabel("Población Presidente:"), presidentePoblacionField));

        formPanel.add(Box.createVerticalStrut(30));

        JButton altaButton = createActionButton("🏆 Dar de Alta Club", e -> {
            String nombre = nombreField.getText().trim();
            String fechaFundacionStr = fechaFundacionField.getText().trim();
            String presidenteDNI = presidenteDNIField.getText().trim();
            String presidenteNombre = presidenteNombreField.getText().trim();
            String presidenteApellido1 = presidenteApellido1Field.getText().trim();
            String presidenteApellido2 = presidenteApellido2Field.getText().trim();
            String presidenteFechaNacimientoStr = presidenteFechaNacimientoField.getText().trim();
            String presidenteUsuario = presidenteUsuarioField.getText().trim();
            String presidentePassword = presidentePasswordField.getText().trim();
            String presidentePoblacion = presidentePoblacionField.getText().trim();


            if (nombre.isEmpty() || fechaFundacionStr.isEmpty() || presidenteDNI.isEmpty() ||
                presidenteNombre.isEmpty() || presidenteApellido1.isEmpty() ||
                presidenteFechaNacimientoStr.isEmpty() || presidenteUsuario.isEmpty() ||
                presidentePassword.isEmpty() || presidentePoblacion.isEmpty()) {
                showMessage("Por favor, complete todos los campos obligatorios para el club y el presidente.", true);
                return;
            }

            try {
                LocalDate fechaFundacion = LocalDate.parse(fechaFundacionStr);
                LocalDate presidenteFechaNacimiento = LocalDate.parse(presidenteFechaNacimientoStr);

                // Primero crear y guardar la Persona del presidente
                Persona presidente = federacion.nuevaPersona(presidenteDNI, presidenteNombre,
                                                            presidenteApellido1, presidenteApellido2,
                                                            presidenteFechaNacimiento, presidenteUsuario,
                                                            presidentePassword, presidentePoblacion);

                // Luego crear el Club usando la Persona del presidente
                federacion.nuevoClub(nombre, fechaFundacion, presidente);
                showMessage("Club '" + nombre + "' dado de alta exitosamente con presidente " + presidente.getNombre() + ".", false);

                // Limpiar campos
                nombreField.setText("");
                fechaFundacionField.setText("");
                presidenteDNIField.setText("");
                presidenteNombreField.setText("");
                presidenteApellido1Field.setText("");
                presidenteApellido2Field.setText("");
                presidenteFechaNacimientoField.setText("");
                presidenteUsuarioField.setText("");
                presidentePasswordField.setText("");
                presidentePoblacionField.setText("");

            } catch (DateTimeParseException ex) {
                showMessage("Formato de fecha incorrecto. Use: YYYY-MM-DD", true);
            } catch (RuntimeException ex) { // Capturar las RuntimeException lanzadas desde Federacion
                showMessage("Error al dar de alta el club o presidente: " + ex.getMessage(), true);
            } catch (Exception ex) {
                showMessage("Error inesperado: " + ex.getMessage(), true);
            }
        });

        formPanel.add(altaButton);
        updateContentPanel(formPanel, "🏆 Alta de Club");
    }
    
    // Panel para Alta categoría
    private void showAltaCategoriaPanel() {
        JPanel formPanel = createFormPanel();

        JTextField nombreField = new JTextField();
        JTextField ordenField = new JTextField();
        JTextField precioLicenciaField = new JTextField();
        
        formPanel.add(createFormFieldPanel(new JLabel("Nombre de la Categoría:"), nombreField));
        formPanel.add(createFormFieldPanel(new JLabel("Orden:"), ordenField));
        formPanel.add(createFormFieldPanel(new JLabel("Precio Licencia (€):"), precioLicenciaField));
        formPanel.add(Box.createVerticalStrut(30));

        JButton altaButton = createActionButton("📋 Dar de Alta Categoría", e -> {
            String nombre = nombreField.getText().trim();
            String ordenStr = ordenField.getText().trim();
            String precioStr = precioLicenciaField.getText().trim();

            if (nombre.isEmpty() || ordenStr.isEmpty() || precioStr.isEmpty()) {
                showMessage("Por favor, complete todos los campos obligatorios.", true);
                return;
            }

            try {
                int orden = Integer.parseInt(ordenStr);
                double precio = Double.parseDouble(precioStr);
                federacion.nuevaCategoria(nombre, orden, precio);
                showMessage("Categoría '" + nombre + "' dada de alta exitosamente.", false);

                nombreField.setText("");
                ordenField.setText("");
                precioLicenciaField.setText("");
            } catch (NumberFormatException ex) {
                showMessage("Orden y precio deben ser valores numéricos válidos.", true);
            } catch (RuntimeException ex) {
                showMessage("Error al dar de alta la categoría: " + ex.getMessage(), true);
            }
        });

        formPanel.add(altaButton);
        updateContentPanel(formPanel, "📋 Alta de Categoría");
    }

    // Panel para Alta Grupo
    private void showAltaGrupoPanel() {
        JPanel formPanel = createFormPanel();

        JTextField categoriaField = new JTextField();
        categoriaField.setEditable(false);
        JButton seleccionarCategoriaButton = createActionButton("Seleccionar Categoría", e -> {
            List<Categoria> categorias = federacion.obtenerCategorias();
            if (categorias.isEmpty()) {
                showMessage("No hay categorías registradas. Por favor, cree una categoría primero.", true);
                return;
            }
            String[] nombresCategorias = categorias.stream().map(Categoria::getNombre).toArray(String[]::new);
            String selectedCategoria = (String) JOptionPane.showInputDialog(
                frame,
                "Seleccione una Categoría:",
                "Seleccionar Categoría",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombresCategorias,
                nombresCategorias[0]);

            if (selectedCategoria != null) {
                categoriaField.setText(selectedCategoria);
                showMessage("Categoría '" + selectedCategoria + "' seleccionada.", false);
            }
        });

        JTextField nombreField = new JTextField();
        
        formPanel.add(createFormFieldPanel(new JLabel("Categoría Seleccionada:"), categoriaField));
        formPanel.add(seleccionarCategoriaButton);
        formPanel.add(createFormFieldPanel(new JLabel("Nombre del Grupo:"), nombreField));
        formPanel.add(Box.createVerticalStrut(30));

        JButton altaButton = createActionButton("📋 Dar de Alta Grupo", e -> {
            String nombreCategoria = categoriaField.getText().trim();
            String nombreGrupo = nombreField.getText().trim();

            if (nombreCategoria.isEmpty() || nombreGrupo.isEmpty()) {
                showMessage("Por favor, seleccione una categoría y complete el nombre del grupo.", true);
                return;
            }

            try {
                Categoria categoria = federacion.obtenerCategorias().stream()
                    .filter(c -> c.getNombre().equalsIgnoreCase(nombreCategoria))
                    .findFirst()
                    .orElse(null);
                if (categoria == null) {
                    showMessage("Categoría no encontrada: " + nombreCategoria, true);
                    return;
                }

                federacion.nuevoGrupo(categoria, nombreGrupo);
                showMessage("Grupo '" + nombreGrupo + "' dado de alta exitosamente en la categoría '" + nombreCategoria + "'.", false);

                categoriaField.setText("");
                nombreField.setText("");
            } catch (RuntimeException ex) {
                showMessage("Error al dar de alta el grupo: " + ex.getMessage(), true);
            }
        });

        formPanel.add(altaButton);
        updateContentPanel(formPanel, "📋 Alta de Grupo");
    }
    
    // Panel para Alta Empleado
    private void showAltaEmpleadoPanel() {
        JPanel formPanel = createFormPanel();

        JTextField dniField = new JTextField();
        JTextField nombreField = new JTextField();
        JTextField apellido1Field = new JTextField();
        JTextField apellido2Field = new JTextField();
        JTextField fechaNacimientoField = new JTextField();
        JTextField usuarioField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField poblacionField = new JTextField();
        JTextField numEmpleadoField = new JTextField();
        JTextField inicioContratoField = new JTextField();
        JTextField segSocialField = new JTextField();
        
        fechaNacimientoField.setToolTipText("Formato: YYYY-MM-DD (ej: 1990-12-25)");
        inicioContratoField.setToolTipText("Formato: YYYY-MM-DD (ej: 2023-01-01)");

        formPanel.add(createFormFieldPanel(new JLabel("DNI:"), dniField));
        formPanel.add(createFormFieldPanel(new JLabel("Nombre:"), nombreField));
        formPanel.add(createFormFieldPanel(new JLabel("Primer Apellido:"), apellido1Field));
        formPanel.add(createFormFieldPanel(new JLabel("Segundo Apellido:"), apellido2Field));
        formPanel.add(createFormFieldPanel(new JLabel("Fecha de Nacimiento (YYYY-MM-DD):"), fechaNacimientoField));
        formPanel.add(createFormFieldPanel(new JLabel("Usuario:"), usuarioField));
        formPanel.add(createFormFieldPanel(new JLabel("Contraseña:"), passwordField));
        formPanel.add(createFormFieldPanel(new JLabel("Población:"), poblacionField));
        formPanel.add(createFormFieldPanel(new JLabel("Número de Empleado:"), numEmpleadoField));
        formPanel.add(createFormFieldPanel(new JLabel("Inicio Contrato (YYYY-MM-DD):"), inicioContratoField));
        formPanel.add(createFormFieldPanel(new JLabel("Seguridad Social:"), segSocialField));
        formPanel.add(Box.createVerticalStrut(30));

        JButton altaButton = createActionButton("👤 Dar de Alta Empleado", e -> {
            String dni = dniField.getText().trim();
            String nombre = nombreField.getText().trim();
            String apellido1 = apellido1Field.getText().trim();
            String apellido2 = apellido2Field.getText().trim();
            String fechaNacimientoStr = fechaNacimientoField.getText().trim();
            String usuario = usuarioField.getText().trim();
            String password = passwordField.getText().trim();
            String poblacion = poblacionField.getText().trim();
            String numEmpleadoStr = numEmpleadoField.getText().trim();
            String inicioContratoStr = inicioContratoField.getText().trim();
            String segSocial = segSocialField.getText().trim();

            if (dni.isEmpty() || nombre.isEmpty() || apellido1.isEmpty() || fechaNacimientoStr.isEmpty() ||
                usuario.isEmpty() || password.isEmpty() || poblacion.isEmpty() ||
                numEmpleadoStr.isEmpty() || inicioContratoStr.isEmpty() || segSocial.isEmpty()) {
                showMessage("Por favor, complete todos los campos obligatorios.", true);
                return;
            }

            try {
                LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
                LocalDate inicioContrato = LocalDate.parse(inicioContratoStr);
                int numEmpleado = Integer.parseInt(numEmpleadoStr);
                federacion.nuevoEmpleado(dni, nombre, apellido1, apellido2,
                    fechaNacimiento, usuario, password, poblacion, numEmpleado, inicioContrato, segSocial);
                showMessage("Empleado '" + nombre + " " + apellido1 + "' dado de alta exitosamente.", false);

                dniField.setText("");
                nombreField.setText("");
                apellido1Field.setText("");
                apellido2Field.setText("");
                fechaNacimientoField.setText("");
                usuarioField.setText("");
                passwordField.setText("");
                poblacionField.setText("");
                numEmpleadoField.setText("");
                inicioContratoField.setText("");
                segSocialField.setText("");
            } catch (DateTimeParseException ex) {
                showMessage("Formato de fecha incorrecto. Use: YYYY-MM-DD", true);
            } catch (NumberFormatException ex) {
                showMessage("Número de empleado debe ser un valor numérico válido.", true);
            } catch (RuntimeException ex) {
                showMessage("Error al dar de alta el empleado: " + ex.getMessage(), true);
            }
        });

        formPanel.add(altaButton);
        updateContentPanel(formPanel, "👤 Alta de Empleado");
    }
    
    // Panel para Alta Instalación
    private void showAltaInstalacionPanel() {
        JPanel formPanel = createFormPanel();

        JTextField nombreField = new JTextField();
        JTextField direccionField = new JTextField();
        JTextField superficieField = new JTextField();

        formPanel.add(createFormFieldPanel(new JLabel("Nombre de la Instalación:"), nombreField));
        formPanel.add(createFormFieldPanel(new JLabel("Dirección:"), direccionField));
        formPanel.add(createFormFieldPanel(new JLabel("Superficie (CESPED_NATURAL, CESPED_ARTIFICIAL, TIERRA):"), superficieField));
        formPanel.add(Box.createVerticalStrut(30));

        JButton altaButton = createActionButton("🏟️ Dar de Alta Instalación", e -> {
            String nombre = nombreField.getText().trim();
            String direccion = direccionField.getText().trim();
            String superficie = superficieField.getText().trim();

            if (nombre.isEmpty() || direccion.isEmpty() || superficie.isEmpty()) {
                showMessage("Por favor, complete todos los campos obligatorios.", true);
                return;
            }

            try {
                Instalacion nuevaInstalacion = federacion.nuevaInstalacion(nombre, direccion, superficie);
                showMessage("Instalación '" + nombre + "' dada de alta exitosamente.", false);
                nombreField.setText("");
                direccionField.setText("");
                superficieField.setText("");
            } catch (RuntimeException ex) {
                showMessage("Error al dar de alta la instalación: " + ex.getMessage(), true);
            }
        });

        formPanel.add(altaButton);
        updateContentPanel(formPanel, "🏟️ Alta de Instalación");
    }

    // Método para listar equipos
    private void showListarEquiposPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);

        try {
            List<Equipo> equipos = federacion.obtenerEquipos();
            if (equipos.isEmpty()) {
                JPanel emptyPanel = createEmptyStatePanel("⚽", "No hay equipos registrados",
                    "Comience agregando un nuevo equipo desde el menú superior.");
                mainPanel.add(emptyPanel, BorderLayout.CENTER);
            } else {
                JPanel equiposPanel = new JPanel();
                equiposPanel.setLayout(new BoxLayout(equiposPanel, BoxLayout.Y_AXIS));
                equiposPanel.setOpaque(false);

                for (Equipo equipo : equipos) {
                    JPanel equipoCard = createEquipoCard(equipo);
                    equiposPanel.add(equipoCard);
                    equiposPanel.add(Box.createVerticalStrut(15));
                }

                JScrollPane scrollPane = new JScrollPane(equiposPanel);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                mainPanel.add(scrollPane, BorderLayout.CENTER);
            }
        } catch (Exception ex) {
            showMessage("Error al cargar la lista de equipos: " + ex.getMessage(), true);
            JPanel errorPanel = createErrorPanel("Error al cargar equipos", ex.getMessage());
            mainPanel.add(errorPanel, BorderLayout.CENTER);
        }

        updateContentPanel(mainPanel, "📋 Lista de Equipos");
    }

    // Método para listar categorías
    private void showListarCategoriasPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);

        try {
            List<Categoria> categorias = federacion.obtenerCategorias();
            if (categorias.isEmpty()) {
                JPanel emptyPanel = createEmptyStatePanel("📋", "No hay categorías registradas",
                    "Comience agregando una nueva categoría desde el menú superior.");
                mainPanel.add(emptyPanel, BorderLayout.CENTER);
            } else {
                JPanel categoriasPanel = new JPanel();
                categoriasPanel.setLayout(new BoxLayout(categoriasPanel, BoxLayout.Y_AXIS));
                categoriasPanel.setOpaque(false);

                for (Categoria categoria : categorias) {
                    JPanel categoriaCard = createCategoriaCard(categoria);
                    categoriasPanel.add(categoriaCard);
                    categoriasPanel.add(Box.createVerticalStrut(15));
                }

                JScrollPane scrollPane = new JScrollPane(categoriasPanel);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                mainPanel.add(scrollPane, BorderLayout.CENTER);
            }
        } catch (Exception ex) {
            showMessage("Error al cargar la lista de categorías: " + ex.getMessage(), true);
            JPanel errorPanel = createErrorPanel("Error al cargar categorías", ex.getMessage());
            mainPanel.add(errorPanel, BorderLayout.CENTER);
        }

        updateContentPanel(mainPanel, "📋 Lista de Categorías");
    }

    // Método para listar personas
    private void showListarPersonasPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);

        try {
            List<Persona> personas = federacion.obtenerPersonas();
            if (personas.isEmpty()) {
                JPanel emptyPanel = createEmptyStatePanel("👤", "No hay personas registradas",
                    "Comience agregando una nueva persona desde el menú superior.");
                mainPanel.add(emptyPanel, BorderLayout.CENTER);
            } else {
                JPanel personasPanel = new JPanel();
                personasPanel.setLayout(new BoxLayout(personasPanel, BoxLayout.Y_AXIS));
                personasPanel.setOpaque(false);

                for (Persona persona : personas) {
                    JPanel personaCard = createPersonaCard(persona);
                    personasPanel.add(personaCard);
                    personasPanel.add(Box.createVerticalStrut(15));
                }

                JScrollPane scrollPane = new JScrollPane(personasPanel);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                mainPanel.add(scrollPane, BorderLayout.CENTER);
            }
        } catch (Exception ex) {
            showMessage("Error al cargar la lista de personas: " + ex.getMessage(), true);
            JPanel errorPanel = createErrorPanel("Error al cargar personas", ex.getMessage());
            mainPanel.add(errorPanel, BorderLayout.CENTER);
        }

        updateContentPanel(mainPanel, "📋 Lista de Personas");
    }
    
    // Método para listar empleados
    private void showListarEmpleadosPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);

        try {
            List<Empleado> empleados = federacion.obtenerEmpleados();
            if (empleados.isEmpty()) {
                JPanel emptyPanel = createEmptyStatePanel("👤", "No hay empleados registrados",
                    "Comience agregando un nuevo empleado desde el menú superior.");
                mainPanel.add(emptyPanel, BorderLayout.CENTER);
            } else {
                JPanel empleadosPanel = new JPanel();
                empleadosPanel.setLayout(new BoxLayout(empleadosPanel, BoxLayout.Y_AXIS));
                empleadosPanel.setOpaque(false);

                for (Empleado empleado : empleados) {
                    JPanel empleadoCard = createEmpleadoCard(empleado);
                    empleadosPanel.add(empleadoCard);
                    empleadosPanel.add(Box.createVerticalStrut(15));
                }

                JScrollPane scrollPane = new JScrollPane(empleadosPanel);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                mainPanel.add(scrollPane, BorderLayout.CENTER);
            }
        } catch (Exception ex) {
            showMessage("Error al cargar la lista de empleados: " + ex.getMessage(), true);
            JPanel errorPanel = createErrorPanel("Error al cargar empleados", ex.getMessage());
            mainPanel.add(errorPanel, BorderLayout.CENTER);
        }

        updateContentPanel(mainPanel, "📋 Lista de Empleados");
    }

    // Método para listar grupos por categoría
    private void showListarGruposPorCategoriaPanel() {
        JPanel formPanel = createFormPanel();

        JTextField categoriaField = new JTextField();
        categoriaField.setEditable(false);
        JButton seleccionarCategoriaButton = createActionButton("Seleccionar Categoría", e -> {
            List<Categoria> categorias = federacion.obtenerCategorias();
            if (categorias.isEmpty()) {
                showMessage("No hay categorías registradas. Por favor, cree una categoría primero.", true);
                return;
            }
            String[] nombresCategorias = categorias.stream().map(Categoria::getNombre).toArray(String[]::new);
            String selectedCategoria = (String) JOptionPane.showInputDialog(
                frame,
                "Seleccione una Categoría:",
                "Seleccionar Categoría",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombresCategorias,
                nombresCategorias[0]);

            if (selectedCategoria != null) {
                categoriaField.setText(selectedCategoria);
                showMessage("Categoría '" + selectedCategoria + "' seleccionada.", false);
            }
        });

        JPanel gruposPanel = new JPanel();
        gruposPanel.setLayout(new BoxLayout(gruposPanel, BoxLayout.Y_AXIS));
        gruposPanel.setOpaque(false);

        // Crear el JScrollPane y configurarlo correctamente
        JScrollPane gruposScrollPane = new JScrollPane(gruposPanel);
        gruposScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gruposScrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        gruposScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gruposScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gruposScrollPane.setPreferredSize(new Dimension(600, 300)); // Tamaño inicial para asegurar visibilidad

        formPanel.add(createFormFieldPanel(new JLabel("Categoría Seleccionada:"), categoriaField));
        formPanel.add(seleccionarCategoriaButton);
        formPanel.add(Box.createVerticalStrut(20));

        JButton listarButton = createActionButton("📋 Listar Grupos", e -> {
            String nombreCategoria = categoriaField.getText().trim();
            if (nombreCategoria.isEmpty()) {
                showMessage("Por favor, seleccione una categoría.", true);
                return;
            }

            try {
                Categoria categoria = federacion.obtenerCategorias().stream()
                    .filter(c -> c.getNombre().equalsIgnoreCase(nombreCategoria))
                    .findFirst()
                    .orElse(null);
                if (categoria == null) {
                    showMessage("Categoría no encontrada: " + nombreCategoria, true);
                    return;
                }

                List<Grupo> grupos = federacion.obtenerGrupos(categoria);
                gruposPanel.removeAll();
                if (grupos.isEmpty()) {
                    JPanel emptyPanel = createEmptyStatePanel("📋", "No hay grupos registrados",
                        "Comience agregando un nuevo grupo desde el menú superior.");
                    gruposPanel.add(emptyPanel);
                } else {
                    for (Grupo grupo : grupos) {
                        JPanel grupoCard = createGrupoCard(grupo);
                        gruposPanel.add(grupoCard);
                        gruposPanel.add(Box.createVerticalStrut(15));
                    }
                }
                // Forzar la actualización del JScrollPane
                gruposPanel.revalidate();
                gruposPanel.repaint();
                gruposScrollPane.revalidate();
                gruposScrollPane.repaint();
                showMessage("Grupos listados para la categoría '" + nombreCategoria + "'.", false);
            } catch (RuntimeException ex) {
                showMessage("Error al listar grupos: " + ex.getMessage(), true);
            }
        });

        formPanel.add(listarButton);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(gruposScrollPane); // Añadir el JScrollPane al formPanel
        updateContentPanel(formPanel, "📋 Lista de Grupos por Categoría");
    }

    // Método para listar licencias
    private void showListarLicenciasPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);

        try {
            List<Licencia> licencias = federacion.obtenerLicencias();
            if (licencias.isEmpty()) {
                JPanel emptyPanel = createEmptyStatePanel("📜", "No hay licencias registradas",
                    "Comience agregando una nueva licencia desde el menú superior.");
                mainPanel.add(emptyPanel, BorderLayout.CENTER);
            } else {
                JPanel licenciasPanel = new JPanel();
                licenciasPanel.setLayout(new BoxLayout(licenciasPanel, BoxLayout.Y_AXIS));
                licenciasPanel.setOpaque(false);

                for (Licencia licencia : licencias) {
                    JPanel licenciaCard = createLicenciaCard(licencia);
                    licenciasPanel.add(licenciaCard);
                    licenciasPanel.add(Box.createVerticalStrut(15));
                }

                JScrollPane scrollPane = new JScrollPane(licenciasPanel);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                mainPanel.add(scrollPane, BorderLayout.CENTER);
            }
        } catch (Exception ex) {
            showMessage("Error al cargar la lista de licencias: " + ex.getMessage(), true);
            JPanel errorPanel = createErrorPanel("Error al cargar licencias", ex.getMessage());
            mainPanel.add(errorPanel, BorderLayout.CENTER);
        }

        updateContentPanel(mainPanel, "📜 Lista de Licencias");
    }

    // Método para buscar persona por DNI
    private void showBuscarPersonaDniPanel() {
        JPanel formPanel = createFormPanel();

        JTextField dniField = new JTextField();
        JTextArea resultArea = new JTextArea(10, 40); // Cambiado a 10 filas y 40 columnas para mayor tamaño
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Inter", Font.PLAIN, 12)); // Ajustado a 12 para mantener legibilidad
        resultArea.setBackground(new Color(252, 253, 255));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        formPanel.add(createFormFieldPanel(new JLabel("DNI:"), dniField));
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createActionButton("🔍 Buscar Persona", e -> {
            String dni = dniField.getText().trim();
            if (dni.isEmpty()) {
                showMessage("Por favor, ingrese un DNI.", true);
                return;
            }

            try {
                Persona persona = federacion.buscaPersona(dni);
                if (persona == null) {
                    resultArea.setText("No se encontró ninguna persona con DNI: " + dni);
                    showMessage("No se encontró ninguna persona con DNI: " + dni, true);
                } else {
                    resultArea.setText("DNI: " + persona.getDni() + "\n" +
                            "Nombre: " + persona.getNombre() + "\n" +
                            "Apellido1: " + persona.getApellido1() + "\n" +
                            "Apellido2: " + (persona.getApellido2() != null ? persona.getApellido2() : "") + "\n" +
                            "Fecha de Nacimiento: " + persona.getFechaNacimiento() + "\n" +
                            "Población: " + persona.getPoblacion());
                    showMessage("Persona encontrada: " + persona.getNombre() + " " + persona.getApellido1(), false);
                }
            } catch (RuntimeException ex) {
                showMessage("Error al buscar persona: " + ex.getMessage(), true);
                resultArea.setText("Error: " + ex.getMessage());
            }
        }));

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(new JScrollPane(resultArea));
        updateContentPanel(formPanel, "🔍 Buscar Persona por DNI");
    }

    // Método para buscar personas por nombre
    private void showBuscarPersonasPorNombrePanel() {
        JPanel formPanel = createFormPanel();

        JTextField nombreField = new JTextField();
        JTextField apellido1Field = new JTextField();
        JTextField apellido2Field = new JTextField();
        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Inter", Font.PLAIN, 14));
        resultArea.setBackground(new Color(252, 253, 255));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        formPanel.add(createFormFieldPanel(new JLabel("Nombre:"), nombreField));
        formPanel.add(createFormFieldPanel(new JLabel("Primer Apellido (opcional):"), apellido1Field));
        formPanel.add(createFormFieldPanel(new JLabel("Segundo Apellido (opcional):"), apellido2Field));
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createActionButton("🔍 Buscar Personas", e -> {
            String nombre = nombreField.getText().trim();
            String apellido1 = apellido1Field.getText().trim();
            String apellido2 = apellido2Field.getText().trim();

            try {
                List<Persona> personas = federacion.buscaPersonas(
                    nombre.isEmpty() ? null : nombre,
                    apellido1.isEmpty() ? null : apellido1,
                    apellido2.isEmpty() ? null : apellido2
                );
                if (personas.isEmpty()) {
                    resultArea.setText("No se encontraron personas con los criterios especificados.");
                    showMessage("No se encontraron personas con los criterios especificados.", true);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Persona persona : personas) {
                        sb.append("DNI: ").append(persona.getDni()).append("\n")
                          .append("Nombre: ").append(persona.getNombre()).append("\n")
                          .append("Apellido1: ").append(persona.getApellido1()).append("\n")
                          .append("Apellido2: ").append(persona.getApellido2() != null ? persona.getApellido2() : "").append("\n")
                          .append("Fecha de Nacimiento: ").append(persona.getFechaNacimiento()).append("\n")
                          .append("Población: ").append(persona.getPoblacion()).append("\n")
                          .append("----------------------------------------\n");
                    }
                    resultArea.setText(sb.toString());
                    showMessage("Personas encontradas: " + personas.size(), false);
                }
            } catch (RuntimeException ex) {
                showMessage("Error al buscar personas: " + ex.getMessage(), true);
                resultArea.setText("Error: " + ex.getMessage());
            }
        }));

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(new JScrollPane(resultArea));
        updateContentPanel(formPanel, "🔍 Buscar Personas por Nombre");
    }

    // Método auxiliar para crear tarjeta de equipo
    private JPanel createEquipoCard(Equipo equipo) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 16, 16);
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(252, 253, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 16, 16);
                g2d.setColor(SECONDARY_COLOR);
                g2d.setStroke(new BasicStroke(6));
                g2d.drawLine(6, 16, 6, getHeight() - 16);
            }
        };

        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nombreLabel = new JLabel("⚽ " + equipo.getLetra());
        nombreLabel.setFont(new Font("Inter", Font.BOLD, 20));
        nombreLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(nombreLabel, gbc);

        JLabel clubLabel = new JLabel("🏆 Club: " + equipo.getClub().getNombre());
        clubLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        clubLabel.setForeground(TEXT_SECONDARY);
        gbc.gridx = 1; gbc.gridy = 1;
        card.add(clubLabel, gbc);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setCursor(Cursor.getDefaultCursor());
                card.repaint();
            }
        });

        return card;
    }

    // Método auxiliar para crear tarjeta de categoría
    private JPanel createCategoriaCard(Categoria categoria) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 16, 16);
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(252, 253, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 16, 16);
                g2d.setColor(SECONDARY_COLOR);
                g2d.setStroke(new BasicStroke(6));
                g2d.drawLine(6, 16, 6, getHeight() - 16);
            }
        };

        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

    JLabel nombreLabel = new JLabel("📋 " + categoria.getNombre());
    nombreLabel.setFont(new Font("Inter", Font.BOLD, 20));
    nombreLabel.setForeground(PRIMARY_COLOR);
    gbc.gridx = 0; gbc.gridy = 0;
    gbc.gridwidth = 2;
    card.add(nombreLabel, gbc);

    JLabel precioLabel = new JLabel("💶 Precio Licencia: €" + categoria.getPrecioLicencia());
    precioLabel.setFont(new Font("Inter", Font.PLAIN, 16));
    precioLabel.setForeground(TEXT_SECONDARY);
    gbc.gridx = 1; gbc.gridy = 1;
    card.add(precioLabel, gbc);

    card.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            card.setCursor(Cursor.getDefaultCursor());
            card.repaint();
        }
    });

    return card;
}

    // Método auxiliar para crear tarjeta de persona
    private JPanel createPersonaCard(Persona persona) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 16, 16);
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(252, 253, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 16, 16);
                g2d.setColor(SECONDARY_COLOR);
                g2d.setStroke(new BasicStroke(6));
                g2d.drawLine(6, 16, 6, getHeight() - 16);
            }
        };

        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nombreLabel = new JLabel("👤 " + persona.getNombre() + " " + persona.getApellido1());
        nombreLabel.setFont(new Font("Inter", Font.BOLD, 20));
        nombreLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(nombreLabel, gbc);

        JLabel dniLabel = new JLabel("DNI: " + persona.getDni());
        dniLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        dniLabel.setForeground(TEXT_SECONDARY);
        gbc.gridx = 1; gbc.gridy = 1;
        card.add(dniLabel, gbc);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setCursor(Cursor.getDefaultCursor());
                card.repaint();
            }
        });

        return card;
    }

    // Método auxiliar para crear tarjeta de empleado
    private JPanel createEmpleadoCard(Empleado empleado) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 16, 16);
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(252, 253, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 16, 16);
                g2d.setColor(SECONDARY_COLOR);
                g2d.setStroke(new BasicStroke(6));
                g2d.drawLine(6, 16, 6, getHeight() - 16);
            }
        };

        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nombreLabel = new JLabel("👤 " + empleado.getNombre() + " " + empleado.getApellido1());
        nombreLabel.setFont(new Font("Inter", Font.BOLD, 20));
        nombreLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(nombreLabel, gbc);

        JLabel numEmpleadoLabel = new JLabel("Nº Empleado: " + empleado.getNumEmpleado());
        numEmpleadoLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        numEmpleadoLabel.setForeground(TEXT_SECONDARY);
        gbc.gridx = 1; gbc.gridy = 1;
        card.add(numEmpleadoLabel, gbc);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setCursor(Cursor.getDefaultCursor());
                card.repaint();
            }
        });

        return card;
    }
    
    // Método auxiliar para crear tarjeta de grupo
    private JPanel createGrupoCard(Grupo grupo) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 16, 16);
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(252, 253, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 16, 16);
                g2d.setColor(SECONDARY_COLOR);
                g2d.setStroke(new BasicStroke(6));
                g2d.drawLine(6, 16, 6, getHeight() - 16);
            }
        };

        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nombreLabel = new JLabel("📋 " + grupo.getNombre());
        nombreLabel.setFont(new Font("Inter", Font.BOLD, 20));
        nombreLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(nombreLabel, gbc);

        JLabel categoriaLabel = new JLabel("📋 Categoría: " + grupo.getCategoria().getNombre());
        categoriaLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        categoriaLabel.setForeground(TEXT_SECONDARY);
        gbc.gridx = 1; gbc.gridy = 1;
        card.add(categoriaLabel, gbc);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setCursor(Cursor.getDefaultCursor());
                card.repaint();
            }
        });

        return card;
    }

    // Método auxiliar para crear tarjeta de licencia
    private JPanel createLicenciaCard(Licencia licencia) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 16, 16);
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(252, 253, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 16, 16);
                g2d.setColor(SECONDARY_COLOR);
                g2d.setStroke(new BasicStroke(6));
                g2d.drawLine(6, 16, 6, getHeight() - 16);
            }
        };

        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel numeroLabel = new JLabel("📜 " + licencia.getNumeroLicencia());
        numeroLabel.setFont(new Font("Inter", Font.BOLD, 20));
        numeroLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(numeroLabel, gbc);

        Persona jugador = licencia.getJugador();
        String nombrePersona = (jugador != null) ? jugador.getNombre() + " " + jugador.getApellido1() : "Sin jugador asignado";
        JLabel personaLabel = new JLabel("👤 " + nombrePersona);
        personaLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        personaLabel.setForeground(TEXT_SECONDARY);
        gbc.gridx = 1; gbc.gridy = 1;
        card.add(personaLabel, gbc);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setCursor(Cursor.getDefaultCursor());
                card.repaint();
            }
        });

        return card;
    }

    // Método para alta licencia sin equipo
    private void showAltaLicenciaSinEquipoPanel() {
        JPanel formPanel = createFormPanel();

        JTextField dniField = new JTextField();
        formPanel.add(createFormFieldPanel(new JLabel("DNI de la Persona:"), dniField));
        formPanel.add(Box.createVerticalStrut(30));

        JButton altaButton = createActionButton("📜 Dar de Alta Licencia", e -> {
            String dni = dniField.getText().trim();
            if (dni.isEmpty()) {
                showMessage("Por favor, complete el DNI.", true);
                return;
            }
            try {
                Persona persona = federacion.buscaPersona(dni);
                if (persona == null) {
                    showMessage("Persona no encontrada con DNI: " + dni, true);
                    return;
                }
                Licencia nuevaLicencia = federacion.nuevaLicencia(persona);
                showMessage("Licencia creada exitosamente para " + persona.getNombre() + " (sin equipo).", false);
                dniField.setText("");
            } catch (RuntimeException ex) {
                showMessage("Error al crear licencia: " + ex.getMessage(), true);
            }
        });

        formPanel.add(altaButton);
        updateContentPanel(formPanel, "📜 Alta de Licencia (Sin Equipo)");
    }

    // Método para alta licencia con equipo
    private void showAltaLicenciaConEquipoPanel() {
        JPanel formPanel = createFormPanel();

        JTextField dniField = new JTextField();
        JTextField equipoField = new JTextField();
        equipoField.setEditable(false);

        JButton seleccionarEquipoButton = createActionButton("Seleccionar Equipo", e -> {
            List<Equipo> equipos = federacion.obtenerEquipos();
            if (equipos.isEmpty()) {
                showMessage("No hay equipos registrados.", true);
                return;
            }
            String[] nombresEquipos = equipos.stream().map(Equipo::toString).toArray(String[]::new);
            String selectedEquipo = (String) JOptionPane.showInputDialog(frame, "Seleccione un Equipo:", "Seleccionar Equipo", JOptionPane.QUESTION_MESSAGE, null, nombresEquipos, nombresEquipos[0]);
            if (selectedEquipo != null) {
                equipoField.setText(selectedEquipo);
                showMessage("Equipo '" + selectedEquipo + "' seleccionado.", false);
            }
        });

        formPanel.add(createFormFieldPanel(new JLabel("DNI de la Persona:"), dniField));
        formPanel.add(createFormFieldPanel(new JLabel("Equipo Seleccionado:"), equipoField));
        formPanel.add(seleccionarEquipoButton);
        formPanel.add(Box.createVerticalStrut(30));

        JButton altaButton = createActionButton("📜 Dar de Alta Licencia", e -> {
            String dni = dniField.getText().trim();
            String equipoStr = equipoField.getText().trim();
            if (dni.isEmpty() || equipoStr.isEmpty()) {
                showMessage("Complete todos los campos.", true);
                return;
            }
            try {
                Persona persona = federacion.buscaPersona(dni);
                if (persona == null) {
                    showMessage("Persona no encontrada.", true);
                    return;
                }
                Equipo equipo = federacion.obtenerEquipos().stream()
                        .filter(eq -> eq.toString().equals(equipoStr))
                        .findFirst().orElse(null);
                if (equipo == null) {
                    showMessage("Equipo no encontrado.", true);
                    return;
                }
                Licencia nuevaLicencia = federacion.nuevaLicencia(persona, equipo);
                showMessage("Licencia creada para " + persona.getNombre() + " en " + equipoStr + ".", false);
                dniField.setText("");
                equipoField.setText("");
            } catch (RuntimeException ex) {
                showMessage("Error: " + ex.getMessage(), true);
            }
        });

        formPanel.add(altaButton);
        updateContentPanel(formPanel, "📜 Alta de Licencia (Con Equipo)");
    }

    // Método para añadir licencia a equipo
    private void showAddLicenciaAEquipoPanel() {
        JPanel formPanel = createFormPanel();

        JTextField numeroLicenciaField = new JTextField();
        JTextField equipoField = new JTextField();
        equipoField.setEditable(false);

        JButton seleccionarEquipoButton = createActionButton("Seleccionar Equipo", e -> {
            List<Equipo> equipos = federacion.obtenerEquipos();
            if (equipos.isEmpty()) {
                showMessage("No hay equipos registrados.", true);
                return;
            }
            String[] nombresEquipos = equipos.stream().map(Equipo::toString).toArray(String[]::new);
            String selectedEquipo = (String) JOptionPane.showInputDialog(frame, "Seleccione un Equipo:", "Seleccionar Equipo", JOptionPane.QUESTION_MESSAGE, null, nombresEquipos, nombresEquipos[0]);
            if (selectedEquipo != null) {
                equipoField.setText(selectedEquipo);
                showMessage("Equipo '" + selectedEquipo + "' seleccionado.", false);
            }
        });

        formPanel.add(createFormFieldPanel(new JLabel("Número de Licencia:"), numeroLicenciaField));
        formPanel.add(createFormFieldPanel(new JLabel("Equipo Seleccionado:"), equipoField));
        formPanel.add(seleccionarEquipoButton);
        formPanel.add(Box.createVerticalStrut(30));

        JButton addButton = createActionButton("📜 Añadir Licencia a Equipo", e -> {
            String numeroLicencia = numeroLicenciaField.getText().trim();
            String equipoStr = equipoField.getText().trim();
            if (numeroLicencia.isEmpty() || equipoStr.isEmpty()) {
                showMessage("Complete todos los campos.", true);
                return;
            }
            try {
                Licencia licencia = Licencia.buscarPorNumero(numeroLicencia);
                if (licencia == null) {
                    showMessage("Licencia no encontrada.", true);
                    return;
                }
                Equipo equipo = federacion.obtenerEquipos().stream()
                        .filter(eq -> eq.toString().equals(equipoStr))
                        .findFirst().orElse(null);
                if (equipo == null) {
                    showMessage("Equipo no encontrado.", true);
                    return;
                }
                federacion.addLicencia(licencia, equipo);
                showMessage("Licencia '" + numeroLicencia + "' añadida a " + equipoStr + ".", false);
                numeroLicenciaField.setText("");
                equipoField.setText("");
            } catch (RuntimeException ex) {
                showMessage("Error: " + ex.getMessage(), true);
            } catch (SQLException ex) {
                Logger.getLogger(MainApp2.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        formPanel.add(addButton);
        updateContentPanel(formPanel, "📜 Añadir Licencia a Equipo");
    }

    // Método para calcular precio de licencia
    private void showCalcularPrecioLicenciaPanel() {
        JPanel formPanel = createFormPanel();

        JTextField equipoField = new JTextField();
        equipoField.setEditable(false);
        JButton seleccionarEquipoButton = createActionButton("Seleccionar Equipo", e -> {
            List<Equipo> equipos = federacion.obtenerEquipos();
            if (equipos.isEmpty()) {
                showMessage("No hay equipos registrados. Por favor, cree un equipo primero.", true);
                return;
            }
            String[] nombresEquipos = equipos.stream()
                .map(eq -> eq.getLetra() + " - " + eq.getClub().getNombre())
                .toArray(String[]::new);
            String selectedEquipo = (String) JOptionPane.showInputDialog(
                frame,
                "Seleccione un Equipo:",
                "Seleccionar Equipo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombresEquipos,
                nombresEquipos[0]);

            if (selectedEquipo != null) {
                equipoField.setText(selectedEquipo);
                showMessage("Equipo '" + selectedEquipo + "' seleccionado.", false);
            }
        });

        JTextArea resultArea = new JTextArea(3, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Inter", Font.PLAIN, 14));
        resultArea.setBackground(new Color(252, 253, 255));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        formPanel.add(createFormFieldPanel(new JLabel("Equipo Seleccionado:"), equipoField));
        formPanel.add(seleccionarEquipoButton);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createActionButton("💶 Calcular Precio", e -> {
            String equipoStr = equipoField.getText().trim();
            if (equipoStr.isEmpty()) {
                showMessage("Por favor, seleccione un equipo.", true);
                return;
            }

            try {
                String[] parts = equipoStr.split(" - ");
                String letra = parts[0];
                Equipo equipo = federacion.obtenerEquipos().stream()
                    .filter(eq -> eq.getLetra().equals(letra))
                    .findFirst()
                    .orElse(null);
                if (equipo == null) {
                    showMessage("Equipo no encontrado: " + equipoStr, true);
                    return;
                }

                double precio = federacion.calcularPrecioLicencia(equipo);
                resultArea.setText("Precio de la licencia: €" + precio);
                showMessage("Precio calculado para el equipo '" + equipoStr + "': €" + precio, false);
            } catch (RuntimeException ex) {
                showMessage("Error al calcular precio: " + ex.getMessage(), true);
                resultArea.setText("Error: " + ex.getMessage());
            }
        }));

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(new JScrollPane(resultArea));
        updateContentPanel(formPanel, "💶 Calcular Precio de Licencia");
    }

    // Método para buscar instalaciones
    private void showBuscarInstalacionesPanel() {
        JPanel formPanel = createFormPanel();

        JTextField nombreField = new JTextField();
        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Inter", Font.PLAIN, 14));
        resultArea.setBackground(new Color(252, 253, 255));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        formPanel.add(createFormFieldPanel(new JLabel("Nombre de la Instalación:"), nombreField));
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createActionButton("🔍 Buscar Instalaciones", e -> {
            String nombre = nombreField.getText().trim();
            if (nombre.isEmpty()) {
                showMessage("Por favor, ingrese un nombre para buscar.", true);
                return;
            }

            try {
                List<Instalacion> instalaciones = federacion.buscarInstalaciones(nombre);
                if (instalaciones.isEmpty()) {
                    resultArea.setText("No se encontraron instalaciones con el nombre: " + nombre);
                    showMessage("No se encontraron instalaciones con el nombre: " + nombre, true);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Instalacion instalacion : instalaciones) {
                        sb.append("Nombre: ").append(instalacion.getNombre()).append("\n")
                          .append("Dirección: ").append(instalacion.getDireccion()).append("\n")
                          .append("Superficie: ").append(instalacion.getSuperficie()).append("\n")
                          .append("----------------------------------------\n");
                    }
                    resultArea.setText(sb.toString());
                    showMessage("Instalaciones encontradas: " + instalaciones.size(), false);
                }
            } catch (RuntimeException ex) {
                showMessage("Error al buscar instalaciones: " + ex.getMessage(), true);
                resultArea.setText("Error: " + ex.getMessage());
            }
        }));

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(new JScrollPane(resultArea));
        updateContentPanel(formPanel, "🔍 Buscar Instalaciones");
        }
    
    // Método para limpiar base de datos
    private void limpiarBaseDeDatos() {
        int confirm = JOptionPane.showConfirmDialog(
            frame,
            "⚠️ ¿Está seguro que desea limpiar TODA la base de datos?\nEsta acción no se puede deshacer.",
            "Confirmar Limpieza de Base de Datos",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                federacion.limpiarTablas();
                showMessage("Base de datos limpiada exitosamente. Reinicie la aplicación.", false);

                // Opcional: Cerrar la aplicación o reiniciar la interfaz
                JOptionPane.showMessageDialog(
                    frame,
                    "La base de datos ha sido limpiada.\nPor favor, reinicie la aplicación.",
                    "Operación Completada",
                    JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {
                showMessage("Error al limpiar la base de datos: " + ex.getMessage(), true);
                JOptionPane.showMessageDialog(
                    frame,
                    "Error al limpiar la base de datos:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    private class AnimatedWelcomePanel extends JPanel {
    private float alpha = 0.0f; // Transparencia para la animación

    public AnimatedWelcomePanel() {
        setOpaque(false); // Importante para que se vea el fondo pintado
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo con gradiente radial
        Point2D center = new Point2D.Float(getWidth() / 2f, getHeight() / 2f);
        float radius = Math.max(getWidth(), getHeight()) / 2f;
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {Color.WHITE, new Color(248, 250, 252)};
        RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);

        // Sombra sutil alrededor
        g2d.setColor(new Color(0, 0, 0, 8));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 24, 24);

        // Borde elegante
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
        
        // Aplicar animación de fade-in
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        // No llamar a super.paintChildren(g) aquí, ya que el layout se encarga de los hijos.
        // Los componentes hijos se pintan automáticamente después de paintComponent.
    }

    // Método para establecer la transparencia
    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }
}
}