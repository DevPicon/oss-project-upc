-- ============================================================================
-- V6: Insert Test Data
-- ============================================================================
-- Description: Inserts dummy data for Users, Employees, Devices, and Assignments
--              to facilitate backend testing.
-- ============================================================================

-- 1. USUARIOS ADICIONALES
-- Password hash is 'admin123' ($2a$10$N9qo8uLOickgx2ZMRZoMye6R6jPQEqS7pqGJzKHXX0v5N5J8x5F6e)
INSERT INTO usuario (username, password_hash, email, nombre_completo, activo) VALUES
('soporte1', '$2a$10$N9qo8uLOickgx2ZMRZoMye6R6jPQEqS7pqGJzKHXX0v5N5J8x5F6e', 'soporte1@upc.edu.pe', 'Juan Soporte', true),
('rrhh_user', '$2a$10$N9qo8uLOickgx2ZMRZoMye6R6jPQEqS7pqGJzKHXX0v5N5J8x5F6e', 'rrhh@upc.edu.pe', 'Maria RRHH', true),
('logistica', '$2a$10$N9qo8uLOickgx2ZMRZoMye6R6jPQEqS7pqGJzKHXX0v5N5J8x5F6e', 'logistica@upc.edu.pe', 'Pedro Logistica', true),
('auditor', '$2a$10$N9qo8uLOickgx2ZMRZoMye6R6jPQEqS7pqGJzKHXX0v5N5J8x5F6e', 'auditor@upc.edu.pe', 'Ana Auditoria', true),
('gerente', '$2a$10$N9qo8uLOickgx2ZMRZoMye6R6jPQEqS7pqGJzKHXX0v5N5J8x5F6e', 'gerente@upc.edu.pe', 'Carlos Gerencia', true);

-- 2. EMPLEADOS
-- Helper CTE or subqueries used inline for IDs to ensure portability across different ID generations

INSERT INTO empleado (codigo_empleado, nombre, apellido_paterno, apellido_materno, email, telefono, id_area, id_puesto, id_sede, fecha_ingreso, id_estado_empleado)
VALUES
-- TI Developers
('EMP-001', 'Ricardo', 'Palma', 'Soriano', 'ricardo.palma@upc.edu.pe', '991122334', 
    (SELECT id FROM cat_area WHERE codigo='TI-DEV'), 
    (SELECT id FROM cat_puesto WHERE codigo='DEV-SR'), 
    (SELECT id FROM cat_sede WHERE codigo='SEDE-LIMA'), '2022-01-15', 1),
('EMP-002', 'Sofia', 'Mulanovich', 'Aljovin', 'sofia.m@upc.edu.pe', '991122335', 
    (SELECT id FROM cat_area WHERE codigo='TI-DEV'), 
    (SELECT id FROM cat_puesto WHERE codigo='DEV-JR'), 
    (SELECT id FROM cat_sede WHERE codigo='SEDE-LIMA'), '2023-05-10', 1),
-- Infraestructura
('EMP-003', 'Gaston', 'Acurio', 'Jaramillo', 'gaston.acurio@upc.edu.pe', '991122336', 
    (SELECT id FROM cat_area WHERE codigo='TI-INF'), 
    (SELECT id FROM cat_puesto WHERE codigo='SYS-ADMIN'), 
    (SELECT id FROM cat_sede WHERE codigo='SEDE-MIRAFLORES'), '2021-03-20', 1),
-- Soporte
('EMP-004', 'Paolo', 'Guerrero', 'Gonzales', 'paolo.guerrero@upc.edu.pe', '991122337', 
    (SELECT id FROM cat_area WHERE codigo='TI-SUP'), 
    (SELECT id FROM cat_puesto WHERE codigo='TECH-SUP'), 
    (SELECT id FROM cat_sede WHERE codigo='SEDE-CALLAO'), '2023-01-05', 1),
('EMP-005', 'Jefferson', 'Farfan', 'Guadalupe', 'jefferson.farfan@upc.edu.pe', '991122338', 
    (SELECT id FROM cat_area WHERE codigo='TI-SUP'), 
    (SELECT id FROM cat_puesto WHERE codigo='TECH-SUP'), 
    (SELECT id FROM cat_sede WHERE codigo='SEDE-LIMA'), '2023-02-15', 1),
