workspace(name = "admin_console")

load("//:bazlets.bzl", "load_bazlets")

load_bazlets(
    commit = "87fd5f0d0a89d01df13deaf2d21a4bdb3bc03cfd",
    #local_path = "/home/<user>/projects/bazlets",
)

load(
    "@com_googlesource_gerrit_bazlets//:gerrit_api.bzl",
    "gerrit_api",
)

gerrit_api()
