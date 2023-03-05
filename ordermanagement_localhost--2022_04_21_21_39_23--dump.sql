--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4
-- Dumped by pg_dump version 13.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client (
    id integer NOT NULL,
    name character varying(45) NOT NULL,
    address character varying(45) NOT NULL,
    email character varying(45) NOT NULL,
    age integer NOT NULL
);


ALTER TABLE public.client OWNER TO postgres;

--
-- Name: client_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.client_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.client_id_seq OWNER TO postgres;

--
-- Name: client_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.client_id_seq OWNED BY public.client.id;


--
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    "clientID" integer NOT NULL,
    "productID" integer NOT NULL,
    quantity integer NOT NULL
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id integer NOT NULL,
    "foodName" character varying(20) NOT NULL,
    "currentStock" integer NOT NULL,
    price integer NOT NULL
);


ALTER TABLE public.product OWNER TO postgres;

--
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.product_id_seq OWNER TO postgres;

--
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.product_id_seq OWNED BY public.product.id;


--
-- Name: client id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client ALTER COLUMN id SET DEFAULT nextval('public.client_id_seq'::regclass);


--
-- Name: product id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product ALTER COLUMN id SET DEFAULT nextval('public.product_id_seq'::regclass);


--
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.client VALUES (1, 'Trasculescu Tudor', 'Mestecenilor 14', 'tudortrasculescu@gmail.com', 20);
INSERT INTO public.client VALUES (3, 'Pelle Andrei', 'Cipariu 12', 'andreipelle@gmail.com', 20);
INSERT INTO public.client VALUES (4, 'Fetean Flavius', 'Buna Ziua 15', 'ffetean@gmail.com', 20);
INSERT INTO public.client VALUES (5, 'Gligor Bogdan', 'Vladimirescu 5', 'gligorean@gmail.com', 19);
INSERT INTO public.client VALUES (6, 'Suciu Cezar', 'Buna ziua 2', 'suciucez@yahoo.com', 21);
INSERT INTO public.client VALUES (7, 'Marcus Cristian', 'Ceahlau 77', 'vmarcus@gmail.com', 21);
INSERT INTO public.client VALUES (2, 'Iaz Ania', 'Horia 10', 'aniaiaz@yahoo.com', 20);


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.orders VALUES (2, 7, 2);
INSERT INTO public.orders VALUES (2, 5, 1);


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.product VALUES (6, 'Hamburger Special', 20, 25);
INSERT INTO public.product VALUES (9, 'Lasagna Vegan', 12, 15);
INSERT INTO public.product VALUES (10, 'Lasagna Special', 6, 20);
INSERT INTO public.product VALUES (2, 'Pizza Carbonara', 14, 25);
INSERT INTO public.product VALUES (4, 'Pizza Prosciuto', 9, 23);
INSERT INTO public.product VALUES (3, 'Pizza Canibale', 6, 30);
INSERT INTO public.product VALUES (8, 'Caesar Salad', 30, 20);
INSERT INTO public.product VALUES (1, 'Pizza Capriciosa', 15, 25);
INSERT INTO public.product VALUES (7, 'French fries', 29, 6);
INSERT INTO public.product VALUES (5, 'Hamburger Vegan', 10, 18);


--
-- Name: client_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.client_id_seq', 15, true);


--
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_id_seq', 12, true);


--
-- Name: client client_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pk PRIMARY KEY (id);


--
-- Name: orders orders_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pk PRIMARY KEY ("clientID", "productID");


--
-- Name: product product_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pk PRIMARY KEY (id);


--
-- Name: client_address_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX client_address_uindex ON public.client USING btree (address);


--
-- Name: client_email_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX client_email_uindex ON public.client USING btree (email);


--
-- Name: client_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX client_id_uindex ON public.client USING btree (id);


--
-- Name: client_name_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX client_name_uindex ON public.client USING btree (name);


--
-- Name: product_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX product_id_uindex ON public.product USING btree (id);


--
-- Name: product_name_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX product_name_uindex ON public.product USING btree ("foodName");


--
-- PostgreSQL database dump complete
--

