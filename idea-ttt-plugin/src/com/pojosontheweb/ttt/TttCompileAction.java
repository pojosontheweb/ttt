package com.pojosontheweb.ttt;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TttCompileAction extends AnAction {

    // If you register the action from Java code, this constructor is used to set the menu item name
    // (optionally, you can specify the menu description and an icon to display next to the menu item).
    // You can omit this constructor when registering the action in the plugin.xml file.
    public TttCompileAction() {
        // Set the menu item name.
        super("Compile _Ttt", "Compile TTT templates", TttIcons.FILE);
        // Set the menu item name, description and icon.
        // super("Text _Boxes","Item description",IconLoader.getIcon("/Mypackage/icon.png"));
    }

    public void actionPerformed(AnActionEvent event) {
        // compile all ttt files in project
        Project project = event.getData(PlatformDataKeys.PROJECT);
        compileTemplates(project);
    }

    public static void compileTemplates(Project project) {
        if (project!=null) {
            Module[] modules = ModuleManager.getInstance(project).getModules();
            for (Module m : modules) {
                TttModuleComponent tttModuleComponent = m.getComponent(TttModuleComponent.class);
                if (tttModuleComponent != null && tttModuleComponent.isEnabled()) {
                    // get target gen path
                    String target = tttModuleComponent.getTargetPath();
                    if (target == null) {
                        target = "ttt-gen";
                    }
                    String fullTarget = project.getBasePath() + File.separator + target;
                    // rm previously generated code and collect
                    // source roots to be visited
                    VirtualFile[] srcRoots = ModuleRootManager.getInstance(m).getSourceRoots();
                    List<VirtualFile> rootsToVisit = new ArrayList<VirtualFile>();
                    VirtualFile targetDir = null;
                    for (VirtualFile srcRoot : srcRoots) {
                        if (srcRoot.getPath().equals(fullTarget)) {
                            // remove all files in target
                            for (VirtualFile child : srcRoot.getChildren()) {
                                FileUtil.delete(new File(child.getPath()));
                            }
                            targetDir = srcRoot;
                        } else {
                            // collect this src root for later...
                            rootsToVisit.add(srcRoot);
                        }
                    }

                    final VirtualFile td = targetDir;

                    if (td != null) {

                        // generate base class
                        try {
                            TttCompiler.generateTemplateBaseClass(new File(targetDir.getPath()));
                        } catch (Exception e) {
                            // TODO handle error ?
                            throw new RuntimeException(e);
                        }

                        // now visit the source roots
                        List<File> generatedFiles = new ArrayList<>();
                        for (VirtualFile srcRoot : rootsToVisit) {
                            final String srcRootPath = srcRoot.getPath();
                            // find all ".ttt" files under this...
                            VfsUtilCore.visitChildrenRecursively(srcRoot, new VirtualFileVisitor() {
                                @Override
                                public boolean visitFile(VirtualFile file) {
                                    if (file.getName().endsWith(".ttt")) {
                                        try {
                                            Reader in = new InputStreamReader(file.getInputStream());
                                            String targetDir = td.getPath();
                                            String relPath = file.getPath().substring(srcRootPath.length());
                                            String absPath = targetDir + relPath;
                                            absPath = absPath.replace(".ttt", ".java");
                                            File f = new File(absPath);
                                            File parent = f.getParentFile();
                                            if (!parent.exists()) {
                                                parent.mkdirs();
                                            }
                                            Writer out = new FileWriter(f);
                                            String fqn = relPath
                                                .substring(1)
                                                .replace(File.separatorChar, '.')
                                                .replace(".ttt", "");
                                            try {
                                                TttCompiler.compile(in, out, fqn);
                                                generatedFiles.add(f);
                                            } finally {
                                                out.close();
                                            }
                                        } catch (Exception e) {
                                            // TODO handle error
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    return true;
                                }
                            });
                        }

                        td.refresh(true, true, () -> {
                            String msg = generatedFiles.size() + " file(s) generated to " + td.getPath();
                            final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
                            if (statusBar != null) {
                                statusBar.setInfo(msg);
                            }
                        });
                    }

                }
            }
        }
    }

}