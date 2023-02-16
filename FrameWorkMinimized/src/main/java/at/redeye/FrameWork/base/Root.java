package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.translation.MLHelper;
import at.redeye.FrameWork.utilities.Storage;

import java.nio.file.Path;
import java.util.Locale;

public class Root {
    private static Root static_root;
    private final String app_name;
    private final String app_title;
    private final Setup setup;
    private final Plugins plugins;
    private final Path storage;
    private final Dialogs dialogs;
    private final MLHelper ml_helper;

    /**
     * language most of the aplication is programmed in
     * can be set in each dialog but if not set
     * here you can define the default
     * <p>
     * This is set to "en" by default.
     */
    private String base_language;

    /**
     * fallback language if the target language is not
     * available.
     * Eg: Dialogs are programmed in german, transalations
     * are available in english too. But on a french PC
     * we have no translatios. So whout should we do now?
     * Here you can define the default language which can be
     * diferrent from the language the dialog was translated
     * for.
     * <p>
     * This is set to "en" by default
     */
    private String default_language;

    /**
     * path to transaltion files as resource view
     * eg: /at/redeye/Zeiterfassung/resources/translations
     */
    private String language_resource_path;

    /**
     * language used in messages, can differ from
     * Locale.getDefault()
     */
    private String display_language;

    private String[] startupArgs;

    public Root(String app_name) {
        this(app_name, app_name);
    }

    public Root(String app_name, String app_title) {
        this.app_name = app_name;
        this.app_title = app_title;
        static_root = this;
        setup = new Setup(app_name);
        plugins = new Plugins(app_name);
        storage = Storage.getEphemeralStorage(this.app_name);
        dialogs = new Dialogs(this);
        ml_helper = new MLHelper(this);
    }

    public Setup getSetup() {
        return setup;
    }

    public void saveSetup() {
        ml_helper.saveMissingProps();
        setup.saveProps();
    }

    public Dialogs getDialogs() {
        return dialogs;
    }

    void appExit() {
        saveSetup();
        System.exit(0);
    }

    public String getAppName() {
        return app_name;
    }

    public String getAppTitle() {
        return app_title;
    }

    public Plugins getPlugins() {
        return plugins;
    }

    /**
     * language most of the aplication is programmed in
     * can be set in each dialog but if not set
     * here you can define the default
     * <p>
     * This is set to "en" by default.
     */
    public void setBaseLanguage(String language) {
        base_language = language;
    }

    /**
     * fallback language if the target language is not
     * available.
     * Eg: Dialogs are programmed in german, transalations
     * are available in english too. But on a french PC
     * we have no translatios. So whout should we do now?
     * Here you can define the default language which can be
     * diferrent from the language the dialog was translated
     * for.
     * <p>
     * This is set to "en" by default
     */
    public void setDefaultLanguage( String language )
    {
        default_language = language;
    }

    /**
     * fallback language if the target language is not
     * available.
     * Eg: Dialogs are programmed in german, transalations
     * are available in english too. But on a french PC
     * we have no translatios. So whout should we do now?
     * Here you can define the default language which can be
     * diferrent from the language the dialog was translated
     * for.
     * <p>
     * This is set to "en" by default
     */
    public String getDefaultLanguage()
    {
        if( default_language == null )
            default_language = "en";

        return default_language;
    }

    /**
     * language most of the aplication is programmed in
     * can be set in each dialog but if not set
     * here you can define the default
     * <p>
     * This is set to "en" by default.
     */
    public String getBaseLanguage()
    {
        if( base_language == null )
            base_language = "en";

        return base_language;
    }

    /**
     * path to transaltion files as resource view
     * eg: /at/redeye/Zeiterfassung/resources/translations
     */
    public void setLanguageTranslationResourcePath( String path )
    {
        language_resource_path = path;
        ml_helper.autoLoadCurrentLocale();
    }

    /**
     * @return language that should be used for translations
     * can be Locale.getDefault() or something userdefined
     */
    public String getDisplayLanguage()
    {
        if (display_language == null)
        {
            Setup setup = getSetup();

            display_language = Locale.getDefault().toString();

            if (setup == null) {
                return display_language;
            }

            String lang = setup.getConfig(BaseAppConfigDefinitions.DisplayLanguage);

            if (lang != null && lang.trim().isEmpty()) {
                return display_language;
            }

            display_language = lang;
        }

        return display_language;
    }

    /**
     * @return path to transaltion files as resource view
     * eg: /at/redeye/Zeiterfassung/resources/translations
     * or null if not set
     */
    public String getLanguageTranslationResourcePath() {
        return language_resource_path;
    }

    public String MlM(String message) {
        return ml_helper.MlM(message);
    }

    /**
     * load a MlM file for a spacific class
     *
     * @param impl_locale the locale the class was originaly implemented
     *                    eg "de" for german
     */
    public void loadMlM4Class(Object obj, String impl_locale) {
        ml_helper.autoLoadFile4Class(obj, getDisplayLanguage(), impl_locale);
    }

    /**
     * load a MlM file for a spacific class
     */
    public void loadMlM4ClassName(String name, String impl_locale) {
        ml_helper.autoLoadFile4ClassName(name, getDisplayLanguage(), impl_locale);

    }

    /**
     * only use this in case of emergency
     *
     * @return the last instace of the root class
     */
    public static Root getLastRoot() {
        return static_root;
    }

    public Path getStorage() {
        return storage;
    }

    public void setStartupArgs(String[] args) {
        startupArgs = args;
    }

    public String[] getStartupArgs() {
        return startupArgs;
    }
}
