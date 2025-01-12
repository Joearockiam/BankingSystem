CREATE TABLE IF NOT EXISTS public.account_test
(
    account_no character(5)NOT NULL,
    balance double precision,
    created_date timestamp(6) without time zone,
    updated_date timestamp(6) without time zone,
    CONSTRAINT account_pkey PRIMARY KEY (account_no)
)

CREATE TABLE IF NOT EXISTS public.account_txn
(
    id serial NOT NULL,
    amount double precision,
    balance double precision,
    txn_date char(8) NOT NULL,
    txn_id character(11) NULL,
    txn_type character(1) NOT NULL,
    updated_date timestamp(6) without time zone,
    account_no character(5)NOT NULL,
    CONSTRAINT account_txn_pkey PRIMARY KEY (id),
    CONSTRAINT fkey FOREIGN KEY (account_no) REFERENCES public.account (account_no)
)

CREATE TABLE IF NOT EXISTS public.eod_balance
(
    eod_date character(8) NOT NULL,
    account_no character(5) NOT NULL,
    balance numeric(10,2) NOT NULL,
    app_int_rule character varying(255) NOT NULL,
    app_int_rate numeric(5,2),
    earned_interest numeric(10,4),
    CONSTRAINT eod_balance_pkey PRIMARY KEY (eod_date, account_no)
)


CREATE TABLE IF NOT EXISTS public.rules
(
    rule_date character(8) NOT NULL,
    rule_id character varying(255)NOT NULL,
    rate double precision NOT NULL,
    CONSTRAINT rules_pkey PRIMARY KEY (rule_date)
)