include_defs('//lib/maven.defs')

gerrit_plugin(
  name = 'admin-console',
  srcs = glob(['src/main/java/**/*.java']),
  resources = glob(['src/main/resources/**/*']),
  deps = [
    '//gerrit-lucene:lucene',
  ],
  manifest_entries = [
    'Gerrit-PluginName: admin-console',
    'Gerrit-SshModule: com.googlesource.gerrit.plugins.adminconsole.AdminConsoleCommandModule'
  ]
)
