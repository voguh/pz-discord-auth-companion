# Project Zomboid Discord Auth Companion

This project implements a Discord bot that links with Project Zomboid (through the Source RCON Protocol). Its purpose is
to control the authentication of the Zomboid server, allowing the configuration of 3 Discord roles for "regular player",
"moderator" and "administrator".

Changes made to users are reflected in real-time on the Zomboid server thanks to the RCON connection.

When a new user receives the role configured for player, the "unbanuser" command is used to add the user with the same
Discord username. The user is created without a password, and the player can set it on the first login.

When a user loses the role related to player, they are automatically banned and their access levels are revoked on the
Zomboid server.

When a user undergoes role changes, such as losing or receiving the role of moderator and/or administrator, these new
permissions are automatically reflected on the Zomboid server.


## License

This project is under [GNU LESSER GENERAL PUBLIC LICENSE, Version 3](./LICENSE).
