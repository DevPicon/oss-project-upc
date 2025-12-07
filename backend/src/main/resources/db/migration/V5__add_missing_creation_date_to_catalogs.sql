-- ============================================================================
-- V5: Add missing fecha_creacion column to catalog tables
-- ============================================================================
-- Description: Adds fecha_creacion column to catalog tables that were missing it
--              but define it in their BaseEntity.
-- Author: Antigravity
-- Date: 2025-12-07
-- ============================================================================

-- Add fecha_creacion to cat_estado_dispositivo
ALTER TABLE cat_estado_dispositivo 
ADD COLUMN fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add fecha_creacion to cat_estado_asignacion
ALTER TABLE cat_estado_asignacion 
ADD COLUMN fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add fecha_creacion to cat_motivo_reemplazo
ALTER TABLE cat_motivo_reemplazo 
ADD COLUMN fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add fecha_creacion to cat_estado_reemplazo
ALTER TABLE cat_estado_reemplazo 
ADD COLUMN fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add fecha_creacion to cat_estado_solicitud
ALTER TABLE cat_estado_solicitud 
ADD COLUMN fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add fecha_creacion to cat_condicion_devolucion
ALTER TABLE cat_condicion_devolucion 
ADD COLUMN fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add fecha_creacion to cat_tipo_movimiento
ALTER TABLE cat_tipo_movimiento 
ADD COLUMN fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
