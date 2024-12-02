package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;

import java.io.File;

public abstract class BaseWin extends BaseDialog implements MainDialog {
    private String dialog_id;
    protected final FileChooser fileChooser;

    public BaseWin(Root root) {
        super(root, root.MlM(root.getAppName()));
        fileChooser = new FileChooser(this);
    }

    @Override
    public String getUniqueDialogIdentifier() {
        if (dialog_id == null)
            dialog_id = super.getUniqueDialogIdentifier();

        return dialog_id;
    }

    public abstract void openFile(File file);
}
