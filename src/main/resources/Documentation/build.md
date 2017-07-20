# Build

This plugin can be built with Bazel, and two build modes are supported:

* Standalone
* In Gerrit tree

Standalone build mode is recommended, as this mode doesn't require local Gerrit
tree to exist. Moreover, there are some limitations and additional manual steps
required when building in Gerrit tree mode (see corresponding sections).

## Build standalone

To build the plugin, issue the following command:

```
  bazel build @PLUGIN@
```

The output is created in

```
  bazel-genfiles/@PLUGIN@.jar
```

To package the plugin sources run:

```
  bazel build lib@PLUGIN@__plugin-src.jar
```

The output is created in:

```
  bazel-bin/lib@PLUGIN@__plugin-src.jar
```

This project can be imported into the Eclipse IDE:

```
  ./tools/eclipse/project.sh
```

## Build in Gerrit tree

Clone or link this plugin to the plugins directory of Gerrit's
source tree. From Gerrit source tree issue the command:

```
  bazel build plugins/@PLUGIN@
```

Note that due to a [known issue in Bazel][bazelissue], if the plugin
has previously been built in standalone mode, it is necessary to clean
the workspace before building in-tree:

```
  cd plugins/@PLUGIN@
  bazel clean --expunge
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
[bazelissue]: https://github.com/bazelbuild/bazel/issues/2797
