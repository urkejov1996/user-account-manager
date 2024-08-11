# User Account Manager

## Opis Projekta

User Account Manager je REST API servis razvijen u okviru Spring Boot aplikacije. Ovaj servis omogućava CRUD (Create, Read, Update, Delete) operacije nad podacima o korisnicima i njihovim tekućim računima. Podaci se čuvaju u Microsoft SQL Server bazi podataka koristeći Hibernate kao ORM alat za komunikaciju sa bazom.

## Tehnologije

- **JDK 17+**
- **Spring Boot**
- **Hibernate**
- **Microsoft SQL Server**
- **Maven**

## Instalacija i Pokretanje

### 1. Priprema Baze Podataka

Pre nego što pokrenete aplikaciju, morate kreirati bazu podataka i tabele koristeći SQL skriptu priloženu u projektu.

- Otvorite Microsoft SQL Server Management Studio (SSMS) ili sličan SQL alat.
- Otvorite `create_tables.sql` fajl i pokrenite skriptu da biste kreirali potrebne tabele u bazi podataka.

### 2. Konfiguracija

- Konfigurišite aplikaciju tako da se poveže sa vašom SQL Server bazom podataka. Prilagodite `application.properties` fajl prema vašoj konfiguraciji:

```properties
spring.application.name=user-account-manager

spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=user_account_management;
spring.datasource.username=username
spring.datasource.password=password
spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

- Zamenite your_username i your_password sa vašim korisničkim imenom i lozinkom za SQL Server.

### 3. Pokretanje Aplikacije

- Preuzmite ili klonirajte projekat.
- Otvorite terminal ili komandnu liniju i idite u direktorijum projekta.
- Pokrenite sledeću komandu da biste instalirali zavisnosti i pokrenuli aplikaciju:

mvn clean install
mvn spring-boot:run

### 4. Testiranje API-ja

- Aplikacija je sada dostupna na http://localhost:8080. Možete koristiti alat kao što je Postman ili Insomnia za testiranje API endpoint-a. Evo nekoliko primera:

- Kreiranje korisnika:
POST http://localhost:8080/api/v1/users
Body: {"firstName": "Uroš", "lastName": "Jovanović", "username": "urkejov", "email": "urkejov1996@gmail.com", "phoneNumber": 0600250626, "address": "Knjazevačka 128/20"}

- Pregled korisnika:
GET http://localhost:8080/api/v1/users/{userId}

- Ažuriranje korisnika:
PUT http://localhost:8080/api/v1/users/{userId}
Body: {"firstName": "Uroš", "lastName": "Jovanović", "username": "urkejov", "email": "urkejov1996@gmail.com", "phoneNumber": 0600250626, "address": "Knjazevačka 128/20"}

- Brisanje korisnika:
DELETE http://localhost:8080/api/v1/users/{userId}

- Kreiranje tekućeg računa:
POST http://localhost:8080/api/v1/accounts
Body: {"userId": "{userId}", "balance": 1000.00}

- Pregled tekućeg računa za korisnika:
  GET http://localhost:8080/api/v1/accounts/{userId}/{accountId}

- Pregled svih tekućih računa za korisnika:
  GET http://localhost:8080/api/v1/accounts/{userId}

Za više informacija o API-ju i njegovim endpoint-ima, pogledajte dokumentaciju u kodu i komentare u kontrolerima i servisima.

- Kontakt:
Ako imate bilo kakvih pitanja ili problema, slobodno se obratite na email: urkejov1996@gmail.com.
