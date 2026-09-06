# Pensionat RMJ – Microservices

Ett bokningssystem för ett pensionat, uppdelat i fristående microservices som
kommunicerar via REST. Varje tjänst har en egen databas och startas tillsammans
med **ett enda kommando** via Docker Compose.

Projektet är en vidareutveckling av en monolit (Backend 1) som här delats upp i
separata tjänster (Backend 2).

---

## Arkitektur

```
                         ┌──────────────────────┐
        HTTP (webbläsare) │                      │
   ─────────────────────► │   booking-service    │  :8080  (Thymeleaf-frontend + REST)
                          │   (rum & bokningar)  │
                          └───────┬──────────────┘
                                  │  ▲
       "finns kunden?"  REST      │  │  REST  "har kunden aktiva bokningar?"
                                  ▼  │
                          ┌───────┴──────────────┐
                          │  customer-service    │  :8081  (REST/JSON)
                          │   (kundhantering)    │
                          └──────────────────────┘

   booking-service ──► booking-db   (MySQL, databas: booking_system)
   customer-service ─► customer-db  (MySQL, databas: customer_system)
```

Tjänsterna läser **aldrig** i varandras databaser – all kommunikation sker över REST.

---

## Vad varje tjänst gör

### booking-service (port 8080)
Ansvarar för **rum och bokningar** och innehåller systemets **frontend** (Thymeleaf).
- Visar lediga rum och låter gäster boka.
- Sparar bokningar i sin egen databas. En bokning lagrar bara kundens **email**
  som referens – inte hela kundobjektet.
- Regler: ingen dubbelbokning av samma rum på överlappande datum.
- Verifierar vid varje ny bokning att kunden finns genom att fråga
  **customer-service** över REST.
- Exponerar ett endpoint som customer-service använder för att kolla om en kund
  har aktiva bokningar.

### customer-service (port 8081)
Ansvarar för **all kundhantering** och är ett rent REST-API (JSON).
- Registrera, hämta, ändra och ta bort kunder.
- Sparar kunder i sin egen databas.
- Innan en kund tas bort frågar tjänsten **booking-service** om kunden har
  aktiva bokningar. Har kunden det nekas raderingen.

---

## Hur tjänsterna pratar med varandra

All kommunikation sker via **HTTP/REST**. Adresserna kommer från miljövariabler,
så tjänsterna hittar varandra via sina namn i Dockers interna nätverk
(`http://booking-service:8080`, `http://customer-service:8081`).

**1. När en bokning skapas** – booking-service frågar customer-service:
```
booking-service  ──►  GET /api/customers/email/{email}  ──►  customer-service
```
- Kunden finns (200) → bokningen skapas (201).
- Kunden finns inte (404) → bokningen nekas (404).
- Kundtjänsten svarar inte → tydligt fel (503), appen kraschar inte.

**2. När en kund ska tas bort** – customer-service frågar booking-service:
```
customer-service ──►  GET /api/bookings/customer/{email}/active  ──►  booking-service
```
- Kunden har aktiva bokningar → raderingen nekas (409).
- Inga aktiva bokningar → kunden tas bort.

### Statuskoder
| Kod | Betydelse |
|-----|-----------|
| 200 | OK |
| 201 | Skapad (t.ex. ny bokning) |
| 400 | Felaktig inmatning |
| 404 | Hittas inte (t.ex. okänd kund) |
| 409 | Konflikt (dubbelbokning / kund med aktiva bokningar) |
| 503 | Den andra tjänsten är inte tillgänglig |

---

## Så startar du hela systemet

### Förutsättningar
- Docker Desktop installerat och igång.

### 1. Skapa en `.env`-fil i projektroten
Filen innehåller databasuppgifterna som Compose läser in:
```
DB_USERNAME=root
DB_PASSWORD=root
```

### 2. Starta allt med ett kommando
```bash
docker compose up --build
```
Det startar **fyra containrar**:
- `booking-service`  – bokningstjänsten (http://localhost:8080)
- `booking-db`       – MySQL för bokningar
- `customer-service` – kundtjänsten (http://localhost:8081)
- `customer-db`      – MySQL för kunder

Databaserna får en healthcheck så apparna väntar tills MySQL är redo – hela
systemet kommer alltså upp korrekt på första försöket.

### 3. Öppna appen
- Frontend: <http://localhost:8080>
- Kund-API: <http://localhost:8081/api/customers>

### Stänga av
```bash
docker compose down
```

---

## API-översikt

### booking-service (`:8080`)
| Metod | URL | Beskrivning |
|-------|-----|-------------|
| GET | `/api/bookings` | Alla bokningar |
| POST | `/api/bookings` | Skapa bokning |
| PUT | `/api/bookings/{id}` | Ändra bokning |
| DELETE | `/api/bookings/{id}` | Avboka |
| GET | `/api/bookings/customer/{email}` | En kunds bokningar |
| GET | `/api/bookings/customer/{email}/active` | Har kunden aktiva bokningar? |

### customer-service (`:8081`)
| Metod | URL | Beskrivning |
|-------|-----|-------------|
| GET | `/api/customers` | Alla kunder |
| GET | `/api/customers/{id}` | Hämta kund via id |
| GET | `/api/customers/email/{email}` | Hämta kund via email |
| POST | `/api/customers` | Registrera kund |
| PUT | `/api/customers/{id}` | Ändra kund |
| DELETE | `/api/customers/{id}` | Ta bort kund |

---

## Teknik
- Java 17, Spring Boot
- Spring Data JPA + MySQL 8 (en databas per tjänst)
- Thymeleaf (frontend i booking-service)
- Docker & Docker Compose
- Integrationstester: JUnit 5, MockMvc, H2 (in-memory), Mockito

---

## Tester
Kör bokningstjänstens tester (inklusive integrationstester mot en in-memory-databas):
```bash
cd booking-system
./mvnw test
```
