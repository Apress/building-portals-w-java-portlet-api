Building Portals with the Java Portlet API
by Jeff Linwood and Dave Minter

http://www.portalbook.com

Published by Apress



Release Notes
==========================================

Release History 

1.0 July 23, 2004 initial release


Installation and Build Instructions
==========================================
Each chapter has an Apache Ant (http://ant.apache.org) build file to build the chapter's source. You may also use your IDE to build the files.

Set the location of your tomcat shared library (contains portlet-api.jar or similar) in common.properties. Also put in the location of your servlet library (servlet-api.jar).

You may need to modify build.properties and build.xml files for your installation.


Deployment Instructions
==========================================
See Chapter 2 of the book for deployment instructions for Apache Pluto, or your vendor's documentation if you are using another JSR-168 compatible portal. Also see the book web site at http://www.portalbook.com/ for any useful information.



Distributed files with each chapter
==========================================
Download Jakarta Apache project source and binary files from http://jakarta.apache.org/.



Chapter 1
=============
No code.



Chapter 2
=============
No libraries.


Chapter 3
==============
No libraries.


Chapter 4
==============
lib - commons-fileupload-1.0.jar - Jakarta Commons File Upload Library 1.0 -  Apache Software License
src/org/apache - Portlet File Upload classes. - Apache Software License


Chapter 5
==============
todo/web-inf/lib - jstl.jar, standard.jar - Jakarta Apache Standard Tag Library 1.0.4 - Apache Software License
todo/web-inf/tld - c.tld, fmt.tld (also comes with the Apache JSTL 1.0.4) - Apache Software License



Chapter 6
===============
Install XDoclet 1.2 (http://xdoclet.sourceforge.net/), and modify build.properties to point to your installation of Tomcat, and your installation of XDoclet.


Chapter 7
===============
No libraries



Chapter 8
==============
This builds a command-line application. You will need to create an appropriate policy file as described in the text of the chapter and configure a Kerberos server to authenticate against.


Chapter 9
=============
Chapter 9 requires the rss4j library from Churchill Objects (http://www.churchillobjects.com/). You will need to unzip the jar file that you download from this site and update build.xml to point to its location.


Chapter 10
==============
lib - lucene-1.4-rc3.jar, lucene-demos-1.4-rc3.jar. Jakarta Apache Lucene 1.4 RC3 - Apache Software License
lib - taglibs-lucene.jar Lucene Tag Library - http://www.javaxp.net/lucene-taglib/ - Apache Software License


Chapter 11
==============
No libraries.


Chapter 12
==============
No code.

Chapter 13
==============
The source for the YAZD application can be downloaded from http://paperstack.com/yazd in a form suitable for building the code for this chapter, or in its initial from from http://yazd.yasna.com/ 

You will need to install the YAZD application as a stand-alone application with a database before you can run the portlet.

Chapter 14
==============
lib - gnujaxp.jar, jcommon-0.9.2.jar, jfreechart-0.9.17.jar, log4j-1.2.8.jar - libraries for JFreeChart 0.9.17 from http://www.jfree.org/. gnujaxp, jcommon, and jfreechart are LGPL licensed, log4j is apache licensed.


Chapter 15
===============
You will need a WebDAV server to test the portlet. We used the Slide 2.0 server/Tomcat 5 binary distribution. You will need to change the port numbers for Slide's Tomcat so it does not conflict with Pluto's Tomcat. Do that in the Slide Tomcat's conf/server.xml file. Change 8005 to 9005, and 8080 to 9080.

lib - commons-httpclient.jar, commons-logging.jar, jakarta-slide-webdavlib-2.0.jar - Jakarta Apache  Slide Client 2.0 - Apache Software License 2.0
lib - jstl.jar, standard.jar - Jakarta Apache Standard Tag Library 1.0.4 - Apache Software License
web-inf/tld - c.tld, fmt.tld (also comes with the Apache JSTL 1.0.4) - Apache Software License



More Information
===========================================
For more information about portals and portlet development, visit the book's website at http://www.portalbook.com/.






This product includes software developed by the Apache Software Foundation (http://www.apache.org/).