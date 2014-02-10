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

import java.io.IOException;

import org.eclipse.jgit.errors.ConfigInvalidException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import com.google.gerrit.common.data.AccessSection;
import com.google.gerrit.common.data.GlobalCapability;
import com.google.gerrit.common.data.Permission;
import com.google.gerrit.common.data.PermissionRule;
import com.google.gerrit.extensions.annotations.CapabilityScope;
import com.google.gerrit.extensions.annotations.RequiresCapability;
import com.google.gerrit.reviewdb.client.Project;
import com.google.gerrit.server.git.MetaDataUpdate;
import com.google.gerrit.server.git.ProjectConfig;
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.inject.Inject;

@RequiresCapability(value=GlobalCapability.ADMINISTRATE_SERVER, scope=CapabilityScope.CORE)
@CommandMetaData(name = "show-repo-access", description = "Displays access on a specific repository")
public final class ShowRepoAccessCommand extends SshCommand {

  @Argument(usage = "project to show access for?")
  private String projectName = "";

  @Option(name = "-w", usage = "display without line width truncation")
  private boolean wide;

  @Inject
  ShowRepoAccessCommand(final MetaDataUpdate.Server metaDataUpdateFactory)
      throws ConfigInvalidException, IOException {
    this.metaDataUpdateFactory = metaDataUpdateFactory;
  }

  private final MetaDataUpdate.Server metaDataUpdateFactory;

  private int columns = 80;
  private int permissionGroupWidth;

  @Override
  public void run() throws UnloggedFailure, Failure, IOException,
      ConfigInvalidException {
    // space indented Strings to be used as format for String.format() later
    String sectionNameFormatter = "  %-25s\n";
    String ruleNameFormatter = "    %-15s\n ";
    String permissionNameFormatter = "      %5s %9s %s\n";

    if (projectName.isEmpty()) {
      throw new UnloggedFailure(1, "Please specify a project to show access for");
    }
    final Project.NameKey nameKey = new Project.NameKey(projectName);

    permissionGroupWidth = wide ? Integer.MAX_VALUE : columns - 9 - 5 - 9;

    ProjectConfig config;
    try {
      MetaDataUpdate md = metaDataUpdateFactory.create(nameKey);
      config = ProjectConfig.read(md);
      for (AccessSection accessSection : config.getAccessSections()) {

        stdout.print((String.format(sectionNameFormatter, accessSection
            .getName().toString())));

        for (Permission permission : accessSection.getPermissions()) {

          for (PermissionRule rule : permission.getRules()) {
            stdout
                .print(String.format(ruleNameFormatter, permission.getName()));
            stdout.print(String.format(permissionNameFormatter,
                (rule.getMin() != rule.getMax()) ? "" + rule.getMin() + " "
                    + rule.getMax() : rule.getAction(),
                (permission.getExclusiveGroup() ? "EXCLUSIVE" : ""),
                format(rule.getGroup().getName())));
          }
        }
      }
    } catch (RepositoryNotFoundException e) {
      throw new UnloggedFailure(1, "Repository not found");
    }
  }

  private String format(final String s) {
    if (s.length() < permissionGroupWidth) {
      return s;
    } else {
      return s.substring(0, permissionGroupWidth);
    }
  }
}
