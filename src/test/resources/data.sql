
INSERT INTO part_number (number, description, photo) VALUES
                                                         ('PN101', 'Description PN101', 'photo1.jpg'),
                                                         ('PN102', 'Description PN102', 'photo2.jpg');

INSERT INTO job (job_name) VALUES
                                   ('Job01'),
                                   ('Job02');

INSERT INTO equipment (part_number_id, serial_number, health_status, allocation_status, job_id, created_at,
                       allocation_status_last_modified, last_job, comments) VALUES
                            ('PN101', 'SN001', 'RITE', 'ON_LOCATION', 1, CURRENT_TIMESTAMP, CURRENT_DATE, 'Job01', 'No comments'),
                            ('PN102', 'SN002', 'RONG', 'ON_BASE', NULL, CURRENT_TIMESTAMP, CURRENT_DATE, 'Job02', 'Comment');

-- INSERT INTO certified_equipment (id, certification_date, certification_period, file_certificate, next_certification_date,
--                                  certification_status) VALUES
--                             (1, CURRENT_DATE, 12, 'certificate1.pdf', '2025-01-15', 'VALID'),
--                             (2, '2001-06-01', 24, 'certificate2.pdf', '2003-06-01', 'EXPIRED');
--
INSERT INTO app_user (id, username, password, role) VALUES
                                                        (1, 'admin', 'encrypted_password_1', 'ADMIN'),
                                                        (2, 'user', 'encrypted_password_2', 'USER');
