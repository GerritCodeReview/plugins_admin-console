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
import com.google.gerrit.server.ProjectUtil;
import com.google.gerrit.server.account.AccountResolver;
import com.google.gerrit.server.account.AccountResolver.UnresolvableAccountException;
import com.google.gerrit.server.permissions.PermissionBackend;
import com.google.gerrit.server.permissions.PermissionBackendException;
import com.google.gerrit.server.permissions.ProjectPermission;
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.inject.Inject;
import java.io.IOException;
import org.eclipse.jgit.errors.ConfigInvalidException;
import org.kohsuke.args4j.Option;

@CommandMetaData(
    name = "check-project-visibility",
    description = "Check project visibility to a specific user",
    runsAt = MASTER_OR_SLAVE)
public class CheckProjectVisibility extends SshCommand {
  @Inject private AccountResolver accountResolver;

  @Inject private IdentifiedUser.GenericFactory userFactory;

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
      usage = "user for which the groups should be listed")
  private String userName;

  @Override
  protected void run() throws Failure {
    IdentifiedUser userAccount;
    try {
      userAccount = accountResolver.resolve(userName).asUniqueUser();
    } catch (IOException | ConfigInvalidException | UnresolvableAccountException e) {
      throw die(e);
    }

    if (userAccount == null) {
      stdout.print("No single user could be found when searching for: " + userName + '\n');
      stdout.flush();
      return;
    }

    while (projectName.endsWith("/")) {
      projectName = projectName.substring(0, projectName.length() - 1);
    }

    while (projectName.startsWith("/")) {
      // Be nice and drop the leading "/" if supplied by an absolute path.
      // We don't have a file system hierarchy, just a flat namespace in
      // the database's Project entities. We never encode these with a
      // leading '/' but users might accidentally include them in Git URLs.
      //
      projectName = projectName.substring(1);
    }

    String nameWithoutSuffix = ProjectUtil.stripGitSuffix(projectName);
    Project.NameKey nameKey = Project.nameKey(nameWithoutSuffix);
    IdentifiedUser user = userFactory.create(userAccount.getAccountId());

    try {
      permissionBackend.user(user).project(nameKey).check(ProjectPermission.READ);
      stdout.print("true\n");
    } catch (AuthException | PermissionBackendException e) {
      stdout.print("false\n");
    }
  }
}
