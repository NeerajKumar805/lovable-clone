
# Lovable clone Website — Entity Layer Documentation

*Complete, ultra-detailed reference for all domain models, enums, relationships, and inferred database structure*

---

## 1. Overview

The entity layer defines the entire data structure of the Lovable system.
It captures how projects, users, files, chats, previews, subscriptions, and logs are stored and connected.

This documentation explains:

* The purpose behind each entity
* All fields with meaning and constraints
* Enum definitions and how they are used
* Relationship patterns (simple IDs vs JPA relationships)
* Inferred SQL schema for each table
* Design decisions
* Future expansion paths

This serves as your **master reference** for onboarding devs, debugging, migrations, or planning new features.

---

## 2. Architectural Philosophy

Before diving into each entity, here’s the core philosophy of this domain model:

### 2.1 Flat Entities Over Heavy Relations

Instead of deep JPA relationships (which cause N+1 problems, cascade issues, and opaque queries), this project uses:

* `Long` IDs instead of entity references
* Explicit joins in services if ever needed
* Embedded IDs only where they make strong logical sense (like project membership)

This keeps the backend predictable, easier to scale, and safer during refactors.

### 2.2 Enum-Driven State Machines

Statuses like subscription, preview, and chat roles use strict enums.
This prevents invalid states and makes reasoning much easier.

### 2.3 Metadata Fields Everywhere

Most entities contain a `metadata` string.
This acts as a flexible storage for:

* Experiment features
* Small flags
* Debugging trails
* JSON blobs

It ensures the domain layer doesn’t need schema changes for every tiny tweak.

---

## 3. Enums

### 3.1 MessageRole

Values: `USER`, `ASSISTANT`, `SYSTEM`
Purpose: Defines which side generated a chat message.

### 3.2 PreviewStatus

Values: `CREATING`, `RUNNING`, `FAILED`
Purpose: Tracks preview generation lifecycle.

### 3.3 ProjectRole

Values: `EDITOR`
Purpose: Defines access level of project members.
(Currently one role; future-ready for ADMIN, VIEWER, etc.)

### 3.4 SubscriptionStatus

Values: `ACTIVE`, `TRIALING`, `CANCELED`, `PAST_DUE`
Purpose: Manages user billing lifecycle.

---

# 4. Entities (Ultra Detailed)

Below is the full entity documentation, including:

* Purpose
* Field-by-field explanation
* Inferred SQL table structure
* Relationship logic
* Future expansion notes

---

# 4.1 User

### Purpose

Represents a platform user with authentication credentials and billing linkage.

### Fields and Meaning

| Field            | Type    | Notes                            |
| ---------------- | ------- | -------------------------------- |
| id               | Long    | Primary key                      |
| name             | String  | User display name                |
| email            | String  | Login credential; must be unique |
| password         | String  | Hashed password                  |
| stripeCustomerId | String  | Stripe billing linkage           |
| createdAt        | Instant | Auto timestamp                   |
| updatedAt        | Instant | Auto timestamp                   |
| metadata         | String  | Optional custom info             |

### Inferred SQL

```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  stripe_customer_id VARCHAR(255),
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  metadata TEXT
);
```

### Notes

* Email uniqueness must be handled by DB + service layer.
* Stripe ID allows multiple subscriptions historically.

---

# 4.2 Project

### Purpose

A user-owned workspace containing files, chat sessions, and previews.

### Fields

| Field       | Type    |
| ----------- | ------- |
| id          | Long    |
| name        | String  |
| description | String  |
| ownerId     | Long    |
| createdAt   | Instant |
| updatedAt   | Instant |

### SQL

```sql
CREATE TABLE projects (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  description TEXT,
  owner_id BIGINT,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);
```

### Relationships

* Owner is a `User`, but referenced via `ownerId` to avoid forced joins.

### Future expansion

* Access control: ADMIN / VIEWER roles
* Archived projects

---

# 4.3 ProjectMember + ProjectMemberId (Composite Key)

### Purpose

Allows multiple users to collaborate on a project.

### Embedded ID Fields

| Field     | Type |
| --------- | ---- |
| projectId | Long |
| userId    | Long |

### ProjectMember Fields

| Field    | Type            |
| -------- | --------------- |
| id       | ProjectMemberId |
| role     | ProjectRole     |
| joinedAt | Instant         |
| metadata | String          |
| active   | Boolean         |
| notes    | String          |

### SQL

```sql
CREATE TABLE project_members (
  project_id BIGINT,
  user_id BIGINT,
  role VARCHAR(50),
  joined_at TIMESTAMP,
  metadata TEXT,
  active BOOLEAN,
  notes TEXT,
  PRIMARY KEY (project_id, user_id)
);
```

### Notes

