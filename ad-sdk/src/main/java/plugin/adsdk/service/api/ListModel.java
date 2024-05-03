package plugin.adsdk.service.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Despicable on 7/11/2020.
 */
public class ListModel {

    //app controls
    @SerializedName("pkgName")
    @Expose
    public String packageName = "";
    @SerializedName("account_name")
    @Expose
    public String accountName = "";
    @SerializedName("privacy_policy_url")
    @Expose
    public String privacyPolicyUrl = "";
    @SerializedName("base64_in_app_key")
    @Expose
    public String base64InAppKey = "";

    //app-open controls
    @SerializedName("on_demand_app_open")
    @Expose
    public boolean onDemandAppOpen = false;
    @SerializedName("initial_app_open")
    @Expose
    public boolean initialAppOpen = false;
    @SerializedName("count_app_open_interval")
    @Expose
    public boolean countAppOpenInterval = false;
    @SerializedName("block_initial_app_open")
    @Expose
    public boolean blockInitialAppOpen = false;
    @SerializedName("app_open_optimization")
    @Expose
    public String appOpenOptimization = "";
    @SerializedName("allow_ad_interval_app_open")
    @Expose
    public boolean allowAdIntervalAppOpen = false;
    @SerializedName("ad_interval_time_app_open")
    @Expose
    public int adIntervalTimeAppOpen = 0;

    //inter controls
    @SerializedName("activity_count")
    @Expose
    public int activityCount = 0;
    @SerializedName("ad_on_back")
    @Expose
    public boolean adOnBack = false;
    @SerializedName("preload_interstitial")
    @Expose
    public boolean preloadInterstitial = false;
    @SerializedName("replace_inter_with_app_open")
    @Expose
    public boolean replaceInterWithAppOpen = false;
    @SerializedName("alternate_ads")
    @Expose
    public boolean alternateAds = false;
    @SerializedName("allow_ad_interval_interstitial")
    @Expose
    public boolean allowAdIntervalInterstitial = false;
    @SerializedName("ad_interval_time_interstitial")
    @Expose
    public int adIntervalTimeInterstitial = 0;

    //native controls
    @SerializedName("list_native_count")
    @Expose
    public int listNativeCount = 6;
    @SerializedName("native_height_percentage")
    @Expose
    public int nativeHeightPercentage;
    @SerializedName("preload_native")
    @Expose
    public boolean preloadNative;
    @SerializedName("flashing_native")
    @Expose
    public boolean flashingNative = false;
    @SerializedName("forced_native_color")
    @Expose
    public String forcedNativeColor = "";

    //extra screen controls
    @SerializedName("screen_count")
    @Expose
    public String screenCount;
    @SerializedName("repeat_screen_count")
    @Expose
    public int repeatScreenCount;
    public List<String> startScreens = new ArrayList<>();
    public int startScreenRepeatCount = 0;

    //extra controls
    @SuppressWarnings("unused")
    @SerializedName("website_link")
    @Expose
    public List<String> webSiteLink = new ArrayList<>();
    @SuppressWarnings("unused")
    @SerializedName("video")
    @Expose
    public String video = "";

    //qureka controls
    @SerializedName("qureka")
    @Expose
    public boolean qurekaEnabled = false;
    @SerializedName("qureka_url")
    @Expose
    public String qurekaURL = "https://425.live.qureka.com/";
    @SerializedName("qureka_buttons")
    @Expose
    public String qurekaButtons = "";

    @SerializedName("forced_app_update")
    @Expose
    public boolean forcedAppUpdate = false;

    /***********limited controls***********/

    @SerializedName("limited_countries")
    @Expose
    public String limitedCountries = "";

    //limited app-open controls
    @SerializedName("limited_on_demand_app_open")
    @Expose
    public boolean limitedOnDemandAppOpen = false;
    @SerializedName("limited_initial_app_open")
    @Expose
    public boolean limitedInitialAppOpen = false;
    @SerializedName("limited_count_app_open_interval")
    @Expose
    public boolean limitedCountAppOpenInterval = false;
    @SerializedName("limited_block_initial_app_open")
    @Expose
    public boolean limitedBlockInitialAppOpen = false;
    @SerializedName("limited_app_open_optimization")
    @Expose
    public String limitedAppOpenOptimization = "";
    @SerializedName("limited_allow_ad_interval_app_open")
    @Expose
    public boolean limitedAllowAdIntervalAppOpen = false;
    @SerializedName("limited_ad_interval_time_app_open")
    @Expose
    public int limitedAdIntervalTimeAppOpen = 0;

