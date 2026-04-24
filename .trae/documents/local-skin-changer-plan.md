# Local Skin Changer (Fabric) — Plan

## Summary
Build a **client-side Fabric mod** that adds a **Title Screen (“home menu”) button** opening a simple **skin picker UI** and **overrides only the local player’s rendered skin** (no packets, no server-side/global change). Ship the two provided skins as bundled assets (offline-safe) and persist the selected skin in a small config file.

## Current State Analysis
- The repository currently contains only [LICENSE](file:///workspace/LICENSE) (already MIT).
- No Gradle/Fabric/Minecraft project structure exists yet, so the mod scaffolding must be created from scratch.

## Goals / Success Criteria
- Title Screen includes a new button that opens a “Bedrock-like” skin picker screen.
- The picker lists exactly these skins and allows equipping either:
  - `blob-duckie-23995388.png?v951`
  - `inverted-23814924.png?v951`
- Changing skins affects **only the local player’s client render**:
  - Other players do not see the change.
  - The server does not receive any “skin change”.
- Provide:
  - MIT License (already present; keep as-is unless user wants different attribution text)
  - README that describes everything using emojis
  - A short mod description summary text (1–2 lines) suitable for a mod page

## Key Decisions (Locked)
- Mod loader: **Fabric**
- Scope: **client-side only**
- Skin delivery: **bundled in the mod jar** (no runtime download required)
- Multi-version: build outputs for **Minecraft 1.20.4**, **Minecraft 1.21.x**, and **Minecraft 26.x**

## Proposed Changes (Files / Structure)

### 1) Create a multi-module Gradle workspace
Create a root Gradle project with three subprojects so each Minecraft line can use the correct mappings/dependencies without forcing one codepath to compile everywhere.

- **New root files**
  - `/workspace/settings.gradle` (include the three modules)
  - `/workspace/build.gradle` (shared repositories + Java toolchain conventions)
  - `/workspace/gradle.properties` (shared identifiers: `mod_id`, `maven_group`, `mod_version`)
  - `/workspace/gradlew`, `/workspace/gradlew.bat`, `/workspace/gradle/wrapper/*`

- **New subprojects**
  - `/workspace/mod-1_20_4/` (Fabric + Yarn mappings)
  - `/workspace/mod-1_21_x/` (Fabric + Yarn mappings)
  - `/workspace/mod-26_x/` (Fabric + official/Mojang names per Fabric guidance for 26.x)

Each subproject contains:
- `build.gradle`
- `src/main/java/...` (client code)
- `src/main/resources/fabric.mod.json`
- `src/main/resources/<mixin configs>`
- `src/main/resources/assets/<mod_id>/skins/*.png` (the two provided skins)

### 2) Implement the client-only skin override (per module)
Core behavior:
- Store selected skin id in a tiny JSON config in the standard config directory:
  - `config/local_skin_changer.json`
- Register a texture from the bundled PNG using the game’s texture manager.
- Intercept the game’s “which skin texture should I use?” path and return the custom texture **only when the queried player is the local client player**.

Implementation detail (varies by Minecraft line; separate code per module):
- **Mixin target**: the method that returns the skin texture/skin textures for an `AbstractClientPlayer*` instance.
- **Guard**: only override when `this == MinecraftClient.getInstance().player` (or equivalent in that version).

Expected new classes (per module, same package naming but compiled separately):
- `.../LocalSkinChangerClient` (client initializer; loads config; registers textures)
- `.../config/LocalSkinChangerConfig` (read/write JSON)
- `.../skin/SkinRegistry` (maps skin ids → bundled resource path → runtime `Identifier`)
- `.../mixin/AbstractClientPlayerMixin` (returns overridden skin texture only for local player)

### 3) Add “home menu” (Title Screen) button + skin picker screen (per module)
UI behavior:
- Add a new button on the Title Screen, labeled for example: `Skins`.
- Clicking opens a `SkinChangerScreen` showing:
  - Two selectable entries (name + optional small preview if feasible without heavy UI work)
  - An `Equip` action per skin (or click-to-equip)
  - A `Back` button
- When equipped:
  - Update config immediately
  - Ensure the local player’s skin texture is refreshed (re-register or reload texture)

Expected new classes:
- `.../screen/SkinChangerScreen`
- `.../mixin/TitleScreenMixin` (inject into `init` and add the button)

### 4) Assets (skins)
Add the two PNGs into each module’s resources:
- `assets/local_skin_changer/skins/blob_duckie.png`
- `assets/local_skin_changer/skins/inverted.png`

The UI will display friendly names:
- “Blob Duckie”
- “Inverted”

### 5) README (emoji-heavy) + short mod description
- **New** `/workspace/README.md`
  - Emoji sections for: features, install, usage, compatibility, credits, license.
  - Clear note: “client-side only; others will not see your skin change”.
- Provide a short mod description summary (non-emoji, 1–2 lines) inside README and also separately in final response.

## Assumptions & Notes
- This mod is intentionally **not** a real account skin changer; it only changes the **rendered texture locally**.
- For Minecraft **26.x**, Fabric has significant tooling/mapping changes; the `mod-26_x` module will use the recommended approach for that line (official names, updated Loom/Gradle settings). If the exact 26.x minor version you use differs, you may need to bump dependency versions in that module.

## Verification Steps
- Build all modules:
  - `./gradlew :mod-1_20_4:build`
  - `./gradlew :mod-1_21_x:build`
  - `./gradlew :mod-26_x:build`
- Manual sanity checks in-game (per version):
  - Title Screen shows `Skins` button.
  - Selecting a skin updates only the local player model (third-person view or inventory preview).
  - Joining a server does not change what other players see.

