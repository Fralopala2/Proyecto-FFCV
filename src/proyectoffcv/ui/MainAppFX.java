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

        mainLayout = new BorderPane();
        mainLayout.setPrefSize(1000, 750);

        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, null,
                new Stop(0, Color.web("#F5F7FA")),
                new Stop(1, Color.web("#C8E6C9")));
        mainLayout.setBackground(new Background(new BackgroundFill(gradient, null, null)));

        mainLayout.setTop(createHeader());
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(15));
        contentArea.getChildren().add(createWelcomePane());
        mainLayout.setCenter(contentArea);
        mainLayout.setLeft(createMenu());

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
            Button menuButton = new Button(menuItems[i]);
            ImageView icon = new ImageView(new Image(iconPaths[i]));
            icon.setFitHeight(20);
            icon.setFitWidth(20);
            menuButton.setGraphic(icon);
            menuButton.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
            menuButton.setTextFill(Color.WHITE);
            menuButton.setPrefWidth(200);
            menuButton.setAlignment(Pos.CENTER_LEFT);
            menuButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            menuButton.setOnMouseEntered(e -> menuButton.setStyle("-fx-background-color: #424242; -fx-text-fill: white; -fx-cursor: hand;"));
            menuButton.setOnMouseExited(e -> menuButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;"));
            menuButton.setOnAction(e -> switchContent(menuButton.getText()));
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

        Button crearButton = new Button("Crear Categoría", new ImageView(new Image("file:src/resources/iconos/cross.png")));
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            try {
                String nombre = nombreField.getText();
                int orden = Integer.parseInt(ordenField.getText());
                double precio = Double.parseDouble(precioField.getText());
                Categoria categoria = new Categoria(nombre, orden, precio);
                categoria.guardar();
                showAlert("Categoría creada: " + categoria, Alert.AlertType.INFORMATION);
                clearFields(nombreField, ordenField, precioField);
            } catch (NumberFormatException ex) {
                showAlert("Nivel y precio deben ser numéricos.", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                showAlert("Error al guardar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button listarButton = new Button("Listar Categorías", new ImageView(new Image("file:src/resources/iconos/magnifier.png")));
        styleButton(listarButton, false);
        listarButton.setOnAction(e -> {
            try {
                List<Categoria> categorias = Categoria.obtenerTodas();
                TextArea textArea = new TextArea("Categorías:\n" + categorias.stream().map(Categoria::toString).reduce("", (a, b) -> a + b + "\n"));
                textArea.setEditable(false);
                showDialog("Categorías", textArea);
            } catch (Exception ex) {
                showAlert("Error al listar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
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
        grid.setVgap(15); // Corrección aquí
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

        Button crearButton = new Button("Crear Club", new ImageView(new Image("file:src/resources/iconos/cross.png")));
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            try {
                String nombre = nombreField.getText();
                LocalDate fecha = LocalDate.parse(fechaField.getText());
                String dni = dniPresField.getText();
                Persona presidente = Persona.buscarPorDni(dni);
                if (presidente == null) {
                    showAlert("Presidente no encontrado.", Alert.AlertType.ERROR);
                    return;
                }
                Club club = new Club(nombre, fecha, presidente);
                club.guardar();
                showAlert("Club creado: " + club, Alert.AlertType.INFORMATION);
                clearFields(nombreField, fechaField, dniPresField);
            } catch (DateTimeParseException ex) {
                showAlert("Formato de fecha inválido.", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                showAlert("Error al guardar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button buscarButton = new Button("Buscar Club", new ImageView(new Image("file:src/resources/iconos/magnifier.png")));
        styleButton(buscarButton, false);
        buscarButton.setOnAction(e -> {
            try {
                String nombre = buscarField.getText();
                Club club = Club.buscarPorNombre(nombre);
                showAlert(club != null ? "Club encontrado: " + club : "Club no encontrado.", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                showAlert("Error al buscar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
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

        Button crearButton = new Button("Crear Persona", new ImageView(new Image("file:src/resources/iconos/cross.png")));
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            try {
                String dni = dniField.getText();
                String nombre = nombreField.getText();
                String apellido1 = apellido1Field.getText();
                String apellido2 = apellido2Field.getText();
                LocalDate fecha = LocalDate.parse(fechaField.getText());
                String poblacion = poblacionField.getText();
                Persona persona = new Persona(dni, nombre, apellido1, apellido2, fecha, "user" + dni, "pass" + dni, poblacion);
                persona.guardar();
                showAlert("Persona creada: " + persona, Alert.AlertType.INFORMATION);
                clearFields(dniField, nombreField, apellido1Field, apellido2Field, fechaField, poblacionField);
            } catch (DateTimeParseException ex) {
                showAlert("Formato de fecha inválido.", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                showAlert("Error al guardar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button buscarButton = new Button("Buscar Persona", new ImageView(new Image("file:src/resources/iconos/magnifier.png")));
        styleButton(buscarButton, false);
        buscarButton.setOnAction(e -> {
            try {
                String dni = buscarDniField.getText();
                Persona persona = Persona.buscarPorDni(dni);
                showAlert(persona != null ? "Persona encontrada: " + persona : "Persona no encontrada.", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                showAlert("Error al buscar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button buscarMultiButton = new Button("Buscar por Nombre y Apellidos", new ImageView(new Image("file:src/resources/iconos/magnifier.png")));
        styleButton(buscarMultiButton, false);
        buscarMultiButton.setOnAction(e -> {
            try {
                String nombre = nombreField.getText();
                String apellido1 = apellido1Field.getText();
                String apellido2 = apellido2Field.getText();
                List<Persona> personas = Persona.buscarPorNombreYApellidos(nombre, apellido1, apellido2);
                TextArea textArea = new TextArea("Personas encontradas:\n" + personas.stream().map(Persona::toString).reduce("", (a, b) -> a + b + "\n"));
                textArea.setEditable(false);
                showDialog("Resultados", textArea);
            } catch (Exception ex) {
                showAlert("Error al buscar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
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
        Label puestoLabel = new Label("Puesto:");
        TextField puestoField = new TextField();
        Label inicioContratoLabel = new Label("Inicio Contrato (YYYY-MM-DD):");
        TextField inicioContratoField = new TextField();
        Label segSocialLabel = new Label("Seguridad Social:");
        TextField segSocialField = new TextField();

        Button crearButton = new Button("Crear Empleado", new ImageView(new Image("file:src/resources/iconos/cross.png")));
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            try {
                String dni = dniField.getText();
                String nombre = nombreField.getText();
                String apellido1 = apellido1Field.getText();
                LocalDate fecha = LocalDate.parse(fechaField.getText());
                int numEmp = Integer.parseInt(numEmpField.getText());
                String puesto = puestoField.getText();
                LocalDate inicioContrato = LocalDate.parse(inicioContratoField.getText());
                String segSocial = segSocialField.getText();
                Empleado empleado = new Empleado(dni, nombre, apellido1, "Apellido2", fecha, "user" + numEmp, "pass" + numEmp, "Población", puesto, numEmp, inicioContrato, segSocial);
                empleado.guardar();
                showAlert("Empleado creado: " + empleado, Alert.AlertType.INFORMATION);
                clearFields(dniField, nombreField, apellido1Field, fechaField, numEmpField, puestoField, inicioContratoField, segSocialField);
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
        grid.add(puestoLabel, 0, 5);
        grid.add(puestoField, 1, 5);
        grid.add(inicioContratoLabel, 0, 6);
        grid.add(inicioContratoField, 1, 6);
        grid.add(segSocialLabel, 0, 7);
        grid.add(segSocialField, 1, 7);
        grid.add(crearButton, 0, 8, 2, 1);

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
        ComboBox<Instalacion.TipoSuperficie> superficieCombo = new ComboBox<>();
        superficieCombo.getItems().addAll(Instalacion.TipoSuperficie.values());
        Label buscarLabel = new Label("Buscar por Nombre:");
        TextField buscarField = new TextField();

        Button crearButton = new Button("Crear Instalación", new ImageView(new Image("file:src/resources/iconos/cross.png")));
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            try {
                String nombre = nombreField.getText();
                String direccion = direccionField.getText();
                Instalacion.TipoSuperficie superficie = superficieCombo.getValue();
                Instalacion instalacion = new Instalacion(nombre, direccion, superficie);
                instalacion.guardar();
                showAlert("Instalación creada: " + instalacion, Alert.AlertType.INFORMATION);
                clearFields(nombreField, direccionField);
            } catch (Exception ex) {
                showAlert("Error al guardar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button buscarButton = new Button("Buscar Instalaciones", new ImageView(new Image("file:src/resources/iconos/magnifier.png")));
        styleButton(buscarButton, false);
        buscarButton.setOnAction(e -> {
            try {
                String nombre = buscarField.getText();
                List<Instalacion> instalaciones = Instalacion.buscarPorNombreParcial(nombre);
                TextArea textArea = new TextArea("Instalaciones encontradas:\n" + instalaciones.stream().map(Instalacion::toString).reduce("", (a, b) -> a + b + "\n"));
                textArea.setEditable(false);
                showDialog("Resultados", textArea);
            } catch (Exception ex) {
                showAlert("Error al buscar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
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

        Button crearButton = new Button("Crear Grupo", new ImageView(new Image("file:src/resources/iconos/cross.png")));
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            try {
                String nombreCategoria = categoriaField.getText();
                String nombreGrupo = nombreField.getText();
                Categoria categoria = Categoria.buscarPorNombre(nombreCategoria);
                if (categoria == null) {
                    showAlert("Categoría no encontrada.", Alert.AlertType.ERROR);
                    return;
                }
                Grupo grupo = new Grupo(nombreGrupo);
                grupo.setCategoria(categoria);
                grupo.guardar();
                showAlert("Grupo creado: " + grupo, Alert.AlertType.INFORMATION);
                clearFields(categoriaField, nombreField);
            } catch (Exception ex) {
                showAlert("Error al guardar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button listarButton = new Button("Listar Grupos", new ImageView(new Image("file:src/resources/iconos/magnifier.png")));
        styleButton(listarButton, false);
        listarButton.setOnAction(e -> {
            try {
                String nombreCategoria = categoriaField.getText();
                Categoria categoria = Categoria.buscarPorNombre(nombreCategoria);
                if (categoria == null) {
                    showAlert("Categoría no encontrada.", Alert.AlertType.ERROR);
                    return;
                }
                List<Grupo> grupos = Grupo.obtenerPorCategoria(categoria);
                TextArea textArea = new TextArea("Grupos en " + categoria.getNombre() + ":\n" + grupos.stream().map(Grupo::toString).reduce("", (a, b) -> a + b + "\n"));
                textArea.setEditable(false);
                showDialog("Lista de Grupos", textArea);
            } catch (Exception ex) {
                showAlert("Error al listar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
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

        Button crearButton = new Button("Crear Equipo", new ImageView(new Image("file:src/resources/iconos/cross.png")));
        styleButton(crearButton, true);
        crearButton.setOnAction(e -> {
            try {
                String letra = letraField.getText();
                String nombreInstalacion = instalacionField.getText();
                String nombreGrupo = grupoField.getText();
                String nombreClub = clubField.getText();

                Instalacion instalacion = Instalacion.buscarPorNombre(nombreInstalacion);
                if (instalacion == null) {
                    instalacion = new Instalacion(nombreInstalacion, "Dirección por defecto", Instalacion.TipoSuperficie.CESPED_NATURAL);
                    instalacion.guardar();
                }

                Grupo grupo = Grupo.buscarPorNombre(nombreGrupo);
                if (grupo == null) {
                    showAlert("Grupo no encontrado.", Alert.AlertType.ERROR);
                    return;
                }

                Equipo equipo = new Equipo(letra, instalacion, grupo);
                equipo.guardar();

                if (!nombreClub.isEmpty()) {
                    Club club = Club.buscarPorNombre(nombreClub);
                    if (club != null) {
                        club.addEquipo(equipo);
                    } else {
                        showAlert("Club no encontrado.", Alert.AlertType.WARNING);
                    }
                }

                showAlert("Equipo creado: " + equipo, Alert.AlertType.INFORMATION);
                clearFields(letraField, instalacionField, grupoField, clubField);
            } catch (Exception ex) {
                showAlert("Error al guardar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button buscarJugadorButton = new Button("Buscar Jugador", new ImageView(new Image("file:src/resources/iconos/magnifier.png")));
        styleButton(buscarJugadorButton, false);
        buscarJugadorButton.setOnAction(e -> {
            try {
                String letra = buscarLetraField.getText();
                String dni = dniField.getText();
                Equipo equipo = Equipo.buscarPorLetra(letra);
                if (equipo == null) {
                    showAlert("Equipo no encontrado con la letra: " + letra, Alert.AlertType.ERROR);
                    return;
                }
                Persona jugador = equipo.buscarJugador(dni);
                showAlert(jugador != null ? "Jugador encontrado: " + jugador : "Jugador no encontrado.", Alert.AlertType.INFORMATION);
                clearFields(buscarLetraField, dniField);
            } catch (Exception ex) {
                showAlert("Error al buscar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
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

        Button crearSimpleButton = new Button("Crear Licencia Simple", new ImageView(new Image("file:src/resources/iconos/cross.png")));
        styleButton(crearSimpleButton, true);
        crearSimpleButton.setOnAction(e -> {
            try {
                String dni = dniField.getText();
                Persona persona = Persona.buscarPorDni(dni);
                if (persona == null) {
                    showAlert("Persona no encontrada.", Alert.AlertType.ERROR);
                    return;
                }
                Licencia licencia = new Licencia(persona, "LIC" + System.currentTimeMillis());
                licencia.guardar();
                showAlert("Licencia creada: " + licencia, Alert.AlertType.INFORMATION);
                clearFields(dniField);
            } catch (Exception ex) {
                showAlert("Error al guardar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button crearEquipoButton = new Button("Crear Licencia con Equipo", new ImageView(new Image("file:src/resources/iconos/cross.png")));
        styleButton(crearEquipoButton, true);
        crearEquipoButton.setOnAction(e -> {
            try {
                String dni = dniField.getText();
                String letraEquipo = equipoField.getText();
                Persona persona = Persona.buscarPorDni(dni);
                if (persona == null) {
                    showAlert("Persona no encontrada.", Alert.AlertType.ERROR);
                    return;
                }
                Equipo equipo = Equipo.buscarPorLetra(letraEquipo);
                if (equipo == null) {
                    showAlert("Equipo no encontrado.", Alert.AlertType.ERROR);
                    return;
                }
                Licencia licencia = new Licencia(persona, "LIC" + System.currentTimeMillis());
                licencia.guardar();
                licencia.asignarAEquipo(equipo);
                showAlert("Licencia con equipo creada: " + licencia, Alert.AlertType.INFORMATION);
                clearFields(dniField, equipoField);
            } catch (Exception ex) {
                showAlert("Error al guardar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button calcularButton = new Button("Calcular Precio Licencia", new ImageView(new Image("file:src/resources/iconos/precio_licencia.png")));
        styleButton(calcularButton, false);
        calcularButton.setOnAction(e -> {
            try {
                String letraEquipo = calcularField.getText();
                Equipo equipo = Equipo.buscarPorLetra(letraEquipo);
                if (equipo == null) {
                    showAlert("Equipo no encontrado.", Alert.AlertType.ERROR);
                    return;
                }
                double precio = equipo.calcularPrecioLicencia();
                showAlert("Precio de la licencia: " + precio, Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                showAlert("Error al calcular: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
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
            case "Categorías": contentArea.getChildren().add(createCategoriaPane()); break;
            case "Clubes": contentArea.getChildren().add(createClubPane()); break;
            case "Personas": contentArea.getChildren().add(createPersonaPane()); break;
            case "Empleados": contentArea.getChildren().add(createEmpleadoPane()); break;
            case "Instalaciones": contentArea.getChildren().add(createInstalacionPane()); break;
            case "Grupos": contentArea.getChildren().add(createGrupoPane()); break;
            case "Equipos": contentArea.getChildren().add(createEquipoPane()); break;
            case "Licencias": contentArea.getChildren().add(createLicenciaPane()); break;
            default: contentArea.getChildren().add(createWelcomePane()); break;
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
        for (TextField field : fields) field.clear();
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