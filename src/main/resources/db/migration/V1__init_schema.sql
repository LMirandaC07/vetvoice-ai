CREATE TABLE client (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(150)  NOT NULL,
    email       VARCHAR(150)  NOT NULL UNIQUE,
    phone       VARCHAR(30)
);

CREATE TABLE veterinarian (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(150)  NOT NULL,
    email       VARCHAR(150)  NOT NULL UNIQUE,
    specialty   VARCHAR(100)  NOT NULL
);

CREATE TABLE pet (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL,
    species     VARCHAR(50)   NOT NULL,
    breed       VARCHAR(80),
    client_id   BIGINT        NOT NULL REFERENCES client(id)
);

CREATE TABLE appointment (
    id                BIGSERIAL PRIMARY KEY,
    pet_id            BIGINT        NOT NULL REFERENCES pet(id),
    veterinarian_id   BIGINT        NOT NULL REFERENCES veterinarian(id),
    scheduled_at      TIMESTAMP     NOT NULL,
    status            VARCHAR(20)   NOT NULL,
    notes             VARCHAR(500),
    version           BIGINT        NOT NULL DEFAULT 0
);

-- FASE 2 (concorrÃªncia): impede que o MESMO veterinÃ¡rio tenha dois agendamentos
-- ativos no MESMO horÃ¡rio. Ã‰ um Ã­ndice Ãºnico PARCIAL: sÃ³ considera linhas cujo
-- status ainda nÃ£o Ã© CANCELLED. Isso empurra a regra de negÃ³cio pro banco,
-- que Ã© o Ãºnico lugar que consegue garantir isso de forma 100% segura sob
-- concorrÃªncia (duas requisiÃ§Ãµes simultÃ¢neas nÃ£o conseguem "passar" pela
-- validaÃ§Ã£o em Java ao mesmo tempo e ambas inserirem, porque o banco rejeita
-- a segunda inserÃ§Ã£o na hora do INSERT/COMMIT).
CREATE UNIQUE INDEX uq_appointment_vet_slot
    ON appointment (veterinarian_id, scheduled_at)
    WHERE status <> 'CANCELLED';

