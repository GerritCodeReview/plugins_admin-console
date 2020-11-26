workspace(name = "admin_console")

load("//:bazlets.bzl", "load_bazlets")

load_bazlets(
    commit = "cf0bbc90e09a8a1d7c042d79f1555e3fa40984e1",
    #local_path = "/home/<user>/projects/bazlets",
)

load(
    "@com_googlesource_gerrit_bazlets//:gerrit_api.bzl",
    "gerrit_api",
)

gerrit_api(version = "3.3.0-SNAPSHOT")
