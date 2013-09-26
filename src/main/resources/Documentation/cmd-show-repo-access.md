@PLUGIN@ show-repo-access
================

NAME
----
@PLUGIN@ show-repo-access repository access information

SYNOPSIS
--------
>     ssh -p <port> <host> @PLUGIN@ show-repo-access
>      [repository]
>      [-w]


DESCRIPTION
-----------
Displays access for a specific repository.  Does not interpret repository inheritance (currently - may change in the future, or be added as an option)
Note, see [cmd-show-account](cmd-show-account.html) for more information on the user search functionality provided here.

OPTIONS
-------
repository
> Repository to show access for

-w
> Display without line width truncation
--help

-h
> Display usage information.

ACCESS
------
Gerrit Administrators only.

SCRIPTING
---------
This command is not intended to be used in scripts.

EXAMPLES
--------

Find Access for All-Projects

>     $ ssh -p @SSH_PORT@ review.example.com @PLUGIN@ show-repo-access All-Projects

Find Access for a repository named "my-project"
>     $ ssh -p @SSH_PORT@ review.example.com @PLUGIN@ show-repo-access my-project