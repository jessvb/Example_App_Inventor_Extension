# MIT App Inventor Extension Source Code
This repo contains the source code for an example App Inventor Extension that enables App Inventor to connect with Amazon Alexa skills. 

## Generating a .aix File for MIT App Inventor
To generate a .aix file to import into App Inventor:
1. Place the .java source code in `appinventor-sources\appinventor\components\src\com\google\appinventor\components\runtime` within the [MIT App Inventor Sources](https://github.com/mit-cml/appinventor-sources).
2. cd into `appinventor-sources\appinventor`, and run `ant extensions`. This generates the .aix file.
3. Find the .aix file in `appinventor-sources\appinventor\components\build\extensions`.
4. Upload the .aix file to [MIT App Inventor](http://appinventor.mit.edu/explore/)
5. The extension should appear underneath "Extension" in the Palette. Drag the extension into the "Viewer". It should now appear underneath "Non-visible components" in the Viewer.
6. Go to the "blocks" page and use the extension block that appears underneath your sceen name to create cool apps!
