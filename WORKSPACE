workspace(name = "admin_console")

load("//:bazlets.bzl", "load_bazlets")

load_bazlets(
    commit = "8a96621f0c36fa9d5291377b0c3b63d8a9112848",
    #local_path = "/home/<user>/projects/bazlets",
)

# Snapshot Plugin API
#load(
#    "@com_googlesource_gerrit_bazlets//:gerrit_api_maven_local.bzl",
#    "gerrit_api_maven_local",
#)

# Load snapshot Plugin API
#gerrit_api_maven_local()

# Release Plugin API
load(
    "@com_googlesource_gerrit_bazlets//:gerrit_api.bzl",
    "gerrit_api",
)

# Load release Plugin API
gerrit_api()
