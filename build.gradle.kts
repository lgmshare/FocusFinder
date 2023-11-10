// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

buildscript {
    dependencies {
        classpath("com.github.megatronking.stringfog:gradle-plugin:5.1.0")
        // 选用加解密算法库，默认实现了xor和aes-cbc两种简单算法，也可以使用自己的加解密库。
        classpath("com.github.megatronking.stringfog:xor:5.0.0")
        classpath("com.github.qq549631030:android-junk-code:1.3.2")
    }
}
