// Copyright (C) 2012 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.googlesource.gerrit.plugins.adminconsole;

import com.google.gerrit.sshd.PluginCommandModule;

public class AdminConsoleCommandModule extends PluginCommandModule {
  @Override
  protected void configureCommands() {
    command(ShowAccountCommand.class);
    command(ListUsersCommand.class);
    command(GetFullPathCommand.class);
    alias("show-account", ShowAccountCommand.class);
    alias("show-repo-account-access", ShowRepoAccountAccessCommand.class);
    alias("show-repo-access", ShowRepoAccessCommand.class);
    alias("get-path", GetFullPathCommand.class);
  }
}
