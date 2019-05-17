load(
    "@com_googlesource_gerrit_bazlets//:gerrit_plugin.bzl",
    _deps = "PLUGIN_DEPS",
    _plugin = "gerrit_plugin",
)

PLUGIN_DEPS = _deps
gerrit_plugin = _plugin

