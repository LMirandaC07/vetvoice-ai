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

-- FASE 2 (concorrência): impede que o MESMO veterinário tenha dois agendamentos
-- ativos no MESMO horário. É um índice único PARCIAL: só considera linhas cujo
-- status ainda não é CANCELLED. Isso empurra a regra de negócio pro banco,
-- que é o único lugar que consegue garantir isso de forma 100% segura sob
-- concorrência (duas requisições simultâneas não conseguem "passar" pela
-- validação em Java ao mesmo tempo e ambas inserirem, porque o banco rejeita
-- a segunda inserção na hora do INSERT/COMMIT).
CREATE UNIQUE INDEX uq_appointment_vet_slot
    ON appointment (veterinarian_id, scheduled_at)
    WHERE status <> 'CANCELLED';
