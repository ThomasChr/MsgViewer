package at.redeye.FrameWork.base;

public class CloseSubDialogHelper
{
    private final BaseDialogBase parent;

    CloseSubDialogHelper(BaseDialogBase parent)
    {
        this.parent = parent;
    }

    public void closeSubDialog( final BaseDialogBase dialog )
    {
        parent.registerOnCloseListener(dialog::close);
    }
}
