package info.hebbeker.david.memorex;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private ListPreference speedPreference;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);
        speedPreference = (ListPreference) findPreference(getString(R.string.preference_speed_list));
        speedPreference.setSummary(speedPreference.getEntry());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause()
    {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences,
                                          final String key)
    {
        if (key.equals(getString(R.string.preference_speed_list)))
        {
            // Set summary to be the user-description for the selected value
            speedPreference.setSummary(speedPreference.getEntry());
            SymbolButton.updateSignallingDurationSettings(Long.parseLong(speedPreference.getValue()));
        }
    }
}
