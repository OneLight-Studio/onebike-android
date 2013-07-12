# Velib N' Roses
An Android application that makes Velib'Toulouse even more fabulous

# MIT License
This application is released under the MIT License. See the LICENSE file for more details.

# Importing the project into Android Studio

* Install SDK targets 8 and 17, Google Play Services and Android Support Library.

* Copy the Google Play library

 `<android_sdk>/extras/google/google_play_services/libproject/google-play-services_lib`

 to the project directory

 `src/VelibNRoses`

* Create a file `build.gradle` in the `google-play-services_lib` folder:

<pre>
		buildscript {
			repositories {
				mavenCentral()
			}
			dependencies {
				classpath 'com.android.tools.build:gradle:0.5.+'
			}
		}
		apply plugin: 'android-library'

		dependencies {
			compile fileTree(dir: 'libs', include: '*.jar')
		}

		android {
			compileSdkVersion 8
			buildToolsVersion "17.0.0"

			sourceSets {
				main {
					manifest.srcFile 'AndroidManifest.xml'
					java.srcDirs = ['src']
					resources.srcDirs = ['src']
					aidl.srcDirs = ['src']
					renderscript.srcDirs = ['src']
					res.srcDirs = ['res']
					assets.srcDirs = ['assets']
				}

				instrumentTest.setRoot('tests')
			}
		}
</pre>

* Create a file `local.properties`:

<pre>
		sdk.dir=&lt;android_sdk>
</pre>

* Import the project into Android Studio:
	* Open Android Studio
	* Import Project
	* Select VelibNRoses (top-level)
	* Import project from external model: Gradle
	* Check Use auto-import and select Use gradle wrapper

That's it!

