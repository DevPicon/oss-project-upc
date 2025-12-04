-- ============================================================================
-- V2: Creación de Tablas Principales
-- ============================================================================
-- Descripción: Script para crear las tablas principales del sistema OSS
--              con todas sus relaciones a tablas de catálogos.
-- Autor: Equipo de Desarrollo OSS UPC
-- Fecha: 2025-01-15
-- ============================================================================

-- ============================================================================
-- TABLA DE USUARIOS
-- ============================================================================

-- Tabla de Usuarios del Sistema
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    nombre_completo VARCHAR(200) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultima_actualizacion TIMESTAMP
);

COMMENT ON TABLE usuario IS 'Usuarios del sistema con credenciales de acceso';
COMMENT ON COLUMN usuario.password_hash IS 'Hash del password (BCrypt recomendado)';

-- ============================================================================
-- TABLA DE EMPLEADOS
-- ============================================================================

-- Tabla Principal de Empleados
CREATE TABLE empleado (
    id SERIAL PRIMARY KEY,
    codigo_empleado VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido_paterno VARCHAR(100) NOT NULL,
    apellido_materno VARCHAR(100),
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    id_area INTEGER NOT NULL,
    id_puesto INTEGER NOT NULL,
    id_sede INTEGER NOT NULL,
    fecha_ingreso DATE NOT NULL,
    fecha_termino DATE,
    id_estado_empleado INTEGER NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultima_actualizacion TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_empleado_area FOREIGN KEY (id_area)
        REFERENCES cat_area(id) ON DELETE RESTRICT,
    CONSTRAINT fk_empleado_puesto FOREIGN KEY (id_puesto)
        REFERENCES cat_puesto(id) ON DELETE RESTRICT,
    CONSTRAINT fk_empleado_sede FOREIGN KEY (id_sede)
        REFERENCES cat_sede(id) ON DELETE RESTRICT,
    CONSTRAINT fk_empleado_estado FOREIGN KEY (id_estado_empleado)
        REFERENCES cat_estado_empleado(id) ON DELETE RESTRICT,

    -- Constraints
    CONSTRAINT chk_empleado_email CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT chk_empleado_fechas CHECK (fecha_termino IS NULL OR fecha_termino >= fecha_ingreso)
);

COMMENT ON TABLE empleado IS 'Empleados de la organización';
COMMENT ON COLUMN empleado.codigo_empleado IS 'Código único del empleado en la organización';
COMMENT ON COLUMN empleado.fecha_termino IS 'Fecha de término de la relación laboral (NULL si está activo)';

-- ============================================================================
-- TABLA DE DISPOSITIVOS
-- ============================================================================

-- Tabla Principal de Dispositivos IT
CREATE TABLE dispositivo (
    id SERIAL PRIMARY KEY,
    codigo_activo VARCHAR(50) NOT NULL UNIQUE,
    numero_serie VARCHAR(100) UNIQUE,
    id_tipo_dispositivo INTEGER NOT NULL,
    id_marca INTEGER NOT NULL,
    modelo VARCHAR(100),
    id_estado_dispositivo INTEGER NOT NULL,
    especificaciones TEXT,
    fecha_adquisicion DATE,
    valor_adquisicion NUMERIC(10, 2),
    id_proveedor INTEGER,
    observaciones TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultima_actualizacion TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_dispositivo_tipo FOREIGN KEY (id_tipo_dispositivo)
        REFERENCES cat_tipo_dispositivo(id) ON DELETE RESTRICT,
    CONSTRAINT fk_dispositivo_marca FOREIGN KEY (id_marca)
        REFERENCES cat_marca(id) ON DELETE RESTRICT,
    CONSTRAINT fk_dispositivo_estado FOREIGN KEY (id_estado_dispositivo)
        REFERENCES cat_estado_dispositivo(id) ON DELETE RESTRICT,
    CONSTRAINT fk_dispositivo_proveedor FOREIGN KEY (id_proveedor)
        REFERENCES cat_proveedor(id) ON DELETE SET NULL,

    -- Constraints
    CONSTRAINT chk_dispositivo_valor CHECK (valor_adquisicion IS NULL OR valor_adquisicion >= 0)
);

