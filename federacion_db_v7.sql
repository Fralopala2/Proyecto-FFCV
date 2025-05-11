-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 11-05-2025 a las 18:35:44
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `federacion_db`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE `categoria` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `orden` int(11) NOT NULL,
  `precioLicencia` double NOT NULL CHECK (`precioLicencia` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `categoria`
--

INSERT INTO `categoria` (`id`, `nombre`, `orden`, `precioLicencia`) VALUES
(1, 'Senior', 1, 100);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `club`
--

CREATE TABLE `club` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `fechaFundacion` date NOT NULL,
  `presidente_dni` varchar(20) NOT NULL,
  `secretario_dni` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `club`
--

INSERT INTO `club` (`id`, `nombre`, `fechaFundacion`, `presidente_dni`, `secretario_dni`) VALUES
(1, 'FC Valencia', '1920-01-01', '53095891T', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `club_equipo`
--

CREATE TABLE `club_equipo` (
  `club_id` int(11) NOT NULL,
  `equipo_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `dni` varchar(20) NOT NULL,
  `puesto` varchar(50) NOT NULL,
  `numeroEmpleado` int(11) NOT NULL,
  `inicioContrato` date NOT NULL,
  `segSocial` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`dni`, `puesto`, `numeroEmpleado`, `inicioContrato`, `segSocial`) VALUES
('53095891T', 'Presidente', 1001, '2025-05-10', '123456789012'),
('53097594R', 'Jugador', 1002, '2025-05-10', '123456789011');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `equipo`
--

CREATE TABLE `equipo` (
  `id` int(11) NOT NULL,
  `letra` varchar(10) NOT NULL,
  `instalacion_id` int(11) NOT NULL,
  `grupo_id` int(11) NOT NULL,
  `club_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `equipo`
--

INSERT INTO `equipo` (`id`, `letra`, `instalacion_id`, `grupo_id`, `club_id`) VALUES
(1, 'A', 1, 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `equipo_jugador`
--

CREATE TABLE `equipo_jugador` (
  `equipo_id` int(11) NOT NULL,
  `dni_jugador` varchar(20) NOT NULL,
  `fecha_registro` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupo`
--

CREATE TABLE `grupo` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `categoria_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `grupo`
--

INSERT INTO `grupo` (`id`, `nombre`, `categoria_id`) VALUES
(1, 'Grupo A', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `instalacion`
--

CREATE TABLE `instalacion` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `direccion` varchar(200) NOT NULL,
  `superficie` enum('TIERRA','CESPED_NATURAL','CESPED_ARTIFICIAL') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `instalacion`
--

INSERT INTO `instalacion` (`id`, `nombre`, `direccion`, `superficie`) VALUES
(1, 'Estadio Municipal', 'Calle Mayor', 'CESPED_ARTIFICIAL');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `licencia`
--

CREATE TABLE `licencia` (
  `numeroLicencia` varchar(36) NOT NULL,
  `persona_dni` varchar(20) NOT NULL,
  `equipo_id` int(11) DEFAULT NULL,
  `abonada` tinyint(1) DEFAULT 0,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `persona`
--

CREATE TABLE `persona` (
  `dni` varchar(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido1` varchar(100) NOT NULL,
  `apellido2` varchar(100) DEFAULT NULL,
  `fechaNacimiento` date NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `poblacion` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `persona`
--

INSERT INTO `persona` (`dni`, `nombre`, `apellido1`, `apellido2`, `fechaNacimiento`, `usuario`, `password`, `poblacion`) VALUES
('53095891T', 'Paco', 'Lopez', 'Alarte', '1979-03-01', 'plopez', 'pass123', 'Xirivella'),
('53097594R', 'Sonia', 'Pucheta', 'Barranco', '1977-11-01', 'spu', 'pass123', 'Xirivella');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categoria`
--
ALTER TABLE `categoria`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `club`
--
ALTER TABLE `club`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD KEY `presidente_dni` (`presidente_dni`),
  ADD KEY `secretario_dni` (`secretario_dni`) USING BTREE;

--
-- Indices de la tabla `club_equipo`
--
ALTER TABLE `club_equipo`
  ADD PRIMARY KEY (`club_id`,`equipo_id`),
  ADD KEY `equipo_id` (`equipo_id`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`dni`),
  ADD UNIQUE KEY `numeroEmpleado` (`numeroEmpleado`);

--
-- Indices de la tabla `equipo`
--
ALTER TABLE `equipo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `instalacion_id` (`instalacion_id`),
  ADD KEY `grupo_id` (`grupo_id`),
  ADD KEY `club_id` (`club_id`);

--
-- Indices de la tabla `equipo_jugador`
--
ALTER TABLE `equipo_jugador`
  ADD PRIMARY KEY (`equipo_id`,`dni_jugador`),
  ADD KEY `dni_jugador` (`dni_jugador`);

--
-- Indices de la tabla `grupo`
--
ALTER TABLE `grupo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `categoria_id` (`categoria_id`);

--
-- Indices de la tabla `instalacion`
--
ALTER TABLE `instalacion`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `licencia`
--
ALTER TABLE `licencia`
  ADD PRIMARY KEY (`numeroLicencia`),
  ADD KEY `persona_dni` (`persona_dni`),
  ADD KEY `equipo_id` (`equipo_id`);

--
-- Indices de la tabla `persona`
--
ALTER TABLE `persona`
  ADD PRIMARY KEY (`dni`),
  ADD UNIQUE KEY `usuario` (`usuario`),
  ADD KEY `idx_persona_usuario` (`usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categoria`
--
ALTER TABLE `categoria`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `club`
--
ALTER TABLE `club`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `equipo`
--
ALTER TABLE `equipo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `grupo`
--
ALTER TABLE `grupo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `instalacion`
--
ALTER TABLE `instalacion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `club`
--
ALTER TABLE `club`
  ADD CONSTRAINT `Club_ibfk_1` FOREIGN KEY (`presidente_dni`) REFERENCES `persona` (`dni`);

--
-- Filtros para la tabla `club_equipo`
--
ALTER TABLE `club_equipo`
  ADD CONSTRAINT `club_equipo_ibfk_1` FOREIGN KEY (`club_id`) REFERENCES `club` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `club_equipo_ibfk_2` FOREIGN KEY (`equipo_id`) REFERENCES `equipo` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD CONSTRAINT `Empleado_ibfk_1` FOREIGN KEY (`dni`) REFERENCES `persona` (`dni`) ON DELETE CASCADE;

--
-- Filtros para la tabla `equipo`
--
ALTER TABLE `equipo`
  ADD CONSTRAINT `Equipo_ibfk_1` FOREIGN KEY (`instalacion_id`) REFERENCES `instalacion` (`id`),
  ADD CONSTRAINT `Equipo_ibfk_2` FOREIGN KEY (`grupo_id`) REFERENCES `grupo` (`id`),
  ADD CONSTRAINT `Equipo_ibfk_3` FOREIGN KEY (`club_id`) REFERENCES `club` (`id`);

--
-- Filtros para la tabla `equipo_jugador`
--
ALTER TABLE `equipo_jugador`
  ADD CONSTRAINT `equipo_jugador_ibfk_1` FOREIGN KEY (`equipo_id`) REFERENCES `equipo` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `equipo_jugador_ibfk_2` FOREIGN KEY (`dni_jugador`) REFERENCES `persona` (`dni`) ON DELETE CASCADE;

--
-- Filtros para la tabla `grupo`
--
ALTER TABLE `grupo`
  ADD CONSTRAINT `Grupo_ibfk_1` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `licencia`
--
ALTER TABLE `licencia`
  ADD CONSTRAINT `Licencia_ibfk_1` FOREIGN KEY (`persona_dni`) REFERENCES `persona` (`dni`),
  ADD CONSTRAINT `Licencia_ibfk_2` FOREIGN KEY (`equipo_id`) REFERENCES `equipo` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
