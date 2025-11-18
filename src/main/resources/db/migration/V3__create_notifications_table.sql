-- ============================================================================
-- V2: Crear tabla de notificaciones
-- ============================================================================

-- Tabla: notifications
-- Almacena todas las notificaciones del sistema
-- Cada notificación pertenece a un usuario (user_email)
-- y tiene un canal (EMAIL, SMS, PUSH) y un status (PENDING, SENDING, SENT, FAILED)

CREATE TABLE IF NOT EXISTS notifications (
    -- ID autoincremental
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Título de la notificación (max 255 caracteres)
                                             title VARCHAR(255) NOT NULL,

    -- Contenido de la notificación (texto largo)
    content TEXT NOT NULL,

    -- Canal de envío (EMAIL, SMS, PUSH)
    -- Guardado como String (VARCHAR) por @Enumerated(EnumType.STRING)
    channel VARCHAR(20) NOT NULL,

    -- Estado de la notificación (PENDING, SENDING, SENT, FAILED)
    -- Guardado como String (VARCHAR) por @Enumerated(EnumType.STRING)
    status VARCHAR(20) NOT NULL,

    -- Email del usuario dueño de la notificación
    -- Relación lógica con users.email (no es FK para simplificar)
    user_email VARCHAR(100) NOT NULL,

    -- Timestamp de creación (automático en la entidad)
    created_at TIMESTAMP,

    -- Timestamp de envío (null si aún no se envió)
    sent_at TIMESTAMP,

    -- Índices para mejorar performance
    -- Índice en user_email porque filtramos mucho por este campo
    INDEX idx_user_email (user_email),

    -- Índice en status para queries de monitoreo (opcional)
    INDEX idx_status (status),

    -- Índice compuesto para findByIdAndUserEmail (más eficiente)
    INDEX idx_id_user_email (id, user_email)

    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentarios de las columnas (opcional, para documentación)
ALTER TABLE notifications
    MODIFY COLUMN title VARCHAR(255) NOT NULL COMMENT 'Notification title',
    MODIFY COLUMN content TEXT NOT NULL COMMENT 'Notification body/content',
    MODIFY COLUMN channel VARCHAR(20) NOT NULL COMMENT 'Delivery channel: EMAIL, SMS, or PUSH',
    MODIFY COLUMN status VARCHAR(20) NOT NULL COMMENT 'Status: PENDING, SENDING, SENT, or FAILED',
    MODIFY COLUMN user_email VARCHAR(100) NOT NULL COMMENT 'Owner email (from users table)',
    MODIFY COLUMN created_at TIMESTAMP COMMENT 'Creation timestamp',
    MODIFY COLUMN sent_at TIMESTAMP COMMENT 'Sent timestamp (null if not sent yet)';