COMMENT ON TABLE dispositivo IS 'Dispositivos IT de la organización';
COMMENT ON COLUMN dispositivo.codigo_activo IS 'Código de activo fijo del dispositivo';
COMMENT ON COLUMN dispositivo.numero_serie IS 'Número de serie del fabricante';
COMMENT ON COLUMN dispositivo.especificaciones IS 'Especificaciones técnicas en formato JSON o texto';

-- ============================================================================
-- TABLA DE ASIGNACIONES
-- ============================================================================

-- Tabla de Asignaciones de Dispositivos a Empleados
CREATE TABLE asignacion_dispositivo (
    id SERIAL PRIMARY KEY,
    id_dispositivo INTEGER NOT NULL,
    id_empleado INTEGER NOT NULL,
    fecha_asignacion DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_devolucion DATE,
    id_usuario_asigna INTEGER NOT NULL,
    id_usuario_recibe INTEGER,
    id_estado_asignacion INTEGER NOT NULL,
    observaciones_asignacion TEXT,
    observaciones_devolucion TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultima_actualizacion TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_asignacion_dispositivo FOREIGN KEY (id_dispositivo)
        REFERENCES dispositivo(id) ON DELETE RESTRICT,
    CONSTRAINT fk_asignacion_empleado FOREIGN KEY (id_empleado)
        REFERENCES empleado(id) ON DELETE RESTRICT,
    CONSTRAINT fk_asignacion_usuario_asigna FOREIGN KEY (id_usuario_asigna)
        REFERENCES usuario(id) ON DELETE RESTRICT,
    CONSTRAINT fk_asignacion_usuario_recibe FOREIGN KEY (id_usuario_recibe)
        REFERENCES usuario(id) ON DELETE RESTRICT,
    CONSTRAINT fk_asignacion_estado FOREIGN KEY (id_estado_asignacion)
        REFERENCES cat_estado_asignacion(id) ON DELETE RESTRICT,

    -- Constraints
    CONSTRAINT chk_asignacion_fechas CHECK (fecha_devolucion IS NULL OR fecha_devolucion >= fecha_asignacion)
);

COMMENT ON TABLE asignacion_dispositivo IS 'Registro de asignaciones de dispositivos a empleados';
COMMENT ON COLUMN asignacion_dispositivo.fecha_devolucion IS 'Fecha de devolución (NULL si aún está asignado)';
COMMENT ON COLUMN asignacion_dispositivo.id_usuario_asigna IS 'Usuario que realizó la asignación';
COMMENT ON COLUMN asignacion_dispositivo.id_usuario_recibe IS 'Usuario que recibió la devolución';

-- ============================================================================
-- TABLA DE REEMPLAZOS
-- ============================================================================

