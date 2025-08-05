# 🏦 Bank Cards REST API

REST API для управления банковскими картами с PostgreSQL.

---
## Основные возможности

- Регистрация и аутентификация пользователей (JWT)
- CRUD для банковских карт
- Переводы между своими картами
- Просмотр баланса и истории переводов
- Блокировка и активация карт
- Маскирование и шифрование номеров карт
- Пагинация и фильтрация по картам
- Управление пользователями (только для ADMIN)
- Liquibase миграции для БД
- Документация через Swagger/OpenAPI

---

## 🚀 Быстрый старт

### 1. Запуск через Docker Compose 

> **Всё поднимается одной командой!**

```bash
# Запуск PostgreSQL и приложения
docker-compose up -d

# Проверка статуса контейнеров
docker-compose ps

# Просмотр логов приложения 
docker-compose logs -f app
```

- Приложение будет доступно на [http://localhost:8080]

---

### 2. Запуск в IntelliJ IDEA

1. **Запуск БД через Docker:**
   ```bash
   docker-compose up db -d
   ```
2. **Откройте проект в IntelliJ IDEA**  
3. **Убедитесь, что установлен JDK 17+**
4. **Соберите проект:**  
   - Через Maven: `mvn clean install`
   - Или через Build → Build Project в IDEA
5. **Запустите приложение:**  
   - Через кнопку ▶️ рядом с `BankCardsApplication.java`
   - Или через терминал:
     ```bash
     mvn spring-boot:run
     ```

---

### 3. Сборка и запуск через Docker вручную

```bash
# Собрать образ
docker build -t bankcards .

# Запустить контейнер
docker run -p 8080:8080 bankcards
```

---

## 🗄️ Доступ к базе данных

- **Host:** localhost:5432
- **Database:** bankdb
- **Username:** bankuser
- **Password:** bankpass

---

## 📋 Доступные URL

- **API:** `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **API Docs:** `http://localhost:8080/v3/api-docs`

---

## Важно

- Для работы с API нужен JWT-токен (получить через /api/auth/login).

---

