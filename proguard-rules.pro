# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Please add these rules to your existing keep rules in order to suppress warnings.

# This is generated automatically by the Android Gradle plugin.

#model
-keep class com.youhajun.ui.models.** { *; }
-keep class com.youhajun.domain.models.** { *; }
-keep class com.youhajun.data.models.** { *; }
-keep class com.youhajun.ui.utils.webRtc.models.** { *; }

#debugable
-keepattributes LineNumberTable,SourceFile #소스파일, 라인 정보 유지
-renamesourcefileattribute SourceFile
#-dontobfuscate                              #난독화를 수행하지 않도록 함

#buildError
-dontwarn org.videolan.R$id
-dontwarn java.lang.invoke.StringConcatFactory

#kakao, google social login
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-keep class com.google.googlesignin.** { *; }