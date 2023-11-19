plugins {
    id("metro-app-library-convention")
    id("kotlin-parcelize")
}

android {
    namespace = "com.louis993546.metro.demo.wordle"
    // K2 lint crash
    lint {
        disable.add("MutableCollectionMutableState")
        disable.add("AutoboxingStateCreation")
    }
}