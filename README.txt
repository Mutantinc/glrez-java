Overview:
---------
This is a port of my good friend Rez' first intro: GLRez.
http://www.pouet.net/prod.php?which=16327
It started in 2005 when Rez stayed at my place for a couple of weeks. I showed him it was possible to do some efficient 3D in Java by porting the code of his first intro.
Done in just 5 or 6 hours, the Java port was extremely close to the original (and therefore had a very linear code). It was using JOGL (Java bindings for OpenGL) and NativeFmod (Java bindings for Fmod).


The code stayed dormant until the original C source was released in may 2012. https://github.com/chiptune/glrez
I felt I wanted to share my part too, thus allowing GlRez to run on virtually any platform, but the code was really too ugly, and it wouldn't even compile anymore. 
So I split the code and made it object oriented. Yes it can be better, fell free. 
I updated JOGL, and moved from NativeFmod to NativeFmodEx to get x64 compatibility.
I also added support for JOAL (Java bindings for OpenAL) and used IBXM to decode the module.
I salted it with a few classes of framework of mine to make it easier to manage events within the intro and coded the startup options dialog.
Eventually I configured the project for Maven, and following Rez' advice, added the necessay to let GLrez run in JavaWebStart mode in addition to the standalone mode.
The result is a 100kB JAR archive.

http://jogamp.org/jogl/www/  
http://jogamp.org/joal/www/
https://sites.google.com/site/mumart/
http://jerome.jouvie.free.fr/nativefmodex/


Build instructions:
-------------------
First, get and install a JDK6 at least.
The project is structured and may be built as a Maven artifact. I don't like Maven but it's rather standard and you won't be lost.
After cloning the souce code from github you may also import the project in Eclipse or Netbeans using the existing Maven configuration (pom.xml).

Though, probably no Maven repository contains the necessary external libs, so you will have to install them yourself.
Also, in JavaWabStart mode, the glrez jar will need to be signed, and all the jars it uses at runtime too. JOGL, JOAL and NativeFmodEx are already signed. IBXM is not.

Using portecle for example, https://portecle.sourceforge.net, create yourself a keystore.
Then generate a key pair into it with the CN=ibxm and alias=ibxm and give it a password
Then generate a key pair into it with the CN=glrez and alias=glrez and give it a password


On http://code.google.com/p/micromod/ get the latest distrib of IBXM (e.g ibxm-a61.jar)
Sign it using: jarsigner -keystore /home/myaccount/keystore.jks -storepass mystorepass -keypass mykeypass /home/mars/ibxm-a61.jar ibxm
 

On http://jogamp.org/deployment/jogamp-current/archive/ get the latest distrib jogamp-all-platforms.7z and extract jogl.all.jar, gluegen-rt.jar and joal.jar 
	mvn install:install-file -Dfile=/home/myaccount/jogl.all.jar -DgroupId=com.jogamp -DartifactId=jogl.all -Dversion=2.0-20120502 -Dpackaging=jar
	mvn install:install-file -Dfile=/home/myaccount/gluegen-rt.jar -DgroupId=com.jogamp -DartifactId=gluegen-rt.all -Dversion=2.0-20120502 -Dpackaging=jar
	mvn install:install-file -Dfile=/home/myaccount/joal.jar -DgroupId=com.jogamp -DartifactId=joal -Dversion=2.0-20120502 -Dpackaging=jar

On http://jerome.jouvie.free.fr/nativefmodex/ get the latest distrib (e.g the distrib NativeFmodEx-1.5.0-jws.rar) and extract NativeFmodEx.jar 
mvn install:install-file -Dfile=/home/myaccount/NativeFmodEx.jar -DgroupId=org.jouvieje -DartifactId=nativefmodex -Dversion=1.5.0 -Dpackaging=jar

And do the same with the ibxm jar (signed or not)
mvn install:install-file -Dfile=/home/myaccount/ibxm-a61.jar -DgroupId=ibxm -DartifactId=ibxm -Dversion=a61-signed -Dpackaging=jar

Then update this project's pom.xml with the keystore location+password, and glrez-aliased keypair password
Build the project: mvn clean package. 


Installation instructions:
--------------------------
For the standalone mode, just make sure the glrez bat & jar sit next to a /lib folder containing all the dependencies (jogl, joal, NativeFmodEx for all the needed platforms, plus the ibxm jar)
On Unix, make sure to set glrez.sh as executable

For JavaWebstart,
Update glrez.jnlp codebase, href, jar href, and ibxm extention to reflect the location you're going to install in.
Update ibxm.jnlp codebase, href, jar href, and ibxm extention to reflect the location you're going to install in.
Upload glrez.jnlp, the glrez jar, ibxm.jnlp and the *signed* ibxm jar into the same remote folder.


Run as a standalone application:
--------------------------------
You need a JRE6 at least to run GLRez: http://www.oracle.com/technetwork/java/javase/downloads/
If using OpenAL under Windows or MacOS, you may also have to install the OpenAL lib: the OpenAL library from http://connect.creativelabs.com/openal/

- As a standalone app, launch:
    On Unix: glrez.sh or java -jar glrez-1.0.jar
    On Windows: blrez.bat or javaw.exe -Dsun.java2d.noddraw=true -jar glrez-1.0.jar

- As a JavaWebStart application, call the aforementioned glrez.jnlp from a browser.

In either case, under Windows, don't forget the VM option -Dsun.java2d.noddraw=true else DirectDraw will mess with OpenGL and the display will remain black in fullscreen.


Keys:
  F1: switch between full screen and windowed mode
  Space: pause/resume
  F: write framerate to the standard output
  Escape: exit  
