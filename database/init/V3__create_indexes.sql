-- ============================================================================
-- V3: Creación de Índices
-- ============================================================================
-- Descripción: Índices para mejorar el rendimiento de consultas frecuentes
-- ============================================================================

-- ============================================================================
-- ÍNDICES PARA USUARIO
-- ============================================================================

CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_usuario_activo ON usuario(activo);

-- ============================================================================
-- ÍNDICES PARA EMPLEADO
-- ============================================================================

CREATE INDEX idx_empleado_codigo ON empleado(codigo_empleado);
CREATE INDEX idx_empleado_email ON empleado(email);
CREATE INDEX idx_empleado_area ON empleado(id_area);
CREATE INDEX idx_empleado_estado ON empleado(id_estado_empleado);

-- ============================================================================
-- ÍNDICES PARA DISPOSITIVO
-- ============================================================================

CREATE INDEX idx_dispositivo_codigo ON dispositivo(codigo_activo);
CREATE INDEX idx_dispositivo_serie ON dispositivo(numero_serie);
CREATE INDEX idx_dispositivo_tipo ON dispositivo(id_tipo_dispositivo);
CREATE INDEX idx_dispositivo_estado ON dispositivo(id_estado_dispositivo);

-- ============================================================================
-- ÍNDICES PARA ASIGNACION_DISPOSITIVO
-- ============================================================================

CREATE INDEX idx_asignacion_dispositivo ON asignacion_dispositivo(id_dispositivo);
CREATE INDEX idx_asignacion_empleado ON asignacion_dispositivo(id_empleado);
CREATE INDEX idx_asignacion_estado ON asignacion_dispositivo(id_estado_asignacion);
CREATE INDEX idx_asignacion_fecha ON asignacion_dispositivo(fecha_asignacion);

-- ============================================================================
-- ÍNDICES PARA REEMPLAZO_DISPOSITIVO
-- ============================================================================

CREATE INDEX idx_reemplazo_empleado ON reemplazo_dispositivo(id_empleado);
CREATE INDEX idx_reemplazo_dispositivo_orig ON reemplazo_dispositivo(id_dispositivo_original);
CREATE INDEX idx_reemplazo_estado ON reemplazo_dispositivo(id_estado_reemplazo);

-- ============================================================================
-- ÍNDICES PARA SOLICITUD_DEVOLUCION
-- ============================================================================

CREATE INDEX idx_solicitud_empleado ON solicitud_devolucion(id_empleado);
CREATE INDEX idx_solicitud_estado ON solicitud_devolucion(id_estado_solicitud);
CREATE INDEX idx_solicitud_fecha_prog ON solicitud_devolucion(fecha_devolucion_programada);

-- ============================================================================
-- ÍNDICES PARA DETALLE_DEVOLUCION
-- ============================================================================

CREATE INDEX idx_detalle_solicitud ON detalle_devolucion(id_solicitud_devolucion);
CREATE INDEX idx_detalle_dispositivo ON detalle_devolucion(id_dispositivo);

-- ============================================================================
-- ÍNDICES PARA HISTORIAL_DISPOSITIVO
-- ============================================================================

CREATE INDEX idx_historial_dispositivo ON historial_dispositivo(id_dispositivo);
CREATE INDEX idx_historial_fecha ON historial_dispositivo(fecha_movimiento);

-- ============================================================================
-- ÍNDICES PARA CATÁLOGOS (solo códigos)
-- ============================================================================

CREATE INDEX idx_cat_estado_empleado_codigo ON cat_estado_empleado(codigo);
CREATE INDEX idx_cat_area_codigo ON cat_area(codigo);
CREATE INDEX idx_cat_puesto_codigo ON cat_puesto(codigo);
CREATE INDEX idx_cat_sede_codigo ON cat_sede(codigo);
CREATE INDEX idx_cat_tipo_dispositivo_codigo ON cat_tipo_dispositivo(codigo);
CREATE INDEX idx_cat_marca_codigo ON cat_marca(codigo);
CREATE INDEX idx_cat_estado_dispositivo_codigo ON cat_estado_dispositivo(codigo);
CREATE INDEX idx_cat_proveedor_codigo ON cat_proveedor(codigo);
CREATE INDEX idx_cat_estado_asignacion_codigo ON cat_estado_asignacion(codigo);
CREATE INDEX idx_cat_motivo_reemplazo_codigo ON cat_motivo_reemplazo(codigo);
CREATE INDEX idx_cat_estado_reemplazo_codigo ON cat_estado_reemplazo(codigo);
CREATE INDEX idx_cat_estado_solicitud_codigo ON cat_estado_solicitud(codigo);
CREATE INDEX idx_cat_condicion_devolucion_codigo ON cat_condicion_devolucion(codigo);
CREATE INDEX idx_cat_tipo_movimiento_codigo ON cat_tipo_movimiento(codigo);
