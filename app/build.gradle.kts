plugins {
    id("com.android.application")
}

android {
    namespace = "com.amzi.cnews3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.amzi.cnews3"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0-alpha12")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.razorpay:checkout:1.6.34")
    implementation("com.google.firebase:firebase-ads:22.4.0")
    implementation("com.google.firebase:firebase-config:21.4.1")
    implementation("com.firebase:firebase-client-android:2.5.2")
    implementation("com.google.firebase:firebase-messaging:23.2.1")
    implementation("com.google.firebase:firebase-storage:20.2.1")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-database:20.2.2")
    implementation("com.google.firebase:firebase-auth:22.1.2")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.firebaseui:firebase-ui-database:8.0.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.daimajia.easing:library:2.4@aar")
    implementation("com.daimajia.androidanimations:library:2.4@aar")
    //noinspection RiskyLibrary
    implementation("com.google.android.gms:play-services-ads:22.4.0")

}