-- Tabla de Reemplazos de Dispositivos
CREATE TABLE reemplazo_dispositivo (
    id SERIAL PRIMARY KEY,
    id_asignacion_original INTEGER NOT NULL,
    id_dispositivo_original INTEGER NOT NULL,
    id_dispositivo_reemplazo INTEGER NOT NULL,
    id_empleado INTEGER NOT NULL,
    fecha_reemplazo DATE NOT NULL DEFAULT CURRENT_DATE,
    id_motivo_reemplazo INTEGER NOT NULL,
    descripcion_motivo TEXT,
    id_usuario_registra INTEGER NOT NULL,
    id_estado_reemplazo INTEGER NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultima_actualizacion TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_reemplazo_asignacion FOREIGN KEY (id_asignacion_original)
        REFERENCES asignacion_dispositivo(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reemplazo_dispositivo_original FOREIGN KEY (id_dispositivo_original)
        REFERENCES dispositivo(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reemplazo_dispositivo_reemplazo FOREIGN KEY (id_dispositivo_reemplazo)
        REFERENCES dispositivo(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reemplazo_empleado FOREIGN KEY (id_empleado)
        REFERENCES empleado(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reemplazo_motivo FOREIGN KEY (id_motivo_reemplazo)
        REFERENCES cat_motivo_reemplazo(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reemplazo_usuario FOREIGN KEY (id_usuario_registra)
        REFERENCES usuario(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reemplazo_estado FOREIGN KEY (id_estado_reemplazo)
        REFERENCES cat_estado_reemplazo(id) ON DELETE RESTRICT,

    -- Constraints
    CONSTRAINT chk_reemplazo_dispositivos CHECK (id_dispositivo_original <> id_dispositivo_reemplazo)
);

COMMENT ON TABLE reemplazo_dispositivo IS 'Registro de reemplazos de dispositivos asignados';
COMMENT ON COLUMN reemplazo_dispositivo.id_asignacion_original IS 'Asignación original que se está reemplazando';
COMMENT ON COLUMN reemplazo_dispositivo.descripcion_motivo IS 'Descripción detallada del motivo del reemplazo';

-- ============================================================================
-- TABLA DE SOLICITUDES DE DEVOLUCIÓN
-- ============================================================================

-- Tabla de Solicitudes de Devolución (típicamente por cese de empleado)
CREATE TABLE solicitud_devolucion (
    id SERIAL PRIMARY KEY,
    id_empleado INTEGER NOT NULL,
    fecha_solicitud DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_termino_empleado DATE NOT NULL,
    fecha_devolucion_programada DATE NOT NULL,
    fecha_devolucion_real DATE,
    id_estado_solicitud INTEGER NOT NULL,
    id_usuario_solicita INTEGER NOT NULL,
    id_usuario_recibe INTEGER,
    observaciones TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultima_actualizacion TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_solicitud_empleado FOREIGN KEY (id_empleado)
        REFERENCES empleado(id) ON DELETE RESTRICT,
    CONSTRAINT fk_solicitud_estado FOREIGN KEY (id_estado_solicitud)
        REFERENCES cat_estado_solicitud(id) ON DELETE RESTRICT,
    CONSTRAINT fk_solicitud_usuario_solicita FOREIGN KEY (id_usuario_solicita)
        REFERENCES usuario(id) ON DELETE RESTRICT,
    CONSTRAINT fk_solicitud_usuario_recibe FOREIGN KEY (id_usuario_recibe)
        REFERENCES usuario(id) ON DELETE RESTRICT,

    -- Constraints
    CONSTRAINT chk_solicitud_fechas CHECK (
        fecha_devolucion_programada >= fecha_termino_empleado AND
        (fecha_devolucion_real IS NULL OR fecha_devolucion_real >= fecha_solicitud)
    )
);

COMMENT ON TABLE solicitud_devolucion IS 'Solicitudes de devolución de dispositivos (generalmente por cese de empleado)';
COMMENT ON COLUMN solicitud_devolucion.fecha_termino_empleado IS 'Fecha en que el empleado termina su relación laboral';
COMMENT ON COLUMN solicitud_devolucion.fecha_devolucion_programada IS 'Fecha programada para la devolución de equipos';
COMMENT ON COLUMN solicitud_devolucion.fecha_devolucion_real IS 'Fecha real en que se devolvieron los equipos';

-- ============================================================================
-- TABLA DE DETALLE DE DEVOLUCIÓN
-- ============================================================================

-- Tabla de Detalle de Devoluciones (qué dispositivos se devolvieron)
CREATE TABLE detalle_devolucion (
    id SERIAL PRIMARY KEY,
    id_solicitud_devolucion INTEGER NOT NULL,
    id_dispositivo INTEGER NOT NULL,
    id_asignacion INTEGER NOT NULL,
    id_condicion_devolucion INTEGER NOT NULL,
    observaciones TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_detalle_solicitud FOREIGN KEY (id_solicitud_devolucion)
        REFERENCES solicitud_devolucion(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_dispositivo FOREIGN KEY (id_dispositivo)
        REFERENCES dispositivo(id) ON DELETE RESTRICT,
    CONSTRAINT fk_detalle_asignacion FOREIGN KEY (id_asignacion)
        REFERENCES asignacion_dispositivo(id) ON DELETE RESTRICT,
    CONSTRAINT fk_detalle_condicion FOREIGN KEY (id_condicion_devolucion)
        REFERENCES cat_condicion_devolucion(id) ON DELETE RESTRICT,

    -- Constraint: un dispositivo no puede aparecer dos veces en la misma solicitud
    CONSTRAINT uq_detalle_solicitud_dispositivo UNIQUE (id_solicitud_devolucion, id_dispositivo)
);

COMMENT ON TABLE detalle_devolucion IS 'Detalle de dispositivos devueltos en una solicitud de devolución';
COMMENT ON COLUMN detalle_devolucion.id_condicion_devolucion IS 'Condición física del dispositivo al momento de la devolución';

-- ============================================================================
-- TABLA DE HISTORIAL
-- ============================================================================

-- Tabla de Historial de Dispositivos (Auditoría)
CREATE TABLE historial_dispositivo (
    id SERIAL PRIMARY KEY,
    id_dispositivo INTEGER NOT NULL,
    id_tipo_movimiento INTEGER NOT NULL,
    id_usuario INTEGER NOT NULL,
    descripcion TEXT NOT NULL,
    datos_anteriores TEXT,
    datos_nuevos TEXT,
    fecha_movimiento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_historial_dispositivo FOREIGN KEY (id_dispositivo)
        REFERENCES dispositivo(id) ON DELETE CASCADE,
    CONSTRAINT fk_historial_tipo_movimiento FOREIGN KEY (id_tipo_movimiento)
        REFERENCES cat_tipo_movimiento(id) ON DELETE RESTRICT,
    CONSTRAINT fk_historial_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuario(id) ON DELETE RESTRICT
);

COMMENT ON TABLE historial_dispositivo IS 'Historial completo de movimientos y cambios de cada dispositivo';
COMMENT ON COLUMN historial_dispositivo.datos_anteriores IS 'Estado anterior del dispositivo (formato JSON)';
COMMENT ON COLUMN historial_dispositivo.datos_nuevos IS 'Estado nuevo del dispositivo (formato JSON)';

-- ============================================================================
-- TRIGGERS PARA ACTUALIZACIÓN AUTOMÁTICA
-- ============================================================================

-- Función para actualizar el campo ultima_actualizacion
CREATE OR REPLACE FUNCTION update_ultima_actualizacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.ultima_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers para actualización automática de ultima_actualizacion
CREATE TRIGGER trg_usuario_update
    BEFORE UPDATE ON usuario
    FOR EACH ROW
    EXECUTE FUNCTION update_ultima_actualizacion();

CREATE TRIGGER trg_empleado_update
    BEFORE UPDATE ON empleado
    FOR EACH ROW
    EXECUTE FUNCTION update_ultima_actualizacion();

CREATE TRIGGER trg_dispositivo_update
    BEFORE UPDATE ON dispositivo
    FOR EACH ROW
    EXECUTE FUNCTION update_ultima_actualizacion();

CREATE TRIGGER trg_asignacion_update
    BEFORE UPDATE ON asignacion_dispositivo
    FOR EACH ROW
    EXECUTE FUNCTION update_ultima_actualizacion();

CREATE TRIGGER trg_reemplazo_update
    BEFORE UPDATE ON reemplazo_dispositivo
    FOR EACH ROW
    EXECUTE FUNCTION update_ultima_actualizacion();

CREATE TRIGGER trg_solicitud_update
    BEFORE UPDATE ON solicitud_devolucion
    FOR EACH ROW
    EXECUTE FUNCTION update_ultima_actualizacion();

-- ============================================================================
-- FIN DEL SCRIPT V2
-- ============================================================================