-- RRHH
('EMP-006', 'Susana', 'Baca', 'De la Colina', 'susana.baca@upc.edu.pe', '991122339', 
    (SELECT id FROM cat_area WHERE codigo='RRHH'), 
    (SELECT id FROM cat_puesto WHERE codigo='RRHH-GEN'), 
    (SELECT id FROM cat_sede WHERE codigo='SEDE-MIRAFLORES'), '2020-08-15', 1),
-- Marketing
('EMP-007', 'Gian', 'Marco', 'Zignago', 'gianmarco@upc.edu.pe', '991122340', 
    (SELECT id FROM cat_area WHERE codigo='MKT'), 
    (SELECT id FROM cat_puesto WHERE codigo='RRHH-GEN'), -- Asumimos puesto generico por ahora
    (SELECT id FROM cat_sede WHERE codigo='SEDE-LIMA'), '2022-11-01', 1),
('EMP-008', 'Eva', 'Ayllon', 'Urbina', 'eva.ayllon@upc.edu.pe', '991122341', 
    (SELECT id FROM cat_area WHERE codigo='MKT'), 
    (SELECT id FROM cat_puesto WHERE codigo='RRHH-GEN'), 
    (SELECT id FROM cat_sede WHERE codigo='SEDE-LIMA'), '2021-06-30', 1),
-- Operaciones
('EMP-009', 'Claudio', 'Pizarro', 'Bossio', 'claudio.pizarro@upc.edu.pe', '991122342', 
    (SELECT id FROM cat_area WHERE codigo='OPE'), 
    (SELECT id FROM cat_puesto WHERE codigo='RRHH-GEN'), 
    (SELECT id FROM cat_sede WHERE codigo='SEDE-CALLAO'), '2019-01-10', 1),
('EMP-010', 'Juan', 'Diego', 'Florez', 'juan.florez@upc.edu.pe', '991122343', 
    (SELECT id FROM cat_area WHERE codigo='OPE'), 
    (SELECT id FROM cat_puesto WHERE codigo='RRHH-GEN'), 
    (SELECT id FROM cat_sede WHERE codigo='SEDE-CALLAO'), '2018-05-20', 1);


-- 3. DISPOSITIVOS
INSERT INTO dispositivo (codigo_activo, numero_serie, id_tipo_dispositivo, id_marca, modelo, id_estado_dispositivo, fecha_adquisicion, valor_adquisicion, id_proveedor, observaciones)
VALUES
-- Laptops
('ACT-1001', 'SN-DELL-1001', (SELECT id FROM cat_tipo_dispositivo WHERE codigo='LAPTOP'), (SELECT id FROM cat_marca WHERE codigo='DELL'), 'Latitude 7420', (SELECT id FROM cat_estado_dispositivo WHERE codigo='ASIGNADO'), '2023-01-10', 1500.00, 1, 'Laptop principal Dev'),
('ACT-1002', 'SN-DELL-1002', (SELECT id FROM cat_tipo_dispositivo WHERE codigo='LAPTOP'), (SELECT id FROM cat_marca WHERE codigo='DELL'), 'Latitude 5420', (SELECT id FROM cat_estado_dispositivo WHERE codigo='DISPONIBLE'), '2023-01-10', 1200.00, 1, 'Stock'),
('ACT-1003', 'SN-HP-1001', (SELECT id FROM cat_tipo_dispositivo WHERE codigo='LAPTOP'), (SELECT id FROM cat_marca WHERE codigo='HP'), 'EliteBook 840 G8', (SELECT id FROM cat_estado_dispositivo WHERE codigo='ASIGNADO'), '2023-02-15', 1400.00, 2, 'Laptop RRHH'),
('ACT-1004', 'SN-LEN-1001', (SELECT id FROM cat_tipo_dispositivo WHERE codigo='LAPTOP'), (SELECT id FROM cat_marca WHERE codigo='LENOVO'), 'ThinkPad T14', (SELECT id FROM cat_estado_dispositivo WHERE codigo='EN_REPARACION'), '2022-11-20', 1350.00, 3, 'Pantalla dañada'),
('ACT-1005', 'SN-APP-1001', (SELECT id FROM cat_tipo_dispositivo WHERE codigo='LAPTOP'), (SELECT id FROM cat_marca WHERE codigo='APPLE'), 'MacBook Pro M1', (SELECT id FROM cat_estado_dispositivo WHERE codigo='ASIGNADO'), '2023-03-01', 2500.00, 2, 'Diseño Marketing'),

