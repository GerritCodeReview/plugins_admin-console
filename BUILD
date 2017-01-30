load("//tools/bzl:plugin.bzl", "gerrit_plugin")

gerrit_plugin(
  name = "admin-console",
  srcs = glob(["src/main/java/**/*.java"]),
  resources = glob(["src/main/resources/**/*"]),
  manifest_entries = [
    "Gerrit-PluginName: admin-console",
    "Gerrit-SshModule: com.googlesource.gerrit.plugins.adminconsole.AdminConsoleCommandModule",
    "Implementation-Title: Plugin admin-console",
    "Implementation-URL: https://gerrit-review.googlesource.com/#/admin/projects/plugins/admin-console",
  ]
)
