load("//tools/bzl:plugin.bzl", "PLUGIN_DEPS")
load("//tools/bzl:classpath.bzl", "classpath_collector")

CP_DEPS = PLUGIN_DEPS + [
    "//:admin-console__plugin",
]

java_library(
    name = "classpath",
    runtime_deps = CP_DEPS,
)

classpath_collector(
    name = "main_classpath_collect",
    deps = CP_DEPS,
)