-- Desktops
('ACT-2001', 'SN-DELL-2001', (SELECT id FROM cat_tipo_dispositivo WHERE codigo='DESKTOP'), (SELECT id FROM cat_marca WHERE codigo='DELL'), 'OptiPlex 7090', (SELECT id FROM cat_estado_dispositivo WHERE codigo='DISPONIBLE'), '2022-06-15', 900.00, 1, 'PC Escritorio Stock'),
('ACT-2002', 'SN-HP-2001', (SELECT id FROM cat_tipo_dispositivo WHERE codigo='DESKTOP'), (SELECT id FROM cat_marca WHERE codigo='HP'), 'ProDesk 600', (SELECT id FROM cat_estado_dispositivo WHERE codigo='ASIGNADO'), '2022-07-20', 850.00, 2, 'PC Recepción'),

-- Monitores
('ACT-3001', 'SN-LG-3001', (SELECT id FROM cat_tipo_dispositivo WHERE codigo='MONITOR'), (SELECT id FROM cat_marca WHERE codigo='LG'), '24MK600', (SELECT id FROM cat_estado_dispositivo WHERE codigo='ASIGNADO'), '2023-01-10', 180.00, 3, 'Monitor Dev 1'),
('ACT-3002', 'SN-LG-3002', (SELECT id FROM cat_tipo_dispositivo WHERE codigo='MONITOR'), (SELECT id FROM cat_marca WHERE codigo='LG'), '24MK600', (SELECT id FROM cat_estado_dispositivo WHERE codigo='ASIGNADO'), '2023-01-10', 180.00, 3, 'Monitor Dev 2'),
('ACT-3003', 'SN-SAM-3001', (SELECT id FROM cat_tipo_dispositivo WHERE codigo='MONITOR'), (SELECT id FROM cat_marca WHERE codigo='SAMSUNG'), 'F24T35', (SELECT id FROM cat_estado_dispositivo WHERE codigo='DISPONIBLE'), '2023-02-01', 170.00, 3, 'Stock Monitor'),
('ACT-3004', 'SN-DELL-3001', (SELECT id FROM cat_tipo_dispositivo WHERE codigo='MONITOR'), (SELECT id FROM cat_marca WHERE codigo='DELL'), 'P2419H', (SELECT id FROM cat_estado_dispositivo WHERE codigo='ASIGNADO'), '2023-03-05', 220.00, 1, 'Monitor Marketing'),

-- Perifericos
('ACT-4001', NULL, (SELECT id FROM cat_tipo_dispositivo WHERE codigo='TECLADO'), (SELECT id FROM cat_marca WHERE codigo='LOGITECH'), 'K120', (SELECT id FROM cat_estado_dispositivo WHERE codigo='ASIGNADO'), '2023-01-10', 15.00, 3, 'Teclado Std'),
('ACT-4002', NULL, (SELECT id FROM cat_tipo_dispositivo WHERE codigo='MOUSE'), (SELECT id FROM cat_marca WHERE codigo='LOGITECH'), 'M100', (SELECT id FROM cat_estado_dispositivo WHERE codigo='ASIGNADO'), '2023-01-10', 10.00, 3, 'Mouse Std'),
('ACT-4003', NULL, (SELECT id FROM cat_tipo_dispositivo WHERE codigo='HEADSET'), (SELECT id FROM cat_marca WHERE codigo='LOGITECH'), 'H390', (SELECT id FROM cat_estado_dispositivo WHERE codigo='DISPONIBLE'), '2023-01-10', 40.00, 3, 'Headset USB'),

-- Tablets
('ACT-5001', 'SN-SAM-5001', (SELECT id FROM cat_tipo_dispositivo WHERE codigo='TABLET'), (SELECT id FROM cat_marca WHERE codigo='SAMSUNG'), 'Galaxy Tab S7', (SELECT id FROM cat_estado_dispositivo WHERE codigo='ASIGNADO'), '2023-04-10', 600.00, 3, 'Tablet Operaciones');


