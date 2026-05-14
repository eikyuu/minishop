# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4.0.6 multi-module Maven project (Java 26, Maven wrapper checked in). The root `pom.xml` is an aggregator (`packaging=pom`) and orchestrates two autonomous microservices:

- `product-service` — gestion des produits (port **8081**, base H2 `productdb`)
- `order-service` — gestion des commandes (port **8082**, base H2 `orderdb`)

Chaque service a sa propre classe `@SpringBootApplication`, sa propre BDD et son cycle de vie indépendant.

## Commands

Toutes les commandes se lancent depuis la racine du projet.

### Build

```bash
# Compiler tous les modules (sans tests)
./mvnw clean install -DskipTests

# Compiler avec exécution des tests
./mvnw clean install

# Compiler un seul module (et ses dépendances)
./mvnw -pl product-service -am clean install
```

### Run (mode dev)

```bash
# Lancer product-service (http://localhost:8081)
./mvnw -pl product-service spring-boot:run

# Lancer order-service (http://localhost:8082)
./mvnw -pl order-service spring-boot:run
```

Pour lancer les deux services en parallèle, ouvrir deux terminaux.

### Run (via JAR exécutable)

```bash
# Après ./mvnw clean install
java -jar product-service/target/product-service-0.0.1-SNAPSHOT.jar
java -jar order-service/target/order-service-0.0.1-SNAPSHOT.jar
```

### Tests

```bash
# Tous les tests
./mvnw test

# Tests d'un seul module
./mvnw -pl product-service test

# Une seule classe de test
./mvnw -pl product-service test -Dtest=ProductServiceTest
```

### Endpoints utiles (dev)

- product-service : `http://localhost:8081/api/products`
- order-service   : `http://localhost:8082/api/orders`
- H2 console      : `http://localhost:8081/h2-console` (JDBC URL : `jdbc:h2:mem:productdb`)
- H2 console      : `http://localhost:8082/h2-console` (JDBC URL : `jdbc:h2:mem:orderdb`)

## Architecture notes

The project is in early setup — only the main application class exists. As features are added, follow a standard layered structure:

- `controller` — `@RestController` classes, input validation with `@Valid`
- `service` — business logic, `@Transactional` boundaries
- `repository` — Spring Data JPA repositories or Spring Data JDBC repositories
- `domain` / `model` — JPA entities and value objects
- `config` — Spring `@Configuration` classes (Security, RabbitMQ, RestClient beans, etc.)

Flyway runs automatically on startup; add new migration scripts rather than modifying existing ones.


## 📁 Structure du projet

```
src/
├── main/
│   ├── java/com/example/app/
│   │   ├── config/          # Beans de configuration Spring (@Configuration)
│   │   ├── controller/      # Couche REST (@RestController)
│   │   ├── service/         # Logique métier (@Service)
│   │   ├── repository/      # Accès données (JpaRepository)
│   │   ├── domain/          # Entités JPA (@Entity)
│   │   ├── dto/             # Objets de transfert (record Java ou classe)
│   │   ├── mapper/          # Conversion domain <-> DTO (MapStruct)
│   │   ├── exception/       # Exceptions métier + handler global
│   │   └── security/        # Filtres JWT, UserDetailsService
│   └── resources/
│       ├── application.properties
└── test/
    └── java/com/example/app/
        ├── controller/      # Tests d'intégration (MockMvc)
        ├── service/         # Tests unitaires (Mockito)
        └── repository/      # Tests JPA (@DataJpaTest)
```

## 🧱 Conventions de code

### Nommage
- **Classes** : `PascalCase` — `UserService`, `ProductController`
- **Méthodes / variables** : `camelCase` — `findUserById`, `isActive`
- **Constantes** : `UPPER_SNAKE_CASE` — `MAX_RETRY_COUNT`
- **Tables BDD** : `snake_case` — `user_account`, `product_category`
- **Packages** : tout en minuscules — `com.example.app.service`

