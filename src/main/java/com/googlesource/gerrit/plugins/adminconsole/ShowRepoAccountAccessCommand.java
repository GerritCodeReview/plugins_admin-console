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

import com.google.gerrit.common.data.AccessSection;
import com.google.gerrit.common.data.GlobalCapability;
import com.google.gerrit.common.data.Permission;
import com.google.gerrit.common.data.PermissionRule;
import com.google.gerrit.extensions.annotations.CapabilityScope;
import com.google.gerrit.extensions.annotations.RequiresCapability;
import com.google.gerrit.extensions.common.GroupInfo;
import com.google.gerrit.reviewdb.client.Account;
import com.google.gerrit.reviewdb.client.Account.Id;
import com.google.gerrit.reviewdb.client.Project;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.account.AccountResolver;
import com.google.gerrit.server.account.AccountResource;
import com.google.gerrit.server.git.meta.MetaDataUpdate;
import com.google.gerrit.server.project.ProjectConfig;
import com.google.gerrit.server.restapi.account.GetGroups;
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

@RequiresCapability(value = GlobalCapability.ADMINISTRATE_SERVER, scope = CapabilityScope.CORE)
@CommandMetaData(
    name = "show-repo-account-access",
    description = "Displays user's access on a specific repository")
public final class ShowRepoAccountAccessCommand extends SshCommand {

  @Argument(usage = "project to show access for?")
  private String projectName = "";

  @Option(
      name = "--user",
      usage =
          "User information to find: LastName,\\ Firstname,  email@address.com, account id or an user name. "
              + "Be sure to double-escape spaces, for example: \"show-repo-account-access All-Projects --user Last,\\\\ First\"")
  private String name = "";

  @Option(name = "-w", usage = "display without line width truncation")
  private boolean wide;

  @Inject
  ShowRepoAccountAccessCommand(
      MetaDataUpdate.Server metaDataUpdateFactory,
      Provider<GetGroups> accountGetGroups,
      AccountResolver accountResolver,
      IdentifiedUser.GenericFactory userFactory) {
    this.metaDataUpdateFactory = metaDataUpdateFactory;
    this.accountGetGroups = accountGetGroups;
    this.accountResolver = accountResolver;
    this.userFactory = userFactory;
  }

  private final MetaDataUpdate.Server metaDataUpdateFactory;
  private final AccountResolver accountResolver;
  private final Provider<GetGroups> accountGetGroups;
  private final IdentifiedUser.GenericFactory userFactory;
  private int columns = 80;
  private int permissionGroupWidth;

  @Override
  public void run() throws UnloggedFailure, Failure, Exception {
    Account account;
    String sectionNameFormatter = "  %-25s\n";
    String ruleNameFormatter = "    %-15s\n ";
    String permissionNameFormatter = "      %5s %9s %s\n";

    Boolean userHasPermissionsInSection = false;
    Boolean userHasPermissionsInProject = false;

    if (projectName.isEmpty()) {
      throw new UnloggedFailure(1, "Please specify a project to show access for");
    }

    if (name.isEmpty()) {
      throw new UnloggedFailure(
          1,
          "You need to tell me who to find:  LastName,\\\\ Firstname, email@address.com, account id or an user name.  "
              + "Be sure to double-escape spaces, for example: \"show-repo-account-access All-Projects --user Last,\\\\ First\"");
    }
    Set<Id> idList = accountResolver.findAll(name);
    if (idList.isEmpty()) {
      throw new UnloggedFailure(
          1,
          "No accounts found for your query: \""
              + name
              + "\""
              + " Tip: Try double-escaping spaces, for example: \"--user Last,\\\\ First\"");
    }

    Project.NameKey nameKey = new Project.NameKey(projectName);

    try {
      MetaDataUpdate md = metaDataUpdateFactory.create(nameKey);
      ProjectConfig config;
      config = ProjectConfig.read(md);

      permissionGroupWidth = wide ? Integer.MAX_VALUE : columns - 9 - 5 - 9;

      for (Id id : idList) {
        userHasPermissionsInProject = false;
        account = accountResolver.find(id.toString());
        stdout.println("Full name:         " + account.getFullName());
        // Need to know what groups the user is in. This is not a great
        // solution, but it does work.
        List<GroupInfo> groupInfos =
            accountGetGroups.get().apply(new AccountResource(userFactory.create(id)));
        HashSet<String> groupHash = new HashSet<>();

        for (GroupInfo groupInfo : groupInfos) {
          groupHash.add(groupInfo.name);
        }

        for (AccessSection accessSection : config.getAccessSections()) {
          StringBuilder sb = new StringBuilder();
          sb.append((String.format(sectionNameFormatter, accessSection.getName().toString())));
          // This is a solution to prevent displaying a section heading unless
          // the user has permissions for it
          // not the best solution, but I haven't been able to find
          // "Is user a member of this group" based on the information I have
          // in a more efficient manner yet.
          userHasPermissionsInSection = false;
          for (Permission permission : accessSection.getPermissions()) {

            for (PermissionRule rule : permission.getRules()) {

              if (groupHash.contains(rule.getGroup().getName())) {
                sb.append(String.format(ruleNameFormatter, permission.getName()));
                sb.append(
                    String.format(
                        permissionNameFormatter,
                        (!rule.getMin().equals(rule.getMax()))
                            ? "" + rule.getMin() + " " + rule.getMax()
                            : rule.getAction(),
                        (permission.getExclusiveGroup() ? "EXCLUSIVE" : ""),
                        format(rule.getGroup().getName())));
                userHasPermissionsInSection = true;
              }
            }
          }

          if (userHasPermissionsInSection) {
            stdout.print(sb.toString());

            userHasPermissionsInProject = true;
          }
        }

        if (!userHasPermissionsInProject) {
          stdout.println("  No access found for this user on this repository");
        }
      }
    } catch (RepositoryNotFoundException e) {
      throw new UnloggedFailure(1, "Repository not found");
    }
  }

  private String format(String s) {
    if (s.length() < permissionGroupWidth) {
      return s;
    }
    return s.substring(0, permissionGroupWidth);
  }
}
