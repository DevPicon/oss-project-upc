-- ============================================================================
-- V4: Datos Iniciales
-- ============================================================================
-- Descripción: Inserta datos iniciales necesarios para el funcionamiento
--              del sistema (catálogos, usuario admin, etc.)
-- ============================================================================

-- ============================================================================
-- CATÁLOGO: ESTADOS DE EMPLEADO
-- ============================================================================

INSERT INTO cat_estado_empleado (codigo, nombre, descripcion, activo) VALUES
('ACTIVO', 'Activo', 'Empleado activo en la organización', true),
('INACTIVO', 'Inactivo', 'Empleado temporalmente inactivo', true),
('CESADO', 'Cesado', 'Empleado que ya no pertenece a la organización', true),
('VACACIONES', 'En Vacaciones', 'Empleado de vacaciones', true);

-- ============================================================================
-- CATÁLOGO: ÁREAS
-- ============================================================================

INSERT INTO cat_area (codigo, nombre, descripcion, id_area_superior, activo) VALUES
('TI', 'Tecnologías de Información', 'Área de tecnología e informática', NULL, true),
('RRHH', 'Recursos Humanos', 'Gestión de personal', NULL, true),
('FIN', 'Finanzas', 'Área financiera y contable', NULL, true),
('OPE', 'Operaciones', 'Operaciones y logística', NULL, true),
('MKT', 'Marketing', 'Marketing y comunicaciones', NULL, true);

-- Sub-áreas de TI
INSERT INTO cat_area (codigo, nombre, descripcion, id_area_superior, activo) VALUES
('TI-DEV', 'Desarrollo', 'Desarrollo de software', (SELECT id FROM cat_area WHERE codigo = 'TI'), true),
('TI-INF', 'Infraestructura', 'Infraestructura y redes', (SELECT id FROM cat_area WHERE codigo = 'TI'), true),
('TI-SUP', 'Soporte', 'Soporte técnico', (SELECT id FROM cat_area WHERE codigo = 'TI'), true);

-- ============================================================================
-- CATÁLOGO: SEDES
-- ============================================================================

INSERT INTO cat_sede (codigo, nombre, direccion, ciudad, pais, activo) VALUES
('SEDE-LIMA', 'Sede Lima', 'Av. Primavera 2390, Surco', 'Lima', 'Perú', true),
('SEDE-MIRAFLORES', 'Sede Miraflores', 'Av. Arequipa 2020', 'Lima', 'Perú', true),
('SEDE-CALLAO', 'Sede Callao', 'Av. Colonial 1500', 'Callao', 'Perú', true);

-- ============================================================================
-- CATÁLOGO: PUESTOS
-- ============================================================================

INSERT INTO cat_puesto (codigo, nombre, descripcion, id_area, activo) VALUES
('DEV-SR', 'Desarrollador Senior', 'Desarrollador senior', (SELECT id FROM cat_area WHERE codigo = 'TI-DEV'), true),
('DEV-JR', 'Desarrollador Junior', 'Desarrollador junior', (SELECT id FROM cat_area WHERE codigo = 'TI-DEV'), true),
('SYS-ADMIN', 'Administrador de Sistemas', 'Administrador de infraestructura', (SELECT id FROM cat_area WHERE codigo = 'TI-INF'), true),
('TECH-SUP', 'Técnico de Soporte', 'Soporte técnico', (SELECT id FROM cat_area WHERE codigo = 'TI-SUP'), true),
('RRHH-GEN', 'Analista de RRHH', 'Analista de recursos humanos', (SELECT id FROM cat_area WHERE codigo = 'RRHH'), true);

-- ============================================================================
-- CATÁLOGO: TIPOS DE DISPOSITIVO
-- ============================================================================

INSERT INTO cat_tipo_dispositivo (codigo, nombre, descripcion, requiere_serie, activo) VALUES
('LAPTOP', 'Laptop', 'Computadora portátil', true, true),
('DESKTOP', 'Desktop', 'Computadora de escritorio', true, true),
('MONITOR', 'Monitor', 'Monitor de pantalla', true, true),
('TECLADO', 'Teclado', 'Teclado', false, true),
('MOUSE', 'Mouse', 'Mouse', false, true),
('HEADSET', 'Audífonos', 'Audífonos con micrófono', false, true),
('DOCK', 'Docking Station', 'Estación de acoplamiento', true, true),
('TABLET', 'Tablet', 'Tableta', true, true);

-- ============================================================================
-- CATÁLOGO: MARCAS
-- ============================================================================

INSERT INTO cat_marca (codigo, nombre, activo) VALUES
('DELL', 'Dell', true),
('HP', 'HP', true),
('LENOVO', 'Lenovo', true),
('APPLE', 'Apple', true),
('SAMSUNG', 'Samsung', true),
('LG', 'LG', true),
('LOGITECH', 'Logitech', true),
('MICROSOFT', 'Microsoft', true);

-- ============================================================================
-- CATÁLOGO: ESTADOS DE DISPOSITIVO
-- ============================================================================

