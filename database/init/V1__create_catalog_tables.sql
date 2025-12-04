-- ============================================================================
-- V1: Creación de Tablas de Catálogos
-- ============================================================================
-- Descripción: Script para crear todas las tablas de catálogos (lookup tables)
--              que son necesarias como referencia para las tablas principales.
-- Autor: Equipo de Desarrollo OSS UPC
-- Fecha: 2025-01-15
-- ============================================================================

-- ============================================================================
-- CATÁLOGOS DE EMPLEADOS
-- ============================================================================

-- Catálogo de Estados de Empleado (ACTIVO, INACTIVO, CESADO, etc.)
CREATE TABLE cat_estado_empleado (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE cat_estado_empleado IS 'Catálogo de estados posibles de un empleado';
COMMENT ON COLUMN cat_estado_empleado.codigo IS 'Código único del estado (ej: ACTIVO, CESADO)';
COMMENT ON COLUMN cat_estado_empleado.activo IS 'Indica si el registro está activo en el sistema';

-- Catálogo de Áreas (Departamentos/Divisiones de la organización)
CREATE TABLE cat_area (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    id_area_superior INTEGER,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_area_superior FOREIGN KEY (id_area_superior)
        REFERENCES cat_area(id) ON DELETE SET NULL
);

COMMENT ON TABLE cat_area IS 'Catálogo de áreas organizacionales con estructura jerárquica';
COMMENT ON COLUMN cat_area.id_area_superior IS 'Referencia al área padre en la jerarquía organizacional';

-- Catálogo de Puestos (Cargos/Posiciones)
CREATE TABLE cat_puesto (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    id_area INTEGER,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_puesto_area FOREIGN KEY (id_area)
        REFERENCES cat_area(id) ON DELETE SET NULL
);

COMMENT ON TABLE cat_puesto IS 'Catálogo de puestos de trabajo vinculados a áreas';
COMMENT ON COLUMN cat_puesto.id_area IS 'Área a la que pertenece el puesto';

-- Catálogo de Sedes (Ubicaciones físicas)
CREATE TABLE cat_sede (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(255),
    ciudad VARCHAR(100),
    pais VARCHAR(100) DEFAULT 'Perú',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE cat_sede IS 'Catálogo de sedes o ubicaciones físicas de la organización';

-- ============================================================================
-- CATÁLOGOS DE DISPOSITIVOS
-- ============================================================================

-- Catálogo de Tipos de Dispositivo (Laptop, Monitor, Mouse, etc.)
CREATE TABLE cat_tipo_dispositivo (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    requiere_serie BOOLEAN NOT NULL DEFAULT TRUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE cat_tipo_dispositivo IS 'Catálogo de tipos de dispositivos IT';
COMMENT ON COLUMN cat_tipo_dispositivo.requiere_serie IS 'Indica si el tipo de dispositivo requiere número de serie';

-- Catálogo de Marcas (Dell, HP, Lenovo, etc.)
CREATE TABLE cat_marca (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE cat_marca IS 'Catálogo de marcas de dispositivos';

-- Catálogo de Estados de Dispositivo (DISPONIBLE, ASIGNADO, EN_REPARACION, etc.)
CREATE TABLE cat_estado_dispositivo (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    disponible_asignacion BOOLEAN NOT NULL DEFAULT FALSE,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE cat_estado_dispositivo IS 'Catálogo de estados de un dispositivo';
COMMENT ON COLUMN cat_estado_dispositivo.disponible_asignacion IS 'Indica si el dispositivo puede ser asignado en este estado';

-- Catálogo de Proveedores
CREATE TABLE cat_proveedor (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    razon_social VARCHAR(200) NOT NULL,
    nombre_comercial VARCHAR(200),
    ruc VARCHAR(11),
    email VARCHAR(100),
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE cat_proveedor IS 'Catálogo de proveedores de dispositivos';
COMMENT ON COLUMN cat_proveedor.ruc IS 'RUC del proveedor (Perú)';

-- ============================================================================
-- CATÁLOGOS DE ASIGNACIONES
-- ============================================================================

-- Catálogo de Estados de Asignación (ACTIVA, DEVUELTA, CANCELADA)
CREATE TABLE cat_estado_asignacion (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE cat_estado_asignacion IS 'Catálogo de estados de una asignación de dispositivo';

-- ============================================================================
-- CATÁLOGOS DE REEMPLAZOS
-- ============================================================================

-- Catálogo de Motivos de Reemplazo (FALLA, OBSOLESCENCIA, UPGRADE, etc.)
CREATE TABLE cat_motivo_reemplazo (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE cat_motivo_reemplazo IS 'Catálogo de motivos de reemplazo de dispositivos';

-- Catálogo de Estados de Reemplazo (PENDIENTE, APROBADO, COMPLETADO, RECHAZADO)
CREATE TABLE cat_estado_reemplazo (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE cat_estado_reemplazo IS 'Catálogo de estados del proceso de reemplazo';

-- ============================================================================
-- CATÁLOGOS DE DEVOLUCIONES
-- ============================================================================

-- Catálogo de Estados de Solicitud de Devolución
CREATE TABLE cat_estado_solicitud (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE cat_estado_solicitud IS 'Catálogo de estados de una solicitud de devolución';

-- Catálogo de Condiciones de Devolución (EXCELENTE, BUENO, REGULAR, MALO, DAÑADO)
CREATE TABLE cat_condicion_devolucion (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE cat_condicion_devolucion IS 'Catálogo de condiciones físicas al devolver un dispositivo';

-- ============================================================================
-- CATÁLOGOS DE AUDITORÍA
-- ============================================================================

-- Catálogo de Tipos de Movimiento para el Historial
CREATE TABLE cat_tipo_movimiento (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE cat_tipo_movimiento IS 'Catálogo de tipos de movimientos registrados en el historial de dispositivos';
COMMENT ON COLUMN cat_tipo_movimiento.codigo IS 'Códigos: ASIGNACION, DEVOLUCION, REEMPLAZO, BAJA, REPARACION, etc.';

-- ============================================================================
-- FIN DEL SCRIPT V1
-- ============================================================================
