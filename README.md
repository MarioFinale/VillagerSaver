# VillagerSaver
A Minecraft plugin for Spigot servers

**[Spigot Resource Page](https://www.spigotmc.org/resources/villager-saver.84738/)**

**VillagerSaver** keeps villagers alive through zombie encounters by turning them into zombie villagers—ready for you to cure and restore. From basic zombie attacks to drowned tridents or even TNT and creeper explosions triggered by zombie variants, your villagers stay safe. Trades, homes, job sites, and reputation remain intact.

- **Key Features**:
  - Converts villagers killed by zombie variants (zombies, husks, drowned, etc.) into zombie villagers.
  - Handles extra threats like tridents and TNT/creeper explosions primed by zombie variants.
  - Offers a configurable world blacklist with commands (`/villagersaver blacklistworld <world>`).
  - Logs zombification events with XYZ coordinates for easy monitoring.

Ideal for servers that depend on villager trading or aim to spare players the pain of losing valuable villagers.

- **Permissions**:
  - `villagersaver.blacklist`: Allows managing the VillagerSaver world blacklist (defaults to operators).
- **Config**:
  - The blacklist is saved at `/plugins/VillagerSaver/WorldBlackList.yml`. Manual editing is not recommended to avoid file corruption.

## Changelog

### 1.4.5
- Updated to minecraft 1.21.8

### 1.4.4
- Updated to Minecraft 1.21.4.
- Added `villagersaver.blacklist` permission for operator-only blacklist management.
- Added XYZ coordinate logging for villager zombification events.
- Refactored fields and methods for improved encapsulation and maintainability.
- Improved safety and exception handling for file operations and events.
- Updated README with permissions and config sections.

### 1.4.3
- Compiled against Java 21.

### 1.4.2
- Updated to 1.21. (Requires Java 23)

### 1.4.1
- Fixes [#22](https://github.com/MarioFinale/VillagerSaver/issues/22) (ClassCastException when a Villager is Killed by a Creeper Explosion).

### 1.4.0
- Now converts villagers killed by drowned tridents or a [TNT explosion primed by a trident](https://www.youtube.com/watch?v=qR_jv8wefAY).

### 1.3.5
- Specify API version as String.

### 1.3.4
- Minor changes.
- Updated to 1.20.1.

### 1.3.3
- Updated to 1.19.2.

### 1.3.2
- Plugin now checks on death events for improved performance (fewer function calls).
- Plugin now uses the *zombify* function (should fix [#18](https://github.com/MarioFinale/VillagerSaver/issues/18)).
- Improved readability.

### 1.3.1
- Updated to 1.19.

### 1.2.5
- Updated to 1.18.2.

### 1.2.4
- Updated to 1.18 - 1.18.1.

### 1.2.3
- Fixed logging (now using proper Bukkit logger, fixes [#15](https://github.com/MarioFinale/VillagerSaver/issues/15)).
- Fixed nag message about using stdout.
- Minor code changes to improve readability.
- Changed plugin messages to better communicate plugin status.
- Added a severe warning about plugin hijacking, advising downloads from GitHub only (fixes [#16](https://github.com/MarioFinale/VillagerSaver/issues/16)).

### 1.2.2
- Updated to 1.17.1.

### 1.2.1
- Updated to 1.17.

### 1.2.0
- Fixes [#13](https://github.com/MarioFinale/VillagerSaver/issues/13) (Merges Kasama's fork into main).

### 1.1.6
- Fixes [#10](https://github.com/MarioFinale/VillagerSaver/issues/10) (Many thanks to Kasama).

### 1.1.5
- Spigot support.

### 1.1.4
- Adds [#8](https://github.com/MarioFinale/VillagerSaver/issues/8).

### 1.1.3
- Fixes [#7](https://github.com/MarioFinale/VillagerSaver/issues/7).
- Updated to 1.16.5.

### 1.1.2
- Fixes [#6](https://github.com/MarioFinale/VillagerSaver/issues/6).

### 1.1.1
- World blacklist, usage:
  - `/villagersaver blacklistworld <world_name>`
  - `/villagersaver unblacklistworld <world_name>`
- Fixes [#4](https://github.com/MarioFinale/VillagerSaver/issues/4).
- Fixes [#3](https://github.com/MarioFinale/VillagerSaver/issues/3).

[![ko-fi](https://www.ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/W7W52TMLM)