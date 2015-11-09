# METEORIC
Game for Android devices, powered by libGDX

https://play.google.com/store/apps/details?id=guilledelacruz.meteoric.android

Para probar la versión de la aplicación tanto de Android como de escritorio es necesario realizar varios pasos para configurar el proyecto correctamente.

* Android:

- En el archivo local.properties de la raíz del proyecto, es necesario cambiar la ruta donde se haya tu Android SDK.
- En el subdirectorio de Android, en el archivo AndroidLauncher.java existe una variable String con un ID para la publicidad de AdView; dicha ID es de prueba. Si deseas conseguir una, debes registrarte en AdMob. 
- Para incluir la librería de AdMob, en el SDK Manager de Android debes haber descargado Google Play Services dentro del paquete Extras. Al hacer esto se descargará el proyecto de AdMob en el directorio del SDK Manager. Lo siguiente es incluir como librería dicho proyecto. La ruta es la siguiente:

<android-sdk>/extras/google/google_play_services/libproject/google-play-services_lib/

* Escritorio:

- Para lanzar correctamente este subproyecto, debes editar un apartado de la configuración de lanzamiento del programa, una vez creada, concretamente el 'Working directory'. Esto es necesario para que libGDX busque los recursos (imagenes, música, etc) en el lugar correcto, en caso de no hacerlo, obtendras excepciones. Debes indicar la ruta de la carpeta 'assets' del subproyecto de Android.