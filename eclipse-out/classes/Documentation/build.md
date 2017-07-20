Build
=====

This plugin is built using Bazel.
Only the Gerrit in-tree build is supported.


```
  bazel build plugins/@PLUGIN@
```

The output is created in

```
  bazel-genfiles/plugins/@PLUGIN@/@PLUGIN@.jar
```

This project can be imported into the Eclipse IDE.
Add the plugin name to the `CUSTOM_PLUGINS` set in
Gerrit core in `tools/bzl/plugins.bzl`, and execute:

```
  ./tools/eclipse/project.py
```

How to build the Gerrit Plugin API is described in the [Gerrit
documentation](../../../Documentation/dev-bazel.html#_extension_and_plugin_api_jar_files).

[Back to @PLUGIN@ documentation index][index]

[index]: index.html