-- 4. ASIGNACIONES (Vincular empleados con dispositivos)
-- EMP-001 (Ricardo Palma) tiene Laptop DELL y Monitor LG
INSERT INTO asignacion_dispositivo (id_dispositivo, id_empleado, fecha_asignacion, id_usuario_asigna, id_estado_asignacion, observaciones_asignacion)
VALUES
((SELECT id FROM dispositivo WHERE codigo_activo='ACT-1001'), (SELECT id FROM empleado WHERE codigo_empleado='EMP-001'), '2023-01-20', (SELECT id FROM usuario WHERE username='admin'), (SELECT id FROM cat_estado_asignacion WHERE codigo='ACTIVA'), 'Entrega inicial equipo completo'),
((SELECT id FROM dispositivo WHERE codigo_activo='ACT-3001'), (SELECT id FROM empleado WHERE codigo_empleado='EMP-001'), '2023-01-20', (SELECT id FROM usuario WHERE username='admin'), (SELECT id FROM cat_estado_asignacion WHERE codigo='ACTIVA'), 'Entrega inicial monitor');

-- EMP-006 (Susana Baca - RRHH) tiene Laptop HP
INSERT INTO asignacion_dispositivo (id_dispositivo, id_empleado, fecha_asignacion, id_usuario_asigna, id_estado_asignacion, observaciones_asignacion)
VALUES
((SELECT id FROM dispositivo WHERE codigo_activo='ACT-1003'), (SELECT id FROM empleado WHERE codigo_empleado='EMP-006'), '2023-02-20', (SELECT id FROM usuario WHERE username='soporte1'), (SELECT id FROM cat_estado_asignacion WHERE codigo='ACTIVA'), 'Renovacion equipo');

-- EMP-007 (Gian Marco - MKT) tiene MacBook y Monitor Dell
INSERT INTO asignacion_dispositivo (id_dispositivo, id_empleado, fecha_asignacion, id_usuario_asigna, id_estado_asignacion, observaciones_asignacion)
VALUES
((SELECT id FROM dispositivo WHERE codigo_activo='ACT-1005'), (SELECT id FROM empleado WHERE codigo_empleado='EMP-007'), '2023-03-05', (SELECT id FROM usuario WHERE username='soporte1'), (SELECT id FROM cat_estado_asignacion WHERE codigo='ACTIVA'), 'Equipo diseño'),
((SELECT id FROM dispositivo WHERE codigo_activo='ACT-3004'), (SELECT id FROM empleado WHERE codigo_empleado='EMP-007'), '2023-03-05', (SELECT id FROM usuario WHERE username='soporte1'), (SELECT id FROM cat_estado_asignacion WHERE codigo='ACTIVA'), 'Monitor diseño');

-- EMP-009 (Claudio Pizarro - OPE) tiene Tablet
INSERT INTO asignacion_dispositivo (id_dispositivo, id_empleado, fecha_asignacion, id_usuario_asigna, id_estado_asignacion, observaciones_asignacion)
VALUES
((SELECT id FROM dispositivo WHERE codigo_activo='ACT-5001'), (SELECT id FROM empleado WHERE codigo_empleado='EMP-009'), '2023-04-15', (SELECT id FROM usuario WHERE username='admin'), (SELECT id FROM cat_estado_asignacion WHERE codigo='ACTIVA'), 'Uso en campo');

-- Asignacion Pasada (Devuelta)
-- EMP-002 tuvo un teclado pero lo devolvió
INSERT INTO asignacion_dispositivo (id_dispositivo, id_empleado, fecha_asignacion, fecha_devolucion, id_usuario_asigna, id_usuario_recibe, id_estado_asignacion, observaciones_asignacion, observaciones_devolucion)
VALUES
((SELECT id FROM dispositivo WHERE codigo_activo='ACT-4001'), (SELECT id FROM empleado WHERE codigo_empleado='EMP-002'), '2023-05-11', '2023-05-15', (SELECT id FROM usuario WHERE username='soporte1'), (SELECT id FROM usuario WHERE username='soporte1'), (SELECT id FROM cat_estado_asignacion WHERE codigo='DEVUELTA'), 'Prestamo temporal', 'Devolucion ok');

