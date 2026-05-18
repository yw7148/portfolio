DECLARE
    next_contact_id NUMBER;
BEGIN
    SELECT GREATEST(COALESCE(MAX(id), 0) + 1, 1)
    INTO next_contact_id
    FROM contact;

    EXECUTE IMMEDIATE
        'CREATE SEQUENCE contact_id_seq START WITH ' || next_contact_id || ' INCREMENT BY 1 NOCACHE';
END;
/
