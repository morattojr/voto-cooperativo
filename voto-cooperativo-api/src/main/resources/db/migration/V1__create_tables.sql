-- Create initial tables for Agenda and Vote

CREATE TABLE agenda (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    voting_session_start TIMESTAMP,
    voting_session_end TIMESTAMP,
    voting_session_open BOOLEAN DEFAULT FALSE
);

CREATE TABLE vote (
    id BIGSERIAL PRIMARY KEY,
    agenda_id BIGINT NOT NULL,
    user_id VARCHAR(11) NOT NULL, -- Assuming CPF as user ID
    vote_value VARCHAR(3) NOT NULL, -- 'YES' or 'NO'
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_agenda FOREIGN KEY (agenda_id) REFERENCES agenda(id),
    CONSTRAINT uk_agenda_user UNIQUE (agenda_id, user_id) -- Ensure one vote per user per agenda
);