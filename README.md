# OnlineRecord

A plugin for Spigot/Paper/etc which announces and logs when your server reaches a new high player count achievement.

## Quick Links

* [Discord Server](https://discord.gg/arcaneplugins-752310043214479462) for chat and support
* [GitHub Repository](https://github.com/lokka30/OnlineRecord) for source code and issue tracking

## About

When a player joins, the plugin simply checks its stored highest player count and compares it against the current player
count. If there are more players than the previous highest player count, then a message can be broadcasted to all online
players and the achievement is also logged to the console.

All players online during the achievement are also logged.

## Requirements

* Server software: SpigotMC / PaperMC.
  * Other server software may work too, but they are unsupported by the maintainers.
* Server version: Minecraft 1.21
  * Older versions may work too, but they are unsupported by the maintainers.

## Installation and Configuration

* Check the Requirements section above to see if your server will be compatible and supported prior to installing it on
  your server.
* Download the resource.
* Move the downloaded file into your server's `/plugins/` folder.
* If your server is already running: stopthe server by sending the `/stop` command.
* Start your server. This will generate the configuration files.
* Once your server has started, navigate to the `/plugins/OnlineRecord/` folder.
* Open the `config.yml` file with your preferred editor. I recommend Notepad++ for Windows users.
* Change any settings to your liking.
* Apply the changes by running `/or reload` or restarting your server.
* Enjoy! :)

## Commands & Permissions

* `/onlinerecord`
    * Description: `Contains commands to manage the resource.`
    * Aliases: `/or`
    * Requires permission: `onlinerecord.command`

* `/onlinerecord reload`
    * Description: `Reload the configuration.`
    * Requires permissions: `onlinerecord.command` & `onlinerecord.reload`

## License

Licensed under [GNU GPL v3.0](LICENSE.md).
