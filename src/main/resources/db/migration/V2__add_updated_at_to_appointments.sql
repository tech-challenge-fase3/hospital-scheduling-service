UPDATE appointments
SET updated_at = created_at
WHERE updated_at IS NULL;

ALTER TABLE appointments
    ALTER COLUMN updated_at SET NOT NULL;