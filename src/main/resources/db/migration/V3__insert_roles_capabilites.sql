-- Insert roles
INSERT INTO roles (name)
VALUES
    ('ADMIN'),
    ('EMPLOYEE'),
    ('ANALYST');

-- Insert capabilities
INSERT INTO capabilities (name, description)
VALUES
    ('INSERT_ANALYST',   'Create a new analyst'),
    ('VIEW_ANALYSTS',    'View analyst list and details'),
    ('VIEW_ANALYST',     'View analyst'),
    ('EDIT_ANALYST',     'Modify existing analyst'),
    ('DELETE_ANALYST',   'Remove an analyst'),
    ('VIEW_ONLY_ANALYST','View only own analyst details');

-- Assign all capabilities to ADMIN
INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
CROSS JOIN capabilities c
WHERE r.name = 'ADMIN';

-- Assign VIEW_ANALYSTS and VIEW_ANALYST to EMPLOYEE
INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
CROSS JOIN capabilities c
WHERE r.name = 'EMPLOYEE'
  AND c.name IN ('VIEW_ANALYSTS', 'VIEW_ANALYST');

-- Assign VIEW_ONLY_ANALYST to ANALYST
INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
CROSS JOIN capabilities c
WHERE r.name = 'ANALYST'
  AND c.name IN ('VIEW_ONLY_ANALYST');