### Architecture (règles strictes)
- Les **controllers** ne contiennent aucune logique métier — ils délèguent aux services.
- Les **services** ne dépendent jamais directement des repositories d'un autre domaine — passer par le service correspondant.
- Les **entités JPA** ne sortent jamais des services — toujours convertir en DTO avant de retourner.
- Pas de `@Autowired` sur les champs — utiliser **l'injection par constructeur** (compatible Lombok `@RequiredArgsConstructor`).

The project follows a standard layered Spring Boot structure: controllers → services → repositories. Place code under `com.example.demo` using feature-based sub-packages (e.g., `com.example.demo.user`, `com.example.demo.order`).

- **Entities** use JPA annotations; prefer Java 21 records for DTOs/request/response bodies.
- **Security** is provided by Spring Security — configure `SecurityFilterChain` beans rather than extending `WebSecurityConfigurerAdapter` (removed in Spring Boot 4.x).
- **AMQP** listeners go in `@Component` classes annotated with `@RabbitListener`; declare exchanges/queues as `@Bean`s in a `@Configuration` class.
- **Spring AI** client (`ChatClient`) is auto-configured when `spring.ai.anthropic.api-key` is set in `application.properties` or environment.
- **OpenAPI** docs are auto-generated; annotate controllers with `@Tag` and operations with `@Operation` as needed.

### DTOs
- Préférer les **records Java** pour les DTOs immuables (requêtes/réponses simples).
- Utiliser des classes classiques si le DTO a besoin de validation (`@Valid`) ou de logique de transformation.

### Gestion des exceptions
- Toutes les exceptions métier héritent de `BusinessException` (dans `exception/`).
- Le handler global `GlobalExceptionHandler` (annoté `@RestControllerAdvice`) centralise les réponses d'erreur.
- Format de réponse d'erreur standardisé : `{ "status": 400, "error": "...", "message": "...", "timestamp": "..." }`.

---

## 🔐 Sécurité

- L'authentification repose sur des **tokens JWT** passés dans le header `Authorization: Bearer <token>`.
- Ne jamais logger un token, un mot de passe ou des données personnelles.
- Les endpoints publics sont déclarés explicitement dans `SecurityConfig` — tout le reste est protégé par défaut.
- Les mots de passe sont hashés avec **BCrypt** (via `PasswordEncoder`).

---

## 🧪 Tests

- Chaque service doit avoir une couverture de tests unitaires (Mockito).
- Chaque controller doit avoir un test d'intégration avec `MockMvc`.
- Les tests JPA utilisent `@DataJpaTest` + base H2 (profil `test`).
- Les tests ne doivent jamais dépendre d'un ordre d'exécution (isolation garantie).
- Nommage des méthodes de test : `methodName_should_expectedBehavior_when_condition`.

---

## 🚫 Ce que Claude NE doit PAS faire

- Ne pas modifier `pom.xml` sans demande explicite (gestion des dépendances = décision d'équipe).
- Ne pas supprimer ou réécrire les migrations Flyway existantes (dans `resources/db/migration/`).
- Ne pas contourner la validation (`@Valid`) ou les règles de sécurité.
- Ne pas utiliser `@Transactional` sur les controllers — uniquement sur les services.
- Ne pas exposer les entités JPA directement dans les réponses REST.
- Ne pas utiliser `System.out.println` — toujours passer par `Slf4j` / `@Slf4j` (Lombok).

---

## 📌 Bonnes pratiques à privilégier

- **Lombok** : utiliser `@Getter`, `@Builder`, `@RequiredArgsConstructor`, `@Slf4j` pour réduire le boilerplate.
- **Pagination** : toujours utiliser `Pageable` pour les endpoints qui retournent des listes.
- **Validation** : annoter les DTOs entrants avec `@Valid` et les contraintes Jakarta (`@NotNull`, `@Size`, etc.).
- **Profils Spring** : ne jamais hard-coder des URLs ou credentials — toujours passer par `application-{profil}.yml`.
- **Transactions** : définir la portée au niveau du service, en précisant `readOnly = true` pour les lectures.

---

## 🔗 Liens utiles (internes)

- Swagger UI (dev) : `http://localhost:8080/swagger-ui.html`
- Actuator health : `http://localhost:8080/actuator/health`
- Console H2 (test) : `http://localhost:8080/h2-console`