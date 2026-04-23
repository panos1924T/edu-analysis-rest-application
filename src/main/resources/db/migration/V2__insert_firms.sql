INSERT INTO firms (id, name) VALUES
                                 (1,'Bank of America Merrill Lynch'),
                                 (2,'Barclays'),
                                 (3,'Citigroup'),
                                 (4,'Credit Suisse'),
                                 (5,'Deutsche Bank'),
                                 (6,'Goldman Sachs'),
                                 (7,'HSBC'),
                                 (8,'JPMorgan Chase'),
                                 (9,'Morgan Stanley'),
                                 (10,'Wells Fargo');

-- Advance the identity sequence past the explicitly inserted IDs
ALTER TABLE firms ALTER COLUMN id RESTART WITH 11;