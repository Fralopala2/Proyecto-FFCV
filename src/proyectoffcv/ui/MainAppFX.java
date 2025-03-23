package proyectoffcv.ui;

import entidades.*;
import proyectoffcv.logica.Federacion;
import proyectoffcv.logica.IFederacion;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainAppFX extends Application {

    private IFederacion federacion;
    private BorderPane mainLayout;
    private StackPane contentArea;

    public MainAppFX() {
        federacion = Federacion.getInstance();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Proyecto FFCV - Gestión Federativa");
        primaryStage.getIcons().add(new Image("file:src/resources/logo.png"));

        // Layout principal
        mainLayout = new BorderPane();
        mainLayout.setPrefSize(1000, 750);

        // Fondo con gradiente
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, null,
                new Stop(0, Color.web("#F5F7FA")),
                new Stop(1, Color.web("#C8E6C9")));
        mainLayout.setBackground(new Background(new BackgroundFill(gradient, null, null)));

        // Header
        mainLayout.setTop(createHeader());

        // Área de contenido
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(15));
        contentArea.getChildren().add(createWelcomePane());
        mainLayout.setCenter(contentArea);

        // Menú
        mainLayout.setLeft(createMenu());

        // Escena
        Scene scene = new Scene(mainLayout);
        scene.getStylesheets().add("file:src/resources/styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 20, 10, 20));
        header.setBackground(new Background(new BackgroundFill(Color.web("#A60707"), null, null)));

        ImageView logo = new ImageView(new Image("file:src/resources/logo.png"));
        logo.setFitHeight(50);
        logo.setFitWidth(50);

        Label title = new Label("Sistema de Gestión Federativa FFCV");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setTextFill(Color.WHITE);

        header.getChildren().addAll(logo, title);
        return header;
    }

    private VBox createMenu() {
    VBox menu = new VBox(10);
    menu.setPadding(new Insets(15));
    menu.setBackground(new Background(new BackgroundFill(Color.web("#212529"), null, null)));

    String[] menuItems = {"Categorías", "Clubes", "Personas", "Empleados", "Instalaciones", "Grupos", "Equipos", "Licencias"};
    String[] iconPaths = {
        "file:src/resources/iconos/categorias.png",
        "file:src/resources/iconos/club.png",
        "file:src/resources/iconos/persona.png",
        "file:src/resources/iconos/empleado.png",
        "file:src/resources/iconos/instalaciones.png",
        "file:src/resources/iconos/grupos.png",
        "file:src/resources/iconos/teams.png",
        "file:src/resources/iconos/licencia.png"
    };

    for (int i = 0; i < menuItems.length; i++) {
        final String menuItem = menuItems[i]; 
        Button menuButton = new Button(menuItems[i]);
        ImageView icon = new ImageView(new Image(iconPaths[i]));
        icon.setFitHeight(20);
        icon.setFitWidth(20);
        menuButton.setGraphic(icon);
        menuButton.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        menuButton.setTextFill(Color.WHITE);
        menuButton.setPrefWidth(200); 
        menuButton.setAlignment(Pos.CENTER_LEFT); // Alineado izquierda
        menuButton.setContentDisplay(ContentDisplay.LEFT); 
        menuButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        menuButton.setOnMouseEntered(e -> menuButton.setStyle("-fx-background-color: #424242; -fx-text-fill: white; -fx-cursor: hand;"));
        menuButton.setOnMouseExited(e -> menuButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;"));
        menuButton.setOnAction(e -> switchContent(menuItem));
        menu.getChildren().add(menuButton);
    }
    return menu;
    }
    
    private Pane createWelcomePane() {
        VBox welcomePane = new VBox(20);
        welcomePane.setAlignment(Pos.CENTER);
        welcomePane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
        welcomePane.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Bienvenido al Sistema de Gestión FFCV");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        welcomeLabel.setTextFill(Color.web("#212529"));

        Label infoLabel = new Label("Seleccione una opción del menú 'Gestión' para comenzar.\n" +
                "Gestione categorías, clubes, personas, equipos y más.");
        infoLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        infoLabel.setTextFill(Color.web("#424242"));
        infoLabel.setWrapText(true);
        infoLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        welcomePane.getChildren().addAll(welcomeLabel, infoLabel);
        return welcomePane;
    }

    private Pane createCategoriaPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));

        Label nombreLabel = new Label("Nombre:");
        TextField nombreField = new TextField();
        Label ordenLabel = new Label("Nivel/Orden:");
        TextField ordenField = new TextField();
        Label precioLabel = new Label("Precio Licencia:");
        TextField precioField = new TextField();

        Button crearButton = new Button("Crear Categoría");
        ImageView crearIcon = new ImageView(new Image("file:src/resources/iconos/cross.png"));
        crearIcon.setFitHeight(16);
        crearIcon.setFitWidth(16);
        crearButton.setGraphic(crearIcon);
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            try {
                String nombre = nombreField.getText();
                int orden = Integer.parseInt(ordenField.getText());
                double precio = Double.parseDouble(precioField.getText());
                Categoria categoria = federacion.nuevaCategoria(nombre, orden, precio);
                showAlert("Categoría creada: " + categoria, Alert.AlertType.INFORMATION);
                clearFields(nombreField, ordenField, precioField);
            } catch (NumberFormatException ex) {
                showAlert("Nivel y precio deben ser numéricos.", Alert.AlertType.ERROR);
            }
        });

        Button listarButton = new Button("Listar Categorías");
        ImageView listarIcon = new ImageView(new Image("file:src/resources/iconos/magnifier.png"));
        listarIcon.setFitHeight(16);
        listarIcon.setFitWidth(16);
        listarButton.setGraphic(listarIcon);
        styleButton(listarButton, false);
        listarButton.setOnAction(e -> {
            List<Categoria> categorias = federacion.obtenerCategorias();
            TextArea textArea = new TextArea();
            textArea.setEditable(false);
            textArea.setText("Categorías:\n");
            categorias.forEach(c -> textArea.appendText(c.toString() + "\n"));
            showDialog("Categorías", textArea);
        });

        grid.add(nombreLabel, 0, 0);
        grid.add(nombreField, 1, 0);
        grid.add(ordenLabel, 0, 1);
        grid.add(ordenField, 1, 1);
        grid.add(precioLabel, 0, 2);
        grid.add(precioField, 1, 2);
        grid.add(crearButton, 0, 3, 2, 1);
        grid.add(listarButton, 0, 4, 2, 1);

        return grid;
    }

    private Pane createClubPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));

        Label nombreLabel = new Label("Nombre Club:");
        TextField nombreField = new TextField();
        Label fechaLabel = new Label("Fecha Fundación (YYYY-MM-DD):");
        TextField fechaField = new TextField();
        Label dniPresLabel = new Label("DNI Presidente:");
        TextField dniPresField = new TextField();
        Label buscarLabel = new Label("Buscar Club por Nombre:");
        TextField buscarField = new TextField();

        Button crearButton = new Button("Crear Club");
        ImageView crearIcon = new ImageView(new Image("file:src/resources/iconos/cross.png"));
        crearIcon.setFitHeight(16);
        crearIcon.setFitWidth(16);
        crearButton.setGraphic(crearIcon);
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            try {
                String nombre = nombreField.getText();
                LocalDate fecha = LocalDate.parse(fechaField.getText());
                String dni = dniPresField.getText();
                Persona presidente = federacion.buscaPersona(dni);
                if (presidente == null) {
                    showAlert("Presidente no encontrado.", Alert.AlertType.ERROR);
                    return;
                }
                Club club = federacion.nuevoClub(nombre, fecha, presidente);
                showAlert("Club creado: " + club, Alert.AlertType.INFORMATION);
                clearFields(nombreField, fechaField, dniPresField);
            } catch (DateTimeParseException ex) {
                showAlert("Formato de fecha inválido.", Alert.AlertType.ERROR);
            }
        });

        Button buscarButton = new Button("Buscar Club");
        ImageView buscarIcon = new ImageView(new Image("file:src/resources/iconos/magnifier.png"));
        buscarIcon.setFitHeight(16);
        buscarIcon.setFitWidth(16);
        buscarButton.setGraphic(buscarIcon);
        styleButton(buscarButton, false);
        buscarButton.setOnAction(e -> {
            String nombre = buscarField.getText();
            Club club = federacion.buscarClub(nombre);
            showAlert(club != null ? "Club encontrado: " + club : "Club no encontrado.", Alert.AlertType.INFORMATION);
        });

        grid.add(nombreLabel, 0, 0);
        grid.add(nombreField, 1, 0);
        grid.add(fechaLabel, 0, 1);
        grid.add(fechaField, 1, 1);
        grid.add(dniPresLabel, 0, 2);
        grid.add(dniPresField, 1, 2);
        grid.add(buscarLabel, 0, 3);
        grid.add(buscarField, 1, 3);
        grid.add(crearButton, 0, 4, 2, 1);
        grid.add(buscarButton, 0, 5, 2, 1);

        return grid;
    }

    private Pane createPersonaPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));

        Label dniLabel = new Label("DNI:");
        TextField dniField = new TextField();
        Label nombreLabel = new Label("Nombre:");
        TextField nombreField = new TextField();
        Label apellido1Label = new Label("Apellido 1:");
        TextField apellido1Field = new TextField();
        Label apellido2Label = new Label("Apellido 2:");
        TextField apellido2Field = new TextField();
        Label fechaLabel = new Label("Fecha Nacimiento (YYYY-MM-DD):");
        TextField fechaField = new TextField();
        Label poblacionLabel = new Label("Población:");
        TextField poblacionField = new TextField();
        Label buscarDniLabel = new Label("Buscar por DNI:");
        TextField buscarDniField = new TextField();

        Button crearButton = new Button("Crear Persona");
        ImageView crearIcon = new ImageView(new Image("file:src/resources/iconos/cross.png"));
        crearIcon.setFitHeight(16);
        crearIcon.setFitWidth(16);
        crearButton.setGraphic(crearIcon);
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            try {
                String dni = dniField.getText();
                String nombre = nombreField.getText();
                String apellido1 = apellido1Field.getText();
                String apellido2 = apellido2Field.getText();
                LocalDate fecha = LocalDate.parse(fechaField.getText());
                String poblacion = poblacionField.getText();
                Persona persona = federacion.nuevaPersona(dni, nombre, apellido1, apellido2, fecha, "user" + dni, "pass" + dni, poblacion);
                showAlert("Persona creada: " + persona, Alert.AlertType.INFORMATION);
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, poblacionField);
            } catch (DateTimeParseException ex) {
                showAlert("Formato de fecha inválido.", Alert.AlertType.ERROR);
            }
        });

        Button buscarButton = new Button("Buscar Persona");
        ImageView buscarIcon = new ImageView(new Image("file:src/resources/iconos/magnifier.png"));
        buscarIcon.setFitHeight(16);
        buscarIcon.setFitWidth(16);
        buscarButton.setGraphic(buscarIcon);
        styleButton(buscarButton, false);
        buscarButton.setOnAction(e -> {
            String dni = buscarDniField.getText();
            Persona persona = federacion.buscaPersona(dni);
            showAlert(persona != null ? "Persona encontrada: " + persona : "Persona no encontrada.", Alert.AlertType.INFORMATION);
        });

        Button buscarMultiButton = new Button("Buscar por Nombre y Apellidos");
        ImageView buscarMultiIcon = new ImageView(new Image("file:src/resources/iconos/magnifier.png"));
        buscarMultiIcon.setFitHeight(16);
        buscarMultiIcon.setFitWidth(16);
        buscarMultiButton.setGraphic(buscarMultiIcon);
        styleButton(buscarMultiButton, false);
        buscarMultiButton.setOnAction(e -> {
            String nombre = nombreField.getText();
            String apellido1 = apellido1Field.getText();
            String apellido2 = apellido2Field.getText();
            List<Persona> personas = federacion.buscaPersonas(nombre, apellido1, apellido2);
            TextArea textArea = new TextArea();
            textArea.setEditable(false);
            textArea.setText("Personas encontradas:\n");
            personas.forEach(p -> textArea.appendText(p.toString() + "\n"));
            showDialog("Resultados", textArea);
        });

        grid.add(dniLabel, 0, 0);
        grid.add(dniField, 1, 0);
        grid.add(nombreLabel, 0, 1);
        grid.add(nombreField, 1, 1);
        grid.add(apellido1Label, 0, 2);
        grid.add(apellido1Field, 1, 2);
        grid.add(apellido2Label, 0, 3);
        grid.add(apellido2Field, 1, 3);
        grid.add(fechaLabel, 0, 4);
        grid.add(fechaField, 1, 4);
        grid.add(poblacionLabel, 0, 5);
        grid.add(poblacionField, 1, 5);
        grid.add(buscarDniLabel, 0, 6);
        grid.add(buscarDniField, 1, 6);
        grid.add(crearButton, 0, 7, 2, 1);
        grid.add(buscarButton, 0, 8, 2, 1);
        grid.add(buscarMultiButton, 0, 9, 2, 1);

        return grid;
    }

    private Pane createEmpleadoPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));

        Label dniLabel = new Label("DNI:");
        TextField dniField = new TextField();
        Label nombreLabel = new Label("Nombre:");
        TextField nombreField = new TextField();
        Label apellido1Label = new Label("Apellido 1:");
        TextField apellido1Field = new TextField();
        Label fechaLabel = new Label("Fecha Nacimiento (YYYY-MM-DD):");
        TextField fechaField = new TextField();
        Label numEmpLabel = new Label("Número Empleado:");
        TextField numEmpField = new TextField();

        Button crearButton = new Button("Crear Empleado");
        ImageView crearIcon = new ImageView(new Image("file:src/resources/iconos/cross.png"));
        crearIcon.setFitHeight(16);
        crearIcon.setFitWidth(16);
        crearButton.setGraphic(crearIcon);
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            try {
                String dni = dniField.getText();
                String nombre = nombreField.getText();
                String apellido1 = apellido1Field.getText();
                LocalDate fecha = LocalDate.parse(fechaField.getText());
                int numEmp = Integer.parseInt(numEmpField.getText());
                Empleado empleado = federacion.nuevoEmpleado(dni, nombre, apellido1, "Apellido2", fecha,
                        "user" + numEmp, "pass" + numEmp, "Población", numEmp, LocalDate.now(), "SS" + numEmp);
                showAlert("Empleado creado: " + empleado, Alert.AlertType.INFORMATION);
                clearFields(dniField, nombreField, apellido1Field, fechaField, numEmpField);
            } catch (Exception ex) {
                showAlert("Error en los datos: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        grid.add(dniLabel, 0, 0);
        grid.add(dniField, 1, 0);
        grid.add(nombreLabel, 0, 1);
        grid.add(nombreField, 1, 1);
        grid.add(apellido1Label, 0, 2);
        grid.add(apellido1Field, 1, 2);
        grid.add(fechaLabel, 0, 3);
        grid.add(fechaField, 1, 3);
        grid.add(numEmpLabel, 0, 4);
        grid.add(numEmpField, 1, 4);
        grid.add(crearButton, 0, 5, 2, 1);

        return grid;
    }

    private Pane createInstalacionPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));

        Label nombreLabel = new Label("Nombre:");
        TextField nombreField = new TextField();
        Label direccionLabel = new Label("Dirección:");
        TextField direccionField = new TextField();
        Label superficieLabel = new Label("Tipo de Superficie:");
        ComboBox<String> superficieCombo = new ComboBox<>();
        superficieCombo.getItems().addAll("CESPED_NATURAL", "CESPED_ARTIFICIAL", "PAVIMENTO");
        Label buscarLabel = new Label("Buscar por Nombre:");
        TextField buscarField = new TextField();

        Button crearButton = new Button("Crear Instalación");
        ImageView crearIcon = new ImageView(new Image("file:src/resources/iconos/cross.png"));
        crearIcon.setFitHeight(16);
        crearIcon.setFitWidth(16);
        crearButton.setGraphic(crearIcon);
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            String nombre = nombreField.getText();
            String direccion = direccionField.getText();
            String superficie = superficieCombo.getValue();
            Instalacion instalacion = federacion.nuevaInstalacion(nombre, direccion, superficie);
            showAlert("Instalación creada: " + instalacion, Alert.AlertType.INFORMATION);
            clearFields(nombreField, direccionField);
        });

        Button buscarButton = new Button("Buscar Instalaciones");
        ImageView buscarIcon = new ImageView(new Image("file:src/resources/iconos/magnifier.png"));
        buscarIcon.setFitHeight(16);
        buscarIcon.setFitWidth(16);
        buscarButton.setGraphic(buscarIcon);
        styleButton(buscarButton, false);
        buscarButton.setOnAction(e -> {
            String nombre = buscarField.getText();
            List<Instalacion> instalaciones = federacion.buscarInstalaciones(nombre);
            TextArea textArea = new TextArea();
            textArea.setEditable(false);
            textArea.setText("Instalaciones encontradas:\n");
            instalaciones.forEach(i -> textArea.appendText(i.toString() + "\n"));
            showDialog("Resultados", textArea);
        });

        grid.add(nombreLabel, 0, 0);
        grid.add(nombreField, 1, 0);
        grid.add(direccionLabel, 0, 1);
        grid.add(direccionField, 1, 1);
        grid.add(superficieLabel, 0, 2);
        grid.add(superficieCombo, 1, 2);
        grid.add(buscarLabel, 0, 3);
        grid.add(buscarField, 1, 3);
        grid.add(crearButton, 0, 4, 2, 1);
        grid.add(buscarButton, 0, 5, 2, 1);

        return grid;
    }

    private Pane createGrupoPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));

        Label categoriaLabel = new Label("Nombre Categoría:");
        TextField categoriaField = new TextField();
        Label nombreLabel = new Label("Nombre Grupo:");
        TextField nombreField = new TextField();

        Button crearButton = new Button("Crear Grupo");
        ImageView crearIcon = new ImageView(new Image("file:src/resources/iconos/cross.png"));
        crearIcon.setFitHeight(16);
        crearIcon.setFitWidth(16);
        crearButton.setGraphic(crearIcon);
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            String nombreCategoria = categoriaField.getText();
            String nombreGrupo = nombreField.getText();
            Categoria categoria = federacion.obtenerCategorias().stream()
                    .filter(c -> c.getNombre().equalsIgnoreCase(nombreCategoria))
                    .findFirst().orElse(null);
            if (categoria == null) {
                showAlert("Categoría no encontrada.", Alert.AlertType.ERROR);
                return;
            }
            Grupo grupo = federacion.nuevoGrupo(categoria, nombreGrupo);
            showAlert("Grupo creado: " + grupo, Alert.AlertType.INFORMATION);
            clearFields(categoriaField, nombreField);
        });

        Button listarButton = new Button("Listar Grupos");
        ImageView listarIcon = new ImageView(new Image("file:src/resources/iconos/magnifier.png"));
        listarIcon.setFitHeight(16);
        listarIcon.setFitWidth(16);
        listarButton.setGraphic(listarIcon);
        styleButton(listarButton, false);
        listarButton.setOnAction(e -> {
            String nombreCategoria = categoriaField.getText();
            Categoria categoria = federacion.obtenerCategorias().stream()
                    .filter(c -> c.getNombre().equalsIgnoreCase(nombreCategoria))
                    .findFirst().orElse(null);
            if (categoria == null) {
                showAlert("Categoría no encontrada.", Alert.AlertType.ERROR);
                return;
            }
            List<Grupo> grupos = federacion.obtenerGrupos(categoria);
            TextArea textArea = new TextArea();
            textArea.setEditable(false);
            textArea.setText("Grupos en " + categoria.getNombre() + ":\n");
            grupos.forEach(g -> textArea.appendText(g.toString() + "\n"));
            showDialog("Lista de Grupos", textArea);
        });

        grid.add(categoriaLabel, 0, 0);
        grid.add(categoriaField, 1, 0);
        grid.add(nombreLabel, 0, 1);
        grid.add(nombreField, 1, 1);
        grid.add(crearButton, 0, 2, 2, 1);
        grid.add(listarButton, 0, 3, 2, 1);

        return grid;
    }

    private Pane createEquipoPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));

        Label letraLabel = new Label("Letra:");
        TextField letraField = new TextField();
        Label instalacionLabel = new Label("Nombre Instalación:");
        TextField instalacionField = new TextField();
        Label grupoLabel = new Label("Nombre Grupo:");
        TextField grupoField = new TextField();
        Label clubLabel = new Label("Nombre Club (opcional):");
        TextField clubField = new TextField();
        Label buscarLetraLabel = new Label("Letra Equipo para Buscar:");
        TextField buscarLetraField = new TextField();
        Label dniLabel = new Label("DNI Jugador:");
        TextField dniField = new TextField();

        Button crearButton = new Button("Crear Equipo");
        ImageView crearIcon = new ImageView(new Image("file:src/resources/iconos/cross.png"));
        crearIcon.setFitHeight(16);
        crearIcon.setFitWidth(16);
        crearButton.setGraphic(crearIcon);
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
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
                showAlert("No hay categorías disponibles.", Alert.AlertType.ERROR);
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
                    } catch (IllegalArgumentException ex) {
                        showAlert("El equipo ya está en el club.", Alert.AlertType.WARNING);
                    }
                } else {
                    showAlert("Club no encontrado.", Alert.AlertType.WARNING);
                }
            }
            showAlert("Equipo creado: " + equipo, Alert.AlertType.INFORMATION);
            clearFields(letraField, instalacionField, grupoField, clubField);
        });

        Button buscarJugadorButton = new Button("Buscar Jugador");
        ImageView buscarIcon = new ImageView(new Image("file:src/resources/iconos/magnifier.png"));
        buscarIcon.setFitHeight(16);
        buscarIcon.setFitWidth(16);
        buscarJugadorButton.setGraphic(buscarIcon);
        styleButton(buscarJugadorButton, false);
        buscarJugadorButton.setOnAction(e -> {
            String letra = buscarLetraField.getText();
            String dni = dniField.getText();
            Equipo equipo = federacion.obtenerCategorias().stream()
                    .flatMap(c -> federacion.obtenerGrupos(c).stream())
                    .flatMap(g -> g.getEquipos().stream())
                    .filter(e1 -> e1.getLetra().equalsIgnoreCase(letra))
                    .findFirst().orElse(null);
            if (equipo == null) {
                showAlert("Equipo no encontrado con la letra: " + letra, Alert.AlertType.ERROR);
                return;
            }
            Persona jugador = equipo.buscarJugador(dni);
            showAlert(jugador != null ? "Jugador encontrado: " + jugador : "Jugador no encontrado.", Alert.AlertType.INFORMATION);
            clearFields(buscarLetraField, dniField);
        });

        grid.add(letraLabel, 0, 0);
        grid.add(letraField, 1, 0);
        grid.add(instalacionLabel, 0, 1);
        grid.add(instalacionField, 1, 1);
        grid.add(grupoLabel, 0, 2);
        grid.add(grupoField, 1, 2);
        grid.add(clubLabel, 0, 3);
        grid.add(clubField, 1, 3);
        grid.add(crearButton, 0, 4, 2, 1);
        grid.add(buscarLetraLabel, 0, 5);
        grid.add(buscarLetraField, 1, 5);
        grid.add(dniLabel, 0, 6);
        grid.add(dniField, 1, 6);
        grid.add(buscarJugadorButton, 0, 7, 2, 1);

        return grid;
    }

    private Pane createLicenciaPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));

        Label dniLabel = new Label("DNI Persona:");
        TextField dniField = new TextField();
        Label equipoLabel = new Label("Letra Equipo (opcional):");
        TextField equipoField = new TextField();
        Label calcularLabel = new Label("Letra Equipo para Precio:");
        TextField calcularField = new TextField();

        Button crearSimpleButton = new Button("Crear Licencia Simple");
        ImageView crearSimpleIcon = new ImageView(new Image("file:src/resources/iconos/cross.png"));
        crearSimpleIcon.setFitHeight(16);
        crearSimpleIcon.setFitWidth(16);
        crearSimpleButton.setGraphic(crearSimpleIcon);
        styleButton(crearSimpleButton, true);
        crearSimpleButton.setOnAction(e -> {
            String dni = dniField.getText();
            Persona persona = federacion.buscaPersona(dni);
            if (persona == null) {
                showAlert("Persona no encontrada.", Alert.AlertType.ERROR);
                return;
            }
            Licencia licencia = federacion.nuevaLicencia(persona);
            showAlert("Licencia creada: " + licencia, Alert.AlertType.INFORMATION);
            clearFields(dniField);
        });

        Button crearEquipoButton = new Button("Crear Licencia con Equipo");
        ImageView crearEquipoIcon = new ImageView(new Image("file:src/resources/iconos/cross.png"));
        crearEquipoIcon.setFitHeight(16);
        crearEquipoIcon.setFitWidth(16);
        crearEquipoButton.setGraphic(crearEquipoIcon);
        styleButton(crearEquipoButton, true);
        crearEquipoButton.setOnAction(e -> {
            String dni = dniField.getText();
            String letraEquipo = equipoField.getText();
            Persona persona = federacion.buscaPersona(dni);
            if (persona == null) {
                showAlert("Persona no encontrada.", Alert.AlertType.ERROR);
                return;
            }
            Equipo equipo = federacion.obtenerCategorias().stream()
                    .flatMap(c -> federacion.obtenerGrupos(c).stream())
                    .flatMap(g -> g.getEquipos().stream())
                    .filter(e1 -> e1.getLetra().equalsIgnoreCase(letraEquipo))
                    .findFirst().orElse(null);
            if (equipo == null) {
                showAlert("Equipo no encontrado.", Alert.AlertType.ERROR);
                return;
            }
            Licencia licencia = federacion.nuevaLicencia(persona, equipo);
            federacion.addLicencia(licencia, equipo);
            showAlert("Licencia con equipo creada: " + licencia, Alert.AlertType.INFORMATION);
            clearFields(dniField, equipoField);
        });

        Button calcularButton = new Button("Calcular Precio Licencia");
        ImageView calcularIcon = new ImageView(new Image("file:src/resources/iconos/precio_licencia.png"));
        calcularIcon.setFitHeight(16);
        calcularIcon.setFitWidth(16);
        calcularButton.setGraphic(calcularIcon);
        styleButton(calcularButton, false);
        calcularButton.setOnAction(e -> {
            String letraEquipo = calcularField.getText();
            Equipo equipo = federacion.obtenerCategorias().stream()
                    .flatMap(c -> federacion.obtenerGrupos(c).stream())
                    .flatMap(g -> g.getEquipos().stream())
                    .filter(e1 -> e1.getLetra().equalsIgnoreCase(letraEquipo))
                    .findFirst().orElse(null);
            if (equipo == null) {
                showAlert("Equipo no encontrado.", Alert.AlertType.ERROR);
                return;
            }
            double precio = federacion.calcularPrecioLicencia(equipo);
            showAlert("Precio de la licencia: " + precio, Alert.AlertType.INFORMATION);
        });

        grid.add(dniLabel, 0, 0);
        grid.add(dniField, 1, 0);
        grid.add(equipoLabel, 0, 1);
        grid.add(equipoField, 1, 1);
        grid.add(calcularLabel, 0, 2);
        grid.add(calcularField, 1, 2);
        grid.add(crearSimpleButton, 0, 3, 2, 1);
        grid.add(crearEquipoButton, 0, 4, 2, 1);
        grid.add(calcularButton, 0, 5, 2, 1);

        return grid;
    }

    private void switchContent(String option) {
        contentArea.getChildren().clear();
        switch (option) {
            case "Categorías":
                contentArea.getChildren().add(createCategoriaPane());
                break;
            case "Clubes":
                contentArea.getChildren().add(createClubPane());
                break;
            case "Personas":
                contentArea.getChildren().add(createPersonaPane());
                break;
            case "Empleados":
                contentArea.getChildren().add(createEmpleadoPane());
                break;
            case "Instalaciones":
                contentArea.getChildren().add(createInstalacionPane());
                break;
            case "Grupos":
                contentArea.getChildren().add(createGrupoPane());
                break;
            case "Equipos":
                contentArea.getChildren().add(createEquipoPane());
                break;
            case "Licencias":
                contentArea.getChildren().add(createLicenciaPane());
                break;
            default:
                contentArea.getChildren().add(createWelcomePane());
                break;
        }
    }

    private void styleButton(Button button, boolean isCreateButton) {
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        String bgColor = isCreateButton ? "#D32F2F" : "#212529";
        String hoverColor = isCreateButton ? "#EF5350" : "#424242";
        button.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: white;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: white;"));
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showDialog(String title, TextArea content) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().setContent(new ScrollPane(content));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}