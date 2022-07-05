INSERT INTO transaction (id, type, date, account_number, currency, amount, merchant_name, merchant_logo)
VALUES (1, "CHEQUE", now(), '1234567', 'USD', 100.00, 'Acme Ltd.', 'images/acme.png'),
 (2, "CRYPTO", now(), '1234567', 'USD', 5.00, 'Caffeinenation Inc', 'images/caffeinenation.png');