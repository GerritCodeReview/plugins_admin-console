workspace(name = "admin_console")

load("//:bazlets.bzl", "load_bazlets")

load_bazlets(
    commit = "a010a539824bd467059f414412a47b80101352d7",
    #local_path = "/home/<user>/projects/bazlets",
)

load(
    "@com_googlesource_gerrit_bazlets//:gerrit_api.bzl",
    "gerrit_api",
)

gerrit_api()
