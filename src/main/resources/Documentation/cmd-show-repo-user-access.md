admin-console show-repo-account-access
================

NAME
----
admin-console show-repo-account-access show user account information

SYNOPSIS
--------
>     ssh -p <port> <host> admin-console show-repo-account-access
>      [repository]
>      [--user] [user]
>      [-w]


DESCRIPTION
-----------
Displays access for a specific repository by user.  Does not interpret repository inheritance (currently - may change in the future, or be added as an option)

OPTIONS
-------
repository
> Repository to show access for

--user
> User to look up: This can be in one of several formats: LastName,\\\\ FirstName,  email\@address.com, account id or an user name. Be sure to double-escape spaces.  Case-sensitive

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

Find a user named Foo

>     $ ssh -p 29418 review.example.com admin-console show-repo-account-access Foo

Find a user with email foo@bar.com

>     $ ssh -p 29418 review.example.com admin-console show-repo-account-access foo@bar.com

Find a user named Foo Bar

>     $ ssh -p 29418 review.example.com admin-console show-repo-account-access Bar,\\\\ Foo

Find a user named Foo and show all groups the user is a member of

>     $ ssh -p 29418 review.example.com admin-console show-repo-account-access Foo --show-groups

Find a user named Foo and show all groups containing "baz" that the user is a member of

>     $ ssh -p 29418 review.example.com admin-console show-repo-account-access Foo --show-groups --filter-groups baz
