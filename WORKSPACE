workspace(name = "admin_console")

load("//:bazlets.bzl", "load_bazlets")

load_bazlets(
    commit = "bd8db0cf3057397bcf7287c28cc93886e663989b",
    #local_path = "/home/<user>/projects/bazlets",
)

load(
    "@com_googlesource_gerrit_bazlets//:gerrit_api.bzl",
    "gerrit_api",
)

gerrit_api()
