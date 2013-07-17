admin-console show-account
================

NAME
----
admin-console show-account show user account information

SYNOPSIS
--------
>     ssh -p <port> <host> admin-console show-account
>      [user]
>      [--show-groups]
>      [--filter-groups] [filter-string]
>      [--show-keys]

DESCRIPTION
-----------
Displays useful information about a specific user.  For search strings that match >1 user, will return multiple result sets.

OPTIONS
-------

user
> User to look up: This can be in one of several formats: LastName,\\\\ FirstName,  email\@address.com, account id or an user name. Be sure to double-escape spaces.  Case-sensitive

--show-groups
> Show all groups user is a member of?

--filter-groups
> Filter group list?

--filter-string
> String to perform group filtering on.  Does not currently support regex.  Case-insensitive.

--show-keys
> Show users ssh public keys?

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

>     $ ssh -p 29418 review.example.com admin-console show-user Foo

Find a user with email foo@bar.com

>     $ ssh -p 29418 review.example.com admin-console show-user foo@bar.com

Find a user named Foo Bar

>     $ ssh -p 29418 review.example.com admin-console show-user Bar,\\\\ Foo

Find a user named Foo and show all groups the user is a member of

>     $ ssh -p 29418 review.example.com admin-console show-user Foo --show-groups

Find a user named Foo and show all groups containing "baz" that the user is a member of

>     $ ssh -p 29418 review.example.com admin-console show-user Foo --show-groups --filter-groups baz