* Composite keys are the correct choice for membership tables.
* No cascade relationships—keeps deletion safe.

---

# 4.4 ProjectFile

### Purpose

Represents a file within a project.
Could be code, media, docs, etc.

### Fields

| Field     | Type    |
| --------- | ------- |
| id        | Long    |
| projectId | Long    |
| fileName  | String  |
| fileType  | String  |
| fileSize  | Long    |
| path      | String  |
| createdAt | Instant |
| metadata  | String  |

### SQL

```sql
CREATE TABLE project_files (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT,
  file_name VARCHAR(255),
  file_type VARCHAR(255),
  file_size BIGINT,
  path TEXT,
  created_at TIMESTAMP,
  metadata TEXT
);
```

### Notes

* `path` can point to local storage, S3, or other storage engines.

---

# 4.5 ChatSession

### Purpose

Represents a conversation inside a project.

### Fields

| Field      | Type    |
| ---------- | ------- |
| id         | Long    |
| projectId  | Long    |
| sessionKey | String  |
| createdAt  | Instant |
| updatedAt  | Instant |
| metadata   | String  |

### SQL

```sql
CREATE TABLE chat_sessions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT,
  session_key VARCHAR(255),
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  metadata TEXT
);
```

---

# 4.6 ChatMessage

### Purpose

Stores individual messages in a chat session.

### Fields

| Field      | Type        |
| ---------- | ----------- |
| id         | Long        |
| content    | String      |
| role       | MessageRole |
| sessionId  | String      |
| createdAt  | Instant     |
| updatedAt  | Instant     |
| tokenCount | Integer     |

### SQL

```sql
CREATE TABLE chat_messages (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  content TEXT,
  role VARCHAR(50),
  session_id VARCHAR(255),
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  token_count INT
);
```

---

# 4.7 Preview

### Purpose

Handles async preview generation for files or projects.

### Fields

| Field        | Type          |
| ------------ | ------------- |
| id           | Long          |
| projectId    | Long          |
| fileId       | Long          |
| status       | PreviewStatus |
| resultUrl    | String        |
| startedAt    | Instant       |
| finishedAt   | Instant       |
| errorMessage | String        |
| metadata     | String        |

### SQL

```sql
CREATE TABLE previews (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT,
  file_id BIGINT,
  status VARCHAR(50),
  result_url TEXT,
  started_at TIMESTAMP,
  finished_at TIMESTAMP,
  error_message TEXT,
  metadata TEXT
);
```

---

# 4.8 Subscription

### Purpose

Represents a user's billing subscription.

### Fields

| Field                | Type               |
| -------------------- | ------------------ |
| id                   | Long               |
| userId               | Long               |
| planId               | Long               |
| status               | SubscriptionStatus |
| startedAt            | Instant            |
| endedAt              | Instant            |
| currentPeriodEnd     | Instant            |
| amount               | BigDecimal         |
| stripeSubscriptionId | String             |
| metadata             | String             |

### SQL

```sql
CREATE TABLE subscriptions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  plan_id BIGINT,
  status VARCHAR(50),
  started_at TIMESTAMP,
  ended_at TIMESTAMP,
  current_period_end TIMESTAMP,
  amount DECIMAL(12,2),
  stripe_subscription_id VARCHAR(255),
  metadata TEXT
);
```

---

# 4.9 Plan

### Purpose

Defines subscription plans.

### Fields

| Field         | Type       |
| ------------- | ---------- |
| id            | Long       |
| name          | String     |
| price         | BigDecimal |
| stripePriceId | String     |
| interval      | String     |
| features      | String     |
| isDefault     | Boolean    |
| metadata      | String     |

---

# 4.10 UsageLog

### Purpose

Tracks billing-related usage (tokens, files, actions, compute time).

### Fields

| Field     | Type    |
| --------- | ------- |
| id        | Long    |
| userId    | Long    |
| projectId | Long    |
| type      | String  |
| amount    | Long    |
| createdAt | Instant |
| notes     | String  |
| metadata  | String  |

---

# 5. Suggested Improvements (Future Safe)

Here’s what would make the model even stronger over time.

### 5.1 Add soft delete

Add:

```
deletedAt
```

to most entities.

### 5.2 Convert metadata to JSON

Use `@JdbcTypeCode(SqlTypes.JSON)` or PostgreSQL JSONB.

### 5.3 Add relational integrity

Foreign key constraints optional but useful:

* user → subscription
* project → preview
* project → file

### 5.4 Add indexes

Critical fields to index:

* `projectId`
* `userId`
* `sessionKey`
* `stripeSubscriptionId`

---

# 6. Conclusion

This README gives a fully detailed, structured, production-ready explanation of the entity layer.
It covers everything the backend needs to stay maintainable and scalable.
