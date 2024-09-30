Languages

[![fa](https://img.shields.io/badge/lang-farsi-green.svg)](https://github.com/arzhangap/compose-persian-date-picker/blob/master/README.fa.md)  
# Compose Persian Date Picker

`Compose Persian Date Picker` is an android library that allows developers to easily add a Jalali Date Picker to their apps.

<img src="https://github.com/user-attachments/assets/e7fcb14b-a40b-46b5-98cb-69921f16967e" alt="Alt Text" width="350" />

## List of Content
1. [Features](#features)
2. [Installation](#installation)
3. [Usage](#usage)
4. [Customization](#customization)
5. [Contribution](#contribution)
6. [Support](#support)
7. [License](#license)

---
## Features
- Easy integration with Jetpack Compose.
- scrollable
- Lightweight and efficient.
- Material Design 3

## Installation

Here's a quick example of how to use the library:

1. Add the JitPack repository to your project-level build.gradle or settings.gradle file:

settings.gradle.kts:
```kts
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // add the following:
        maven { url =  uri("https://jitpack.io") }
    }
}
```

2. Add the library dependency to your app-level build.gradle file:

```kts
    dependencies {
            implementation("com.github.arzhangap:compose-persian-date-picker:1.0.0")
    } 
```

## Usage
   // remember state
   val datePickerState = rememberPersianDatePickerState()

   // Consume the state to the composable
   PersianDatePicker(persianDatePickerState = datePickerState)

  // Open/Close Dialog using toggleDialog()
  Button(onClick = { datePickerState.toggleDialog()}) {
        Text(text = "Open")
  }
  
    // you can get chosenDate and formatted string of date using chosenDate and string() function
    // chosenDate is nullable, you can set default string for null chosenDate as a parameter to string("message") function.
   Text(text = calenderState.chosenDate.string())


## Customization
Customize DatePicker using its parameters.
``` kotlin
    PersianDatePicker(
    ...
    dialogColor = MaterialTheme.colorScheme.surfaceContainer,
    headerBackgroundColor = Color.Unspecified,
    textColor = MaterialTheme.colorScheme.onSurface,
    dayIconColor = Color.Transparent,
    selectedDayIconColor = MaterialTheme.colorScheme.primary,
    textColorHighlight = Color.Unspecified,
    actionBtnTextColor = MaterialTheme.colorScheme.primary,
    dayOfWeekLabelColor = MaterialTheme.colorScheme.primary,
    fontFamily = FontFamily.Default,
    actionBtnFontSize = 16.sp
    )
```

## Contributation
Contributions are welcome! To get started:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Submit a pull request with a clear description of your changes.

Please ensure your code follows our code style.

---

## Support
if you are using my project and like it, you can donate me a coffee or give me a star ⭐.

<a href="https://www.coffeebede.com/arzhangap">
    <img class="img-fluid" src="https://coffeebede.ir/DashboardTemplateV2/app-assets/images/banner/default-yellow.svg" width="300" />
</a>

## License

```
Copyright 2024 arzhangap (Arzhang Alvandi Pour)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

