package com.amzi.cnews3.utility;

public interface wallpaperListener {
    void onFreeWallpaperFound(String message);

    void onPremiumWallpaperFound(String message);

    void onUnlockableWallpaperFound(String message);

    void onSavedWallpaperFound(String error);

    void onUnsavedWallpaperFound(String error);

    void onFavouriteWallpaperFound(String message);

    void onUnfavouriteWallpaperFound(String message);

    void onWallpaperUnlocked(String message);

    void onWallpaperFavourited(String message);

    void onWallpaperUnfavourited(String message);

    void onWallpaperDownloaded(String message);

    void onWallpaperDownloadingProgress(String progress);

    void onWallpaperChanged(String currentInfo);


}
