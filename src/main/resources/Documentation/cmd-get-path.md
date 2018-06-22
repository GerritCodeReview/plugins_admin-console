@PLUGIN@ get-path
================

NAME
----
@PLUGIN@ get-path get full path of repository

SYNOPSIS
--------
>     ssh -p <port> <host> @PLUGIN@ get-path
>      [repository]


DESCRIPTION
-----------
Get the full path of an existing repository.

ARGUMENTS
-------
`repository`
> Repository to get the full path for

ACCESS
------
Gerrit Administrators only.

SCRIPTING
---------
This command is intended to be used in scripts.

EXAMPLES
--------
Get full path for a repository named "my-project"
>     $ ssh -p @SSH_PORT@ @SSH_HOST@ @PLUGIN@ get-path my-project