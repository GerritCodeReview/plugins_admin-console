@PLUGIN@ start-reindex
======================

NAME
----
start-reindex

SYNOPSIS
--------
>     ssh -p <port> <host> @PLUGIN@ start-reindex
>      {version}

DESCRIPTION
-----------
Starts a full online re-index with the specified Lucene index version.

The index version for a Gerrit site can be found in
$review_site/index/gerrit_index.config


OPTIONS
-------

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

Start online reindex with index version 14 (for Gerrit 2.11.x)

>     $ ssh -p @SSH_PORT@ review.example.com @PLUGIN@ start-reindex 14

*Note - will need to check Gerrit log to verify successful execution.

SEE ALSO
--------

* [Access Controls](../../../Documentation/access-control.html)
* [Command Line Tools](../../../Documentation/cmd-index.html)

GERRIT
------
Part of [Gerrit Code Review](../../Documentation/index.html)