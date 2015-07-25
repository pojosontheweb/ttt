package com.pojosontheweb.ttt;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by vankeisb on 25/07/15.
 */
public class TttApplicationComponent implements ApplicationComponent {

    private final Logger LOG = Logger.getInstance(TttApplicationComponent.class.getName());

    @Override
    public void initComponent() {
        VirtualFileManager.getInstance().addVirtualFileListener(new TttCompileOnSaveListener());
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "TttApplicationComponent";
    }

}
