check-project-visibility
=====================

NAME
----
check-project-visibility - Check project readability to a specific user

SYNOPSIS
--------
>    ssh -p @SSH_PORT@ @SSH_HOST@ @PLUGIN@ check-project-visibility [--project <PROJECT> | -p <PROJECT>] [--user <USER> | -u <USER>]

DESCRIPTION
-----------
Allows any user to query if a specific user can read non-config references in the specific project.

ACCESS
------
Any user who has configured an SSH key.

SCRIPTING
---------
This command is intended to be used in scripts.

EXAMPLES
------

Check if project "test_project" is visible to "test_user".

>    $ssh -p @SSH_PORT@ @SSH_HOST@ @PLUGIN@ check-project-visibility -p test_project -u test_user


GERRIT
------
Part of [Gerrit Code Review](../../../Documentation/index.html)
