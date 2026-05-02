CREATE TABLE author_roles
(
    author_id UUID NOT NULL,
    role_id   UUID NOT NULL,
    CONSTRAINT pk_author_roles PRIMARY KEY (author_id, role_id)
);

CREATE TABLE authors
(
    id                         UUID    NOT NULL,
    name                       VARCHAR(255),
    last_name                  VARCHAR(255),
    email                      VARCHAR(255),
    password                   VARCHAR(255),
    username                   VARCHAR(255),
    is_enabled                 BOOLEAN NOT NULL,
    is_account_non_expired     BOOLEAN NOT NULL,
    is_account_non_locked      BOOLEAN NOT NULL,
    is_credentials_non_expired BOOLEAN NOT NULL,
    CONSTRAINT pk_authors PRIMARY KEY (id)
);

CREATE TABLE permissions
(
    id   UUID         NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_permissions PRIMARY KEY (id)
);

CREATE TABLE role_permissions
(
    permission_id UUID NOT NULL,
    role_id       UUID NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (permission_id, role_id)
);

CREATE TABLE roles
(
    id   UUID NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE tasks
(
    id             UUID NOT NULL,
    title          VARCHAR(255),
    description    VARCHAR(255),
    create_ad      date,
    create_ad_time time WITHOUT TIME ZONE,
    update_ad      date,
    update_ad_time time WITHOUT TIME ZONE,
    status         VARCHAR(255),
    author_id      UUID,
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);

ALTER TABLE authors
    ADD CONSTRAINT uc_authors_username UNIQUE (username);

ALTER TABLE tasks
    ADD CONSTRAINT FK_TASKS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES authors (id);

ALTER TABLE author_roles
    ADD CONSTRAINT fk_autrol_on_role_entity FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE author_roles
    ADD CONSTRAINT fk_autrol_on_user_entity FOREIGN KEY (author_id) REFERENCES authors (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission_entity FOREIGN KEY (permission_id) REFERENCES permissions (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role_entity FOREIGN KEY (role_id) REFERENCES roles (id);