    //limited inter controls
    @SerializedName("limited_activity_count")
    @Expose
    public int limitedActivityCount = 0;
    @SerializedName("limited_ad_on_back")
    @Expose
    public boolean limitedAdOnBack = false;
    @SerializedName("limited_preload_interstitial")
    @Expose
    public boolean limitedPreloadInterstitial = false;
    @SerializedName("limited_replace_inter_with_app_open")
    @Expose
    public boolean limitedReplaceInterWithAppOpen = false;
    @SerializedName("limited_alternate_ads")
    @Expose
    public boolean limitedAlternateAds = false;
    @SerializedName("limited_allow_ad_interval_interstitial")
    @Expose
    public boolean limitedAllowAdIntervalInterstitial = false;
    @SerializedName("limited_ad_interval_time_interstitial")
    @Expose
    public int limitedAdIntervalTimeInterstitial = 0;

    //limited native controls
    @SerializedName("limited_list_native_count")
    @Expose
    public int limitedListNativeCount = 6;
    @SerializedName("limited_native_height_percentage")
    @Expose
    public int limitedNativeHeightPercentage = 40;
    @SerializedName("limited_preload_native")
    @Expose
    public boolean limitedPreloadNative;
    @SerializedName("limited_flashing_native")
    @Expose
    public boolean limitedFlashingNative = false;
    @SerializedName("limited_forced_native_color")
    @Expose
    public String limitedForcedNativeColor = "";

    //limited extra screen controls
    @SerializedName("limited_screen_count")
    @Expose
    public String limitedScreenCount;
    @SerializedName("limited_repeat_screen_count")
    @Expose
    public int limitedRepeatScreenCount;

    //limited qureka controls
    @SerializedName("limited_qureka")
    @Expose
    public boolean limitedQurekaEnabled = false;
    @SerializedName("limited_qureka_buttons")
    @Expose
    public String limitedQurekaButtons = "";

    //ad ids
    @SerializedName("Admob")
    @Expose
    public CommonModel adMob = new CommonModel();

    public void migrateToNoAds() {
        screenCount = "";
        startScreens = Collections.emptyList();
        startScreenRepeatCount = 0;
        adMob.nativeAd = "";
        adMob.bannerAd = "";
        adMob.appOpenId = "";
        adMob.interstitialAd = "";
        adMob.videoAd = "";
        activityCount = 1000;
        adOnBack = false;
        onDemandAppOpen = false;
        flashingNative = false;
        limitedCountries = "";
        listNativeCount = 0;
        qurekaEnabled = false;
        blockInitialAppOpen = false;
        appOpenOptimization = "";
        alternateAds = false;
        allowAdIntervalAppOpen = false;
        adIntervalTimeInterstitial = 1000;
        allowAdIntervalInterstitial = false;
        adIntervalTimeAppOpen = 3600;
    }

    public void addLimits() {
        //ao
        onDemandAppOpen = limitedOnDemandAppOpen;
        initialAppOpen = limitedInitialAppOpen;
        countAppOpenInterval = limitedCountAppOpenInterval;
        blockInitialAppOpen = limitedBlockInitialAppOpen;
        appOpenOptimization = limitedAppOpenOptimization;
        allowAdIntervalAppOpen = limitedAllowAdIntervalAppOpen;
        adIntervalTimeAppOpen = limitedAdIntervalTimeAppOpen;
        //inter
        activityCount = limitedActivityCount;
        adOnBack = limitedAdOnBack;
        preloadInterstitial = limitedPreloadInterstitial;
        replaceInterWithAppOpen = limitedReplaceInterWithAppOpen;
        alternateAds = limitedAlternateAds;
        allowAdIntervalInterstitial = limitedAllowAdIntervalInterstitial;
        adIntervalTimeInterstitial = limitedAdIntervalTimeInterstitial;
        //native
        listNativeCount = limitedListNativeCount;
        nativeHeightPercentage = limitedNativeHeightPercentage;
        preloadNative = limitedPreloadNative;
        flashingNative = limitedFlashingNative;
        forcedNativeColor = limitedForcedNativeColor;
        //extras
        screenCount = limitedScreenCount;
        repeatScreenCount = limitedRepeatScreenCount;
        //qureka
        qurekaEnabled = limitedQurekaEnabled;
        qurekaButtons = limitedQurekaButtons;

    }
}