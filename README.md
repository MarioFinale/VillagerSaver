# VillagerSaver
 Minecraft plugin for Spigot
 
Resource on Spigot Website: https://www.spigotmc.org/resources/villager-saver.84738/
 
Prevents the death of villagers from zombies. They will be always converted to zombie villagers so, worst case scenario the player can just heal them.
It preserves trades, homes, job location and updates reputation.

## Changelog

### 1.4.3
- Compiled against Java 21.

### 1.4.2
- Updated to 1.21. (Requires java 23)

### 1.4.1
- Fixes [#22](https://github.com/MarioFinale/VillagerSaver/issues/22) (ClassCastException when a Villager is Killed by a Creeper Explosion).

### 1.4.0
- Now it converts Villagers Killed by Drowned tridents or a [TNT explosion primed by a Trident](https://www.youtube.com/watch?v=qR_jv8wefAY).

### 1.3.5
- Specify API version as String.

### 1.3.4
- Minor changes.
- Updated to 1.20.1.

### 1.3.3
- Updated to 1.19.2.

### 1.3.2
- Plugin now checks on death events for improved performance (fewer function calls)
- Plugin now uses the *zombify* function (Should fix [#18](https://github.com/MarioFinale/VillagerSaver/issues/18)) 
- Improved readability

### 1.3.1
- Updated to 1.19.

### 1.2.5
- Updated to 1.18.2.

### 1.2.4
- Updated to 1.18 - 1.18.1.

### 1.2.3
- Fixed logging (now using proper Bukkit logger, fixes [#15]https://github.com/MarioFinale/VillagerSaver/issues/15)).
- Fixed nag message about using stdout.
- Minor code changes to improve readability
- Change plugin messages to better communicate plugin status.
- Add a severe waring message about the plugin being hijacked and advising user to always download the plugin from a trusted source (GitHub). Fixes [#16](https://github.com/MarioFinale/VillagerSaver/issues/16).

### 1.2.2
- Updated to 1.17.1

### 1.2.1
- Updated to 1.17

### 1.2.0
- Fixes [#13](https://github.com/MarioFinale/VillagerSaver/issues/13) (Merges Kasama's fork into main)

### 1.1.6
- Fixes [#10](https://github.com/MarioFinale/VillagerSaver/issues/10) (Many thanks to Kasama)

### 1.1.5
- Spigot support.

### 1.1.4
- Adds [#8](https://github.com/MarioFinale/VillagerSaver/issues/8)

### 1.1.3
- Fixes [#7](https://github.com/MarioFinale/VillagerSaver/issues/7)
- Updated to 1.16.5

### 1.1.2
- Fixes [#6](https://github.com/MarioFinale/VillagerSaver/issues/4)

### 1.1.1:
- World blacklist, usage:
  - /villagersaver blacklistworld {world_name}
  - /villagersaver unblacklistworld {world_name}
- Fixes [#4](https://github.com/MarioFinale/VillagerSaver/issues/4)
- Fixes [#3](https://github.com/MarioFinale/VillagerSaver/issues/3)

The blacklist is saved on /plugins/VillagerSaver/WorldBlackList.yml I do not recommend editing this file manually.




[![ko-fi](https://www.ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/W7W52TMLM)
