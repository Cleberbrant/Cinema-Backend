ALTER TABLE filme
ALTER COLUMN em_cartaz TYPE boolean
USING CASE
    WHEN em_cartaz = 'true' THEN true
    WHEN em_cartaz = 'yes' THEN true
    WHEN em_cartaz = '1' THEN true
    ELSE false
END;
