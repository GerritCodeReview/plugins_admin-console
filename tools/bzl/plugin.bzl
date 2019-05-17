load(
    "@com_googlesource_gerrit_bazlets//:gerrit_plugin.bzl",
    _gerrit_plugin = "gerrit_plugin",
    _plugin_deps = "PLUGIN_DEPS",
)

PLUGIN_DEPS = _plugin_deps
gerrit_plugin = _gerrit_plugin
