@PLUGIN@ ls-users
=================

NAME
----
@PLUGIN@ ls-users list user accounts

SYNOPSIS
--------
>     ssh -p <port> <host> @PLUGIN@ ls-users

DESCRIPTION
-----------
Lists all user accounts registered on the site.

OPTIONS
-------
`--active-only`
> Show only active users

`--inactive-only`
> Show only inactive users

`--help, -h`
> Display usage information.

ACCESS
------
Gerrit Administrators only.

SCRIPTING
---------
This command is not intended to be used in scripts.
