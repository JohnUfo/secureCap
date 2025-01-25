SET client_encoding = 'UTF8';
SET timezone = 'US/Eastern';

DROP TABLE IF EXISTS public.Users CASCADE;

CREATE TABLE public.Users
(
    id           BIGSERIAL PRIMARY KEY,
    first_name   VARCHAR(50)  NOT NULL,
    last_name    VARCHAR(50)  NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    password     VARCHAR(255) DEFAULT NULL,
    address      VARCHAR(255) DEFAULT NULL,
    phone        VARCHAR(30)  DEFAULT NULL,
    title        VARCHAR(50)  DEFAULT NULL,
    bio          VARCHAR(255) DEFAULT NULL,
    enabled      BOOLEAN      DEFAULT FALSE,
    non_locked   BOOLEAN      DEFAULT TRUE,
    using_mfa    BOOLEAN      DEFAULT FALSE,
    created_date TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    image_url    VARCHAR(255) DEFAULT 'https://img.icons8.com/material/344/user-male-circle--v1.png'
);

DROP TABLE IF EXISTS public.Roles CASCADE;

CREATE TABLE public.Roles
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(50)  NOT NULL UNIQUE,
    permission VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS public.UserRoles CASCADE;

CREATE TABLE public.UserRoles
(
    id      SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public.Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES public.Roles (id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (user_id, role_id)
);

DROP TABLE IF EXISTS public.Events CASCADE;

CREATE TABLE public.Events
(
    id          BIGSERIAL PRIMARY KEY,
    type        VARCHAR(50)  NOT NULL CHECK (type IN ('LOGIN_ATTEMPT',
                                                      'LOGIN_ATTEMPT_FAILURE',
                                                      'LOGIN_ATTEMPT_SUCCESS',
                                                      'PROFILE_UPDATE',
                                                      'PROFILE_PICTURE_UPDATE',
                                                      'ROLE_UPDATE',
                                                      'ACCOUNT_SETTING_UPDATE',
                                                      'PASSWORD_UPDATE',
                                                      'MFA_UPDATE')),
    description VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS public.UserEvent CASCADE;

CREATE TABLE public.UserEvent
(
    id         SERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    event_id   BIGINT NOT NULL,
    device     VARCHAR(100) DEFAULT NULL,
    ip_address VARCHAR(100) DEFAULT NULL,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_event_user FOREIGN KEY (user_id) REFERENCES public.Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_user_event_event FOREIGN KEY (event_id) REFERENCES public.Events (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS public.AccountVerifications CASCADE;

CREATE TABLE public.AccountVerifications
(
    id      SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    url     VARCHAR(255) DEFAULT NULL,
    CONSTRAINT fk_account_verification_user FOREIGN KEY (user_id) REFERENCES public.Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (user_id, url)
);

DROP TABLE IF EXISTS public.ResetPasswordVerifications CASCADE;

CREATE TABLE public.ResetPasswordVerifications
(
    id              SERIAL PRIMARY KEY,
    user_id         BIGINT    NOT NULL,
    url             VARCHAR(255) DEFAULT NULL,
    expiration_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_ResetPasswordVerification_user FOREIGN KEY (user_id) REFERENCES public.Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (user_id, url)
);

DROP TABLE IF EXISTS public.TwoFactorVerifications CASCADE;

CREATE TABLE public.TwoFactorVerifications
(
    id              SERIAL PRIMARY KEY,
    user_id         BIGINT    NOT NULL UNIQUE,
    code            VARCHAR(100) DEFAULT NULL UNIQUE,
    expiration_date TIMESTAMP NOT NULL
);

INSERT INTO Roles (name, permission)
VALUES
    ('ROLE_USER', 'READ:USER,READ:CUSTOMER'),
    ('ROLE_MANAGER', 'READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
    ('ROLE_ADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
    ('ROLE_SYSADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER,DELETE:USER,DELETE:CUSTOMER');
