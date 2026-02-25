# HiveKey ProGuard Rules

# Keep Cloudinary classes
-keep class com.cloudinary.** { *; }
-dontwarn com.cloudinary.**

# Keep Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule { <init>(...); }
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
    *** rewind();
}

# Keep data classes used with Cloudinary callbacks
-keepclassmembers class * {
    @com.cloudinary.android.callback.* <methods>;
}