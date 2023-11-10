plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("stringfog")
    id("android-junk-code")
}

stringfog {
    // 开关
    enable = true
    // 加解密库的实现类路径，需和上面配置的加解密算法库一致。
    implementation = "com.github.megatronking.stringfog.xor.StringFogImpl"
    // 可选：指定需加密的代码包路径，可配置多个，未指定将默认全部加密。
    fogPackages = arrayOf("com.aomiga.focus")
    kg = com.github.megatronking.stringfog.plugin.kg.HardCodeKeyGenerator("erfinder")
}

androidJunkCode {
    variantConfig {
        create("release") {
            //注意：这里的release是变体名称，如果没有设置productFlavors就是buildType名称，如果有设置productFlavors就是flavor+buildType，例如（freeRelease、proRelease）
            packageBase = "lais.focus.finder"  //生成java类根包名
            packageCount = 31 //生成包数量
            activityCountPerPackage = 3 //每个包下生成Activity类数量
            excludeActivityJavaFile = false
            //是否排除生成Activity的Java文件,默认false(layout和写入AndroidManifest.xml还会执行)，主要用于处理类似神策全埋点编译过慢问题
            otherCountPerPackage = 51  //每个包下生成其它类的数量
            methodCountPerClass = 26  //每个类下生成方法数量
            resPrefix = "hsia_"  //生成的layout、drawable、string等资源名前缀
            drawableCount = 331  //生成drawable资源数量
            stringCount = 312  //生成string数量
        }
    }
}

android {
    namespace = "com.aomiga.focus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aomiga.focus"
        minSdk = 24
        targetSdk = 33
        versionCode = 3
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //重命名打包文件，对apk和aab都生效
        setProperty("archivesBaseName", "${applicationId}-${versionName}-${versionCode}")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-process:2.6.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.preference:preference:1.1.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    implementation("com.otaliastudios:cameraview:2.7.2")
    implementation("com.github.chrisbanes:PhotoView:latest.release")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.megatronking.stringfog:xor:5.0.0")

    // PictureSelector basic (Necessary)
    implementation("io.github.lucksiege:pictureselector:v3.11.1")

    implementation("com.blankj:utilcodex:1.31.1")


}