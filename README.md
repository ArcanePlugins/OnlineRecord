# PeakPlayersRecord
A SpigotMC plugin which logs and announces the details of when your server has its peak player count.

## Quick Links
* [Discord Server](https://discord.io/arcaneplugins) (best medium to chat & receive technical support)
* [GitHub Repository](https://github.com/lokka30/PeakPlayersRecord) (source code)
* [Trello](https://trello.com/b/xAVFFEXQ/plugins-to-do-list) (to-do list for all of my resources)

## About
When a player joins, the plugin simply checks its stored peak player count and compares it against the current player count. If there are more players than the previous peak player count, then a message can be broadcasted to all online players and the achievement is also logged to the console.

All players online during the achievement are also logged.

Advantages of this resource:
* The plugin allows you to configure every chat message it sends. All configured chat messages also have multi-line support and color code support.
* The plugin is lightweight - no repeating tasks or bloated features.
* The plugin is simple to use, as it follows the standard procedure for installation and configuration on this platform.

## Requirements
* Server software: PaperMC (recommended) and SpigotMC are supported. CraftBukkit and other Bukkit-base software should be compatible too.
* Server version: Minecraft 1.7.10 or newer. Older versions may be compatible although they are unsupported by the author.

## Installation and Configuration
* Check the Requirements section above to see if your server will be compatible and supported prior to installing it on your server.
* Download the resource.
* Move the downloaded file into your server's `/plugins/` folder. 
* If your server is already running: stopthe server by sending the `/stop` command.
* Start your server. This will generate the configuration files.
* Once your server has started, navigate to the `/plugins/PeakPlayersRecord/` folder.
* Open the `config.yml` file with your preferred editor. I recommend Notepad++ for Windows users.
* Change any settings to your liking.
* Apply the changes by running `/ppr reload` or restarting your server.
* Enjoy! :)

## Commands & Permissions
* `/peakplayersrecord`
  * Description: `Contains commands to manage the resource.`
  * Aliases: `/ppr`, `/peakplayers`
  * Requires permission: `peakplayersrecord.command`
  
* `/peakplayersrecord reload`
  * Description: `Reload the configuration.`
  * Requires permissions: `peakplayersrecord.command` & `peakplayersrecord.reload`
  
## Contact
Got a question, issue or suggestion? Feel free to send it my way. I will do my best to help you out. :)

Support is provided through the following methods:
* I highly recommend you join the [ArcanePlugins Discord Server](https://discord.io/arcaneplugins) as you will get a far quicker response, and communication is easier here too.
* If you do not wish to use Discord, then [send me a PM on Spigot](https://www.spigotmc.org/conversations/add?to=lokka30).

Support requests through the review section will be ignored.

## Support
If you wish to support me and my projects, here are a few ways:

* You can leave a nice review down below, and/or hit the 'Like' button
* You may donate to me on [PayPal](https://bit.ly/2Gxvw7R) or on my [Patreon](https://www.patreon.com/arcaneplugins).
  * Donators and patrons are eligible for a special role on my Discord server, linked above. ;)

Thank you *very* much!

## License
Licensed under [GNU GPL v3.0](https://github.com/lokka30/PeakPlayersRecord/blob/master/LICENSE.md).

## Reviews Note
As stated in the Contact section, **I will ignore support requests in the review section**. The reviews section was never intended for this purpose.

Feel free to contact me prior to a review to get any issues ironed out.