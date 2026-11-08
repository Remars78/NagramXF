package tw.nekomimi.nekogram.settings;

import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.view.View;

import org.telegram.messenger.R;
import org.telegram.ui.Components.RecyclerListView;

import tw.nekomimi.nekogram.config.CellGroup;
import tw.nekomimi.nekogram.config.cell.AbstractConfigCell;
import tw.nekomimi.nekogram.config.cell.ConfigCellHeader;
import tw.nekomimi.nekogram.config.cell.ConfigCellTextCheck;
import xyz.nextalone.nagram.NaConfig;

public class NekoCustomSettingsActivity extends BaseNekoXSettingsActivity {

    private ListAdapter listAdapter;
    private final CellGroup cellGroup = new CellGroup(this);

    // Media & Photo Picker
    private final AbstractConfigCell headerMedia = cellGroup.appendCell(new ConfigCellHeader(getString(R.string.ChatGallery)));
    private final AbstractConfigCell useGooglePhotoPickerRow = cellGroup.appendCell(new ConfigCellTextCheck(
            NaConfig.INSTANCE.getUseGooglePhotoPicker(),
            getString(R.string.UseGooglePhotoPickerNotice)
    ));

    @Override
    protected RecyclerListView.SelectionAdapter getListAdapter() {
        return listAdapter;
    }

    @Override
    protected CellGroup getCellGroup() {
        return cellGroup;
    }

    @Override
    protected String getSettingsPrefix() {
        return "custom";
    }

    @Override
    public String getTitle() {
        return getString(R.string.CustomSettings);
    }

    @Override
    public View createView(Context context) {
        View superView = super.createView(context);
        listAdapter = new ListAdapter(context);
        listView.setAdapter(listAdapter);
        setupDefaultListeners();
        return superView;
    }

    private class ListAdapter extends BaseListAdapter {
        public ListAdapter(Context context) {
            super(context);
        }
    }
}