INSERT INTO cat_estado_dispositivo (codigo, nombre, descripcion, disponible_asignacion, activo) VALUES
('DISPONIBLE', 'Disponible', 'Dispositivo disponible para asignar', true, true),
('ASIGNADO', 'Asignado', 'Dispositivo asignado a un empleado', false, true),
('EN_REPARACION', 'En Reparación', 'Dispositivo en reparación', false, true),
('BAJA', 'Dado de Baja', 'Dispositivo dado de baja', false, true),
('EN_REVISION', 'En Revisión', 'Dispositivo en proceso de revisión', false, true),
('EXTRAVIADO', 'Extraviado', 'Dispositivo extraviado o perdido', false, true);

-- ============================================================================
-- CATÁLOGO: PROVEEDORES
-- ============================================================================

INSERT INTO cat_proveedor (codigo, razon_social, nombre_comercial, ruc, email, telefono, activo) VALUES
('PROV001', 'Tecnología Total S.A.C.', 'TecnoTotal', '20123456789', 'ventas@tecnototal.com', '01-2345678', true),
('PROV002', 'Distribuidora Digital S.A.', 'DigiDist', '20987654321', 'contacto@digidist.com', '01-8765432', true),
('PROV003', 'Importaciones Tech S.R.L.', 'ImportTech', '20456789123', 'info@importtech.com', '01-4567891', true);

-- ============================================================================
-- CATÁLOGO: ESTADOS DE ASIGNACIÓN
-- ============================================================================

INSERT INTO cat_estado_asignacion (codigo, nombre, descripcion, activo) VALUES
('ACTIVA', 'Activa', 'Asignación activa', true),
('DEVUELTA', 'Devuelta', 'Dispositivo devuelto', true),
('CANCELADA', 'Cancelada', 'Asignación cancelada', true);

-- ============================================================================
-- CATÁLOGO: MOTIVOS DE REEMPLAZO
-- ============================================================================

INSERT INTO cat_motivo_reemplazo (codigo, nombre, descripcion, activo) VALUES
('FALLA_HW', 'Falla de Hardware', 'El dispositivo presenta fallas de hardware', true),
('FALLA_SW', 'Falla de Software', 'Problemas de software irrecuperables', true),
('OBSOLETO', 'Obsolescencia', 'Dispositivo obsoleto', true),
('UPGRADE', 'Upgrade', 'Mejora de equipo', true),
('DANO', 'Daño Físico', 'Dispositivo con daño físico', true);

-- ============================================================================
-- CATÁLOGO: ESTADOS DE REEMPLAZO
-- ============================================================================

INSERT INTO cat_estado_reemplazo (codigo, nombre, descripcion, activo) VALUES
('PENDIENTE', 'Pendiente', 'Reemplazo pendiente de aprobación', true),
('APROBADO', 'Aprobado', 'Reemplazo aprobado', true),
('EN_PROCESO', 'En Proceso', 'Reemplazo en proceso de ejecución', true),
('COMPLETADO', 'Completado', 'Reemplazo completado exitosamente', true),
('RECHAZADO', 'Rechazado', 'Reemplazo rechazado', true);

-- ============================================================================
-- CATÁLOGO: ESTADOS DE SOLICITUD DE DEVOLUCIÓN
-- ============================================================================

INSERT INTO cat_estado_solicitud (codigo, nombre, descripcion, activo) VALUES
('PENDIENTE', 'Pendiente', 'Solicitud pendiente de devolución', true),
('EN_PROCESO', 'En Proceso', 'Devolución en proceso', true),
('COMPLETADA', 'Completada', 'Devolución completada', true),
('PARCIAL', 'Parcial', 'Devolución parcial', true),
('CANCELADA', 'Cancelada', 'Solicitud cancelada', true);

-- ============================================================================
-- CATÁLOGO: CONDICIONES DE DEVOLUCIÓN
-- ============================================================================

INSERT INTO cat_condicion_devolucion (codigo, nombre, descripcion, activo) VALUES
('EXCELENTE', 'Excelente', 'Dispositivo en excelente estado', true),
('BUENO', 'Bueno', 'Dispositivo en buen estado', true),
('REGULAR', 'Regular', 'Dispositivo con signos de uso', true),
('MALO', 'Malo', 'Dispositivo en mal estado', true),
('DANADO', 'Dañado', 'Dispositivo dañado', true);

-- ============================================================================
-- CATÁLOGO: TIPOS DE MOVIMIENTO (HISTORIAL)
-- ============================================================================

INSERT INTO cat_tipo_movimiento (codigo, nombre, descripcion, activo) VALUES
('ASIGNACION', 'Asignación', 'Asignación de dispositivo', true),
('DEVOLUCION', 'Devolución', 'Devolución de dispositivo', true),
('REEMPLAZO', 'Reemplazo', 'Reemplazo de dispositivo', true),
('BAJA', 'Baja', 'Baja de dispositivo', true),
('REPARACION', 'Reparación', 'Envío a reparación', true),
('ACTUALIZACION', 'Actualización', 'Actualización de datos', true);

-- ============================================================================
-- USUARIO ADMINISTRADOR INICIAL
-- ============================================================================

-- Contraseña: admin123 (hash BCrypt)
-- IMPORTANTE: Cambiar esta contraseña en producción
INSERT INTO usuario (username, password_hash, email, nombre_completo, activo) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye6R6jPQEqS7pqGJzKHXX0v5N5J8x5F6e',
 'admin@upc.edu.pe', 'Administrador del Sistema', true);
