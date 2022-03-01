// Copyright (C) 2022 The Android Open Source Project
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

import static com.google.gerrit.sshd.CommandMetaData.Mode.MASTER_OR_SLAVE;

import com.google.gerrit.entities.Project;
import com.google.gerrit.extensions.restapi.AuthException;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.permissions.PermissionBackend;
import com.google.gerrit.server.permissions.PermissionBackendException;
import com.google.gerrit.server.permissions.ProjectPermission;
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.inject.Inject;
import java.io.IOException;
import java.util.Set;
import org.eclipse.jgit.errors.ConfigInvalidException;
import org.kohsuke.args4j.Option;

@CommandMetaData(
    name = "check-project-visibility",
    description = "Check project visibility to a specific user",
    runsAt = MASTER_OR_SLAVE)
public class CheckProjectVisibility extends SshCommand {
  @Inject private AdminConsoleUtil adminConsoleUtil;

  @Inject private PermissionBackend permissionBackend;

  @Option(
      name = "--project",
      aliases = {"-p"},
      metaVar = "PROJECT",
      required = true,
      usage = "project name to check")
  private String projectName;

  @Option(
      name = "--user",
      aliases = {"-u"},
      metaVar = "USER",
      required = true,
      usage =
          "User information to find: LastName,\\ Firstname,  email@address.com, account id or an user name. "
              + "Be sure to double-escape spaces, for example: \"check-project-visibility -p All-Projects --user Last,\\\\ First\"")
  private String userName;

  @Override
  protected void run() throws Failure, ConfigInvalidException, IOException {
    Set<IdentifiedUser> userList = adminConsoleUtil.getUserList(userName);

    for (IdentifiedUser user : userList) {
      Project.NameKey nameKey = Project.nameKey(adminConsoleUtil.getProjectName(projectName));

      try {
        permissionBackend.user(user).project(nameKey).check(ProjectPermission.READ);
        stdout.print(String.format("%s true\n", user.getAccount().fullName()));
      } catch (AuthException | PermissionBackendException e) {
        stdout.print(String.format("%s false\n", user.getAccount().fullName()));
      }
    }
  }
}
