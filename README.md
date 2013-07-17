# Velib N' Roses
An Android application that makes Velib'Toulouse even more fabulous

# Importing the project into Android Studio

* Install SDK targets 8 and 17, Google Play Services and Android Support Library.

* Create a file `local.properties` in the folder `src/VelibNRoses`:

<pre>
		sdk.dir=&lt;android_sdk>
</pre>

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
