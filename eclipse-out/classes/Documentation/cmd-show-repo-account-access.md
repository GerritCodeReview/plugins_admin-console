@PLUGIN@ show-repo-account-access
================

NAME
----
@PLUGIN@ show-repo-account-access show repository access by account

SYNOPSIS
--------
>     ssh -p <port> <host> @PLUGIN@ show-repo-account-access
>      [repository]
>      [--user] [user]
>      [-w]


DESCRIPTION
-----------
Displays access for a specific repository by user.  Does not interpret repository inheritance (currently - may change in the future, or be added as an option)

OPTIONS
-------
`repository`
> Repository to show access for

`--user`
> User to look up: This can be in one of several formats: LastName,\\\\ FirstName,  email\@address.com, account id or an user name. Be sure to double-escape spaces.  Case-sensitive

`-w`
> Display without line width truncation

`--help, -h`
> Display usage information.

ACCESS
------
Gerrit Administrators only.

SCRIPTING
---------
This command is not intended to be used in scripts.

EXAMPLES
--------

Find Access for All-Projects and a user named Foo

>     $ ssh -p @SSH_PORT@ review.example.com @PLUGIN@ show-repo-account-access All-Projects --user Foo

Find Access for a repository named "my-project" and a user named Foo

>     $ ssh -p @SSH_PORT@ review.example.com @PLUGIN@ show-repo-access my-project --user Foo