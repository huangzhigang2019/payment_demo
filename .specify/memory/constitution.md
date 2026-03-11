<!--
Sync Impact Report
- Version change: (template) → 1.0.0
- Modified principles: N/A (initial fill from template)
- Added sections: Core Principles (5), Additional Constraints, Development Workflow, Governance
- Removed sections: None
- Templates: plan-template.md ✅ (Constitution Check references constitution file); spec-template.md ✅ (no mandatory sections changed); tasks-template.md ✅ (task types unchanged); commands not updated (generic guidance)
- Follow-up TODOs: None
-->
# Payment Demo Constitution

## Core Principles

### I. Default Language (English)

All user-facing text in the application MUST default to English. Strings used in the UI (labels, messages, buttons, errors) MUST be defined in English as the default language. Localization to other languages MAY be added via locale resources, but the base/default strings MUST be in English.

**Rationale**: Ensures consistent product presentation and simplifies maintenance for an international or English-first payment demo.

### II. Technology Stack

The project MUST use Java 8+ in line with Android Gradle and reference implementations. Core dependencies MUST include NeptuneLiteAPI (device abstraction layer), EMV libraries (emv/commonlib/sdk modules or equivalent), and AndroidX/Support libraries. UI assets MUST align with the UI_Resource and Lanhu design deliverables where specified.

**Rationale**: Compatibility with existing PAX/NeptuneLite and EMV reference stacks is required for deployment on supported payment devices.

### III. Code Style and Structure

Source MUST follow standard Java and Android conventions. Project structure MUST reflect a clear separation of app, SDK, EMV, and commonlib modules. New features MUST reuse existing architecture and utilities; unnecessary complexity MUST be avoided.

**Rationale**: Consistency and simplicity reduce defects and ease onboarding.

### IV. Testing and Quality

Features that affect transaction flow, EMV handling, or device interaction MUST be verifiable (e.g., by manual or automated tests). Regression fixes MUST be validated with runtime evidence where applicable.

**Rationale**: Payment flows require reliable behavior; evidence-based verification reduces regressions.

### V. Simplicity and Maintainability

Implementations MUST be as small and targeted as possible. Speculative or unproven fixes MUST NOT be retained without runtime evidence. Documentation and comments MUST be updated when behavior or constraints change.

**Rationale**: Keeps the codebase maintainable and avoids drift from design intent.

## Additional Constraints

- **Platform**: Android; target SDK and build tools as defined in the project Gradle files.
- **Compliance**: EMV and payment-related behavior MUST align with applicable specs and reference implementations (e.g., JemvDemo2.0 where adopted).
- **Security**: Sensitive data (e.g., full card numbers, PINs) MUST NOT be logged or persisted in plain form; use masking and secure channels as appropriate.

## Development Workflow

- Feature work SHOULD be driven by specs under `specs/` and plans under `.specify/` where present.
- Changes that affect user-facing text MUST preserve the default-English principle (Principle I).
- Constitution compliance SHOULD be checked in plan/spec reviews; deviations MUST be justified and documented.

## Governance

This constitution supersedes ad-hoc project practices where it applies. Amendments require updating this file, incrementing the version per semantic versioning (MAJOR: backward-incompatible principle changes; MINOR: new principles or material guidance; PATCH: clarifications and typos), and updating the Last Amended date. All PRs and reviews SHOULD verify that changes align with the principles above; exceptions MUST be documented.

**Version**: 1.0.0 | **Ratified**: 2026-03-12 | **Last Amended**: 2026-03-12
