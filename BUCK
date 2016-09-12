include_defs('//bucklets/gerrit_plugin.bucklet')
include_defs('//bucklets/java_sources.bucklet')

SOURCES = glob(['src/main/java/**/*.java'])
RESOURCES = glob(['src/main/resources/**/*'])

gerrit_plugin(
  name = 'admin-console',
  srcs = SOURCES,
  resources = RESOURCES,
  manifest_entries = [
    'Gerrit-PluginName: admin-console',
    'Gerrit-SshModule: com.googlesource.gerrit.plugins.adminconsole.AdminConsoleCommandModule',
    'Implementation-Title: Plugin admin-console',
    'Implementation-URL: https://gerrit-review.googlesource.com/#/admin/projects/plugins/admin-console',
  ]
)

java_library(
  name = 'classpath',
  deps = GERRIT_PLUGIN_API + [
    ':admin-console__plugin',
  ],
)

java_sources(
  name = 'admin-console-sources',
  srcs = SOURCES + RESOURCES,
)
