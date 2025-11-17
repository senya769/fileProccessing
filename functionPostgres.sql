CREATE OR REPLACE FUNCTION sum_and_median()
RETURNS TABLE(sum_int BIGINT, median_float DOUBLE PRECISION)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        SUM(int_value) AS sum_int,
        PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY float_value) AS median_float
    FROM records;
END;
$$;
SELECT * FROM sum_and_median();