# Velib N' Roses
An Android application that makes Velib'Toulouse even more fabulous

# Importing the project into Android Studio

* Download and install Android Studio
	* Create an environment variable "ANDROID_HOME" to PATH_TO_ANDROID_STUDIO_INSTALL/sdk
	* Add to PATH, ANDROID_HOME/tools and DROID_HOME/platform-tools
* Open Android Manager with <code>android</code> command and install (or check if already installed)
	* Android 4.2.2 (API 17) -> SDK Platform
	* Android 2.2 (API 8) -> SDK Platform
	* Extras
		* Google Play Services
		* Google Repository
		* Android Support Repository
		* Android Support Library
	* Tools
		* Android SDK Tools
		* Android SDK Platform-tools
		* Android SDK Build-tools
* Create a file `api_keys.properties` in the folder `src/VelibNRoses/VelibNRoses/src/main/res/raw` (create the folder if doesn't exist) :

<pre>
	An_API=###
</pre>

* Import the project into Android Studio:
	* Open Android Studio
	* Import Project
	* Select VelibNRoses (src/VelibNRoses)
	* Import project from external model: Gradle
	* Check Use auto-import and select Use gradle wrapper

That's it!

# MIT License
This application is released under the MIT License. See the LICENSE file for more details.
