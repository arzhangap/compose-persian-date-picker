# Compose Persian Date Picker
[![fa](https://img.shields.io/badge/lang-farsi-green.svg)](https://github.com/arzhangap/compose-persian-date-picker/blob/master/README.fa.md)  

`Compose Persian Date Picker` is an android library that allows developers to easily add a Jalali Date Picker to their apps.

## Give a Star! ⭐
If you like or are using this project to learn or start your solution, please give it a star. Thanks!

## Features
- Easy integration with Jetpack Compose.
- Lightweight and efficient.
- scrollable
- Material Design v3

## Installation

Here's a quick example of how to use the library:

1. Add the JitPack repository to your project-level build.gradle or settings.gradle file:

for settings.gradle.kts:
```kts
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // add the following
        maven { url =  uri("https://jitpack.io") }
    }
}
```
for settings.gradle:
```groovy
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```

2. Add the library dependency to your app-level build.gradle file:

```kts
    dependencies {
            implementation("com.github.arzhangap:compose-persian-date-picker:1.0.0")
    } 
```

3. Use the Persian Date Picker in your app:
```kotlin
   // remember state for date picker
   val datePickerState = rememberPersianDatePickerState()

   //pass the state to composable function
   // if you wish to customize it futher use provided parameters.
   PersianDatePicker(persianDatePickerState = datePickerState)

  // use toggleDialog() for opening and closing the dialog
  Button(onClick = { datePickerState.toggleDialog()}) {
        Text(text = "Open")
  }
    // access chosen date using chosenDate property of state.
    // in order to get a formatted string use string function on chosenDate.
   // if no date is selected it will show nullMessage.
   Text(text = calenderState.chosenDate.string()
```
